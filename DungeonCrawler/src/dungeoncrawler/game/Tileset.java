package dungeoncrawler.game;

import java.io.FileReader;
import java.io.BufferedReader;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Tileset extends Image
{
	private int _tileW;
	private int _tileH;
	private boolean[][] _collisions;
	private float _zoom = 1;
	
	public Tileset(String name) throws SlickException
	{
		super(name);
		init(name, 32, 32);
	}
	
	public Tileset(String name, int tileW, int tileH) throws SlickException
	{
		super(name);
		init(name, tileW, tileH);
	}
	
	private void init(String name, int tileW, int tileH)
	{
		_tileW = tileW;
		_tileH = tileH;
		_collisions = new boolean[getWidth() / _tileW][getHeight() / _tileH];
		try
		{
			BufferedReader file = new BufferedReader(new FileReader(name.substring(0, name.lastIndexOf(".")) + ".tst"));
			String line = file.readLine();
			int y = 0;
			while (line != null && y < _collisions[0].length)
			{
				for (int x = 0; x < line.length(); ++x)
				{
					if (_collisions.length <= x)
						break;
					_collisions[x][y] = (line.charAt(x) == '1');
				}
				line = file.readLine();
				y++;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			for (int x = 0; x < _collisions.length; ++x)
				for (int y = 0; y < _collisions[0].length; ++y)
					_collisions[x][y] = false;
		}
	}
	
	public void setZoom(float zoom) { _zoom = zoom; }
	public float getZoom() { return _zoom; }
	public int getTileWidth() { return _tileW; }
	public int getTileHeight() { return _tileH; }
	public int getTileByRow() { return _collisions.length; }
	public int getTileByColumn() { return _collisions[0].length; }
	
	public boolean getCollision(int x, int y)
	{
		return _collisions[x][y];
	}
	
	public void drawTile(int tx, int ty, float x, float y)
	{
		draw(x, y, x + _tileW * _zoom, y + _tileH * _zoom,
				tx * _tileW, ty * _tileH, (tx + 1) * _tileW, (ty + 1) * _tileH);
	}
	
	public void drawTile(int tid, float x, float y)
	{
		drawTile(tid % (getWidth() / _tileW), tid / (getWidth() / _tileW), x, y);
	}
	
	public void drawTile(int tx, int ty, int x, int y, int width, int height)
	{
		for (int ix = 0; ix < width; ix++)
			for (int iy = 0; iy < height; iy++)
				drawTile(tx, ty, x + ix * _tileW * _zoom, y + iy * _tileH * _zoom);
	}
	
	public void drawTile(int tid, int x, int y, int width, int height)
	{
		for (int ix = 0; ix < width; ix++)
			for (int iy = 0; iy < height; iy++)
				drawTile(tid, x + ix * _tileW * _zoom, y + iy * _tileH * _zoom);
	}
	
	public void drawTiles(int tx, int ty, int width, int height, int x, int y)
	{
		for (int ix = 0; ix < width; ix++)
			for (int iy = 0; iy < height; iy++)
				drawTile(tx + ix, ty + iy, x + ix * _tileW * _zoom, y + iy * _tileH * _zoom);
	}
	
	public void drawTiles(int tx, int ty, int tw, int th, int x, int y, int sw, int sh)
	{
		for (int ix = 0; ix < sw; ix++)
			for (int iy = 0; iy < sh; iy++)
				drawTile(tx + ix % tw, ty + iy % th,
						x + ix * _tileW * _zoom, y + iy * _tileH * _zoom);
	}
}
