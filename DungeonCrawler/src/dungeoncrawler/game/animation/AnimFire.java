package dungeoncrawler.game.animation;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.character.Character;
import dungeoncrawler.game.character.Character.e_dir;
import dungeoncrawler.game.object.AObject;

public class AnimFire extends Animation
{
	int _waitTime = 10;
	int _currWait = 0;
	float _speed = 0.5f;
	Character _parent;
	e_dir _dir;
	
	public AnimFire(Map map, Character parent) throws SlickException
	{
		super(map, Data.animation_path + "Fire1.png", 5, 2);
		_parent = parent;
		setLayer(1);
		if (_map.getPlayer().getX() - 1 <= _parent.getX() &&
				_parent.getX() <= _map.getPlayer().getX() + 1)
			_dir = (_map.getPlayer().getY() < _parent.getY() ? e_dir.UP : e_dir.DOWN);
		else if (_map.getPlayer().getY() - 1 <= _parent.getY() &&
				_parent.getY() <= _map.getPlayer().getY() + 1)
			_dir = (_map.getPlayer().getX() < _parent.getX() ? e_dir.LEFT : e_dir.RIGHT);
		else
			_dir = e_dir.values()[(int)(Math.random() * e_dir.values().length)];
		if (_dir == e_dir.UP)
			setRotation(180);
		if (_dir == e_dir.LEFT)
			setRotation(90);
		if (_dir == e_dir.RIGHT)
			setRotation(-90);
		setFrames(new int[]{0, 1, 2, 3, 4, 5});
		SoundManager.playSeveral(SoundManager.fire1);
	}

	@Override
	public void update(GameContainer gc, int delta)
	{
		if (_currWait++ < _waitTime)
			return ;
		_currWait = 0;
		List<AObject> objs = new LinkedList<AObject>();
		for (int x = -1; x <= 1; ++x)
			for (int y = -1; y <= 1; ++y)
			{
				List<AObject> tmp = _map.getObjects((int)_x + x, (int)_y + y);
				if (tmp != null)
					objs.addAll(tmp);
			}
		int oldFrame = getCurrentFrame();
		int i = 0;
		while (i < objs.size())
		{
			if (objs.get(i) == _parent ||
					objs.get(i) instanceof AnimFire)
				objs.remove(i);
			else
				i++;
		}
		if (! objs.isEmpty() ||
				_map.getTile(0, Math.round(_x), Math.round(_y)) != 0 || 1 < oldFrame)
		{
			if (oldFrame == 1)
			{
				SoundManager.playSeveral(SoundManager.fire2);
				next();
			}
			for (AObject obj : objs)
				if (obj != _parent && obj instanceof Character)
					((Character)obj).lightDamage(_parent.getStrength() / 5);
		}
		else
		{
			if (oldFrame <= 1)
				switch(_dir)
				{
				case RIGHT:
					setPos(_x + _speed, _y);
					break;
				case LEFT:
					setPos(_x - _speed, _y);
					break;
				case DOWN:
					setPos(_x, _y + _speed);
					break;
				case UP:
					setPos(_x, _y - _speed);
					break;
				}
		}
		if (isEnded())
			delete();
		else if (oldFrame != 1)
			next();
	}
}
