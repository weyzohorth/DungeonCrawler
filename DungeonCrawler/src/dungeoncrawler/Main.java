package dungeoncrawler;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import dungeoncrawler.gui.Window;
import dungeoncrawler.gui.menu.MenuNewGame;

public class Main
{
	static private class WinInit extends Window.IWinInit
	{
		public void init(Window win) throws SlickException
		{
			win.addObject(new MenuNewGame());
		}
	}

	public static void main(String[] args) throws SlickException
	{
	 	Window win = Window.Create("DungeonCrawler");
	 	win.run(new WinInit());
	 	return ;
	}
}
