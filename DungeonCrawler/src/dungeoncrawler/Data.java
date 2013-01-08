package dungeoncrawler;

import dungeoncrawler.game.Game;
import dungeoncrawler.game.GameSummary;

public class Data
{
	static final public int screen_width = 800;
	static final public int screen_height = 600;
	static final public String data_path = ".\\data\\";
	static final public String save_path = data_path + "autosave";
	static final public String font_path = data_path + "font\\";
	static final public String img_path = data_path + "img\\";
	static final public String animation_path = img_path + "animation\\";
	static final public String character_path = img_path + "character\\";
	static final public String gui_path = img_path + "gui\\";
	static final public String tileset_path = img_path + "tileset\\";
	static final public String sound_path = data_path + "sound\\";
	static final public String music_path = sound_path + "music\\";
	static final public String effect_path = sound_path + "effect\\";
	static final public int wait_time = 1000 / 60;
	static final public int changeWorld = 3;
	
	static public Game game = null;
	static public GameSummary sum = null;
}
