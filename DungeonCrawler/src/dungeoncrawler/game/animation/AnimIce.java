package dungeoncrawler.game.animation;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.character.Character;
import dungeoncrawler.game.object.AObject;

public class AnimIce extends Animation
{
	int _waitTime = 10;
	int _currWait = 0;
	Character _parent;
	
	public AnimIce(Map map, Character parent) throws SlickException
	{
		super(map, Data.animation_path + "Ice2.png", 5, 4);
		_parent = parent;
		setFrames(new int[]{4, 5, 6, 7, 8, 9, 8, 7, 8, 9, 3, 2, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 9, 8, 7, 6, 5, 4});
		SoundManager.playSeveral(SoundManager.ice1);
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
		if (getFrames()[getCurrentFrame()] == 3)
			SoundManager.playSeveral(SoundManager.ice2);
		if (getFrames()[getCurrentFrame()] == 3 || getFrames()[getCurrentFrame()] == 9)
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
						{
							if (getFrames()[getCurrentFrame()] == 3)
								((Character)obj).damage(_parent.getStrength());
							((Character)obj).setCanMove(getFrames()[getCurrentFrame()] == 9);
						}
					}
				}
	}

}
