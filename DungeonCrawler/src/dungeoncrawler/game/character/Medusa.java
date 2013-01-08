package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class Medusa extends Npc
{
	public Medusa(Map map) throws SlickException
	{
		super(map, Data.character_path + "ice\\Medusa.png");
		_howl = SoundManager.medusa;
		setAttackSpeed(1.75f);
		_speed = 5;
		_health = 45;
		_strength = 7;
		updateStats();
	}
}
