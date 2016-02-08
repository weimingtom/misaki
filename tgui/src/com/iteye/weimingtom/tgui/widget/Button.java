package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.Texture;
import com.iteye.weimingtom.tgui.WidgetPhase;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.IntRect;
import com.iteye.weimingtom.tgui.sf.Keyboard;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Text;

/**
 * 20150903
 * @author Administrator
 *
 */
public class Button extends ClickableWidget {
	public static enum ButtonCallbacks {
        SpaceKeyPressed(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 1),
        ReturnKeyPressed(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 2),
        AllButtonCallbacks(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 4 - 1),
        ButtonCallbacksCount(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 4);
        
        int value;
        
        ButtonCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }

    protected String m_LoadedConfigFile = "";

    protected Texture m_TextureNormal_L = new Texture();
    protected Texture m_TextureHover_L = new Texture();
    protected Texture m_TextureDown_L = new Texture();
    protected Texture m_TextureFocused_L = new Texture();

    protected Texture m_TextureNormal_M = new Texture();
    protected Texture m_TextureHover_M = new Texture();
    protected Texture m_TextureDown_M = new Texture();
    protected Texture m_TextureFocused_M = new Texture();

    protected Texture m_TextureNormal_R = new Texture();
    protected Texture m_TextureHover_R = new Texture();
    protected Texture m_TextureDown_R = new Texture();
    protected Texture m_TextureFocused_R = new Texture();

    protected boolean m_SplitImage = false;

    protected boolean m_SeparateHoverImage = false;

    protected Text m_Text = new Text();

    protected int m_TextSize = 0;
	
	public Button() {
	    m_SplitImage = false;
	    m_SeparateHoverImage = false;
	    m_TextSize = 0;
	    
        this.getCallbackManager().m_Callback.widgetType 
        	= WidgetTypes.Type_Button;
        m_Text.setColor(Color.Black);
	}

	public Button(Button copy) {
	    super(copy);
	    m_LoadedConfigFile = copy.m_LoadedConfigFile;
	    m_SplitImage = copy.m_SplitImage;
	    m_SeparateHoverImage = copy.m_SeparateHoverImage;
	    m_Text = copy.m_Text;
	    m_TextSize = copy.m_TextSize;		

        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureNormal_L, m_TextureNormal_L);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureNormal_M, m_TextureNormal_M);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureNormal_R, m_TextureNormal_R);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureHover_L, m_TextureHover_L);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureHover_M, m_TextureHover_M);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureHover_R, m_TextureHover_R);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureDown_L, m_TextureDown_L);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureDown_M, m_TextureDown_M);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureDown_R, m_TextureDown_R);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureFocused_L, m_TextureFocused_L);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureFocused_M, m_TextureFocused_M);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureFocused_R, m_TextureFocused_R);
	}

	public void destroy() {
		super.destroy();
        if (m_TextureNormal_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureNormal_L);
        }
        if (m_TextureNormal_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureNormal_M);
        }
        if (m_TextureNormal_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureNormal_R);
        }

        if (m_TextureHover_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_L);
        }
        if (m_TextureHover_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_M);
        }
        if (m_TextureHover_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_R);
        }

        if (m_TextureDown_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureDown_L);
        }
        if (m_TextureDown_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureDown_M);
        }
        if (m_TextureDown_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureDown_R);
        }

        if (m_TextureFocused_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_L);
        }
        if (m_TextureFocused_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_M);
        }
        if (m_TextureFocused_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_R);	
        }
	}

	public Button assign(Button right) {
        if (this != right) {
            Button temp = new Button(right);
            super.assign(right);

            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_TextureNormal_L = temp.m_TextureNormal_L;
            m_TextureNormal_M = temp.m_TextureNormal_M;
            m_TextureNormal_R = temp.m_TextureNormal_R;
            m_TextureHover_L = temp.m_TextureHover_L;
            m_TextureHover_M = temp.m_TextureHover_M;
            m_TextureHover_R = temp.m_TextureHover_R;
            m_TextureDown_L = temp.m_TextureDown_L;
            m_TextureDown_M = temp.m_TextureDown_M;
            m_TextureDown_R = temp.m_TextureDown_R;
            m_TextureFocused_L = temp.m_TextureFocused_L;
            m_TextureFocused_M = temp.m_TextureFocused_M;
            m_TextureFocused_R = temp.m_TextureFocused_R;
            m_SplitImage = temp.m_SplitImage;
            m_SeparateHoverImage = temp.m_SeparateHoverImage;
            m_Text = temp.m_Text;
            m_TextSize = temp.m_TextSize;
        }

        return this;
	}

	public Button cloneObj() {
		return new Button(this);
	}

	public boolean load(String configFileFilename) {
		m_LoadedConfigFile = configFileFilename;

        m_Loaded = false;

        if (m_TextureNormal_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureNormal_L);
        }
        if (m_TextureNormal_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureNormal_M);
        }
        if (m_TextureNormal_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureNormal_R);
        }
        if (m_TextureHover_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_L);
        }
        if (m_TextureHover_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_M);
        }
        if (m_TextureHover_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_R);
        }
        if (m_TextureDown_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureDown_L);
        }
        if (m_TextureDown_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureDown_M);
        }
        if (m_TextureDown_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureDown_R);
        }
        if (m_TextureFocused_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_L);
        }
        if (m_TextureFocused_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_M);
        }
        if (m_TextureFocused_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_R);
        }

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("Button", properties, values)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to parse " + configFileFilename + ".");
            return false;
        }

        configFile.close();

        String configFileFolder = "";
        //FIXME:
        int slashPos = configFileFilename.lastIndexOf("/");
        if (slashPos == -1) {
        	slashPos = configFileFilename.lastIndexOf("\\"); 
        }
        if (slashPos != -1) {
            configFileFolder = configFileFilename.substring(0, slashPos + 1);
        }
        
        // Handle the read properties
        for (int i = 0; i < properties.size(); ++i) {
            String property = properties.get(i);
            String value = values.get(i);

            if ("separatehoverimage".equals(property)) {
                m_SeparateHoverImage = configFile.readBool(value, false);
            } else if ("textcolor".equals(property)) {
                m_Text.setColor(configFile.readColor(value));
            } else if ("normalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage in section Button in " + configFileFilename + ".");
                    return false;
                }
                m_SplitImage = false;
            } else if ("hoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureHover_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for HoverImage in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("downimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureDown_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for DownImage in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("focusedimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFocused_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FocusedImage in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("normalimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_L)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage_L in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("normalimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage_M in section Button in " + configFileFilename + ".");
                    return false;
                }
                m_SplitImage = true;
            } else if ("normalimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_R)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage_R in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("hoverimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureHover_L)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for HoverImage_L in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("hoverimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureHover_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for HoverImage_M in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("hoverimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureHover_R)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for HoverImage_R in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("downimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureDown_L)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for DownImage_L in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("downimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureDown_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for DownImage_M in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("downimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureDown_R)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for DownImage_R in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("focusedimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFocused_L)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FocusedImage_L in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("focusedimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFocused_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FocusedImage_M in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else if ("focusedimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFocused_R)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FocusedImage_R in section Button in " + configFileFilename + ".");
                    return false;
                }
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section Button in " + configFileFilename + ".");
            }
        }

        if (m_SplitImage) {
            if ((m_TextureNormal_L.data != null) && 
            	(m_TextureNormal_M.data != null) && 
            	(m_TextureNormal_R.data != null)) {
                m_Loaded = true;
                setSize((float)(m_TextureNormal_L.getSize().x + m_TextureNormal_M.getSize().x + m_TextureNormal_R.getSize().x),
                        (float)(m_TextureNormal_M.getSize().y));
                
                m_TextureNormal_M.data.texture.setRepeated(true);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the button. Is the Button section in " + configFileFilename + " complete?");
                return false;
            }

            if ((m_TextureFocused_L.data != null) && 
            	(m_TextureFocused_M.data != null) && 
            	(m_TextureFocused_R.data != null)) {
                m_AllowFocus = true;
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Focused.value();

                m_TextureFocused_M.data.texture.setRepeated(true);
            }
            if ((m_TextureHover_L.data != null) && 
            	(m_TextureHover_M.data != null) && 
            	(m_TextureHover_R.data != null)) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();

                m_TextureHover_M.data.texture.setRepeated(true);
            }
            if ((m_TextureDown_L.data != null) && 
            	(m_TextureDown_M.data != null) && 
            	(m_TextureDown_R.data != null)) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_MouseDown.value();

                m_TextureDown_M.data.texture.setRepeated(true);
            }
        } else {
            if (m_TextureNormal_M.data != null) {
                m_Loaded = true;
                setSize((float)(m_TextureNormal_M.getSize().x), 
                	(float)(m_TextureNormal_M.getSize().y));
            } else {
                Defines.TGUI_OUTPUT("TGUI error: NormalImage wasn't loaded. Is the Button section in " + configFileFilename + " complete?");
                return false;
            }

            if (m_TextureFocused_M.data != null) {
                m_AllowFocus = true;
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Focused.value();
            }
            if (m_TextureHover_M.data != null) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();
            }
            if (m_TextureDown_M.data != null) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_MouseDown.value();
            }
        }

        return true;
	}

	public String getLoadedConfigFile() {
		return m_LoadedConfigFile;
	}

	public void setPosition(float x, float y) {
        //Transformable::
		//using Transformable::setPosition;
        super.setPosition(x, y);

        if (m_SplitImage) {
            m_TextureDown_L.sprite.setPosition(x, y);
            m_TextureHover_L.sprite.setPosition(x, y);
            m_TextureNormal_L.sprite.setPosition(x, y);
            m_TextureFocused_L.sprite.setPosition(x, y);

            // Check if the middle image may be drawn
            if ((m_TextureNormal_M.sprite.getScale().y * (m_TextureNormal_L.getSize().x + 
            	m_TextureNormal_R.getSize().x)) < m_Size.x) {
                float scalingY = m_Size.y / m_TextureNormal_M.getSize().y;

                m_TextureDown_M.sprite.setPosition(x + (m_TextureDown_L.getSize().x * m_TextureDown_L.sprite.getScale().x), y);
                m_TextureHover_M.sprite.setPosition(x + (m_TextureHover_L.getSize().x * m_TextureHover_L.sprite.getScale().x), y);
                m_TextureNormal_M.sprite.setPosition(x + (m_TextureNormal_L.getSize().x * m_TextureNormal_L.sprite.getScale().x), y);
                m_TextureFocused_M.sprite.setPosition(x + (m_TextureFocused_L.getSize().x * m_TextureFocused_L.sprite.getScale().x), y);

                m_TextureDown_R.sprite.setPosition(m_TextureDown_M.sprite.getPosition().x + (m_TextureDown_M.sprite.getTextureRect().width * scalingY), y);
                m_TextureHover_R.sprite.setPosition(m_TextureHover_M.sprite.getPosition().x + (m_TextureHover_M.sprite.getTextureRect().width * scalingY), y);
                m_TextureNormal_R.sprite.setPosition(m_TextureNormal_M.sprite.getPosition().x + (m_TextureNormal_M.sprite.getTextureRect().width * scalingY), y);
                m_TextureFocused_R.sprite.setPosition(m_TextureFocused_M.sprite.getPosition().x + (m_TextureFocused_M.sprite.getTextureRect().width * scalingY), y);
            } else {
                m_TextureDown_R.sprite.setPosition(x + (m_TextureDown_L.getSize().x * m_TextureDown_L.sprite.getScale().x), y);
                m_TextureHover_R.sprite.setPosition(x + (m_TextureHover_L.getSize().x * m_TextureHover_L.sprite.getScale().x), y);
                m_TextureNormal_R.sprite.setPosition(x + (m_TextureNormal_L.getSize().x * m_TextureNormal_L.sprite.getScale().x), y);
                m_TextureFocused_R.sprite.setPosition(x + (m_TextureFocused_L.getSize().x * m_TextureFocused_L.sprite.getScale().x), y);
            }
        } else {
            m_TextureDown_M.sprite.setPosition(x, y);
            m_TextureHover_M.sprite.setPosition(x, y);
            m_TextureNormal_M.sprite.setPosition(x, y);
            m_TextureFocused_M.sprite.setPosition(x, y);
        }

        m_Text.setPosition(
        	(float)Math.floor(x + (m_Size.x - m_Text.getLocalBounds().width) * 0.5f -  m_Text.getLocalBounds().left),
        	(float)Math.floor(y + (m_Size.y - m_Text.getLocalBounds().height) * 0.5f -  m_Text.getLocalBounds().top));		
	}
    

	public void setSize(float width, float height) {
        if (m_Loaded == false) {
            return;
        }

        m_Size.x = width;
        m_Size.y = height;

        if (m_Size.x < 0) {
        	m_Size.x = -m_Size.x;
        }
        if (m_Size.y < 0) {
        	m_Size.y = -m_Size.y;
        }

        if (m_TextSize == 0) {
            setText(m_Text.getString());
        }
        
        if (m_SplitImage) {
            float scalingY = m_Size.y / m_TextureNormal_M.getSize().y;
            float minimumWidth = (m_TextureNormal_L.getSize().x + m_TextureNormal_R.getSize().x) * scalingY;

            if (m_Size.x < minimumWidth)
                m_Size.x = minimumWidth;

            m_TextureDown_L.sprite.setScale(scalingY, scalingY);
            m_TextureHover_L.sprite.setScale(scalingY, scalingY);
            m_TextureNormal_L.sprite.setScale(scalingY, scalingY);
            m_TextureFocused_L.sprite.setScale(scalingY, scalingY);

            m_TextureDown_M.sprite.setTextureRect(new IntRect(0, 0, (int)((m_Size.x - minimumWidth) / scalingY), m_TextureDown_M.getSize().y));
            m_TextureHover_M.sprite.setTextureRect(new IntRect(0, 0, (int)((m_Size.x - minimumWidth) / scalingY), m_TextureHover_M.getSize().y));
            m_TextureNormal_M.sprite.setTextureRect(new IntRect(0, 0, (int)((m_Size.x - minimumWidth) / scalingY), m_TextureNormal_M.getSize().y));
            m_TextureFocused_M.sprite.setTextureRect(new IntRect(0, 0, (int)((m_Size.x - minimumWidth) / scalingY), m_TextureFocused_M.getSize().y));

            m_TextureDown_M.sprite.setScale(scalingY, scalingY);
            m_TextureHover_M.sprite.setScale(scalingY, scalingY);
            m_TextureNormal_M.sprite.setScale(scalingY, scalingY);
            m_TextureFocused_M.sprite.setScale(scalingY, scalingY);

            m_TextureDown_R.sprite.setScale(scalingY, scalingY);
            m_TextureHover_R.sprite.setScale(scalingY, scalingY);
            m_TextureNormal_R.sprite.setScale(scalingY, scalingY);
            m_TextureFocused_R.sprite.setScale(scalingY, scalingY);
        } else {
            m_TextureDown_M.sprite.setScale(m_Size.x / m_TextureDown_M.getSize().x, m_Size.y / m_TextureDown_M.getSize().y);
            m_TextureHover_M.sprite.setScale(m_Size.x / m_TextureHover_M.getSize().x, m_Size.y / m_TextureHover_M.getSize().y);
            m_TextureNormal_M.sprite.setScale(m_Size.x / m_TextureNormal_M.getSize().x, m_Size.y / m_TextureNormal_M.getSize().y);
            m_TextureFocused_M.sprite.setScale(m_Size.x / m_TextureFocused_M.getSize().x, m_Size.y / m_TextureFocused_M.getSize().y);
        }

        setPosition(getPosition());
	}

	public void setText(String text) {
	       // Don't do anything when the button wasn't loaded correctly
        if (m_Loaded == false)
            return;

        // Set the new text
        m_Text.setString(text);
        this.getCallbackManager().m_Callback.text = text;

        if (m_TextSize == 0) {
            float size = m_Size.y * 0.85f;
            m_Text.setCharacterSize((int)(size));
            m_Text.setCharacterSize((int)(m_Text.getCharacterSize() - m_Text.getLocalBounds().top));

            if (m_Text.getGlobalBounds().width > (m_Size.x * 0.8f)) {
                m_Text.setCharacterSize((int)(size * m_Size.x * 0.8f / m_Text.getGlobalBounds().width));
                m_Text.setCharacterSize((int)(m_Text.getCharacterSize() - m_Text.getLocalBounds().top));
            }
        } else {
            m_Text.setCharacterSize(m_TextSize);
        }

        m_Text.setPosition(
        	(float)Math.floor(getPosition().x + (m_Size.x - m_Text.getLocalBounds().width) * 0.5f -  m_Text.getLocalBounds().left),
        	(float)Math.floor(getPosition().y + (m_Size.y - m_Text.getLocalBounds().height) * 0.5f -  m_Text.getLocalBounds().top));		
	}

	public String getText() {
        return m_Text.getString();
	}

	public void setTextFont(Font font) {
		m_Text.setFont(font);
	}

	public Font getTextFont() {
		return m_Text.getFont();
	}

	public void setTextColor(Color color) {
		m_Text.setColor(color);
	}

	public Color getTextColor() {
		return m_Text.getColor();
	}
	
	public void setTextSize(int size) {
        m_TextSize = size;

        setText(m_Text.getString());		
	}

	public int getTextSize() {
		return m_Text.getCharacterSize();
	}

	public void setTransparency(int transparency) {
        //ClickableWidget::
		super.setTransparency(transparency);

        if (m_SplitImage) {
            m_TextureNormal_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
            m_TextureHover_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
            m_TextureDown_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
            m_TextureFocused_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));

            m_TextureNormal_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
            m_TextureHover_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
            m_TextureDown_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
            m_TextureFocused_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        }

        m_TextureNormal_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureHover_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureDown_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureFocused_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));		
	}

	public void keyPressed(Keyboard.Key key) {
	    if (key == Keyboard.Key.Space) {
            if (this.getCallbackManager().m_CallbackFunctions
            		.get(ButtonCallbacks.SpaceKeyPressed.value())
            			.isEmpty() == false) {
                this.getCallbackManager().m_Callback.trigger 
                	= ButtonCallbacks.SpaceKeyPressed.value();
                addCallback();
            }
        } else if (key == Keyboard.Key.Return) {
            if (this.getCallbackManager().m_CallbackFunctions
            		.get(ButtonCallbacks.ReturnKeyPressed.value())
            			.isEmpty() == false) {
            	this.getCallbackManager().m_Callback.trigger 
            		= ButtonCallbacks.ReturnKeyPressed.value();
                addCallback();
            }
        }
	}

	public void widgetFocused() {
        if ((m_WidgetPhase & WidgetPhase.WidgetPhase_Focused.value()) == 0) {
            unfocus();
        } else {
            //Widget::
        	super.widgetFocused();
        }
	}

	public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            load(value);
        } else if ("text".equals(property)) {
            setText(value);
        } else if ("textcolor".equals(property)) {
            setTextColor(Defines.extractColor(value));
        } else if ("textsize".equals(property)) {
        	try {
        		setTextSize(Integer.parseInt(value.trim()));
        	} catch (NumberFormatException e) {
        		
        	}
        } else if ("callback".equals(property)) {
            //ClickableWidget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("SpaceKeyPressed".equals(it)) || 
                	("spacekeypressed".equals(it))) {
                    this.getCallbackManager()
                    	.bindCallback(ButtonCallbacks.SpaceKeyPressed.value());
                } else if (("ReturnKeyPressed".equals(it)) || 
                	("returnkeypressed".equals(it))) {
                    this.getCallbackManager()
                    	.bindCallback(ButtonCallbacks.ReturnKeyPressed.value());
                }
            }
        } else {
        	//ClickableWidget::
            return super.setProperty(property, value);
        }
        
        return true;
	}

	public boolean getProperty(String property, String[] value) {
		property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            value[0] = getLoadedConfigFile();
        } else if ("text".equals(property)) {
            value[0] = getText();
        } else if ("textcolor".equals(property)) { 
            value[0] = 
            	"(" + Integer.toString((int)(getTextColor().r)) + 
            	"," + Integer.toString((int)(getTextColor().g)) + 
            	"," + Integer.toString((int)(getTextColor().b)) + 
            	"," + Integer.toString((int)(getTextColor().a)) + 
            	")";
        } else if ("textsize".equals(property)) {
            value[0] = Integer.toString(getTextSize());
		} else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //ClickableWidget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(ButtonCallbacks.SpaceKeyPressed.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ButtonCallbacks.SpaceKeyPressed.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ButtonCallbacks.SpaceKeyPressed.value()).get(0) == null)) {
                callbacks.add("SpaceKeyPressed");
			}
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(ButtonCallbacks.ReturnKeyPressed.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ButtonCallbacks.ReturnKeyPressed.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ButtonCallbacks.ReturnKeyPressed.value()).get(0) == null)) {
                callbacks.add("ReturnKeyPressed");
			}
            
            Defines.encodeList(callbacks, value);
            
            if (value[0] == null || value[0].length() == 0) {
                value[0] = tempValue[0];
            } else if (tempValue != null && tempValue.length != 0) {
                value[0] += "," + tempValue;
            }
        } else {
        	//ClickableWidget::
            return super.getProperty(property, value);
        }
        // You pass here when one of the properties matched
        return true;
	}

	public List<Pair<String, String>> getPropertyList() {
        //ClickableWidget::
		List<Pair<String, String>> list = super.getPropertyList();
        list.add(new Pair<String, String>("ConfigFile", "string"));
        list.add(new Pair<String, String>("Text", "string"));
        list.add(new Pair<String, String>("TextColor", "color"));
        list.add(new Pair<String, String>("TextSize", "uint"));
        return list;
	}

	protected void initialize(Container parent) {
        m_Parent = parent;
        m_Text.setFont(m_Parent.getGlobalFont());
	}

	public void draw(RenderTarget target, RenderStates states) {
        if (m_SplitImage) {
            if (m_SeparateHoverImage) {
                if ((m_MouseDown) && (m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_MouseDown.value()) != 0)) {
                    target.draw(m_TextureDown_L.getSprite(), states);
                    target.draw(m_TextureDown_M.getSprite(), states);
                    target.draw(m_TextureDown_R.getSprite(), states);
                } else if ((m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureHover_L.getSprite(), states);
                    target.draw(m_TextureHover_M.getSprite(), states);
                    target.draw(m_TextureHover_R.getSprite(), states);
                } else {
                    target.draw(m_TextureNormal_L.getSprite(), states);
                    target.draw(m_TextureNormal_M.getSprite(), states);
                    target.draw(m_TextureNormal_R.getSprite(), states);
                }
            } else {
                if ((m_MouseDown) && (m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_MouseDown.value()) != 0)) {
                    target.draw(m_TextureDown_L.getSprite(), states);
                    target.draw(m_TextureDown_M.getSprite(), states);
                    target.draw(m_TextureDown_R.getSprite(), states);
                } else {
                    target.draw(m_TextureNormal_L.getSprite(), states);
                    target.draw(m_TextureNormal_M.getSprite(), states);
                    target.draw(m_TextureNormal_R.getSprite(), states);
                }
                if ((m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureHover_L.getSprite(), states);
                    target.draw(m_TextureHover_M.getSprite(), states);
                    target.draw(m_TextureHover_R.getSprite(), states);
                }
            }
            
            if ((m_Focused) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Focused.value()) != 0)) {
                target.draw(m_TextureFocused_L.getSprite(), states);
                target.draw(m_TextureFocused_M.getSprite(), states);
                target.draw(m_TextureFocused_R.getSprite(), states);
            }
        } else {
            if (m_SeparateHoverImage) {
                if ((m_MouseDown) && (m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_MouseDown.value()) != 0)) {
                    target.draw(m_TextureDown_M.getSprite(), states);
                } else if ((m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureHover_M.getSprite(), states);
                } else {
                    target.draw(m_TextureNormal_M.getSprite(), states);
                }
            } else {
                if ((m_MouseDown) && (m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_MouseDown.value()) != 0)) {
                    target.draw(m_TextureDown_M.getSprite(), states);
                } else {
                    target.draw(m_TextureNormal_M.getSprite(), states);
                }

                if ((m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureHover_M.getSprite(), states);
                }
            }

            if ((m_Focused) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Focused.value()) != 0)) {
                target.draw(m_TextureFocused_M.getSprite(), states);
            }
        }

        target.draw(m_Text, states);
	}
}
