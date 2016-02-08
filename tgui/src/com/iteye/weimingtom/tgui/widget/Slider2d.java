package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.GL;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.Texture;
import com.iteye.weimingtom.tgui.WidgetPhase;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20151014
 * @author Administrator
 *
 */
public class Slider2d extends ClickableWidget {
    public static enum Slider2dCallbacks {
        ValueChanged(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 1),            
        ThumbReturnedToCenter(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 2),  
        AllSlider2dCallbacks(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 4 - 1), 
        Slider2dCallbacksCount(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 4);
        
        int value;
        
        Slider2dCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }

    protected String m_LoadedConfigFile;

    protected Vector2f m_Minimum;
    protected Vector2f m_Maximum;
    protected Vector2f m_Value;

    protected boolean m_ReturnThumbToCenter;
    protected boolean m_FixedThumbSize;

    protected Texture m_TextureThumbNormal;
    protected Texture m_TextureThumbHover;
    protected Texture m_TextureTrackNormal;
    protected Texture m_TextureTrackHover;

    protected boolean m_SeparateHoverImage;
	
	public Slider2d() {
	    m_Minimum = new Vector2f(-1, -1);
	    m_Maximum = new Vector2f(1, 1);
	    m_Value = new Vector2f(0, 0);
	    m_ReturnThumbToCenter = false;
	    m_FixedThumbSize = true;
	    m_SeparateHoverImage = false;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_Slider2d;
        m_DraggableWidget = true;
	}

	public Slider2d(Slider2d copy) {
	    //ClickableWidget
	    super(copy);
	    m_LoadedConfigFile = copy.m_LoadedConfigFile;
	    m_Minimum = copy.m_Minimum;
	    m_Maximum = copy.m_Maximum;
	    m_Value = copy.m_Value;
	    m_ReturnThumbToCenter = copy.m_ReturnThumbToCenter;
	    m_FixedThumbSize = copy.m_FixedThumbSize;
	    m_SeparateHoverImage = copy.m_SeparateHoverImage;
	    
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureTrackNormal, m_TextureTrackNormal);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureTrackHover, m_TextureTrackHover);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureThumbNormal, m_TextureThumbNormal);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureThumbHover, m_TextureThumbHover);
	}

	public void destroy() {
		super.destroy();
		
        if (m_TextureTrackNormal.data != null) { 
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackNormal);
        }
        if (m_TextureTrackHover.data != null) {  
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackHover);
        }
        if (m_TextureThumbNormal.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureThumbNormal);
        }
        if (m_TextureThumbHover.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureThumbHover);
        }
	}

	public Slider2d assign(Slider2d right) {
        if (this != right) {
            Slider2d temp = new Slider2d(right);
            //this->ClickableWidget::operator=(right);
            super.assign(right);
            
            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_Minimum = temp.m_Minimum;
            m_Maximum = temp.m_Maximum;
            m_Value = temp.m_Value;
            m_ReturnThumbToCenter = temp.m_ReturnThumbToCenter;
            m_FixedThumbSize = temp.m_FixedThumbSize;
            m_TextureTrackNormal = temp.m_TextureTrackNormal;
            m_TextureTrackHover = temp.m_TextureTrackHover;
            m_TextureThumbNormal = temp.m_TextureThumbNormal;
            m_TextureThumbHover = temp.m_TextureThumbHover;
            m_SeparateHoverImage = temp.m_SeparateHoverImage;
        }

        return this;
	}

	public Slider2d cloneObj() {
        return new Slider2d(this);
	}

	public boolean load(String configFileFilename) {
        m_LoadedConfigFile = configFileFilename;

        m_Loaded = false;

        if (m_TextureTrackNormal.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackNormal);
        }
        if (m_TextureTrackHover.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureTrackHover);
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
        if (!configFile.read("Slider2d", properties, values)) {
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
            } else if ("tracknormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackNormal)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackNormalImage in section Slider2d in " + configFileFilename + ".");
                    return false;
                }
            } else if ("trackhoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureTrackHover)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for TrackHoverImage in section Slider2d in " + configFileFilename + ".");
                    return false;
                }
            } else if ("thumbnormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureThumbNormal)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ThumbNormalImage in section Slider2d in " + configFileFilename + ".");
                    return false;
                }
            } else if ("thumbhoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureThumbHover)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ThumbHoverImage in section Slider2d in " + configFileFilename + ".");
                    return false;
                }
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section Slider2d in " + configFileFilename + ".");
            }
        }

        if ((m_TextureTrackNormal.data != null) && 
        	(m_TextureThumbNormal.data != null)) {
            m_Size = new Vector2f(m_TextureTrackNormal.getSize());
        } else {
            Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the slider. Is the Slider2d section in " + configFileFilename + " complete?");
            return false;
        }

        if ((m_TextureTrackHover.data != null) && 
        	(m_TextureThumbHover.data != null)) {
            m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();
        }

        return m_Loaded = true;
	}

	public String getLoadedConfigFile() {
        return m_LoadedConfigFile;
	}

	public void setSize(float width, float height) {
        // Don't do anything when the slider wasn't loaded correctly
        if (m_Loaded == false)
            return;

        // A negative size is not allowed for this widget
        if (width  < 0) width  = -width;
        if (height < 0) height = -height;

        // Store the size of the slider
        m_Size.x = width;
        m_Size.y = height;
	}

	public void setMinimum(Vector2f minimum) {
        // Set the new minimum
        m_Minimum = minimum;

        // The minimum can never be greater than the maximum
        if (m_Minimum.x > m_Maximum.x)
            m_Maximum.x = m_Minimum.x;
        if (m_Minimum.y > m_Maximum.y)
            m_Maximum.y = m_Minimum.y;

        // When the value is below the minimum then adjust it
        if (m_Value.x < m_Minimum.x)
            m_Value.x = m_Minimum.x;
        if (m_Value.y < m_Minimum.y)
            m_Value.y = m_Minimum.y;
	}

	public void setMaximum(Vector2f maximum) {
        // Set the new maximum
        m_Maximum = maximum;

        // The maximum can never be below the minimum
        if (m_Maximum.x < m_Minimum.x)
            m_Minimum.x = m_Maximum.x;
        if (m_Maximum.y < m_Minimum.y)
            m_Minimum.y = m_Maximum.y;

        // When the value is above the maximum then adjust it
        if (m_Value.x > m_Maximum.x)
            m_Value.x = m_Maximum.x;
        if (m_Value.y > m_Maximum.y)
            m_Value.y = m_Maximum.y;
	}

	public void setValue(Vector2f value) {
        m_Value = value;

        if (m_Value.x < m_Minimum.x) {
            m_Value.x = m_Minimum.x;
        } else if (m_Value.x > m_Maximum.x) {
            m_Value.x = m_Maximum.x;
        }

        if (m_Value.y < m_Minimum.y) {
            m_Value.y = m_Minimum.y;
        } else if (m_Value.y > m_Maximum.y) {
            m_Value.y = m_Maximum.y;
        }
	}

	public Vector2f getMinimum() {
        return m_Minimum;
	}

	public Vector2f getMaximum() {
        return m_Maximum;
	}

	public Vector2f getValue() {
        return m_Value;
	}

	public void setFixedThumbSize(boolean fixedSize) {
        m_FixedThumbSize = fixedSize;
	}

	public boolean getFixedThumbSize() {
        return m_FixedThumbSize;
	}

	public void enableThumbCenter(boolean autoCenterThumb) {
        m_ReturnThumbToCenter = autoCenterThumb;
	}

	public void centerThumb() {
        setValue(new Vector2f(
        	(m_Maximum.x + m_Minimum.x) * 0.5f, 
        	(m_Maximum.y + m_Minimum.y) * 0.5f));
	}

	public void setTransparency(int transparency) {
        //ClickableWidget::
        super.setTransparency(transparency);

        m_TextureThumbNormal.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureThumbHover.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureTrackNormal.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureTrackHover.sprite.setColor(new Color(255, 255, 255, m_Opacity));
	}

	public void leftMousePressed(float x, float y) {
        //ClickableWidget::
        super.leftMousePressed(x, y);

        mouseMoved(x, y);
	}

    public void leftMouseReleased(float x, float y) {
        //ClickableWidget::
        super.leftMouseReleased(x, y);

        if (m_ReturnThumbToCenter) {
            setValue(new Vector2f(
            	(m_Maximum.x + m_Minimum.x) * 0.5f, 
            	(m_Maximum.y + m_Minimum.y) * 0.5f));

            if (this.getCallbackManager().m_CallbackFunctions
            		.get(Slider2dCallbacks.ThumbReturnedToCenter.value())
            		.isEmpty() == false) {
            	this.getCallbackManager().m_Callback.trigger = Slider2dCallbacks.ThumbReturnedToCenter.value();
                this.getCallbackManager().m_Callback.value2d = m_Value;
                addCallback();
            }
		}
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

        Vector2f oldValue = m_Value;

        if (m_MouseDown) {
            if ((y - position.y) > 0) {
                m_Value.y = ((y - position.y) / m_Size.y) * (m_Maximum.y - m_Minimum.y) + m_Minimum.y;
            } else {
            	m_Value.y = m_Minimum.y;
            }
            
            if ((x - position.x) > 0) {
                m_Value.x = ((x - position.x) / m_Size.x) * (m_Maximum.x - m_Minimum.x) + m_Minimum.x;
            } else { 
            	m_Value.x = m_Minimum.x;
            }
            
            setValue(m_Value);

            //FIXME:
            if ((oldValue != m_Value) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(Slider2dCallbacks.ValueChanged.value())
            		.isEmpty() == false)) {
            	this.getCallbackManager().m_Callback.trigger = Slider2dCallbacks.ValueChanged.value();
                this.getCallbackManager().m_Callback.value2d = m_Value;
                addCallback();
            }
        }
    }

    public void widgetFocused() {
        unfocus();
    }

    public void mouseNoLongerDown() {
		m_MouseDown = false;

		if (m_ReturnThumbToCenter) {
			//FIXME:
		    if (m_Value != new Vector2f((m_Maximum.x + m_Minimum.x) * 0.5f, (m_Maximum.y + m_Minimum.y) * 0.5f)) {
		        setValue(new Vector2f((m_Maximum.x + m_Minimum.x) * 0.5f, (m_Maximum.y + m_Minimum.y) * 0.5f));

                if (this.getCallbackManager().m_CallbackFunctions
                	.get(Slider2dCallbacks.ThumbReturnedToCenter.value())
                	.isEmpty() == false) {
                	this.getCallbackManager().m_Callback.trigger = Slider2dCallbacks.ThumbReturnedToCenter.value();
                    this.getCallbackManager().m_Callback.value2d = m_Value;
                    addCallback();
                }
		    }
		}
    }

    public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            load(value);
        } else if ("minimum".equals(property)) {
            if (value.length() >= 5) {
                if ((value.charAt(0) == '(') && 
                	(value.charAt(value.length() -1) == ')')) {
                    int commaPos = value.indexOf(',');
                    if ((commaPos != -1) && 
                    	(value.indexOf(',', commaPos + 1) == -1)) {
                        setMinimum(new Vector2f((float)(Float.parseFloat(value.substring(1, commaPos - 1 + 1))),
                                                (float)(Float.parseFloat(value.substring(commaPos + 1, value.length() - commaPos - 2 + commaPos + 1)))));
                    } else {
                    	Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Minimum' property.");
                    }
                } else {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Minimum' property.");
                }
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Minimum' property.");
            }
        } else if ("maximum".equals(property)) {
            if (value.length() >= 5) {
                if ((value.charAt(0) == '(') && 
                	(value.charAt(value.length() - 1) == ')')) {
                    int commaPos = value.indexOf(',');
                    if ((commaPos != -1) && 
                    	(value.indexOf(',', commaPos + 1) == -1)) {
                        setMaximum(new Vector2f((float)(Float.parseFloat(value.substring(1, commaPos-1 + 1))),
                                                (float)(Float.parseFloat(value.substring(commaPos + 1, value.length()-commaPos-2 + commaPos+1)))));
                    } else {
                        Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Maximum' property.");
                    }
                } else {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Maximum' property.");
                }
            } else {
            	Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Maximum' property.");
            }
        } else if ("value".equals(property)) {
            if (value.length() >= 5) {
                if ((value.charAt(0) == '(') && 
                	(value.charAt(value.length() - 1) == ')')) {
                    int commaPos = value.indexOf(',');
                    if ((commaPos != -1) && 
                    	(value.indexOf(',', commaPos + 1) == -1)) {
                        setValue(new Vector2f(
                        	(float)(Float.parseFloat(value.substring(1, commaPos - 1 + 1))),
                            (float)(Float.parseFloat(value.substring(commaPos+1, value.length()-commaPos-2 + commaPos+1)))));
                    } else {
                    	Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Value' property.");
                    }
                } else {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Value' property.");
                }
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Value' property.");
            }
        } else if ("fixedthumbsize".equals(property)) {
            if (("true".equals(value)) || 
            	("True".equals(value))) {
                setFixedThumbSize(true);
            } else if (("false".equals(value)) || 
            	("False".equals(value))) {
                setFixedThumbSize(false);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'FixedThumbSize' property.");
            }
        } else if ("enablethumbcenter".equals(property)) {
            if (("true".equals(value)) || 
            	("True".equals(value))) {
                enableThumbCenter(true);
            } else if (("false".equals(value)) || 
            	("False".equals(value))) {
                enableThumbCenter(false);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'EnableThumbCenter' property.");
            }
        } else if ("callback".equals(property)) {
            //ClickableWidget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("ValueChanged".equals(it)) || 
                	("valuechanged".equals(it))) {
                	this.getCallbackManager().bindCallback(Slider2dCallbacks.ValueChanged.value());
                } else if (("ThumbReturnedToCenter".equals(it)) || 
                	("thumbreturnedtocenter".equals(it))) {
                    this.getCallbackManager().bindCallback(Slider2dCallbacks.ThumbReturnedToCenter.value());
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
        } else if ("minimum".equals(property)) {
            value[0] = "(" + 
            	Float.toString(getMinimum().x) + "," + 
            	Float.toString(getMinimum().y) + ")";
        } else if ("maximum".equals(property)) {
            value[0] = "(" + 
            	Float.toString(getMaximum().x) + "," + 
            	Float.toString(getMaximum().y) + ")";
        } else if ("value".equals(property)) {
            value[0] = "(" + 
            	Float.toString(getValue().x) + "," + 
            	Float.toString(getValue().y) + ")";
        } else if ("fixedthumbsize".equals(property)) {
            value[0] = m_FixedThumbSize ? "true" : "false";
        } else if ("enablethumbcenter".equals(property)) {
            value[0] = m_ReturnThumbToCenter ? "true" : "false";
        } else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //ClickableWidget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(Slider2dCallbacks.ValueChanged.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(Slider2dCallbacks.ValueChanged.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(Slider2dCallbacks.ValueChanged.value()).get(0) == null)) {
                callbacks.add("ValueChanged");
			}
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(Slider2dCallbacks.ThumbReturnedToCenter.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(Slider2dCallbacks.ThumbReturnedToCenter.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(Slider2dCallbacks.ThumbReturnedToCenter.value()).get(0) == null)) {
                callbacks.add("ThumbReturnedToCenter");
			}

            Defines.encodeList(callbacks, value);

            if (value[0].length() == -1) {
                value[0] = tempValue[0];
        	} else if (tempValue[0].length() != 0) {
                value[0] += "," + tempValue[0];
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
        list.add(new Pair<String, String>("Minimum", "custom"));
        list.add(new Pair<String, String>("Maximum", "custom"));
        list.add(new Pair<String, String>("Value", "custom"));
        list.add(new Pair<String, String>("FixedThumbSize", "bool"));
        list.add(new Pair<String, String>("EnableThumbCenter", "bool"));
        return list;
    }

    public void draw(RenderTarget target, RenderStates states) {
        if (m_Loaded == false) {
            return;
        }

        float scaleViewX = target.getSize().x / target.getView().getSize().x;
        float scaleViewY = target.getSize().y / target.getView().getSize().y;

        Vector2f topLeftPosition = states.transform.transformPoint(
        	Vector2f.plus(Vector2f.minus(getPosition(), target.getView().getCenter()), 
        	Vector2f.devide(target.getView().getSize(), 2.f)));
        Vector2f bottomRightPosition = states.transform.transformPoint(
        	Vector2f.plus(Vector2f.minus(Vector2f.plus(getPosition(), new Vector2f(m_Size)),
        	target.getView().getCenter()), 
        	Vector2f.devide(target.getView().getSize(), 2.f)));

        Transform.multiplyEqual(states.transform, getTransform());

        Vector2f scaling = new Vector2f();
        scaling.x = m_Size.x / m_TextureTrackNormal.getSize().x;
        scaling.y = m_Size.y / m_TextureTrackNormal.getSize().y;

        states.transform.scale(scaling);

        if (m_SeparateHoverImage) {
            if ((m_MouseHover) && 
            	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                target.draw(m_TextureTrackHover.getSprite(), states);
        	} else {
                target.draw(m_TextureTrackNormal.getSprite(), states);
        	}
        } else {
            target.draw(m_TextureTrackNormal.getSprite(), states);

            if ((m_MouseHover) && 
            	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                target.draw(m_TextureTrackHover.getSprite(), states);
            }
        }

        states.transform.scale(1.f / scaling.x, 1.f / scaling.y);

        if (m_FixedThumbSize) {
            states.transform.translate((((m_Value.x - m_Minimum.x) / (m_Maximum.x - m_Minimum.x)) * m_TextureTrackNormal.getSize().x * scaling.x) - (m_TextureThumbNormal.getSize().x * 0.5f),
                                       (((m_Value.y - m_Minimum.y) / (m_Maximum.y - m_Minimum.y)) * m_TextureTrackNormal.getSize().y * scaling.y) - (m_TextureThumbNormal.getSize().y * 0.5f));
        } else {
            states.transform.translate((((m_Value.x - m_Minimum.x) / (m_Maximum.x - m_Minimum.x)) * m_TextureTrackNormal.getSize().x * scaling.x) - (m_TextureThumbNormal.getSize().x * 0.5f * scaling.y),
                                       (((m_Value.y - m_Minimum.y) / (m_Maximum.y - m_Minimum.y)) * m_TextureTrackNormal.getSize().y * scaling.y) - (m_TextureThumbNormal.getSize().y * 0.5f * scaling.x));
            states.transform.scale(scaling);
        }

        // Get the old clipping area
        int[] scissor = new int[4];
        GL.glGetIntegerv(GL.GL_SCISSOR_BOX, scissor);

        int scissorLeft = (int)Defines.TGUI_MAXIMUM((int)(topLeftPosition.x * scaleViewX), scissor[0]);
        int scissorTop = (int)Defines.TGUI_MAXIMUM((int)(topLeftPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1] - scissor[3]);
        int scissorRight = (int)Defines.TGUI_MINIMUM((int)(bottomRightPosition.x * scaleViewX), scissor[0] + scissor[2]);
        int scissorBottom = (int)Defines.TGUI_MINIMUM((int)(bottomRightPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1]);

        if (scissorRight < scissorLeft) {
            scissorRight = scissorLeft;
    	} else if (scissorBottom < scissorTop) {
            scissorTop = scissorBottom;
        }

        GL.glScissor(scissorLeft, target.getSize().y - scissorBottom, scissorRight - scissorLeft, scissorBottom - scissorTop);

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
            	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0))
                target.draw(m_TextureThumbHover.getSprite(), states);
        }

        GL.glScissor(scissor[0], scissor[1], scissor[2], scissor[3]);
    }
}
