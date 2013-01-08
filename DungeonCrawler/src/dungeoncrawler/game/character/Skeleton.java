package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class Skeleton extends Npc
{
	public Skeleton(Map map) throws SlickException
	{
		super(map, Data.character_path + "ice\\Skeleton.png");
		_howl = SoundManager.skeleton;
		setSightDistance(2);
		setAttackSpeed(3.5f);
		_speed = 4;
		_health = 35;
		_strength = 8;
		updateStats();
	}
}
