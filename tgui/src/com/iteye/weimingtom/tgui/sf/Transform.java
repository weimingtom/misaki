package com.iteye.weimingtom.tgui.sf;

import java.util.Arrays;

public class Transform {
	public final static Transform Identity = new Transform();
    private float[] m_matrix = new float[16]; 
	
	public Transform() {
	    // Identity matrix
	    m_matrix[0] = 1.f; 
	    m_matrix[4] = 0.f; 
	    m_matrix[8] = 0.f; 
	    m_matrix[12] = 0.f;
	    
	    m_matrix[1] = 0.f; 
	    m_matrix[5] = 1.f; 
	    m_matrix[9] = 0.f; 
	    m_matrix[13] = 0.f;
	    
	    m_matrix[2] = 0.f; 
	    m_matrix[6] = 0.f; 
	    m_matrix[10] = 1.f; 
	    m_matrix[14] = 0.f;
	    
	    m_matrix[3] = 0.f; 
	    m_matrix[7] = 0.f; 
	    m_matrix[11] = 0.f; 
	    m_matrix[15] = 1.f;
	}
	
	public Transform(Transform copy) {
		//TODO:
		for (int i = 0; i < m_matrix.length; i++) {
			m_matrix[i] = copy.m_matrix[i];
		}
	}
	
    public Transform assign(Transform copy) {
		for (int i = 0; i < m_matrix.length; i++) {
			m_matrix[i] = copy.m_matrix[i];
		}
    	return this;
    }
	
    public Transform(float a00, float a01, float a02,
            float a10, float a11, float a12,
            float a20, float a21, float a22) {
        m_matrix[0] = a00; 
        m_matrix[4] = a01; 
        m_matrix[8] = 0.f; 
        m_matrix[12] = a02;
        
        m_matrix[1] = a10; 
        m_matrix[5] = a11; 
        m_matrix[9] = 0.f; 
        m_matrix[13] = a12;
        
        m_matrix[2] = 0.f; 
        m_matrix[6] = 0.f; 
        m_matrix[10] = 1.f; 
        m_matrix[14] = 0.f;
        
        m_matrix[3] = a20; 
        m_matrix[7] = a21; 
        m_matrix[11] = 0.f; 
        m_matrix[15] = a22;
    }
    
    public float[] getMatrix() {
        return m_matrix;
    }

    public FloatRect transformRect(FloatRect rectangle) {
        final Vector2f[] points = {
            transformPoint(rectangle.left, rectangle.top),
            transformPoint(rectangle.left, rectangle.top + rectangle.height),
            transformPoint(rectangle.left + rectangle.width, rectangle.top),
            transformPoint(rectangle.left + rectangle.width, rectangle.top + rectangle.height)
        };

        float left = points[0].x;
        float top = points[0].y;
        float right = points[0].x;
        float bottom = points[0].y;
        for (int i = 1; i < 4; ++i) {
            if (points[i].x < left) {
            	left = points[i].x;
            } else if (points[i].x > right) {
            	right = points[i].x;
            }
            if (points[i].y < top) {
            	top = points[i].y;
            } else if (points[i].y > bottom) {
            	bottom = points[i].y;
            }
        }
        return new FloatRect(left, top, right - left, bottom - top);
    }
    
    public Transform scale(float scaleX, float scaleY) {
        Transform scaling = new Transform(
        	scaleX, 0,      0,
            0,      scaleY, 0,
            0,      0,      1);

        return combine(scaling);
    }
    
    public Transform scale(Vector2f factors) {
    	return scale(factors.x, factors.y);
    }
    
    //Transform& operator *=(Transform& left, const Transform& right);
    public static Transform multiplyEqual(Transform left, Transform right) {
        return left.combine(right);
    }
    
    public Transform translate(float x, float y) {
        Transform translation = new Transform(
        		1, 0, x,
                0, 1, y,
                0, 0, 1);
        return combine(translation);
    }

    public Transform translate(Vector2f offset) {
    	return translate(offset.x, offset.y);
    }
    
    public Vector2f transformPoint(float x, float y) {
        return new Vector2f(m_matrix[0] * x + m_matrix[4] * y + m_matrix[12],
        	m_matrix[1] * x + m_matrix[5] * y + m_matrix[13]);
    }

    public Vector2f transformPoint(Vector2f point) {
    	return transformPoint(point.x, point.y);
    }
    
    public Transform rotate(float angle, float centerX, float centerY) {
        float rad = angle * 3.141592654f / 180.f;
        float cos = (float)Math.cos(rad);
        float sin = (float)Math.sin(rad);

        Transform rotation = new Transform(
        	cos, -sin, centerX * (1 - cos) + centerY * sin,
            sin,  cos, centerY * (1 - cos) - centerX * sin,
            0,    0,   1);

        return combine(rotation);
    }
    
    //========================================
    
    public Transform getInverse() {
        float det = m_matrix[0] * (m_matrix[15] * m_matrix[5] - m_matrix[7] * m_matrix[13]) -
                m_matrix[1] * (m_matrix[15] * m_matrix[4] - m_matrix[7] * m_matrix[12]) +
                m_matrix[3] * (m_matrix[13] * m_matrix[4] - m_matrix[5] * m_matrix[12]);

	    if (det != 0.f) {
	        return new Transform((m_matrix[15] * m_matrix[5] - m_matrix[7] * m_matrix[13]) / det,
	                         -(m_matrix[15] * m_matrix[4] - m_matrix[7] * m_matrix[12]) / det,
	                          (m_matrix[13] * m_matrix[4] - m_matrix[5] * m_matrix[12]) / det,
	                         -(m_matrix[15] * m_matrix[1] - m_matrix[3] * m_matrix[13]) / det,
	                          (m_matrix[15] * m_matrix[0] - m_matrix[3] * m_matrix[12]) / det,
	                         -(m_matrix[13] * m_matrix[0] - m_matrix[1] * m_matrix[12]) / det,
	                          (m_matrix[7]  * m_matrix[1] - m_matrix[3] * m_matrix[5])  / det,
	                         -(m_matrix[7]  * m_matrix[0] - m_matrix[3] * m_matrix[4])  / det,
	                          (m_matrix[5]  * m_matrix[0] - m_matrix[1] * m_matrix[4])  / det);
	    } else {
	        return new Transform(Identity);
	    }
    }
    
    public Transform combine(Transform transform) {
        float[] a = m_matrix;
        float[] b = transform.m_matrix;

        this.assign(new Transform(a[0] * b[0]  + a[4] * b[1]  + a[12] * b[3],
                          a[0] * b[4]  + a[4] * b[5]  + a[12] * b[7],
                          a[0] * b[12] + a[4] * b[13] + a[12] * b[15],
                          a[1] * b[0]  + a[5] * b[1]  + a[13] * b[3],
                          a[1] * b[4]  + a[5] * b[5]  + a[13] * b[7],
                          a[1] * b[12] + a[5] * b[13] + a[13] * b[15],
                          a[3] * b[0]  + a[7] * b[1]  + a[15] * b[3],
                          a[3] * b[4]  + a[7] * b[5]  + a[15] * b[7],
                          a[3] * b[12] + a[7] * b[13] + a[15] * b[15]));
        return this;
    }
    
    public Transform rotate(float angle) {
        float rad = angle * 3.141592654f / 180.f;
        float cos = (float)Math.cos(rad);
        float sin = (float)Math.sin(rad);

        Transform rotation = new Transform(
        	cos, -sin, 0,
            sin,  cos, 0,
            0,    0,   1);

        return combine(rotation);
    }
    
    public Transform rotate(float angle, Vector2f center) {
        return rotate(angle, center.x, center.y);
    }
    
    public Transform scale(float scaleX, float scaleY, float centerX, float centerY) {
        Transform scaling = new Transform(
        	scaleX, 0,      centerX * (1 - scaleX),
            0,      scaleY, centerY * (1 - scaleY),
            0,      0,      1);

        return combine(scaling);
    }
    
    public Transform scale(Vector2f factors, Vector2f center) {
        return scale(factors.x, factors.y, center.x, center.y);
    }
    
    //Transform& operator *(Transform& left, const Transform& right);
    public static Transform multiply(Transform left, Transform right) {
        return new Transform(left).combine(right);
    }
    
    //Vector2f operator *(const Transform& left, const Vector2f& right)
    public static Vector2f multiply(Transform left, Vector2f right) {
        return left.transformPoint(right);
    }
    
    public String toString() {
    	return "Transform : " + 
    		Arrays.toString(this.m_matrix);
    }
}
