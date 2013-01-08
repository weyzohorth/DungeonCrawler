package dungeoncrawler.game.character;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import dungeoncrawler.Data;
import dungeoncrawler.Utils;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.object.Bonus;

public class Boss extends Npc
{
	private Sound _music;
	public Boss(Map map, String img, String music) throws SlickException
	{
		super(map, img);
		_music = new Sound(music);
		_type = e_type.BOSS;
		_canBeCanceledAttack = false;
		setSightDistance(30);
	}
	
	@Override
	public void update(GameContainer gc, int delta)
	{
		super.update(gc, delta);
		if (_player.isDead())
			_music.stop();
		else if (Utils.sqdist(_x, _y, _player.getX(), _player.getY()) <=
				Utils.sqpw(_player.getSightDistance()) && ! _music.playing())
		{
			if (Data.game != null)
				Data.game.getMusic().fade(2000, 0, false);
			_music.loop();
		}
	}
	
	@Override
	public void die()
	{
		try
		{
			Bonus bonus = new Bonus(_map, 10);
			bonus.setPos(_x, _y);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		super.die();
	}
	
	@Override
	public void close()
	{
		if (! _player.isDead())
			Data.game.getMusic().fade(1000, 1, false);
		_music.stop();
	}
}
