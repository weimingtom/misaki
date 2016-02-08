package com.iteye.weimingtom.tgui;

public class Borders {
    public int left;
    public int top;
    public int right;
    public int bottom;

    public Borders() {
    	this(0, 0, 0, 0);
    }
    
    public Borders(int leftBorderWidth) {
    	this(leftBorderWidth, 0, 0, 0);
    }
    
    public Borders(int leftBorderWidth,
        	int topBorderHeight) {
    	this(leftBorderWidth, topBorderHeight, 0, 0);
    }
    
    public Borders(int leftBorderWidth,
        	int topBorderHeight,
            int rightBorderWidth) {
    	this(leftBorderWidth, topBorderHeight, rightBorderWidth, 0);
    }
    
    public Borders(int leftBorderWidth,
    	int topBorderHeight,
        int rightBorderWidth,
        int bottomBorderHeight) {
    	left = leftBorderWidth;
        top = topBorderHeight;
        right = rightBorderWidth;
        bottom = bottomBorderHeight;
    }
}
