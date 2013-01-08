package dungeoncrawler.game.animation;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.character.Character.e_dir;

public class AnimSword extends Animation
{
	e_dir _dir;
	int _waitTime = 5;
	int _currWait = 0;
	
	public AnimSword(Map map, e_dir dir) throws SlickException
	{
		super(map, Data.animation_path + "Sword.png", 5, 5);
		SoundManager.play(SoundManager.attack);
		_dir = dir;
		if (_dir == e_dir.LEFT)
			setRotation(180);
		if (_dir == e_dir.DOWN)
			setRotation(90);
		if (_dir == e_dir.UP)
			setRotation(-90);
		setFrames(new int[]{3, 4, 5, 6});
	}

	@Override
	public void update(GameContainer gc, int delta)
	{
		if (_currWait++ < _waitTime)
			return ;
		_currWait = 0;
		if (! isEnded())
			next();
		else
			delete();
	}

}
