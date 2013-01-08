package dungeoncrawler.gui;

import java.util.List;
import java.util.LinkedList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;

public class Window extends BasicGame
{
	static abstract public class IWinInit
	{
		abstract public void init(Window win) throws SlickException;
	}
	// Attributes
	static private int _defaultWidth = Data.screen_width;
	static private int _defaultHeight = Data.screen_height;
	static private String _defaultTitle = "Game";
	
	private int _ellapsedTime = Data.wait_time;
	private AppGameContainer _app;
	private Window.IWinInit _initializer = null;
	private List<IWinObject> _objects = new LinkedList<IWinObject>();
	
	// Game constructors
	static public Window Create() throws SlickException
	{
		return Create(_defaultWidth, _defaultHeight, null);
	}
	
	static public Window Create(String title) throws SlickException
	{
		return Create(_defaultWidth, _defaultHeight, title);
	}
	
	static public Window Create(int width, int height) throws SlickException
	{
		return Create(width, height, null);
	}
	
	static public Window Create(int width, int height, String title) throws SlickException
	{
		Window win = new Window(title);
		win.setApp(new AppGameContainer(win));
		 
	    win.getApp().setDisplayMode(width, height, false);
	    return win;
	}
	
	// Constructor
	private Window(String title) throws SlickException
	{
		super(title == null ? _defaultTitle : title);
	}
	
	// Methods
	public void run(Window.IWinInit initializer) throws SlickException
	{
		_initializer = initializer;
		_app.start();
	}
	
	@Override
    public void init(GameContainer gc) 
			throws SlickException
	{
		if (_initializer != null)
			_initializer.init(this);
		gc.setTargetFrameRate(60);
		gc.setShowFPS(false);
    }
 
    @Override
    public void update(GameContainer gc, int delta) 
			throws SlickException     
    {
    	_ellapsedTime += delta;
    	if (_ellapsedTime < Data.wait_time)
    		return ;
    	_ellapsedTime = 0;
    	for (IWinObject obj : _objects)
    		obj.update(gc, delta);
    }
 
    public void render(GameContainer gc, Graphics g) 
			throws SlickException 
    {
    	for (IWinObject obj : _objects)
    		obj.render(gc, g);
    }
    
	// Getters/Setters
	public AppGameContainer	getApp() 						{ return _app; }
	public void 			setApp(AppGameContainer app)	{ _app = app; }

	public void	addObject(IWinObject obj)
	{
		obj.setWindow(this);
		_objects.add(obj);
	}
	public void	removeObject(IWinObject obj)	{ _objects.remove(obj); }
	
	public void replaceObject(IWinObject oldOne, IWinObject newOne)
	{
		removeObject(oldOne);
		addObject(newOne);
	}
}
