package dungeoncrawler.game;

import java.io.Serializable;

public class GameSummary implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public int difficulty = 1;
	public int score = 0;
	public int finalLevel = 0;
	public int chests[] = new int[4];
	public int scoreByChests[] = new int[4];
	public int killedMobs = 0;
	public int scoreByKilledMobs = 0;
	public int killedElementals = 0;
	public int scoreByKilledElementals = 0;
	public int killedBosses = 0;
	public int scoreByKilledBosses = 0;
	public long totalGameTime = 0;
	
	public int strength = 0;
	public int health = 0;
	public float speed = 0;
	public int atkspd = 0;
	public int sightdistance = 0;
}
