package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.Borders;
import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.SharedWidgetPtr;
import com.iteye.weimingtom.tgui.WidgetBorders;
import com.iteye.weimingtom.tgui.WidgetBordersImpl;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.FloatRect;
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.RectangleShape;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20150903
 * @author Administrator
 *
 */
public class ChatBox extends Widget implements WidgetBorders{
	public WidgetBordersImpl _WidgetBordersImpl = new WidgetBordersImpl();
    public static enum ChatBoxCallbacks {
        AllChatBoxCallbacks(WidgetCallbacks.WidgetCallbacksCount.value() - 1),
        ChatBoxCallbacksCount(WidgetCallbacks.WidgetCallbacksCount.value());
        
        int value;
        
        ChatBoxCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }
	
    protected String m_LoadedConfigFile;
    protected int m_TextSize;
    protected Color m_BorderColor;
    protected int m_MaxLines;
    protected float m_FullTextHeight;
    protected Panel m_Panel;
    protected Scrollbar m_Scroll;
    
    public ChatBox() {
        m_TextSize = 15;
        m_BorderColor = Color.Black; //FIXME:
        m_MaxLines = 0;
        m_FullTextHeight = 0;
        m_Scroll = null;
        
        this.getCallbackManager().m_Callback.widgetType 
        	= WidgetTypes.Type_ChatBox;
        m_DraggableWidget = true;
        
        m_Panel = new Panel();
        m_Panel.setSize(360, 200);
        m_Panel.setBackgroundColor(Color.White);

        m_Loaded = true;
    }

    public ChatBox(ChatBox copy) {
        super(copy);
        _WidgetBordersImpl = new WidgetBordersImpl(copy._WidgetBordersImpl);
        m_LoadedConfigFile = copy.m_LoadedConfigFile;
        m_TextSize = copy.m_TextSize;
        m_BorderColor = copy.m_BorderColor;
        m_MaxLines = copy.m_MaxLines;
        m_FullTextHeight = copy.m_FullTextHeight;
        
        m_Panel = new Panel(copy.m_Panel);

        if (copy.m_Scroll != null) {
            m_Scroll = new Scrollbar(copy.m_Scroll);
        } else {
            m_Scroll = null;
        }
    }

    public void destroy() {
    	super.destroy();
        m_Panel.destroy();
        m_Panel = null;

        if (m_Scroll != null) {
            m_Scroll.destroy();
            m_Scroll = null;
        }
    }

    public ChatBox assign(ChatBox right) {
        if (this != right) {
            if (m_Scroll != null) {
                m_Scroll.destroy();
                m_Scroll = null;
            }

            ChatBox temp = new ChatBox(right);
            super.assign(right);
            //this->WidgetBorders::operator=(right);

            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_TextSize = temp.m_TextSize;
            m_BorderColor = temp.m_BorderColor;
            m_MaxLines = temp.m_MaxLines;
            m_FullTextHeight = temp.m_FullTextHeight;
            m_Panel = temp.m_Panel;
            m_Scroll = temp.m_Scroll;
        }
        return this;
    }

    public ChatBox cloneObj() {
    	return new ChatBox(this);
    }

    public boolean load(String configFileFilename) {
        m_LoadedConfigFile = configFileFilename;

        m_Loaded = false;

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
        if (!configFile.read("ChatBox", properties, values)) {
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
                setBackgroundColor(configFile.readColor(value));
            } else if ("bordercolor".equals(property)) {
                setBorderColor(configFile.readColor(value));
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
                if (m_Scroll.load(configFileFolder + value.substring(1, value.length() - 2)) == false) {
                    m_Scroll.destroy();
                    m_Scroll = null;

                    return false;
                } else {
                    m_Scroll.setVerticalScroll(true);
                    m_Scroll.setLowValue((int)(m_Panel.getSize().y - _WidgetBordersImpl.m_TopBorder - _WidgetBordersImpl.m_BottomBorder));
                    m_Scroll.setSize(m_Scroll.getSize().x, m_Panel.getSize().y - _WidgetBordersImpl.m_TopBorder - _WidgetBordersImpl.m_BottomBorder);
                    m_Scroll.setMaximum((int)(m_FullTextHeight));
                }
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section ChatBox in " + configFileFilename + ".");
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

        if (width < 0) {
        	width = -width;
        }
        if (height < 0) {
        	height = -height;
        }

        if (m_Scroll == null) {
            width = (float)Defines.TGUI_MAXIMUM(50 + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder, width);
        } else {
            width = (float)Defines.TGUI_MAXIMUM(50 + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder + m_Scroll.getSize().x, width);
        }
        
        float oldHeight = m_Panel.getSize().y;

        m_Panel.setSize(width, height);

        if (m_Scroll != null) {
            m_Scroll.setLowValue((int)(m_Panel.getSize().y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder));
            m_Scroll.setSize(m_Scroll.getSize().x, m_Panel.getSize().y - _WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
        }

        float heightDiff = m_Panel.getSize().y - oldHeight;

        List<SharedWidgetPtr<? extends Widget>> labels = m_Panel.getWidgets();
        for (SharedWidgetPtr<? extends Widget> it : labels) {
            it.get().setPosition(it.get().getPosition().x, it.get().getPosition().y + heightDiff);    	
        }
    }
    
    public Vector2f getSize() {
    	return m_Panel.getSize();
    }

    public void addLine(String text, Color color) {
        List<SharedWidgetPtr<? extends Widget>> widgets = m_Panel.getWidgets();

        if ((m_MaxLines > 0) && (m_MaxLines < widgets.size() + 1)) {
            removeLine(0);
        }

        SharedWidgetPtr<Label> label = 
        	new SharedWidgetPtr<Label>(new Label(), m_Panel);
        label.get().setTextColor(color);
        label.get().setTextSize(m_TextSize);

        SharedWidgetPtr<Label> tempLine =
            	new SharedWidgetPtr<Label>(new Label());
        tempLine.get().setTextSize(m_TextSize);
        tempLine.get().setTextFont(label.get().getTextFont());

        float width;
        if (m_Scroll == null) {
            width = m_Panel.getSize().x - _WidgetBordersImpl.m_LeftBorder - _WidgetBordersImpl.m_RightBorder;
        } else {
            width = m_Panel.getSize().x - _WidgetBordersImpl.m_LeftBorder - _WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x;
        }
        if (width < 0) {
            width = 0;
        }

        int pos = 0;
        int size = 0;
        while (pos + size < text.length()) {
            tempLine.get().setText(text.substring(pos, ++size));

            if (tempLine.get().getSize().x + 4.0f > width) {
                label.get().setText(label.get().getText() + text.substring(pos, size - 1) + "\n");

                pos = pos + size - 1;
                size = 0;
            }
        }
        label.get().setText(label.get().getText() + tempLine.get().getText());

        m_FullTextHeight += label.get().getSize().y + (label.get().getTextFont().getLineSpacing(label.get().getTextSize()) - label.get().getTextSize());

        if (m_Scroll != null) {
            m_Scroll.setMaximum((int)(m_FullTextHeight));

            if (m_Scroll.getMaximum() > m_Scroll.getLowValue()) {
                m_Scroll.setValue(m_Scroll.getMaximum() - m_Scroll.getLowValue());
            }
        }

        updateDisplayedText();    	
    }

    public String getLine(int lineIndex) {
        if (lineIndex < m_Panel.getWidgets().size()) {
        	//FIXME:
            return ((SharedWidgetPtr<Label>)m_Panel.getWidgets().get(lineIndex)).get().getText();
        } else {
            return "";
        }
    }

    public boolean removeLine(int lineIndex) {
        if (lineIndex < m_Panel.getWidgets().size()) {
        	SharedWidgetPtr<Label> label = (SharedWidgetPtr<Label>)m_Panel.getWidgets().get(lineIndex);
            m_FullTextHeight -= label.get().getSize().y + (label.get().getTextFont().getLineSpacing(label.get().getTextSize()) - label.get().getTextSize());
            m_Panel.remove(label);

            if (m_Scroll != null) {
                m_Scroll.setMaximum((int)(m_FullTextHeight));
            }
            updateDisplayedText();
            return true;
        } else {
            return false;
        }
    }

    public void removeAllLines() {
        m_Panel.removeAllWidgets();

        m_FullTextHeight = 0;

        if (m_Scroll != null) {
            m_Scroll.setMaximum((int)(m_FullTextHeight));
        }
        updateDisplayedText();
    }
    		
    public int getLineAmount() {
    	return m_Panel.getWidgets().size();
    }

    public void setLineLimit(int maxLines) {
        m_MaxLines = maxLines;

        if ((m_MaxLines > 0) && (m_MaxLines < m_Panel.getWidgets().size())) {
            while (m_MaxLines < m_Panel.getWidgets().size()) {
            	SharedWidgetPtr<Label> label = (SharedWidgetPtr<Label>)m_Panel.getWidgets().get(0);
                m_FullTextHeight -= label.get().getSize().y + (label.get().getTextFont().getLineSpacing(label.get().getTextSize()) - label.get().getTextSize());
                m_Panel.remove(label);
            }

            if (m_Scroll != null) {
                m_Scroll.setMaximum((int)(m_FullTextHeight));
            }
            updateDisplayedText();
        }    	
    }

    public void setTextFont(Font font) {
        m_Panel.setGlobalFont(font);

        List<SharedWidgetPtr<? extends Widget>> labels = m_Panel.getWidgets();
        for (SharedWidgetPtr<? extends Widget> it : labels) {
        	((SharedWidgetPtr<Label>)it).get().setTextFont(font);   	
        }
    }

    public Font getTextFont() {
    	return m_Panel.getGlobalFont();
    }

    public void setTextSize(int size) {
        m_TextSize = size;

        if (m_TextSize < 8) {
            m_TextSize = 8;
        }
    }

    public int getTextSize() {
    	return m_TextSize;
    }

    public void setBorders(int leftBorder,
                           int topBorder,
                           int rightBorder,
                           int bottomBorder) {
        // Reposition the labels
    	List<SharedWidgetPtr<? extends Widget>> labels = m_Panel.getWidgets();
        for (SharedWidgetPtr<? extends Widget> it : labels) {
            it.get().setPosition(
            	it.get().getPosition().x + leftBorder - _WidgetBordersImpl.m_LeftBorder, 
            	it.get().getPosition().y);
        }
        // Set the new border size
        _WidgetBordersImpl.m_LeftBorder = leftBorder;
        _WidgetBordersImpl.m_TopBorder = topBorder;
        _WidgetBordersImpl.m_RightBorder = rightBorder;
        _WidgetBordersImpl.m_BottomBorder = bottomBorder;

        float width = m_Panel.getSize().x;
        if (width < (50.f + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder)) {
            width = 50.f + _WidgetBordersImpl.m_LeftBorder + _WidgetBordersImpl.m_RightBorder;
        }

        m_Panel.setSize(width, m_Panel.getSize().y);

        if (m_Scroll != null) {
            m_Scroll.setLowValue((int)(m_Panel.getSize().y) - _WidgetBordersImpl.m_TopBorder - _WidgetBordersImpl.m_BottomBorder);
            m_Scroll.setSize(m_Scroll.getSize().x, m_Panel.getSize().y - _WidgetBordersImpl.m_TopBorder - _WidgetBordersImpl.m_BottomBorder);
        }    	
    }

    public void setBackgroundColor(Color backgroundColor) {
    	m_Panel.setBackgroundColor(backgroundColor);
    }

    public void setBorderColor(Color borderColor) {
    	m_BorderColor = borderColor;
    }

    public Color getBackgroundColor() {
    	return m_Panel.getBackgroundColor();
    }

    public Color getBorderColor() {
    	return m_BorderColor;
    }

    public boolean setScrollbar(String scrollbarConfigFileFilename) {
        if (scrollbarConfigFileFilename == null || 
        	scrollbarConfigFileFilename.length() == 0) {
            return false;
        }

        if (m_Scroll != null) {
            m_Scroll.destroy();
            m_Scroll = null;
        }

        m_Scroll = new Scrollbar(); 
        if(m_Scroll.load(scrollbarConfigFileFilename) == false) {
            m_Scroll.destroy();
            m_Scroll = null;

            return false;
        } else {
            m_Scroll.setVerticalScroll(true);
            m_Scroll.setSize(m_Scroll.getSize().x, m_Panel.getSize().y - _WidgetBordersImpl.m_TopBorder - _WidgetBordersImpl.m_BottomBorder);
            m_Scroll.setLowValue((int)(m_Panel.getSize().y) - _WidgetBordersImpl.m_TopBorder - _WidgetBordersImpl.m_BottomBorder);
            m_Scroll.setMaximum((int)(m_FullTextHeight));
            return true;
        }
    }

    public void removeScrollbar() {
        m_Scroll.destroy();
        m_Scroll = null;
    }

    public void setTransparency(byte transparency) {
        //Widget::
        super.setTransparency(transparency);

        m_Panel.setTransparency(transparency);

        if (m_Scroll != null) {
            m_Scroll.setTransparency(transparency);
        }
    }

    public boolean mouseOnWidget(float x, float y) {
        Vector2f position = getPosition();

        if (m_Scroll != null) {
            m_Scroll.setPosition(position.x + m_Panel.getSize().x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, 
            	position.y + this._WidgetBordersImpl.m_TopBorder);

            m_Scroll.mouseOnWidget(x, y);

            m_Scroll.setPosition(0, 0);
        }

        if (getTransform().transformRect(
        		new FloatRect(
        			(float)(this._WidgetBordersImpl.m_LeftBorder),
        			(float)(this._WidgetBordersImpl.m_TopBorder),
        			m_Panel.getSize().x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_RightBorder,
                    m_Panel.getSize().y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder)).contains(x, y)) {
            return true;
    	} else {
            if (m_MouseHover) {
                mouseLeftWidget();
            }
            m_MouseHover = false;
            return false;
        }
    }
    
    public void leftMousePressed(float x, float y) {
        if (m_Loaded == false) {
            return;
        }

        m_MouseDown = true;

        if (m_Scroll != null) {
            int oldValue = m_Scroll.getValue();

            m_Scroll.setPosition(
            	getPosition().x + m_Panel.getSize().x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, 
            	getPosition().y + this._WidgetBordersImpl.m_TopBorder);

            if (m_Scroll.mouseOnWidget(x, y)) {
                m_Scroll.leftMousePressed(x, y);
            }

            m_Scroll.setPosition(0, 0);

            if (oldValue != m_Scroll.getValue()) {
                updateDisplayedText();
            }
        }
    }
    
    public void leftMouseReleased(float x, float y) {
        if (m_Scroll != null) {
            if (m_Scroll.m_MouseDown == true) {
                int oldValue = m_Scroll.getValue();

                m_Scroll.setPosition(
                	getPosition().x + m_Panel.getSize().x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, 
                	getPosition().y + this._WidgetBordersImpl.m_TopBorder);

                m_Scroll.leftMouseReleased(x, y);

                m_Scroll.setPosition(0, 0);

                if (oldValue != m_Scroll.getValue()) {
                    if (m_Scroll.getValue() == oldValue + 1) {
                        m_Scroll.setValue(m_Scroll.getValue() - 1);

                        m_Scroll.setValue(m_Scroll.getValue() + m_TextSize - (m_Scroll.getValue() % m_TextSize));
                    } else if (m_Scroll.getValue() == oldValue - 1) {
                        m_Scroll.setValue(m_Scroll.getValue() + 1);

                        if (m_Scroll.getValue() % m_TextSize > 0) {
                            m_Scroll.setValue(m_Scroll.getValue() - (m_Scroll.getValue() % m_TextSize));
                        } else {
                            m_Scroll.setValue(m_Scroll.getValue() - m_TextSize);
                        }
                    }

                    updateDisplayedText();
                }
            }
        }
        m_MouseDown = false;    	
    }

    public void mouseMoved(float x, float y) {
        if (m_MouseHover == false) {
            mouseEnteredWidget();
        }

        m_MouseHover = true;

        if (m_Scroll != null) {
            m_Scroll.setPosition(
            	getPosition().x + m_Panel.getSize().x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, 
            	getPosition().y + this._WidgetBordersImpl.m_TopBorder);

            if ((m_Scroll.m_MouseDown) && (m_Scroll.m_MouseDownOnThumb)) {
                int oldValue = m_Scroll.getValue();

                m_Scroll.mouseMoved(x, y);

                if (oldValue != m_Scroll.getValue()) {
                    updateDisplayedText();
                }
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
                    m_Scroll.setValue(m_Scroll.getValue() + ((int)(-delta) * m_TextSize));
                } else {
                    int change = (int)(delta) * m_TextSize;

                    if (change < m_Scroll.getValue()) {
                        m_Scroll.setValue(m_Scroll.getValue() - change);
                	} else {
                        m_Scroll.setValue(0);
                	}
                }

                updateDisplayedText();
            }
        }    	
    }

    public void mouseNotOnWidget() {
        if (m_MouseHover) {
            mouseLeftWidget();
        }
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
        } else if ("textsize".equals(property)) {
        	try {
        		setTextSize(Integer.parseInt(value.trim()));
        	} catch (NumberFormatException e) {
        		
        	}
        } else if ("borders".equals(property)) {
            Borders borders = new Borders();
            if (Defines.extractBorders(value, borders)) {
                setBorders(borders.left, borders.top, borders.right, borders.bottom);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Borders' property.");
            }
        } else if ("backgroundcolor".equals(property)) {
            setBackgroundColor(Defines.extractColor(value));
        } else if ("bordercolor".equals(property)) {
            setBorderColor(Defines.extractColor(value));
        } else if ("lines".equals(property)) {
            removeAllLines();

            List<String> lines = new ArrayList<String>();
            Defines.decodeList(value, lines);

            for (String line : lines) {
            	
                if ((line.length() >= 2) && 
                	(line.charAt(0) == '(' && 
                	line.charAt(line.length() - 1) == ')'))  {
                    
                	//FIXME:
                	//line.erase(0, 1);
                    //line.erase(line.length()-1, 1);
                	line = line.substring(1, line.length()-1);
                	
                    int openBracketPos = line.lastIndexOf('('); //FIXME:
                    int closeBracketPos = line.lastIndexOf(')'); //FIXME:

                    if ((openBracketPos == -1) || 
                    	(closeBracketPos == -1) || 
                    	(openBracketPos >= closeBracketPos)) {
                        return false;
                    }

                    Color color = Defines.extractColor(line.substring(openBracketPos, closeBracketPos - openBracketPos + 1));

                    int commaPos = line.lastIndexOf(',', openBracketPos); //FIXME:
                    if (commaPos == -1) {
                        return false;
                    }

                    //FIXME:
                    //line.erase(commaPos);
                    line = line.substring(0, commaPos);
                    
                    addLine(line, color);
                } else {
                    return false;
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
        } else if ("textsize".equals(property)) {
            value[0] = Integer.toString(getTextSize());
        } else if ("borders".equals(property)) {
            value[0] = "(" + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().left) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().top) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().right) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().bottom) + ")";
        } else if ("backgroundcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getBackgroundColor().r)) + "," + 
            	Integer.toString((int)(getBackgroundColor().g)) + "," + 
            	Integer.toString((int)(getBackgroundColor().b)) + "," + 
            	Integer.toString((int)(getBackgroundColor().a)) + ")";
        } else if ("bordercolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getBorderColor().r)) + "," + 
            	Integer.toString((int)(getBorderColor().g)) + "," + 
            	Integer.toString((int)(getBorderColor().b)) + "," + 
            	Integer.toString((int)(getBorderColor().a)) + ")";
        } else if ("lines".equals(property)) {
            List<String> lines = new ArrayList<String>();
            List<SharedWidgetPtr<? extends Widget>> labels = m_Panel.getWidgets();

            for (SharedWidgetPtr<? extends Widget> it : labels) {
            	lines.add("(" + ((SharedWidgetPtr<Label>)(it)).get().getText() + "," + 
                	Defines.convertColorToString(((SharedWidgetPtr<Label>)(it)).get().getTextColor()) + ")");
            }
            Defines.encodeList(lines, value);
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
        list.add(new Pair<String, String>("TextSize", "uint"));
        list.add(new Pair<String, String>("Borders", "borders"));
        list.add(new Pair<String, String>("BackgroundColor", "color"));
        list.add(new Pair<String, String>("BorderColor", "color"));
        list.add(new Pair<String, String>("Lines", "string"));
        return list;
    }
    
    protected void initialize(Container parent) {
        m_Parent = parent;
        setTextFont(m_Parent.getGlobalFont());    	
    }

    protected void updateDisplayedText() {
        float position;
        if (m_Scroll != null) {
            position = this._WidgetBordersImpl.m_TopBorder + 2.0f - (float)(m_Scroll.getValue()) ;
        } else {
            position = _WidgetBordersImpl.m_TopBorder + 2.0f;
        }
        List<SharedWidgetPtr<? extends Widget>> labels = m_Panel.getWidgets();
        for (SharedWidgetPtr<? extends Widget> it : labels) {
        	SharedWidgetPtr<Label> label = (SharedWidgetPtr<Label>)it;
            label.get().setPosition(this._WidgetBordersImpl.m_LeftBorder + 2.0f, position);

            position += label.get().getSize().y + 
            	(label.get().getTextFont().getLineSpacing(
            		label.get().getTextSize()) - label.get().getTextSize());
        }

        if ((m_Scroll == null) && (!labels.isEmpty())) {
        	SharedWidgetPtr<Label> label = (SharedWidgetPtr<Label>)labels.get(labels.size() - 1);
            position -= (label.get().getTextFont().getLineSpacing(label.get().getTextSize()) - 
            		label.get().getTextSize());

            if (position > m_Panel.getSize().y - 
            	this._WidgetBordersImpl.m_TopBorder - 
            	this._WidgetBordersImpl.m_BottomBorder) {
                float diff = position - (m_Panel.getSize().y - 
                	this._WidgetBordersImpl.m_TopBorder - 
                	this._WidgetBordersImpl.m_BottomBorder);
                for (SharedWidgetPtr<? extends Widget> it : labels) {
                    it.get().setPosition(
                    	it.get().getPosition().x, 
                    	it.get().getPosition().y - diff);
                }
            }
        }
    }

    public void draw(RenderTarget target, RenderStates states) {
    	Transform.multiplyEqual(states.transform, getTransform());

        target.draw(m_Panel, states);

        RectangleShape border = new RectangleShape(
        	new Vector2f(
        		(float)(this._WidgetBordersImpl.m_LeftBorder), 
        		m_Panel.getSize().y));
        border.setFillColor(m_BorderColor);
        target.draw(border, states);

        border.setSize(new Vector2f(m_Panel.getSize().x, (float)(this._WidgetBordersImpl.m_TopBorder)));
        target.draw(border, states);

        // Draw right border
        border.setPosition(m_Panel.getSize().x - this._WidgetBordersImpl.m_RightBorder, 0);
        border.setSize(
        	new Vector2f(
        		(float)(this._WidgetBordersImpl.m_RightBorder), 
        		m_Panel.getSize().y));
        target.draw(border, states);

        // Draw bottom border
        border.setPosition(0, 
        	m_Panel.getSize().y - this._WidgetBordersImpl.m_BottomBorder);
        border.setSize(
        	new Vector2f(
        		m_Panel.getSize().x, 
        		(float)(this._WidgetBordersImpl.m_BottomBorder)));
        target.draw(border, states);

        if (m_Scroll != null) {
            states.transform.translate(
            	m_Panel.getSize().x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, 
            	(float)(this._WidgetBordersImpl.m_TopBorder));
            target.draw(m_Scroll, states);
        }    	
    }


}
