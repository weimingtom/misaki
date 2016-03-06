package com.iteye.weimingtom.tgui.sf;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Image {
	private Vector2u m_size;
	protected java.awt.image.BufferedImage mSurface;
	
	public Image() {
		this.m_size = new Vector2u();
	}
	
	public void create(int width, int height) {
		this.create(width, height, new Color(0, 0, 0));
	}
	
    public void create(int width, int height, Color color) {
        if (width != 0 && height != 0) {
            m_size.x = width;
            m_size.y = height;

            mSurface = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    		int[] pixels = new int[width * height];
    		mSurface.getRGB(0, 0, width, height, pixels, 0, width);
    		int pixels_position = 0; 
    		while (pixels_position < pixels.length) {
    			pixels[pixels_position] = 0;
    			pixels[pixels_position] |= (color.a << 24);
    			pixels[pixels_position] |= (color.r << 16);
    			pixels[pixels_position] |= (color.g <<  8);
    			pixels[pixels_position] |= (color.b <<  0);
    			pixels_position++;
    		}
    		mSurface.setRGB(0, 0, width, height, pixels, 0, width);            
        } else {
            // Create an empty image
            m_size.x = 0;
            m_size.y = 0;
            mSurface = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
    }

    public void setPixel(int x, int y, Color color) {
        int[] pixels = new int[1];
		pixels[0] = 0;
		pixels[0] |= (color.a << 24);
		pixels[0] |= (color.r << 16);
		pixels[0] |= (color.g <<  8);
		pixels[0] |= (color.b <<  0);
		mSurface.setRGB(x, y, 1, 1, pixels, 0, 1);
    }
    
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
