package dungeoncrawler.game.object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import dungeoncrawler.game.Map;

public abstract class AObject extends Image
{
	protected Map _map;
	protected int _layer = Map.default_object_layer;
	protected float _prevx;
	protected float _prevy;
	protected float _x;
	protected float _y;
	protected int _nextx;
	protected int _nexty;
	protected boolean _fly = false;
	protected float _zoom = 1;
	
	public AObject(Map map, String filename) throws SlickException
	{
		super(filename);
		_map = map;
		_map.addObject(this);
		_map.addCollision(_x, _y);
	}
	
	public AObject(Map map) throws SlickException
	{
		super(0, 0);
		_map = map;
		_map.addObject(this);
		_map.addCollision(_x, _y);
	}
	
	public void close()
	{
	}
	
	public void delete()
	{
		_map.removeObject(this);
		_map.removeObjectOnLayer(this, _layer, _nextx, _nexty);
	}
	
	abstract public void update(GameContainer gc, int delta);
	abstract public void drawAt(float x, float y);
	
	public void setLayer(int layer)
	{
		_map.removeObjectOnLayer(this, _layer, (int)_x, (int)_y);
		_layer = layer;
		_map.addObjectOnLayer(this, _layer, (int)_x, (int)_y);
	}
	
	public void setFly(boolean fly)
	{
		if (fly != _fly)
		{
			if (fly)
				_map.removeCollision(_x, _y);
			else	
				_map.addCollision(_x, _y);
		}
		_fly = fly;
	}
	
	public float getX() {return _x; }
	public float getY() {return _y; }
	
	public void setPos(float x, float y)
	{
		_prevx = x;
		_prevy = y;
		setPos(x, y, true);
	}
	
	public void setPos(float x, float y, boolean byPassCollision)
	{
		if (! _fly)
			_map.removeCollision(_x, _y);
		int col = _map.getCollision(x, y);
		if (byPassCollision == false && (col != 0 && ! _fly || 10 <= col))
		{
			if (! _fly)
				_map.addCollision(_x, _y);
			return ;
		}
		_prevx = _x;
		_prevy = _y;
		if ((int)_x != _x && (int)_x + 1 < _map.getWidth())
			_x = (int)_x + 1;
		if ((int)_y != _y && (int)_y + 1 < _map.getHeight())
			_y = (int)_y + 1;
		_map.removeObjectOnLayer(this, _layer, (int)_x, (int)_y);
		_x = x;
		_y = y;
		if ((int)x != x && (int)x + 1 < _map.getWidth())
			x = (int)x + 1;
		if ((int)y != y && (int)y + 1 < _map.getHeight())
			y = (int)y + 1;
		_map.addObjectOnLayer(this, _layer, (int)x, (int)y);
		_nextx = (int)x;
		_nexty = (int)y;
		if (! _fly)
			_map.addCollision(_x, _y);
	}
}
