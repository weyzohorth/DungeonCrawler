package dungeoncrawler.game.character;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.animation.AnimWater;

public class BossWater extends Boss
{
	public BossWater(Map map) throws SlickException
	{
		super(map, Data.character_path + "water\\BossWater.png",
				Data.music_path + "Celestial Aeon Project - Stormfront.ogg");
		_howl = SoundManager.bosswater;
		setAttackSpeed(1.5f);
		_speed = 4;
		_health = 140;
		_strength = 8;
		updateStats();
	}
	
	@Override
	public void update(GameContainer gc, int delta)
	{
		if (_chasePlayer)
		{
			if (Math.random() * 1000 < Data.sum.finalLevel / Data.changeWorld)
				try
				{
						AnimWater anim = new AnimWater(_map, this);
						anim.setPos((int)_x, (int)_y);
				}
				catch (SlickException e)
				{
					e.printStackTrace();
				}
		}
		super.update(gc, delta);
	}
}
