package com.iteye.weimingtom.tgui.sf;

public class Color {
    public int r; 
    public int g; 
    public int b; 
    public int a; 
    
	public Color() {
		this.r = 0;
		this.g = 0;
		this.b = 0;
		this.a = 255;
	}
	
	public Color(Color color) {
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.a = color.a;
	}
	
	public Color assign(Color color) {
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.a = color.a;
		return this;
	}

    public Color(int red, int green, int blue) {
    	this(red, green, blue, 255);
    }
	
    public Color(int red, int green, int blue, int alpha) {
    	this.r = red;
    	this.g = green;
    	this.b = blue;
    	this.a = alpha;
    }
    
    public final static Color Black = new Color(0, 0, 0);
    public final static Color White = new Color(255, 255, 255);
    
    public final static Color Red = new Color(255, 0, 0); //not used
    public final static Color Green = new Color(0, 255, 0); //not used
    public final static Color Blue = new Color(0, 0, 255); //not used
    public final static Color Yellow = new Color(255, 255, 0); //not used
    public final static Color Magenta = new Color(255, 0, 255); //not used
    public final static Color Cyan = new Color(0, 0, 255); //not used
    
    public final static Color Transparent = new Color(0, 0, 0, 0);
    
    //operator !=
    public static boolean notEqual(Color left, Color right) {
    	return !equal(left, right);
    }
    
    //operator ==
    public static boolean equal(Color left, Color right) {
    	return left.r == right.r &&
    		left.g == right.g &&
    		left.b == right.b &&
    		left.a == right.a;
    }
    
    public String toString() {
    	return "Color : " +
    		"{r=" + this.r + 
    		",g=" + this.g +
    		",b=" + this.b +
    		",a=" + this.a +
    		"}";
    }
}
