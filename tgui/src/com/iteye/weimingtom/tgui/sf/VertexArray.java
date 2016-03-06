package com.iteye.weimingtom.tgui.sf;

import java.util.ArrayList;
import java.util.List;

public class VertexArray implements Drawable {
	private List<Vertex> m_vertices;
	private PrimitiveType m_primitiveType;
	
	public VertexArray() {
		this.m_vertices = new ArrayList<Vertex>();
		this.m_primitiveType = PrimitiveType.Points;
	}
	
	public VertexArray(PrimitiveType type) {
		this(type, 0);
	}
	
	public VertexArray(PrimitiveType type, int vertexCount) {
		this.m_vertices = new ArrayList<Vertex>(vertexCount);
		this.m_primitiveType = type;
	}

	public int getVertexCount() {
		return this.m_vertices.size();
	}
	
	//Vertex& operator [](unsigned int index)
	//const Vertex& operator [](unsigned int index) const;
	public Vertex get(int index) {
		return this.m_vertices.get(index);
	}
	
	public void clear() {
		this.m_vertices.clear();
	}
	
	public void resize(int vertexCount) {
		//FIXME:
	}
	
	public void append(Vertex vertex) {
		this.m_vertices.add(vertex);
	}
	
	public void setPrimitiveType(PrimitiveType type) {
		this.m_primitiveType = type;
	}
	
	public PrimitiveType getPrimitiveType() {
		return this.m_primitiveType;
	}
	
	public FloatRect getBounds() {
	    if (!m_vertices.isEmpty()) {
	        float left = this.m_vertices.get(0).position.x;
	        float top = this.m_vertices.get(0).position.y;
	        float right = this.m_vertices.get(0).position.x;
	        float bottom = this.m_vertices.get(0).position.y;

	        for (int i = 1; i < m_vertices.size(); ++i) {
	            Vector2f position = new Vector2f(m_vertices.get(i).position);
	            
	            // Update left and right
	            if (position.x < left) {
	                left = position.x;
	            } else if (position.x > right) {
	                right = position.x;
	            }
	            
	            // Update top and bottom
	            if (position.y < top) {
	                top = position.y;
	            } else if (position.y > bottom) {
	                bottom = position.y;
	            }
	        }
	        return new FloatRect(left, top, right - left, bottom - top);
	    } else {
	        // Array is empty
	        return new FloatRect();
	    }
	}
	
	@Override
	public/*private*/ void draw(RenderTarget target, RenderStates states) {
	    if (!m_vertices.isEmpty()) {
	        target.draw(m_vertices.toArray(new Vertex[m_vertices.size()]), 
	        	m_vertices.size(), m_primitiveType, states);
	    }
	}

}
