package com.iteye.weimingtom.tgui;

import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

public abstract class Transformable {
    private Vector2f m_Position;
    private boolean m_TransformNeedUpdate;
    private Transform m_Transform;
	
	public Transformable() {
	    m_Position = new Vector2f(0, 0);
	    m_TransformNeedUpdate = true;
	    m_Transform = new Transform();
	}

	public Transformable(Transformable copy) {
	    m_Position = new Vector2f(copy.m_Position);
	    m_TransformNeedUpdate = copy.m_TransformNeedUpdate;
	    m_Transform = new Transform(copy.m_Transform);
	}
	
	public Transformable assign(Transformable right) {
		//FIXME:
	    m_Position = new Vector2f(right.m_Position);
	    m_TransformNeedUpdate = right.m_TransformNeedUpdate;
	    m_Transform = new Transform(right.m_Transform);
	    return this;
	}
	
	public void destroy() {
		
	}

	public void setPosition(float x, float y) {
        m_Position.x = (float)Math.floor(x + 0.5f);
        m_Position.y = (float)Math.floor(y + 0.5f);

        m_TransformNeedUpdate = true;		
	}

	public void setPosition(Vector2f position) {
		setPosition(position.x, position.y);
	}

	public Vector2f getPosition() {
		return m_Position;
	}

	public void move(float offsetX, float offsetY) {
		setPosition(m_Position.x + offsetX, m_Position.y + offsetY);
	}

	public void move(Vector2f offset) {
		setPosition(m_Position.x + offset.x, m_Position.y + offset.y);
	}

	public abstract void setSize(float width, float height);

	public abstract Vector2f getSize();

	public void scale(float factorX, float factorY) {
		setSize(getSize().x * factorX, getSize().y * factorY);
	}

	public void scale(Vector2f factors) {
		setSize(getSize().x * factors.x, getSize().y * factors.y);
	}

	public Transform getTransform() {
        if (m_TransformNeedUpdate) {
            m_Transform = new Transform( 1, 0, m_Position.x,
                                         0, 1, m_Position.y,
                                         0.f, 0.f, 1.f);
            m_TransformNeedUpdate = false;
        }
        return m_Transform;
	}
}
