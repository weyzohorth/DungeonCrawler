package dungeoncrawler.gui.menu;

import java.io.FileInputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.game.Game;
import dungeoncrawler.gui.Font;

public class MenuDifficulty extends Menu
{
	private class SelectionDifficulty extends AMenuSelection
	{
		private Menu _parent;
		
		public SelectionDifficulty(Menu parent) throws SlickException
		{
			super(new Font(Data.font_path + "MATURASC.TTF", 40, Color.lightGray),
					new Font(Data.font_path + "MATURASC.TTF", 45));
			_parent = parent;
			int i = 0;
			String texts[] = new String[]{"Normal", "Hard", "Insane"};
			for (String text : texts)
				addSelection(text, 220 - i * 30, (i++ - 1) * 70 + 350);
		}

		void Do(GameContainer gc, int select, int delta) throws SlickException
		{
			Data.game = new Game(_parent, select + 1);
			_window.replaceObject(_parent, Data.game);
		}
	}
	
	private Font _title;

	public MenuDifficulty() throws SlickException
	{
		super();
		_title = new Font(Data.font_path + "Coalition.ttf", 40, Color.red);
		MenuImage img = new MenuImage(Data.img_path + "Title.jpg", 0, 20);
		img.setScreenDimension(800, 560);
		addItem(img);
		img = new MenuImage(Data.img_path + "Title2.png");
		img.setScreenDimension(800, 600);
		addItem(img);
		addItem(new MenuText(_title, "Difficulty", 120, 135));
		addItem(new SelectionDifficulty(this));
	}

}
