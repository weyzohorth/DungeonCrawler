package dungeoncrawler.gui.menu;

import java.io.FileInputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.game.Game;
import dungeoncrawler.gui.Font;

public class MenuNewGame extends Menu
{
	private class SelectionNewGame extends AMenuSelection
	{
		private Menu _parent;
		private boolean _continue;
		
		public SelectionNewGame(Menu parent) throws SlickException
		{
			super(new Font(Data.font_path + "MATURASC.TTF", 40, Color.lightGray),
					new Font(Data.font_path + "MATURASC.TTF", 45));
			_parent = parent;
			int i = 0;
			String texts[];
			try
			{
				FileInputStream file = new FileInputStream(Data.save_path);
				file.close();
				texts = new String[]{"Continue", "New Game", "Exit"};
				_continue = true;
			}
			catch (Exception e)
			{
				texts = new String[]{"New Game", "Exit"};
				_continue = false;
			}
			for (String text : texts)
				addSelection(text, 220 - i * 30, (i++ - (_continue ? 1 : 0)) * 70 + 350);
		}

		void Do(GameContainer gc, int select, int delta) throws SlickException
		{
			switch (select)
			{
			case 0:
				if (! _continue)
					_window.replaceObject(_parent, new MenuDifficulty());
				else
				{
					Data.game = new Game(_parent, _continue);
					_window.replaceObject(_parent, Data.game);
				}
				break;
			case 1:
				if (! _continue)
					gc.exit();
				else
					_window.replaceObject(_parent, new MenuDifficulty());
				break;
			case 2:
				gc.exit();
				break;
			}
		}
	}
	
	private Font _title;
	private Music _music;

	public MenuNewGame() throws SlickException
	{
		super();
		_music = new Music(Data.music_path + "zero-project - The crusader's return.ogg");
		_music.loop();
		_title = new Font(Data.font_path + "Coalition.ttf", 40, Color.red);
		MenuImage img = new MenuImage(Data.img_path + "Title.jpg", 0, 20);
		img.setScreenDimension(800, 560);
		addItem(img);
		img = new MenuImage(Data.img_path + "Title2.png");
		img.setScreenDimension(800, 600);
		addItem(img);
		addItem(new MenuText(_title, "DungeonCrawler", 120, 135));
		addItem(new SelectionNewGame(this));
	}

}
