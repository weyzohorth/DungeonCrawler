package dungeoncrawler.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.game.object.AObject;
import dungeoncrawler.gui.IWinObject;

public class Camera extends IWinObject
{
	private Map _map;
	private AObject _target;
	private float _x = 0;
	private float _y = 0;
	private int _width = Data.screen_width;
	private int _height = Data.screen_height;
	private float _rowSize;
	private float _columnSize;
	private int _rowHalfSize;
	private int _columnHalfSize;
	private float _tileW;
	private float _tileH;
	private float _zoom = 0;
	
	public Camera()
	{	
	}
	
	public Camera(Map map)
	{
		_map = map;
	}
	
	public void setTarget(AObject target) { _target = target; }
	public void setMap(Map map) { _map = map; }
	public void setPos(float x, float y)
	{
		_x = x;
		_y = y;
	}
	public float[] getPos() { return new float[]{_x, _y}; }
	
	public void update(GameContainer gc, int delta)
	{
		if (_zoom != _map.getZoom())
		{
			_zoom = _map.getZoom();
			_tileW = _zoom * _map.getTileWidth();
			_tileH = _zoom * _map.getTileHeight();
			_rowSize = _width / _tileW;
			_columnSize = _height / _tileH;
			_rowHalfSize = (int)(_rowSize / 2);
			_columnHalfSize = (int)(_columnSize / 2);
		}
		if (_target == null)
			return;
		_x = _target.getX();
		_y = _target.getY();
		
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		
		float x = _x * _tileW;
		float y = _y * _tileH;
		for (int i = 0; i < _map.getNbLayers(); ++i)
			for (int iy = -1; iy < _columnSize + 1; ++iy)
			{
				for (int ix = -1; ix < _rowSize + 1; ++ix)
				{
					float fog = _map.getFog((int)_x + ix - _rowHalfSize, (int)_y + iy - _columnHalfSize); 
					if (fog == 1)
						continue;
				
					int tid = _map.getTile(i, (int)_x + ix - _rowHalfSize,
							(int)_y + iy - _columnHalfSize);
					if (tid != -1)
						_map.getTileset().drawTile(tid, ix * _tileW - x % _tileW,
								iy * _tileH - y % _tileH);
					if (_map.getSight((int)_x + ix - _rowHalfSize, (int)_y + iy - _columnHalfSize) &&
							_map.getObjects(i, (int)_x + ix - _rowHalfSize,
							(int)_y + iy - _columnHalfSize) != null)
						for (AObject obj : _map.getObjects(i, (int)_x + ix - _rowHalfSize,
								(int)_y + iy - _columnHalfSize))
							obj.drawAt(obj.getX() - _x + _rowHalfSize,
									obj.getY() - _y + _columnHalfSize);
					g.setColor(new Color(0, 0, 0, fog));
					g.fillRect(ix * _tileW - x % _tileW, iy * _tileH - y % _tileH, _tileW, _tileH);
				}
			}
	}
}
