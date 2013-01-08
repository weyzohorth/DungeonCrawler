package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class LittleDevil extends Npc
{
	public LittleDevil(Map map) throws SlickException
	{
		super(map, Data.character_path + "ice\\LittleDevil.png");
		_howl = SoundManager.littledevil;
		setFly(true);
		setLayer(2);
		setSightDistance(7);
		setAttackSpeed(1.75f);
		_speed = 7.5f;
		_health = 40;
		_strength = 7;
		updateStats();
	}
}
