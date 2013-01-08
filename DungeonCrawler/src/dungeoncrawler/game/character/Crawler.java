package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class Crawler extends Npc
{
	public Crawler(Map map) throws SlickException
	{
		super(map, Data.character_path + "fire\\Crawler.png");
		_howl = SoundManager.crawler;
		setSightDistance(5);
		setAttackSpeed(1.5f);
		_speed = 5;
		_health = 40;
		_strength = 6;
		updateStats();
	}
}
