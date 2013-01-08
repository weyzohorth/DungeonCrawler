package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class Scorpion extends Npc
{
	public Scorpion(Map map) throws SlickException
	{
		super(map, Data.character_path + "fire\\Scorpion.png");
		_howl = SoundManager.scorpion;
		setAttackSpeed(3);
		setSightDistance(5);
		_speed = 4.5f;
		_health = 55;
		_strength = 7;
		updateStats();
	}
}
