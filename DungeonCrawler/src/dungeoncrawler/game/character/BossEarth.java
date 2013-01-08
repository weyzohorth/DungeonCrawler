package dungeoncrawler.game.character;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.animation.AnimEarth;

public class BossEarth extends Boss
{
	private int _powerRange = 4;
	
	public BossEarth(Map map) throws SlickException
	{
		super(map, Data.character_path + "earth\\BossEarth.png",
				Data.music_path + "Celestial Aeon Project - Broken.ogg");
		_howl = SoundManager.bossearth;
		setAttackSpeed(0.5f);
		_speed = 3;
		_health = 100;
		_strength = 15;
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
					int x = (int)_x + (int)(Math.random() * _powerRange * 2) - _powerRange;
					int y = (int)_y + (int)(Math.random() * _powerRange * 2) - _powerRange;
					if (_map.canSpawn(x, y))
					{
						AnimEarth anim;
						anim = new AnimEarth(_map, this);
						anim.setPos(x, y);
					}
				}
				catch (SlickException e)
				{
					e.printStackTrace();
				}
		}
		super.update(gc, delta);
	}
}
