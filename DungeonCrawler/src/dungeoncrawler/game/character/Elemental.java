package dungeoncrawler.game.character;

import org.newdawn.slick.SlickException;

import dungeoncrawler.SoundManager;
import dungeoncrawler.game.Map;

public class Elemental extends Npc
{
	public Elemental(Map map, String filename) throws SlickException
	{
		super(map, filename);
		_type = e_type.ELEMENTAL;
		setSightDistance(20);
	}
	
	@Override
	public void die()
	{
		SoundManager.play(SoundManager.elemental);
		super.die();
	}
}
