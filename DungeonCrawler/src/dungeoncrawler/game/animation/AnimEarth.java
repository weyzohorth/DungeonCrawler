package dungeoncrawler.game.animation;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.character.Character;
import dungeoncrawler.game.object.AObject;

public class AnimEarth extends Animation
{
	int _waitTime = 10;
	int _currWait = 0;
	Character _parent;
	
	public AnimEarth(Map map, Character parent) throws SlickException
	{
		super(map, Data.animation_path + "Earth1.png", 5, 2);
		_parent = parent;
		SoundManager.playSeveral(SoundManager.earth1);
		setFrames(new int[]{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 4, 5, 6});
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
		if (getFrames()[getCurrentFrame()] == 2)
		{
			SoundManager.playSeveral(SoundManager.earth2);
			for (int x = -getFrameWidth() / (2 * tileW) - 1;
					x <= getFrameWidth() / (2 * tileH) + 1; ++x)
				for (int y = -getFrameHeight() / tileH - 1;
						y <= 1; ++y)
				{
					if (_x + x < 0 || _map.getWidth() <= _x + x ||
							_y + y < 0 || _map.getHeight() <= _y + y)
						continue;
					List<AObject> objs = _map.getObjects(x, y);
					if (objs == null)
						continue;
					for (AObject obj : _map.getObjects((int)_x + x, (int)_y + y))
					{
						if (obj == _parent)
							continue;
						if (obj instanceof Character)
							((Character)obj).damage(_parent.getStrength() * 2);
					}
				}
		}
	}

}
