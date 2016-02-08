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
import com.iteye.weimingtom.tgui.sf.FloatRect;
import com.iteye.weimingtom.tgui.sf.IntRect;
import com.iteye.weimingtom.tgui.sf.Keyboard;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20151011
 * @author Administrator
 *
 */
public class Slider extends Widget {
	  
    public static enum SliderCallbacks {
        ValueChanged(WidgetCallbacks.WidgetCallbacksCount.value() * 1),
        AllSliderCallbacks(WidgetCallbacks.WidgetCallbacksCount.value() * 2 - 1),
        SliderCallbacksCount(WidgetCallbacks.WidgetCallbacksCount.value() * 2);
        
        int value;
        
        SliderCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }

    protected String m_LoadedConfigFile;

    protected boolean m_MouseDownOnThumb;
    protected Vector2f m_MouseDownOnThumbPos;

    protected int m_Minimum;
    protected int m_Maximum;
    protected int m_Value;

    protected boolean m_VerticalScroll;

    protected boolean m_VerticalImage;

    protected boolean m_SplitImage;

    protected boolean m_SeparateHoverImage;

    protected Vector2f m_Size;
    protected Vector2f m_ThumbSize;

    protected Texture m_TextureTrackNormal_L;
    protected Texture m_TextureTrackHover_L;
    protected Texture m_TextureTrackNormal_M;
    protected Texture m_TextureTrackHover_M;
    protected Texture m_TextureTrackNormal_R;
    protected Texture m_TextureTrackHover_R;
    protected Texture m_TextureThumbNormal;
    protected Texture m_TextureThumbHover;
	
	public Slider() {
	    m_MouseDownOnThumb = false;
	    m_Minimum = 0;
	    m_Maximum = 10;
	    m_Value = 0;
	    m_VerticalScroll = true;
	    m_VerticalImage = true;
	    m_SplitImage = false;
	    m_SeparateHoverImage = false;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_Slider;
        m_DraggableWidget = true;
	}

	public Slider(Slider copy) {
	    //Widget               (copy),
	    super(copy);
		m_LoadedConfigFile = copy.m_LoadedConfigFile;
	    m_MouseDownOnThumb = copy.m_MouseDownOnThumb;
	    m_MouseDownOnThumbPos = copy.m_MouseDownOnThumbPos;
	    m_Minimum = copy.m_Minimum;
	    m_Maximum = copy.m_Maximum;
	    m_Value = copy.m_Value;
	    m_VerticalScroll = copy.m_VerticalScroll;
	    m_VerticalImage = copy.m_VerticalImage;
	    m_SplitImage = copy.m_SplitImage;
	    m_SeparateHoverImage = copy.m_SeparateHoverImage;
	    m_Size = copy.m_Size;
	    m_ThumbSize = copy.m_ThumbSize;
	    
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureTrackNormal_L, m_TextureTrackNormal_L);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureTrackHover_L, m_TextureTrackHover_L);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureTrackNormal_M, m_TextureTrackNormal_M);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureTrackHover_M, m_TextureTrackHover_M);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureTrackNormal_R, m_TextureTrackNormal_R);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureTrackHover_R, m_TextureTrackHover_R);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureThumbNormal, m_TextureThumbNormal);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureThumbHover, m_TextureThumbHover);
	}

	public void destroy() {
		super.destroy();
		
        if (m_TextureTrackNormal_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackNormal_L);
        }
        if (m_TextureTrackHover_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackHover_L);
        }
        if (m_TextureTrackNormal_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackNormal_M);
        }
        if (m_TextureTrackHover_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackHover_M);
        }
        if (m_TextureTrackNormal_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackNormal_R);
        }
        if (m_TextureTrackHover_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackHover_R);
        }
        if (m_TextureThumbNormal.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureThumbNormal);
        }
        if (m_TextureThumbHover.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureThumbHover);
        }
	}

	public Slider assign(Slider right) {
        if (this != right) {
            Slider temp = new Slider(right);
            //this->Widget::operator=(right);
            super.assign(right);
            
            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_MouseDownOnThumb = temp.m_MouseDownOnThumb;
            m_MouseDownOnThumbPos = temp.m_MouseDownOnThumbPos;
            m_Minimum = temp.m_Minimum;
            m_Maximum = temp.m_Maximum;
            m_Value = temp.m_Value;
            m_VerticalScroll = temp.m_VerticalScroll;
            m_VerticalImage = temp.m_VerticalImage;
            m_SplitImage = temp.m_SplitImage;
            m_SeparateHoverImage = temp.m_SeparateHoverImage;
            m_Size = temp.m_Size;
            m_ThumbSize = temp.m_ThumbSize;
            m_TextureTrackNormal_L = temp.m_TextureTrackNormal_L;
            m_TextureTrackHover_L = temp.m_TextureTrackHover_L;
            m_TextureTrackNormal_M = temp.m_TextureTrackNormal_M;
            m_TextureTrackHover_M = temp.m_TextureTrackHover_M;
            m_TextureTrackNormal_R = temp.m_TextureTrackNormal_R;
            m_TextureTrackHover_R = temp.m_TextureTrackHover_R;
            m_TextureThumbNormal = temp.m_TextureThumbNormal;
            m_TextureThumbHover = temp.m_TextureThumbHover;
        }

        return this;
	}

	public Slider cloneObj() {
        return new Slider(this);
	}

	public boolean load(String configFileFilename) {
        m_LoadedConfigFile = configFileFilename;

        m_Loaded = false;

        if (m_TextureTrackNormal_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackNormal_L);
        }
        if (m_TextureTrackHover_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackHover_L);
        }
        if (m_TextureTrackNormal_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackNormal_M);
        }
        if (m_TextureTrackHover_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackHover_M);
        }
        if (m_TextureTrackNormal_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackNormal_R);
        }
        if (m_TextureTrackHover_R.data != null) { 
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackHover_R);
        }
        if (m_TextureThumbNormal.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureThumbNormal);
        }
        if (m_TextureThumbHover.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureThumbHover);
        }

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("Slider", properties, values)) {
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

            if ("separatehoverimage".equals(property)) {
                m_SeparateHoverImage = configFile.readBool(value, false);
            } else if ("verticalscroll".equals(property)) {
                m_VerticalScroll = configFile.readBool(value, false);
                m_VerticalImage = m_VerticalScroll;
            } else if ("tracknormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackNormal_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackNormalImage in section Slider in " + configFileFilename + ".");
                    return false;
                }

                m_SplitImage = false;
            } else if ("trackhoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackHover_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackHoverImage in section Slider in " + configFileFilename + ".");
                    return false;
                }
            } else if ("thumbnormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureThumbNormal)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ThumbNormalImage in section Slider in " + configFileFilename + ".");
                    return false;
                }
            } else if ("thumbhoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureThumbHover)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ThumbHoverImage in section Slider in " + configFileFilename + ".");
                    return false;
                }
            } else if ("tracknormalimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackNormal_L)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackNormalImage_L in section Slider in " + configFileFilename + ".");
                    return false;
                }
            } else if ("tracknormalimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackNormal_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackNormalImage_M in section Slider in " + configFileFilename + ".");
                    return false;
                }

                m_SplitImage = true;
            } else if ("tracknormalimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackNormal_R)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackNormalImage_R in section Slider in " + configFileFilename + ".");
                    return false;
                }
            } else if ("trackhoverimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackHover_L)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackHoverImage_L in section Slider in " + configFileFilename + ".");
                    return false;
                }
            } else if ("trackhoverimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackHover_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackHoverImage_M in section Slider in " + configFileFilename + ".");
                    return false;
                }
            } else if ("trackhoverimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackHover_R)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackHoverImage_R in section Slider in " + configFileFilename + ".");
                    return false;
                }
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section Slider in " + configFileFilename + ".");
            }
        }

        if (m_SplitImage) {
            Defines.TGUI_OUTPUT("TGUI error: SplitImage is not supported in Slider.");
            return false;
        } else {
            if ((m_TextureTrackNormal_M.data != null) && 
            	(m_TextureThumbNormal.data != null)) {
                m_Size = new Vector2f(m_TextureTrackNormal_M.getSize());

                m_ThumbSize = new Vector2f(m_TextureThumbNormal.getSize());
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the slider. Is the Slider section in " + configFileFilename + " complete?");
                return false;
            }

            if ((m_TextureTrackHover_M.data != null) && 
            	(m_TextureThumbHover.data != null)) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();
            }
        }

        return m_Loaded = true;
	}

	public String getLoadedConfigFile() {
        return m_LoadedConfigFile;
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

        if (m_SplitImage) {
            if (m_VerticalImage) {
                float scalingX = m_Size.x / m_TextureTrackNormal_M.getSize().x;
                float minimumHeight = (m_TextureTrackNormal_L.getSize().y + m_TextureTrackNormal_R.getSize().y) * scalingX;
                if (m_Size.y < minimumHeight) {
                    m_Size.y = minimumHeight;
                }

                m_TextureTrackNormal_M.sprite.setTextureRect(new IntRect(0, 0, m_TextureTrackNormal_M.getSize().x, (int)((m_Size.y - minimumHeight) / scalingX)));
                m_TextureTrackHover_M.sprite.setTextureRect(new IntRect(0, 0, m_TextureTrackHover_M.getSize().x, (int)((m_Size.y - minimumHeight) / scalingX)));
            } else {
                float scalingY = m_Size.y / m_TextureTrackNormal_M.getSize().y;
                float minimumWidth = (m_TextureTrackNormal_L.getSize().x + m_TextureTrackNormal_R.getSize().x) * scalingY;
                if (m_Size.x < minimumWidth) {
                    m_Size.x = minimumWidth;
                }

                m_TextureTrackNormal_M.sprite.setTextureRect(new IntRect(0, 0, (int)((m_Size.x - minimumWidth) / scalingY), m_TextureTrackNormal_M.getSize().y));
                m_TextureTrackHover_M.sprite.setTextureRect(new IntRect(0, 0, (int)((m_Size.x - minimumWidth) / scalingY), m_TextureTrackHover_M.getSize().y));
            }
        }

        if (m_VerticalImage == m_VerticalScroll) {
            if (m_VerticalScroll) {
                m_ThumbSize.x = (m_Size.x / m_TextureTrackNormal_M.getSize().x) * m_TextureThumbNormal.getSize().x;
                m_ThumbSize.y = (m_Size.x / m_TextureTrackNormal_M.getSize().x) * m_TextureThumbNormal.getSize().y;
            } else {
                m_ThumbSize.x = (m_Size.y / m_TextureTrackNormal_M.getSize().y) * m_TextureThumbNormal.getSize().x;
                m_ThumbSize.y = (m_Size.y / m_TextureTrackNormal_M.getSize().y) * m_TextureThumbNormal.getSize().y;
            }
        } else {
            if (m_VerticalScroll) {
                m_ThumbSize.x = (m_Size.x / m_TextureTrackNormal_M.getSize().y) * m_TextureThumbNormal.getSize().y;
                m_ThumbSize.y = (m_Size.x / m_TextureTrackNormal_M.getSize().y) * m_TextureThumbNormal.getSize().x;
            } else {
                m_ThumbSize.x = (m_Size.y / m_TextureTrackNormal_M.getSize().x) * m_TextureThumbNormal.getSize().y;
                m_ThumbSize.y = (m_Size.y / m_TextureTrackNormal_M.getSize().x) * m_TextureThumbNormal.getSize().x;
            }
        }
	}

	public Vector2f getSize() {
        if (m_Loaded) {
            return new Vector2f(m_Size.x, m_Size.y);
        } else {
            return new Vector2f(0, 0);
        }
    }

	public void setMinimum(int minimum) {
        m_Minimum = minimum;

        if (m_Value < m_Minimum) {
            setValue(m_Minimum);
        }

        if (m_Maximum < m_Minimum) {
            m_Maximum = m_Minimum;
        }
	}

	public void setMaximum(int maximum) {
        if (maximum > 0) {
            m_Maximum = maximum;
        } else {
            m_Maximum = 1;
        }
        
        if (m_Value > m_Maximum) {
            setValue(m_Maximum);
        }

        if (m_Minimum > m_Maximum) {
            m_Minimum = m_Maximum;
        }
	}
	
	public void setValue(int value) {
		if (m_Value != value) {
            m_Value = value;

            if (m_Value < m_Minimum) {
                m_Value = m_Minimum;
            } else if (m_Value > m_Maximum) {
                m_Value = m_Maximum;
            }

            if (this.getCallbackManager().m_CallbackFunctions.get(SliderCallbacks.ValueChanged.value()).isEmpty() == false) {
                this.getCallbackManager().m_Callback.trigger = SliderCallbacks.ValueChanged.value();
                this.getCallbackManager().m_Callback.value = (int)(m_Value);
                addCallback();
            }
        }
	}

	public void setVerticalScroll(boolean verticalScroll) {
        if (m_VerticalScroll != verticalScroll) {
            m_VerticalScroll = verticalScroll;

            if (m_VerticalScroll) {
                if (m_Size.x > m_Size.y) {
                    setSize(m_Size.y, m_Size.x);
            	} else {
                    setSize(m_Size.x, m_Size.y);
                }
            } else {
                if (m_Size.y > m_Size.x) {
                    setSize(m_Size.y, m_Size.x);
                } else {
                    setSize(m_Size.x, m_Size.y);
                }
            }
        }
	}

	public int getMinimum() {
        return m_Minimum;
	}

	public int getMaximum() {
        return m_Maximum;
	}

	public int getValue() {
        return m_Value;
	}

	public boolean getVerticalScroll() {
        return m_VerticalScroll;
	}

	public void setTransparency(int transparency) {
        //Widget::
        super.setTransparency(transparency);

        m_TextureTrackNormal_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureTrackHover_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureTrackNormal_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureTrackHover_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureTrackNormal_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureTrackHover_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureThumbNormal.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureThumbHover.sprite.setColor(new Color(255, 255, 255, m_Opacity));
	}

	public void leftMousePressed(float x, float y) {
        m_MouseDown = true;

        mouseMoved(x, y);
	}

	public void leftMouseReleased(float x, float y) {
        m_MouseDown = false;
	}

	public void mouseMoved(float x, float y) {
		 if (m_Loaded == false) {
	         return;
		 }
	
	     if (m_MouseHover == false) {
	         mouseEnteredWidget();
	     }
	
	     m_MouseHover = true;
	
	     Vector2f position = getPosition();
	
	     if (m_MouseDown) {
	         if (m_VerticalScroll) {
	             if (m_MouseDownOnThumb) {
	                 if ((y - m_MouseDownOnThumbPos.y + (m_ThumbSize.y / 2.0f) - position.y) > 0) {
	                     setValue((int)((((y - m_MouseDownOnThumbPos.y + (m_ThumbSize.y / 2.0f) - position.y) / m_Size.y) * (m_Maximum - m_Minimum)) + m_Minimum + 0.5f));
	             	 } else {
	                     setValue(m_Minimum);
	                 }
	             } else {
	                 if ((y - position.y) > 0) {
	                     setValue((int)((((y - position.y) / m_Size.y) * (m_Maximum - m_Minimum)) + m_Minimum + 0.5f));
	                 } else { 
	                	 setValue(m_Minimum);
	                 }
	             }
	         } else {
	             if (m_MouseDownOnThumb) {
	                 if ((x - m_MouseDownOnThumbPos.x + (m_ThumbSize.x / 2.0f) - position.x) > 0) {
	                     setValue((int)((((x - m_MouseDownOnThumbPos.x + (m_ThumbSize.x / 2.0f) - position.x) / m_Size.x) * (m_Maximum - m_Minimum)) + m_Minimum + 0.5f));
	                 } else {
	                     setValue(m_Minimum);
	                 }
	             } else {
	                 if (x - position.x > 0) {
	                     setValue((int)((((x - position.x) / m_Size.x) * (m_Maximum - m_Minimum)) + m_Minimum + 0.5f));
	                 } else {
	                	 setValue(m_Minimum);
	                 }
	             }
	         }
	     }
	}

	public void keyPressed(Keyboard.Key key) {
        /// TODO: Respond on arrow presses
	}

	public void mouseWheelMoved(int delta, int x, int y) {
        if ((int)(m_Value) - delta < (int)(m_Minimum)) {
            setValue(m_Minimum);
        } else {
            setValue((int)(m_Value - delta));
        }
	}

    public void widgetFocused() {
        unfocus();
    }

    public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            load(value);
        } else if ("minimum".equals(property)) {
            setMinimum(Integer.parseInt(value.trim()));
        } else if ("maximum".equals(property)) {
            setMaximum(Integer.parseInt(value.trim()));
        } else if ("value".equals(property)) {
            setValue(Integer.parseInt(value.trim()));
        } else if ("verticalscroll".equals(property)) {
            if ((value == "true") || (value == "True")) {
                setVerticalScroll(true);
            } else if ((value == "false") || (value == "False")) {
                setVerticalScroll(false);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'VerticalScroll' property.");
            }
        } else if ("callback".equals(property)) {
            //Widget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("ValueChanged".equals(it)) || 
                	("valuechanged".equals(it))) {
                    this.getCallbackManager()
                    	.bindCallback(SliderCallbacks.ValueChanged.value());
                }
            }
        } else {
        	//Widget::
            return super.setProperty(property, value);
        }
        return true;
    }

    public boolean getProperty(String property, String[] value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            value[0] = getLoadedConfigFile();
        } else if ("minimum".equals(property)) {
            value[0] = Integer.toString(getMinimum());
        } else if ("maximum".equals(property)) {
            value[0] = Integer.toString(getMaximum());
        } else if ("value".equals(property)) {
            value[0] = Integer.toString(getValue());
        } else if ("verticalscroll".equals(property)) {
            value[0] = m_VerticalScroll ? "true" : "false";
        } else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //Widget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(SliderCallbacks.ValueChanged.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(SliderCallbacks.ValueChanged.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(SliderCallbacks.ValueChanged.value()).get(0) == null)) {
                callbacks.add("ValueChanged");
			}
            
            Defines.encodeList(callbacks, value);

            if (value[0].length() == 0) {
                value[0] = tempValue[0];
            } else if (tempValue[0].length() != 0) {
                value[0] += "," + tempValue[0];
            }
        } else {
        	//Widget::
            return super.getProperty(property, value);
        }
        
        return true;
    }

    public List<Pair<String, String>> getPropertyList() {
        //Widget::
    	List<Pair<String, String>> list = super.getPropertyList();
        list.add(new Pair<String, String>("ConfigFile", "string"));
        list.add(new Pair<String, String>("Minimum", "uint"));
        list.add(new Pair<String, String>("Maximum", "uint"));
        list.add(new Pair<String, String>("Value", "uint"));
        list.add(new Pair<String, String>("VerticalScroll", "bool"));
        return list;
    }

    public void draw(RenderTarget target, RenderStates states) {
        if (m_Loaded == false) {
            return;
        }

        Vector2f scaling = new Vector2f();

        Transform.multiplyEqual(states.transform, getTransform());

        Transform oldTransform = states.transform;

        if (m_SplitImage) {
            if (m_VerticalScroll == m_VerticalImage) {
                scaling.x = m_Size.x / (m_TextureTrackNormal_L.getSize().x + m_TextureTrackNormal_M.getSize().x + m_TextureTrackNormal_R.getSize().x);
                scaling.y = m_Size.y / m_TextureTrackNormal_M.getSize().y;
            } else {
                if ((m_VerticalImage == true) && (m_VerticalScroll == false)) {
                    states.transform.rotate(-90,
                                            (m_TextureTrackNormal_L.getSize().x + m_TextureTrackNormal_M.getSize().x + m_TextureTrackNormal_R.getSize().x) * 0.5f,
                                            m_TextureTrackNormal_M.getSize().x * 0.5f);
                } else {
                    states.transform.rotate(90,
                                            (m_TextureTrackNormal_L.getSize().y + m_TextureTrackNormal_M.getSize().y + m_TextureTrackNormal_R.getSize().y) * 0.5f,
                                            m_TextureTrackNormal_M.getSize().y * 0.5f);
                }

                scaling.x = m_Size.x / (m_TextureTrackNormal_L.getSize().y + m_TextureTrackNormal_M.getSize().y + m_TextureTrackNormal_R.getSize().y);
                scaling.y = m_Size.y / m_TextureTrackNormal_M.getSize().x;
            }

            states.transform.scale(scaling.y, scaling.y);

            {
                if (m_SeparateHoverImage) {
                    if ((m_MouseHover) && 
                    	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureTrackHover_L.getSprite(), states);
                    } else {
                        target.draw(m_TextureTrackNormal_L.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureTrackNormal_L.getSprite(), states);

                    if ((m_MouseHover) && 
                    	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureTrackHover_L.getSprite(), states);
                    }
                }
            }

            if ((scaling.y * (m_TextureTrackNormal_L.getSize().x + m_TextureTrackNormal_R.getSize().x)) < 
            	scaling.x * (m_TextureTrackNormal_L.getSize().x + m_TextureTrackNormal_M.getSize().x + m_TextureTrackNormal_R.getSize().x)) {
                float scaleX = (((m_TextureTrackNormal_L.getSize().x + m_TextureTrackNormal_M.getSize().x + m_TextureTrackNormal_R.getSize().x)  * scaling.x)
                                 - ((m_TextureTrackNormal_L.getSize().x + m_TextureTrackNormal_R.getSize().x) * scaling.y))
                               / m_TextureTrackNormal_M.getSize().x;

                states.transform.translate((float)(m_TextureTrackNormal_L.getSize().x), 0);

                states.transform.scale(scaleX / scaling.y, 1);

                {
                    if (m_SeparateHoverImage) {
                        if ((m_MouseHover) && 
                        	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                            target.draw(m_TextureTrackHover_M.getSprite(), states);
                    	} else {
                            target.draw(m_TextureTrackNormal_M.getSprite(), states);
                    	}
                    } else {
                        target.draw(m_TextureTrackNormal_M.getSprite(), states);

                        if ((m_MouseHover) && 
                        	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                            target.draw(m_TextureTrackHover_M.getSprite(), states);
                        }
                    }
                }

                states.transform.translate((float)(m_TextureTrackNormal_M.getSize().x), 0);

                states.transform.scale(scaling.y / scaleX, 1);
            } else {
                states.transform.translate((float)(m_TextureTrackNormal_L.getSize().x), 0);
            }

            {
                if (m_SeparateHoverImage) {
                    if ((m_MouseHover) && 
                    	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureTrackHover_R.getSprite(), states);
                	} else {
                        target.draw(m_TextureTrackNormal_R.getSprite(), states);
                	}
                } else {
                    target.draw(m_TextureTrackNormal_R.getSprite(), states);

                    if ((m_MouseHover) && 
                    	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureTrackHover_R.getSprite(), states);
                    }
                }
            }
        } else {
            if (m_VerticalScroll == m_VerticalImage) {
                scaling.x = m_Size.x / m_TextureTrackNormal_M.getSize().x;
                scaling.y = m_Size.y / m_TextureTrackNormal_M.getSize().y;
                states.transform.scale(scaling);
            } else {
                scaling.x = m_Size.x / m_TextureTrackNormal_M.getSize().y;
                scaling.y = m_Size.y / m_TextureTrackNormal_M.getSize().x;
                states.transform.scale(scaling);

                if ((m_VerticalImage == true) && (m_VerticalScroll == false)) {
                    states.transform.rotate(-90, m_TextureTrackNormal_M.getSize().x * 0.5f, m_TextureTrackNormal_M.getSize().x * 0.5f);
                } else {
                    states.transform.rotate(90, m_TextureTrackNormal_M.getSize().y * 0.5f, m_TextureTrackNormal_M.getSize().y * 0.5f);
                }
            }

            target.draw(m_TextureTrackNormal_M.getSprite(), states);

            if ((m_MouseHover) && 
            	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                target.draw(m_TextureTrackHover_M.getSprite(), states);
            }
        }

        states.transform = oldTransform;

        if (m_VerticalScroll) {
            states.transform.translate((int)(m_Size.x - m_ThumbSize.x) * 0.5f,
                                       (((float)(m_Value - m_Minimum) / (m_Maximum - m_Minimum)) * m_Size.y) - (m_ThumbSize.y * 0.5f));

            states.transform.scale(scaling.x, scaling.x);
        } else {
            states.transform.translate((((float)(m_Value - m_Minimum) / (m_Maximum - m_Minimum)) * m_Size.x) - (m_ThumbSize.x * 0.5f),
                                        (m_Size.y - m_ThumbSize.y) * 0.5f);

            states.transform.scale(scaling.y, scaling.y);
        }

        if ((m_VerticalImage == true) && (m_VerticalScroll == false)) {
            states.transform.rotate(-90, m_TextureThumbNormal.getSize().x * 0.5f, m_TextureThumbNormal.getSize().x * 0.5f);
        } else if ((m_VerticalImage == false) && (m_VerticalScroll == true)) {
            states.transform.rotate(90, m_TextureThumbNormal.getSize().y * 0.5f, m_TextureThumbNormal.getSize().y * 0.5f);
        }

        target.draw(m_TextureThumbNormal.getSprite(), states);

        if ((m_MouseHover) && 
        	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
            target.draw(m_TextureThumbHover.getSprite(), states);
        }
    }
    
	@Override
	public boolean mouseOnWidget(float x, float y) {
        if (m_Loaded == false) {
            return false;
        }

        float thumbWidth, thumbHeight;
        float thumbLeft, thumbTop;

        Vector2f position = getPosition();

        if (m_VerticalImage == m_VerticalScroll) {
            thumbWidth = m_ThumbSize.x;
            thumbHeight = m_ThumbSize.y;
        } else {
            thumbWidth = m_ThumbSize.y;
            thumbHeight = m_ThumbSize.x;
        }

        if (m_VerticalScroll) {
            thumbLeft = (m_Size.x - thumbWidth) / 2.0f;
            thumbTop = (((float)(m_Value - m_Minimum) / (m_Maximum - m_Minimum)) * m_Size.y) - (thumbHeight / 2.0f);
        } else {
            thumbLeft = (((float)(m_Value - m_Minimum) / (m_Maximum - m_Minimum)) * m_Size.x) - (thumbWidth / 2.0f);
            thumbTop = (m_Size.y - thumbHeight) / 2.0f;
        }

        if (new FloatRect(position.x + thumbLeft, position.y + thumbTop, thumbWidth, thumbHeight)
        	.contains(x, y)) {
            m_MouseDownOnThumb = true;
            m_MouseDownOnThumbPos.x = x - position.x - thumbLeft;
            m_MouseDownOnThumbPos.y = y - position.y - thumbTop;
            return true;
        } else {
            m_MouseDownOnThumb = false;
        }
        
        if (getTransform().transformRect(
        		new FloatRect(0, 0, m_Size.x, m_Size.y))
        			.contains(x, y)) {
        	return true;
        }

        if (m_MouseHover) {
            mouseLeftWidget();
        }
        
        m_MouseHover = false;
        return false;
	}
}
