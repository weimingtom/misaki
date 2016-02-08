package com.iteye.weimingtom.tgui.sf;

public class Vector2f {
	public float x;
	public float y;
	
	public Vector2f() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vector2f v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vector2f assign(Vector2f copy) {
		this.x = copy.x;
		this.y = copy.y;
		return this;
	}
	
	public Vector2f(Vector2u v) {
		this.x = v.x;
		this.y = v.y;
	}

	// operator +
	public static Vector2f plus(Vector2f left, Vector2f right) {
	    return new Vector2f(left.x + right.x, left.y + right.y);
	}
	
	// operator -
	public static Vector2f minus(Vector2f left, Vector2f right) {
	    return new Vector2f(left.x - right.x, left.y - right.y);
	}
	
	// operator /
	public static Vector2f devide(Vector2f left, float right) {
	    return new Vector2f(left.x / right, left.y / right);
	}
	
	// operator +=
	public static Vector2f plusEqual(Vector2f left, Vector2f right) {
	    left.x += right.x;
	    left.y += right.y;

	    return left;
	}
	
    public String toString() {
    	return "Vector2f : " +
    		"{x=" + this.x + 
    		",y=" + this.y +
    		"}";
    }
}
