package dungeoncrawler.game;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import dungeoncrawler.Data;
import dungeoncrawler.game.character.Npc;
import dungeoncrawler.game.character.Player;
import dungeoncrawler.game.character.Character.e_dir;
import dungeoncrawler.game.object.Chest;
import dungeoncrawler.game.object.Light;

public class MapGenerator
{
	enum e_wall {HORIZONTAL, VERTICAL};
	private Map _map;
	private final int _widthWall = 3;
	private final int _heightWall = 3;
	private final int _roomSize = 2;
	private int _width;
	private int _height;
	private int _pathWidth;
	private int _pathHeight;
	private int[][] _lab;
	private int[] _end;
	private Cell[][] _path;
	private List<Cell> _cells;
	private List<List<Cell>> _noExit;
	private List<List<Cell>> _weightCells;
	
	private class Cell
	{
		public int value;
		public int x;
		public int y;
		public int width = 1;
		public int height = 1;
		
		public Cell(int value, int x, int y)
		{
			this.value = value;
			this.x = x;
			this.y = y;
		}
	}
	
	public MapGenerator(Map map, int width, int height) throws SlickException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		_map = map;
		width = (width + (width % 2 == 1 ? 0 : 1)) * _widthWall;
		height = (height + (height % 2 == 1 ? 0 : 1)) * _heightWall;
		_map.init(width, height);
		_width = map.getWidth() / _widthWall;
		_height = map.getHeight() / _heightWall;
		_pathWidth = (_width - 1) / 2;
		_pathHeight = (_height - 1) / 2;
		
		Log.debug("Map 1/11: Initialization");
		init();
		Log.debug("Map 2/11: Creating Paths");
		generatePaths();
		Log.debug("Map 3/11: Creating Rooms");
		generateRooms();
		Log.debug("Map 4/11: Setting weight");
		setWeight();
		
		Log.debug("Map 5/11: Wearing Tiles");
		addTiles();
		Log.debug("Map 6/11: Creating the entry");
		addEntry();
		Log.debug("Map 7/11: Creating the exit");
		addExit();
		Log.debug("Map 8/11: Adding Lights");
		addLights();
		Log.debug("Map 9/11: Adding Chests");
		addChests();
		Log.debug("Map 10/11: Adding Tile Items");
		addTileItems();
		Log.debug("Map 11/11: Adding Mobs");
		addMobs();
	}
	
	public void init()
	{
		_lab = new int[_width][_height];
		_path = new Cell[_pathWidth][_pathHeight];
		_cells = new ArrayList<Cell>(_width * _height);
		_noExit = new LinkedList<List<Cell>>();
		_weightCells = new LinkedList<List<Cell>>();
		for (int ix = 0; ix < _width; ++ix)
			for (int iy = 0; iy < _height; ++iy)
			{
				_lab[ix][iy] = (ix % 2 == 0 || iy % 2 == 0 ? -1 : ix + iy * _width);
				if (ix % 2 != 0 && iy % 2 != 0)
				{
					int x = (ix - 1) / 2;
					int y = (iy - 1) / 2;
					_path[x][y] = new Cell(x + y * _pathWidth, x, y);
					_cells.add(_path[x][y]);
				}
			}
	}
	
	public void generateRooms()
	{
		int roomMax = (int)(Math.random() * ((_pathWidth + _pathHeight) * 3));
		int roomWidthMax = _pathWidth / 5;
		roomWidthMax = (roomWidthMax < _roomSize ? _roomSize : roomWidthMax) - _roomSize;
		int roomHeightMax = _pathHeight / 5;
		roomHeightMax = (roomHeightMax < _roomSize ? _roomSize : roomHeightMax) - _roomSize;
		for (int i = 0; i < roomMax; ++i)
		{
			Cell cell = _cells.get((int)(Math.random() * _cells.size()));
			int width = (int)(Math.random() * roomWidthMax) + _roomSize;
			int height = (int)(Math.random() * roomHeightMax) + _roomSize;
			
			
			if (cell.width != 1)
				continue;
			if (_pathWidth < cell.x + width)
			{
				if (_pathWidth - cell.x < _roomSize)
					continue;
				else
					width = _pathWidth - cell.x;
			}
			if (_pathHeight < cell.y + height)
			{
				if (_pathHeight - cell.y < _roomSize)
					continue;
				else
					height = _pathHeight - cell.y;
			}
			List<Cell> values = new LinkedList<Cell>();
			boolean stop = false;
			for (int ix = 0; ! stop && ix < width; ++ix)
				for (int iy = 0; ! stop && iy < height; ++iy)
					if (values.contains(_path[cell.x + ix][cell.y + iy]))
						stop = true;
					else
						values.add(_path[cell.x + ix][cell.y + iy]);
			if (stop)
				continue;
			cell.width = width;
			cell.height = height;
			
			for (Cell tmp : _cells)
			{
				if (tmp == cell)
					continue;
				if (cell.x <= tmp.x && tmp.x < cell.x + cell.width &&
						cell.y <= tmp.y && tmp.y < cell.y + cell.height)
					createRoom(cell, tmp);
			}
		}
	}
	
	private void createRoom(Cell cell, Cell tmp)
	{
		for (int x = 0; x < tmp.width; ++x)
			for (int y = 0; y < tmp.height; ++y)
			{
				_path[tmp.x + x][tmp.y + y] = cell;
				if (tmp.x + x + 1 < cell.x + cell.width)
				{
					_lab[(tmp.x + x) * 2 + 2][(tmp.y + y) * 2 + 1] = cell.value;
					if (cell.y < tmp.y + y)
						_lab[(tmp.x + x) * 2 + 1][(tmp.y + y) * 2] = cell.value;
				}
				if (tmp.y + y + 1 < cell.y + cell.height)
				{
					_lab[(tmp.x + x) * 2 + 1][(tmp.y + y) * 2 + 2] = cell.value;
					if (cell.x < tmp.x + x)
						_lab[(tmp.x + x) * 2][(tmp.y + y) * 2 + 1] = cell.value;
				}
				if (cell.x < tmp.x + x && cell.y < tmp.y + y)
					_lab[(tmp.x + x) * 2][(tmp.y + y) * 2] = cell.value;
			}
	}
	
	public void generatePaths()
	{
		List<Integer> values = new ArrayList<Integer>(_pathWidth * _pathHeight);
		List<Cell> save;
		for (Cell cell : _cells)
			values.add(cell.value);
		while (1 < values.size())
		{
			save = new ArrayList<Cell>(_cells.size());
			int oldSize = -1;
			while (! _cells.isEmpty() && oldSize != _cells.size())
			{
				oldSize = _cells.size();
				Cell cell = _cells.get((int)(Math.random() * _cells.size()));
				List<Integer> dirs = new ArrayList<Integer>(4);
				for (int i = 0; i < 4; ++i)
					dirs.add(i);
				int dir = 0;
				int x = cell.x;
				int y = cell.y;
				int ix = 0;
				int iy = 0;
				while (! dirs.isEmpty())
				{
					dir = dirs.get((int)(Math.random() * dirs.size()));
					ix = (int)Math.cos(Math.toRadians(dir * 90));
					iy = -(int)Math.sin(Math.toRadians(dir * 90));
					x = cell.x + ix;
					y = cell.y + iy;
					if (0 <= x && x < _pathWidth && 0 <= y && y < _pathHeight &&
							_path[x][y].value != cell.value)
						break;
					dirs.remove(dirs.indexOf(dir));
				}
				if (dirs.isEmpty())
				{
					_cells.remove(cell);
					continue;
				}
				int value = _path[x][y].value;
				for (int jx = 0; jx < _pathWidth; ++jx)
					for (int jy = 0; jy < _pathHeight; ++jy)
						if (_path[jx][jy].value == value)
							_path[jx][jy].value = cell.value;
				values.remove(new Integer(value));
				x = cell.x * 2 + 1 + ix;
				y = cell.y * 2 + 1 + iy;
				_lab[x][y] = cell.value;
				_cells.remove(cell);
				save.add(cell);
			}
			while (! save.isEmpty())
			{
				_cells.add(save.get(0));
				save.remove(0);
			}
			
		}
	}
	
	public void setWeight()
	{
		for (int x = 0; x < _lab.length; ++x)
			for (int y = 0; y < _lab[0].length; ++y)
				if (_lab[x][y] != -1)
					_lab[x][y] = 0;
		LinkedList<Cell> cells = new LinkedList<Cell>();
		LinkedList<Cell> exits = new LinkedList<Cell>();
		cells.add(new Cell(1, 0, 0));
		_lab[1][1] = 1;
		while (cells.size() != 0)
		{
			_weightCells.add(cells);
			_noExit.add(exits);
			cells = new LinkedList<Cell>();
			for (Cell cell : _weightCells.get(_weightCells.size() - 1))
			{
				int numExits = 0;
				for (int dir = 0; dir < 4; ++dir)
				{
					int modif[] = {(int)Math.cos(Math.toRadians(dir * 90)),
							-(int)Math.sin(Math.toRadians(dir * 90))};
					if (_lab[cell.x * 2 + 1 + modif[0]][cell.y * 2 + 1 + modif[1]] != -1 &&
							_lab[cell.x * 2 + 1 + modif[0] * 2][cell.y * 2 + 1 + modif[1] * 2] == 0)
					{
						cells.addFirst(new Cell(_weightCells.size() + 1,
								cell.x + modif[0], cell.y + modif[1]));
						_lab[cell.x * 2 + 1 + modif[0] * 2][cell.y * 2 + 1 + modif[1] * 2] = cells.get(0).value;
						numExits++;
					}
				}
				if (numExits == 0)
					exits.add(cell);
			}
			exits = new LinkedList<Cell>();
		}
		if (_noExit.get(_noExit.size() - 1).size() == 0)
			_noExit.remove(_noExit.size() - 1);
		_noExit.add(cells);
	}
	
	public void addTiles()
	{
		int x, y;
		
		for (int ix = 0; ix < _lab.length; ++ix)
			for (int iy = 0; iy < _lab[0].length; ++iy)
			{
				x = ix * _widthWall;
				y = iy * _heightWall;
				if (_lab[ix][iy] != -1)
					for (int jx = 0; jx < _widthWall; ++jx)
						for (int jy = 0; jy < _heightWall; ++jy)
							_map.setTile(0, 0, 0, x + jx, y + jy);
				else
				{
					boolean left = false;
					boolean right = false;
					boolean up = false;
					boolean down = false;
					if (ix + 1 < _width && _lab[ix + 1][iy] == -1)
						right = true;
					if (0 <= ix - 1 && _lab[ix - 1][iy] == -1)
						left = true;
					if (iy + 1 < _height && _lab[ix][iy + 1] == -1)
						down = true;
					if (0 <= iy - 1 && _lab[ix][iy - 1] == -1)
						up = true;
					
					leftTopCorner(x, y, left, up);
					top(x + 1, y, up);
					rightTopCorner(x + 2, y, right, up);
					leftBottomCorner(x, y, left, down);
					bot(x + 1, y, down);
					rightBottomCorner(x + 2, y, right, down);
				}
			}
	}
	
	private int getMaxItem(boolean diff)
	{
		return _width * _height / (10 - (! diff ? 0 : Data.sum.difficulty * Data.sum.difficulty));
	}
	
	private int getMinItem(boolean diff)
	{
		return (int)(getMaxItem(diff) * 0.75f);
	}
	
	private int getRandNumberItem(boolean diff)
	{
		int max = getMaxItem(diff);
		int min = getMinItem(diff);
		return (int)(Math.random() * min) + (max - min);
	}
	
	public void addMobs() throws SlickException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		int rand = getRandNumberItem(true);
		for (int i = 0; i < rand; ++i)
			spawnMob(_map.getMobs().get((int)(Math.random() * (_map.getMobs().size() - 2))));
		for (int i = 0; i < _lab.length * _lab[0].length / 100; ++i)
			spawnMob(_map.getMobs().get(_map.getMobs().size() - 2));
		if (_map.thereIsABoss())
			spawnMob(_map.getMobs().get(_map.getMobs().size() - 1));
	}
	
	private void spawnMob(Class<?> mobClass) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		for (int i = 0; i < 5; ++i)
		{
			int x = (int)(Math.random() * _map.getWidth());
			int y = (int)(Math.random() * _map.getHeight());
			if (x <= 6 && y <= 6 || ! _map.canSpawn(x, y))
				continue;
			Object[] args = {_map};
			Npc mob = (Npc) mobClass.getConstructors()[0].newInstance(args);
			mob.setPos(x , y);
			break;
		}
	}
	
	public void addLights() throws SlickException
	{
		int max = _pathWidth * _pathHeight / 10;
		int min = (int)(max * 0.75f);
		for (int i = 0; i < Math.random() * min + (max - min); ++i)
		{
			while (true)
			{
				int x = (int)(Math.random() * _pathWidth);
				int y = (int)(Math.random() * _pathHeight);
				x = (x * 2 + 1) * _widthWall + 1;
				y = (y * 2 + 1) * _heightWall;
				if (x <= 6 && y <= 6 || _map.getTile(0, x, y) != 0)
					continue;
				Light light = new Light(_map, (int)(Math.random() * 3 + 3),
						(float)Math.random() * 0.50f + 0.25f);
				light.setPos(x , y);
				break;
			}
		}
	}
	
	private void addChests() throws SlickException
	{
		for (int i = 0; i < _noExit.size(); ++i)
			for (Cell exit : _noExit.get(i))
			{
				int size = (int)(((double)i / _noExit.size() * 4) * Math.random());
				int x = exit.x * 2 + 1;
				int y = exit.y * 2 + 1;
				Chest chest = new Chest(_map, size, e_dir.DOWN);
				chest.setPos(x * _widthWall + 1, y * _heightWall);
				e_dir dirs[] = new e_dir[]{e_dir.RIGHT, e_dir.UP, e_dir.LEFT, e_dir.DOWN};
				for (int j = 0; j < 4; ++j)
					if (_lab[x + (int)Math.cos(Math.toRadians(j * 90))]
							[y - (int)Math.sin(Math.toRadians(j * 90))] != -1)
					{
						chest.setDir(dirs[j]);
						break;
					}
			}
	}
	
	public void addTileItems()
	{
		int tids[] = new int[Map.tileIdDecs.length + Map.tileIdDecCols.length];
		int i;
		for (i = 0; i < Map.tileIdDecCols.length; ++i)
			tids[i] = Map.tileIdDecCols[i];
		for (int j = 0; j < Map.tileIdDecs.length; ++j)
			tids[i + j] = Map.tileIdDecs[j];
		int rand = getRandNumberItem(false) * _widthWall * _heightWall;
		for (int j = 0; j < rand + Data.sum.finalLevel * 2; ++j)
		{
			for (int k = 0; k < 5; ++k)
			{
				int x = (int)(Math.random() * _map.getWidth());
				int y = (int)(Math.random() * _map.getHeight());
				if (x <= 6 && y <= 6 ||
						_map.getTile(0, x, y) != 0 || _map.getTile(1, x, y) != -1 ||
						_map.getTile(2, x, y) != -1 ||
						! _map.getObjects(x, y).isEmpty())
					continue;
				if (_end[0] - 6 < x && x < _end[0] + 6 && _end[1] - 6 < y && y < _end[1] + 6)
					continue;
				_map.setTile(tids[(int)(Math.random() * tids.length)], 1, x, y);
				break;
			}
		}
	}
	
	private void addEntry() throws SlickException
	{
		for (int ix = 0; ix < 3; ++ix)
		{
			_map.setTile(5 + ix, 21, 0, 3 + ix, 3);
			for (int iy = 0; iy < 2; ++iy)
			{
				_map.setTile(5 + ix, 13 + iy, 1, 3 + ix, 1 + iy);
				_map.setTile(5 + ix, 18 + iy, 0, 3 + ix, 4 + iy);
			}
		}
		Player player = new Player(_map);
		_map.setPlayer(player);
		player.setDir(e_dir.DOWN);
		player.setPos(4, 3);
		
		Light light = new Light(_map, 6, 0.75f);
		light.setPos(4, 2);
	}
	
	private void addExit() throws SlickException
	{
		for (int i = 1; i <= _noExit.size(); ++i)
		{
			List<Cell> exits = _noExit.get(_noExit.size() - i);
			for (Cell exit : exits)
				if (_lab[exit.x * 2 + 1][exit.y * 2] == -1)
				{
					setExit(exit);
					exits.remove(exit);
					return;
				}
		}
		setExit(_path[_path.length - 1][0]);
	}
	
	private void setExit(Cell exit) throws SlickException
	{
		int x = (exit.x * 2 + 1) * _widthWall;
		int y = (exit.y * 2) * _heightWall;
		for (int ix = 0; ix < 3; ++ix)
			for (int iy = 0; iy < 2; ++iy)
				_map.setTile(5 + ix, 11 + iy, 1, x + ix, y + iy + 1);
		Light light = new Light(_map, 3, 0.30f);
		light.setPos(x + 1, y + _heightWall);
		_map.setExit(x + 1, y + _heightWall);
		_end = new int[]{x, y};
	}
	
	private void leftTopCorner(int x, int y, boolean left, boolean up)
	{
		if (y != 0)
		{
			if (left == false && up == false)
				_map.setTile(0, 4, 2, x, y - 1);
			if (left == false && up == true)
				_map.setTile(0, 5, 0, x, y - 1);
			if (left == true && up == false)
				_map.setTile(1, 4, 2, x, y - 1);
			if (left == true && up == true)
				_map.setTile(3, 4, 0, x, y - 1);
		}
	}
	
	private void leftBottomCorner(int x, int y, boolean left, boolean down)
	{
		if (left == false && down == false)
		{
			_map.setTile(0, 6, 0, x, y);
			_map.setTile(0, 7, 0, x, y + 1);
			_map.setTile(0, 8, 0, x, y + 2);
		}
		if (left == false && down == true)
		{
			_map.setTile(0, 5, 0, x, y);
			_map.setTile(0, 5, 0, x, y + 1);
			_map.setTile(0, 5, 0, x, y + 2);
		}
		if (left == true && down == false)
		{
			_map.setTile(1, 6, 0, x, y);
			_map.setTile(1, 7, 0, x, y + 1);
			_map.setTile(1, 8, 0, x, y + 2);
		}
		if (left == true && down == true)
		{
			_map.setTile(3, 5, 0, x, y);
			_map.setTile(0, 5, 0, x, y + 1);
			_map.setTile(0, 5, 0, x, y + 2);
		}
	}
	
	private void top(int x, int y, boolean up)
	{
		if (y != 0)
		{
			if (up)
				_map.setTile(1, 5, 0, x, y - 1);
			else
				_map.setTile(1, 4, 2, x, y - 1);
		}
		_map.setTile(1, 5, 0, x, y);
	}
	
	private void bot(int x, int y, boolean down)
	{
		if (down)
		{
			_map.setTile(1, 5, 0, x, y);
			_map.setTile(1, 5, 0, x, y + 1);
			_map.setTile(1, 5, 0, x, y + 2);
		}
		else
		{
			_map.setTile(1, 6, 0, x, y);
			_map.setTile(1, 7, 0, x, y + 1);
			_map.setTile(1, 8, 0, x, y + 2);
		}
	}
	
	private void rightTopCorner(int x, int y, boolean right, boolean up)
	{
		if (y != 0)
		{
			if (right == false && up == false)
				_map.setTile(2, 4, 2, x, y - 1);
			if (right == false && up == true)
				_map.setTile(2, 5, 0, x, y - 1);
			if (right == true && up == false)
				_map.setTile(1, 4, 2, x, y - 1);
			if (right == true && up == true)
				_map.setTile(4, 4, 0, x, y - 1);
		}
	}
	
	private void rightBottomCorner(int x, int y, boolean right, boolean down)
	{
		if (right == false && down == false)
		{
			_map.setTile(2, 6, 0, x, y);
			_map.setTile(2, 7, 0, x, y + 1);
			_map.setTile(2, 8, 0, x, y + 2);
		}
		if (right == false && down == true)
		{
			_map.setTile(2, 5, 0, x, y);
			_map.setTile(2, 5, 0, x, y + 1);
			_map.setTile(2, 5, 0, x, y + 2);
		}
		if (right == true && down == false)
		{
			_map.setTile(1, 6, 0, x, y);
			_map.setTile(1, 7, 0, x, y + 1);
			_map.setTile(1, 8, 0, x, y + 2);
		}
		if (right == true && down == true)
		{
			_map.setTile(4, 5, 0, x, y);
			_map.setTile(2, 5, 0, x, y + 1);
			_map.setTile(2, 5, 0, x, y + 2);
		}
	}
}
