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

/**
 * 20151006
 * @author Administrator
 *
 */
public class SpinButton extends ClickableWidget {
    public static enum SpinButtonCallbacks {
        ValueChanged(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 1),
        AllSpinButtonCallbacks(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 2 - 1), 
        SpinButtonCallbacksCount(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 2);
        
        int value;
        
        SpinButtonCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }
    
    protected String m_LoadedConfigFile;

    protected boolean m_VerticalScroll;

    protected int m_Minimum;
    protected int m_Maximum;
    protected int m_Value;

    protected boolean m_SeparateHoverImage;

    protected boolean m_MouseHoverOnTopArrow;
    protected boolean m_MouseDownOnTopArrow;

    protected Texture m_TextureArrowUpNormal;
    protected Texture m_TextureArrowUpHover;
    protected Texture m_TextureArrowDownNormal;
    protected Texture m_TextureArrowDownHover;
	
	public SpinButton() {
	    m_VerticalScroll = true;
	    m_Minimum = 0;
	    m_Maximum = 10;
	    m_Value = 0;
	    m_SeparateHoverImage = false;
	    m_MouseHoverOnTopArrow = false;
	    m_MouseDownOnTopArrow = false;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_SpinButton;
	}

	public SpinButton(SpinButton copy) {
	    super(copy);
	    m_LoadedConfigFile = copy.m_LoadedConfigFile;
	    m_VerticalScroll = copy.m_VerticalScroll;
	    m_Minimum = copy.m_Minimum;
	    m_Maximum = copy.m_Maximum;
	    m_Value = copy.m_Value;
	    m_SeparateHoverImage = copy.m_SeparateHoverImage;
	    m_MouseHoverOnTopArrow = copy.m_MouseHoverOnTopArrow;
	    m_MouseDownOnTopArrow = copy.m_MouseDownOnTopArrow;
	    
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

	public SpinButton assign(SpinButton right) {
        if (this != right) {
            SpinButton temp = new SpinButton(right);
            //this->ClickableWidget::operator=(right);
            super.assign(right);
            
            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_VerticalScroll = temp.m_VerticalScroll;
            m_Minimum = temp.m_Minimum;
            m_Maximum = temp.m_Maximum;
            m_Value = temp.m_Value;
            m_SeparateHoverImage = temp.m_SeparateHoverImage;
            m_MouseHoverOnTopArrow = temp.m_MouseHoverOnTopArrow;
            m_MouseDownOnTopArrow = temp.m_MouseDownOnTopArrow;
            m_TextureArrowUpNormal = temp.m_TextureArrowUpNormal;
            m_TextureArrowUpHover = temp.m_TextureArrowUpHover;
            m_TextureArrowDownNormal = temp.m_TextureArrowDownNormal;
            m_TextureArrowDownHover = temp.m_TextureArrowDownHover;
        }

        return this;
	}

	public SpinButton cloneObj() {
        return new SpinButton(this);
	}

	public boolean load(String configFileFilename) {
        m_LoadedConfigFile = configFileFilename;

        m_Loaded = false;

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
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("SpinButton", properties, values)) {
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
            } else if ("arrowupnormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowUpNormal)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowUpNormalImage in section SpinButton in " + configFileFilename + ".");
                    return false;
                }
            } else if ("arrowuphoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowUpHover)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowUpHoverImage in section SpinButton in " + configFileFilename + ".");
                    return false;
                }
            } else if ("arrowdownnormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowDownNormal)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowDownNormalImage in section SpinButton in " + configFileFilename + ".");
                    return false;
                }
            } else if ("arrowdownhoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowDownHover)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowDownHoverImage in section SpinButton in " + configFileFilename + ".");
                    return false;
                }
            } else {
            	Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section SpinButton in " + configFileFilename + ".");
            }
        }

        if ((m_TextureArrowUpNormal.data != null) && 
        	(m_TextureArrowDownNormal.data != null)) {
            m_Size.x = (float)(m_TextureArrowUpNormal.getSize().x);
            m_Size.y = (float)(m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y);
        } else {
        	Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the spin button. Is the SpinButton section in " + configFileFilename + " complete?");
            return false;
        }

        if ((m_TextureArrowUpHover.data != null) && 
        	(m_TextureArrowDownHover.data != null)) {
            m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();
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
	}

	public void setMinimum(int minimum) {
        m_Minimum = minimum;

        if (m_Minimum > m_Maximum) {
            m_Maximum = m_Minimum;
        }

        if (m_Value < m_Minimum) {
            m_Value = m_Minimum;
        }
	}

	public void setMaximum(int maximum) {
        m_Maximum = maximum;

        if (m_Maximum < m_Minimum) {
            m_Minimum = m_Maximum;
        }

        if (m_Value > m_Maximum) {
            m_Value = m_Maximum;
        }
	}

	public void setValue(int value) {
        m_Value = value;

        if (m_Value < m_Minimum) {
            m_Value = m_Minimum;
        } else if (m_Value > m_Maximum) {
            m_Value = m_Maximum;
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

	public void setVerticalScroll(boolean verticalScroll) {
        m_VerticalScroll = verticalScroll;
	}

	public boolean getVerticalScroll() {
        return m_VerticalScroll;
	}

	public void setTransparency(int transparency) {
        //ClickableWidget::
        super.setTransparency(transparency);

        m_TextureArrowUpNormal.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureArrowUpHover.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureArrowDownNormal.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureArrowDownHover.sprite.setColor(new Color(255, 255, 255, m_Opacity));
	}

	public void leftMousePressed(float x, float y) {
        m_MouseDown = true;

        if (m_VerticalScroll) {
            if (getTransform().transformRect(
            		new FloatRect(0, 0, m_Size.x, m_Size.y / 2.f))
            			.contains(x, y)) {
                m_MouseDownOnTopArrow = true;
            } else {
                m_MouseDownOnTopArrow = false;
            }
        } else {
            if (getTransform().transformRect(
            		new FloatRect(0, 0, m_Size.x / 2.f, m_Size.y))
            			.contains(x, y)) {
                m_MouseDownOnTopArrow = false;
            } else {
                m_MouseDownOnTopArrow = true;
            }
        }
	}

	public void leftMouseReleased(float x, float y) {
	    if (m_MouseDown) {
            m_MouseDown = false;

            if (m_MouseDownOnTopArrow) {
                if (((m_VerticalScroll == true) && 
                	(getTransform().transformRect(
                		new FloatRect(0, 0, m_Size.x, m_Size.y / 2.f))
                			.contains(x, y))) || 
                	((m_VerticalScroll == false) && 
                	(getTransform().transformRect(
                		new FloatRect(0, 0, m_Size.x / 2.f, m_Size.y))
                			.contains(x, y) == false))) {
                    if (m_Value < m_Maximum) {
                        ++m_Value;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                if (((m_VerticalScroll == true) && 
                	(getTransform().transformRect(
                		new FloatRect(0, 0, m_Size.x, m_Size.y / 2.f))
                			.contains(x, y) == false)) || 
                	((m_VerticalScroll == false) && 
                	(getTransform().transformRect(
                		new FloatRect(0, 0, m_Size.x / 2.f, m_Size.y))
                			.contains(x, y)))) {
                    if (m_Value > m_Minimum) {
                        --m_Value;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }

            if (this.getCallbackManager().m_CallbackFunctions.get(SpinButtonCallbacks.ValueChanged.value()).isEmpty() == false) {
            	this.getCallbackManager().m_Callback.trigger = SpinButtonCallbacks.ValueChanged.value();
            	this.getCallbackManager().m_Callback.value = (int)(m_Value);
                addCallback();
            }
        }
	}

	public void mouseMoved(float x, float y) {
	    if (m_VerticalScroll) {
            if (getTransform().transformRect(
            		new FloatRect(0, 0, m_Size.x, m_Size.y / 2.f))
            			.contains(x, y)) {
                m_MouseHoverOnTopArrow = true;
            } else {
                m_MouseHoverOnTopArrow = false;
            }
        } else {
            if (getTransform().transformRect(
            		new FloatRect(0, 0, m_Size.x / 2.f, m_Size.y))
            			.contains(x, y)) {
                m_MouseHoverOnTopArrow = false;
            } else {
                m_MouseHoverOnTopArrow = true;
            }
        }

        if (m_MouseHover == false) {
            mouseEnteredWidget();
        }

        m_MouseHover = true;
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
            if (("true".equals(value)) || 
            	("True".equals(value))) {
                setVerticalScroll(true);
        	} else if (("false".equals(value)) || 
        		("False".equals(value))) {
                setVerticalScroll(false);
        	} else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'VerticalScroll' property.");
            }
        } else if ("callback".equals(property)) {
            //ClickableWidget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("ValueChanged".equals(it)) || 
                	("valuechanged".equals(it))) {
                    this.getCallbackManager().bindCallback(
                    	SpinButtonCallbacks.ValueChanged.value());
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

        if (property == "configfile") {
            value[0] = getLoadedConfigFile();
        } else if (property == "minimum") {
            value[0] = Integer.toString(getMinimum());
    	} else if (property == "maximum") {
            value[0] = Integer.toString(getMaximum());
		} else if (property == "value") {
            value[0] = Integer.toString(getValue());
		} else if (property == "verticalscroll") {
            value[0] = m_VerticalScroll ? "true" : "false";
		} else if (property == "callback") {
            String[] tempValue = new String[1];
            //ClickableWidget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(SpinButtonCallbacks.ValueChanged.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(SpinButtonCallbacks.ValueChanged.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(SpinButtonCallbacks.ValueChanged.value()).get(0) == null)) {
                callbacks.add("ValueChanged");
			}
            
            Defines.encodeList(callbacks, value);

            if (value[0].length() == 0) {
                value[0] = tempValue[0];
			} else if (tempValue[0].length() != 0) {
                value[0] += "," + tempValue;
            }
        } else {
        	//ClickableWidget::
            return super.getProperty(property, value);
        }
        return true;
    }

    public List<Pair<String, String>> getPropertyList() {
    	//ClickableWidget::
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

        Transform.multiplyEqual(states.transform, getTransform());

        if (m_VerticalScroll) {
            states.transform.scale(m_Size.x / m_TextureArrowUpNormal.getSize().x, m_Size.y / (m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y));

            if (m_SeparateHoverImage) {
                if ((m_MouseHover) && (m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0) {
                    if (m_MouseHoverOnTopArrow) {
                        target.draw(m_TextureArrowUpHover.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureArrowUpNormal.getSprite(), states);
                }
            } else {
                target.draw(m_TextureArrowUpNormal.getSprite(), states);

                if ((m_MouseHover) && (m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0) {
                    if (m_MouseHoverOnTopArrow) {
                        target.draw(m_TextureArrowUpHover.getSprite(), states);
                    }
                }
            }

            states.transform.translate(0, (float)(m_TextureArrowUpNormal.getSize().y));

            if (m_SeparateHoverImage) {
                if ((m_MouseHover) && (m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0) {
                    if (m_MouseHoverOnTopArrow) {
                        target.draw(m_TextureArrowDownHover.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureArrowDownNormal.getSprite(), states);
                }
            } else {
                target.draw(m_TextureArrowDownNormal.getSprite(), states);

                if ((m_MouseHover) && (m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0) {
                    if (!m_MouseHoverOnTopArrow) {
                        target.draw(m_TextureArrowDownHover.getSprite(), states);
                    }
                }
            }
        } else {
            states.transform.scale(m_Size.x / (m_TextureArrowUpNormal.getSize().y + m_TextureArrowDownNormal.getSize().y), m_Size.y / m_TextureArrowUpNormal.getSize().x);

            states.transform.rotate(-90, (float)(m_TextureArrowUpNormal.getSize().x), (float)(m_TextureArrowUpNormal.getSize().y));

            states.transform.translate((float)(m_TextureArrowUpNormal.getSize().y), 0);

            if (m_SeparateHoverImage) {
                if ((m_MouseHover) && (m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0) {
                    if (m_MouseHoverOnTopArrow) {
                        target.draw(m_TextureArrowUpHover.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureArrowUpNormal.getSprite(), states);
                }
            } else {
                target.draw(m_TextureArrowUpNormal.getSprite(), states);

                if ((m_MouseHover) && (m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0) {
                    if (!m_MouseHoverOnTopArrow) {
                        target.draw(m_TextureArrowUpHover.getSprite(), states);
                    }
                }
            }

            states.transform.translate(0, (float)(m_TextureArrowUpNormal.getSize().y));

            if (m_SeparateHoverImage) {
                if ((m_MouseHover) && (m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0) {
                    if (m_MouseHoverOnTopArrow) {
                        target.draw(m_TextureArrowDownHover.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureArrowDownNormal.getSprite(), states);
                }
            } else {
                target.draw(m_TextureArrowDownNormal.getSprite(), states);

                if ((m_MouseHover) && (m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0) {
                    if (m_MouseHoverOnTopArrow) {
                        target.draw(m_TextureArrowDownHover.getSprite(), states);
                    }
                }
            }
        }
    }
}
