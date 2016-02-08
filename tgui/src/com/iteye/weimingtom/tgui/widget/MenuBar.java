package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.Pair;
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

/**
 * 20151013
 * @author Administrator
 *
 */
public class MenuBar extends Widget {
	public static enum MenuBarCallbacks {
        MenuItemClicked(WidgetCallbacks.WidgetCallbacksCount.value() * 1),
        AllMenuBarCallbacks(WidgetCallbacks.WidgetCallbacksCount.value() * 2 - 1), 
        MenuBarCallbacksCount(WidgetCallbacks.WidgetCallbacksCount.value() * 2);
    
        int value;
        
        MenuBarCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
	}
	
    protected class Menu {
        Text text;
        List<Text> menuItems = new ArrayList<Text>();
        int selectedMenuItem;
    }

    protected String m_LoadedConfigFile;

    protected List<Menu> m_Menus = new ArrayList<Menu>();

    protected int m_VisibleMenu;

    protected Font m_TextFont;

    protected Vector2f m_Size;

    protected int m_TextSize;

    protected int m_DistanceToSide;

    protected int m_MinimumSubMenuWidth;

    protected Color m_BackgroundColor;
    protected Color m_TextColor;
    protected Color m_SelectedBackgroundColor;
    protected Color m_SelectedTextColor;
    
	public MenuBar() {
	    m_VisibleMenu = -1;
	    m_TextFont = null;
	    m_TextSize = 0;
	    m_DistanceToSide = 4;
	    m_MinimumSubMenuWidth = 125;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_MenuBar;

        setSize(0, 20);
        changeColors();

        m_Loaded = true;
	}

	public MenuBar(MenuBar copy) {
		//FIXME:
	}
	
    public MenuBar cloneObj() {
        return new MenuBar(this);
    }

    public boolean load(String configFileFilename) {
        m_LoadedConfigFile = configFileFilename;

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("MenuBar", properties, values)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to parse " + configFileFilename + ".");
            return false;
        }

        configFile.close();

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
            } else if ("distancetoside".equals(property)) {
                setDistanceToSide((int)(Integer.parseInt(value.trim())));
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section MenuBar in " + configFileFilename + ".");
            }
        }

        return false;
    }

    public String getLoadedConfigFile() {
        return m_LoadedConfigFile;
    }

    public void setSize(float width, float height) {
        m_Size.x = width;
        m_Size.y = height;

        if (m_Size.y < 10) {
            m_Size.y = 10;
        }

        setTextSize((int)(height * 0.85f));
    }

    public Vector2f getSize() {
        return m_Size;
    }

    public void addMenu(String text) {
        Menu menu = new Menu();

        menu.selectedMenuItem = -1;

        menu.text.setFont(m_TextFont);
        menu.text.setString(text);
        menu.text.setColor(m_TextColor);
        menu.text.setCharacterSize(m_TextSize);
        menu.text.setCharacterSize((int)(menu.text.getCharacterSize() - menu.text.getLocalBounds().top));

        m_Menus.add(menu);
    }

    public boolean addMenuItem(String menu, String text) {
        for (int i = 0; i < m_Menus.size(); ++i) {
            if (m_Menus.get(i).text.getString().equals(menu)) {
                Text menuItem = new Text();
                menuItem.setFont(m_TextFont);
                menuItem.setString(text);
                menuItem.setColor(m_TextColor);
                menuItem.setCharacterSize(m_TextSize);
                menuItem.setCharacterSize((int)(menuItem.getCharacterSize() - menuItem.getLocalBounds().top));

                m_Menus.get(i).menuItems.add(menuItem);
                return true;
            }
        }

        return false;
    }

    public boolean removeMenu(String menu) {
        for (int i = 0; i < m_Menus.size(); ++i) {
            if (m_Menus.get(i).text.getString() == menu) {
                m_Menus.remove(i);

                if (m_VisibleMenu == (int)(i)) {
                    m_VisibleMenu = -1;
                }

                return true;
            }
        }

        return false;
    }

    public void removeAllMenus() {
        m_Menus.clear();
    }

    public boolean removeMenuItem(String menu, String menuItem) {
        for (int i = 0; i < m_Menus.size(); ++i) {
            if (m_Menus.get(i).text.getString().equals(menu)) {
                for (int j = 0; j < m_Menus.get(i).menuItems.size(); ++j) {
                    if (m_Menus.get(i).menuItems.get(j).getString() == menuItem) {
                        m_Menus.get(i).menuItems.remove(j);

                        if (m_Menus.get(i).selectedMenuItem == (int)(j)) {
                            m_Menus.get(i).selectedMenuItem = -1;
                        }

                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void changeColors() {
    	changeColors(Color.White, Color.Black, 
    		new Color(50, 100, 200), Color.White);
    }
    
    public void changeColors(Color backgroundColor,
    		Color textColor,
    		Color selectedBackgroundColor,
    		Color selectedTextColor) {
        m_BackgroundColor = backgroundColor;
        m_TextColor = textColor;
        m_SelectedBackgroundColor = selectedBackgroundColor;
        m_SelectedTextColor = selectedTextColor;

        for (int i = 0; i < m_Menus.size(); ++i) {
            for (int j = 0; j < m_Menus.get(i).menuItems.size(); ++j) {
                if (m_Menus.get(i).selectedMenuItem == (int)(j)) {
                    m_Menus.get(i).menuItems.get(j).setColor(selectedTextColor);
                } else {
                    m_Menus.get(i).menuItems.get(j).setColor(textColor);
                }
            }

            m_Menus.get(i).text.setColor(textColor);
        }
    }

    public void setBackgroundColor(Color backgroundColor) {
        m_BackgroundColor = backgroundColor;
    }

    public void setTextColor(Color textColor) {
        m_TextColor = textColor;

        for (int i = 0; i < m_Menus.size(); ++i) {
            for (int j = 0; j < m_Menus.get(i).menuItems.size(); ++j) {
                if (m_Menus.get(i).selectedMenuItem != (int)(j)) {
                    m_Menus.get(i).menuItems.get(j).setColor(textColor);
                }
            }

            m_Menus.get(i).text.setColor(textColor);
        }
    }

    public void setSelectedBackgroundColor(Color selectedBackgroundColor) {
        m_SelectedBackgroundColor = selectedBackgroundColor;
    }

    public void setSelectedTextColor(Color selectedTextColor) {
        m_SelectedTextColor = selectedTextColor;

        if (m_VisibleMenu != -1) {
            if (m_Menus.get(m_VisibleMenu).selectedMenuItem != -1) {
                m_Menus.get(m_VisibleMenu).menuItems
                	.get(m_Menus.get(m_VisibleMenu).selectedMenuItem)
                	.setColor(selectedTextColor);
            }
        }
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

    public void setTextFont(Font font){
        m_TextFont = font;

        for (int i = 0; i < m_Menus.size(); ++i) {
            for (int j = 0; j < m_Menus.get(i).menuItems.size(); ++j) {
                m_Menus.get(i).menuItems.get(j).setFont(font);
            }

            m_Menus.get(i).text.setFont(font);
        }

        setTextSize(m_TextSize);
    }

    public Font getTextFont() {
        return m_TextFont;
    }

    public void setTextSize(int size) {
        m_TextSize = size;

        for (int i = 0; i < m_Menus.size(); ++i) {
            for (int j = 0; j < m_Menus.get(i).menuItems.size(); ++j) {
                m_Menus.get(i).menuItems.get(j).setCharacterSize(m_TextSize);
                m_Menus.get(i).menuItems.get(j).setCharacterSize(
                	(int)(m_Menus.get(i).menuItems.get(j).getCharacterSize() - 
                		m_Menus.get(i).menuItems.get(j).getLocalBounds().top));
            }

            m_Menus.get(i).text.setCharacterSize(m_TextSize);
            m_Menus.get(i).text.setCharacterSize((int)(m_Menus.get(i).text.getCharacterSize() - 
            	m_Menus.get(i).text.getLocalBounds().top));
        }
    }

    public int getTextSize() {
        return m_TextSize;
    }

    public void setDistanceToSide(int distanceToSide) {
        m_DistanceToSide = distanceToSide;    	
    }

    public int getDistanceToSide() {
        return m_DistanceToSide;
    }

    public void setMinimumSubMenuWidth(int minimumWidth) {
        m_MinimumSubMenuWidth = minimumWidth;
    }

    public int getMinimumSubMenuWidth() {
        return m_MinimumSubMenuWidth;
    }

    public boolean mouseOnWidget(float x, float y) {
        if (m_Loaded) {
            if (getTransform().transformRect(
            		new FloatRect(0, 0, m_Size.x, m_Size.y))
            		.contains(x, y)) {
                return true;
            } else {
                if (m_VisibleMenu != -1) {
                    float left = 0;
                    for (int i = 0; i < m_VisibleMenu; ++i) {
                        left += m_Menus.get(i).text.getLocalBounds().width + (2 * m_DistanceToSide);
                    }
                    
                    // Find out what the width of the menu should be
                    float width = 0;
                    for (int j = 0; j < m_Menus.get(m_VisibleMenu).menuItems.size(); ++j) {
                        if (width < m_Menus.get(m_VisibleMenu).menuItems.get(j).getLocalBounds().width + (3 * m_DistanceToSide))
                            width = m_Menus.get(m_VisibleMenu).menuItems.get(j).getLocalBounds().width + (3 * m_DistanceToSide);
                    }

                    if (width < m_MinimumSubMenuWidth) {
                        width = (float)(m_MinimumSubMenuWidth);
                    }
                    
                    if (getTransform().transformRect(
                    		new FloatRect(left, m_Size.y, width, 
                    			m_Size.y * m_Menus.get(m_VisibleMenu).menuItems.size()))
                    		.contains(x, y)) {
                        return true;
                    }
                }
            }
        }

        if (m_MouseHover) {
            mouseLeftWidget();
        }

        m_MouseHover = false;
        return false;
    }

    public void leftMousePressed(float x, float y) {
        if (y <= m_Size.y + getPosition().y) {
            float menuWidth = 0;
            for (int i = 0; i < m_Menus.size(); ++i) {
                menuWidth += m_Menus.get(i).text.getLocalBounds().width + (2 * m_DistanceToSide);
                if (x < menuWidth) {
                    if (m_VisibleMenu == (int)(i)) {
                        if (m_Menus.get(m_VisibleMenu).selectedMenuItem != -1) {
                            m_Menus.get(m_VisibleMenu).menuItems
                            	.get(m_Menus.get(m_VisibleMenu).selectedMenuItem)
                            	.setColor(m_TextColor);
                            m_Menus.get(m_VisibleMenu).selectedMenuItem = -1;
                        }

                        m_VisibleMenu = -1;
                    } else if (!m_Menus.get(i).menuItems.isEmpty()) {
                        m_VisibleMenu = (int)(i);
                    }

                    break;
                }
            }
        }

        m_MouseDown = true;
    }

    public void leftMouseReleased(float x, float y) {
        if (m_MouseDown) {
            if (y > m_Size.y + getPosition().y) {
                int selectedMenuItem = (int)((y - m_Size.y - getPosition().y) / m_Size.y);

                if (selectedMenuItem < m_Menus.get(m_VisibleMenu).menuItems.size()) {
                    if (this.getCallbackManager().m_CallbackFunctions
                    		.get(MenuBarCallbacks.MenuItemClicked.value()).isEmpty() == false) {
                    	this.getCallbackManager().m_Callback.trigger = MenuBarCallbacks.MenuItemClicked.value();
                        this.getCallbackManager().m_Callback.text = m_Menus.get(m_VisibleMenu).menuItems.get(selectedMenuItem).getString();
                        this.getCallbackManager().m_Callback.index = m_VisibleMenu;
                        addCallback();
                    }

                    if (m_VisibleMenu != -1) {
                        if (m_Menus.get(m_VisibleMenu).selectedMenuItem != -1) {
                            m_Menus.get(m_VisibleMenu).menuItems
                            	.get(m_Menus.get(m_VisibleMenu).selectedMenuItem)
                            	.setColor(m_TextColor);
                            m_Menus.get(m_VisibleMenu).selectedMenuItem = -1;
                        }

                        m_VisibleMenu = -1;
                    }
                }
            }
        }
    }

    public void mouseMoved(float x, float y) {
        if (m_MouseHover == false) {
            mouseEnteredWidget();
        }

        m_MouseHover = true;

        if (y <= m_Size.y + getPosition().y) {
            if (m_VisibleMenu != -1) {
                float menuWidth = 0;
                for (int i = 0; i < m_Menus.size(); ++i) {
                    menuWidth += m_Menus.get(i).text.getLocalBounds().width + (2 * m_DistanceToSide);
                    if (x < menuWidth) {
                        if (m_VisibleMenu == (int)(i)) {
                            if (m_Menus.get(m_VisibleMenu).selectedMenuItem != -1) {
                                m_Menus.get(m_VisibleMenu).menuItems
                                	.get(m_Menus.get(m_VisibleMenu).selectedMenuItem)
                                	.setColor(m_TextColor);
                                m_Menus.get(m_VisibleMenu).selectedMenuItem = -1;
                            }
                        } else {
                            if (m_VisibleMenu != -1) {
                                if (m_Menus.get(m_VisibleMenu).selectedMenuItem != -1) {
                                    m_Menus.get(m_VisibleMenu).menuItems
                                    	.get(m_Menus.get(m_VisibleMenu).selectedMenuItem)
                                    	.setColor(m_TextColor);
                                    m_Menus.get(m_VisibleMenu).selectedMenuItem = -1;
                                }

                                m_VisibleMenu = -1;
                            }

                            if (!m_Menus.get(i).menuItems.isEmpty()) {
                                m_VisibleMenu = (int)(i);
                            }
                        }
                        break;
                    }
                }
            }
        } else {
            int selectedMenuItem = (int)((y - m_Size.y - getPosition().y) / m_Size.y);

            if (selectedMenuItem != m_Menus.get(m_VisibleMenu).selectedMenuItem) {
                if (m_Menus.get(m_VisibleMenu).selectedMenuItem != -1) {
                    m_Menus.get(m_VisibleMenu).menuItems
                    	.get(m_Menus.get(m_VisibleMenu).selectedMenuItem)
                    	.setColor(m_TextColor);
                }
                
                m_Menus.get(m_VisibleMenu).selectedMenuItem = selectedMenuItem;
                m_Menus.get(m_VisibleMenu).menuItems
                	.get(m_Menus.get(m_VisibleMenu).selectedMenuItem)
                	.setColor(m_SelectedTextColor);
            }
        }
    }

    public void mouseNoLongerDown() {
        if (m_VisibleMenu != -1) {
            if (m_Menus.get(m_VisibleMenu).selectedMenuItem != -1) {
                m_Menus.get(m_VisibleMenu).menuItems
                	.get(m_Menus.get(m_VisibleMenu).selectedMenuItem)
                	.setColor(m_TextColor);
                m_Menus.get(m_VisibleMenu).selectedMenuItem = -1;
            }

            m_VisibleMenu = -1;
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
        } else if ("textsize".equals(property)) {
            setTextSize(Integer.parseInt(value.trim()));
        } else if ("distancetoside".equals(property)) {
            setDistanceToSide(Integer.parseInt(value.trim()));
        } else if ("minimumsubmenuwidth".equals(property)) {
            setMinimumSubMenuWidth(Integer.parseInt(value.trim()));
        } else if ("menus".equals(property)) {
            removeAllMenus();

            List<String> menus = new ArrayList<String>();
            Defines.decodeList(value, menus);

            for (String menuIt : menus) {
                int commaPos = menuIt.indexOf(',');
                if (commaPos != -1) {
                    List<String> menuItems = new ArrayList<String>();
                    String menu = menuIt.substring(0, commaPos);
                    menuIt = menuIt.substring(commaPos + 1); //FIXME:

                    addMenu(menu);
                    Defines.decodeList(menuIt, menuItems);

                    for (String menuItemIt : menuItems) {
                        addMenuItem(menu, menuItemIt);
                    }
                } else {
                    addMenu(menuIt);
                }
            }
        } else if ("callback".equals(property)) {
            //Widget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("MenuItemClicked".equals(it)) || 
                	("menuitemclicked".equals(it))) {
                    this.getCallbackManager().bindCallback(
                    	MenuBarCallbacks.MenuItemClicked.value());
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
            value[0] = "(" + 
	            	Integer.toString((int)(getBackgroundColor().r)) + "," + 
	            	Integer.toString((int)(getBackgroundColor().g)) + "," + 
	            	Integer.toString((int)(getBackgroundColor().b)) + "," + 
	            	Integer.toString((int)(getBackgroundColor().a)) + ")";
        } else if ("textcolor".equals(property)) {
            value[0] = "(" + 
	            	Integer.toString((int)(getTextColor().r)) + "," + 
	            	Integer.toString((int)(getTextColor().g)) + "," + 
	            	Integer.toString((int)(getTextColor().b)) + "," + 
	            	Integer.toString((int)(getTextColor().a)) + ")";
        } else if ("selectedbackgroundcolor".equals(property)) {
            value[0] = "(" + 
            		Integer.toString((int)(getSelectedBackgroundColor().r)) + "," + 
            		Integer.toString((int)(getSelectedBackgroundColor().g)) + "," + 
            		Integer.toString((int)(getSelectedBackgroundColor().b)) + "," + 
            		Integer.toString((int)(getSelectedBackgroundColor().a)) + ")";
        } else if ("selectedtextcolor".equals(property)) {
            value[0] = "(" + 
            		Integer.toString((int)(getSelectedTextColor().r)) + "," + 
            		Integer.toString((int)(getSelectedTextColor().g)) + "," + 
            		Integer.toString((int)(getSelectedTextColor().b)) + "," + 
            		Integer.toString((int)(getSelectedTextColor().a)) + ")";
        } else if ("textsize".equals(property)) {
            value[0] = Integer.toString(getTextSize());
        } else if ("distancetoside".equals(property)) {
            value[0] = Integer.toString(getDistanceToSide());
        } else if ("minimumsubmenuwidth".equals(property)) {
            value[0] = Integer.toString(getMinimumSubMenuWidth());
        } else if ("menus".equals(property)) {
            List<String> menusList = new ArrayList<String>();

            for (Menu menuIt : m_Menus) {
                List<String> menuItemsList = new ArrayList<String>();
                menuItemsList.add(menuIt.text.getString());

                for (Text menuItemIt : menuIt.menuItems) {
                    menuItemsList.add(menuItemIt.getString());
                }
                
                String[] menuItemsString = new String[1];
                Defines.encodeList(menuItemsList, menuItemsString);

                menusList.add(menuItemsString[0]);
            }

            Defines.encodeList(menusList, value);
        } else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //Widget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(MenuBarCallbacks.MenuItemClicked.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(MenuBarCallbacks.MenuItemClicked.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(MenuBarCallbacks.MenuItemClicked.value()).get(0) == null)) {
                callbacks.add("MenuItemClicked");
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
        list.add(new Pair<String, String>("BackgroundColor", "color"));
        list.add(new Pair<String, String>("TextColor", "color"));
        list.add(new Pair<String, String>("SelectedBackgroundColor", "color"));
        list.add(new Pair<String, String>("SelectedTextColor", "color"));
        list.add(new Pair<String, String>("TextSize", "uint"));
        list.add(new Pair<String, String>("DistanceToSide", "uint"));
        list.add(new Pair<String, String>("MinimumSubMenuWidth", "uint"));
        list.add(new Pair<String, String>("menus", "string"));
        return list;
    }

    protected void initialize(Container parent) {
        m_Parent = parent;
        setTextFont(m_Parent.getGlobalFont());
        m_Size.x = m_Parent.getSize().x;
    }

    public void draw(RenderTarget target, RenderStates states) {
        Transform.multiplyEqual(states.transform, getTransform());

        RectangleShape background = new RectangleShape(m_Size);
        background.setFillColor(m_BackgroundColor);
        target.draw(background, states);

        for (int i = 0; i < m_Menus.size(); ++i) {
            states.transform.translate((float)(m_DistanceToSide), 0);
            target.draw(m_Menus.get(i).text, states);

            if (m_VisibleMenu == (int)(i)) {
                states.transform.translate(-(float)(m_DistanceToSide), m_Size.y);

                float menuWidth = 0;
                for (int j = 0; j < m_Menus.get(i).menuItems.size(); ++j) {
                    if (menuWidth < m_Menus.get(i).menuItems.get(j).getLocalBounds().width + (3 * m_DistanceToSide)) {
                        menuWidth = m_Menus.get(i).menuItems.get(j).getLocalBounds().width + (3 * m_DistanceToSide);
                    }
                }

                if (menuWidth < m_MinimumSubMenuWidth) {
                    menuWidth = (float)(m_MinimumSubMenuWidth);
                }
                
                background = new RectangleShape(
                	new Vector2f(menuWidth, 
                		m_Size.y * m_Menus.get(i).menuItems.size()));
                background.setFillColor(m_BackgroundColor);
                target.draw(background, states);

                if (m_Menus.get(i).selectedMenuItem != -1) {
                    states.transform.translate(0, m_Menus.get(i).selectedMenuItem * m_Size.y);
                    background = new RectangleShape(new Vector2f(menuWidth, m_Size.y));
                    background.setFillColor(m_SelectedBackgroundColor);
                    target.draw(background, states);
                    states.transform.translate(0, m_Menus.get(i).selectedMenuItem * -m_Size.y);
                }

                states.transform.translate(2.0f * m_DistanceToSide, 0);

                for (int j = 0; j < m_Menus.get(i).menuItems.size(); ++j) {
                    target.draw(m_Menus.get(i).menuItems.get(j), states);
                    states.transform.translate(0, m_Size.y);
                }

                states.transform.translate(m_Menus.get(i).text.getLocalBounds().width, 
                	-m_Size.y * (m_Menus.get(i).menuItems.size() + 1));
            } else {
                states.transform.translate(m_Menus.get(i).text.getLocalBounds().width + m_DistanceToSide, 0);
            }
        }
    }
}
