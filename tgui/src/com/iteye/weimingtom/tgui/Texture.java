package com.iteye.weimingtom.tgui;

import com.iteye.weimingtom.tgui.sf.Sprite;
import com.iteye.weimingtom.tgui.sf.Vector2u;

public class Texture {
    public TextureData data = null;
    public Sprite sprite = new Sprite();

	public Texture() {
		data = null;
	}
	
    public Vector2u getSize() {
        if (data != null) {
            return data.texture.getSize();
        } else {
            return new Vector2u(0, 0);
        }
    }
    
    public boolean isTransparentPixel(int x, int y) {
        if (data.image.getPixel(
        		x + data.rect.left, 
        		y + data.rect.top)
        		.a == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Sprite getSprite() {
    	return sprite;
    }
}
