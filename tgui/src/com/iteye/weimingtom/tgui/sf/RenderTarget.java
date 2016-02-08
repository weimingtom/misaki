package com.iteye.weimingtom.tgui.sf;

import java.awt.image.BufferedImage;

/**
 * All drawing here
 * @author Administrator
 *
 */
public abstract class RenderTarget {
	private class StatesCache {
		
	}
	
	private View m_defaultView = new View(); 
	private View m_view = new View();
	private StatesCache m_cache = new StatesCache(); 
	
	//RenderStates::Default
	public void draw(Drawable drawable, RenderStates states) {
		drawable.draw(this, states);
	}
	
	public abstract Vector2u getSize();
	
	public View getView() {
		return m_view;
	}
	
	public Vector2f mapPixelToCoords(Vector2i point, 
			View view) {
		return null;
	}
	
	public View getDefaultView() {
		return null;
	}
	
    public void draw(Vertex[] vertices, int vertexCount,
            PrimitiveType type, RenderStates states) {
    	//TODO: this is different from original OpenGL implementation
    	
    	//FIXME:important
    	if (states.texture != null) {
    		BufferedImage _texture = states.texture.m_texture;
    		for (int i = 0; i < vertexCount - 2; i++) {
    			Vertex[] vertexs = new Vertex[3];
    			Vertex[] vs = new Vertex[3];
    			for (int j = 0; j < 3; j++) {
    				vertexs[j] = vertices[(i * 2 + j) % vertexCount];
    			
        			vs[j] = new Vertex();
        			vs[j].position = new Vector2f(vertexs[j].position);
        			vs[j].position = Transform.multiply(states.transform, vs[j].position);
        			vs[j].color = new Color(vertexs[j].color);
        			vs[j].texCoords = new Vector2f(vertexs[j].texCoords);
        			System.out.println("vs[" + j + "].position == " + vs[j].position);
            	}
	    		_draw3t(_texture, vs[0], vs[1], vs[2]); 
	    	}
    	}
    }
    
    public abstract void _draw3t(BufferedImage textureImg, 
    		Vertex v0, Vertex v1, Vertex v2);
}
