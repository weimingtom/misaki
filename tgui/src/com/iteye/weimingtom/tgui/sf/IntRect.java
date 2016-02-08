package com.iteye.weimingtom.tgui.sf;

public class IntRect {
    public int left;
    public int top;
    public int width;  
    public int height; 
    
	public IntRect() {
		this.left = 0;
		this.top = 0;
		this.width = 0;
		this.height = 0;
	}
	
	public IntRect(int rectLeft, int rectTop, int rectWidth, int rectHeight) {
		this.left = rectLeft;
		this.top = rectTop;
		this.width = rectWidth;
		this.height = rectHeight;
	}
	
	public IntRect(IntRect copy) {
		this.left = copy.left;
		this.top = copy.top;
		this.width = copy.width;
		this.height = copy.height;	
	}
	
	public IntRect assign(IntRect copy) {
		this.left = copy.left;
		this.top = copy.top;
		this.width = copy.width;
		this.height = copy.height;
		return this;
	}
	
    public boolean contains(int x, int y)  {
        int minX = Math.min(left, left + width);
        int maxX = Math.max(left, left + width);
        int minY = Math.min(top, top + height);
        int maxY = Math.max(top, top + height);

        return (x >= minX) && (x < maxX) && (y >= minY) && (y < maxY);
    }
	
	public static boolean isEqual(IntRect left, IntRect right) {
	    return (left.left == right.left) && (left.width == right.width) &&
	    	   (left.top == right.top) && (left.height == right.height);
	}
	
	public static boolean isNotEqual(IntRect left, IntRect right) {
		return !isEqual(left, right);
	}
}
