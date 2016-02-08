package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.Borders;
import com.iteye.weimingtom.tgui.Call0;
import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.GL;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.SharedWidgetPtr;
import com.iteye.weimingtom.tgui.Texture;
import com.iteye.weimingtom.tgui.WidgetBorders;
import com.iteye.weimingtom.tgui.WidgetBordersImpl;
import com.iteye.weimingtom.tgui.WidgetPhase;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.RectangleShape;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Text;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20150927
 * @author Administrator
 *
 */
public class ComboBox extends Widget implements WidgetBorders {
	public WidgetBordersImpl _WidgetBordersImpl = new WidgetBordersImpl();
    public static enum ComboBoxCallbacks {
        ItemSelected(WidgetCallbacks.WidgetCallbacksCount.value() * 1),             
        AllComboBoxCallbacks(WidgetCallbacks.WidgetCallbacksCount.value() * 2 - 1),
        ComboBoxCallbacksCount(WidgetCallbacks.WidgetCallbacksCount.value() * 2);
        
        int value;
        
        ComboBoxCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    };
	
    protected String m_LoadedConfigFile;
    protected boolean m_SeparateHoverImage;
    protected int m_NrOfItemsToDisplay;
    protected SharedWidgetPtr<ListBox> m_ListBox;
    protected Texture m_TextureArrowUpNormal;
    protected Texture m_TextureArrowUpHover;
    protected Texture m_TextureArrowDownNormal;
    protected Texture m_TextureArrowDownHover;
    
    public ComboBox() {
        m_SeparateHoverImage = false;
        m_NrOfItemsToDisplay = 0;
        
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_ComboBox;
        m_DraggableWidget = true;

        m_ListBox.get().hide();
        m_ListBox.get().setSize(50, 24);
        m_ListBox.get().setItemHeight(24);
        m_ListBox.get().changeColors();
        m_ListBox.get().getCallbackManager()
        	.bindCallback(new Call0() {
				@Override
				public void call() {
					newItemSelectedCallbackFunction();
				}
        	}, this, ListBox.ListBoxCallbacks.ItemSelected.value());
        m_ListBox.get().getCallbackManager()
        	.bindCallback(new Call0() {
				@Override
				public void call() {
					listBoxUnfocusedCallbackFunction();
				}
        	}, this, Widget.WidgetCallbacks.Unfocused.value());
    }

    public ComboBox(ComboBox copy) {
        super(copy);
        _WidgetBordersImpl = new WidgetBordersImpl(copy._WidgetBordersImpl);
        m_LoadedConfigFile = copy.m_LoadedConfigFile;
        m_SeparateHoverImage = copy.m_SeparateHoverImage;
        m_NrOfItemsToDisplay = copy.m_NrOfItemsToDisplay;
        m_ListBox = copy.m_ListBox.cloneObj();
        
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
    
    public ComboBox assign(ComboBox right) {
        if (this != right) {
            ComboBox temp = new ComboBox(right);
            //this->Widget::operator=(right);
            super.assign(right);
            this._WidgetBordersImpl = new WidgetBordersImpl(right._WidgetBordersImpl);

            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_SeparateHoverImage = temp.m_SeparateHoverImage;
            m_NrOfItemsToDisplay = temp.m_NrOfItemsToDisplay;
            m_ListBox = temp.m_ListBox; //FIXME:
            m_TextureArrowUpNormal = temp.m_TextureArrowUpNormal;
            m_TextureArrowUpHover = temp.m_TextureArrowUpHover;
            m_TextureArrowDownNormal = temp.m_TextureArrowDownNormal;
            m_TextureArrowDownHover = temp.m_TextureArrowDownHover;
        }

        return this;
    }

    public ComboBox cloneObj() {
    	return new ComboBox(this);
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
        if (!configFile.read("ComboBox", properties, values)) {
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

            if ("separatehoverimage".equals(property)) {
                m_SeparateHoverImage = configFile.readBool(value, false);
            } else if ("backgroundcolor".equals(property)) {
                setBackgroundColor(configFile.readColor(value));
            } else if ("textcolor".equals(property)) {
                setTextColor(configFile.readColor(value));
            } else if ("selectedbackgroundcolor".equals(property)) {
                setSelectedBackgroundColor(configFile.readColor(value));
            } else if ("selectedtextcolor".equals(property)) {
                setSelectedTextColor(configFile.readColor(value));
            } else if ("bordercolor".equals(property)) {
                setBorderColor(configFile.readColor(value));
            } else if ("borders".equals(property)) {
                Borders borders = new Borders();
                if (Defines.extractBorders(value, borders)) {
                    setBorders(borders.left, borders.top, borders.right, borders.bottom);
                }
            } else if ("arrowupnormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowUpNormal)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowUpNormalImage in section ComboBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("arrowuphoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowUpHover)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowUpHoverImage in section ComboBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("arrowdownnormalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowDownNormal)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowDownNormalImage in section ComboBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("arrowdownhoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureArrowDownHover)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ArrowDownHoverImage in section ComboBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("scrollbar".equals(property)) {
                if ((value.length() < 3) || 
                	(value.charAt(0) != '"') || 
                	(value.charAt(value.length() - 1) != '"')) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for Scrollbar in section ComboBox in " + configFileFilename + ".");
                    return false;
                }

                if (!m_ListBox.get().setScrollbar(configFileFolder + value.substring(1, 1 + value.length() - 2))) { //FIXME:
                    return false;
                }
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section ComboBox in " + configFileFilename + ".");
            }
        }

        if ((m_TextureArrowUpNormal.data == null) || 
        	(m_TextureArrowDownNormal.data == null)) {
            Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the combo box. Is the ComboBox section in " + configFileFilename + " complete?");
            return false;
        }

        if ((m_TextureArrowUpHover.data != null) && 
        	(m_TextureArrowDownHover.data != null)) {
            m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();
        }

        m_ListBox.get().removeAllItems();

        return m_Loaded = true;
    }

    public String getLoadedConfigFile() {
        return m_LoadedConfigFile;
    }

    public void setSize(float width, float height) {
        if (m_Loaded == false) {
            return;
        }
        if (width  < 0) {
        	width  = -width;
        }
        if (height < 0) {
        	height = -height;
        }

        if (height > this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder) {
            m_ListBox.get().setItemHeight((int)(height - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder));
        } else {
            m_ListBox.get().setItemHeight(10);
        }
        if (m_NrOfItemsToDisplay > 0) {
            m_ListBox.get().setSize(width, (float)(m_ListBox.get().getItemHeight() * (Defines.TGUI_MINIMUM(m_NrOfItemsToDisplay, m_ListBox.get().getItems().size())) + 2 * this._WidgetBordersImpl.m_BottomBorder));
    	} else {
            m_ListBox.get().setSize(width, (float)(m_ListBox.get().getItemHeight() * m_ListBox.get().getItems().size() + 2 * this._WidgetBordersImpl.m_BottomBorder));
    	}
    }

    public Vector2f getSize() {
        if (m_Loaded) {
            return new Vector2f(m_ListBox.get().getSize().x, (float)(m_ListBox.get().getItemHeight() + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder));
        } else {
            return new Vector2f(0, 0);
        }
    }

    public void setItemsToDisplay(int nrOfItemsInList) {
        m_NrOfItemsToDisplay = nrOfItemsInList;

        if (m_NrOfItemsToDisplay < m_ListBox.get().m_Items.size()) {
            m_ListBox.get().setSize(m_ListBox.get().getSize().x, (m_NrOfItemsToDisplay * m_ListBox.get().getItemHeight()) + 2.0f * this._WidgetBordersImpl.m_BottomBorder);
        }
    }

    public int getItemsToDisplay() {
        return m_NrOfItemsToDisplay;
    }

    public void changeColors(Color backgroundColor,
                      Color textColor,
                      Color selectedBackgroundColor,
                      Color selectedTextColor,
                      Color borderColor) {
        m_ListBox.get().changeColors(backgroundColor, textColor, selectedBackgroundColor, selectedTextColor, borderColor);
    }

    public void setBackgroundColor(Color backgroundColor) {
        m_ListBox.get().setBackgroundColor(backgroundColor);
    }

    public void setTextColor(Color textColor) {
        m_ListBox.get().setTextColor(textColor);
    }

    public void setSelectedBackgroundColor(Color selectedBackgroundColor) {
        m_ListBox.get().setSelectedBackgroundColor(selectedBackgroundColor);
    }

    public void setSelectedTextColor(Color selectedTextColor) {
        m_ListBox.get().setSelectedTextColor(selectedTextColor);
    }

    public void setBorderColor(Color borderColor) {
        m_ListBox.get().setBorderColor(borderColor);
    }

    public Color getBackgroundColor() {
        return m_ListBox.get().getBackgroundColor();
    }
    
    public Color getTextColor() {
        return m_ListBox.get().getTextColor();
    }

    public Color getSelectedBackgroundColor() {
        return m_ListBox.get().getSelectedBackgroundColor();
    }

    public Color getSelectedTextColor() {
        return m_ListBox.get().getSelectedTextColor();
    }

    public Color getBorderColor() {
        return m_ListBox.get().getBorderColor();
    }

    public void setTextFont(Font font) {
        m_ListBox.get().setTextFont(font);
    }

    public Font getTextFont() {
        return m_ListBox.get().getTextFont();
    }

    @Override
	public void setBorders(int leftBorder, int topBorder,
			int rightBorder, int bottomBorder) {
        int itemHeight = m_ListBox.get().getItemHeight() + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder - topBorder - bottomBorder;

        this._WidgetBordersImpl.m_LeftBorder = leftBorder;
        this._WidgetBordersImpl.m_TopBorder = topBorder;
        this._WidgetBordersImpl.m_RightBorder = rightBorder;
        this._WidgetBordersImpl.m_BottomBorder = bottomBorder;
        m_ListBox.get().setBorders(this._WidgetBordersImpl.m_LeftBorder, this._WidgetBordersImpl.m_BottomBorder, this._WidgetBordersImpl.m_RightBorder, this._WidgetBordersImpl.m_BottomBorder);

        if (m_Loaded == false) {
            return;
        }

        if (m_ListBox.get().getSize().x < 50 + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder + m_TextureArrowDownNormal.getSize().x) {
            m_ListBox.get().setSize(50.0f + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder + m_TextureArrowDownNormal.getSize().x, m_ListBox.get().getSize().y);
        }
        
        m_ListBox.get().setItemHeight(itemHeight);
	}

    public int addItem(String item) {
        if (m_Loaded == false) {
            return 0;
        }
        
        if ((m_NrOfItemsToDisplay == 0) || (m_NrOfItemsToDisplay > m_ListBox.get().getItems().size())) {
            m_ListBox.get().setSize(m_ListBox.get().getSize().x, (m_ListBox.get().getItemHeight() * (m_ListBox.get().getItems().size() + 1)) + 2.0f * this._WidgetBordersImpl.m_BottomBorder);
        }
        
        return m_ListBox.get().addItem(item);
    }

    public boolean setSelectedItem(String itemName) {
        return m_ListBox.get().setSelectedItem(itemName);
    }

    public boolean setSelectedItem(int index) {
        return m_ListBox.get().setSelectedItem(index);
    }

    public void deselectItem() {
        m_ListBox.get().deselectItem();
    }

    public boolean removeItem(int index) {
        return m_ListBox.get().removeItem(index);
    }

    public boolean removeItem(String itemName) {
        return m_ListBox.get().removeItem(itemName);
    }

    public void removeAllItems() {
        m_ListBox.get().removeAllItems();
    }

    public String getItem(int index) {
        return m_ListBox.get().getItem(index);
    }

    public int getItemIndex(String itemName) {
        return m_ListBox.get().getItemIndex(itemName);
    }

    public List<String> getItems() {
        return m_ListBox.get().getItems();
    }

    public String getSelectedItem() {
        return m_ListBox.get().getSelectedItem();
    }

    public int getSelectedItemIndex() {
        return m_ListBox.get().getSelectedItemIndex();
    }

    public boolean setScrollbar(String scrollbarConfigFileFilename) {
        return m_ListBox.get().setScrollbar(scrollbarConfigFileFilename);
    }

    public void removeScrollbar() {
        m_ListBox.get().removeScrollbar();
    }

    public void setMaximumItems(int maximumItems) {
        m_ListBox.get().setMaximumItems(maximumItems);
    }

    public int getMaximumItems() {
        return m_ListBox.get().getMaximumItems();
    }

    public void setTransparency(int transparency) {
        //Widget::
        super.setTransparency(transparency);

        m_ListBox.get().setTransparency(m_Opacity);

        m_TextureArrowUpNormal.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureArrowDownNormal.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureArrowUpHover.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureArrowDownHover.sprite.setColor(new Color(255, 255, 255, m_Opacity));
    }

    @Override
	public boolean mouseOnWidget(float x, float y) {
        if (m_Loaded == false) {
            return false;
        }

        Vector2f position = getPosition();

        if ((x > position.x) && (x < position.x + m_ListBox.get().getSize().x) && 
        	(y > position.y) && (y < position.y + m_ListBox.get().getItemHeight() + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder)) {
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

        if (!m_ListBox.get().isVisible()) {
            showListBox();

            if (m_ListBox.get().m_Scroll != null) {
                if (m_NrOfItemsToDisplay > 0) {
                    if ((int)(m_ListBox.get().getSelectedItemIndex() + 1) > m_NrOfItemsToDisplay) {
                        m_ListBox.get().m_Scroll.setValue(((int)(m_ListBox.get().getSelectedItemIndex()) - m_NrOfItemsToDisplay + 1) * m_ListBox.get().getItemHeight());
                	} else {
                        m_ListBox.get().m_Scroll.setValue(0);
                	}
               	}
            }
        } else {
            hideListBox();
        }
    }

    public void leftMouseReleased(float x, float y) {
        m_MouseDown = false;
    }

    public void mouseWheelMoved(int delta, int x, int y) {
        if (!m_ListBox.get().isVisible()) {
            if (delta < 0) {
                if ((int)(m_ListBox.get().getSelectedItemIndex() + 1) < m_ListBox.get().m_Items.size()) {
                    m_ListBox.get().setSelectedItem((int)(m_ListBox.get().getSelectedItemIndex() + 1));
                }
            } else {
                if (m_ListBox.get().getSelectedItemIndex() > 0) {
                    m_ListBox.get().setSelectedItem((int)(m_ListBox.get().getSelectedItemIndex()-1));
                }
            }
        }
    }

    public void mouseNoLongerDown() {
        m_MouseDown = false;
    }

    public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            load(value);
        } else if ("itemstodisplay".equals(property)) {
            setItemsToDisplay(Integer.parseInt(value.trim()));
        } else if ("backgroundcolor".equals(property)) {
            setBackgroundColor(Defines.extractColor(value));
        } else if ("textcolor".equals(property)) {
            setTextColor(Defines.extractColor(value));
        } else if ("selectedbackgroundcolor".equals(property)) {
            setSelectedBackgroundColor(Defines.extractColor(value));
        } else if ("selectedtextcolor".equals(property)) {
            setSelectedTextColor(Defines.extractColor(value));
        } else if ("bordercolor".equals(property)) {
            setBorderColor(Defines.extractColor(value));
        } else if ("borders".equals(property)) {
            Borders borders = new Borders();
            if (Defines.extractBorders(value, borders)) {
                setBorders(borders.left, borders.top, borders.right, borders.bottom);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Borders' property.");
            }
        } else if ("maximumitems".equals(property)) {
            setMaximumItems(Integer.parseInt(value.trim()));
        } else if (property == "items") {
            removeAllItems();

            List<String> items = new ArrayList<String>();
            Defines.decodeList(value, items);

            for (String it : items) {
                addItem(it);
            }
        } else if ("selecteditem".equals(property)) {
            setSelectedItem(Integer.parseInt(value.trim()));
        } else if ("callback".equals(property)) {
            //Widget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("ItemSelected".equals(it)) || 
                	("itemselected".equals(it))) {
                    this.getCallbackManager().bindCallback(ComboBoxCallbacks.ItemSelected.value());
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
        } else if ("itemstodisplay".equals(property)) {
            value[0] = Integer.toString(getItemsToDisplay());
    	} else if ("backgroundcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getBackgroundColor().r)) + "," + 
            	Integer.toString((int)(getBackgroundColor().g)) + "," + 
            	Integer.toString((int)(getBackgroundColor().b)) + "," + 
            	Integer.toString((int)(getBackgroundColor().a)) + 
            	")";
		} else if ("textcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getTextColor().r)) + "," + 
            	Integer.toString((int)(getTextColor().g)) + "," + 
            	Integer.toString((int)(getTextColor().b)) + "," + 
            	Integer.toString((int)(getTextColor().a)) + 
            	")";
		} else if ("selectedbackgroundcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getSelectedBackgroundColor().r)) + "," + 
            	Integer.toString((int)(getSelectedBackgroundColor().g)) + "," + 
            	Integer.toString((int)(getSelectedBackgroundColor().b)) + "," + 
            	Integer.toString((int)(getSelectedBackgroundColor().a)) + 
            	")";
		} else if ("selectedtextcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getSelectedTextColor().r)) + "," + 
            	Integer.toString((int)(getSelectedTextColor().g)) + "," + 
            	Integer.toString((int)(getSelectedTextColor().b)) + "," + 
            	Integer.toString((int)(getSelectedTextColor().a)) + 
            	")";
		} else if ("bordercolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getBorderColor().r)) + "," + 
            	Integer.toString((int)(getBorderColor().g)) + "," + 
            	Integer.toString((int)(getBorderColor().b)) + "," + 
            	Integer.toString((int)(getBorderColor().a)) + 
            	")";
		} else if ("borders".equals(property)) {
            value[0] = "(" + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().left) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().top) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().right) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().bottom) + 
            	")";
		} else if ("maximumitems".equals(property)) {
            value[0] = Integer.toString(getMaximumItems());
		} else if ("items".equals(property)) {
            Defines.encodeList(m_ListBox.get().getItems(), value);
		} else if ("selecteditem".equals(property)) {
            value[0] = Integer.toString(getSelectedItemIndex());
		} else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //Widget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(ComboBoxCallbacks.ItemSelected.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ComboBoxCallbacks.ItemSelected.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ComboBoxCallbacks.ItemSelected.value()).get(0) == null)) {
                callbacks.add("ItemSelected");
            }

            Defines.encodeList(callbacks, value);

            if (value == null || value.length == 0 || 
            	tempValue == null || tempValue.length == 0) {
                value[0] += tempValue[0];
            } else {
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
        list.add(new Pair<String, String>("ItemsToDisplay", "uint"));
        list.add(new Pair<String, String>("BackgroundColor", "color"));
        list.add(new Pair<String, String>("TextColor", "color"));
        list.add(new Pair<String, String>("SelectedBackgroundColor", "color"));
        list.add(new Pair<String, String>("SelectedTextColor", "color"));
        list.add(new Pair<String, String>("BorderColor", "color"));
        list.add(new Pair<String, String>("Borders", "borders"));
        list.add(new Pair<String, String>("MaximumItems", "uint"));
        list.add(new Pair<String, String>("Items", "string"));
        list.add(new Pair<String, String>("SelectedItem", "int"));
        return list;
    }
    
    protected void initialize(Container parent) {
        m_Parent = parent;
        m_ListBox.get().setTextFont(m_Parent.getGlobalFont());
    }

    protected void showListBox() {
        if (!m_ListBox.get().isVisible()) {
            m_ListBox.get().show();

            Vector2f position = new Vector2f(getPosition().x, getPosition().y + m_ListBox.get().getItemHeight() + this._WidgetBordersImpl.m_TopBorder);

            Widget container = this;
            while (container.getParent() != null) {
                container = container.getParent();
                Vector2f.plusEqual(position, container.getPosition());

                if (container.getWidgetType() == WidgetTypes.Type_ChildWindow) {
                    ChildWindow child = (ChildWindow)(container);
                    position.x += child._WidgetBordersImpl.getBorders().left;
                    position.y += child._WidgetBordersImpl.getBorders().top + child.getTitleBarHeight();
                }
            }

            m_ListBox.get().setPosition(position);
            ((Container)container).add(m_ListBox);
            m_ListBox.get().focus();
        }
    }

    protected void hideListBox() {
        if (m_ListBox.get().isVisible()) {
            m_ListBox.get().hide();

            Widget container = this;
            while (container.getParent() != null) {
                container = container.getParent();
            }
            ((Container)container).remove(m_ListBox);
        }
    }

    protected void newItemSelectedCallbackFunction() {
        if (this.getCallbackManager().m_CallbackFunctions
        	.get(ComboBoxCallbacks.ItemSelected.value()).isEmpty() == false) {
            this.getCallbackManager().m_Callback.text = m_ListBox.get().getSelectedItem();
            this.getCallbackManager().m_Callback.value = m_ListBox.get().getSelectedItemIndex();
            this.getCallbackManager().m_Callback.trigger = ComboBoxCallbacks.ItemSelected.value();
            addCallback();
        }

        hideListBox();
    }

    protected void listBoxUnfocusedCallbackFunction() {
        if (m_MouseHover == false)
            hideListBox();
    }

    @Override
    public void draw(RenderTarget target, RenderStates states) {
        if (m_Loaded == false) {
            return;
        }
        
        float scaleViewX = target.getSize().x / target.getView().getSize().x;
        float scaleViewY = target.getSize().y / target.getView().getSize().y;

        Vector2f viewPosition = Vector2f.minus(Vector2f.devide(target.getView().getSize(), 2.f), target.getView().getCenter());

        Vector2f temp = new Vector2f((float)(this._WidgetBordersImpl.m_LeftBorder), (float)(this._WidgetBordersImpl.m_TopBorder));
        Vector2f topLeftPosition = states.transform.transformPoint(
        		Vector2f.plus(Vector2f.plus(getPosition(), temp), viewPosition));
        Vector2f bottomRightPosition = states.transform.transformPoint(
        			getPosition().x + m_ListBox.get().getSize().x - this._WidgetBordersImpl.m_RightBorder - (m_TextureArrowDownNormal.getSize().x * ((float)(m_ListBox.get().getItemHeight()) / m_TextureArrowDownNormal.getSize().y)) + viewPosition.x,
                    getPosition().y + m_ListBox.get().getSize().y - this._WidgetBordersImpl.m_BottomBorder + viewPosition.y);

        Transform.multiplyEqual(states.transform, getTransform());

        Transform oldTransform = states.transform;

        RectangleShape border = new RectangleShape(
        	new Vector2f((float)(this._WidgetBordersImpl.m_LeftBorder), 
        		(float)(m_ListBox.get().getItemHeight() + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder)));
        border.setFillColor(m_ListBox.get().m_BorderColor);
        target.draw(border, states);

        border.setSize(new Vector2f(m_ListBox.get().getSize().x, 
        	(float)(this._WidgetBordersImpl.m_TopBorder)));
        target.draw(border, states);

        border.setPosition(m_ListBox.get().getSize().x - this._WidgetBordersImpl.m_RightBorder, 0);
        border.setSize(new Vector2f(
        	(float)(this._WidgetBordersImpl.m_RightBorder), 
        	(float)(m_ListBox.get().getItemHeight() + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder)));
        target.draw(border, states);

        border.setPosition(0, (float)(m_ListBox.get().getItemHeight() + this._WidgetBordersImpl.m_TopBorder));
        border.setSize(new Vector2f(
        	m_ListBox.get().getSize().x, 
        	(float)(this._WidgetBordersImpl.m_BottomBorder)));
        target.draw(border, states);

        states.transform.translate(
        	(float)(this._WidgetBordersImpl.m_LeftBorder), (float)(this._WidgetBordersImpl.m_TopBorder));

        RectangleShape Front = new RectangleShape(
        	new Vector2f((float)(m_ListBox.get().getSize().x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_RightBorder),
        			(float)(m_ListBox.get().getItemHeight())));
        Front.setFillColor(m_ListBox.get().getBackgroundColor());
        target.draw(Front, states);

        Text tempText = new Text("kg", m_ListBox.get().getTextFont());
        tempText.setCharacterSize(m_ListBox.get().getItemHeight());
        tempText.setCharacterSize((int)(tempText.getCharacterSize() - tempText.getLocalBounds().top));
        tempText.setColor(m_ListBox.get().getTextColor());

        int[] scissor = new int[4];
        GL.glGetIntegerv(GL.GL_SCISSOR_BOX, scissor);

        int scissorLeft = (int)Defines.TGUI_MAXIMUM((int)(topLeftPosition.x * scaleViewX), scissor[0]);
        int scissorTop = (int)Defines.TGUI_MAXIMUM((int)(topLeftPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1] - scissor[3]);
        int scissorRight = (int)Defines.TGUI_MINIMUM((int)(bottomRightPosition.x  * scaleViewX), scissor[0] + scissor[2]);
        int scissorBottom = (int)Defines.TGUI_MINIMUM((int)(bottomRightPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1]);

        if (scissorRight < scissorLeft) {
            scissorRight = scissorLeft;
        } else if (scissorBottom < scissorTop) {
            scissorTop = scissorBottom;
        }

        GL.glScissor(scissorLeft, target.getSize().y - scissorBottom, scissorRight - scissorLeft, scissorBottom - scissorTop);

        states.transform.translate(2f, (float)Math.floor(((int)(m_ListBox.get().getItemHeight()) - tempText.getLocalBounds().height) / 2.0f -  tempText.getLocalBounds().top));
        tempText.setString(m_ListBox.get().getSelectedItem());
        target.draw(tempText, states);

        GL.glScissor(scissor[0], scissor[1], scissor[2], scissor[3]);

        states.transform = oldTransform;

        if (m_ListBox.get().isVisible()) {
            float scaleFactor = (float)(m_ListBox.get().getItemHeight()) / m_TextureArrowUpNormal.getSize().y;
            states.transform.translate(m_ListBox.get().getSize().x - this._WidgetBordersImpl.m_RightBorder - (m_TextureArrowUpNormal.getSize().x * scaleFactor), (float)(this._WidgetBordersImpl.m_TopBorder));
            states.transform.scale(scaleFactor, scaleFactor);

            if (m_SeparateHoverImage) {
                if ((m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureArrowUpHover.getSprite(), states);
            	} else {
                    target.draw(m_TextureArrowUpNormal.getSprite(), states);
            	}
            } else {
                target.draw(m_TextureArrowUpNormal.getSprite(), states);

                if ((m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureArrowUpHover.getSprite(), states);
                }
            }
        } else {
            float scaleFactor = (float)(m_ListBox.get().getItemHeight()) / m_TextureArrowDownNormal.getSize().y;
            states.transform.translate(m_ListBox.get().getSize().x - this._WidgetBordersImpl.m_RightBorder - (m_TextureArrowDownNormal.getSize().x * scaleFactor), (float)(this._WidgetBordersImpl.m_TopBorder));
            states.transform.scale(scaleFactor, scaleFactor);

            if (m_SeparateHoverImage) {
                if ((m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureArrowDownHover.getSprite(), states);
                } else {
                    target.draw(m_TextureArrowDownNormal.getSprite(), states);
                }
            } else {
                target.draw(m_TextureArrowDownNormal.getSprite(), states);

                if ((m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureArrowDownHover.getSprite(), states);
                }
            }
        }
    }
}
