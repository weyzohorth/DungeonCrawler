package dungeoncrawler.game.object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Utils;
import dungeoncrawler.game.Map;

public class Light extends AObject
{
	int _sightDistance = 4;
	float _intensity = 0.99f;
	boolean _gradient = true;
	
	public Light(Map map, int distance, float intensity) throws SlickException
	{
		super(map);
		_sightDistance = distance;
		_intensity = intensity;
	}
	
	public Light(Map map) throws SlickException
	{
		super(map);
	}
	
	@Override
	public void update(GameContainer gc, int delta)
	{
		if (_gradient)
			fogGradient();
		else
			fog();
	}

	@Override
	public void drawAt(float x, float y) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void setPos(float x, float y)
	{
		_x = x;
		_y = y;
	}
	
	public int getSightDistance() { return _sightDistance; }
	public void setSightDistance(int distance) { _sightDistance = distance; }
	
	public float getIntensity() { return _intensity; }
	public void setIntensity(float intensity) { _intensity = intensity; }
	
	public void fog()
	{
		fog(1 - _intensity);
	}
	
	public void fog(boolean sight)
	{
		fog(1 - _intensity, sight);
	}
	
	public void fog(float fog)
	{
		fog(fog, true);
	}
	
	public void fog(float fog, boolean sight)
	{
		for (int x = -_sightDistance; x <= _sightDistance; ++x)
			for (int y = -_sightDistance; y <= _sightDistance; ++y)
				if (Utils.sqpw(x) + Utils.sqpw(y) <= Utils.sqpw(_sightDistance))
				{
					_map.setFog(fog, (int)_x + x, (int)_y + y);
					_map.setSight(sight, (int)_x + x, (int)_y + y);
				}
	}
	
	private float gradientCalc(float actual, float min, float max)
	{
		return min * actual / max;
	}
	
	public void fogGradient()
	{
		fogGradient(1 - _intensity);
	}
	
	public void fogGradient(boolean sight)
	{
		fogGradient(1 - _intensity, sight);
	}
	
	public void fogGradient(float fogMax)
	{
		fogGradient(fogMax, true);
	}
	
	public void fogGradient(float fogMax, boolean sight)
	{
		float dist;
		float distMax = Utils.sqpw(_sightDistance);
		for (int x = -_sightDistance; x <= _sightDistance; ++x)
			for (int y = -_sightDistance; y <= _sightDistance; ++y)
			{
				dist = Utils.sqdist(x, y, 0, 0);
				if (dist <= distMax)
				{
					dist = gradientCalc(fogMax, dist, distMax);
					if (dist < _map.getFog((int)_x + x, (int)_y + y))
						_map.setFog(dist, (int)_x + x, (int)_y + y);
					_map.setSight(sight, (int)_x + x, (int)_y + y);
				}
			}
	}
}
