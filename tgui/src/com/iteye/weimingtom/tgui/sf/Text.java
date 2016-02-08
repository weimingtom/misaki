package com.iteye.weimingtom.tgui.sf;

/**
 * FIXME:
 * @author Administrator
 *
 */
public class Text extends SFTransformable implements Drawable {
	private String m_string = "";
	private Font m_font = null;
	private int m_characterSize;
	private int m_style;
	private Color m_color = new Color();
	private VertexArray m_vertices = new VertexArray();      
	private FloatRect m_bounds = new FloatRect();
	
	public Text() {
		
	}
	
	public Text(Text copy) {
		
	}
	
	public Text(String string, Font font) {
    	this(string, font, 30);
    }
	
	public Text(String string, Font font, int characterSize) {
    	
    }
	
	public void setColor(Color color) {
		
	}
	
	public FloatRect getLocalBounds() {
		return m_bounds;
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		// TODO Auto-generated method stub
		
	}
	
	public String getString() {
		return m_string;
	}
	
	public void setString(String string) {
		
	}
	
	public void setCharacterSize(int size) {
		
	}
	
	public int getCharacterSize() {
		return 0;
	}
	
	public FloatRect getGlobalBounds() {
		return getTransform().transformRect(getLocalBounds());
	}
	
	public void setFont(Font font) {
		
	}
	
	public Font getFont() {
		return null;
	}
	
	public Color getColor() {
		return null;
	}
	
	//     Vector2f findCharacterPos(std::size_t index) const; 
	public Vector2f findCharacterPos(int index) {
		Vector2f position = new Vector2f(0, 0);
		return position;
	}
}
