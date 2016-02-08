package com.iteye.weimingtom.tgui.sf;

public class Sprite extends SFTransformable implements Drawable {
	private Vertex[] m_vertices; 
	private SFTexture m_texture;  
	private IntRect m_textureRect;
	
	public Sprite() {
		m_vertices = new Vertex[4];
		for (int i = 0; i < m_vertices.length; i++) {
			m_vertices[i] = new Vertex();
		}
		m_texture = null;
		m_textureRect = new IntRect();
	}
	
	public Sprite(SFTexture texture) {
		m_vertices = new Vertex[4];
		for (int i = 0; i < m_vertices.length; i++) {
			m_vertices[i] = new Vertex();
		}
		m_texture = null;
		m_textureRect = new IntRect();	    
		setTexture(texture);
	}
	
	public Sprite(SFTexture texture, IntRect rectangle) {
		m_vertices = new Vertex[4];
		for (int i = 0; i < m_vertices.length; i++) {
			m_vertices[i] = new Vertex();
		}
		m_texture = null;
		m_textureRect = new IntRect();	    
		setTexture(texture);
	    setTextureRect(rectangle);
	}
	
	public void setTexture(SFTexture texture) {
		this.setTexture(texture, false);
	}
	
	public void setTexture(SFTexture texture, boolean resetRect) {
	    if (resetRect || (m_texture == null && (IntRect.isEqual(m_textureRect, new IntRect())))) {
	        setTextureRect(new IntRect(0, 0, texture.getSize().x, texture.getSize().y));
	    }
	    m_texture = texture;
	}
	
	public void setTextureRect(IntRect rectangle) {
	    if (IntRect.isNotEqual(rectangle, m_textureRect)) {
	        m_textureRect.assign(rectangle);
	        updatePositions();
	        updateTexCoords();
	    }
	}
	
	public void setColor(Color color) {
	    m_vertices[0].color.assign(color);
	    m_vertices[1].color.assign(color);
	    m_vertices[2].color.assign(color);
	    m_vertices[3].color.assign(color);
	}
	
	public SFTexture getTexture() {
	    return m_texture;
	}

	public IntRect getTextureRect() {
		return m_textureRect;
	}
	
	public Color getColor() {
	    return m_vertices[0].color;
	}
	
	public FloatRect getLocalBounds() {
	    float width = (float)(Math.abs(m_textureRect.width));
	    float height = (float)(Math.abs(m_textureRect.height));

	    return new FloatRect(0.f, 0.f, width, height);
	}
	
	public FloatRect getGlobalBounds() {
	    return getTransform().transformRect(getLocalBounds());
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		// TODO Auto-generated method stub
	    if (m_texture != null) {
	        Transform.multiplyEqual(states.transform, getTransform());
	        states.texture = m_texture;
	        target.draw(m_vertices, 4, PrimitiveType.Quads, states);
	    }
	}
	
	private void updatePositions() {
	    FloatRect bounds = getLocalBounds();

	    m_vertices[0].position.assign(new Vector2f(0, 0));
	    m_vertices[1].position.assign(new Vector2f(0, bounds.height));
	    m_vertices[2].position.assign(new Vector2f(bounds.width, bounds.height));
	    m_vertices[3].position.assign(new Vector2f(bounds.width, 0));
	}

	private void updateTexCoords() {
	    float left = (float)(m_textureRect.left);
	    float right = left + m_textureRect.width;
	    float top = (float)(m_textureRect.top);
	    float bottom = top + m_textureRect.height;

	    m_vertices[0].texCoords.assign(new Vector2f(left, top));
	    m_vertices[1].texCoords.assign(new Vector2f(left, bottom));
	    m_vertices[2].texCoords.assign(new Vector2f(right, bottom));
	    m_vertices[3].texCoords.assign(new Vector2f(right, top));
	}
}
