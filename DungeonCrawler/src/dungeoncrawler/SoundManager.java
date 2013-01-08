package dungeoncrawler;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundManager
{
	static public final float volume = 0.5f;
	static public Sound attack = null;
	static public Sound earth1 = null;
	static public Sound earth2 = null;
	static public Sound fire1 = null;
	static public Sound fire2 = null;
	static public Sound ice1 = null;
	static public Sound ice2 = null;
	static public Sound water = null;
	
	static public Sound bonus = null;
	static public Sound chest = null;
	
	static public Sound bat = null;
	static public Sound batracian = null;
	static public Sound bossearth = null;
	static public Sound bossfire = null;
	static public Sound bossice = null;
	static public Sound bosswater = null;
	static public Sound crawler = null;
	static public Sound elemental = null;
	static public Sound firedog = null;
	static public Sound goblin = null;
	static public Sound littledevil = null;
	static public Sound livingwater = null;
	static public Sound lizard = null;
	static public Sound medusa = null;
	static public Sound scorpion = null;
	static public Sound skeleton = null;
	static public Sound zombie = null;
	
	public SoundManager() throws SlickException
	{
		if (SoundManager.attack == null)
		{
			attack = new Sound(Data.effect_path + "Attack.ogg");
			earth1 = new Sound(Data.effect_path + "Earth1.ogg");
			earth2 = new Sound(Data.effect_path + "Earth2.ogg");
			fire1 = new Sound(Data.effect_path + "Fire1.ogg");
			fire2 = new Sound(Data.effect_path + "Fire2.ogg");
			ice1 = new Sound(Data.effect_path + "Ice1.ogg");
			ice2 = new Sound(Data.effect_path + "Ice2.ogg");
			water = new Sound(Data.effect_path + "Water.ogg");
			
			bonus = new Sound(Data.effect_path + "Bonus.ogg");
			chest = new Sound(Data.effect_path + "Chest.ogg");
			
			bat = new Sound(Data.effect_path + "Monster3.ogg");
			batracian = new Sound(Data.effect_path + "Frog.ogg");
			bossearth = new Sound(Data.effect_path + "BossEarth.ogg");
			bossfire = new Sound(Data.effect_path + "BossFire.ogg");
			bossice = new Sound(Data.effect_path + "BossIce.ogg");
			bosswater = new Sound(Data.effect_path + "BossWater.ogg");
			crawler = new Sound(Data.effect_path + "Crawler.ogg");
			elemental = new Sound(Data.effect_path + "Elemental.ogg");
			firedog = new Sound(Data.effect_path + "Monster8.ogg");
			goblin = new Sound(Data.effect_path + "Monster2.ogg");
			littledevil = new Sound(Data.effect_path + "Monster7.ogg");
			livingwater = new Sound(Data.effect_path + "LivingWater.ogg");
			lizard = new Sound(Data.effect_path + "Monster4.ogg");
			medusa = new Sound(Data.effect_path + "Monster5.ogg");
			scorpion = new Sound(Data.effect_path + "Scorpion.ogg");
			skeleton = new Sound(Data.effect_path + "Monster6.ogg");
			zombie = new Sound(Data.effect_path + "Monster1.ogg");
		}
	}
	
	static public void play(Sound sound)
	{
		if (! sound.playing())
			playSeveral(sound);
	}
	
	static public void playSeveral(Sound sound)
	{
		sound.play(1, SoundManager.volume);
	}
}
