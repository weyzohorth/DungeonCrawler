package dungeoncrawler.game.object;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.character.Character.e_dir;
import dungeoncrawler.game.character.Player;
import dungeoncrawler.game.object.Bonus.e_bonus;
import dungeoncrawler.gui.Font;

import org.newdawn.slick.util.Log;

public class Chest extends AObject
{
	static private int _frameNumber = 4;
	static private Color _textColors[];
	private Image _sprites[];
	private int _frameW[];
	private int _frameH[];
	private e_dir _direction;
	private int _size;
	private int _frame = 0;
	private boolean _open = false;
	private int _currWait = 0;
	private int _waitTime = 15;
	private int _score = 100;
	private Font _font;
	private float _textY = 0;
	private int _timeDisplay = 60;
	static private final int _bonus = 100;
	
	public Chest(Map map, int size, e_dir dir) throws SlickException
	{
		super(map);
		if (_textColors == null)
			_textColors = new Color[]{Color.yellow, Color.orange, Color.red, new Color(150, 0, 200)};
		_font = new Font(Data.font_path + "jawbrko1.ttf", 20, true, false, _textColors[size]);
		_sprites = new Image[4];
		_frameW = new int[4];
		_frameH = new int[4];
		for (int i = 0; i < 4; ++i)
		{
			_sprites[i] = (new Image(Data.character_path + "Chest" + Integer.toString(i) + ".png"));
			_frameW[i] = _sprites[i].getWidth() / _frameNumber;
			_frameH[i] = _sprites[i].getHeight() / _frameNumber;
		}
		_direction = dir;
		_size = size;
		int bonus = 1;
		for (int i = 0; i < _size; ++i)
			bonus *= 2;
		bonus = _bonus * bonus;
		_score += bonus - Math.random() * bonus / 2;
	}

	public void setDir(e_dir dir)
	{
		_direction = dir;
	}
	
	@Override
	public void update(GameContainer gc, int delta)
	{
		if (_open)
		{
			if (_frameNumber - 1 <= _frame)
			{
				if (0 < _timeDisplay)
					_timeDisplay--;
			}
			else
			{
				_textY += 0.5;
				if (_waitTime <= ++_currWait)
				{
					_frame++;
					_currWait = 0;
				}
			}
			return ;
		}
		Player player = _map.getPlayer();
		for (int i = 0; i < 4 && ! _open; ++i)
		{
			double angle = Math.toRadians(i * 90);
			if (player.getX() == (int)(_x + Math.cos(angle)) &&
					player.getY() == (int)(_y - Math.sin(angle)))
			{
				_open = true;
				Data.game.addScore(_score);
				Data.sum.chests[_size]++;
				Data.sum.scoreByChests[_size] += _score;
				try
				{
					Bonus bonus = new Bonus(_map, e_bonus.HEAL, 0.5f + _size / 4.0f);
					bonus.setPos(player.getX(), player.getY());
					if (0 < _size)
					{
						bonus = new Bonus(_map, _size);
						bonus.setPos(player.getX(), player.getY());
					}
				}
				catch (SlickException e)
				{
					e.printStackTrace();
				}
				SoundManager.play(SoundManager.chest);
			}
		}

	}

	@Override
	public void drawAt(float x, float y)
	{
		float tw = 32;
		float th = 32;
		int frameH = _frameH[_direction.ordinal()];
		int frameW = _frameW[_direction.ordinal()];

		_sprites[_direction.ordinal()].draw(x * tw, y * th - frameH / 2,
				x * tw + frameW * _zoom, y * th + frameH * _zoom - frameH / 2,
				_size * frameW, _frame * frameH, (_size + 1) * frameW, (_frame + 1) * frameH);
		if (_open && 0 < _timeDisplay)
			_font.drawText(Integer.toString(_score), (int)(x * tw) - 5, (int)(y * th - _textY));
	}

}
