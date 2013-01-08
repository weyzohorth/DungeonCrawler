package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class FireDog extends Npc
{
	public FireDog(Map map) throws SlickException
	{
		super(map, Data.character_path + "fire\\FireDog.png");
		_howl = SoundManager.firedog;
		setSightDistance(5);
		setAttackSpeed(1.5f);
		_speed = 7f;
		_health = 45;
		_strength = 7;
		updateStats();
	}
}
