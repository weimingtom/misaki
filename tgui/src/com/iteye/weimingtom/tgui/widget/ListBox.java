package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.Borders;
import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.GL;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.WidgetBorders;
import com.iteye.weimingtom.tgui.WidgetBordersImpl;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.FloatRect;
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.RectangleShape;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Text;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;
import com.iteye.weimingtom.tgui.sf.Vector2u;

/**
 * 20151021
 * @author Administrator
 *
 */
public class ListBox extends Widget implements WidgetBorders {
	public WidgetBordersImpl _WidgetBordersImpl = new WidgetBordersImpl();
    public static enum ListBoxCallbacks {
        ItemSelected(WidgetCallbacks.WidgetCallbacksCount.value() * 1),           
        AllListBoxCallbacks(WidgetCallbacks.WidgetCallbacksCount.value() * 2 - 1), 
        ListBoxCallbacksCount(WidgetCallbacks.WidgetCallbacksCount.value() * 2);
        
        int value;
        
        ListBoxCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    };
	
    protected String m_LoadedConfigFile;
    protected List<String> m_Items = new ArrayList<String>();
    protected int m_SelectedItem;
    protected Vector2u m_Size = new Vector2u();
    protected int m_ItemHeight;
    protected int m_TextSize;
    protected int m_MaxItems;
    protected Scrollbar m_Scroll;
    protected Color m_BackgroundColor;
    protected Color m_TextColor;
    protected Color m_SelectedBackgroundColor;
    protected Color m_SelectedTextColor;
    protected Color m_BorderColor;
    protected Font m_TextFont;
    
    public ListBox() {
        m_SelectedItem = -1;
        m_Size = new Vector2u(50, 100);
        m_ItemHeight = 24;
        m_TextSize = 19;
        m_MaxItems = 0;
        m_Scroll = null;
        m_TextFont = null;
        
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_ListBox;
        m_DraggableWidget = true;
        m_Loaded = true;

        changeColors();
    }
    
    public ListBox(ListBox copy) {
        //Widget
        super(copy);
        //WidgetBorders            (copy),
        _WidgetBordersImpl = new WidgetBordersImpl(copy._WidgetBordersImpl);
        m_LoadedConfigFile = copy.m_LoadedConfigFile;
        m_Items = copy.m_Items;
        m_SelectedItem = copy.m_SelectedItem;
        m_Size = copy.m_Size;
        m_ItemHeight = copy.m_ItemHeight;
        m_TextSize = copy.m_TextSize;
        m_MaxItems = copy.m_MaxItems;
        m_BackgroundColor = copy.m_BackgroundColor;
        m_TextColor = copy.m_TextColor;
        m_SelectedBackgroundColor = copy.m_SelectedBackgroundColor;
        m_SelectedTextColor = copy.m_SelectedTextColor;
        m_BorderColor = copy.m_BorderColor;
        m_TextFont = copy.m_TextFont;
        
        if (copy.m_Scroll != null) {
            m_Scroll = new Scrollbar(copy.m_Scroll);
        } else {
            m_Scroll = null;
        }
    }

    public void destroy() {
    	super.destroy();
    	
        if (m_Scroll != null) {
            //delete m_Scroll;
        	m_Scroll.destroy();
        	m_Scroll = null;
        }
    }
    
    public ListBox assign(ListBox right) {
        if (this != right) {
            ListBox temp = new ListBox(right);
            //this->Widget::operator=(right);
            super.assign(right);
            //FIXME:
            //this->WidgetBorders::operator=(right);

            if (m_Scroll != null) {
                m_Scroll.destroy();
                m_Scroll = null;
            }

            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_Items = temp.m_Items;
            m_SelectedItem = temp.m_SelectedItem;
            m_Size = temp.m_Size;
            m_ItemHeight = temp.m_ItemHeight;
            m_TextSize = temp.m_TextSize;
            m_MaxItems = temp.m_MaxItems;
            m_Scroll = temp.m_Scroll;
            m_BackgroundColor = temp.m_BackgroundColor;
            m_TextColor = temp.m_TextColor;
            m_SelectedBackgroundColor = temp.m_SelectedBackgroundColor;
            m_SelectedTextColor = temp.m_SelectedTextColor;
            m_BorderColor = temp.m_BorderColor;
            m_TextFont = temp.m_TextFont;
        }

        return this;
    }

    public ListBox cloneObj() {
        return new ListBox(this);
    }
    
    public boolean load(String configFileFilename) {
        m_LoadedConfigFile = configFileFilename;

        if (m_Scroll != null) {
            m_Scroll.destroy();
            m_Scroll = null;
        }

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("ListBox", properties, values)) {
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
                }
            } else if ("scrollbar".equals(property)) {
                if ((value.length() < 3) || 
                	(value.charAt(0) != '"') || 
                	(value.charAt(value.length() - 1) != '"')) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for Scrollbar in section ChatBox in " + configFileFilename + ".");
                    return false;
                }

                m_Scroll = new Scrollbar();
                //FIXME:
                if (m_Scroll.load(configFileFolder + value.substring(1, value.length() - 2 + 1)) == false) {
                    //delete m_Scroll;
                	m_Scroll.destroy();
                    m_Scroll = null;

                    return false;
                } else {
                    m_Scroll.setVerticalScroll(true);
                    m_Scroll.setSize(m_Scroll.getSize().x, (float)(m_Size.y) - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
                    m_Scroll.setLowValue(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
                    m_Scroll.setMaximum(m_Items.size() * m_ItemHeight);
                }
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section ListBox in " + configFileFilename + ".");
            }
        }

        return true;
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

        if (m_Scroll == null) {
            width = (int)Defines.TGUI_MAXIMUM(50.f + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder, width);
        } else {
            width = (int)Defines.TGUI_MAXIMUM(50.f + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder + m_Scroll.getSize().x, width);
        }
        
        if (height < (m_ItemHeight + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder)) {
            height = (float)(m_ItemHeight + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder);
        }
        
        m_Size.x = (int)(width);
        m_Size.y = (int)(height);

        if (m_Scroll != null) {
            m_Scroll.setSize(m_Scroll.getSize().x, (float)(m_Size.y) - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
            m_Scroll.setLowValue(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
        }
    }

    public Vector2f getSize() {
        return new Vector2f((float)(m_Size.x), (float)(m_Size.y));
    }

    public void changeColors() {
    	this.changeColors(
    		Color.White, Color.Black, 
    		new Color(50, 100, 200),
    		Color.White, Color.Black);
    }
    
    public void changeColors(Color backgroundColor,
            Color textColor,
            Color selectedBackgroundColor,
            Color selectedTextColor,
            Color borderColor) {
        // Store the new colors
        m_BackgroundColor         = backgroundColor;
        m_TextColor               = textColor;
        m_SelectedBackgroundColor = selectedBackgroundColor;
        m_SelectedTextColor       = selectedTextColor;
        m_BorderColor             = borderColor;
    }
    
    public void setBackgroundColor(Color backgroundColor) {
        m_BackgroundColor = backgroundColor;
    }

    public void setTextColor(Color textColor) {
        m_TextColor = textColor;
    }

    public void setSelectedBackgroundColor(Color selectedBackgroundColor) {
        m_SelectedBackgroundColor = selectedBackgroundColor;    	
    }
    
    public void setSelectedTextColor(Color selectedTextColor) {
        m_SelectedTextColor = selectedTextColor;    	
    }

    public void setBorderColor(Color borderColor) {
        m_BorderColor = borderColor;    	
    }

    public Color getBackgroundColor() {
        return m_BackgroundColor;
    }

    public Color getTextColor() {
        return m_TextColor;
    }

    public Color getSelectedBackgroundColor() {
        return m_SelectedBackgroundColor;
    }
    
    public Color getSelectedTextColor() {
        return m_SelectedTextColor;
    }

    public Color getBorderColor() {
        return m_BorderColor;
    }

    public void setTextFont(Font font) {
        m_TextFont = font;
    }

    public Font getTextFont() {
        return m_TextFont;
    }

    public int addItem(String itemName) {
        if ((m_MaxItems == 0) || 
        	(m_Items.size() < m_MaxItems)) {
            if (m_Scroll == null) {
                int maximumItems = (m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder) / m_ItemHeight;

                if (m_Items.size() == maximumItems) {
                    return -1;
                }
            }

            m_Items.add(itemName);

            if (m_Scroll != null) {
                m_Scroll.setMaximum(m_Items.size() * m_ItemHeight);
            }
            
            return m_Items.size() - 1;
        } else {
            return -1;
        }
    }

    public boolean setSelectedItem(String itemName) {
        for (int i = 0; i < m_Items.size(); ++i) {
            if (m_Items.get(i).equals(itemName)) {
                m_SelectedItem = (int)(i);
                return true;
            }
        }

        Defines.TGUI_OUTPUT("TGUI warning: Failed to select the item in the list box. The name didn't match any item.");

        m_SelectedItem = -1;
        return false; 
    }
    
    public boolean setSelectedItem(int index) {
        if (index < 0) {
            deselectItem();
            return true;
        }

        if (index > (int)(m_Items.size()-1)) {
            Defines.TGUI_OUTPUT("TGUI warning: Failed to select the item in the list box. The index was too high.");
            m_SelectedItem = -1;
            return false;
        }

        m_SelectedItem = index;
        return true;
    }
    
    public void deselectItem() {
        m_SelectedItem = -1;
    }

    public boolean removeItem(int index) {
        if (index > m_Items.size() - 1) {
            Defines.TGUI_OUTPUT("TGUI warning: Failed to remove the item from the list box. The index was too high.");
            return false;
        }

        m_Items.remove(index);

        if (m_Scroll != null) {
            m_Scroll.setMaximum(m_Items.size() * m_ItemHeight);
        }
        
        if (m_SelectedItem == (int)(index)) {
            m_SelectedItem = -1;
        } else if (m_SelectedItem > (int)(index)) {
            --m_SelectedItem;
        }

        return true;
    }

    public boolean removeItem(String itemName) {
        for (int i = 0; i < m_Items.size(); ++i) {
            if (m_Items.get(i).equals(itemName)) {
                m_Items.remove(i);

                if (m_SelectedItem == (int)(i)) {
                    m_SelectedItem = -1;
            	} else if (m_SelectedItem > (int)(i)) {
                    --m_SelectedItem;
                }

                if (m_Scroll != null) {
                    m_Scroll.setMaximum(m_Items.size() * m_ItemHeight);
                }
                
                return true;
            }
        }

        Defines.TGUI_OUTPUT("TGUI warning: Failed to remove the item from the list box. The name didn't match any item.");
        return false;
    }

    public void removeAllItems() {
        m_Items.clear();

        m_SelectedItem = -1;

        if (m_Scroll != null) {
            m_Scroll.setMaximum(0);
        }
    }

    public String getItem(int index) {
        if (index > m_Items.size() - 1) {
            Defines.TGUI_OUTPUT("TGUI warning: The index of the item was too high. Returning an empty string.");
            return "";
        }

        return m_Items.get(index);
    }

    public int getItemIndex(String itemName) {
        for (int i = 0; i < m_Items.size(); ++i) {
            if (m_Items.get(i).equals(itemName)) {
                return i;
            }
        }

        Defines.TGUI_OUTPUT("TGUI warning: The name didn't match any item. Returning -1 as item index.");
        return -1;
    }
    
    public List<String> getItems() {
        return m_Items;
    }
    
    public String getSelectedItem() {
        if (m_SelectedItem == -1) {
            return "";
        } else {
            return m_Items.get(m_SelectedItem);
        }
    }

    public int getSelectedItemIndex() {
        return m_SelectedItem;
    }

    public boolean setScrollbar(String scrollbarConfigFileFilename) {
        if (scrollbarConfigFileFilename.length() == 0) {
            removeScrollbar();
            return true;
        }

        if (m_Scroll != null) {
            //delete m_Scroll;
        	m_Scroll.destroy();
        }

        m_Scroll = new Scrollbar();
        if (m_Scroll.load(scrollbarConfigFileFilename) == false) {
            //delete m_Scroll;
        	m_Scroll.destroy();
        	m_Scroll = null;

            return false;
        } else {
            m_Scroll.setVerticalScroll(true);
            m_Scroll.setSize(m_Scroll.getSize().x, (float)(m_Size.y) - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
            m_Scroll.setLowValue(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
            m_Scroll.setMaximum(m_Items.size() * m_ItemHeight);

            return true;
        }
    }
    
    public void removeScrollbar() {
        // Delete the scrollbar
        //delete m_Scroll;
    	m_Scroll.destroy();
        m_Scroll = null;

        if ((m_Items.size() * m_ItemHeight) > m_Size.y) {
            m_MaxItems = m_Size.y / m_ItemHeight;

            //FIXME:!!!!!!
            int size = m_Items.size();
            int index = m_MaxItems;
            for (int i = 0; i < size - index; i++) {
            	m_Items.remove(index);
            }
        }
    }

    public void setItemHeight(int itemHeight) {
        if (itemHeight < 10) {
            itemHeight = 10;
        }

        m_ItemHeight = itemHeight;
        m_TextSize   = (int)(itemHeight * 0.8f);

        if (m_Scroll == null) {
            if ((m_Items.size() * m_ItemHeight) > m_Size.y) {
                m_MaxItems = m_Size.y / m_ItemHeight;

                //FIXME:
                //m_Items.erase(m_Items.begin() + m_MaxItems, m_Items.end());
                //FIXME:!!!!!!
                int size = m_Items.size();
                int index = m_MaxItems;
                for (int i = 0; i < size - index; i++) {
                	m_Items.remove(index);
                }
            }
        } else {
            m_Scroll.setMaximum(m_Items.size() * m_ItemHeight);
        }
    }

    public int getItemHeight() {
        return m_ItemHeight;
    }

    public void setMaximumItems(int maximumItems) {
        m_MaxItems = maximumItems;

        if ((m_MaxItems > 0) && (m_MaxItems < m_Items.size())) {
            //m_Items.erase(m_Items.begin() + m_MaxItems, m_Items.end());
            //FIXME:!!!!!!
            int size = m_Items.size();
            int index = m_MaxItems;
            for (int i = 0; i < size - index; i++) {
            	m_Items.remove(index);
            }
        	
            if (m_Scroll != null) {
                m_Scroll.setMaximum(m_Items.size() * m_ItemHeight);
            }
        }
    }
    
    public int getMaximumItems() {
        return m_MaxItems;
    }

	@Override
	public void setBorders(int leftBorder, int topBorder,
			int rightBorder, int bottomBorder) {
        this._WidgetBordersImpl.m_LeftBorder = leftBorder;
        this._WidgetBordersImpl.m_TopBorder = topBorder;
        this._WidgetBordersImpl.m_RightBorder = rightBorder;
        this._WidgetBordersImpl.m_BottomBorder = bottomBorder;

        if (m_Size.x < (50 + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder)) {
            m_Size.x = 50 + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder;
        }

        if (m_Scroll == null) {
            if (m_Items.size() > 0) {
                if (m_Size.y < ((m_Items.size() * m_ItemHeight) - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder))
                    m_Size.y = (m_Items.size() * m_ItemHeight) - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder;
            } else {
                if (m_Size.y < (m_ItemHeight - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder)) {
                    m_Size.y = m_ItemHeight - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder;
                }
            }
        } else {
            m_Scroll.setSize(m_Scroll.getSize().x, (float)(m_Size.y) - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
            m_Scroll.setLowValue(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
        }
	}

    public void setTransparency(int transparency) {
        //Widget::
        super.setTransparency(transparency);

        if (m_Scroll != null) {
            m_Scroll.setTransparency(m_Opacity);
        }
    }

	@Override
	public boolean mouseOnWidget(float x, float y) {
        Vector2f position = getPosition();

        if (m_Scroll != null) {
            m_Scroll.setPosition(position.x + m_Size.x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, position.y + this._WidgetBordersImpl.m_TopBorder);

            m_Scroll.mouseOnWidget(x, y);

            m_Scroll.setPosition(0, 0);
        }

        if (getTransform().transformRect(
        		new FloatRect((float)(this._WidgetBordersImpl.m_LeftBorder), 
        			(float)(this._WidgetBordersImpl.m_TopBorder), 
        			(float)(m_Size.x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_RightBorder), 
        			(float)(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder)))
        		.contains(x, y)) {
            return true;
		} else {
            if (m_MouseHover)
                mouseLeftWidget();

            m_MouseHover = false;
            return false;
        }
	}

    public void leftMousePressed(float x, float y) {
        m_MouseDown = true;

        boolean clickedOnListBox = true;

        if (m_Scroll != null) {
            m_Scroll.setPosition(getPosition().x + m_Size.x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, getPosition().y + this._WidgetBordersImpl.m_TopBorder);

            if (m_Scroll.mouseOnWidget(x, y)) {
                m_Scroll.leftMousePressed(x, y);
                clickedOnListBox = false;
            }

            m_Scroll.setPosition(0, 0);
        }

        if (clickedOnListBox) {
            int oldSelectedItem = m_SelectedItem;

            if ((m_Scroll != null) && 
            	(m_Scroll.getLowValue() < m_Scroll.getMaximum())) {
                if (y - getPosition().y - this._WidgetBordersImpl.m_TopBorder <= (m_ItemHeight - (m_Scroll.getValue() % m_ItemHeight))) {
                    m_SelectedItem = (int)(m_Scroll.getValue() / m_ItemHeight);
                } else {
                    if ((m_Scroll.getValue() % m_ItemHeight) == 0) {
                        m_SelectedItem = (int)((y - getPosition().y - this._WidgetBordersImpl.m_TopBorder) / m_ItemHeight + (m_Scroll.getValue() / m_ItemHeight));
                    } else {
                        m_SelectedItem = (int)((((y - getPosition().y - this._WidgetBordersImpl.m_TopBorder) - (m_ItemHeight - (m_Scroll.getValue() % m_ItemHeight))) / m_ItemHeight) + (m_Scroll.getValue() / m_ItemHeight) + 1);
                    }
                }
            } else {
                m_SelectedItem = (int)((y - getPosition().y - this._WidgetBordersImpl.m_TopBorder) / m_ItemHeight);

                if (m_SelectedItem > (int)(m_Items.size())-1)
                    m_SelectedItem = -1;
            }

            if ((oldSelectedItem != m_SelectedItem) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ListBoxCallbacks.ItemSelected.value())
            		.isEmpty() == false)) {
                if (m_SelectedItem < 0) {
                    this.getCallbackManager().m_Callback.text = "";
                } else {
                	this.getCallbackManager().m_Callback.text = m_Items.get(m_SelectedItem);
                }
                this.getCallbackManager().m_Callback.value = m_SelectedItem;
                this.getCallbackManager().m_Callback.trigger = ListBoxCallbacks.ItemSelected.value();
                addCallback();
            }
        }
    }

    public void leftMouseReleased(float x, float y) {
        if (m_Scroll != null) {
            int oldValue = m_Scroll.getValue();

            m_Scroll.setPosition(getPosition().x + (m_Size.x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x), getPosition().y + this._WidgetBordersImpl.m_TopBorder);

            m_Scroll.leftMouseReleased(x, y);

            m_Scroll.setPosition(0, 0);

            if (m_Scroll.getValue() == oldValue + 1) {
                m_Scroll.setValue(m_Scroll.getValue() - 1);

                m_Scroll.setValue(m_Scroll.getValue() + m_ItemHeight - (m_Scroll.getValue() % m_ItemHeight));
            } else if (m_Scroll.getValue() == oldValue - 1) {
                m_Scroll.setValue(m_Scroll.getValue() + 1);

                if (m_Scroll.getValue() % m_ItemHeight > 0) {
                    m_Scroll.setValue(m_Scroll.getValue() - (m_Scroll.getValue() % m_ItemHeight));
                } else {
                    m_Scroll.setValue(m_Scroll.getValue() - m_ItemHeight);
                }
            }
        }

        m_MouseDown = false;
    }

    public void mouseMoved(float x, float y) {
        if (m_MouseHover == false)
            mouseEnteredWidget();

        m_MouseHover = true;

        if (m_Scroll != null) {
            m_Scroll.setPosition(getPosition().x + (m_Size.x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x), getPosition().y + this._WidgetBordersImpl.m_TopBorder);

            if ((m_Scroll.m_MouseDown) && 
            	(m_Scroll.m_MouseDownOnThumb)) {
                m_Scroll.mouseMoved(x, y);
            } else {
                if (m_Scroll.mouseOnWidget(x, y)) {
                    m_Scroll.mouseMoved(x, y);
                }
            }

            m_Scroll.setPosition(0, 0);
        }
    }
    
    public void mouseWheelMoved(int delta, int x, int y) {
        if (m_Scroll != null) {
            if (m_Scroll.getLowValue() < m_Scroll.getMaximum()) {
                if (delta < 0) {
                    m_Scroll.setValue(m_Scroll.getValue() + ((int)(-delta) * (m_ItemHeight / 2)) );
                } else {
                    int change = (int)(delta) * (m_ItemHeight / 2);

                    if (change < m_Scroll.getValue()) {
                        m_Scroll.setValue(m_Scroll.getValue() - change );
                    } else {
                        m_Scroll.setValue(0);
                    }
                }
            }
        }
    }

    public void mouseNotOnWidget() {
        if (m_MouseHover)
            mouseLeftWidget();

        m_MouseHover = false;

        if (m_Scroll != null) {
            m_Scroll.m_MouseHover = false;
        }
    }

    public void mouseNoLongerDown() {
        m_MouseDown = false;

        if (m_Scroll != null) {
            m_Scroll.m_MouseDown = false;
        }
    }

    public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            load(value);
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
        } else if ("itemheight".equals(property)) {
            setItemHeight(Integer.parseInt(value.trim()));
        } else if ("maximumitems".equals(property)) {
            setMaximumItems(Integer.parseInt(value.trim()));
        } else if ("borders".equals(property)) {
            Borders borders = new Borders();
            if (Defines.extractBorders(value, borders)) {
                setBorders(borders.left, borders.top, borders.right, borders.bottom);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Borders' property.");
            }
        } else if ("items".equals(property)) {
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
                    this.getCallbackManager().bindCallback(ListBoxCallbacks.ItemSelected.value());
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
        } else if ("backgroundcolor".equals(property)) {
            value[0]  = "(" + 
            	Integer.toString((int)(getBackgroundColor().r)) + "," + 
            	Integer.toString((int)(getBackgroundColor().g)) + "," + 
            	Integer.toString((int)(getBackgroundColor().b)) + "," + 
            	Integer.toString((int)(getBackgroundColor().a)) + ")";
        } else if ("textcolor".equals(property)) {
            value[0]  = "(" + 
            	Integer.toString((int)(getTextColor().r)) + "," + 
            	Integer.toString((int)(getTextColor().g)) + "," + 
            	Integer.toString((int)(getTextColor().b)) + "," + 
            	Integer.toString((int)(getTextColor().a)) + ")";
        } else if ("selectedbackgroundcolor".equals(property)) {
            value[0]  = "(" + 
            	Integer.toString((int)(getSelectedBackgroundColor().r)) + "," + 
            	Integer.toString((int)(getSelectedBackgroundColor().g)) + "," + 
            	Integer.toString((int)(getSelectedBackgroundColor().b)) + "," + 
            	Integer.toString((int)(getSelectedBackgroundColor().a)) + ")";
        } else if ("selectedtextcolor".equals(property)) {
            value[0]  = "(" + 
            	Integer.toString((int)(getSelectedTextColor().r)) + "," + 
            	Integer.toString((int)(getSelectedTextColor().g)) + "," + 
            	Integer.toString((int)(getSelectedTextColor().b)) + "," + 
            	Integer.toString((int)(getSelectedTextColor().a)) + ")";
        } else if ("bordercolor".equals(property)) {
            value[0]  = "(" + 
            	Integer.toString((int)(getBorderColor().r)) + "," + 
            	Integer.toString((int)(getBorderColor().g)) + "," + 
            	Integer.toString((int)(getBorderColor().b)) + "," + 
            	Integer.toString((int)(getBorderColor().a)) + ")";
        } else if ("itemheight".equals(property)) {
            value[0] = Integer.toString(getItemHeight());
        } else if ("maximumitems".equals(property)) {
            value[0] = Integer.toString(getMaximumItems());
        } else if ("borders".equals(property)) {
            value[0] = "(" + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().left) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().top) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().right) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().bottom) + ")";
        } else if ("items".equals(property)) {
            Defines.encodeList(m_Items, value);
        } else if ("selecteditem".equals(property)) {
            value[0] = Integer.toString(getSelectedItemIndex());
        } else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //Widget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(ListBoxCallbacks.ItemSelected.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ListBoxCallbacks.ItemSelected.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ListBoxCallbacks.ItemSelected.value()).get(0) == null)) {
                callbacks.add("ItemSelected");
			}

            Defines.encodeList(callbacks, value);

            if (value[0].length() == 0 || tempValue[0].length() == 0) {
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
        list.add(new Pair<String, String>("BackgroundColor", "color"));
        list.add(new Pair<String, String>("TextColor", "color"));
        list.add(new Pair<String, String>("SelectedBackgroundColor", "color"));
        list.add(new Pair<String, String>("SelectedTextColor", "color"));
        list.add(new Pair<String, String>("BorderColor", "color"));
        list.add(new Pair<String, String>("ItemHeight", "uint"));
        list.add(new Pair<String, String>("MaximumItems", "uint"));
        list.add(new Pair<String, String>("Borders", "borders"));
        list.add(new Pair<String, String>("Items", "string"));
        list.add(new Pair<String, String>("SelectedItem", "int"));
        return list;
    }

    protected void initialize(Container parent) {
        m_Parent = parent;
        setTextFont(m_Parent.getGlobalFont());
    }

    @Override
    public void draw(RenderTarget target, RenderStates states) {
        float scaleViewX = target.getSize().x / target.getView().getSize().x;
        float scaleViewY = target.getSize().y / target.getView().getSize().y;

        Vector2f topLeftPosition = new Vector2f();
        Vector2f bottomRightPosition = new Vector2f();

        Vector2f viewPosition = Vector2f.minus(Vector2f.devide(target.getView().getSize(), 2.f), 
        		target.getView().getCenter());

        if ((m_Scroll != null) && 
        	(m_Scroll.getLowValue() < m_Scroll.getMaximum())) {
            topLeftPosition = states.transform.transformPoint(
            		Vector2f.plus(Vector2f.plus(getPosition(), new Vector2f((float)(this._WidgetBordersImpl.m_LeftBorder), (float)(this._WidgetBordersImpl.m_TopBorder))), viewPosition));
            bottomRightPosition = states.transform.transformPoint(getPosition().x + m_Size.x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x + viewPosition.x, getPosition().y + m_Size.y - this._WidgetBordersImpl.m_BottomBorder + viewPosition.y);
        } else {
            topLeftPosition = states.transform.transformPoint(
            		Vector2f.plus(Vector2f.plus(getPosition(), new Vector2f((float)(this._WidgetBordersImpl.m_LeftBorder), (float)(this._WidgetBordersImpl.m_TopBorder))), viewPosition));
            bottomRightPosition = states.transform.transformPoint(
            		Vector2f.plus(Vector2f.minus(Vector2f.plus(getPosition(), new Vector2f(m_Size)), new Vector2f((float)(this._WidgetBordersImpl.m_RightBorder), (float)(this._WidgetBordersImpl.m_BottomBorder))), viewPosition));
        }

        Transform.multiplyEqual(states.transform, getTransform());

        Transform oldTransform = states.transform;

        {
            RectangleShape border = new RectangleShape(
            	new Vector2f((float)(this._WidgetBordersImpl.m_LeftBorder), (float)(m_Size.y)));
            border.setFillColor(m_BorderColor);
            target.draw(border, states);

            border.setSize(new Vector2f((float)(m_Size.x), (float)(this._WidgetBordersImpl.m_TopBorder)));
            target.draw(border, states);

            border.setPosition((float)(m_Size.x - this._WidgetBordersImpl.m_RightBorder), 0);
            border.setSize(new Vector2f((float)(this._WidgetBordersImpl.m_RightBorder), (float)(m_Size.y)));
            target.draw(border, states);

            border.setPosition(0, (float)(m_Size.y - this._WidgetBordersImpl.m_BottomBorder));
            border.setSize(new Vector2f((float)(m_Size.x), (float)(this._WidgetBordersImpl.m_BottomBorder)));
            target.draw(border, states);
        }

        states.transform.translate((float)(this._WidgetBordersImpl.m_LeftBorder), (float)(this._WidgetBordersImpl.m_TopBorder));

        {
            RectangleShape front = new RectangleShape(
            		new Vector2f((float)(m_Size.x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_RightBorder),
            				(float)(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder)));
            front.setFillColor(m_BackgroundColor);
            target.draw(front, states);
        }

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

        Text text = new Text("", m_TextFont, m_TextSize);

        if ((m_Scroll != null) && 
        	(m_Scroll.getLowValue() < m_Scroll.getMaximum())) {
            Transform storedTransform = states.transform;

            int firstItem = m_Scroll.getValue() / m_ItemHeight;
            int lastItem = (m_Scroll.getValue() + m_Scroll.getLowValue()) / m_ItemHeight;

            if ((m_Scroll.getValue() + m_Scroll.getLowValue()) % m_ItemHeight != 0) {
                ++lastItem;
            }

            GL.glScissor(scissorLeft, target.getSize().y - scissorBottom, scissorRight - scissorLeft, scissorBottom - scissorTop);

            for (int i = firstItem; i < lastItem; ++i) {
                states.transform = storedTransform;

                text.setString(m_Items.get(i));

                FloatRect bounds = text.getGlobalBounds();

                if (m_SelectedItem == (int)(i)) {
                    {
                        states.transform.translate(0, ((float)(i * m_ItemHeight) - m_Scroll.getValue()));

                        RectangleShape back = new RectangleShape(new Vector2f((float)(m_Size.x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_RightBorder), (float)(m_ItemHeight)));
                        back.setFillColor(m_SelectedBackgroundColor);
                        target.draw(back, states);

                        states.transform = storedTransform;
                    }

                    text.setColor(m_SelectedTextColor);
                } else {
                    text.setColor(m_TextColor);
                }
                
                states.transform.translate(2, (float)Math.floor((float)(i * m_ItemHeight) - m_Scroll.getValue() + ((m_ItemHeight - bounds.height) / 2.0f) - bounds.top));

                target.draw(text, states);
            }
        } else {
            GL.glScissor(scissorLeft, target.getSize().y - scissorBottom, scissorRight - scissorLeft, scissorBottom - scissorTop);

            Transform storedTransform = states.transform;

            for (int i = 0; i < m_Items.size(); ++i) {
                states.transform = storedTransform;

                text.setString(m_Items.get(i));

                if (m_SelectedItem == (int)(i)) {
                    {
                        states.transform.translate(0, (float)(i * m_ItemHeight));

                        RectangleShape back = new RectangleShape(new Vector2f((float)(m_Size.x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_RightBorder), (float)(m_ItemHeight)));
                        back.setFillColor(m_SelectedBackgroundColor);
                        target.draw(back, states);

                        states.transform = storedTransform;
                    }

                    text.setColor(m_SelectedTextColor);
                } else {
                    text.setColor(m_TextColor);
                }
                
                FloatRect bounds = text.getGlobalBounds();

                states.transform.translate(2, (float)Math.floor((i * m_ItemHeight) + ((m_ItemHeight - bounds.height) / 2.0f) - bounds.top));

                target.draw(text, states);
            }
        }

        GL.glScissor(scissor[0], scissor[1], scissor[2], scissor[3]);

        if (m_Scroll != null) {
            states.transform = oldTransform;
            states.transform.translate((float)(m_Size.x) - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, (float)(this._WidgetBordersImpl.m_TopBorder));

            target.draw(m_Scroll, states);
        }
    }
}

