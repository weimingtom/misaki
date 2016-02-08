package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.Texture;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.FloatRect;
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.IntRect;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Text;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20151013
 * @author Administrator
 *
 */
public class LoadingBar extends ClickableWidget {
	public static enum LoadingBarCallbacks {
        ValueChanged(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 1),
        LoadingBarFull(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 2),
        AllLoadingBarCallbacks(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 4 - 1),
        LoadingBarCallbacksCount(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 4);
        
        int value;
        
        LoadingBarCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }
	
	protected String m_LoadedConfigFile;

	protected int m_Minimum;
	protected int m_Maximum;
	protected int m_Value;

	protected boolean m_SplitImage;

	protected Texture m_TextureBack_L;
	protected Texture m_TextureBack_M;
	protected Texture m_TextureBack_R;
	protected Texture m_TextureFront_L;
	protected Texture m_TextureFront_M;
	protected Texture m_TextureFront_R;
	
    protected Text m_Text;
    protected int m_TextSize;
	
	public LoadingBar() {
	    m_Minimum = 0;
	    m_Maximum = 100;
	    m_Value = 0;
	    m_SplitImage = false;
	    m_TextSize = 0;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_LoadingBar;
	}

	public LoadingBar(LoadingBar copy) {
		//ClickableWidget
	    super(copy);
	    m_LoadedConfigFile = copy.m_LoadedConfigFile;
	    m_Minimum = copy.m_Minimum;
	    m_Maximum = copy.m_Maximum;
	    m_Value = copy.m_Value;
	    m_SplitImage = copy.m_SplitImage;
	    m_Text = copy.m_Text;
	    m_TextSize = copy.m_TextSize;
	    
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureBack_L, m_TextureBack_L);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureBack_M, m_TextureBack_M);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureBack_R, m_TextureBack_R);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureFront_L, m_TextureFront_L);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureFront_M, m_TextureFront_M);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureFront_R, m_TextureFront_R);

        recalculateSize();
	}

	public void destroy() {
		super.destroy();
		
        if (m_TextureBack_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureBack_L);
        }
        if (m_TextureBack_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureBack_M);
        }
        if (m_TextureBack_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureBack_R);
        }

        if (m_TextureFront_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFront_L);
        }
        if (m_TextureFront_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFront_M);
        }
        if (m_TextureFront_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFront_R);
        }
	}

	public LoadingBar assign(LoadingBar right) {
        if (this != right) {
            LoadingBar temp = new LoadingBar(right);
            //this->ClickableWidget::operator=(right);
            super.assign(right);
            
            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_Minimum = temp.m_Minimum;
            m_Maximum = temp.m_Maximum;
            m_Value = temp.m_Value;
            m_SplitImage = temp.m_SplitImage;
            m_TextureBack_L = temp.m_TextureBack_L;
            m_TextureBack_M = temp.m_TextureBack_M;
            m_TextureBack_R = temp.m_TextureBack_R;
            m_TextureFront_L = temp.m_TextureFront_L;
            m_TextureFront_M = temp.m_TextureFront_M;
            m_TextureFront_R = temp.m_TextureFront_R;
            m_Text = temp.m_Text;
            m_TextSize = temp.m_TextSize;
        }

        return this;
	}

	public LoadingBar cloneObj() {
        return new LoadingBar(this);
	}

	public boolean load(String configFileFilename) {
		m_LoadedConfigFile = configFileFilename;

        m_Loaded = false;

        if (m_TextureBack_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureBack_L);
        }
        if (m_TextureBack_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureBack_M);
        }
        if (m_TextureBack_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureBack_R);
        }
        if (m_TextureFront_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFront_L);
        }
        if (m_TextureFront_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFront_M);
        }
        if (m_TextureFront_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFront_R);
        }

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("LoadingBar", properties, values)) {
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
            configFileFolder = configFileFilename.substring(0, slashPos+1);
        }
        
        for (int i = 0; i < properties.size(); ++i) {
            String property = properties.get(i);
            String value = values.get(i);

            if ("backimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureBack_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for BackImage in section LoadingBar in " + configFileFilename + ".");
                    return false;
                }

                m_SplitImage = false;
            } else if ("frontimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFront_M))
                {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FrontImage in section LoadingBar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("backimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureBack_L)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for BackImage_L in section LoadingBar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("backimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureBack_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for BackImage_M in section LoadingBar in " + configFileFilename + ".");
                    return false;
                }

                m_SplitImage = true;
            } else if ("backimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureBack_R)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for BackImage_R in section LoadingBar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("frontimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFront_L)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FrontImage_L in section LoadingBar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("frontimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFront_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FrontImage_M in section LoadingBar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("frontimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFront_R)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FrontImage_R in section LoadingBar in " + configFileFilename + ".");
                    return false;
                }
            } else if ("textcolor".equals(property)) {
                setTextColor(Defines.extractColor(value));
            } else if ("textsize".equals(property)) {
                setTextSize(Integer.parseInt(value.trim()));
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section LoadingBar in " + configFileFilename + ".");
            }
        }

        if (m_SplitImage) {
            if ((m_TextureBack_L.data != null) && 
            	(m_TextureBack_M.data != null) && 
            	(m_TextureBack_R.data != null) && 
            	(m_TextureFront_L.data != null) && 
            	(m_TextureFront_M.data != null) && 
            	(m_TextureFront_R.data != null)) {
                m_Size.x = (float)(m_TextureBack_L.getSize().x + 
                	m_TextureBack_M.getSize().x + 
                	m_TextureBack_R.getSize().x);
                m_Size.y = (float)(m_TextureBack_M.getSize().y);

                m_TextureBack_M.data.texture.setRepeated(true);
                m_TextureFront_M.data.texture.setRepeated(true);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the loading bar. Is the LoadingBar section in " + configFileFilename + " complete?");
                return false;
            }
        } else {
            if ((m_TextureBack_M.data != null) && 
            	(m_TextureFront_M.data != null)) {
                m_Size = new Vector2f(m_TextureBack_M.getSize());
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the loading bar. Is the LoadingBar section in " + configFileFilename + " complete?");
                return false;
            }
        }

        m_Loaded = true;

        recalculateSize();

        return true;
	}

	public String getLoadedConfigFile() {
        return m_LoadedConfigFile;
	}

	public void setSize(float width, float height) {
	    if (m_Loaded == false) {
            return;
        }

        if (width < 0) {
        	width = -width;
        }
        if (height < 0) {
        	height = -height;
        }

        m_Size.x = width;
        m_Size.y = height;

        if (m_SplitImage) {
            float minimumWidth = (m_TextureBack_L.getSize().x + m_TextureBack_R.getSize().x) * (m_Size.y / m_TextureBack_M.getSize().y);
            if (m_Size.x < minimumWidth) {
                m_Size.x = minimumWidth;
            }
        }

        recalculateSize();

        setText(m_Text.getString());
	}

	public void setMinimum(int minimum) {
        m_Minimum = minimum;

        if (m_Minimum > m_Maximum) {
            m_Maximum = m_Minimum;
        }

        if (m_Value < m_Minimum) {
            m_Value = m_Minimum;
        }

        recalculateSize();
	}

	public void setMaximum(int maximum) {
        m_Maximum = maximum;

        if (m_Maximum < m_Minimum) {
            m_Minimum = m_Maximum;
        }

        if (m_Value > m_Maximum) {
            m_Value = m_Maximum;
        }

        recalculateSize();
	}

	public void setValue(int value) {
        m_Value = value;

        if (m_Value < m_Minimum) {
            m_Value = m_Minimum;
        } else if (m_Value > m_Maximum) {
            m_Value = m_Maximum;
        }

        recalculateSize();
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

	public int incrementValue() {
        if (m_Value < m_Maximum) {
            ++m_Value;

            if (this.getCallbackManager().m_CallbackFunctions.get(LoadingBarCallbacks.ValueChanged.value()).isEmpty() == false) {
                this.getCallbackManager().m_Callback.trigger = LoadingBarCallbacks.ValueChanged.value();
                this.getCallbackManager().m_Callback.value = (int)(m_Value);
                addCallback();
            }

            if (m_Value == m_Maximum) {
                if (this.getCallbackManager().m_CallbackFunctions.get(LoadingBarCallbacks.LoadingBarFull.value()).isEmpty() == false) {
                	this.getCallbackManager().m_Callback.trigger = LoadingBarCallbacks.LoadingBarFull.value();
                	this.getCallbackManager().m_Callback.value = (int)(m_Value);
                    addCallback();
                }
            }

            recalculateSize();
        }

        return m_Value;
	}

	public void setText(String text) {
        if (m_Loaded == false) {
            return;
        }

        m_Text.setString(text);

        if (m_TextSize == 0) {
            float size = m_Size.y * 0.85f;
            m_Text.setCharacterSize((int)(size));
            m_Text.setCharacterSize((int)(m_Text.getCharacterSize() - m_Text.getLocalBounds().top));

            if (m_Text.getGlobalBounds().width > (m_Size.x * 0.8f)) {
                m_Text.setCharacterSize((int)(size / (m_Text.getGlobalBounds().width / (m_Size.x * 0.8f))));
                m_Text.setCharacterSize((int)(m_Text.getCharacterSize() - m_Text.getLocalBounds().top));
            }
        } else {
            m_Text.setCharacterSize(m_TextSize);
        }
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

        m_TextureBack_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureBack_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureBack_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureFront_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureFront_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureFront_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
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
        } else if ("text".equals(property)) {
            setText(value);
        } else if ("textcolor".equals(property)) {
            setTextColor(Defines.extractColor(value));
        } else if ("textsize".equals(property)) {
            setTextSize(Integer.parseInt(value.trim()));
        } else if ("callback".equals(property)) {
            //ClickableWidget::
        	super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("ValueChanged".equals(it)) || 
                	("valuechanged".equals(it))) {
                    this.getCallbackManager().bindCallback(LoadingBarCallbacks.ValueChanged.value());
            	} else if (("LoadingBarFull".equals(it)) || 
            		("loadingbarfull".equals(it))) {
            		this.getCallbackManager().bindCallback(LoadingBarCallbacks.LoadingBarFull.value());
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
            value[0] = Integer.toString(getMinimum());
        } else if ("maximum".equals(property)) {
            value[0] = Integer.toString(getMaximum());
        } else if ("value".equals(property)) {
            value[0] = Integer.toString(getValue());
        } else if ("text".equals(property)) {
            value[0] = getText();
        } else if ("textcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getTextColor().r)) + "," + 
            	Integer.toString((int)(getTextColor().g)) + "," + 
            	Integer.toString((int)(getTextColor().b)) + "," + 
            	Integer.toString((int)(getTextColor().a)) + ")";
        } else if ("textsize".equals(property)) {
            value[0] = Integer.toString(getTextSize());
        } else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //ClickableWidget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(LoadingBarCallbacks.ValueChanged.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(LoadingBarCallbacks.ValueChanged.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(LoadingBarCallbacks.ValueChanged.value()).get(0) == null)) {
                callbacks.add("ValueChanged");
			}
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(LoadingBarCallbacks.LoadingBarFull.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(LoadingBarCallbacks.LoadingBarFull.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(LoadingBarCallbacks.LoadingBarFull.value()).get(0) == null)) {
                callbacks.add("LoadingBarFull");
			}

            Defines.encodeList(callbacks, value);

            if (value[0].length() == 0) {
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
        list.add(new Pair<String, String>("Minimum", "uint"));
        list.add(new Pair<String, String>("Maximum", "uint"));
        list.add(new Pair<String, String>("Value", "uint"));
        list.add(new Pair<String, String>("Text", "string"));
        list.add(new Pair<String, String>("TextColor", "color"));
        list.add(new Pair<String, String>("TextSize", "uint"));
        return list;
	}

	protected void initialize(Container parent) {
        m_Parent = parent;
        m_Text.setFont(m_Parent.getGlobalFont());
	}

	protected void recalculateSize() {
        if (m_Loaded == false) {
            return;
        }

        if (m_SplitImage) {
            float totalWidth = m_Size.x / (m_Size.y / m_TextureBack_M.getSize().y);

            IntRect bounds_L = m_TextureFront_L.sprite.getTextureRect();
            IntRect bounds_M = m_TextureFront_M.sprite.getTextureRect();
            IntRect bounds_R = m_TextureFront_R.sprite.getTextureRect();

            float frontSize;
            if ((m_Maximum - m_Minimum) > 0) {
                frontSize = totalWidth * ((m_Value - m_Minimum) / (float)(m_Maximum - m_Minimum));
            } else {
                frontSize = totalWidth;
            }
            
            if (frontSize > 0) {
                if (frontSize > m_TextureBack_L.getSize().x) {
                    if (frontSize > totalWidth - m_TextureBack_R.getSize().x) {
                        bounds_L.width = m_TextureBack_L.getSize().x;
                        bounds_M.width = (int)(totalWidth - m_TextureBack_L.getSize().x - m_TextureBack_R.getSize().x);

                        if (frontSize < totalWidth) {
                            bounds_R.width = (int)(frontSize - (totalWidth - m_TextureBack_R.getSize().x));
                        } else {
                            bounds_R.width = m_TextureBack_R.getSize().x;
                        }
                    } else {
                        bounds_L.width = m_TextureBack_L.getSize().x;
                        bounds_M.width = (int)(frontSize - (m_TextureBack_L.getSize().x));
                        bounds_R.width = 0;
                    }
                } else {
                    bounds_L.width = (int)(frontSize);
                    bounds_M.width = 0;
                    bounds_R.width = 0;
                }
            } else {
                bounds_L.width = 0;
                bounds_M.width = 0;
                bounds_R.width = 0;
            }

            m_TextureFront_L.sprite.setTextureRect(bounds_L);
            m_TextureFront_M.sprite.setTextureRect(bounds_M);
            m_TextureFront_R.sprite.setTextureRect(bounds_R);

            m_TextureBack_M.sprite.setTextureRect(new IntRect(0, 0, (int)(totalWidth - m_TextureBack_L.getSize().x - m_TextureBack_R.getSize().x), m_TextureBack_M.getSize().y));
        } else {
            IntRect frontBounds = new IntRect(m_TextureFront_M.sprite.getTextureRect());

            if ((m_Maximum - m_Minimum) > 0) {
                frontBounds.width = (int)(m_TextureBack_M.getSize().x * 
                	((m_Value - m_Minimum) / (float)(m_Maximum - m_Minimum)));
        	} else {
                frontBounds.width = (int)(m_TextureBack_M.getSize().x);
            }
            
            m_TextureFront_M.sprite.setTextureRect(frontBounds);
        }
	}

	public void draw(RenderTarget target, RenderStates states) {
        if (m_Loaded == false) {
            return;
        }

        Transform.multiplyEqual(states.transform, getTransform());

        Transform oldTransform = states.transform;

        if (m_SplitImage) {
            float scalingY = m_Size.y / m_TextureBack_M.getSize().y;

            states.transform.scale(scalingY, scalingY);

            target.draw(m_TextureBack_L.getSprite(), states);
            target.draw(m_TextureFront_L.getSprite(), states);

            if ((scalingY * (m_TextureBack_L.getSize().x + m_TextureBack_R.getSize().x)) < m_Size.x) {
                states.transform.translate(
                	(float)(m_TextureBack_L.getSize().x), 0);

                target.draw(m_TextureBack_M.getSprite(), states);
                target.draw(m_TextureFront_M.getSprite(), states);

                states.transform.translate(m_TextureBack_M.sprite.getGlobalBounds().width, 0);

                target.draw(m_TextureBack_R.getSprite(), states);
                target.draw(m_TextureFront_R.getSprite(), states);
            } else {
                states.transform.translate(
                	(float)(m_TextureBack_L.getSize().x), 0);

                target.draw(m_TextureBack_R.getSprite(), states);
                target.draw(m_TextureFront_R.getSprite(), states);
            }
        } else {
            states.transform.scale(m_Size.x / m_TextureBack_M.getSize().x, m_Size.y / m_TextureBack_M.getSize().y);

            target.draw(m_TextureBack_M.getSprite(), states);
            target.draw(m_TextureFront_M.getSprite(), states);
        }

        if (m_Text.getString().isEmpty() == false) {
            states.transform = oldTransform;

            FloatRect rect = m_Text.getGlobalBounds();

            rect.left = (m_Size.x - rect.width) * 0.5f - rect.left;
            rect.top = (m_Size.y - rect.height) * 0.5f - rect.top;

            states.transform.translate(
            	(float)Math.floor(rect.left + 0.5f), 
            	(float)Math.floor(rect.top + 0.5f));

            target.draw(m_Text, states);
        }
	}
}
