package dungeoncrawler.gui.menu;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import dungeoncrawler.gui.Font;

public abstract class AMenuSelection extends AMenuItem
{
	private MenuText _focus = null;
	private List<MenuText> _actions = new LinkedList<MenuText>(); 
	private int _select = 0;
	private Font _normalFont;
	private Font _focusFont;
	
	public AMenuSelection(Font normal, Font focus)
	{
		super(0, 0);
		_normalFont = normal;
		_focusFont = focus;
	}

	public void addSelection(String name, int x, int y)
	{
		_actions.add(new MenuText(_normalFont, name, x, y));
		if (_focus == null)
			setFocus(_select);
	}
	
	public void setFocus(int select)
	{
		if (select < 0 || _actions.size() <= select)
			return ;
		if (_focus != null)
			_focus.setFont(_normalFont);
		_focus = _actions.get(select);
		_focus.setFont(_focusFont);
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP))
		{
			_select = (_select - 1 < 0 ? _actions.size() - 1 : _select - 1);
			setFocus(_select);
		}
		if (input.isKeyPressed(Input.KEY_S) || input.isKeyPressed(Input.KEY_DOWN))
		{
			_select = (_select + 1) % _actions.size();
			setFocus(_select);
		}
		if (input.isKeyPressed(Input.KEY_ENTER) || input.isKeyPressed(Input.KEY_SPACE))
			Do(gc, _select, delta);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		for (MenuText action : _actions)
			action.render(gc, g);
		
	}
	abstract void Do(GameContainer gc, int select, int delta) throws SlickException;
}
