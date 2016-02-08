package com.iteye.weimingtom.tgui.sf;

public class Vector2u {
	public int x;
	public int y;
	
	public Vector2u() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2u(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2u(Vector2u copy) {
		this.x = copy.x;
		this.y = copy.y;
	}
	
	public Vector2u assign(Vector2u copy) {
		this.x = copy.x;
		this.y = copy.y;
		return this;
	}
	
	public Vector2u(Vector2f copy) {
		this.x = (int)copy.x;
		this.y = (int)copy.y;
	}
	
	// operator +
	public static Vector2u plus(Vector2u left, Vector2u right) {
	    return new Vector2u(left.x + right.x, left.y + right.y);
	}
	
	// operator -
	public static Vector2u minus(Vector2u left, Vector2u right) {
	    return new Vector2u(left.x - right.x, left.y - right.y);
	}
	
	// operator /
	public static Vector2u devide(Vector2u left, int right) {
	    return new Vector2u(left.x / right, left.y / right);
	}
	
	// operator +=
	public static Vector2u plusEqual(Vector2u left, Vector2u right) {
	    left.x += right.x;
	    left.y += right.y;

	    return left;
	}
	
    public String toString() {
    	return "Vector2u : " +
    		"{x=" + this.x + 
    		",y=" + this.y +
    		"}";
    }
}
