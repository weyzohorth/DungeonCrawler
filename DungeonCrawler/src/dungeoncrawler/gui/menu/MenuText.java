package dungeoncrawler.gui.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import dungeoncrawler.gui.Font;

public class MenuText extends AMenuItem
{
	protected Font _font;
	protected String _text;
	
	public MenuText(Font font, String text, int x, int y)
	{
		super(x, y);
		_text = text;
		_font = font;
	}
	
	public void setFont(Font font) { _font = font; }
	public void setText(String text) { _text = text; }
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException
	{
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		_font.drawText(_text, x, y);
	}

}
