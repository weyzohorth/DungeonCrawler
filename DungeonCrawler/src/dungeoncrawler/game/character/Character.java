package dungeoncrawler.game.character;

import dungeoncrawler.Data;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.animation.AnimSword;
import dungeoncrawler.game.object.AObject;
import dungeoncrawler.gui.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Character extends AObject
{
	public enum e_dir {DOWN, LEFT, RIGHT, UP};
	private enum e_state {NONE, MOVE, ATTACK};
	static private Font _font;

	protected int _sightDistance;
	protected boolean _alive = true;
	protected boolean _canMove = true;
	protected boolean _canBeCanceledAttack = true;
	private int _currWait = 0;
	private int _waitTime = 10;
	private int _frameW;
	private int _frameH;
	private int _tileW = 32;
	private int _tileH = 32;
	private int _frameNumber;
	private int _currFrame = 0;
	private float _animLoop = 1;
	private float _moveSpeed = 0;
	private float _movement = 0.25f;
	private float _nextSpeed = 0;
	private float _currLoop = 0;
	private int _attackTime = 0;
	private float _attackTimeMax = 30;
	private int _damageTime = 0;
	private int _damageTimeMax = 20;
	private int _damageTaken;
	protected Color _color = new Color(1, 1, 1);
	protected e_dir _dir = e_dir.DOWN;
	private e_dir _nextDir = e_dir.DOWN;
	private e_state _state = e_state.NONE;
	
	protected int _strength = 10;
	protected int _healthMax = 100;
	protected int _health = _healthMax;
	protected float _speed = 2;
	
	public Character(Map map, String filename) throws SlickException
	{
		super(map, filename);
		init(4);
	}
	
	public Character(Map map, String filename, int frameNumber) throws SlickException
	{
		super(map, filename);
		init(frameNumber);
	}
	
	private void init(int frameNumber) throws SlickException
	{
		_frameNumber = frameNumber;
		_frameW = getWidth() / frameNumber;
		_frameH = getHeight() / frameNumber;
		setAnimLoop(1);
		setSightDistance(4);
		_font = new Font(Data.font_path + "jawbrko1.ttf", 15, true, false, Color.red);
	}
	
	public boolean isDead() { return ! _alive; }
	public void setSightDistance(int sight) { _sightDistance = sight; }
	public int getSightDistance() { return _sightDistance; }
	
	public int getCurrentFrame() { return _currFrame; }
	public void setCurrentFrame(int frame) { _currFrame = frame % _frameNumber; }
	
	public e_dir getDir() { return _dir; }
	public void setDir(e_dir dir) { _nextDir = dir; }
	
	public float getSpeed() { return _speed; }
	public void setSpeed(float speed) { _speed = speed;	}
	
	public int getHealth() { return _health; }
	public void addHealth(int health) { _health = (_health + health <= _healthMax ?
											_health + health : _healthMax); }
	
	public int getHealthMax() { return _healthMax; }
	public void setHealthMax(int healthMax) { _healthMax = healthMax; }
	
	public int getStrength() { return _strength; }
	public void setStrength(int strength) { _strength = strength; }
	
	public void setAttackSpeed(float attackSpeed) { _attackTimeMax = 60 / attackSpeed; }
	public float getAttackSpeed() { return 1 / (_attackTimeMax / 60); }
	
	public void setCanMove(boolean canMove) { _canMove = canMove; }
	
	public void setAnimLoop(float animLoop)
	{
		_animLoop = animLoop;
		_movement = 1 / (_frameNumber * _animLoop);
	}
	
	public void setTileSize(int width, int height)
	{
		_tileW = width;
		_tileH = height;
	}
	
	public void attack()
	{
		if (_state != e_state.NONE || 0 < _attackTime ||
				(0 < _damageTime && _canBeCanceledAttack) || ! _canMove)
			return;
		_attackTime = (int)_attackTimeMax;
		_state = e_state.ATTACK;
	}
	
	private void doAttack()
	{
		if (0 < _damageTime)
			return;
		int x = (int)_x;
		int y = (int)_y;
		if (_dir == e_dir.RIGHT)
			x += 1;
		if (_dir == e_dir.UP)
			y -= 1;
		if (_dir == e_dir.LEFT)
			x -= 1;
		if (_dir == e_dir.DOWN)
			y += 1;
		attack(x, y);
	}
	
	public void die()
	{
		delete();
		if (! _fly)
			_map.removeCollision(_x, _y);
		_alive = false;
	}
	
	public void lightDamage(int damage)
	{
		if (0 < _damageTime)
			return ;
		if (_canBeCanceledAttack)
			_attackTime = 0;
		_health -= damage;
		if (_health <= 0)
			die();	
	}
	
	public void damage(int damage)
	{
		if (0 < _damageTime)
			return ;
		lightDamage(damage);
		_damageTime = _damageTimeMax;
		_damageTaken = damage;
	}
	
	public void attack(int x, int y)
	{		
		for (AObject obj : _map.getObjects(x, y))
			if (obj instanceof Character)
				((Character)obj).damage(_strength);
		try
		{
			AnimSword anim = new AnimSword(_map, _dir);
			anim.setPos(x, y);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	private void updateSpeed()
	{
		_moveSpeed = _nextSpeed;
		_waitTime = (int) (_tileW / _moveSpeed);
		_state = e_state.MOVE;
	}
	
	public void step(e_dir dir, float speed)
	{
		setDir(dir);
		_nextSpeed = speed;
	}
	
	public void move()
	{
		if (_moveSpeed == 0)
			_state = e_state.NONE;
		if (_moveSpeed == 0 || ++_currWait < _waitTime || ! _canMove)
			return ;
		_state = e_state.MOVE;
		switch (_dir)
		{
		case DOWN:
			setPos(_x, _y + _movement, false);
			break;
		case UP:
			setPos(_x, _y - _movement, false);
			break;
		case LEFT:
			setPos(_x - _movement, _y, false);
			break;
		case RIGHT:
			setPos(_x + _movement, _y, false);
			break;
		}
		_currLoop += 0.25f;
		_currWait = 0;
		_currFrame = (_currFrame + 1) % _frameNumber;
		if (_currFrame != 0 || _currLoop < _animLoop)
			return;
		_currLoop = 0;
		_nextSpeed = 0;
		_moveSpeed = 0;
		_nextSpeed = 0;
		_state = e_state.NONE;
		float x = _x;
		float y = _y;
		if (_x != (int)_x)
			x = Math.round(_x);
		if (_y != (int)_y)
			y = Math.round(_y);
		setPos(x, y);
	}
	
	public void update(GameContainer gc, int delta)
	{
		//_state = (_currFrame == 0 && _moveSpeed == 0 ? e_state.NONE : e_state.MOVE);
		if (0 < _damageTime)
			_damageTime--;
		switch (_state)
		{
		case MOVE:
			move();
			break;
		case ATTACK:
			_attackTime--;
			if (_attackTime <= 0)
			{
				doAttack();
				_state = e_state.NONE;
			}
			break;
		default:
			_dir = _nextDir;
			updateSpeed();
		}
	}
	
	public void drawAt(float x, float y)
	{
		drawFrameAt(_dir, _currFrame, x - ((_frameW - _tileW) / 2) / (float)_tileW,
				y - ((_frameH - _tileH)) / (float)_tileH);
	}
	
	public void drawFrameAt(e_dir dir, int frame, float x, float y)
	{
		float tw = _tileW * _zoom;
		float th = _tileH * _zoom;
		
		if (_state == e_state.ATTACK)
		{
			float ratio = (float)_attackTime / _attackTimeMax;
			_color.r = ratio;
			_color.g = ratio;
			_color.b = 1;
		}
		else if (0 < _damageTime)
		{
			float ratio = (float)_damageTime / _damageTimeMax;
			_color.r = 1;
			_color.g = ratio;
			_color.b = ratio;
		}
		else
		{
			_color.r = 1;
			_color.g = 1;
			_color.b = 1;
		}
			draw(x * tw, y * th, x * tw + _frameW * _zoom, y * th + _frameH * _zoom,
					frame * _frameW, dir.ordinal() * _frameH,
					(frame + 1) * _frameW, (dir.ordinal() + 1) * _frameH,
					_color);
		if (0 < _damageTime)
			_font.drawText(Integer.toString(_damageTaken),
					(int)(x * tw), (int)((y + 1) * th) - _damageTimeMax + _damageTime);
	}
}
