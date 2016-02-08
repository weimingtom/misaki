package com.iteye.weimingtom.tgui.sf;

import java.awt.image.BufferedImage;

import com.iteye.weimingtom.jgraphlib.Vector3d;

public class RenderWindow extends RenderTarget {
	public Window _window = null;
	
	public RenderWindow() {
		_window = new Window();
	}

	public RenderWindow(VideoMode mode, String title) {
		this(mode, title, Style.Default, new ContextSettings());
	}
	
	public RenderWindow(VideoMode mode, String title, int style, ContextSettings settings) {
		_window = new Window();
		_window.create(mode, title, style, settings);
	}
	
	public RenderWindow(RenderWindow copy) {
		
	}
	
	//FIXME:
	public Vector2u getSize() {
		return _window.getSize();
	}
	
	//FIXME: ? RenderTarget???
	public void setView(View view) {
		
	}
	
	public boolean isOpen() {
		return true;
	}
	
	//window
	public boolean pollEvent(Event event) {
		return _window.pollEvent(event);
	}
	
	public void close() {
		
	}
	
    public void clear() {
    	
    }
    
    public void display() {
    	
    }
    
    public void _draw3t(BufferedImage textureImg, 
    		Vertex v0, Vertex v1, Vertex v2) {
    	if (_window != null && textureImg != null) {
	    	Vector3d p0 = new Vector3d(v0.position.x, v0.position.y, 0);
	    	Vector3d p1 = new Vector3d(v1.position.x, v1.position.y, 0);
	    	Vector3d p2 = new Vector3d(v2.position.x, v2.position.y, 0);
	    	Vector3d s0 = new Vector3d(v0.texCoords.x, v0.texCoords.y, 0);
	    	Vector3d s1 = new Vector3d(v1.texCoords.x, v1.texCoords.y, 0);
	    	Vector3d s2 = new Vector3d(v2.texCoords.x, v2.texCoords.y, 0);
	    	_window._draw3t(textureImg, p0, p1, p2, s0, s1, s2);
    	}
    }
}
