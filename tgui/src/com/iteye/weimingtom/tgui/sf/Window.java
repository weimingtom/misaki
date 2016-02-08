package com.iteye.weimingtom.tgui.sf;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import com.iteye.weimingtom.firetree.MainFrame;
import com.iteye.weimingtom.jgraphlib.GUtils;
import com.iteye.weimingtom.jgraphlib.Vector3d;
import com.iteye.weimingtom.tgui.Defines;

public class Window {
	private final class AWTMainFrame extends MainFrame {
		private static final long serialVersionUID = 1L;
		
		private BufferedImage bufferedImage, textureImg;
		private double[][] zbuffer;
		
		public AWTMainFrame(String title, int canvasWidth, int canvasHeight,
				int tickPerSecond) {
			super(title, canvasWidth, canvasHeight, tickPerSecond);
	        
			bufferedImage = new BufferedImage(canvasWidth + 1, canvasHeight, BufferedImage.TYPE_INT_ARGB);
            zbuffer = new double[canvasWidth + 1][canvasHeight + 1];
            for (int i = 0; i <= canvasWidth; i++) {
                Arrays.fill(zbuffer[i], -Double.MAX_VALUE);
            }
            
            try {
				textureImg = javax.imageio.ImageIO.read(new java.io.File("./examples/xubuntu_bg_aluminium.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
            
//			if (bufferedImage != null && textureImg != null) {
//				test(bufferedImage, zbuffer, textureImg);
//			}
	    }
		
		@Override
		protected void onDraw(Graphics g) {
//			g.setColor(java.awt.Color.BLACK);
//			g.fillRect(0, 0, canvasWidth, canvasHeight);
			
			Dimension size = this.getSize();
	        if (bufferedImage != null) {
	            g.drawImage(bufferedImage, 0, 0, size.width, size.height, 0, 0/*size.height*/,size.width, /*0*/size.height, null);
	        }
		}
		
		@Override
		protected void onTick() {
            // Now we let the Gui object perform its logic.
//			System.out.println("draw");
			
//          java.awt.Graphics g = this.getBufGraph();
//			g.setColor(java.awt.Color.YELLOW);
//			g.fillRect(0, 0, canvasWidth, canvasHeight);
			
			if (Defines.DEBUG_JGRAPHLIB && 
				bufferedImage != null && textureImg != null) {
				test(bufferedImage, zbuffer, textureImg);
			}
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
		}
		
		
//		@Override
//		public void keyReleased(KeyEvent e) {
//			super.keyReleased(e);
//		}
//
//		@Override
//		public void keyTyped(KeyEvent e) {
//			super.keyTyped(e);
//		}

		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			super.mouseMoved(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);
		}

		@Override
		protected void onExit() {
			super.onExit();
		}
	}
	
	private AWTMainFrame m_impl;
	private Vector2u m_size;
	
	public Window() {
		m_size = new Vector2u(0, 0);
	}
	
	public void create(VideoMode mode, String title, int style, 
		ContextSettings settings) {
		m_impl = new AWTMainFrame(title, mode.width, mode.height, 24);
		m_size.x = mode.width;
		m_size.y = mode.height;
		m_impl.start();
	}
	
	public boolean pollEvent(Event event) {
		return false;
	}
	
	public Vector2u getSize() {
	    return m_size;
	}
	
	public void test(BufferedImage img, double[][] zbuffer,
			BufferedImage textureImg) {
		//FIXME:important
    	
        Vector3d c  = new Vector3d(100, 100, 0);
        Vector3d e  = new Vector3d(0, 100, 0);
        Vector3d w  = new Vector3d(200, 100, 0);
        Vector3d n  = new Vector3d(100, 200, 0);
        Vector3d s  = new Vector3d(100, 0, 0);
        Vector3d ne = new Vector3d(50, 150, 0);
        Vector3d nw = new Vector3d(150, 150, 0);
        Vector3d se = new Vector3d(50, 50, 0);
        Vector3d sw = new Vector3d(150, 50, 0);

        GUtils.drawLine(img, c, ne, GUtils.RED);
        GUtils.drawLine(img, c, nw, GUtils.RED);
        GUtils.drawLine(img, c, se, GUtils.RED);
        GUtils.drawLine(img, c, sw, GUtils.RED);
        GUtils.drawLine(img, c, e, GUtils.GREEN);
        GUtils.drawLine(img, c, w, GUtils.GREEN);
        GUtils.drawLine(img, c, n, GUtils.BLUE);
        GUtils.drawLine(img, c, s, GUtils.BLUE);
        
        if (false)
        {
	        Vector3d p2 = new Vector3d(0, 0, 0);
	        Vector3d p1 = new Vector3d(800, 0, 0);
	        Vector3d p0 = new Vector3d(800, 600, 0);
	        
	        GUtils.drawTriangle(
	        	img, textureImg,
	            p0, p1, p2,
	            p0, p1, p2,
	            1, zbuffer
	        );
        }
        
        if (true)
        {
	        Vector3d p2 = new Vector3d(0, 0, 0);
	        Vector3d p1 = new Vector3d(800, 600, 0);
	        Vector3d p0 = new Vector3d(0, 600, 0);
	        Vector3d p3 = new Vector3d(800, 0, 0);
	        
	        GUtils.drawTriangle(
	        	img, textureImg,
	            p0, p1, p2,
	            p0, p1, p2,
	            1, zbuffer
	        );
	        
	        GUtils.drawTriangle(
	        	img, textureImg,
	            p1, p3, p2,
	            p1, p3, p2,
	            1, zbuffer
	        );
        }
	}
	
	public void _draw3t(BufferedImage textureImg, 
		Vector3d p0, Vector3d p1, Vector3d p2,
		Vector3d s0, Vector3d s1, Vector3d s2) {
		if (textureImg != null) {
	        GUtils.drawTriangle(
	        	m_impl.bufferedImage, textureImg,
	            p0, p1, p2,
	            s0, s1, s2,
	            1, m_impl.zbuffer
	        );		
		}
	}
}
