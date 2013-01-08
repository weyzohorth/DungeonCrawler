package dungeoncrawler.gui.menu;

import java.util.List;
import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import dungeoncrawler.gui.IWinObject;

public class Menu extends IWinObject
{
	private List<AMenuItem> _items = new LinkedList<AMenuItem>(); 
	
	
	public Menu() throws SlickException
	{
	}
	
	public void addItem(AMenuItem item)
	{
		_items.add(item);
	}
	
	public void update(GameContainer gc, int delta) 
			throws SlickException
	{
    	for (AMenuItem item : _items)
    		item.update(gc, delta);
	}
	
	public void render(GameContainer gc, Graphics g) 
			throws SlickException
	{
    	for (AMenuItem item : _items)
    		item.render(gc, g);
	}
}
