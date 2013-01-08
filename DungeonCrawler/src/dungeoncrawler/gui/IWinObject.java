package dungeoncrawler.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class IWinObject
{
	protected Window _window;
	protected IWinObject _child = null;
	protected IWinObject _parent = null;
	
	public IWinObject()
	{
	}
	
	public IWinObject(IWinObject parent)
	{
		_parent = parent;
		parent.setChild(this);
		_window = parent._window;
	}
	
	abstract public void update(GameContainer gc, int delta) 
			throws SlickException;
	
	abstract public void render(GameContainer gc, Graphics g) 
			throws SlickException;
	
	public void setWindow(Window win) { _window = win; }
	public Window getWindow() { return _window; }
	public void setChild(IWinObject child) { _child = child; }
	public void setParent(IWinObject parent) { _parent = parent; }
}
