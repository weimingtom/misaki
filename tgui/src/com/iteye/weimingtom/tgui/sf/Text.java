package com.iteye.weimingtom.tgui.sf;

/**
 * FIXME:
 * @author Administrator
 *
 */
public class Text extends SFTransformable implements Drawable {
	public static enum Style {
		Regular(0),
		Bold(1 << 0),
		Italic(1 << 1),
		Underlined(1 << 2);
	
        int value;
        
        Style(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
	}
	
	private String m_string;
	private Font m_font;
	private int m_characterSize;
	private int m_style;
	private Color m_color;
	private VertexArray m_vertices;      
	private FloatRect m_bounds;
	
	public Text() {
		this.m_string = "";
		this.m_font = null;
		this.m_characterSize = 30;
		this.m_style = Style.Regular.value();
		this.m_color = new Color(255, 255, 255);
		this.m_vertices = new VertexArray(PrimitiveType.Quads);
		this.m_bounds = new FloatRect();
		
		updateGeometry();
	}
	
	public Text(Text copy) {
		//FIXME:
	}
	
	public Text(String string, Font font) {
    	this(string, font, 30);
    }
	
	public Text(String string, Font font, int characterSize) {
		this.m_string = string;
		this.m_font = font;
		this.m_characterSize = characterSize;
		this.m_style = Style.Regular.value();
		this.m_color = new Color(255, 255, 255);
		this.m_vertices = new VertexArray(PrimitiveType.Quads);
		this.m_bounds = new FloatRect();
		
		updateGeometry();    	
    }
	
	public void setString(String string) {
	    this.m_string = string;
	    updateGeometry();
	}
	
	public void setFont(Font font) {
	    if (this.m_font != font) {
	        this.m_font = font;
	        updateGeometry();
	    }		
	}
	
	public void setCharacterSize(int size) {
	    if (this.m_characterSize != size) {
	    	this.m_characterSize = size;
	        updateGeometry();
	    }
	}
	
	public void setStyle(int style) {
	    if (this.m_style != style) {
	        this.m_style = style;
	        updateGeometry();
	    }
	}
	
	public void setColor(Color color) {
	    if (color != this.m_color) {
	        this.m_color = color;
	        for (int i = 0; i < m_vertices.getVertexCount(); ++i) {
	            m_vertices.get(i).color = new Color(m_color);
	        }
	    }
	}
	
	public String getString() {
		return this.m_string;
	}
	
	public Font getFont() {
		return this.m_font;
	}
	
	public int getCharacterSize() {
		return this.m_characterSize;
	}
	
	public int getStyle() {
		return this.m_style;
	}
	
	public Color getColor() {
		return this.m_color;
	}
	
	
	
	
	
	//     Vector2f findCharacterPos(std::size_t index) const; 
	public Vector2f findCharacterPos(int index) {
	    // Make sure that we have a valid font
	    if (m_font == null) {
	        return new Vector2f();
	    }
	    
	    // Adjust the index if it's out of range
	    if (index > m_string.length()) {
	        index = m_string.length();
	    }

	    // Precompute the variables needed by the algorithm
	    boolean bold = (m_style & Style.Bold.value()) != 0;
	    float hspace = (float)(m_font.getGlyph(' ', m_characterSize, bold).advance);
	    float vspace = (float)(m_font.getLineSpacing(m_characterSize));

	    // Compute the position
	    Vector2f position = new Vector2f();
	    int prevChar = 0;
	    for (int i = 0; i < index; ++i) {
	        char curChar = m_string.charAt(i);

	        // Apply the kerning offset
	        position.x += (float)(m_font.getKerning(
	        	prevChar, curChar, m_characterSize));
	        prevChar = curChar;

	        // Handle special characters
	        switch (curChar) {
            case ' ':  
            	position.x += hspace;                 
            	continue;
            
            case '\t': 
            	position.x += hspace * 4;       
            	continue;
            
            case '\n': 
            	position.y += vspace; 
            	position.x = 0; 
            	continue;
            
            case '\u000B': //'\v': 
            	position.y += vspace * 4;       
            	continue;
	        }

	        // For regular characters, add the advance offset of the glyph
	        position.x += (float)(m_font.getGlyph(
	        	curChar, m_characterSize, bold).advance);
	    }

	    // Transform the position to global coordinates
	    position = getTransform().transformPoint(position);

	    return position;
	}
	
	public FloatRect getLocalBounds() {
		return this.m_bounds;
	}
	
	public FloatRect getGlobalBounds() {
		return getTransform().transformRect(getLocalBounds());
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
	    if (m_font != null) {
	    	Transform.multiplyEqual(states.transform, getTransform());
	        states.texture = m_font.getTexture(m_characterSize);
	        target.draw(m_vertices, states);
	    }
	}
	
	private void updateGeometry() {
	    // Clear the previous geometry
	    m_vertices.clear();
	    m_bounds = new FloatRect();

	    // No font: nothing to draw
	    if (m_font == null) {
	        return;
	    }

	    // No text: nothing to draw
	    if (m_string.length() == 0) {
	        return;
	    }

	    // Compute values related to the text style
	    boolean bold = (m_style & Style.Bold.value()) != 0;
	    boolean underlined = (m_style & Style.Underlined.value()) != 0;
	    float italic = (m_style & Style.Italic.value()) != 0 ? 0.208f : 0.f; // 12 degrees
	    float underlineOffset = m_characterSize * 0.1f;
	    float underlineThickness = m_characterSize * (bold ? 0.1f : 0.07f);

	    // Precompute the variables needed by the algorithm
	    float hspace = (float)(m_font.getGlyph(' ', m_characterSize, bold).advance);
	    float vspace = (float)(m_font.getLineSpacing(m_characterSize));
	    float x = 0.f;
	    float y = (float)(m_characterSize);

	    // Create one quad for each character
	    float minY = (float)(m_characterSize);
	    int prevChar = 0;
	    for (int i = 0; i < m_string.length(); ++i) {
	        char curChar = m_string.charAt(i);

	        // Apply the kerning offset
	        x += (float)(m_font.getKerning(prevChar, curChar, m_characterSize));
	        prevChar = curChar;

	        // If we're using the underlined style and there's a new line, draw a line
	        if (underlined && (curChar == '\n')) {
	            float top = y + underlineOffset;
	            float bottom = top + underlineThickness;

	            m_vertices.append(new Vertex(new Vector2f(0, top), m_color, new Vector2f(1, 1)));
	            m_vertices.append(new Vertex(new Vector2f(x, top), m_color, new Vector2f(1, 1)));
	            m_vertices.append(new Vertex(new Vector2f(x, bottom), m_color, new Vector2f(1, 1)));
	            m_vertices.append(new Vertex(new Vector2f(0, bottom), m_color, new Vector2f(1, 1)));
	        }

	        // Handle special characters
	        switch (curChar) {
            case ' ':
                x += hspace;
                continue;

            case '\t':
                x += hspace * 4;
                continue;

            case '\n':
                if (x > m_bounds.width) {
                    m_bounds.width = x;
                }
                y += vspace;
                x = 0;
                continue;

            case '\u000B': //'\v': 
                y += vspace * 4;
                continue;
	        }

	        // Extract the current glyph's description
	        Glyph glyph = m_font.getGlyph(curChar, m_characterSize, bold);

	        int left = glyph.bounds.left;
	        int top = glyph.bounds.top;
	        int right = glyph.bounds.left + glyph.bounds.width;
	        int bottom = glyph.bounds.top  + glyph.bounds.height;

	        float u1 = (float)(glyph.textureRect.left);
	        float v1 = (float)(glyph.textureRect.top);
	        float u2 = (float)(glyph.textureRect.left + glyph.textureRect.width);
	        float v2 = (float)(glyph.textureRect.top  + glyph.textureRect.height);

	        // Add a quad for the current character
	        m_vertices.append(new Vertex(new Vector2f(x + left  - italic * top,    y + top),    m_color, new Vector2f(u1, v1)));
	        m_vertices.append(new Vertex(new Vector2f(x + right - italic * top,    y + top),    m_color, new Vector2f(u2, v1)));
	        m_vertices.append(new Vertex(new Vector2f(x + right - italic * bottom, y + bottom), m_color, new Vector2f(u2, v2)));
	        m_vertices.append(new Vertex(new Vector2f(x + left  - italic * bottom, y + bottom), m_color, new Vector2f(u1, v2)));

	        // Advance to the next character
	        x += glyph.advance;

	        // Update the minimum Y coordinate
	        if (y + top < minY) {
	            minY = y + top;
	        }
	    }

	    // If we're using the underlined style, add the last line
	    if (underlined) {
	        float top = y + underlineOffset;
	        float bottom = top + underlineThickness;

	        m_vertices.append(new Vertex(new Vector2f(0, top),    m_color, new Vector2f(1, 1)));
	        m_vertices.append(new Vertex(new Vector2f(x, top),    m_color, new Vector2f(1, 1)));
	        m_vertices.append(new Vertex(new Vector2f(x, bottom), m_color, new Vector2f(1, 1)));
	        m_vertices.append(new Vertex(new Vector2f(0, bottom), m_color, new Vector2f(1, 1)));
	    }

	    // Update the bounding rectangle
	    m_bounds.left = 0;
	    m_bounds.top = minY;
	    if (x > m_bounds.width) {
	        m_bounds.width = x;
	    }
	    m_bounds.height = y - minY;
	}
}
