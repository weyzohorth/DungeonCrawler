package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class Zombie extends Npc
{
	public Zombie(Map map) throws SlickException
	{
		super(map, Data.character_path + "earth\\Zombie.png");
		_howl = SoundManager.zombie;
		setSightDistance(1);
		setAttackSpeed(1);
		_speed = 3;
		_health = 30;
		_strength = 7;
		updateStats();
	}
}
