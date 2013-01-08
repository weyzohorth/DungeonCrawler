package dungeoncrawler.game.animation;

import dungeoncrawler.game.Map;
import dungeoncrawler.game.object.AObject;

import org.newdawn.slick.SlickException;

abstract public class Animation extends AObject
{
	static public int tileW;
	static public int tileH;
	private int _frameW;
	private int _frameH;
	private int _frameXNumber;
	private int _frameYNumber;
	private int _frames[];
	private float _zoom = 1;
	private int _currFrame = 0;
	private boolean _end = false;
	
	public Animation(Map map, String filename) throws SlickException
	{
		super(map, filename);
		setLayer(Map.top_layer);
		init(4, 4);
	}
	
	public Animation(Map map, String filename, int frameXNumber, int frameYNumber)
			throws SlickException
	{
		super(map, filename);
		init(frameXNumber, frameYNumber);
	}
	
	protected void init(int frameXNumber, int frameYNumber)
	{
		setFly(true);
		_frameXNumber = frameXNumber;
		_frameYNumber = frameYNumber;
		_frameW = getWidth() / frameXNumber;
		_frameH = getHeight() / frameYNumber;
		setCenterOfRotation(_frameW / 2, _frameH / 2);
	}
	
	public void next()
	{
		_currFrame = (++_currFrame % _frames.length);
		if (_currFrame == _frames.length - 1)
			_end = true;
	}
	
	public void restart()
	{
		_currFrame = 0;
		_end = false;
	}
	public int[] getFrames() { return _frames; }
	public void setFrames(int frames[])
	{
		_frames = frames;
		_currFrame = 0;
	}
	
	public int getCurrentFrame() { return _currFrame; }
	public void setCurrentFrame(int frame) { _currFrame = frame % (_frameXNumber * _frameYNumber); }
	public boolean isEnded() { return _end; }
	
	public int getFrameWidth() { return _frameW; }
	public int getFrameHeight() { return _frameH; }

	@Override
	public void drawAt(float x, float y)
	{
		int fx = _frames[_currFrame] % _frameXNumber;
		int fy = _frames[_currFrame] / _frameXNumber;
		draw(x * tileW + (tileW - _frameW) / 2, y * tileH - _frameH / 2,
				x * tileW + _frameW * _zoom + (tileW - _frameW) / 2, y * tileH + _frameH * _zoom - _frameH / 2,
				fx * _frameW, fy * _frameH,
				(fx + 1) * _frameW, (fy + 1) * _frameH);
		
	}
	
	
	
}
