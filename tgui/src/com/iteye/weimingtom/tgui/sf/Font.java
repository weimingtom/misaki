package com.iteye.weimingtom.tgui.sf;

import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see http://www.cjsdn.net/Doc/JDK60/java/awt/Font.html
 * @see http://www.cjsdn.net/Doc/JDK60/java/awt/FontMetrics.html
 * @see http://tool.oschina.net/uploads/apidocs/jdk-zh/java/awt/class-use/FontMetrics.html
 * @see https://github.com/sirinath/Harmony/blob/trunk/classlib/modules/awt/src/test/impl/windows/org/apache/harmony/awt/tests/java/awt/WinFontMetricsTest.java
 * @see https://github.com/ostigter/testproject3/blob/master/CustomUI/src/org/ozsoft/customui/Utils.java
 */
public class Font {	
    public Window _window = null;
	
	private Object m_library;
	private java.awt.Font _dynamicFont;
	private java.awt.Font m_face;
	private Object m_streamRec;
	private Integer m_refCount;
	private Map<Integer, Page> m_pages = new HashMap<Integer, Page>();
	private List<Integer> m_pixelBuffer = new ArrayList<Integer>();
	
    private final static class Row {
        public Row(int rowTop, int rowHeight) {
        	this.width = 0;
        	this.top = rowTop;
        	this.height = rowHeight;
        }

        int width;  
        int top;   
        int height; 
    }
	
    private final static class Page {
        public Page() {
        	this.nextRow = 3;
        	
            Image image = new Image();
            image.create(128, 128, new Color(255, 255, 255, 0));

            // Reserve a 2x2 white square for texturing underlines
            for (int x = 0; x < 2; ++x) {
                for (int y = 0; y < 2; ++y) {
                    image.setPixel(x, y, new Color(255, 255, 255, 255));
                }
            }
            // Create the texture
            texture.loadFromImage(image);
            texture.setSmooth(true);
        }

        public Map<Integer, Glyph> glyphs = new HashMap<Integer, Glyph>(); 
        public SFTexture texture; 
        public int nextRow; 
        public List<Row> rows = new ArrayList<Row>(); 
    }
	
	public boolean loadFromFile(String filename) {
		// Cleanup the previous resources
	    cleanup();
	    m_refCount = new Integer(1);
		
        try {
        	File file = new File(filename);
            _dynamicFont = java.awt.Font.createFont(
					java.awt.Font.TRUETYPE_FONT, file);
            //Font myFont = new Font("Serif", Font.ITALIC | Font.BOLD, 12);
            //deriveFont(Font.PLAIN, 48f);
			this.m_face = _dynamicFont.deriveFont(18);
			Graphics graphics = _window._getGraphics();
			graphics.setFont(this.m_face);
	    } catch (FontFormatException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        return true;
	}
	
	//http://blog.csdn.net/zixiaomuwu/article/details/51068698
	public int getLineSpacing(int characterSize) {
		//this.m_face.getLineMetrics(str, frc);
		java.awt.Graphics graphics = _window._getGraphics();
		java.awt.Font font = this.m_face.deriveFont(characterSize);
		java.awt.FontMetrics fm = graphics.getFontMetrics(font);
		//
		//int ascent = fm.getAscent();
		//int descent = fm.getDescent();
		//
		//getHeight = leading、ascent、descent 
		return fm.getLeading();
	}
	
	public int getKerning(int first, int second, int characterSize) {
		//FIXME:
		return 0;
	}
	
	public Glyph getGlyph(int codePoint, int characterSize, boolean bold) {
		java.awt.Graphics graphics = _window._getGraphics();
		java.awt.Font font = this.m_face.deriveFont(characterSize);
		java.awt.FontMetrics fm = graphics.getFontMetrics(font);
		Rectangle2D rectangle = fm.getStringBounds(Character.toString((char)codePoint), graphics);
		
		Glyph result = new Glyph();
		result.advance = fm.getMaxAdvance(); //FIXME:
		result.bounds = new IntRect((int)rectangle.getX(), 
			(int)rectangle.getY(), 
			(int)rectangle.getWidth(), 
			(int)rectangle.getHeight());
		result.textureRect = new IntRect((int)rectangle.getX(), 
			(int)rectangle.getY(), 
			(int)rectangle.getWidth(), 
			(int)rectangle.getHeight());
		return result;
	}
	
    public SFTexture getTexture(int characterSize) {
    	return null;
    }
    
    
    private void cleanup() {
        // Check if we must destroy the FreeType pointers
        if (m_refCount != null) {
            // Decrease the reference counter
            m_refCount--;

            // Free the resources only if we are the last owner
            if (m_refCount == 0) {
                // Delete the reference counter
                //delete m_refCount;
            	m_refCount = null;
            	
                // Destroy the font face
//                if (m_face) {
//                    FT_Done_Face(static_cast<FT_Face>(m_face));
//                }
                
                // Destroy the stream rec instance, if any (must be done after FT_Done_Face!)
//                if (m_streamRec) {
//                    delete static_cast<FT_StreamRec*>(m_streamRec);
//                }
                
                // Close the library
//                if (m_library) {
//                    FT_Done_FreeType(static_cast<FT_Library>(m_library));
//                }
            }
        }

        // Reset members
        m_library = null;
        m_face = null;
        m_streamRec = null;
        m_refCount = null;
        m_pages.clear();
        m_pixelBuffer.clear();
    }

}
