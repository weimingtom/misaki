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
import com.iteye.weimingtom.tgui.sf.FloatRect;
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Text;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20151018
 * @author Administrator
 *
 */
public class Tab extends Widget {
	public static enum TabCallbacks {
        TabChanged(WidgetCallbacks.WidgetCallbacksCount.value() * 1),
        AllTabCallbacks(WidgetCallbacks.WidgetCallbacksCount.value() * 2 - 1),
        TabCallbacksCount(WidgetCallbacks.WidgetCallbacksCount.value() * 2);
        
        int value;
        
        TabCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }

    protected String m_LoadedConfigFile;

    protected boolean m_SplitImage;
    protected boolean m_SeparateSelectedImage;

    protected int m_TabHeight;
    protected int m_TextSize;

    protected Color m_TextColor;
    protected Color m_SelectedTextColor;

    protected int m_MaximumTabWidth;

    protected int m_DistanceToSide;

    protected int  m_SelectedTab;

    protected List<String> m_TabNames;
    protected List<Float> m_NameWidth;

    protected Texture m_TextureNormal_L;
    protected Texture m_TextureNormal_M;
    protected Texture m_TextureNormal_R;
    protected Texture m_TextureSelected_L;
    protected Texture m_TextureSelected_M;
    protected Texture m_TextureSelected_R;
    
    protected Text m_Text;
	
	public Tab() {
	    m_SplitImage = false;
	    m_SeparateSelectedImage = true;
	    m_TabHeight = 0;
	    m_TextSize = 0;
	    m_MaximumTabWidth = 0;
	    m_DistanceToSide = 5;
	    m_SelectedTab = 0;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_Tab;
	}

	public Tab(Tab copy) {
	    //Widget
	    super(copy);
	    m_LoadedConfigFile = copy.m_LoadedConfigFile;
	    m_SplitImage = copy.m_SplitImage;
	    m_SeparateSelectedImage = copy.m_SeparateSelectedImage;
	    m_TabHeight = copy.m_TabHeight;
	    m_TextSize = copy.m_TextSize;
	    m_TextColor = copy.m_TextColor;
	    m_SelectedTextColor = copy.m_SelectedTextColor;
	    m_MaximumTabWidth = copy.m_MaximumTabWidth;
	    m_DistanceToSide = copy.m_DistanceToSide;
	    m_SelectedTab = copy.m_SelectedTab;
	    m_TabNames = copy.m_TabNames;
	    m_NameWidth = copy.m_NameWidth;
	    m_Text = copy.m_Text;
	    
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureNormal_L, m_TextureNormal_L);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureNormal_M, m_TextureNormal_M);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureNormal_R, m_TextureNormal_R);

        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureSelected_L, m_TextureSelected_L);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureSelected_M, m_TextureSelected_M);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureSelected_R, m_TextureSelected_R);
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

        if (m_TextureSelected_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureSelected_L);
        }
        if (m_TextureSelected_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureSelected_M);
        }
        if (m_TextureSelected_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureSelected_R);
        }
	}

	public Tab assign(Tab right) {
        if (this != right) {
            Tab temp = new Tab(right);
            //this->Widget::operator=(right);
            super.assign(right);
            
            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_SplitImage = temp.m_SplitImage;
            m_SeparateSelectedImage = temp.m_SeparateSelectedImage;
            m_TabHeight = temp.m_TabHeight;
            m_TextSize = temp.m_TextSize;
            m_TextColor = temp.m_TextColor;
            m_SelectedTextColor = temp.m_SelectedTextColor;
            m_MaximumTabWidth = temp.m_MaximumTabWidth;
            m_DistanceToSide = temp.m_DistanceToSide;
            m_SelectedTab = temp.m_SelectedTab;
            m_TabNames = temp.m_TabNames;
            m_NameWidth = temp.m_NameWidth;
            m_TextureNormal_L = temp.m_TextureNormal_L;
            m_TextureNormal_M = temp.m_TextureNormal_M;
            m_TextureNormal_R = temp.m_TextureNormal_R;
            m_TextureSelected_L = temp.m_TextureSelected_L;
            m_TextureSelected_M = temp.m_TextureSelected_M;
            m_TextureSelected_R = temp.m_TextureSelected_R;
            m_Text = temp.m_Text;
        }

        return this;
	}

	public Tab cloneObj() {
        return new Tab(this);
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
        if (m_TextureSelected_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureSelected_L);
        }
        if (m_TextureSelected_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureSelected_M);
        }
        if (m_TextureSelected_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureSelected_R);
        }

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("Tab", properties, values)) {
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

            if ("separateselectedimage".equals(property)) {
                m_SeparateSelectedImage = configFile.readBool(value, false);
            } else if ("textcolor".equals(property)) {
                m_TextColor = configFile.readColor(value);
            } else if ("selectedtextcolor".equals(property)) {
                m_SelectedTextColor = configFile.readColor(value);
            } else if ("distancetoside".equals(property)) {
                setDistanceToSide((int)(Integer.parseInt(value.trim())));
            } else if ("normalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage in section Tab in " + configFileFilename + ".");
                    return false;
                }

                m_SplitImage = false;
            } else if ("selectedimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureSelected_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for SelectedImage in section Tab in " + configFileFilename + ".");
                    return false;
                }
            } else if ("normalimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_L)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage_L in section Tab in " + configFileFilename + ".");
                    return false;
                }
            } else if ("normalimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage_M in section Tab in " + configFileFilename + ".");
                    return false;
                }

                m_SplitImage = true;
            } else if ("normalimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_R)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage_R in section Tab in " + configFileFilename + ".");
                    return false;
                }
            } else if ("selectedimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureSelected_L)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for SelectedImage_L in section Tab in " + configFileFilename + ".");
                    return false;
                }
            } else if ("selectedimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureSelected_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for SelectedImage_M in section Tab in " + configFileFilename + ".");
                    return false;
                }
            } else if ("selectedimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureSelected_R)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for SelectedImage_R in section Tab in " + configFileFilename + ".");
                    return false;
                }
            } else {
            	Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section Tab in " + configFileFilename + ".");
            }
        }

        m_TabNames.clear();
        m_NameWidth.clear();

        if (m_SplitImage) {
            if ((m_TextureNormal_L.data != null) && 
            	(m_TextureNormal_M.data != null) && 
            	(m_TextureNormal_R.data != null)) {
                m_TabHeight = m_TextureNormal_M.getSize().y;

                m_TextureNormal_M.data.texture.setRepeated(true);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the tab. Is the Tab section in " + configFileFilename + " complete?");
                return false;
            }

            if ((m_TextureSelected_L.data != null) && 
            	(m_TextureSelected_M.data != null) && 
            	(m_TextureSelected_R.data != null)) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Selected.value();

                m_TextureSelected_M.data.texture.setRepeated(true);
            }
        } else {
            if (m_TextureNormal_M.data != null) {
                m_TabHeight = m_TextureNormal_M.getSize().y;
            } else {
                Defines.TGUI_OUTPUT("TGUI error: NormalImage wasn't loaded. Is the Tab section in " + configFileFilename + " complete?");
                return false;
            }

            if (m_TextureSelected_M.data != null) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Selected.value();
            }
        }

        if (m_TextSize == 0) {
            setTextSize(0);
        }

        return m_Loaded = true;
	}

	public String getLoadedConfigFile() {
        return m_LoadedConfigFile;
	}

	public void setSize(float width, float height) {
		//do nothing
	}

	public Vector2f getSize() {
        if (m_Loaded == false) {
            return new Vector2f(0, 0);
        }
        
        float width = 0;
        for (int i = 0; i < m_NameWidth.size(); ++i) {
            if (m_SplitImage) {
                width += Defines.TGUI_MAXIMUM(m_MaximumTabWidth != 0 ? 
                	Defines.TGUI_MINIMUM(m_NameWidth.get(i) + (2 * m_DistanceToSide), m_MaximumTabWidth) : 
                		m_NameWidth.get(i) + (2 * m_DistanceToSide), 
                	(m_TextureNormal_L.getSize().x + m_TextureNormal_R.getSize().x) * (m_TabHeight / (float)(m_TextureNormal_M.getSize().y)));
            } else {
                width += m_MaximumTabWidth != 0 ? 
                	Defines.TGUI_MINIMUM(m_NameWidth.get(i) + (2 * m_DistanceToSide), 
                		m_MaximumTabWidth) : 
                	m_NameWidth.get(i) + (2 * m_DistanceToSide);
            }
        }

        return new Vector2f(width, (float)(m_TabHeight));
	}

	public int add(String name) {
		return add(name, true);
	}
	
	public int add(String name, boolean selectTab) {
        m_TabNames.add(name);

        m_Text.setString(name);
        m_NameWidth.add(m_Text.getLocalBounds().width);

        if (selectTab) {
            m_SelectedTab = m_TabNames.size()-1;
        }
        
        return m_TabNames.size()-1;
	}

	public void select(String name) {
        for (int i = 0; i < m_TabNames.size(); ++i) {
            if (m_TabNames.get(i).equals(name)) {
                m_SelectedTab = i;
                return;
            }
        }

        Defines.TGUI_OUTPUT("TGUI warning: Failed to select the tab. The name didn't match any tab.");
	}
	
	public void select(int index) {
        if (index > m_TabNames.size() - 1) {
            Defines.TGUI_OUTPUT("TGUI warning: Failed to select the tab. The index was too high.");
            return;
        }

        m_SelectedTab = index;
	}

	public void deselect() {
        m_SelectedTab = -1;
	}

	public void remove(String name) {
        for (int i = 0; i < m_TabNames.size(); ++i) {
            if (m_TabNames.get(i).equals(name)) {
                m_TabNames.remove(i);
                m_NameWidth.remove(i);

                if (m_SelectedTab == (int)(i)) {
                    m_SelectedTab = -1;
                } else if (m_SelectedTab > (int)(i)) {
                    --m_SelectedTab;
                }

                return;
            }
        }

        Defines.TGUI_OUTPUT("TGUI warning: Failed to remove the tab. The name didn't match any tab.");
	}

	public void remove(int index) {
        if (index > m_TabNames.size() - 1) {
            Defines.TGUI_OUTPUT("TGUI warning: Failed to remove the tab. The index was too high.");
            return;
        }

        m_TabNames.remove(index);
        m_NameWidth.remove(index);

        if (m_SelectedTab == (int)(index)) {
            m_SelectedTab = -1;
        } else if (m_SelectedTab > (int)(index)) {
            --m_SelectedTab;
        }
	}

	public void removeAll() {
        m_TabNames.clear();
        m_NameWidth.clear();
        m_SelectedTab = -1;
	}

	public String getSelected() {
        if (m_SelectedTab == -1) {
            return "";
        } else {
            return m_TabNames.get(m_SelectedTab);
        }
	}

	public int getSelectedIndex() {
        return m_SelectedTab;
	}

	public void setTextFont(Font font) {
        m_Text.setFont(font);
	}

	public Font getTextFont() {
        return m_Text.getFont();
	}

	public void setTextColor(Color color) {
        m_TextColor = color;
    }

	public Color getTextColor() {
        return m_TextColor;
	}

	public void setSelectedTextColor(Color color) {
        m_SelectedTextColor = color;
	}

	public Color getSelectedTextColor() {
        return m_SelectedTextColor;
	}

	public void setTextSize(int size) {
	    m_TextSize = size;

        if (m_TextSize == 0) {
            m_Text.setString("kg");
            m_Text.setCharacterSize((int)(m_TabHeight * 0.85f));
            m_Text.setCharacterSize((int)(m_Text.getCharacterSize() - m_Text.getLocalBounds().top));
        } else {
            m_Text.setCharacterSize(m_TextSize);
        }

        for (int i = 0; i < m_NameWidth.size(); ++i) {
            m_Text.setString(m_TabNames.get(i));
            m_NameWidth.set(i, m_Text.getLocalBounds().width);
        }
	}

	public int getTextSize() {
        return m_Text.getCharacterSize();
    }

	public void setTabHeight(int height) {
        // Make sure that the height changed
        if (m_TabHeight != height)
        {
            m_TabHeight = height;

            // Recalculate the size when auto sizing
            if (m_TextSize == 0)
                setTextSize(0);
        }
	}

	public int getTabHeight() {
        return m_TabHeight;
	}

	public void setMaximumTabWidth(int maximumWidth) {
        m_MaximumTabWidth = maximumWidth;
	}

	public int getMaximumTabWidth() {
        return m_MaximumTabWidth;
	}

	public void setDistanceToSide(int distanceToSide) {
        m_DistanceToSide = distanceToSide;
	}

	public int getDistanceToSide() {
        return m_DistanceToSide;
	}

	public void setTransparency(int transparency) {
        //Widget::
        super.setTransparency(transparency);

        m_TextureNormal_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureNormal_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureNormal_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureSelected_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureSelected_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureSelected_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
	}

	public void leftMousePressed(float x, float y) {
        float width = getPosition().x;

        for (int i = 0; i < m_NameWidth.size(); ++i) {
            if (m_SplitImage) {
                width += Defines.TGUI_MAXIMUM(
                	m_MaximumTabWidth != 0 ? 
                	Defines.TGUI_MINIMUM(m_NameWidth.get(i) + 
                		(2 * m_DistanceToSide), 
                	m_MaximumTabWidth) : m_NameWidth.get(i) + 
                		(2 * m_DistanceToSide),
                    (m_TextureNormal_L.getSize().x + m_TextureNormal_R.getSize().x) * (m_TabHeight / (float)(m_TextureNormal_M.getSize().y)));
            } else {
                width += m_MaximumTabWidth != 0 ? 
                	Defines.TGUI_MINIMUM(m_NameWidth.get(i) + (2 * m_DistanceToSide), 
                			m_MaximumTabWidth) : 
                	m_NameWidth.get(i) + (2 * m_DistanceToSide);
            }
            if (x < width) {
                m_SelectedTab = i;

                if (this.getCallbackManager().m_CallbackFunctions
                		.get(TabCallbacks.TabChanged.value())
                		.isEmpty() == false) {
                    this.getCallbackManager().m_Callback.trigger = TabCallbacks.TabChanged.value();
                    this.getCallbackManager().m_Callback.value = m_SelectedTab;
                    this.getCallbackManager().m_Callback.text = m_TabNames.get(i);
                    this.getCallbackManager().m_Callback.mouse.x = (int)(x - getPosition().x);
                    this.getCallbackManager().m_Callback.mouse.y = (int)(y - getPosition().y);
                    addCallback();
                }

                break;
            }
        }
	}

	public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            load(value);
        } else if ("textcolor".equals(property)) {
            setTextColor(Defines.extractColor(value));
        } else if ("textsize".equals(property)) {
            setTextSize(Integer.parseInt(value.trim()));
        } else if ("tabheight".equals(property)) {
            setTabHeight(Integer.parseInt(value.trim()));
        } else if ("maximumtabwidth".equals(property)) {
            setMaximumTabWidth(Integer.parseInt(value.trim()));
        } else if ("distancetoside".equals(property)) {
            setDistanceToSide(Integer.parseInt(value.trim()));
        } else if ("tabs".equals(property)) {
            removeAll();

            List<String> tabs = new ArrayList<String>();
            Defines.decodeList(value, tabs);

            for (String it : tabs) {
                add(it);
            }
        } else if ("selectedtab".equals(property)) {
            select(Integer.parseInt(value.trim()));
        } else if ("callback".equals(property)) {
            //Widget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("TabChanged".equals(it)) || 
                	("tabchanged".equals(it))) {
                    this.getCallbackManager().bindCallback(TabCallbacks.TabChanged.value());
                }
            }
        } else {
        	//Widget::
            return super.setProperty(property, value);
        }
        
        // You pass here when one of the properties matched
        return true;
	}

	public boolean getProperty(String property, String[] value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            value[0] = getLoadedConfigFile();
        } else if ("textcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getTextColor().r)) + "," + 
            	Integer.toString((int)(getTextColor().g)) + "," + 
            	Integer.toString((int)(getTextColor().b)) + "," + 
            	Integer.toString((int)(getTextColor().a)) + ")";
        } else if ("textsize".equals(property)) {
            value[0] = Integer.toString(getTextSize());
        } else if ("tabheight".equals(property)) {
            value[0] = Integer.toString(getTabHeight());
        } else if ("maximumtabwidth".equals(property)) {
            value[0] = Integer.toString(getMaximumTabWidth());
        } else if ("distancetoside".equals(property)) {
            value[0] = Integer.toString(getDistanceToSide());
        } else if ("tabs".equals(property)) {
            Defines.encodeList(m_TabNames, value);
        } else if ("selectedtab".equals(property)) {
            value[0] = Integer.toString(getSelectedIndex());
        } else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //Widget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(TabCallbacks.TabChanged.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(TabCallbacks.TabChanged.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(TabCallbacks.TabChanged.value()).get(0) == null)) {
                callbacks.add("TabChanged");
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
        list.add(new Pair<String, String>("TextColor", "color"));
        list.add(new Pair<String, String>("TextSize", "uint"));
        list.add(new Pair<String, String>("TabHeight", "uint"));
        list.add(new Pair<String, String>("MaximumTabWidth", "uint"));
        list.add(new Pair<String, String>("DistanceToSide", "uint"));
        list.add(new Pair<String, String>("Tabs", "string"));
        list.add(new Pair<String, String>("SelectedTab", "int"));
        return list;
	}

	protected void initialize(Container parent) {
        m_Parent = parent;
        m_Text.setFont(m_Parent.getGlobalFont());
	}

	public void draw(RenderTarget target, RenderStates states) {
        if (m_Loaded == false) {
            return;
        }

        int[] scissor = new int[4];
        GL.glGetIntegerv(GL.GL_SCISSOR_BOX, scissor);

        float scaleViewX = target.getSize().x / target.getView().getSize().x;
        float scaleViewY = target.getSize().y / target.getView().getSize().y;

        Transform.multiplyEqual(states.transform, getTransform());

        float scalingY = m_TabHeight / (float)(m_TextureNormal_M.getSize().y);
        boolean clippingRequired = false;
        int tabWidth;
        FloatRect realRect = new FloatRect();
        FloatRect defaultRect = new FloatRect();
        Text tempText = new Text(m_Text);

        // Calculate the height and top of all strings
        tempText.setString("kg");
        defaultRect = tempText.getLocalBounds();

        for (int i = 0; i < m_TabNames.size(); ++i) {
            if (m_MaximumTabWidth != 0) {
                if (m_MaximumTabWidth < m_NameWidth.get(i) + (2 * m_DistanceToSide)) {
                    tabWidth = m_MaximumTabWidth;
                    clippingRequired = true;
                } else {
                    tabWidth = (int)(m_NameWidth.get(i) + (2 * m_DistanceToSide));
                }
            } else {
                tabWidth = (int)(m_NameWidth.get(i) + (2 * m_DistanceToSide));
            }
            
            if (tabWidth < 2 * m_DistanceToSide) {
                tabWidth = 2 * m_DistanceToSide;
            }

            if (m_SplitImage) {
                float minimumWidth = (m_TextureNormal_L.getSize().x + m_TextureNormal_R.getSize().x) * scalingY;
                if (tabWidth < minimumWidth) {
                    tabWidth = (int)(minimumWidth);
                }
                
                states.transform.scale(scalingY, scalingY);

                if (m_SeparateSelectedImage) {
                    if ((m_SelectedTab == (int)(i)) && 
                    	((m_WidgetPhase & WidgetPhase.WidgetPhase_Selected.value()) != 0)) {
                        target.draw(m_TextureSelected_L.getSprite(), states);
                	} else {
                        target.draw(m_TextureNormal_L.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureNormal_L.getSprite(), states);

                    if ((m_SelectedTab == (int)(i)) && 
                    	((m_WidgetPhase & WidgetPhase.WidgetPhase_Selected.value()) != 0)) {
                        target.draw(m_TextureSelected_L.getSprite(), states);
                    }
                }

                if ((scalingY * (m_TextureNormal_L.getSize().x + m_TextureNormal_R.getSize().x)) < tabWidth) {
                    float scaleX = (tabWidth / (float)(m_TextureNormal_M.getSize().x)) - (((m_TextureNormal_L.getSize().x + m_TextureNormal_R.getSize().x) * scalingY) / m_TextureNormal_M.getSize().x);

                    states.transform.translate((float)(m_TextureNormal_L.getSize().x), 0);

                    states.transform.scale(scaleX / scalingY, 1);

                    if (m_SeparateSelectedImage) {
                        if ((m_SelectedTab == (int)(i)) && 
                        	((m_WidgetPhase & WidgetPhase.WidgetPhase_Selected.value()) != 0)) {
                            target.draw(m_TextureSelected_M.getSprite(), states);
                    	} else {
                            target.draw(m_TextureNormal_M.getSprite(), states);
                    	}
                    } else {
                        target.draw(m_TextureNormal_M.getSprite(), states);

                        if ((m_SelectedTab == (int)(i)) && 
                        	((m_WidgetPhase & WidgetPhase.WidgetPhase_Selected.value()) != 0)) {
                            target.draw(m_TextureSelected_M.getSprite(), states);
                        }
                    }

                    states.transform.translate((float)(m_TextureNormal_M.getSize().x), 0);

                    states.transform.scale(scalingY / scaleX, 1);

                    if (m_SeparateSelectedImage) {
                        if ((m_SelectedTab == (int)(i)) && 
                        	((m_WidgetPhase & WidgetPhase.WidgetPhase_Selected.value()) != 0)) {
                            target.draw(m_TextureSelected_R.getSprite(), states);
                        } else {
                            target.draw(m_TextureNormal_R.getSprite(), states);
                        }
                    } else {
                        target.draw(m_TextureNormal_R.getSprite(), states);

                        if ((m_SelectedTab == (int)(i)) && 
                        	((m_WidgetPhase & WidgetPhase.WidgetPhase_Selected.value()) != 0)) {
                            target.draw(m_TextureSelected_R.getSprite(), states);
                        }
                    }

                    states.transform.translate(-(m_TextureNormal_L.getSize().x + (m_TextureNormal_M.getSize().x * scaleX / scalingY)), 0);
                } else {
                    states.transform.translate((float)(m_TextureNormal_L.getSize().x), 0);

                    if (m_SeparateSelectedImage) {
                        if ((m_SelectedTab == (int)(i)) && 
                        	((m_WidgetPhase & WidgetPhase.WidgetPhase_Selected.value()) != 0)) {
                            target.draw(m_TextureSelected_R.getSprite(), states);
                    	} else {
                            target.draw(m_TextureNormal_R.getSprite(), states);
                        }
                    } else {
                        target.draw(m_TextureNormal_R.getSprite(), states);

                        if ((m_SelectedTab == (int)(i)) && 
                        	((m_WidgetPhase & WidgetPhase.WidgetPhase_Selected.value()) != 0)) {
                            target.draw(m_TextureSelected_R.getSprite(), states);
                        }
                    }

                    states.transform.translate(-(float)(m_TextureNormal_L.getSize().x), 0);
                }

                states.transform.scale(1.f/scalingY, 1.f/scalingY);
            } else {
                states.transform.scale(tabWidth / (float)(m_TextureNormal_M.getSize().x), scalingY);

                if (m_SeparateSelectedImage) {
                    if ((m_SelectedTab == (int)(i)) && 
                    	((m_WidgetPhase & WidgetPhase.WidgetPhase_Selected.value()) != 0)) {
                        target.draw(m_TextureSelected_M.getSprite(), states);
                    } else {
                        target.draw(m_TextureNormal_M.getSprite(), states);
                    }
                } else {
                    target.draw(m_TextureNormal_M.getSprite(), states);

                    if ((m_SelectedTab == (int)(i)) && 
                    	((m_WidgetPhase & WidgetPhase.WidgetPhase_Selected.value()) != 0)) {
                        target.draw(m_TextureSelected_M.getSprite(), states);
                    }
                }

                states.transform.scale((float)(m_TextureNormal_M.getSize().x) / tabWidth, 1.f / scalingY);
            }

            {
                if (m_SelectedTab == (int)(i)) {
                    tempText.setColor(m_SelectedTextColor);
                } else {
                    tempText.setColor(m_TextColor);
                }
                
                tempText.setString(m_TabNames.get(i));
                realRect = tempText.getLocalBounds();

                if ((m_SplitImage) && 
                	(tabWidth == (m_TextureNormal_L.getSize().x + m_TextureNormal_R.getSize().x) * scalingY)) {
                    realRect.left = ((tabWidth - realRect.width) / 2.f) - realRect.left;
                } else {
                    realRect.left = m_DistanceToSide - realRect.left;
            	}
                realRect.top = ((m_TabHeight - defaultRect.height) / 2.f) - defaultRect.top;

                states.transform.translate(
                	(float)Math.floor(realRect.left + 0.5f), 
                	(float)Math.floor(realRect.top + 0.5f));

                if (clippingRequired) {
                    Vector2f topLeftPosition = states.transform.transformPoint(
                    		Vector2f.minus(Vector2f.devide(target.getView().getSize(), 2.f), target.getView().getCenter()));
                    Vector2f bottomRightPosition = states.transform.transformPoint(
                    		Vector2f.plus(Vector2f.minus(new Vector2f(
                    		tabWidth - (2.0f * m_DistanceToSide), 
                    		(m_TabHeight + defaultRect.height) / 2.f), target.getView().getCenter()), 
                    		Vector2f.devide(target.getView().getSize(), 2.f)));

                    int scissorLeft = (int)Defines.TGUI_MAXIMUM((int)(topLeftPosition.x * scaleViewX), scissor[0]);
                    int scissorTop = (int)Defines.TGUI_MAXIMUM((int)(topLeftPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1] - scissor[3]);
                    int scissorRight = (int)Defines.TGUI_MINIMUM((int)(bottomRightPosition.x * scaleViewX), scissor[0] + scissor[2]);
                    int scissorBottom = (int)Defines.TGUI_MINIMUM((int)(bottomRightPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1]);

                    if (scissorRight < scissorLeft)
                        scissorRight = scissorLeft;
                    else if (scissorBottom < scissorTop)
                        scissorTop = scissorBottom;

                    GL.glScissor(scissorLeft, target.getSize().y - scissorBottom, scissorRight - scissorLeft, scissorBottom - scissorTop);
                }

                target.draw(tempText, states);

                states.transform.translate(
                	-(float)Math.floor(realRect.left + 0.5f), 
                	-(float)Math.floor(realRect.top + 0.5f));

                if (clippingRequired) {
                    clippingRequired = false;
                    GL.glScissor(scissor[0], scissor[1], scissor[2], scissor[3]);
                }
            }

            states.transform.translate((float)(tabWidth), 0);
        }
	}

	@Override
	public boolean mouseOnWidget(float x, float y) {
        if (m_Loaded) {
            if (getTransform().transformRect(
            		new FloatRect(0, 0, getSize().x, getSize().y))
            		.contains(x, y)) {
                return true;
            }
        }

        if (m_MouseHover) {
            mouseLeftWidget();
        }
        
        m_MouseHover = false;
        return false;
	}

}
