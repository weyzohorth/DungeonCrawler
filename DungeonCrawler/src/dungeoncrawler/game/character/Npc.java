package dungeoncrawler.game.character;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.Utils;
import dungeoncrawler.game.Map;


public class Npc extends Character
{
	protected enum e_type
	{
		MOB(1), ELEMENTAL(2), BOSS(4);
		
		private int _id;
		private e_type(int id) { _id = id; }
		public int getId() { return _id; }
	}
	protected Sound _howl = null;
	protected int _score;
	protected e_type _type = e_type.MOB;
	protected Player _player;
	protected boolean _chasePlayer = false;
	private int _sqSightDistance;
	private int _distances[][];
	private int _path[][];
	
	public Npc(Map map, String filename) throws SlickException
	{
		super(map, filename);
		_player = _map.getPlayer();
	}
	
	public Npc(Map map, String filename, int frameNumber) throws SlickException
	{
		super(map, filename, frameNumber);
		_player = _map.getPlayer();
	}
	
	protected void calcScore()
	{
		_score = _health / 10 * _strength * 5 * (int)_speed * 2 * (int)getAttackSpeed();
	}
	
	protected void updateStats()
	{
		_speed += (Data.sum.difficulty - 1) * 0.2f * _type.getId() +
				(Data.sum.finalLevel - 1)  / 100  * 2 * Data.sum.difficulty * _type.getId();
		_health += (Data.sum.difficulty - 1) * 10 * _type.getId() +
				(Data.sum.finalLevel - 1) / Data.changeWorld * 8 * Data.sum.difficulty * _type.getId();
		_strength += (Data.sum.difficulty - 1) * 2 * _type.getId() +
				(Data.sum.finalLevel - 1) / Data.changeWorld  * 2 * Data.sum.difficulty * _type.getId();
		setAttackSpeed((Data.sum.difficulty - 1) * 0.2f * _type.getId() +
				getAttackSpeed() + (Data.sum.finalLevel - 1)  / (Data.changeWorld * 8) * Data.sum.difficulty * _type.getId());
		calcScore();
	}
	
	@Override
	public void update(GameContainer gc, int delta)
	{
		action();
		super.update(gc, delta);
	}
	
	@Override
	public void setSightDistance(int sight)
	{
		super.setSightDistance(sight);
		_sqSightDistance = Utils.sqpw(_sightDistance);
		_distances = new int[_sightDistance * 2 + 1][_sightDistance * 2 + 1];
		_path = new int[_sightDistance * 2 + 1][_sightDistance * 2 + 1];
	}
	
	@Override
	public void die()
	{
		super.die();
		if (_howl != null)
			SoundManager.playSeveral(_howl);
		Data.game.addScore(_score);
		switch (_type)
		{
		case MOB:
			Data.sum.scoreByKilledMobs += _score;
			Data.sum.killedMobs++;
			break;
		case ELEMENTAL:
			Data.sum.scoreByKilledElementals += _score;
			Data.sum.killedElementals++;
			break;
		case BOSS:
			Data.sum.scoreByKilledBosses += _score;
			Data.sum.killedBosses++;
			break;
		}
	}
	
	private void action()
	{
		Player player = _map.getPlayer();
		float distanceFromPlayer = Utils.sqdist(player.getX(), player.getY(), _x, _y);
		if (distanceFromPlayer != 0 && distanceFromPlayer <= 1.5)
		{
			if (_howl != null && (int)(Math.random() * 100) <= 0)
				SoundManager.play(_howl);
			_chasePlayer = true;
			e_dir dir = getDirectionToPlayer();
			if (_dir == dir)
				attack();
			else
				setDir(dir);
		}
		else if (_sqSightDistance <= distanceFromPlayer)
		{
			_chasePlayer = false;
			step(e_dir.values()[(int)(Math.random() * 4)], _speed);
		}
		else
		{
			if (_howl != null && ((int)(Math.random() * 100) <= 1 + (! _chasePlayer ? 9 : 0)))
				SoundManager.play(_howl);
			_chasePlayer = true;
			findPath();
		}
	}
	
	private void initFindPath()
	{
		for (int i = 0; i < _sightDistance * 2 + 1; ++i)
			for (int j = 0; j < _sightDistance * 2 + 1; ++j)
			{
				_distances[i][j] = -1;
				_path[i][j] = -1;
			}
		_distances[_sightDistance][_sightDistance] = 0;
	}
	
	private void findPath()
	{
		initFindPath();
		List<int[]> current = new LinkedList<int[]>();
		current.add(new int[]{0, 0});
		
		int x, y, ix, iy, fx, fy;
		double angle;
		int dist = 0;
		while (! current.isEmpty())
		{
			dist++;
			List<int[]> tmp = new LinkedList<int[]>();
			for (int[] coord : current)
			{
				x = (int)_x + coord[0];
				y = (int)_y + coord[1];
				for (int dir = 0; dir < 4; ++dir)
				{
					angle = Math.toRadians(dir * 90);
					ix = (int)Math.cos(angle);
					iy = -(int)Math.sin(angle);
					if (coord[0] + ix < -_sightDistance || _sightDistance < coord[0] + ix ||
							coord[1] + iy < -_sightDistance || _sightDistance < coord[1] + iy)
						continue;
					if (x + ix != (int)_player.getX() || y + iy != (int)_player.getY())
						if(_map.getCollision(x + ix, y + iy) != 0 ||
								_distances[coord[0] + ix + _sightDistance]
										[coord[1] + iy + _sightDistance] != -1)
						continue;
					tmp.add(new int[]{coord[0] + ix, coord[1] + iy});
					fx = coord[0] + _sightDistance;
					fy = coord[1] + _sightDistance;
					_distances[fx + ix][fy + iy] = dist;
					_path[fx + ix][fy + iy] = (_path[fx][fy] == -1 ? dir : _path[fx][fy]);
					if (x + ix == (int)_player.getX() && y + iy == (int)_player.getY())
					{
						setDirByFindPath();
						return;
					}
				}
			}
			current = tmp;
		}
		setDirByFindPath();
	}
	
	private void setDirByFindPath()
	{
		int dir = _path[_sightDistance + (int)_player.getX() - (int)_x]
				[_sightDistance + (int)_player.getY() - (int)_y];
		if (dir == -1)
		{
			step(e_dir.values()[(int)(Math.random() * 4)], _speed);
			return;
		}
		e_dir dirs[] = {e_dir.RIGHT, e_dir.UP, e_dir.LEFT, e_dir.DOWN};
		step(dirs[dir], _speed);
	}
	
	private e_dir getDirectionToPlayer()
	{
		float difX = _player.getX() - _x;
		float difY = _player.getY() - _y;
		
		if (Math.abs(difX) < Math.abs(difY) && difX != 0 || difY == 0)
			return (difX < 0 ? e_dir.LEFT : e_dir.RIGHT);
		return (difY < 0 ? e_dir.UP: e_dir.DOWN);
	}
}
