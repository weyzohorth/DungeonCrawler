package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class Lizard extends Npc
{
	public Lizard(Map map) throws SlickException
	{
		super(map, Data.character_path + "water\\Lizard.png");
		setSightDistance(5);
		_howl = SoundManager.lizard;
		setAttackSpeed(2);
		_speed = 5;
		_health = 35;
		_strength = 7;
		updateStats();
	}
}
