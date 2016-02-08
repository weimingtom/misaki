package com.iteye.weimingtom.tgui.widget;

import java.util.List;

import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.Texture;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.FloatRect;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Sprite;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20150830
 * @author Administrator
 *
 */
public class Picture extends ClickableWidget {
	public static enum PictureCallbacks {
        AllPictureCallbacks(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() - 1),
        PictureCallbacksCount(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value());
        
        int value;
        
        PictureCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }

    protected String m_LoadedFilename = "";

    protected Texture m_Texture = new Texture();
	
	public Picture() {
		this.getCallbackManager().m_Callback.widgetType = WidgetTypes.Type_Picture;
	}

	public Picture(Picture copy) {
		super(copy);
		this.m_LoadedFilename = copy.m_LoadedFilename;
		Defines.TGUI_TextureManager.copyTexture(copy.m_Texture, m_Texture);
	}

	public void destroy() {
		super.destroy();
        if (this.m_Texture.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_Texture);
        }
	}
	
	public Picture assign(Picture right) {
        if (this != right) {
            Picture temp = new Picture(right);
            super.assign(right);
            m_LoadedFilename = temp.m_LoadedFilename;
            m_Texture = temp.m_Texture;
        }
        return this;
	}

	public Picture cloneObj() {
		return new Picture(this);
	}

	public boolean load(String filename) {
        m_LoadedFilename = filename;

        m_Loaded = false;
        m_Size.x = 0;
        m_Size.y = 0;
        
        if (filename == null || filename.length() == 0) {
            return false;
        }
        
        if (m_Texture.data != null) {
            Defines.TGUI_TextureManager.removeTexture(m_Texture);
        }
        
        if (Defines.TGUI_TextureManager.getTexture(filename, m_Texture)) {
            m_Loaded = true;
            setSize((float)(m_Texture.getSize().x), 
            		(float)(m_Texture.getSize().y));
            return true;
        } else {
            return false;
        }
	}

	public String getLoadedFilename() {
		return m_LoadedFilename;
	}

	public void setPosition(float x, float y) {
		System.out.println(">>>>>Picture : setPosition => x=" + x + ", y=" + y + 
			" => " + this.m_Texture.getSprite().getTransform());
        //using Transformable::setPosition;
		super.setPosition(x, y);
        m_Texture.sprite.setPosition(x, y);		
		System.out.println("<<<<<Picture : setPosition => x=" + x + ", y=" + y + 
				" => " + this.m_Texture.getSprite().getTransform());
	}

	public void setSize(float width, float height) {
		System.out.println(">>>>>Picture : setSize => width=" + width + ", height=" + height + 
			" => " + this.m_Texture.getSprite().getTransform());
        m_Size.x = width;
        m_Size.y = height;

        if (m_Loaded) {
            m_Texture.sprite.setScale(m_Size.x / m_Texture.getSize().x, m_Size.y / m_Texture.getSize().y);
        } else {
            Defines.TGUI_OUTPUT("TGUI warning: Picture::setSize called while Picture wasn't loaded yet.");
        }
		System.out.println("<<<<<Picture : setSize => width=" + width + ", height=" + height + 
				" => " + this.m_Texture.getSprite().getTransform());
	}

	public void setSmooth(boolean smooth) {
        if (m_Loaded) {
            m_Texture.data.texture.setSmooth(smooth);
        } else {
            Defines.TGUI_OUTPUT("TGUI warning: Picture::setSmooth called while Picture wasn't loaded yet.");
        }
	}
	
	public boolean isSmooth() {
        if (m_Loaded) {
            return m_Texture.data.texture.isSmooth();
		} else {
			Defines.TGUI_OUTPUT("TGUI warning: Picture::isSmooth called while Picture wasn't loaded yet.");
            return false;
        }
	}

	public void setTransparency(int transparency) {
        //ClickableWidget::
        super.setTransparency(transparency);

        this.m_Texture.sprite.setColor(new Color(255, 255, 255, m_Opacity));		
	}

	public boolean mouseOnWidget(float x, float y) {
        if (m_Loaded == false) {
            return false;
        }
        
        if (getTransform().transformRect(
        		new FloatRect(0, 0, m_Size.x, m_Size.y))
        		.contains(x, y)) {
            Vector2f scaling = new Vector2f();
            scaling.x = m_Size.x / m_Texture.getSize().x;
            scaling.y = m_Size.y / m_Texture.getSize().y;

            if (!m_Texture.isTransparentPixel(
            		(int)((x - getPosition().x) / scaling.x), 
            		(int)((y - getPosition().y) / scaling.y))) {
                return true;
            }
        }

        if (m_MouseHover == true) {
            mouseLeftWidget();
        }

        m_MouseHover = false;
        return false;
	}

	public boolean setProperty(String property, String value) {
		property = Defines.toLower(property);

        if ("filename".equals(property)) {
            load(value);
        } else if ("smooth".equals(property)) {
            if (("true".equals(value)) || ("True".equals(value))) {
                setSmooth(true);
            } else if (("false".equals(value)) || ("False".equals(value))) {
                setSmooth(false);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Smooth' property.");
            }
        } else {
        	//ClickableWidget::
            return super.setProperty(property, value);
        }
        return true;
	}
	
	public boolean getProperty(String property, String[] value) {
        property = Defines.toLower(property);

        if ("filename".equals(property)) {
            value[0] = getLoadedFilename();
		} else if ("smooth".equals(property)) {
            value[0] = isSmooth() ? "true" : "false";
		} else {
			//ClickableWidget::
            return super.getProperty(property, value);
        }
        return true;
	}

	public List<Pair<String, String>> getPropertyList() {
		//ClickableWidget::
		List<Pair<String, String>> list = super.getPropertyList();
        list.add(new Pair<String, String>("Filename", "string"));
        list.add(new Pair<String, String>("Smooth", "bool"));
        return list;
	}

	public void draw(RenderTarget target, RenderStates states) {
		Sprite sprite = m_Texture.getSprite();
		if (Defines.DEBUG) {
			System.out.println("states.transform == " + states.transform);
			System.out.println("Picture.getTransform == " + this.getTransform());
			System.out.println("sprite.getTransform == " + sprite.getTransform());
		}
		target.draw(sprite, states);
	}
}
