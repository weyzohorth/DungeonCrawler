package dungeoncrawler.game.character;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.animation.AnimIce;

public class BossIce extends Boss
{
	private int _powerRange = 4;
	
	public BossIce(Map map) throws SlickException
	{
		super(map, Data.character_path + "ice\\BossIce.png",
				Data.music_path + "Celestial Aeon Project - No Choices.ogg");
		_howl = SoundManager.bossice;
		setAttackSpeed(1);
		_speed = 3.5f;
		_health = 150;
		_strength = 12;
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
						AnimIce anim = new AnimIce(_map, this);
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
