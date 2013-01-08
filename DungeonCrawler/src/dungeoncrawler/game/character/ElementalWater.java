package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.game.Map;

public class ElementalWater extends Elemental
{
	public ElementalWater(Map map) throws SlickException
	{
		super(map, Data.character_path + "water\\ElementalWater.png");
		setAttackSpeed(2);
		_speed = 6.5f;
		_health = 50;
		_strength = 6;
		updateStats();
	}
}
