package dungeoncrawler.gui;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.font.effects.ColorEffect;

public class Font
{
	static private final Color _defaultColor = Color.white;
	private UnicodeFont _font;
	private String _name;
	private int _size;
	private boolean _bold;
	private boolean _italic;
	private Color _color;
	
	public Font(String fontName, int size, boolean bold, boolean italic, Color color)
			throws SlickException
	{
		Init(fontName, size, bold, italic, color);
	}
	
	public Font(String fontName, int size, boolean bold, boolean italic)
			throws SlickException
	{
		Init(fontName, size, bold, italic, null);
	}
	
	public Font(String fontName, int size)
			throws SlickException
	{
		Init(fontName, size, false, false, null);
	}
	
	public Font(String fontName, int size, Color color)
			throws SlickException
	{
		Init(fontName, size, false, false, color);
	}
	
	@SuppressWarnings("unchecked")
	private void Init(String fontName, int size, boolean bold, boolean italic, Color color)
			throws SlickException
	{
		_name = fontName;
		_size = size;
		_bold = bold;
		_italic = italic;
		_color = (color == null ? _defaultColor : color);
		
		_font = new UnicodeFont(_name, _size, _bold, _italic);
		_font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		_font.addAsciiGlyphs();
		_font.loadGlyphs();
	}
	
	public void drawText(String text, int x, int y)
	{
		_font.drawString(x, y, text, _color);
	}
	
	public void drawText(String text, int x, int y, Color color)
	{
		_font.drawString(x, y, text, color);
	}
}
