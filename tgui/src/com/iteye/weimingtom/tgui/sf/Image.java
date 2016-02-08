package com.iteye.weimingtom.tgui.sf;

import java.io.IOException;

public class Image {
	private Vector2u m_size = new Vector2u();
	protected java.awt.image.BufferedImage mSurface;
	
	public Color getPixel(int x, int y) {
		int[] pixels = new int[1];
		mSurface.getRGB(x, y, 1, 1, pixels, 0, 1);
		int color = pixels[0];
		int a = (color >>> 24) & 0xff;
		int r = (color >>> 16) & 0xff;
		int g = (color >>>  8) & 0xff;
		int b = (color >>>  0) & 0xff;
		return new Color(r, g, b, a);
	}
	
	public boolean loadFromFile(String filename) {
		System.err.println("Image.loadFromFile => " + filename);
    	try {
    		mSurface = javax.imageio.ImageIO.read(new java.io.File(filename));
    		m_size.x = mSurface.getWidth();
    		m_size.y = mSurface.getHeight();
    	} catch (IOException e) {
			e.printStackTrace();
		}
		return mSurface != null;
	}
	
	public Vector2u getSize() {
	    return m_size;
	}
}
