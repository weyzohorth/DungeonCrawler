package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class LivingWater extends Npc
{
	public LivingWater(Map map) throws SlickException
	{
		super(map, Data.character_path + "water\\LivingWater.png");
		_howl = SoundManager.livingwater;
		setSightDistance(5);
		setAttackSpeed(1);
		_speed = 3;
		_health = 140;
		_strength = 5;
		updateStats();
	}
}
