package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.Borders;
import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.GL;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.Texture;
import com.iteye.weimingtom.tgui.WidgetBorders;
import com.iteye.weimingtom.tgui.WidgetBordersImpl;
import com.iteye.weimingtom.tgui.WidgetPhase;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.FloatRect;
import com.iteye.weimingtom.tgui.sf.IntRect;
import com.iteye.weimingtom.tgui.sf.RectangleShape;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.SFTexture;
import com.iteye.weimingtom.tgui.sf.Sprite;
import com.iteye.weimingtom.tgui.sf.Text;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20150920
 * @author Administrator
 *
 */
public class ChildWindow extends Container implements WidgetBorders {
	public WidgetBordersImpl _WidgetBordersImpl = new WidgetBordersImpl();
    public static enum ChildWindowCallbacks {
	    LeftMousePressed(WidgetCallbacks.WidgetCallbacksCount.value() * 1),
	    Closed(WidgetCallbacks.WidgetCallbacksCount.value() * 2),
	    Moved(WidgetCallbacks.WidgetCallbacksCount.value() * 4),
	    //Resized(WidgetCallbacks.WidgetCallbacksCount.value() * 8),
	    AllChildWindowCallbacks(WidgetCallbacks.WidgetCallbacksCount.value() * 16 - 1),
	    ChildWindowCallbacksCount(WidgetCallbacks.WidgetCallbacksCount.value() * 16);
	    
        int value;
        
        ChildWindowCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
	}
	
	public static enum TitleAlignment {
	    TitleAlignmentLeft,
	    TitleAlignmentCentered,
	    TitleAlignmentRight
	}
	
	protected String m_LoadedConfigFile;
	
	protected Vector2f m_Size;

	protected Color m_BackgroundColor;
	protected SFTexture m_BackgroundTexture;
	protected Sprite m_BackgroundSprite;

	protected Texture m_IconTexture;

	protected Text m_TitleText;
	protected int m_TitleBarHeight;
	protected boolean m_SplitImage;
	protected Vector2f m_DraggingPosition;
	protected int m_DistanceToSide;
	protected TitleAlignment m_TitleAlignment;
	protected Color m_BorderColor;

	protected Texture m_TextureTitleBar_L;
	protected Texture m_TextureTitleBar_M;
	protected Texture m_TextureTitleBar_R;

	protected Button m_CloseButton;

	protected boolean m_KeepInParent;
	
	public ChildWindow() {
		super();
	    m_Size = new Vector2f(200, 150);
	    m_BackgroundTexture = null;
	    m_TitleBarHeight = 0;
	    m_SplitImage = false;
	    m_DraggingPosition = new Vector2f(0, 0);
	    m_DistanceToSide = 5;
	    m_TitleAlignment = TitleAlignment.TitleAlignmentCentered;
	    m_BorderColor = new Color(0, 0, 0);
	    m_KeepInParent = false;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_ChildWindow;
        m_CloseButton = new Button();
	}
	
	public ChildWindow(ChildWindow childWindowToCopy) {
	    super(childWindowToCopy);
	    _WidgetBordersImpl = new WidgetBordersImpl(childWindowToCopy._WidgetBordersImpl);
	    m_LoadedConfigFile = childWindowToCopy.m_LoadedConfigFile;
	    m_Size = childWindowToCopy.m_Size;
	    m_BackgroundColor = childWindowToCopy.m_BackgroundColor;
	    m_BackgroundTexture = childWindowToCopy.m_BackgroundTexture;
	    m_TitleText = childWindowToCopy.m_TitleText;
	    m_TitleBarHeight = childWindowToCopy.m_TitleBarHeight;
	    m_SplitImage = childWindowToCopy.m_SplitImage;
	    m_DraggingPosition = childWindowToCopy.m_DraggingPosition;
	    m_DistanceToSide = childWindowToCopy.m_DistanceToSide;
	    m_TitleAlignment = childWindowToCopy.m_TitleAlignment;
	    m_BorderColor = childWindowToCopy.m_BorderColor;
	    m_KeepInParent = childWindowToCopy.m_KeepInParent;
	    
        Defines.TGUI_TextureManager.copyTexture(childWindowToCopy.m_IconTexture, m_IconTexture);
        Defines.TGUI_TextureManager.copyTexture(childWindowToCopy.m_TextureTitleBar_L, m_TextureTitleBar_L);
        Defines.TGUI_TextureManager.copyTexture(childWindowToCopy.m_TextureTitleBar_M, m_TextureTitleBar_M);
        Defines.TGUI_TextureManager.copyTexture(childWindowToCopy.m_TextureTitleBar_R, m_TextureTitleBar_R);

        m_CloseButton = new Button(childWindowToCopy.m_CloseButton);
        
        if (childWindowToCopy.m_BackgroundTexture != null) {
            m_BackgroundSprite.setTexture(m_BackgroundTexture, true);
            m_BackgroundSprite.setScale(
            	m_Size.x / m_BackgroundTexture.getSize().x, 
            	m_Size.y / m_BackgroundTexture.getSize().y);
            m_BackgroundSprite.setColor(
            	new Color(255, 255, 255, m_Opacity));
        }
	}
	
	public void destroy() {
		super.destroy();
        if (m_TextureTitleBar_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTitleBar_L);
        }
        if (m_TextureTitleBar_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTitleBar_M);
        }
        if (m_TextureTitleBar_R.data != null) {  
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTitleBar_R);
        }

        if (m_IconTexture.data != null) {
            Defines.TGUI_TextureManager.removeTexture(m_IconTexture);
        }
        
        if (m_IconTexture.data != null) {
            Defines.TGUI_TextureManager.removeTexture(m_IconTexture);
        }
        
        m_CloseButton.destroy();
        m_CloseButton = null;
	}

	public ChildWindow assign(ChildWindow right) {
        if (this != right) {
            ChildWindow temp = new ChildWindow(right);
            //this.Container::operator=
            super.assign(right);
            //this.WidgetBorders::operator=(right);

            // Delete the old close button
            m_CloseButton.destroy();
            m_CloseButton = null;
            
            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_Size = temp.m_Size;
            m_BackgroundColor = temp.m_BackgroundColor;
            m_BackgroundTexture = temp.m_BackgroundTexture;
            m_BackgroundSprite = temp.m_BackgroundSprite;
            m_IconTexture = temp.m_IconTexture;
            m_TitleText = temp.m_TitleText;
            m_TitleBarHeight = temp.m_TitleBarHeight;
            m_SplitImage = temp.m_SplitImage;
            m_DraggingPosition = temp.m_DraggingPosition;
            m_DistanceToSide = temp.m_DistanceToSide;
            m_TitleAlignment = temp.m_TitleAlignment;
            m_BorderColor = temp.m_BorderColor;
            m_TextureTitleBar_L = temp.m_TextureTitleBar_L;
            m_TextureTitleBar_M = temp.m_TextureTitleBar_M;
            m_TextureTitleBar_R = temp.m_TextureTitleBar_R;
            m_CloseButton = temp.m_CloseButton;
            m_KeepInParent = temp.m_KeepInParent;
        }

        return this;
	}

	public ChildWindow cloneObj() {
        return new ChildWindow(this);
	}
	
	public boolean load(String configFileFilename) {
        m_LoadedConfigFile = configFileFilename;

        m_Loaded = false;

        if (m_TextureTitleBar_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTitleBar_L);
        }
        if (m_TextureTitleBar_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTitleBar_M);
        }
        if (m_TextureTitleBar_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTitleBar_R);
        }
        if (m_CloseButton.m_TextureNormal_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_CloseButton.m_TextureNormal_M);
        }
        if (m_CloseButton.m_TextureHover_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_CloseButton.m_TextureHover_M);
        }
        if (m_CloseButton.m_TextureDown_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_CloseButton.m_TextureDown_M);
        }

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("ChildWindow", properties, values)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to parse " + configFileFilename + ".");
            return false;
        }

        configFile.close();

        String configFileFolder = "";
        int slashPos = configFileFilename.lastIndexOf("/");
        if (slashPos == -1) {
        	slashPos = configFileFilename.lastIndexOf("\\"); 
        }
        if (slashPos != -1) {
            configFileFolder = configFileFilename.substring(0, slashPos + 1);
        }
        
        for (int i = 0; i < properties.size(); ++i) {
            String property = properties.get(i);
            String value = values.get(i);

            if ("backgroundcolor".equals(property)) {
                setBackgroundColor(configFile.readColor(value));
            } else if ("titlecolor".equals(property)) {
                setTitleColor(configFile.readColor(value));
            } else if ("bordercolor".equals(property)) {
                setBorderColor(configFile.readColor(value));
            } else if ("titlebarimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTitleBar_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TitlebarImage in section ChildWindow in " + configFileFilename + ".");
                    return false;
                }
                m_SplitImage = false;
            } else if ("titlebarimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTitleBar_L)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TitlebarImage_L in section ChildWindow in " + configFileFilename + ".");
                    return false;
                }
            } else if ("titlebarimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTitleBar_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TitlebarImage_M in section ChildWindow in " + configFileFilename + ".");
                    return false;
                }

                m_SplitImage = true;
            } else if ("titlebarimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTitleBar_R)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TitlebarImage_R in section ChildWindow in " + configFileFilename + ".");
                    return false;
                }
            } else if ("closebuttonseparatehoverimage".equals(property)) {
                m_CloseButton.m_SeparateHoverImage = configFile.readBool(value, false);
            } else if ("closebuttonnormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_CloseButton.m_TextureNormal_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for CloseButtonNormalImage in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("closebuttonhoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_CloseButton.m_TextureHover_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for CloseButtonHoverImage in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("closebuttondownimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_CloseButton.m_TextureDown_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for CloseButtonDownImage in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("borders".equals(property)) {
                Borders borders = new Borders();
                if (Defines.extractBorders(value, borders)) {
                    setBorders(borders.left, borders.top, borders.right, borders.bottom);
                }
            } else if (property == "distancetoside") {
                setDistanceToSide((int)(Integer.parseInt(value.trim())));
            } else {
            	Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section ChildWindow in " + configFileFilename + ".");
            }
        }

        if (m_CloseButton.m_TextureNormal_M.data != null) {
            if (m_CloseButton.m_TextureHover_M.data != null) {
                m_CloseButton.m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();
            }
            if (m_CloseButton.m_TextureDown_M.data != null) {
                m_CloseButton.m_WidgetPhase |= WidgetPhase.WidgetPhase_MouseDown.value();
            }

            m_CloseButton.m_Size = new Vector2f(m_CloseButton.m_TextureNormal_M.getSize());
            m_CloseButton.m_Loaded = true;
        } else {
            Defines.TGUI_OUTPUT("TGUI error: Missing a CloseButtonNormalImage property in section ChildWindow in " + configFileFilename + ".");
            return false;
        }

        if (m_SplitImage) {
            if ((m_TextureTitleBar_L.data != null) && 
            	(m_TextureTitleBar_M.data != null) && 
            	(m_TextureTitleBar_R.data != null)) {
                m_TitleBarHeight = m_TextureTitleBar_M.getSize().y;

                m_TextureTitleBar_M.data.texture.setRepeated(true);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the child window. Is the ChildWindow section in " + configFileFilename + " complete?");
                return false;
            }
        } else {
            if (m_TextureTitleBar_M.data != null) {
                m_TitleBarHeight = m_TextureTitleBar_M.getSize().y;
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the child window. Is the ChildWindow section in " + configFileFilename + " complete?");
                return false;
            }
        }

        m_TitleText.setCharacterSize(m_TitleBarHeight * 8 / 10);

        return m_Loaded = true;
	}
	
	public String getLoadedConfigFile() {
		return m_LoadedConfigFile;
	}
	
	public void setSize(float width, float height) {
	    if (width < 0) {
	    	width = -width;
	    }
        if (height < 0) {
        	height = -height;
        }

        m_Size.x = width;
        m_Size.y = height;

        if (m_SplitImage) {
            float scalingY = (float)(m_TitleBarHeight) / m_TextureTitleBar_M.getSize().y;
            float minimumWidth = ((m_TextureTitleBar_L.getSize().x + m_TextureTitleBar_R.getSize().x) * scalingY);
            
            if (m_Size.x < minimumWidth + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder) {
                m_Size.x = minimumWidth + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder;
            }

            m_TextureTitleBar_L.sprite.setScale(scalingY, scalingY);
            m_TextureTitleBar_M.sprite.setScale(scalingY, scalingY);
            m_TextureTitleBar_R.sprite.setScale(scalingY, scalingY);

            m_TextureTitleBar_M.sprite.setTextureRect(
            	new IntRect(0, 0, (int)(((m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder) - minimumWidth) / scalingY), m_TextureTitleBar_M.getSize().y));
        } else  {
            m_TextureTitleBar_M.sprite.setScale(
            	(m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder) / m_TextureTitleBar_M.getSize().x,
                (float)(m_TitleBarHeight) / m_TextureTitleBar_M.getSize().y);
        }

        if (m_BackgroundTexture != null) {
            m_BackgroundSprite.setScale(
            	m_Size.x / m_BackgroundTexture.getSize().x, 
            	m_Size.y / m_BackgroundTexture.getSize().y);
        }
	}
	
	public Vector2f getSize() {
		return new Vector2f(m_Size.x, m_Size.y);
	}
	
	public void setBackgroundTexture(SFTexture texture) {
        m_BackgroundTexture = texture;

        if (m_BackgroundTexture != null) {
            m_BackgroundSprite.setTexture(m_BackgroundTexture, true);
            m_BackgroundSprite.setScale(
            	m_Size.x / m_BackgroundTexture.getSize().x, 
            	m_Size.y / m_BackgroundTexture.getSize().y);
        }
	}
	
	public SFTexture getBackgroundTexture() {
		return m_BackgroundTexture;
	}
	
	public void setTitleBarHeight(int height) {
	    if (m_Loaded == false) {
            return;
	    }

        m_TitleBarHeight = height;

        m_CloseButton.setSize(
        	(float)(height) / m_TextureTitleBar_M.getSize().y * m_CloseButton.m_TextureNormal_M.getSize().x,
            (float)(height) / m_TextureTitleBar_M.getSize().y * m_CloseButton.m_TextureNormal_M.getSize().y);

        m_TitleText.setCharacterSize(m_TitleBarHeight * 8 / 10);

        if (m_SplitImage) {
            float scalingY = (float)(m_TitleBarHeight) / m_TextureTitleBar_M.getSize().y;
            float minimumWidth = ((m_TextureTitleBar_L.getSize().x + m_TextureTitleBar_R.getSize().x) * scalingY);

            if (m_Size.x < minimumWidth + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder) {
                m_Size.x = minimumWidth + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder;
            }

            m_TextureTitleBar_L.sprite.setScale(scalingY, scalingY);
            m_TextureTitleBar_M.sprite.setScale(scalingY, scalingY);
            m_TextureTitleBar_R.sprite.setScale(scalingY, scalingY);

            m_TextureTitleBar_M.sprite.setTextureRect(
            	new IntRect(0, 0, 
            		(int)(((m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder) - minimumWidth) / scalingY), 
            		m_TextureTitleBar_M.getSize().y));
        } else {
            m_TextureTitleBar_M.sprite.setScale(
            	(m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder) / m_TextureTitleBar_M.getSize().x,
                (float)(m_TitleBarHeight) / m_TextureTitleBar_M.getSize().y);
        }
	}
	
	public int getTitleBarHeight() {
		return m_TitleBarHeight;
	}

	public void setBackgroundColor(Color backgroundColor) {
		m_BackgroundColor = backgroundColor;
	}
	
	public Color getBackgroundColor() {
		return m_BackgroundColor;
	}
	
	public void setTransparency(int transparency) {
        //Container::
        super.setTransparency(transparency);

        m_BackgroundSprite.setColor(new Color(255, 255, 255, m_Opacity));

        m_IconTexture.sprite.setColor(new Color(255, 255, 255, m_Opacity));

        m_TextureTitleBar_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureTitleBar_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureTitleBar_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));

        m_CloseButton.setTransparency(m_Opacity);
	}
	
	public void setTitle(String title) {
		m_TitleText.setString(title);
	}
	
	public String getTitle() {
		return m_TitleText.getString();
	}
	
	public void setTitleColor(Color color) {
		m_TitleText.setColor(color);
	}
	
	public Color getTitleColor() {
		return m_TitleText.getColor();
	}
	
	public void setBorderColor(Color borderColor) {
		m_BorderColor = borderColor;
	}
	
	public Color getBorderColor() {
		return m_BorderColor;
	}
	
	public void setDistanceToSide(int distanceToSide) {
		m_DistanceToSide = distanceToSide;
	}
	
	public int getDistanceToSide() {
		return m_DistanceToSide;
	}
	
	public void setTitleAlignment(TitleAlignment alignment) {
		m_TitleAlignment = alignment;
	}
	
	public TitleAlignment getTitleAlignment() {
		return m_TitleAlignment;
	}
	
	public void setIcon(String filename) {
	    if (m_IconTexture.data != null) {
            Defines.TGUI_TextureManager.removeTexture(m_IconTexture);
        }
        if (Defines.TGUI_TextureManager.getTexture(filename, m_IconTexture)) {
            m_IconTexture.sprite.setScale(
            	(float)(m_TitleBarHeight) / m_TextureTitleBar_M.getSize().y,
                (float)(m_TitleBarHeight) / m_TextureTitleBar_M.getSize().y);
        } else {
            Defines.TGUI_OUTPUT("Failed to load \"" + filename + "\" as icon for the ChildWindow");
        }
	}
	
	public void removeIcon() {
        if (m_IconTexture.data != null) {
            Defines.TGUI_TextureManager.removeTexture(m_IconTexture);
        }
	}
	
	//FIXME:
	public void destroy_() {
		m_Parent.remove(this);
	}
	
	public void keepInParent(boolean enabled) {
		m_KeepInParent = enabled;
	}
	
	public boolean isKeptInParent() {
		return m_KeepInParent;
	}
	
	public void setPosition(float x, float y) {
       if (m_KeepInParent) {
            if (y < 0) {
                //Transformable::
                super.setPosition(getPosition().x, 0);
        	} else if (y > m_Parent.getSize().y - m_TitleBarHeight) {
                //Transformable::
                super.setPosition(getPosition().x, m_Parent.getSize().y - m_TitleBarHeight);
        	} else {
                //Transformable::
                super.setPosition(getPosition().x, y);
            }
            
            if (x < 0) {
                //Transformable::
                super.setPosition(0, getPosition().y);
        	} else if (x > m_Parent.getSize().x - getSize().x) {
                //Transformable::
                super.setPosition(m_Parent.getSize().x - getSize().x, getPosition().y);
        	} else {
                //Transformable::
                super.setPosition(x, getPosition().y);
            }
        } else {
            //Transformable::
        	super.setPosition(x, y);
        }
	}
	
	//using Transformable::setPosition;
	
	public void leftMousePressed(float x, float y) {
	    m_Parent.moveWidgetToFront(this);

        if (this.getCallbackManager().m_CallbackFunctions.get(ChildWindowCallbacks.LeftMousePressed.value()).isEmpty() == false) {
            this.getCallbackManager().m_Callback.trigger = ChildWindowCallbacks.LeftMousePressed.value();
            this.getCallbackManager().m_Callback.mouse.x = (int)(x - getPosition().x);
            this.getCallbackManager().m_Callback.mouse.y = (int)(y - getPosition().y);
            addCallback();
        }

        if (getTransform().transformRect(
        	new FloatRect(0, 0, 
        		m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder, 
        		(float)(m_TitleBarHeight)))
        		.contains(x, y)) {
            Vector2f position = getPosition();

            // Temporary set the close button to the correct position
            m_CloseButton.setPosition(position.x + ((m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder - m_DistanceToSide - m_CloseButton.getSize().x)), position.y + ((m_TitleBarHeight / 2.f) - (m_CloseButton.getSize().x / 2.f)));

            if (m_CloseButton.mouseOnWidget(x, y)) {
                m_CloseButton.leftMousePressed(x, y);
            } else {
                m_MouseDown = true;

                m_DraggingPosition.x = x - position.x;
                m_DraggingPosition.y = y - position.y;
            }

            m_CloseButton.setPosition(0, 0);
            return;
        } else {
            if (m_CloseButton.m_MouseHover)
                m_CloseButton.mouseNotOnWidget();

            // Check if the mouse is on top of the borders
            if ((getTransform().transformRect(
            		new FloatRect(0, 0, m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder, m_Size.y + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder + m_TitleBarHeight))
            			.contains(x, y)) && 
            	(getTransform().transformRect(
            		new FloatRect(
            			(float)(_WidgetBordersImpl.m_LeftBorder), 
            			(float)(m_TitleBarHeight + _WidgetBordersImpl.m_TopBorder), 
            			m_Size.x, 
            			m_Size.y))
            			.contains(x, y) == false)) {
                return;
            }
        }

        //Container::
        super.leftMousePressed(
        	x - _WidgetBordersImpl.m_LeftBorder, 
        	y - (m_TitleBarHeight + _WidgetBordersImpl.m_TopBorder));
	}
	
	public void leftMouseReleased(float x, float y) {
	       // Check if the mouse is on top of the title bar
        if (getTransform().transformRect(
        	new FloatRect(0, 0, 
        		m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder, 
        		(float)(m_TitleBarHeight)))
        		.contains(x, y)) {
            Vector2f position = getPosition();

            m_CloseButton.setPosition(
            	position.x + ((m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder - m_DistanceToSide - m_CloseButton.getSize().x)), 
            	position.y + ((m_TitleBarHeight / 2.f) - (m_CloseButton.getSize().x / 2.f)));

            m_MouseDown = false;

            if (m_CloseButton.m_MouseDown == true) {
                m_CloseButton.m_MouseDown = false;

                if (m_CloseButton.mouseOnWidget(x, y)) {
                    if (this.getCallbackManager().m_CallbackFunctions.get(ChildWindowCallbacks.Closed.value()).isEmpty() == false) {
                        this.getCallbackManager().m_Callback.trigger = ChildWindowCallbacks.Closed.value();
                        addCallback();
                    } else {
                        destroy_();
                        return;
                    }
                }
            }

            for (int i = 0; i < m_Widgets.size(); ++i) {
                m_Widgets.get(i).get().mouseNoLongerDown();
            }
            m_CloseButton.setPosition(0, 0);
            return;
        } else {
            if (m_CloseButton.m_MouseHover) {
                m_CloseButton.mouseNotOnWidget();
            }
            
            m_MouseDown = false;
            m_CloseButton.mouseNoLongerDown();

            if ((getTransform().transformRect(
            		new FloatRect(0, 0, 
            			m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder, 
            			m_Size.y + _WidgetBordersImpl.m_TopBorder + _WidgetBordersImpl.m_BottomBorder + m_TitleBarHeight))
            			.contains(x, y)) &&  
            	(getTransform().transformRect(
            		new FloatRect(
            			(float)(_WidgetBordersImpl.m_LeftBorder), 
            			(float)(m_TitleBarHeight + _WidgetBordersImpl.m_TopBorder), 
            			m_Size.x, m_Size.y))
            			.contains(x, y) == false)) {
                for (int i = 0; i < m_Widgets.size(); ++i) {
                    m_Widgets.get(i).get().mouseNoLongerDown();
                }
                return;
            }
        }

        //Container::
        super.leftMouseReleased(
        	x - _WidgetBordersImpl.m_LeftBorder, 
        	y - (m_TitleBarHeight + _WidgetBordersImpl.m_TopBorder));
	}
	
	public void mouseMoved(float x, float y) {
		m_MouseHover = true;

        if (m_MouseDown == true) {
            Vector2f position = getPosition();
            setPosition(position.x + (x - position.x - m_DraggingPosition.x), position.y + (y - position.y - m_DraggingPosition.y));

            if (this.getCallbackManager().m_CallbackFunctions.get(ChildWindowCallbacks.Moved.value()).isEmpty() == false) {
                this.getCallbackManager().m_Callback.trigger = ChildWindowCallbacks.Moved.value();
                this.getCallbackManager().m_Callback.position = getPosition();
                addCallback();
            }
        }

        if (getTransform().transformRect(new FloatRect(0, 0, m_Size.x + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder, (float)(m_TitleBarHeight))).contains(x, y)) {
            Vector2f position = getPosition();

            m_CloseButton.setPosition(position.x + ((m_Size.x + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder - m_DistanceToSide - m_CloseButton.getSize().x)), position.y + ((m_TitleBarHeight / 2.f) - (m_CloseButton.getSize().x / 2.f)));

            if (m_CloseButton.mouseOnWidget(x, y)) {
                m_CloseButton.mouseMoved(x, y);
            }

            m_CloseButton.setPosition(0, 0);
            return;
        } else {
            if (m_CloseButton.m_MouseHover) {
                m_CloseButton.mouseNotOnWidget();
            }
            
            // Check if the mouse is on top of the borders
            if ((getTransform().transformRect(new FloatRect(0, 0, m_Size.x + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder, m_Size.y + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder + m_TitleBarHeight)).contains(x, y)) && 
            	(getTransform().transformRect(new FloatRect((float)(this._WidgetBordersImpl.m_LeftBorder), (float)(m_TitleBarHeight + this._WidgetBordersImpl.m_TopBorder), m_Size.x, m_Size.y)).contains(x, y) == false))
            {
                // Don't send the event to the widgets
                return;
            }
        }

        //Container::
        super.mouseMoved(x - this._WidgetBordersImpl.m_LeftBorder, y - (m_TitleBarHeight + this._WidgetBordersImpl.m_TopBorder));
	}
	
	public void mouseWheelMoved(int delta, int x, int y) {
		//Container::
		super.mouseWheelMoved(
			delta, 
			x - this._WidgetBordersImpl.m_LeftBorder, 
			y - (m_TitleBarHeight + this._WidgetBordersImpl.m_TopBorder));
	}
	
	public void mouseNoLongerDown() {
        //Container::
        super.mouseNoLongerDown();
        m_CloseButton.mouseNoLongerDown();
	}
	
	public boolean setProperty(String property, String value) {
		property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            load(value);
        } else if ("titlebarheight".equals(property)) {
            setTitleBarHeight(Integer.parseInt(value.trim()));
        } else if ("backgroundcolor".equals(property)) {
            setBackgroundColor(Defines.extractColor(value));
        } else if ("title".equals(property)) {
            setTitle(value);
        } else if ("titlecolor".equals(property)) {
            setTitleColor(Defines.extractColor(value));
        } else if ("bordercolor".equals(property)) {
            setBorderColor(Defines.extractColor(value));
        } else if ("borders".equals(property)) {
            Borders borders = new Borders();
            if (Defines.extractBorders(value, borders)) {
                setBorders(borders.left, borders.top, borders.right, borders.bottom);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Borders' property.");
            }
        } else if ("distancetoside".equals(property)) {
            setDistanceToSide(Integer.parseInt(value.trim()));
        } else if ("titlealignment".equals(property)) {
            if (("left".equals(value)) || ("Left".equals(value))) {
                setTitleAlignment(TitleAlignment.TitleAlignmentLeft);
        	} else if (("centered".equals(value)) || ("Centered".equals(value))) {
                setTitleAlignment(TitleAlignment.TitleAlignmentCentered);
        	} else if (("right".equals(value)) || ("Right".equals(value))) {
                setTitleAlignment(TitleAlignment.TitleAlignmentRight); 
        	} else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'TitleAlignment' property.");
        	}
        } else if ("callback".equals(property)) {
            //Container::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("LeftMousePressed".equals(it)) || ("leftmousepressed".equals(it))) {
                    this.getCallbackManager().bindCallback(ChildWindowCallbacks.LeftMousePressed.value());
            	} else if (("Closed".equals(it)) || ("closed".equals(it))) {
            		this.getCallbackManager().bindCallback(ChildWindowCallbacks.Closed.value());
        		} else if (("Moved".equals(it)) || ("moved".equals(it))) {
        			this.getCallbackManager().bindCallback(ChildWindowCallbacks.Moved.value());
				}
            }
        } else {
        	//Container::
            return super.setProperty(property, value);
        }
        
        return true;
    }
	
	public boolean getProperty(String property, String[] value) {
		property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            value[0] = getLoadedConfigFile();
        } else if ("titlebarheight".equals(property)) {
            value[0] = Integer.toString(getTitleBarHeight());
        } else if ("backgroundcolor".equals(property)) {
            value[0] = "(" + Integer.toString((int)(getBackgroundColor().r)) + "," + Integer.toString((int)(getBackgroundColor().g)) + "," + Integer.toString((int)(getBackgroundColor().b)) + "," + Integer.toString((int)(getBackgroundColor().a)) + ")";
        } else if ("title".equals(property)) {
            value[0] = getTitle();
        } else if ("titlecolor".equals(property)) {
            value[0] = "(" + Integer.toString((int)(getTitleColor().r)) + "," + Integer.toString((int)(getTitleColor().g)) + "," + Integer.toString((int)(getTitleColor().b)) + "," + Integer.toString((int)(getTitleColor().a)) + ")";
        } else if ("bordercolor".equals(property)) {
            value[0] = "(" + Integer.toString((int)(getBorderColor().r)) + "," + Integer.toString((int)(getBorderColor().g)) + "," + Integer.toString((int)(getBorderColor().b)) + "," + Integer.toString((int)(getBorderColor().a)) + ")";
        } else if ("borders".equals(property)) {
            value[0] = "(" + Integer.toString(this._WidgetBordersImpl.getBorders().left) + "," + Integer.toString(this._WidgetBordersImpl.getBorders().top) + "," + Integer.toString(this._WidgetBordersImpl.getBorders().right) + "," + Integer.toString(this._WidgetBordersImpl.getBorders().bottom) + ")";
        } else if ("distancetoside".equals(property)) {
            value[0] = Integer.toString(getDistanceToSide());
        } else if ("titlealignment".equals(property)) {
            if (m_TitleAlignment == TitleAlignment.TitleAlignmentLeft) {
                value[0]= "Left";
            } else if (m_TitleAlignment == TitleAlignment.TitleAlignmentCentered) {
                value[0] = "Centered";
        	} else if (m_TitleAlignment == TitleAlignment.TitleAlignmentRight) {
                value[0] = "Right";
            }
        } else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //Container::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(ChildWindowCallbacks.LeftMousePressed.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ChildWindowCallbacks.LeftMousePressed.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ChildWindowCallbacks.LeftMousePressed.value()).get(0) == null)) {
                callbacks.add("LeftMousePressed");
            }
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(ChildWindowCallbacks.Closed.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ChildWindowCallbacks.Closed.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ChildWindowCallbacks.Closed.value()).get(0) == null)) {
                callbacks.add("Closed");
            }
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(ChildWindowCallbacks.Moved.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ChildWindowCallbacks.Moved.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ChildWindowCallbacks.Moved.value()).get(0) == null)) {
                callbacks.add("Moved");
            }
            Defines.encodeList(callbacks, value);

            if (value[0].length() == 0 || tempValue[0].length() == 0) {
                value[0] += tempValue[0];
        	} else {
                value[0] += "," + tempValue[0];
            }
        } else {
        	//Container::
            return super.getProperty(property, value);
        }
        // You pass here when one of the properties matched
        return true;
	}
	
	public List<Pair<String, String>> getPropertyList() {
        //Container::
		List<Pair<String, String>> list = super.getPropertyList();
        list.add(new Pair<String, String>("ConfigFile", "string"));
        list.add(new Pair<String, String>("TitleBarHeight", "uint"));
        list.add(new Pair<String, String>("BackgroundColor", "color"));
        list.add(new Pair<String, String>("Title", "string"));
        list.add(new Pair<String, String>("TitleColor", "color"));
        list.add(new Pair<String, String>("BorderColor", "color"));
        list.add(new Pair<String, String>("Borders", "borders"));
        list.add(new Pair<String, String>("DistanceToSide", "uint"));
        list.add(new Pair<String, String>("TitleAlignment", "custom"));
        return list;
	}
	
	protected void initialize(Container parent) {
        m_Parent = parent;
        setGlobalFont(m_Parent.getGlobalFont());
        m_TitleText.setFont(m_Parent.getGlobalFont());
	}
	
	public void draw(RenderTarget target, RenderStates states) {
        if (m_Loaded == false) {
            return;
        }
        
        Vector2f position = getPosition();

        float scaleViewX = target.getSize().x / target.getView().getSize().x;
        float scaleViewY = target.getSize().y / target.getView().getSize().y;

        Vector2f viewPosition = 
        	Vector2f.minus(Vector2f.devide(target.getView().getSize(), 2.f), 
        		target.getView().getCenter());

        Vector2f topLeftPanelPosition = 
        	states.transform.transformPoint(
        		position.x + this._WidgetBordersImpl.m_LeftBorder + viewPosition.x,
                position.y + m_TitleBarHeight + this._WidgetBordersImpl.m_TopBorder + viewPosition.y);
        Vector2f bottomRightPanelPosition = 
        	states.transform.transformPoint(
        		position.x + m_Size.x + this._WidgetBordersImpl.m_LeftBorder + viewPosition.x,
                position.y + m_TitleBarHeight + m_Size.y + this._WidgetBordersImpl.m_TopBorder + viewPosition.y);
        Vector2f topLeftTitleBarPosition;
        Vector2f bottomRightTitleBarPosition;

        if (m_IconTexture.data != null) {
            topLeftTitleBarPosition = 
            	states.transform.transformPoint(
            		position.x + 2 * m_DistanceToSide + (m_IconTexture.getSize().x * m_IconTexture.sprite.getScale().x) + viewPosition.x,
            		position.y + viewPosition.y);
        } else {
            topLeftTitleBarPosition = 
            	states.transform.transformPoint(
            		position.x + m_DistanceToSide + viewPosition.x, 
            		position.y + viewPosition.y);
		}
        bottomRightTitleBarPosition = 
        	states.transform.transformPoint(
        		position.x + m_Size.x + this._WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder - (2*m_DistanceToSide) - m_CloseButton.getSize().x + viewPosition.x,
                position.y + m_TitleBarHeight + viewPosition.y);

        Transform.multiplyEqual(states.transform, getTransform());

        Transform oldTransform = states.transform;

        if (m_SplitImage) {
            target.draw(m_TextureTitleBar_L.getSprite(), states);

            states.transform.translate(m_TextureTitleBar_L.getSize().x * ((float)(m_TitleBarHeight) / m_TextureTitleBar_M.getSize().y), 0);
            target.draw(m_TextureTitleBar_M.getSprite(), states);

            states.transform.translate(m_Size.x + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder - ((m_TextureTitleBar_R.getSize().x + m_TextureTitleBar_L.getSize().x)
                                                                                  * ((float)(m_TitleBarHeight) / m_TextureTitleBar_M.getSize().y)), 0);
            target.draw(m_TextureTitleBar_R.getSprite(), states);
        } else {
            target.draw(m_TextureTitleBar_M.getSprite(), states);
        }

        states.transform = oldTransform;

        if (m_IconTexture.data != null) {
            states.transform.translate((float)(m_DistanceToSide), (m_TitleBarHeight - (m_IconTexture.getSize().y * m_IconTexture.sprite.getScale().y)) / 2.f);
            target.draw(m_IconTexture.getSprite(), states);
            states.transform.translate(m_IconTexture.getSize().x * m_IconTexture.sprite.getScale().x, (m_TitleBarHeight - (m_IconTexture.getSize().y * m_IconTexture.sprite.getScale().y)) / -2.f);
        }

        int[] scissor = new int[4];
        GL.glGetIntegerv(GL.GL_SCISSOR_BOX, scissor);

        if (m_TitleText.getString().isEmpty() == false) {
            int scissorLeft = (int)Defines.TGUI_MAXIMUM((int)(topLeftTitleBarPosition.x * scaleViewX), scissor[0]);
            int scissorTop = (int)Defines.TGUI_MAXIMUM((int)(topLeftTitleBarPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1] - scissor[3]);
            int scissorRight = (int)Defines.TGUI_MINIMUM((int)(bottomRightTitleBarPosition.x * scaleViewX), scissor[0] + scissor[2]);
            int scissorBottom = (int)Defines.TGUI_MINIMUM((int)(bottomRightTitleBarPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1]);

            if (scissorRight < scissorLeft) {
                scissorRight = scissorLeft;
        	} else if (scissorBottom < scissorTop) {
                scissorTop = scissorBottom;
            }

            GL.glScissor(scissorLeft, target.getSize().y - scissorBottom, scissorRight - scissorLeft, scissorBottom - scissorTop);

            if (m_TitleAlignment == TitleAlignment.TitleAlignmentLeft) {
                states.transform.translate(
                	(float)Math.floor((float)(m_DistanceToSide) + 0.5f), 0);
                target.draw(m_TitleText, states);
            } else if (m_TitleAlignment == TitleAlignment.TitleAlignmentCentered) {
                if (m_IconTexture.data != null) {
                    states.transform.translate(
                    	(float)Math.floor(m_DistanceToSide + (((m_Size.x + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) - 4 * m_DistanceToSide - (m_IconTexture.getSize().x * m_IconTexture.sprite.getScale().x) - m_CloseButton.getSize().x - m_TitleText.getGlobalBounds().width) / 2.0f) + 0.5f), 0);
                } else {
                    states.transform.translate(
                    	(float)Math.floor(m_DistanceToSide + (((m_Size.x + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) - 3 * m_DistanceToSide - m_CloseButton.getSize().x - m_TitleText.getGlobalBounds().width) / 2.0f) + 0.5f), 0);
                }
                target.draw(m_TitleText, states);
            } else {
                if (m_IconTexture.data != null) {
                    states.transform.translate(
                    	(float)Math.floor((m_Size.x + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) - (m_IconTexture.getSize().x * m_IconTexture.sprite.getScale().x) - 3*m_DistanceToSide - m_CloseButton.getSize().x - m_TitleText.getGlobalBounds().width + 0.5f), 0);
                } else {
                    states.transform.translate(
                    	(float)Math.floor((m_Size.x + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) - 2*m_DistanceToSide - m_CloseButton.getSize().x - m_TitleText.getGlobalBounds().width + 0.5f), 0);
                }
                
                target.draw(m_TitleText, states);
            }

            GL.glScissor(scissor[0], scissor[1], scissor[2], scissor[3]);
        }

        states.transform = oldTransform;
        states.transform.translate((m_Size.x + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) - m_DistanceToSide - m_CloseButton.getSize().x, (m_TitleBarHeight - m_CloseButton.getSize().y) / 2.f);

        target.draw(m_CloseButton, states);

        states.transform = oldTransform.translate(0, (float)(m_TitleBarHeight));

        RectangleShape border = new RectangleShape(new Vector2f((float)(this._WidgetBordersImpl.m_LeftBorder), m_Size.y + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder));
        border.setFillColor(m_BorderColor);
        target.draw(border, states);

        border.setSize(new Vector2f(
        	m_Size.x + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder, (float)(this._WidgetBordersImpl.m_TopBorder)));
        target.draw(border, states);

        border.setPosition(m_Size.x + this._WidgetBordersImpl.m_LeftBorder, 0);
        border.setSize(new Vector2f((float)(this._WidgetBordersImpl.m_RightBorder), m_Size.y + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder));
        target.draw(border, states);

        border.setPosition(0, m_Size.y + this._WidgetBordersImpl.m_TopBorder);
        border.setSize(new Vector2f(m_Size.x + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder, (float)(this._WidgetBordersImpl.m_BottomBorder)));
        target.draw(border, states);

        states.transform.translate((float)(this._WidgetBordersImpl.m_LeftBorder), (float)(this._WidgetBordersImpl.m_TopBorder));

        if (Color.notEqual(m_BackgroundColor, Color.Transparent)) {
            RectangleShape background = new RectangleShape(new Vector2f(m_Size.x, m_Size.y));
            background.setFillColor(m_BackgroundColor);
            target.draw(background, states);
        }

        if (m_BackgroundTexture != null) {
            target.draw(m_BackgroundSprite, states);
        }
        
        int scissorLeft = (int)Defines.TGUI_MAXIMUM((int)(topLeftPanelPosition.x * scaleViewX), scissor[0]);
        int scissorTop = (int)Defines.TGUI_MAXIMUM((int)(topLeftPanelPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1] - scissor[3]);
        int scissorRight = (int)Defines.TGUI_MINIMUM((int)(bottomRightPanelPosition.x * scaleViewX), scissor[0] + scissor[2]);
        int scissorBottom = (int)Defines.TGUI_MINIMUM((int)(bottomRightPanelPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1]);

        if (scissorRight < scissorLeft) {
            scissorRight = scissorLeft;
        } else if (scissorBottom < scissorTop) {
            scissorTop = scissorBottom;
        }

        GL.glScissor(scissorLeft, target.getSize().y - scissorBottom, scissorRight - scissorLeft, scissorBottom - scissorTop);

        drawWidgetContainer(target, states);

        GL.glScissor(scissor[0], scissor[1], scissor[2], scissor[3]);
	}
	
	@Override
	public void setBorders(int leftBorder, int topBorder,
			int rightBorder, int bottomBorder) {
        _WidgetBordersImpl.m_LeftBorder = leftBorder;
        _WidgetBordersImpl.m_TopBorder = topBorder;
        _WidgetBordersImpl.m_RightBorder = rightBorder;
        _WidgetBordersImpl.m_BottomBorder = bottomBorder;

        if (m_SplitImage) {
            float scalingY = (float)(m_TitleBarHeight) / m_TextureTitleBar_M.getSize().y;
            float minimumWidth = ((m_TextureTitleBar_L.getSize().x + m_TextureTitleBar_R.getSize().x) * scalingY);

            if (m_Size.x < minimumWidth + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder) {
                m_Size.x = minimumWidth + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder;
            }

            m_TextureTitleBar_M.sprite.setTextureRect(
            	new IntRect(0, 0, 
            		(int)(((m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder) - minimumWidth) / scalingY), 
            		m_TextureTitleBar_M.getSize().y));
        } else {
            m_TextureTitleBar_M.sprite.setScale(
            	(m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder) / m_TextureTitleBar_M.getSize().x,
                (float)(m_TitleBarHeight) / m_TextureTitleBar_M.getSize().y);
        }
	}

	@Override
	public boolean mouseOnWidget(float x, float y) {
	    if (m_Loaded == false) {
            return false;
	    }
        
	    if (getTransform().transformRect(
	    	new FloatRect(0, 0, 
	    		m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder, 
	    		(float)(m_TitleBarHeight + _WidgetBordersImpl.m_TopBorder))).contains(x, y)) {
            for (int i = 0; i < m_Widgets.size(); ++i) {
                m_Widgets.get(i).get().mouseNotOnWidget();
            }
            return true;
        } else {
            if (getTransform().transformRect(
            		new FloatRect(0, 0, 
            			m_Size.x + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder, 
            			m_Size.y + _WidgetBordersImpl.m_TopBorder + _WidgetBordersImpl.m_BottomBorder))
            				.contains(x, y - m_TitleBarHeight)) {
                return true;
            } else {
                if (m_MouseHover) {
                    mouseLeftWidget();

                    for (int i = 0; i < m_Widgets.size(); ++i) {
                        m_Widgets.get(i).get().mouseNotOnWidget();
                    }

                    m_CloseButton.mouseNotOnWidget();
                    m_MouseHover = false;
                }

                return false;
            }
        }
	}

}
