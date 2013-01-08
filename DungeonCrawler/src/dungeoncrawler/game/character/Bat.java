package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class Bat extends Npc
{
	public Bat(Map map) throws SlickException
	{
		super(map, Data.character_path + "earth\\Bat.png");
		setFly(true);
		setLayer(2);
		_howl = SoundManager.bat;
		setSightDistance(10);
		setAttackSpeed(2);
		_speed = 8;
		_health = 10;
		_strength = 3;
		updateStats();
	}
}
