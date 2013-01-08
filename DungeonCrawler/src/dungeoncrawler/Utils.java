package dungeoncrawler;

public class Utils
{
	static public int sqpw(int x) { return x * x; }
	static public float sqpw(float x) { return x * x; }
	
	static public int sqdist(int x1, int y1, int x2, int y2)
		{ return sqpw(x2 - x1) + sqpw(y2 - y1); }
	static public float sqdist(float x1, float y1, float x2, float y2)
		{ return sqpw(x2 - x1) + sqpw(y2 - y1); }
}
