package dungeoncrawler.game.object;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.character.Player;
import dungeoncrawler.gui.Font;

public class Bonus extends AObject
{
	public enum e_bonus
	{
		STRENGTH(1, true), HEALTH(3, true),
			ATKSPEED(0.2f, false), SPEED(0.2f, false), SIGHT(1, false), HEAL(20, true);
		private float _value;
		private boolean _mult;
		
		private e_bonus(float value, boolean mult) { _value = value; _mult = mult; }
		public float getValue() { return _value * (! _mult ? 1 : Data.sum.difficulty); }
	}
	private int _currWait = 0;
	private int _waitTime = 15;
	private Font _font;
	private float _maxY = 30;
	private float _textY = 0;
	private int _offsetY = 0;
	private float _incrY = 0.5f;
	private int _timeDisplay = 60;
	private e_bonus _type;
	protected String _name;
	protected float _value;
	
	public Bonus(Map map, float mult) throws SlickException
	{
		super(map);
		_offsetY = 20;
		init(e_bonus.values()[(int)(Math.random() * e_bonus.values().length - 1)], mult);
	}
	
	public Bonus(Map map, e_bonus type, float mult) throws SlickException
	{
		super(map);
		init(type, mult);
	}
	
	private void init(e_bonus type, float mult) throws SlickException
	{
		setFly(true);
		SoundManager.play(SoundManager.bonus);
		_font = new Font(Data.font_path + "jawbrko1.ttf", 20, true, false, new Color(150, 255, 150));
		
		_type = type;
		Player player = _map.getPlayer();
		switch(_type)
		{
		case ATKSPEED:
			Data.sum.atkspd += _type.getValue();
			player.setAttackSpeed(player.getAttackSpeed() + _type.getValue());
			break;
		case HEAL:
			player.addHealth((int)(_type.getValue() * mult));
			break;
		case HEALTH:
			Data.sum.health += _type.getValue() * mult;
			player.setHealthMax(player.getHealthMax() + Data.sum.health);
			break;
		case SIGHT:
			Data.sum.sightdistance += _type.getValue();
			player.setSightDistance(player.getSightDistance() + (int)_type.getValue());
			break;
		case SPEED:
			Data.sum.speed += _type.getValue();
			player.setSpeed(player.getSpeed() + _type.getValue());
			break;
		case STRENGTH:
			Data.sum.strength += _type.getValue() * mult;
			player.setStrength(player.getStrength() + (int)(_type.getValue() * mult));
			break;
		}
	}
	
	@Override
	public void update(GameContainer gc, int delta)
	{
		if (_maxY <= _textY)
		{
			if (0 < _timeDisplay)
				_timeDisplay--;
			else
				delete();
		}
		else
		{
			_textY += _incrY;
			if (_waitTime <= ++_currWait)
				_currWait = 0;
		}

	}

	@Override
	public void drawAt(float x, float y)
	{
		float tw = 32;
		float th = 32;
		_font.drawText(_type.name() + " " + Float.toString(_type.getValue()),
				(int)(x * tw) - 5, (int)(y * th - 15 - _textY - _offsetY));
	}

}
