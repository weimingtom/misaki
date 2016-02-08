package com.iteye.weimingtom.tgui.sf;

public class SFTexture {
	private Vector2u m_size = new Vector2u();
	private Vector2u m_actualSize = new Vector2u();
	public/*private*/ java.awt.image.BufferedImage m_texture; //FIXME:int
	private boolean m_isSmooth;
	private boolean m_isRepeated;
	private boolean m_pixelsFlipped;
	private long m_cacheId;
	
	//===================================================
	
	private static int getValidSize(int size) {
	    //ensureGlContext();

	    // Make sure that GLEW is initialized
	    //priv::ensureGlewInit();

//	    if (GLEW_ARB_texture_non_power_of_two)
//	    {
//	        // If hardware supports NPOT textures, then just return the unmodified size
//	        return size;
//	    }
//	    else
//	    {
//	        // If hardware doesn't support NPOT textures, we calculate the nearest power of two
//	        unsigned int powerOfTwo = 1;
//	        while (powerOfTwo < size)
//	            powerOfTwo *= 2;
//
//	        return powerOfTwo;
//	    }
		return size;
	}
	
	public static int getMaximumSize() {
	    //ensureGlContext();

//	    GLint size;
//	    glCheck(glGetIntegerv(GL_MAX_TEXTURE_SIZE, &size));

//	    return static_cast<unsigned int>(size);
		return Integer.MAX_VALUE;
	}
	
	
	//===================================================
	
	
	public void setSmooth(boolean smooth) {
		if (smooth != this.m_isSmooth) {
			this.m_isSmooth = smooth;
			
			//FIXME: not implemented
		}
	}
	
	public boolean isSmooth() {
		return m_isSmooth;
	}
	
	public Vector2u getSize() {
		return m_size;
	}
	
	public void setRepeated(boolean repeated) {
	    if (repeated != m_isRepeated) {
	        m_isRepeated = repeated;
	        
	        //FIXME: not implemented
	    }
	}
	
	public boolean loadFromImage(Image image) {
		return loadFromImage(image, new IntRect());
	}
	
	public boolean loadFromImage(Image image, IntRect area) {
	    int width = image.getSize().x;
	    int height = image.getSize().y;
	    
	    if (area.width == 0 || (area.height == 0) ||
	    	((area.left <= 0) && (area.top <= 0) && 
	    	 (area.width >= width) && (area.height >= height))) {
	        if (create(image.getSize().x, image.getSize().y)) {
	        	update(image);
	            return true;
	        } else {
	            return false;
	        }
	    } else {
	    	// Load a sub-area of the image
	    	
	        IntRect rectangle = new IntRect(area);
	        if (rectangle.left < 0) {
	        	rectangle.left = 0;
	        }
	        if (rectangle.top < 0) {
	        	rectangle.top = 0;
	        }
	        if (rectangle.left + rectangle.width > width) {
	        	rectangle.width = width - rectangle.left;
	        }
	        if (rectangle.top + rectangle.height > height) {
	        	rectangle.height = height - rectangle.top;
	        }

	        if (create(rectangle.width, rectangle.height)) {
	        	java.awt.Graphics g = m_texture.getGraphics();
	    		g.drawImage(image.mSurface, 
	    			0, 0, m_texture.getWidth(), m_texture.getHeight(), 
	    			0, 0, m_texture.getWidth(), m_texture.getHeight(), 
	    			null);
	    		g.dispose();
	        	return true;
	        } else {
	            return false;
	        }	    	
	    }
	}
	
	public boolean create(int width, int height) {
	    if ((width == 0) || (height == 0)) {
	        System.err.println("Failed to create texture, invalid size (" + width + "x" + height + ")");
	        return false;
	    }
	    
	    Vector2u actualSize = new Vector2u(
	    	getValidSize(width), 
	    	getValidSize(height));

	    int maxSize = getMaximumSize();
	    if ((actualSize.x > maxSize) || (actualSize.y > maxSize)) {
	        System.err.println("Failed to create texture, its internal size is too high " +
	              "(" + actualSize.x + "x" + actualSize.y + ", " +
	              "maximum is " + maxSize + "x" + maxSize + ")");
	        return false;
	    }

	    m_size.x = width;
	    m_size.y = height;
	    m_actualSize.assign(actualSize);
	    m_pixelsFlipped = false;
	    
	    m_texture = new java.awt.image.BufferedImage(
				width, height, 
				java.awt.image.BufferedImage.TYPE_INT_ARGB);
	    return true;
	}
	
	public void update(Image image) {
		java.awt.Graphics g = m_texture.getGraphics();
		g.drawImage(image.mSurface, 
			0, 0, m_texture.getWidth(), m_texture.getHeight(), 
			0, 0, image.mSurface.getWidth(), image.mSurface.getHeight(), 
			null);
		g.dispose();
	}
}
