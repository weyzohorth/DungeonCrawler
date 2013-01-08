package dungeoncrawler.game;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.game.animation.Animation;
import dungeoncrawler.game.character.Bat;
import dungeoncrawler.game.character.Batracian;
import dungeoncrawler.game.character.BossEarth;
import dungeoncrawler.game.character.BossFire;
import dungeoncrawler.game.character.BossIce;
import dungeoncrawler.game.character.BossWater;
import dungeoncrawler.game.character.Character.e_dir;
import dungeoncrawler.game.character.Crawler;
import dungeoncrawler.game.character.ElementalEarth;
import dungeoncrawler.game.character.ElementalFire;
import dungeoncrawler.game.character.ElementalIce;
import dungeoncrawler.game.character.ElementalWater;
import dungeoncrawler.game.character.FireDog;
import dungeoncrawler.game.character.Goblin;
import dungeoncrawler.game.character.LittleDevil;
import dungeoncrawler.game.character.LivingWater;
import dungeoncrawler.game.character.Lizard;
import dungeoncrawler.game.character.Medusa;
import dungeoncrawler.game.character.Player;
import dungeoncrawler.game.character.Scorpion;
import dungeoncrawler.game.character.Skeleton;
import dungeoncrawler.game.character.Zombie;
import dungeoncrawler.game.object.AObject;

public class Map
{
	static public final int default_object_layer = 1;
	static public final int tileIdDecs[] = new int[]{5, 13};
	static public final int tileIdDecCols[] = new int[]{1, 2, 7, 9, 10};
	static public int top_layer;
	private Tileset _tileset;
	int _exit[];
	List<Class<?>> _mobs = new LinkedList<Class<?>>();
	List<int[][]> _tiles;
	List<List<AObject>[][]> _layerObject;
	List<AObject> _objects = new LinkedList<AObject>();
	List<AObject> _objToDel = new LinkedList<AObject>();
	int[][] _collisions;
	float[][] _fog;
	boolean[][] _sight;
	boolean _boss = false;
	Player _player;
	
	public Map(int world, int width, int height) throws SlickException
	{
		_boss = (Data.sum.finalLevel != 0 && Data.sum.finalLevel % Data.changeWorld == 0);
		_tileset = new Tileset(Data.tileset_path + "Cave0" + Integer.toString(world) + ".png");
		_exit = new int[]{0, 0};
		switch (world)
		{
		case 1:
			getEarthMobs(); break;
		case 2:
			getWaterMobs(); break;
		case 3:
			getIceMobs(); break;
		case 4:
			getFireMobs(); break;
		}
		try
		{
			new MapGenerator(this, width, height);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Animation.tileW = _tileset.getTileWidth();
		Animation.tileH = _tileset.getTileHeight();
	}
	
	public void close()
	{
		for (AObject obj : _objects)
			obj.close();
	}
	
	private void getEarthMobs()
	{
		_mobs.add(Zombie.class);
		_mobs.add(Bat.class);
		_mobs.add(Goblin.class);
		_mobs.add(ElementalEarth.class);
		_mobs.add(BossEarth.class);
	}
	
	private void getWaterMobs()
	{
		_mobs.add(Batracian.class);
		_mobs.add(Lizard.class);
		_mobs.add(LivingWater.class);
		_mobs.add(ElementalWater.class);
		_mobs.add(BossWater.class);
	}
	
	private void getIceMobs()
	{
		_mobs.add(Skeleton.class);
		_mobs.add(Medusa.class);
		_mobs.add(LittleDevil.class);
		_mobs.add(ElementalIce.class);
		_mobs.add(BossIce.class);
	}
	
	private void getFireMobs()
	{
		_mobs.add(Crawler.class);
		_mobs.add(Scorpion.class);
		_mobs.add(FireDog.class);
		_mobs.add(ElementalFire.class);
		_mobs.add(BossFire.class);
	}
	
	@SuppressWarnings("unchecked")
	public void init(int width, int height)
	{
		_tiles = new LinkedList<int[][]>();
		_tiles.add(new int[width][height]);
		_tiles.add(new int[width][height]);
		_tiles.add(new int[width][height]);
		_tiles.add(new int[width][height]);
		top_layer = _tiles.size() - 1;
		_layerObject = new LinkedList<List<AObject>[][]>();
		for (int i = 0; i <= top_layer; ++i)
			_layerObject.add(new LinkedList[width][height]);
		_fog = new float[width][height];
		_sight = new boolean[width][height];
		_collisions = new int[width][height];
		
		for (int x = 0; x < _fog.length; x++)
			for (int y = 0; y < _fog[0].length; y++)
			{
				for (int i = 0; i <= top_layer; ++i)
				{
					_tiles.get(i)[x][y] = (i == 0 ? 0 : -1);
					_layerObject.get(i)[x][y] = new LinkedList<AObject>();
				}
				_fog[x][y] = 1;
				_sight[x][y] = false;
				_collisions[x][y] = 0;
			}
	}
	
	public void				setPlayer(Player player)	{ _player = player; }
	public Player			getPlayer()		{ return _player; }
	
	public boolean			thereIsABoss()	{ return _boss; }
	public List<Class<?>>		getMobs()		{ return _mobs; }
	public float			getTileWidth()	{ return _tileset.getTileWidth(); }
	public float			getTileHeight()	{ return _tileset.getTileHeight(); }
	public float			getZoom()		{ return _tileset.getZoom(); }
	public int				getWidth()		{ return _fog.length; }
	public int				getHeight()		{ return _fog[0].length; }
	public Tileset			getTileset()	{ return _tileset; }
	public List<AObject>	getObjects()	{ return _objects; }
	public int				getNbLayers()	{ return _tiles.size(); }
	
	public int getTile(int layer, int x, int y)
	{
		if (x < 0 || _tiles.get(0).length <= x ||
				y < 0 || _tiles.get(0)[0].length <= y ||
				layer < 0 || _tiles.size() <= layer)
			return -1;
		return _tiles.get(layer)[x][y];
	}
	
	public boolean canSpawn(int x, int y)
	{
		return (getTile(0, x, y) != 0 || getTile(1, x, y) != -1 ||
				! getObjects(x, y).isEmpty() ? false : true);
	}
	
	public void setExit(int x, int y)
	{
		_exit[0] = x;
		_exit[1] = y;
	}
	
	public void unsetTile(int layer, int x, int y)
	{
		setTile(-1, -1, layer, x, y);
	}
	
	public void setTile(int tid, int layer, int x, int y)
	{
		setTile(tid % _tileset.getTileByRow(), tid / _tileset.getTileByRow(), layer, x, y);
	}
	
	public void setTile(int tx, int ty, int layer, int x, int y)
	{
		if (x < 0 || _tiles.get(0).length <= x ||
				y < 0 || _tiles.get(0)[0].length <= y ||
				layer < 0 || _tiles.size() <= layer ||
				tx < 0 || ty < 0)
		{
			_tiles.get(layer)[x][y] = -1;
			return;
		}
		_collisions[x][y] += (_tileset.getCollision(tx, ty) ? 1 : 0);
		_tiles.get(layer)[x][y] = tx + ty * _tileset.getTileByRow();
	}
	
	public float getFog(int x, int y)
	{
		if (x < 0 || _fog.length <= x ||
				y < 0 || _fog[0].length <= y)
			return 1;
		return _fog[x][y];
	}
	
	public void setFog(float fog, int x, int y)
	{
		if (x < 0 || _fog.length <= x ||
				y < 0 || _fog[0].length <= y)
			return;
		_fog[x][y] = fog;
	}
	
	public boolean getSight(int x, int y)
	{
		if (x < 0 || _sight.length <= x ||
				y < 0 || _sight[0].length <= y)
			return false;
		return _sight[x][y];
	}
	
	public void setSight(boolean sight, int x, int y)
	{
		if (x < 0 || _sight.length <= x ||
				y < 0 || _sight[0].length <= y)
			return;
		_sight[x][y] = sight;
	}
	
	public void addCollision(float x, float y)
	{
		int addX = 0;
		int addY = 0;
		if ((int)x != x)
			addX = 1;
		if ((int)y != y)
			addY = 1;
		addCollision((int)x, (int)y);
		if (addX == 1|| addY == 1)
			addCollision((int)x + addX, (int)y + addY);
	}
	
	public boolean addCollision(int x, int y)
	{
		if (x < 0 || _collisions.length <= x ||
				y < 0 || _collisions[0].length <= y)
			return false;
		_collisions[x][y]++;
		return true;
	}
	
	public void removeCollision(float x, float y)
	{
		int addX = 0;
		int addY = 0;
		if ((int)x != x)
			addX = 1;
		if ((int)y != y)
			addY = 1;
		removeCollision((int)x, (int)y);
		if (addX == 1|| addY == 1)
			removeCollision((int)x + addX, (int)y + addY);
	}
	
	public boolean removeCollision(int x, int y)
	{
		if (x < 0 || _collisions.length <= x ||
				y < 0 || _collisions[0].length <= y)
			return false;
		if (0 < _collisions[x][y])
			_collisions[x][y]--;
		return true;
	}
	
	public int getCollision(float x, float y)
	{
		int addX = 0;
		int addY = 0;
		if ((int)x != x)
		{
			if (x < (int)x)
				x--;
			addX = 1;
		}
		if ((int)y != y)
		{
			if (y < (int)y)
				y--;
			addY = 1;
		}
		if (addX == 1|| addY == 1)
		{
			addX = getCollision((int)x + addX, (int)y + addY);
			addY = getCollision((int)x, (int)y);
			return (addX < addY ? addY : addX);
		}
		return getCollision((int)x, (int)y);
	}
	
	public int getCollision(int x, int y)
	{
		if (x < 0 || _collisions.length <= x ||
				y < 0 || _collisions[0].length <= y)
			return 999;
		return _collisions[x][y];
	}
	
	public void addObject(AObject obj)
	{
		_objects.add(obj);
	}
	
	public void removeObject(AObject obj)
	{
		_objToDel.add(obj);
	}
	
	public void addObjectOnLayer(AObject obj, int layer, int x, int y)
	{
		if (x < 0 || _layerObject.get(0).length <= x ||
				y < 0 || _layerObject.get(0)[0].length <= y ||
				layer < 0 || _layerObject.size() <= layer)
			return;
		int i = 0;
		for (AObject object : _layerObject.get(layer)[x][y])
		{
			if (obj.getY() < object.getY())
			{
				_layerObject.get(layer)[x][y].add(i, obj);
				return ;
			}
			i++;
		}
		_layerObject.get(layer)[x][y].add(obj);
	}
	
	public void removeObjectOnLayer(AObject obj, int layer, int x, int y)
	{
		if (objectIsAt(obj, layer, x, y))
			_layerObject.get(layer)[x][y].remove(obj);
	}
	
	public boolean objectIsAt(AObject obj, int layer, int x, int y)
	{
		if (x < 0 || _layerObject.get(0).length <= x ||
				y < 0 || _layerObject.get(0)[0].length <= y ||
				layer < 0 || _layerObject.size() <= layer)
			return false;
		return _layerObject.get(layer)[x][y].contains(obj);
	}
	
	public List<AObject> getObjects(int layer, int x, int y)
	{
		if (x < 0 || _layerObject.get(0).length <= x ||
				y < 0 || _layerObject.get(0)[0].length <= y ||
				layer < 0 || _layerObject.size() <= layer)
			return null;
		return _layerObject.get(layer)[x][y];
	}
	
	public List<AObject> getObjects(int x, int y)
	{
		if (x < 0 || _layerObject.get(0).length <= x ||
				y < 0 || _layerObject.get(0)[0].length <= y)
			return null;
		List<AObject> objs = new LinkedList<AObject>();
		for (List<AObject>[][] layer : _layerObject)
			objs.addAll(layer[x][y]);
		return objs;
	}
	
	public void update(GameContainer gc, int delta)
	{
		
		int i = 0;
		while (i < _objects.size())
			_objects.get(i++).update(gc, delta);
		for (AObject obj : _objToDel)
		{
			obj.close();
			_objects.remove(obj);
		}
	}
	
	public boolean playerIsAtExit()
	{
		return (_player.getX() == _exit[0] && _player.getY() == _exit[1] &&
				_player.getDir() == e_dir.UP);
	}
}
