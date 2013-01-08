package dungeoncrawler.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.game.character.Player;

public class Gui extends IWinObject
{
	private Image _score;
	private Image _life;
	private Image _backLife;
	private Player _player;
	private int _width;
	@SuppressWarnings("unused")
	private int _height;
	private Font _font;
	
	public Gui(int width, int height) throws SlickException
	{
		_score = new Image(Data.gui_path + "Score2.png");
		_life = new Image(Data.gui_path + "LifeWing3.png");
		_backLife = new Image(Data.gui_path + "LifeWithoutWing2.png");
		_font = new Font(Data.font_path + "jawbrko1.ttf", 20, Color.yellow);
		_width = width;
		_height = height;
		
	}
	
	public void setPlayer(Player player)
	{
		_player = player;
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		float ratio = (float)_player.getHealth() / 100;
		_score.draw(0, 0);
		_font.drawText(Integer.toString(Data.game.getScore()), 95, 10);
		_backLife.draw(_width - _backLife.getWidth(), 0);
		
		_life.draw(_width - _backLife.getWidth(), 0, new Color(0.5f + 0.5f * ratio, ratio, ratio));
	}

}
