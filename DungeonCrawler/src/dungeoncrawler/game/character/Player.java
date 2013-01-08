package dungeoncrawler.game.character;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import dungeoncrawler.Data;
import dungeoncrawler.game.Map;
import dungeoncrawler.game.object.Light;

public class Player extends Character
{
	Light _light;
	
	public Player(Map map) throws SlickException
	{
		super(map, Data.character_path + "Player.png");
		_light = new Light(map);
		_light.setIntensity(0.75f);
		_sightDistance += Data.sum.sightdistance;
		setAttackSpeed(2 + Data.sum.atkspd);
		_light.setSightDistance(_sightDistance);
		_strength += Data.sum.strength;
		_healthMax += Data.sum.health;
		_health = _healthMax;
		_speed = 5 + Data.sum.speed;
	}
	
	public Player(Map map, String filename, int frameNumber) throws SlickException
	{
		super(map, filename, frameNumber);
	}
	
	@Override
	public void setSightDistance(int sight)
	{
		super.setSightDistance(sight);
		if (_light != null)
			_light.setSightDistance(_sightDistance);
	}
	
	@Override
	public void attack(int x, int y)
	{
		super.attack(x, y);
		int tile = _map.getTile(1, x, y);
		for (int id : Map.tileIdDecCols)
			if (tile == id)
			{
				_map.unsetTile(1, x, y);
				_map.removeCollision(x, y);
				return;
			}
	}
	
	@Override
	public void update(GameContainer gc, int delta)
	{
		Input input = gc.getInput();
		
		if (input.isKeyDown(Input.KEY_RIGHT))
			step(e_dir.RIGHT, _speed);
		if (input.isKeyDown(Input.KEY_LEFT))
			step(e_dir.LEFT, _speed);
		if (input.isKeyDown(Input.KEY_UP))
			step(e_dir.UP, _speed);
		if (input.isKeyDown(Input.KEY_DOWN))
			step(e_dir.DOWN, _speed);
		if (input.isKeyDown(Input.KEY_SPACE))
			attack();
		_light.fog(false);
		super.update(gc, delta);
		_light.setPos(_x, _y);
		_light.fogGradient();
	}
	
	
}
