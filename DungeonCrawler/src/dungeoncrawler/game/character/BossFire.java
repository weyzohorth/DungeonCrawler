package dungeoncrawler.game.character;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.animation.AnimFire;

public class BossFire extends Boss
{
	public BossFire(Map map) throws SlickException
	{
		super(map, Data.character_path + "fire\\BossFire.png",
				Data.music_path + "Celestial Aeon Project - Together We Are Strong.ogg");
		_howl = SoundManager.bossfire;
		setAttackSpeed(1.5f);
		_speed = 5;
		_health = 160;
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
					AnimFire anim = new AnimFire(_map, this);
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
