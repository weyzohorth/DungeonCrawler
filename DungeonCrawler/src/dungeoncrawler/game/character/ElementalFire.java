package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.game.Map;

public class ElementalFire extends Elemental
{
	public ElementalFire(Map map) throws SlickException
	{
		super(map, Data.character_path + "fire\\ElementalFire.png");
		setAttackSpeed(2);
		_speed = 6;
		_health = 70;
		_strength = 8;
		updateStats();
	}
}
