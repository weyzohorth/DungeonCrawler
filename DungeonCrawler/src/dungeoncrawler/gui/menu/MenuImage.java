package dungeoncrawler.gui.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MenuImage extends AMenuItem
{
	protected Image _img;
	protected int _imgX = 0;
	protected int _imgY = 0;
	protected int _imgWidth;
	protected int _imgHeight;
	protected int _screenWidth;
	protected int _screenHeight;
	
	public MenuImage()
	{
		super(0, 0);
	}
	
	public MenuImage(int x, int y)
	{
		super(x, y);
	}
	
	public MenuImage(String filename) throws SlickException
	{
		super(0, 0);
		setImg(filename);
	}
	
	public MenuImage(String filename, int x, int y) throws SlickException
	{
		super(x, y);
		setImg(filename);
	}
	
	public void setScreenDimension(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		setScreenDimension(width, height);
	}
	
	public void setScreenDimension(int width, int height)
	{
		_screenWidth = width;
		_screenHeight = height;
	}
	
	public void setImgLimit(int x, int y, int width, int height)
	{
		_imgX = x;
		_imgY = y;
		_imgWidth = width;
		_imgHeight = height;
		setScreenDimension(_imgWidth, _imgHeight);
	}
	
	public void setImg(String filename) throws SlickException
	{
		setImg(new Image(filename));
	}
	
	public void setImg(Image img) throws SlickException
	{
		_img = img;
		setImgLimit(0, 0, _img.getWidth(), _img.getHeight());
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException
	{
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		_img.draw(x, y, x + _screenWidth, y + _screenHeight,
				_imgX, _imgY, _imgX + _imgWidth, _imgY + _imgHeight);
	}

}
