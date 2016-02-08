package com.iteye.weimingtom.tgui.sf;

public class SFTransformable {
	private Vector2f m_origin;                     
	private Vector2f m_position;   
	private float m_rotation; 
	private Vector2f m_scale;  
	private Transform m_transform;
	private boolean m_transformNeedUpdate;
	private Transform m_inverseTransform;
	private boolean m_inverseTransformNeedUpdate;
	
    public SFTransformable() {
    	m_origin = new Vector2f(0, 0);
    	m_position = new Vector2f(0, 0);
    	m_rotation = 0;
    	m_scale = new Vector2f(1, 1);
    	m_transform = new Transform();
    	m_transformNeedUpdate = true;
    	m_inverseTransform = new Transform();
    	m_inverseTransformNeedUpdate = true;
    }
    
	public void setPosition(float x, float y) {
	    m_position.x = x;
	    m_position.y = y;
	    m_transformNeedUpdate = true;
	    m_inverseTransformNeedUpdate = true;
	}
	
    public void setPosition(Vector2f position) {
        setPosition(position.x, position.y);
    }
	
	public Vector2f getPosition() {
		return m_position;
	}
	
	public void setScale(float factorX, float factorY) {
	    m_scale.x = factorX;
	    m_scale.y = factorY;
	    m_transformNeedUpdate = true;
	    m_inverseTransformNeedUpdate = true;
	}
	
	public void setScale(Vector2f factors) {
		setScale(factors.x, factors.y);
	}
	
	public Vector2f getScale() {
		return m_scale;
	}
	
	public void move(float offsetX, float offsetY) {
	    setPosition(m_position.x + offsetX, m_position.y + offsetY);
	}
	
	//===================================
	public void setRotation(float angle) {
	    m_rotation = (float)(angle % 360); //FIXME: fmod
	    if (m_rotation < 0) {
	        m_rotation += 360.f;
	    }
	    m_transformNeedUpdate = true;
	    m_inverseTransformNeedUpdate = true;
	}
	
	public float getRotation() {
	    return m_rotation;
	}

	public void setOrigin(float x, float y) {
	    m_origin.x = x;
	    m_origin.y = y;
	    m_transformNeedUpdate = true;
	    m_inverseTransformNeedUpdate = true;
	}
	
	public void setOrigin(Vector2f origin) {
	    setOrigin(origin.x, origin.y);
	}
	
	public Vector2f getOrigin() {
	    return m_origin;
	}
	
	public void move(Vector2f offset) {
	    setPosition(m_position.x + offset.x, m_position.y + offset.y);
	}
	
	public void rotate(float angle) {
	    setRotation(m_rotation + angle);
	}
	
	public void scale(float factorX, float factorY) {
	    setScale(m_scale.x * factorX, m_scale.y * factorY);
	}
	
	public void scale(Vector2f factor) {
	    setScale(m_scale.x * factor.x, m_scale.y * factor.y);
	}
	
	public Transform getTransform()	{
	    if (m_transformNeedUpdate) {
	        float angle = -m_rotation * 3.141592654f / 180.f;
	        float cosine = (float)(Math.cos(angle));
	        float sine = (float)(Math.sin(angle));
	        float sxc = m_scale.x * cosine;
	        float syc = m_scale.y * cosine;
	        float sxs = m_scale.x * sine;
	        float sys = m_scale.y * sine;
	        float tx = -m_origin.x * sxc - m_origin.y * sys + m_position.x;
	        float ty = m_origin.x * sxs - m_origin.y * syc + m_position.y;

	        m_transform.assign(new Transform( 
	        		sxc, sys, tx,
	        		-sxs, syc, ty,
	                0.f, 0.f, 1.f));
	        m_transformNeedUpdate = false;
	    }

	    return m_transform;
	}
	
	public Transform getInverseTransform() {
	    if (m_inverseTransformNeedUpdate) {
	        m_inverseTransform = getTransform().getInverse();
	        m_inverseTransformNeedUpdate = false;
	    }

	    return m_inverseTransform;
	}
}
