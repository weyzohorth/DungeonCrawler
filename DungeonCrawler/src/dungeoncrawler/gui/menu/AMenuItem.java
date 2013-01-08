package dungeoncrawler.gui.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class AMenuItem
{
	static protected AMenuItem _focus = null;
	public int x;
	public int y;
	
	abstract public void update(GameContainer gc, int delta) 
			throws SlickException;
	abstract public void render(GameContainer gc, Graphics g) 
			throws SlickException;
	
	public AMenuItem(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean isFocused() { return _focus == this; }
	public void focus() { _focus = this; }
	public void unfocus() { _focus = null; }
}
