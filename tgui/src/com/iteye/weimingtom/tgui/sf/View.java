package com.iteye.weimingtom.tgui.sf;

public class View {
	private Vector2f m_center = new Vector2f();
	private Vector2f m_size = new Vector2f();
	private float m_rotation;
	private FloatRect m_viewport = new FloatRect();
	private Transform m_transform = new Transform(); 
	private Transform m_inverseTransform = new Transform();    
	private boolean m_transformUpdated;    
	private boolean m_invTransformUpdated;
    
	public Vector2f getSize() {
		return m_size;
	}
	
	public Vector2f getCenter() {
		return m_center;
	}
}
