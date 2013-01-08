package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.game.Map;

public class ElementalIce extends Elemental
{
	public ElementalIce(Map map) throws SlickException
	{
		super(map, Data.character_path + "ice\\ElementalIce.png");
		setAttackSpeed(2);
		_speed = 5.5f;
		_health = 60;
		_strength = 8;
		updateStats();
	}
}
