package dungeoncrawler.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.Log;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.gui.Font;
import dungeoncrawler.gui.Gui;
import dungeoncrawler.gui.IWinObject;
import dungeoncrawler.gui.menu.MenuGameOver;

public class Game extends IWinObject
{
	private Map _map;
	private Camera _cam;
	private Gui _gui;
	private Music _music;
	private Sound _goMusic;
	private int _size = 7;
	private final int _sizeMin = 7;
	private Font _gameOver;
	private int _goY;
	private float _fadeOut = 0;
	
	public Game(IWinObject parent) throws SlickException
	{
		super(parent);
		initGame(false, 1);
	}
	
	public Game(IWinObject parent, int difficulty) throws SlickException
	{
		super(parent);
		initGame(false, difficulty);
	}
	
	public Game(IWinObject parent, boolean load) throws SlickException
	{
		super(parent);
		initGame(load, 0);
	}
	
	public void close()
	{
		_map.close();
	}
	
	private void initGame(boolean load, int difficulty) throws SlickException
	{
		new SoundManager();
		if (load)
			load();
		else
		{
			File save = new File(Data.save_path);
			save.delete();
			Data.sum = new GameSummary();
			Data.sum.difficulty = difficulty;
		}
		_goY = -getHeight() / 2;
		Data.game = this;
		_cam = new Camera();
		_gui = new Gui(getWidth(), getHeight());
		_music = new Music(Data.music_path + "NomoreSisyphus - Desert Maze.ogg");
		_goMusic = new Sound(Data.music_path + "Lzn02 - Goodbye Illusion World.ogg");
		_gameOver = new Font(Data.font_path + "Chicken Butt.ttf", 120, Color.red);
		initMap(! load);
	}
	
	private void initMap(boolean save) throws SlickException
	{
		if (Data.sum.finalLevel != 0 && save)
		{
			levelUp();
			save();
		}
		_size = _sizeMin + Data.sum.finalLevel * 2 * Data.sum.difficulty;
		if (_map != null)
			_map.close();
		_map = new Map(((Data.sum.finalLevel++ / Data.changeWorld) % 4) + 1, _size, _size);
		
		_music.loop();
		_cam.setMap(_map);
		_cam.setTarget(_map.getPlayer());
		_gui.setPlayer(_map.getPlayer());
	}
	
	private void levelUp()
	{
		Data.sum.strength += Data.sum.finalLevel / Data.changeWorld * 0.25f;
		Data.sum.health += Data.sum.finalLevel / Data.changeWorld * 3 * 0.25f;
		Data.sum.atkspd += Data.sum.finalLevel / Data.changeWorld * 0.2f * 0.25f;
		Data.sum.speed += Data.sum.finalLevel / Data.changeWorld * 0.2f * 0.25f;
		Data.sum.sightdistance += Data.sum.finalLevel / Data.changeWorld * 0.25f;
	}
	
	private void save()
	{
		try
		{
			FileOutputStream file = new FileOutputStream(Data.save_path);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(Data.sum);
	        out.close();
	        file.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void load()
	{
		try
		{
			FileInputStream file = new FileInputStream(Data.save_path);
			ObjectInputStream in = new ObjectInputStream(file);
			Data.sum = (GameSummary)in.readObject();
	        in.close();
	        file.close();
	        Log.debug("Level: " + Integer.toString(Data.sum.finalLevel));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Data.sum = new GameSummary();
		}
		
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException
	{
		if (! _map.getPlayer().isDead())
		{
			if (_map.playerIsAtExit())
				initMap(true);
			else
				_map.update(gc, delta);
		}
		else
		{
			if (_goY < 0 && _fadeOut == 0)
				_goY++;
			else if (_fadeOut < 1)
				_fadeOut += 0.01;
			else if (- getHeight() / 3.25 < _goY)
				_goY--;
			else
				new MenuGameOver(_goMusic, getWidth() / 4 - 30, getHeight() / 3 + _goY);
		}
		_cam.update(gc, delta);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		_cam.render(gc, g);
		_gui.render(gc, g);
		if (_map.getPlayer().isDead())
		{
			if (! _goMusic.playing())
			{
				_music.fade(3000, 0, true);
				_goMusic.play();
			}
			if (_goY == 0 || 1 <= _fadeOut)
			{
				g.setColor(new Color(0, 0, 0, _fadeOut));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			_gameOver.drawText("Game Over", getWidth() / 4 - 30, getHeight() / 3 + _goY);
		}
	}

	public void addScore(int add) { Data.sum.score += add; }
	public int getScore() { return Data.sum.score; }
	
	public int getLevel() { return Data.sum.finalLevel; }
	
	public int getWidth() { return _window.getApp().getWidth(); }
	public int getHeight() { return _window.getApp().getHeight(); }
	
	public Music getMusic() { return _music; }
}
