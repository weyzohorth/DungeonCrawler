package dungeoncrawler.gui.menu;

import java.io.File;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import dungeoncrawler.Data;
import dungeoncrawler.gui.Font;

public class MenuGameOver extends Menu
{
	private Font _gameOver;
	private Font _name;
	private Font _data;
	private Sound _music;

	public MenuGameOver(Sound music, int x, int y) throws SlickException
	{
		super();
		
		File save = new File(Data.save_path);
		save.delete();
		_music = music;
		_gameOver = new Font(Data.font_path + "Chicken Butt.ttf", 120, Color.red);
		_data = new Font(Data.font_path + "aladdin.ttf", 30, Color.white);
		_name = new Font(Data.font_path + "MATURASC.TTF", 30, Color.red);
		
		int datas[] = new int[] {Data.sum.finalLevel, Data.sum.score,
				Data.sum.killedMobs, Data.sum.scoreByKilledMobs,
				Data.sum.killedElementals, Data.sum.scoreByKilledElementals,
				Data.sum.killedBosses, Data.sum.scoreByKilledBosses,
				Data.sum.chests[0], Data.sum.scoreByChests[0],
				Data.sum.chests[1], Data.sum.scoreByChests[1],
				Data.sum.chests[2], Data.sum.scoreByChests[2],
				Data.sum.chests[3], Data.sum.scoreByChests[3]};
		String names[] = new String[] {"Level:", "Score:",
				"Mobs killed:", "=>",
				"Elementals killed:", "=>",
				"Bosses killed:", "=>",
				"", "=>",
				"", "=>",
				"", "=>",
				"", "=>"};
		
		int startX = 90;
		int startY = 170;
		int incrX = 250;
		int incrY = 50;
		
		addItem(new MenuText(_gameOver, "Game Over", x, y));
		for (int i = 0; i < datas.length; ++i)
		{
			addItem(new MenuText(_name, names[i],
					startX + (i % 2) * (int)(incrX * 1.5), startY + (i / 2) * incrY));
			addItem(new MenuText(_data, Integer.toString(datas[i]),
					startX + incrX + (i % 2) * incrX, startY + (i / 2) * incrY));
		}
		Image chests = new Image(Data.character_path + "Chest0.png");
		int subWidth = chests.getWidth() / 4;
		int subHeight = chests.getHeight() / 4;
		for (int i = 0; i < 4; ++i)
		{
			MenuImage img = new MenuImage(startX - subWidth + incrX / 2,
					startY + (datas.length / 4 + i) * incrY);
			img.setImg(chests);
			img.setImgLimit(i * subWidth, 3 * subHeight, subWidth, subHeight);
			addItem(img);
		}
		Data.game.close();
		Data.game.getWindow().replaceObject(Data.game, this);
		Data.game = null;
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_ENTER))
		{
			input.clearKeyPressedRecord();
			_music.stop();
			_window.replaceObject(this, new MenuNewGame());
		}
	}
}
