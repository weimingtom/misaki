package com.iteye.weimingtom.tgui.sf;

public class Vector2i {
	public int x;
	public int y;
	
	public Vector2i() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i(Vector2i copy) {
		this.x = copy.x;
		this.y = copy.y;
	}
	
	public Vector2i assign(Vector2i copy) {
		this.x = copy.x;
		this.y = copy.y;
		return this;
	}
	
	public Vector2i(Vector2f copy) {
		this.x = (int)copy.x;
		this.y = (int)copy.y;
	}
	
	// operator +
	public static Vector2i plus(Vector2i left, Vector2i right) {
	    return new Vector2i(left.x + right.x, left.y + right.y);
	}
	
	// operator -
	public static Vector2i minus(Vector2i left, Vector2i right) {
	    return new Vector2i(left.x - right.x, left.y - right.y);
	}
	
	// operator /
	public static Vector2i devide(Vector2i left, int right) {
	    return new Vector2i(left.x / right, left.y / right);
	}
	
	// operator +=
	public static Vector2i plusEqual(Vector2i left, Vector2i right) {
	    left.x += right.x;
	    left.y += right.y;

	    return left;
	}
	
    public String toString() {
    	return "Vector2i : " +
    		"{x=" + this.x + 
    		",y=" + this.y +
    		"}";
    }
}
