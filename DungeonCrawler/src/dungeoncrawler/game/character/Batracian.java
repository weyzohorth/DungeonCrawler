package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class Batracian extends Npc
{
	public Batracian(Map map) throws SlickException
	{
		super(map, Data.character_path + "water\\Batracian.png");
		_howl = SoundManager.batracian;
		setSightDistance(5);
		setAttackSpeed(1.5f);
		_speed = 7;
		_health = 30;
		_strength = 5;
		updateStats();
	}
}
