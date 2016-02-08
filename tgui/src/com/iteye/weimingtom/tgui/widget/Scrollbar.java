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
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20151023
 * @author Administrator
 *
 */
public class Scrollbar extends Slider {
    public static enum ScrollbarCallbacks {
        AllScrollbarCallbacks(SliderCallbacks.SliderCallbacksCount.value() - 1),
        ScrollbarCallbacksCount(SliderCallbacks.SliderCallbacksCount.value());
        
        int value;
        
        ScrollbarCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }

    protected int m_LowValue;

    protected boolean m_AutoHide;

    protected boolean m_MouseDownOnArrow;

    protected Texture m_TextureArrowUpNormal;
    protected Texture m_TextureArrowUpHover;

    protected Texture m_TextureArrowDownNormal;
    protected Texture m_TextureArrowDownHover;
	
	public Scrollbar() {
	    m_LowValue = 6;
	    m_AutoHide = true;
	    m_MouseDownOnArrow = false;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_Scrollbar;
	}

	public Scrollbar(Scrollbar copy) {
	    //Slider            (copy),
	    super(copy);
		m_LowValue = copy.m_LowValue;
	    m_AutoHide = copy.m_AutoHide;
	    m_MouseDownOnArrow = copy.m_MouseDownOnArrow;
	    
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureArrowUpNormal, m_TextureArrowUpNormal);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureArrowUpHover, m_TextureArrowUpHover);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureArrowDownNormal, m_TextureArrowDownNormal);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureArrowDownHover, m_TextureArrowDownHover);
	}

	public void destroy() {
		super.destroy();
		
        if (m_TextureArrowUpNormal.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureArrowUpNormal);
        }
        if (m_TextureArrowUpHover.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureArrowUpHover);
        }
        if (m_TextureArrowDownNormal.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureArrowDownNormal);
        }
        if (m_TextureArrowDownHover.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureArrowDownHover);
        }
	}

	public Scrollbar assign(Scrollbar right) {
        if (this != right) {
            Scrollbar temp = new Scrollbar(right);
            //this->Slider::operator=(right);
            super.assign(right);
            
            m_LowValue = temp.m_LowValue;
            m_AutoHide = temp.m_AutoHide;
            m_MouseDownOnArrow = temp.m_MouseDownOnArrow;
            m_TextureArrowUpNormal = temp.m_TextureArrowUpNormal;
            m_TextureArrowUpHover = temp.m_TextureArrowUpHover;
            m_TextureArrowDownNormal = temp.m_TextureArrowDownNormal;
            m_TextureArrowDownHover = temp.m_TextureArrowDownHover;
        }

        return this;
	}

	public Scrollbar cloneObj() {
        return new Scrollbar(this);
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
        if (m_TextureArrowUpNormal.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureArrowUpNormal);
        }
        if (m_TextureArrowUpHover.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureArrowUpHover);
        }
        if (m_TextureArrowDownNormal.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureArrowDownNormal);
        }
        if (m_TextureArrowDownHover.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureArrowDownHover);
        }

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)){
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("Scrollbar", properties, values)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to parse " + configFileFilename + ".");
            return false;
        }

        // Close the config file
        configFile.close();

        String configFileFolder = "";
        int slashPos = configFileFilename.lastIndexOf("/");
        if (slashPos == -1) {
        	slashPos = configFileFilename.lastIndexOf("\\"); 
        }
        if (slashPos != -1) {
            configFileFolder = configFileFilename.substring(0, slashPos+1);
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
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackNormalImage in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }

                m_SplitImage = false;
            } else if ("trackhoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackHover_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackHoverImage in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("thumbnormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureThumbNormal))
                {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ThumbNormalImage in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("thumbhoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureThumbHover)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ThumbHoverImage in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("arrowupnormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowUpNormal)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowUpNormalImage in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("arrowuphoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowUpHover)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowUpHoverImage in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("arrowdownnormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowDownNormal)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowDownNormalImage in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("arrowdownhoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowDownHover)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowDownHoverImage in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("tracknormalimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackNormal_L)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackNormalImage_L in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("tracknormalimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackNormal_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackNormalImage_M in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }

                m_SplitImage = true;
            } else if ("tracknormalimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackNormal_R)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackNormalImage_R in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("trackhoverimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackHover_L)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackHoverImage_L in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("trackhoverimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackHover_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackHoverImage_M in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("trackhoverimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackHover_R)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackHoverImage_R in section Scrollbar in " + configFileFilename + ".");
                    return false;
                }
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section Scrollbar in " + configFileFilename + ".");
            }
        }

        if (m_SplitImage) {
            Defines.TGUI_OUTPUT("TGUI error: SplitImage is not supported in Scrollbar.");
            return false;
        } else {
            if ((m_TextureTrackNormal_M.data != null) && 
            	(m_TextureThumbNormal.data != null) && 
            	(m_TextureArrowUpNormal.data != null) && 
            	(m_TextureArrowDownNormal.data != null)) {
                m_Loaded = true;
                setSize((float)(m_TextureTrackNormal_M.getSize().x), (float)(m_TextureTrackNormal_M.getSize().y));
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the scrollbar. Is the Scrollbar section in " + configFileFilename + " complete?");
                return false;
            }

            if ((m_TextureTrackHover_M.data != null) && 
            	(m_TextureThumbHover.data != null) && 
            	(m_TextureArrowUpHover.data != null) && 
            	(m_TextureArrowDownHover.data != null)) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();
            }
        }

        return true;
	}

	public void setMinimum(int minimum) {
        // Do nothing. The minimum may not be changed.
	}

	public void setMaximum(int maximum) {
        //Slider::
		super.setMaximum(maximum);

        if (m_Maximum < m_LowValue) {
            setValue(0);
        } else if (m_Value > m_Maximum - m_LowValue) {
            setValue(m_Maximum - m_LowValue);
        }
	}

	public void setValue(int value) {
        if (m_Value != value) {
            m_Value = value;

            if (m_Maximum < m_LowValue) {
                m_Value = 0;
            } else if (m_Value > m_Maximum - m_LowValue) {
                m_Value = m_Maximum - m_LowValue;
            }

            if (this.getCallbackManager().m_CallbackFunctions
            		.get(SliderCallbacks.ValueChanged.value())
            		.isEmpty() == false) {
            	this.getCallbackManager().m_Callback.trigger = SliderCallbacks.ValueChanged.value();
            	this.getCallbackManager().m_Callback.value = (int)(m_Value);
                addCallback();
            }
        }
	}

	public void setLowValue(int lowValue) {
        m_LowValue = lowValue;

        if (m_Maximum < m_LowValue) {
            setValue(0);
        } else if (m_Value > m_Maximum - m_LowValue) {
            setValue(m_Maximum - m_LowValue);
        }
	}

	public int getLowValue() {
        return m_LowValue;
	}

	public void setAutoHide(boolean autoHide) {
        m_AutoHide = autoHide;
	}

	public boolean getAutoHide() {
        return m_AutoHide;
	}

	public void setTransparency(int transparency) {
        //Slider::
        super.setTransparency(transparency);

        m_TextureArrowUpNormal.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureArrowUpHover.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureArrowDownNormal.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureArrowDownHover.sprite.setColor(new Color(255, 255, 255, m_Opacity));
	}

	public boolean mouseOnWidget(float x, float y) {
        if (m_Loaded == false) {
            return false;
        }

        if ((m_Maximum <= m_LowValue) && (m_AutoHide == true)) {
            return false;
        }

        if (getTransform().transformRect(
        		new FloatRect(0, 0, m_Size.x, m_Size.y))
        		.contains(x, y)) {
            Vector2f position = getPosition();

            float thumbLeft = 0;
            float thumbTop = 0;

            float thumbWidth = m_ThumbSize.x;
            float thumbHeight = m_ThumbSize.y;

            if (m_VerticalScroll) {
                float scalingX;
                if (m_VerticalImage == m_VerticalScroll) {
                    scalingX = m_Size.x / m_TextureTrackNormal_M.getSize().x;
                } else {
                    scalingX = m_Size.x / m_TextureTrackNormal_M.getSize().y;
                }
                
                if (m_Size.y > (m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scalingX) {
                    float realTrackHeight = m_Size.y - ((m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scalingX);
                    thumbHeight = (((float)(m_LowValue) / m_Maximum) * realTrackHeight);

                    thumbTop = (m_TextureArrowUpNormal.getSize().y * scalingX) + (((float)(m_Value) / (m_Maximum - m_LowValue)) * (realTrackHeight - thumbHeight));
                } else {
                    thumbHeight = 0;
                    thumbTop = (float)(m_TextureArrowUpNormal.getSize().y);
                }
            } else {
                float scalingY;
                if (m_VerticalImage == m_VerticalScroll) {
                    scalingY = m_Size.y / m_TextureTrackNormal_M.getSize().y;
                } else {
                    scalingY = m_Size.y / m_TextureTrackNormal_M.getSize().x;
                }
                
                if (m_Size.x > (m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scalingY) {
                    float realTrackWidth = m_Size.x - ((m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scalingY);
                    thumbWidth = (((float)(m_LowValue) / m_Maximum) * realTrackWidth);

                    thumbLeft = (m_TextureArrowUpNormal.getSize().y * scalingY) + (((float)(m_Value) / (m_Maximum - m_LowValue)) * (realTrackWidth - thumbWidth));
                } else {
                    thumbWidth = 0;
                    thumbLeft = (float)(m_TextureArrowUpNormal.getSize().y);
                }
            }

            if (new FloatRect(position.x + thumbLeft, position.y + thumbTop, thumbWidth, thumbHeight)
            	.contains(x, y)) {
                if (m_MouseDown == false) {
                    m_MouseDownOnThumbPos.x = x - position.x - thumbLeft;
                    m_MouseDownOnThumbPos.y = y - position.y - thumbTop;
                }

                m_MouseDownOnThumb = true;
                return true;
            } else { 
            	m_MouseDownOnThumb = false;
            }
            
            return true;
        }

        if (m_MouseHover) {
            mouseLeftWidget();
        }
        
        m_MouseHover = false;
        return false;
	}

    public void leftMousePressed(float x, float y) {
        m_MouseDown = true;
        m_MouseDownOnArrow = false;

        if (m_VerticalScroll) {
            float scalingX;
            if (m_VerticalImage == m_VerticalScroll) {
                scalingX = m_Size.x / m_TextureTrackNormal_M.getSize().x;
            } else {
                scalingX = m_Size.x / m_TextureTrackNormal_M.getSize().y;
            }
            
            if (m_Size.y > (m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scalingX) {
                if (y < getPosition().y + (m_TextureArrowUpNormal.getSize().y * scalingX)) {
                    m_MouseDownOnArrow = true;
                } else if (y > getPosition().y + m_Size.y - (m_TextureArrowUpNormal.getSize().y * scalingX)) {
                    m_MouseDownOnArrow = true;
                }
            } else { 
            	m_MouseDownOnArrow = true;
            }
        } else {
            float scalingY;
            if (m_VerticalImage == m_VerticalScroll) {
                scalingY = m_Size.y / m_TextureTrackNormal_M.getSize().y;
            } else {
                scalingY = m_Size.y / m_TextureTrackNormal_M.getSize().x;
            }
            
            if (m_Size.x > (m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scalingY) {
                if (x < getPosition().x + (m_TextureArrowUpNormal.getSize().y * scalingY)) {
                    m_MouseDownOnArrow = true;
            	} else if (x > getPosition().x + m_Size.x - (m_TextureArrowUpNormal.getSize().y * scalingY)) {
                    m_MouseDownOnArrow = true;
            	}
            } else { 
            	m_MouseDownOnArrow = true;
            }
        }

        if (m_MouseDownOnArrow == false) {
            mouseMoved(x, y);
        }
    }

    public void leftMouseReleased(float x, float y) {
        if ((m_MouseDown) && (m_MouseDownOnArrow)) {
            if (m_Maximum > m_LowValue) {
                if (m_VerticalScroll) {
                    float scalingX;
                    if (m_VerticalImage == m_VerticalScroll) {
                        scalingX = m_Size.x / m_TextureTrackNormal_M.getSize().x;
                    } else {
                        scalingX = m_Size.x / m_TextureTrackNormal_M.getSize().y;
                    }
                    
                    if (m_Size.y > (m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scalingX) {
                        if (y < getPosition().y + (m_TextureArrowUpNormal.getSize().y * scalingX)) {
                            if (m_Value > 0) {
                                setValue(m_Value - 1);
                            }
                        } else if (y > getPosition().y + m_Size.y - (m_TextureArrowUpNormal.getSize().y * scalingX)) {
                            if (m_Value < (m_Maximum - m_LowValue)) {
                                setValue(m_Value + 1);
                            }
                        }
                    } else {
                        if (y < getPosition().y + (m_TextureArrowUpNormal.getSize().y * ((m_Size.y * 0.5f) / m_TextureArrowUpNormal.getSize().y))) {
                            if (m_Value > 0) {
                                setValue(m_Value - 1);
                            }
                        } else {
                            if (m_Value < (m_Maximum - m_LowValue)) {
                                setValue(m_Value + 1);
                            }
                        }
                    }
                } else {
                    float scalingY;
                    if (m_VerticalImage == m_VerticalScroll) {
                        scalingY = m_Size.y / m_TextureTrackNormal_M.getSize().y;
                    } else {
                        scalingY = m_Size.y / m_TextureTrackNormal_M.getSize().x;
                    }
                    
                    if (m_Size.x > (m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scalingY) {
                        if (x < getPosition().x + (m_TextureArrowUpNormal.getSize().y * scalingY)) {
                            if (m_Value > 0) {
                                setValue(m_Value - 1);
                            }
                        } else if (x > getPosition().x + m_Size.x - (m_TextureArrowUpNormal.getSize().y * scalingY)) {
                            if (m_Value < (m_Maximum - m_LowValue)) {
                                setValue(m_Value + 1);
                            }
                        }
                    } else {
                        if (x < getPosition().x + (m_TextureArrowUpNormal.getSize().y * ((m_Size.x * 0.5f) / m_TextureArrowUpNormal.getSize().y))) {
                            if (m_Value > 0) {
                                setValue(m_Value - 1);
                            }
                        } else {
                            if (m_Value < (m_Maximum - m_LowValue)) {
                                setValue(m_Value + 1);
                            }
                        }
                    }
                }
            }
        }

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

        if ((m_MouseDown) && (m_MouseDownOnArrow == false)) {
            if ((m_Maximum <= m_LowValue) && (m_AutoHide == false)) {
                return;
            }

            Vector2f position = getPosition();

            if (m_VerticalScroll) {
                float scalingX;
                if (m_VerticalImage == m_VerticalScroll) {
                    scalingX = m_Size.x / m_TextureTrackNormal_M.getSize().x;
                } else {
                    scalingX = m_Size.x / m_TextureTrackNormal_M.getSize().y;
                }
                
                float arrowHeight = m_TextureArrowUpNormal.getSize().y * scalingX;

                if (m_MouseDownOnThumb) {
                    if ((y - m_MouseDownOnThumbPos.y - position.y - arrowHeight) > 0) {
                        int value = (int)((((y - m_MouseDownOnThumbPos.y - position.y - arrowHeight) / (m_Size.y - (2 * arrowHeight))) * m_Maximum) + 0.5f);

                        if (value <= (m_Maximum - m_LowValue)) {
                            setValue(value);
                        } else {
                            setValue(m_Maximum - m_LowValue);
                        }
                    } else {
                        setValue(0);
                    }
                } else {
                    if (y > position.y + arrowHeight) {
                        if (y <= position.y + m_Size.y - arrowHeight) {
                            float value = (((y - position.y - arrowHeight) / (m_Size.y - (2 * arrowHeight))) * m_Maximum);

                            if (value <= m_Value) {
                                float subtractValue = m_LowValue / 3.0f;

                                if (value >= subtractValue) {
                                    setValue((int)(value - subtractValue + 0.5f));
                                } else {
                                    setValue(0);
                                }
                            } else {
                                float subtractValue = m_LowValue * 2.0f / 3.0f;

                                if (value <= (m_Maximum - m_LowValue + subtractValue)) {
                                    setValue((int)(value - subtractValue + 0.5f));
                            	} else {
                                    setValue(m_Maximum - m_LowValue);
                            	}
                            }
                        }
                    }
                }
            } else {
                float scalingY;
                if (m_VerticalImage == m_VerticalScroll) {
                    scalingY = m_Size.y / m_TextureTrackNormal_M.getSize().y;
            	} else {
                    scalingY = m_Size.y / m_TextureTrackNormal_M.getSize().x;
                }
                
                float arrowWidth = m_TextureArrowUpNormal.getSize().y * scalingY;

                if (m_MouseDownOnThumb) {
                    if ((x - m_MouseDownOnThumbPos.x - position.x - arrowWidth) > 0) {
                        int value = (int)((((x - m_MouseDownOnThumbPos.x - position.x - arrowWidth) / (m_Size.x - (2 * arrowWidth))) * m_Maximum) + 0.5f);

                        if (value <= (m_Maximum - m_LowValue)) {
                            setValue(value);
                    	} else {
                            setValue(m_Maximum - m_LowValue);
                        }
                    } else { 
                    	setValue(0);
                    }
                } else {
                    if (x > position.x + arrowWidth) {
                        if (x <= position.x + m_Size.x - arrowWidth) {
                            float value = (((x - position.x - arrowWidth) / (m_Size.x - (2 * arrowWidth))) * m_Maximum);

                            if (value <= m_Value) {
                                float subtractValue = m_LowValue / 3.0f;

                                if (value >= subtractValue) {
                                    setValue((int)(value - subtractValue + 0.5f));
                                } else {
                                    setValue(0);
                                }
                            } else {
                                float subtractValue = m_LowValue * 2.0f / 3.0f;

                                if (value <= (m_Maximum - m_LowValue + subtractValue)) {
                                    setValue((int)(value - subtractValue + 0.5f));
                                } else {
                                    setValue(m_Maximum - m_LowValue);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("lowvalue".equals(property)) {
            setLowValue(Integer.parseInt(value.trim()));
        } else if ("autohide".equals(property)) {
            if (("true".equals(value)) || 
            	("True".equals(value))) {
                setAutoHide(true);
            } else if (("false".equals(value)) || 
            	("False".equals(value))) {
                setAutoHide(false);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'AutoHide' property.");
            }
        } else {
        	//Slider::
            return super.setProperty(property, value);
        }
        
        return true;
    }

    public boolean getProperty(String property, String[] value) {
        property = Defines.toLower(property);

        if ("lowvalue".equals(property)) {
            value[0] = Integer.toString(getLowValue());
        } else if ("autohide".equals(property)) {
            value[0] = m_AutoHide ? "true" : "false";
        } else {
        	//Slider::
            return super.getProperty(property, value);
        }
        
        return true;
    }

    public List<Pair<String, String>> getPropertyList() {
    	//Widget::
    	List<Pair<String, String>> list = super.getPropertyList();

        list.add(new Pair<String, String>("Maximum", "uint"));
        list.add(new Pair<String, String>("LowValue", "uint"));
        list.add(new Pair<String, String>("Value", "uint"));
        list.add(new Pair<String, String>("AutoHide", "bool"));
        list.add(new Pair<String, String>("VerticalScroll", "bool"));
        return list;
    }

    public void draw(RenderTarget target, RenderStates states) {
        if (m_Loaded == false) {
            return;
        }

        if ((m_AutoHide == true) && (m_Maximum <= m_LowValue)) {
            return;
        }

        Vector2f scaling = new Vector2f();

        Transform.multiplyEqual(states.transform, getTransform());

        Transform oldTransform = states.transform;

        if (m_VerticalScroll == m_VerticalImage) {
            scaling.x = m_Size.x / m_TextureTrackNormal_M.getSize().x;
            scaling.y = m_Size.y / m_TextureTrackNormal_M.getSize().y;
            states.transform.scale(scaling);
        } else {
            scaling.x = m_Size.x / m_TextureTrackNormal_M.getSize().y;
            scaling.y = m_Size.y / m_TextureTrackNormal_M.getSize().x;
            states.transform.scale(scaling);

            states.transform.rotate(-90, m_TextureTrackNormal_M.getSize().x * 0.5f, m_TextureTrackNormal_M.getSize().x * 0.5f);
        }

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

        states.transform = oldTransform;

        if (m_VerticalScroll) {
            if (m_Size.y > (m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scaling.x) {
                states.transform.scale(scaling.x, scaling.x);

                if (m_SeparateHoverImage) {
                    if ((m_MouseHover) && 
                    	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    	target.draw(m_TextureArrowUpHover.getSprite(), states);
                    } else {
                        target.draw(m_TextureArrowUpNormal.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureArrowUpNormal.getSprite(), states);

                    if ((m_MouseHover) && 
                        ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureArrowUpHover.getSprite(), states);
                    }
                }

                float realTrackHeight = m_Size.y - ((m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scaling.x);

                float scaleY;
                if ((m_AutoHide == false) && (m_Maximum <= m_LowValue)) {
                    scaleY = realTrackHeight / m_ThumbSize.y;
                } else {
                    scaleY = ((((float)(m_LowValue) / m_Maximum)) * realTrackHeight) / m_ThumbSize.y;
                }
                
                if (m_VerticalImage) {
                    states.transform.translate(0, m_TextureArrowUpNormal.getSize().y + (m_Value * (realTrackHeight / m_Maximum)) / scaling.x);
                    states.transform.scale(1, scaleY);
                } else {
                    states.transform.rotate(90, m_TextureThumbNormal.getSize().y * 0.5f, m_TextureThumbNormal.getSize().y * 0.5f);
                    states.transform.translate(m_TextureArrowUpNormal.getSize().y + (m_Value * (realTrackHeight / m_Maximum)) / scaling.x, 0);
                    states.transform.scale(scaleY, 1);
                }

                if (m_SeparateHoverImage) {
                    if ((m_MouseHover) && 
                        ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureThumbHover.getSprite(), states);
                    } else {
                        target.draw(m_TextureThumbNormal.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureThumbNormal.getSprite(), states);

                    if ((m_MouseHover) && 
                        ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureThumbHover.getSprite(), states);
                    }
                }

                states.transform = oldTransform;

                states.transform.translate(0, m_Size.y - (m_TextureArrowDownNormal.getSize().y * scaling.x));

                states.transform.scale(scaling.x, scaling.x);
            } else {
                states.transform.scale(scaling.x, (m_Size.y * 0.5f) / m_TextureArrowUpNormal.getSize().y);

                if (m_SeparateHoverImage) {
                    if ((m_MouseHover) && 
                        ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureArrowUpHover.getSprite(), states);
                	} else {
                        target.draw(m_TextureArrowUpNormal.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureArrowUpNormal.getSprite(), states);

                    if ((m_MouseHover) && 
                        ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureArrowUpHover.getSprite(), states);
                    }
                }

                states.transform = oldTransform;

                states.transform.translate(0, m_Size.y - (m_TextureArrowDownNormal.getSize().y * scaling.x));

                states.transform.scale(scaling.x, (m_Size.y * 0.5f) / m_TextureArrowUpNormal.getSize().y);
            }

            if (m_SeparateHoverImage) {
                if ((m_MouseHover) && 
                    ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureArrowUpHover.getSprite(), states);
            	} else {
                    target.draw(m_TextureArrowDownNormal.getSprite(), states);
                }
            } else {
                target.draw(m_TextureArrowDownNormal.getSprite(), states);

                if ((m_MouseHover) && 
                    ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureArrowUpHover.getSprite(), states);
                }
            }
        } else {
            if (m_Size.x > (m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scaling.y) {
                states.transform.scale(scaling.y, scaling.y);

                states.transform.rotate(-90, m_TextureArrowUpNormal.getSize().x * 0.5f, m_TextureArrowUpNormal.getSize().x * 0.5f);

                if (m_SeparateHoverImage) {
                    if ((m_MouseHover) && 
                        ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureArrowUpHover.getSprite(), states);
                	} else {
                        target.draw(m_TextureArrowUpNormal.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureArrowUpNormal.getSprite(), states);

                    if ((m_MouseHover) && 
                        ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureArrowUpHover.getSprite(), states);
                    }
                }

                float realTrackWidth = m_Size.x - ((m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y) * scaling.y);

                float scaleX;
                if ((m_AutoHide == false) && (m_Maximum <= m_LowValue)) {
                    scaleX = realTrackWidth / m_ThumbSize.x;
            	} else {
                    scaleX = ((((float)(m_LowValue) / m_Maximum)) * realTrackWidth) / m_ThumbSize.x;
                }
                if (m_VerticalImage) {
                    states.transform.translate(0, m_TextureArrowUpNormal.getSize().y + (m_Value * (realTrackWidth / m_Maximum)) / scaling.y);
                    states.transform.scale(1, scaleX);
                } else {
                    states.transform.rotate(90, m_TextureThumbNormal.getSize().y * 0.5f, m_TextureThumbNormal.getSize().y * 0.5f);
                    states.transform.translate(m_TextureArrowUpNormal.getSize().y + (m_Value * (realTrackWidth / m_Maximum)) / scaling.y, 0);
                    states.transform.scale(scaleX, 1);
                }

                if (m_SeparateHoverImage) {
                    if ((m_MouseHover) && 
                        ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureThumbHover.getSprite(), states);
                	} else {
                        target.draw(m_TextureThumbNormal.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureThumbNormal.getSprite(), states);

                    if ((m_MouseHover) && 
                        ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureThumbHover.getSprite(), states);
                    }
                }

                states.transform = oldTransform;

                states.transform.translate(m_Size.x - (m_TextureArrowDownNormal.getSize().y * scaling.y), 0);

                states.transform.scale(scaling.y, scaling.y);
            } else {
                states.transform.scale((m_Size.x * 0.5f) / m_TextureArrowUpNormal.getSize().y, scaling.y);

                states.transform.rotate(-90, m_TextureArrowUpNormal.getSize().x * 0.5f, m_TextureArrowUpNormal.getSize().x * 0.5f);

                if (m_SeparateHoverImage) {
                    if ((m_MouseHover) && 
                        ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureArrowUpHover.getSprite(), states);
                    } else {
                        target.draw(m_TextureArrowUpNormal.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureArrowUpNormal.getSprite(), states);

                    if ((m_MouseHover) && 
                        ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                        target.draw(m_TextureArrowUpHover.getSprite(), states);
                    }
                }

                states.transform = oldTransform;

                states.transform.translate(m_Size.x - (m_TextureArrowDownNormal.getSize().y * scaling.y), 0);

                states.transform.scale((m_Size.x * 0.5f) / m_TextureArrowUpNormal.getSize().y, scaling.y);
            }

            states.transform.rotate(-90, m_TextureArrowUpNormal.getSize().x * 0.5f, m_TextureArrowUpNormal.getSize().x * 0.5f);

            if (m_SeparateHoverImage) {
                if ((m_MouseHover) && 
                	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureArrowUpHover.getSprite(), states);
            	} else {
                    target.draw(m_TextureArrowDownNormal.getSprite(), states);
                }
            } else {
                target.draw(m_TextureArrowDownNormal.sprite, states);

                if ((m_MouseHover) && 
                	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureArrowUpHover.sprite, states);
                }
            }
        }
    }
}
