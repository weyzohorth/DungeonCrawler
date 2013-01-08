package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.game.Map;

public class ElementalEarth extends Elemental
{
	public ElementalEarth(Map map) throws SlickException
	{
		super(map, Data.character_path + "earth\\ElementalEarth.png");
		setAttackSpeed(2);
		_speed = 5.5f;
		_health = 50;
		_strength = 7;
		updateStats();
	}
}
