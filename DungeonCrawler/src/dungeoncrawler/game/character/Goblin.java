package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class Goblin extends Npc
{
	public Goblin(Map map) throws SlickException
	{
		super(map, Data.character_path + "earth\\Goblin.png");
		_howl = SoundManager.goblin;
		setAttackSpeed(1.75f);
		_speed = 4;
		_health = 30;
		_strength = 5;
		updateStats();
	}
}
