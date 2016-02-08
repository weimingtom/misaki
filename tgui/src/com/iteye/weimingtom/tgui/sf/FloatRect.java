package com.iteye.weimingtom.tgui.sf;

public class FloatRect {
    public float left;
    public float top;
    public float width;  
    public float height; 
	
    public FloatRect() {
		this.left = 0;
		this.top = 0;
		this.width = 0;
		this.height = 0;
    }

    public FloatRect(float rectLeft, float rectTop, float rectWidth, float rectHeight) {
		this.left = rectLeft;
		this.top = rectTop;
		this.width = rectWidth;
		this.height = rectHeight;
    }
    
	public FloatRect(FloatRect copy) {
		this.left = copy.left;
		this.top = copy.top;
		this.width = copy.width;
		this.height = copy.height;		
	}
	
    public boolean contains(float x, float y)  {
        float minX = Math.min(left, left + width);
        float maxX = Math.max(left, left + width);
        float minY = Math.min(top, top + height);
        float maxY = Math.max(top, top + height);

        return (x >= minX) && (x < maxX) && (y >= minY) && (y < maxY);
    }
    
	public static boolean isEqual(FloatRect left, FloatRect right) {
	    return (left.left == right.left) && (left.width == right.width) &&
	    	   (left.top == right.top) && (left.height == right.height);
	}
	
	public static boolean isNotEqual(FloatRect left, FloatRect right) {
		return !isEqual(left, right);
	}
}
