package com.iteye.weimingtom.tgui.sf;

public class VideoMode {
	public int width;
	public int height;
	public int bitsPerPixel;
	
	public VideoMode() {
		this.width = 0;
		this.height = 0;
		this.bitsPerPixel = 0;		
	}
	
	public VideoMode(int modeWidth, int modeHeight) {
		 this(modeWidth, modeHeight, 32);
	}
	
	public VideoMode(int modeWidth, int modeHeight, int modeBitsPerPixel) {
		this.width = modeWidth;
		this.height = modeHeight;
		this.bitsPerPixel = modeBitsPerPixel;
	}
}
