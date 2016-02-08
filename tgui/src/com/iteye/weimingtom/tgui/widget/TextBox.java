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
import com.iteye.weimingtom.tgui.sf.Keyboard;
import com.iteye.weimingtom.tgui.sf.RectangleShape;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Text;
import com.iteye.weimingtom.tgui.sf.Time;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;
import com.iteye.weimingtom.tgui.sf.Vector2u;

/**
 * 20151025
 * @author Administrator
 *
 */
public class TextBox extends Widget implements WidgetBorders {
	public WidgetBordersImpl _WidgetBordersImpl = new WidgetBordersImpl();
    public enum TextBoxCallbacks {
        TextChanged(WidgetCallbacks.WidgetCallbacksCount.value() * 1),
        AllTextBoxCallbacks(WidgetCallbacks.WidgetCallbacksCount.value() * 2 - 1), 
        allbacksCount(WidgetCallbacks.WidgetCallbacksCount.value() * 2);
        
        int value;

        TextBoxCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }
	
	protected String m_LoadedConfigFile;

	protected Vector2u m_Size;

	protected String m_Text;
	protected String m_DisplayedText;
	protected int m_TextSize;
	protected int m_LineHeight;
	protected int m_Lines;

	protected int m_MaxChars;
	
	protected int m_TopLine;
	protected int m_VisibleLines;

	protected int m_SelChars;
	protected int m_SelStart;
	protected int m_SelEnd;

	protected Vector2u m_SelectionPointPosition;
	protected boolean m_SelectionPointVisible;

	protected Color m_SelectionPointColor;

	protected int m_SelectionPointWidth;

	protected boolean m_SelectionTextsNeedUpdate;

	protected Color m_BackgroundColor;
	protected Color m_SelectedTextBgrColor;
	protected Color m_BorderColor;

	protected Text m_TextBeforeSelection;
	protected Text m_TextSelection1;
	protected Text m_TextSelection2;
	protected Text m_TextAfterSelection1;
	protected Text m_TextAfterSelection2;

	protected List<Float> m_MultilineSelectionRectWidth = new ArrayList<Float>();

	protected Scrollbar m_Scroll;

	protected boolean m_PossibleDoubleClick;

	
	public TextBox() {
	    m_Size = new Vector2u(360, 200);
	    m_Text = "";
	    m_DisplayedText = "";
	    m_TextSize = 30;
	    m_LineHeight = 40;
	    m_Lines = 1;
	    m_MaxChars = 0;
	    m_TopLine = 1;
	    m_VisibleLines = 1;
	    m_SelChars = 0;
	    m_SelStart = 0;
	    m_SelEnd = 0;
	    m_SelectionPointPosition = new Vector2u(0, 0);
	    m_SelectionPointVisible = true;
	    m_SelectionPointColor = new Color(110, 110, 255);
	    m_SelectionPointWidth = 2;
	    m_SelectionTextsNeedUpdate = true;
	    m_Scroll = null;
	    m_PossibleDoubleClick = false;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_TextBox;
        m_AnimatedWidget = true;
        m_DraggableWidget = true;

        changeColors();

        m_Loaded = true;
	}

	public TextBox(TextBox copy) {
	    //Widget                       (copy),
	    super(copy);
		//WidgetBorders                (copy),
	    m_LoadedConfigFile = copy.m_LoadedConfigFile;
	    m_Size = copy.m_Size;
	    m_Text = copy.m_Text;
	    m_DisplayedText = copy.m_DisplayedText;
	    m_TextSize = copy.m_TextSize;
	    m_LineHeight = copy.m_LineHeight;
	    m_Lines = copy.m_Lines;
	    m_MaxChars = copy.m_MaxChars;
	    m_TopLine = copy.m_TopLine;
	    m_VisibleLines = copy.m_VisibleLines;
	    m_SelChars = copy.m_SelChars;
	    m_SelStart = copy.m_SelStart;
	    m_SelEnd = copy.m_SelEnd;
	    m_SelectionPointPosition = copy.m_SelectionPointPosition;
	    m_SelectionPointVisible = copy.m_SelectionPointVisible;
	    m_SelectionPointColor = copy.m_SelectionPointColor;
	    m_SelectionPointWidth = copy.m_SelectionPointWidth;
	    m_SelectionTextsNeedUpdate = copy.m_SelectionTextsNeedUpdate;
	    m_BackgroundColor = copy.m_BackgroundColor;
	    m_SelectedTextBgrColor = copy.m_SelectedTextBgrColor;
	    m_BorderColor = copy.m_BorderColor;
	    m_TextBeforeSelection = copy.m_TextBeforeSelection;
	    m_TextSelection1 = copy.m_TextSelection1;
	    m_TextSelection2 = copy.m_TextSelection2;
	    m_TextAfterSelection1 = copy.m_TextAfterSelection1;
	    m_TextAfterSelection2 = copy.m_TextAfterSelection2;
	    m_MultilineSelectionRectWidth = copy.m_MultilineSelectionRectWidth;
	    m_PossibleDoubleClick = copy.m_PossibleDoubleClick;
	    
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

	public TextBox assign(TextBox right) {
        if (this != right) {
            if (m_Scroll != null) {
                //delete m_Scroll;
            	m_Scroll.destroy();
                m_Scroll = null;
            }

            TextBox temp = new TextBox(right);
            super.assign(right);
            //this->Widget::operator=(right);
            //this->WidgetBorders::operator=(right);

            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_Size = temp.m_Size;
            m_Text = temp.m_Text;
            m_DisplayedText = temp.m_DisplayedText;
            m_TextSize = temp.m_TextSize;
            m_LineHeight = temp.m_LineHeight;
            m_Lines = temp.m_Lines;
            m_MaxChars = temp.m_MaxChars;
            m_TopLine = temp.m_TopLine;
            m_VisibleLines = temp.m_VisibleLines;
            m_SelChars = temp.m_SelChars;
            m_SelStart = temp.m_SelStart;
            m_SelEnd = temp.m_SelEnd;
            m_SelectionPointPosition = temp.m_SelectionPointPosition;
            m_SelectionPointVisible = temp.m_SelectionPointVisible;
            m_SelectionPointColor = temp.m_SelectionPointColor;
            m_SelectionPointWidth = temp.m_SelectionPointWidth;
            m_SelectionTextsNeedUpdate = temp.m_SelectionTextsNeedUpdate;
            m_BackgroundColor = temp.m_BackgroundColor;
            m_SelectedTextBgrColor = temp.m_SelectedTextBgrColor;
            m_BorderColor = temp.m_BorderColor;
            m_TextBeforeSelection = temp.m_TextBeforeSelection;
            m_TextSelection1 = temp.m_TextSelection1;
            m_TextSelection2 = temp.m_TextSelection2;
            m_TextAfterSelection1 = temp.m_TextAfterSelection1;
            m_TextAfterSelection2 = temp.m_TextAfterSelection2;
            m_MultilineSelectionRectWidth = temp.m_MultilineSelectionRectWidth;
            m_Scroll = temp.m_Scroll;
            m_PossibleDoubleClick = temp.m_PossibleDoubleClick;
        }

        return this;
	}

	public TextBox cloneObj() {
        return new TextBox(this);
	}

	public boolean load(String configFileFilename) {
		m_LoadedConfigFile = configFileFilename;

        if (m_Scroll != null) {
            //delete m_Scroll;
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
        if (!configFile.read("TextBox", properties, values)) {
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
            } else if ("textcolor".equals(property)) {
                setTextColor(configFile.readColor(value));
            } else if ("selectedtextbackgroundcolor".equals(property)) {
                setSelectedTextBackgroundColor(configFile.readColor(value));
            } else if ("selectedtextcolor".equals(property)) {
                setSelectedTextColor(configFile.readColor(value));
            } else if ("selectionpointcolor".equals(property)) {
                setSelectionPointColor(configFile.readColor(value));
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
                if (m_Scroll.load(configFileFolder + value.substring(1, value.length()-2 + 1)) == false) {
                    //delete m_Scroll;
                	m_Scroll.destroy();
                    m_Scroll = null;

                    return false;
                } else {
                    m_Scroll.setVerticalScroll(true);
                    m_Scroll.setLowValue(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
                    m_Scroll.setSize(m_Scroll.getSize().x, (float)(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder));
                }
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section ChatBox in " + configFileFilename + ".");
            }
        }

        return true;
	}

	public String getLoadedConfigFile() {
        return m_LoadedConfigFile;
	}
	
	public void setSize(float width, float height) {
        if (m_Loaded == false) {
            return;
        }

        if (m_LineHeight == 0) {
            return;
        }

        if (width < 0) {
        	width  = -width;
        }
        if (height < 0) {
        	height = -height;
        }

        if (m_Scroll == null) {
            width = (float)Defines.TGUI_MAXIMUM(50 + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder, width);
        } else {
            width = (float)Defines.TGUI_MAXIMUM(50 + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder + m_Scroll.getSize().x, width);
        }
        
        if (m_Scroll == null) {
            if (m_Text.length() > 0) {
                if (m_Size.y < ((m_Lines * m_LineHeight) - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder))
                    m_Size.y = (m_Lines * m_LineHeight) - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder;
            } else {
                if (m_Size.y < (m_LineHeight - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder))
                    m_Size.y = m_LineHeight - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder;
            }
        } else {
            if (m_Size.y < (m_LineHeight - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder))
                m_Size.y = m_LineHeight - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder;
        }

        if (height < (m_LineHeight + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder)) {
            height = (float)(m_LineHeight + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder);
        }

        m_Size.x = (int)(width);
        m_Size.y = (int)(height);

        if (m_Scroll != null) {
            m_Scroll.setLowValue(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
            m_Scroll.setSize(m_Scroll.getSize().x, (float)(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder));
        }

        m_SelectionTextsNeedUpdate = true;
        updateDisplayedText();
	}

	public Vector2f getSize() {
        return new Vector2f((float)(m_Size.x), (float)(m_Size.y));
    }

	public void setText(String text) {
        if (m_Loaded == false) {
            return;
        }

        m_Text = text;

        setSelectionPointPosition(m_Text.length());
    }

	public void addText(String text) {
        if (m_Loaded == false) {
            return;
        }

        m_Text += text;

        setSelectionPointPosition(m_Text.length());
	}

	public String getText() {
        return m_Text;
	}

	public void setTextFont(Font font) {
        m_TextBeforeSelection.setFont(font);
        m_TextSelection1.setFont(font);
        m_TextSelection2.setFont(font);
        m_TextAfterSelection1.setFont(font);
        m_TextAfterSelection2.setFont(font);
	}

	public Font getTextFont() {
        return m_TextBeforeSelection.getFont();
    }
	
	public void setTextSize(int size) {
        m_TextSize = size;

        if (m_TextSize < 8) {
            m_TextSize = 8;
        }

        m_TextBeforeSelection.setCharacterSize(m_TextSize);
        m_TextSelection1.setCharacterSize(m_TextSize);
        m_TextSelection2.setCharacterSize(m_TextSize);
        m_TextAfterSelection1.setCharacterSize(m_TextSize);
        m_TextAfterSelection2.setCharacterSize(m_TextSize);

        m_LineHeight = m_TextBeforeSelection.getFont().getLineSpacing(m_TextSize);

        if (m_LineHeight == 0) {
            return;
        }

        if (m_Size.y < (m_LineHeight + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder)) {
            m_Size.y = m_LineHeight + this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder;
        }

        if (m_Scroll != null) {
            m_Scroll.setLowValue(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
            m_Scroll.setSize(m_Scroll.getSize().x, (float)(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder));
        }

        m_SelectionTextsNeedUpdate = true;
        updateDisplayedText();
	}

	public int getTextSize() {
        return m_TextSize;
	}

	public void setMaximumCharacters(int maxChars) {
        m_MaxChars = maxChars;

        if ((m_MaxChars > 0) && (m_Text.length() > m_MaxChars)) {
            //m_Text.erase(m_MaxChars, sf::String::InvalidPos);
        	m_Text = m_Text.substring(0, m_MaxChars);
        	
            setSelectionPointPosition(m_Text.length());
        }
	}

	public int getMaximumCharacters() {
        return m_MaxChars;
	}

	public void changeColors() {
		changeColors(new Color(50, 50, 50),
	        new Color(0, 0, 0),
	        new Color(255, 255, 255),
	        new Color(10, 110, 255),
	        new Color(0, 0, 0),
	        new Color(110, 110, 255));
	}
	
    public void changeColors(Color backgroundColor,
    		Color color,
    		Color selectedColor,
    		Color selectedBgrColor,
    		Color borderColor,
    		Color selectionPointColor) {
        m_TextBeforeSelection.setColor(color);
        m_TextSelection1.setColor(selectedColor);
        m_TextSelection2.setColor(selectedColor);
        m_TextAfterSelection1.setColor(color);
        m_TextAfterSelection2.setColor(color);

        m_SelectionPointColor           = selectionPointColor;
        m_BackgroundColor               = backgroundColor;
        m_SelectedTextBgrColor          = selectedBgrColor;
        m_BorderColor                   = borderColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        m_BackgroundColor = backgroundColor;
    }

    public void setTextColor(Color textColor) {
        m_TextBeforeSelection.setColor(textColor);
        m_TextAfterSelection1.setColor(textColor);
        m_TextAfterSelection2.setColor(textColor);
    }

    public void setSelectedTextColor(Color selectedTextColor) {
        m_TextSelection1.setColor(selectedTextColor);
        m_TextSelection2.setColor(selectedTextColor);
    }

    public void setSelectedTextBackgroundColor(Color selectedTextBackgroundColor) {
        m_SelectedTextBgrColor = selectedTextBackgroundColor;
    }

    public void setBorderColor(Color borderColor) {
        m_BorderColor = borderColor;
    }

    public void setSelectionPointColor(Color selectionPointColor) {
        m_SelectionPointColor = selectionPointColor;
    }

    public Color getBackgroundColor() {
        return m_BackgroundColor;
    }

    public Color getTextColor() {
        return m_TextBeforeSelection.getColor();
    }

    public Color getSelectedTextColor() {
        return m_TextSelection1.getColor();
    }

    public Color getSelectedTextBackgroundColor() {
        return m_SelectedTextBgrColor;
    }

    public Color getBorderColor() {
        return m_BorderColor;
    }

    public Color getSelectionPointColor() {
        return m_SelectionPointColor;
    }

    public void setSelectionPointPosition(int charactersBeforeSelectionPoint) {
        if (charactersBeforeSelectionPoint > m_Text.length()) {
            charactersBeforeSelectionPoint = m_Text.length();
        }

        if (m_LineHeight == 0) {
            return;
        }

        m_SelChars = 0;
        m_SelStart = charactersBeforeSelectionPoint;
        m_SelEnd = charactersBeforeSelectionPoint;

        m_TextBeforeSelection.setString(m_DisplayedText);
        m_TextSelection1.setString("");
        m_TextSelection2.setString("");
        m_TextAfterSelection1.setString("");
        m_TextAfterSelection2.setString("");

        updateDisplayedText();

        if (m_Scroll != null) {
            int newlines = 0;
            int newlinesAdded = 0;
            int totalLines = 0;

            for (int i = 0; i < m_SelEnd; ++i) {
                if (m_Text.charAt(i) != '\n') {
                    if (m_DisplayedText.charAt(i + newlinesAdded) == '\n') {
                        ++newlinesAdded;
                        ++totalLines;

                        if (i < m_SelEnd) {
                            ++newlines;
                        }
                    }
                } else {
                    ++totalLines;

                    if (i < m_SelEnd) {
                        ++newlines;
                    }
                }
            }

            if ((newlines < m_TopLine - 1) || 
            	((newlines < m_TopLine) && (m_Scroll.getValue() % m_LineHeight > 0))) {
                m_Scroll.setValue(newlines * m_LineHeight);
                updateDisplayedText();
            } else if (newlines > m_TopLine + m_VisibleLines - 2) {
                m_Scroll.setValue((newlines - m_VisibleLines + 1) * m_LineHeight);
                updateDisplayedText();
            } else if ((newlines > m_TopLine + m_VisibleLines - 3) && (m_Scroll.getValue() % m_LineHeight > 0)) {
                m_Scroll.setValue((newlines - m_VisibleLines + 2) * m_LineHeight);
                updateDisplayedText();
            }
        }
    }

    public boolean setScrollbar(String scrollbarConfigFileFilename) {
        if (scrollbarConfigFileFilename.length() == 0) {
            return false;
        }

        if (m_Scroll != null) {
            //delete m_Scroll;
        	m_Scroll.destroy();
        	m_Scroll = null;
        }

        m_Scroll = new Scrollbar();
        if (m_Scroll.load(scrollbarConfigFileFilename) == false) {
            //delete m_Scroll;
        	m_Scroll.destroy();
            m_Scroll = null;

            return false;
        } else {
            m_Scroll.setVerticalScroll(true);
            m_Scroll.setSize(m_Scroll.getSize().x, (float)(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder));
            m_Scroll.setLowValue(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
            m_Scroll.setMaximum(m_Lines * m_LineHeight);

            return true;
        }
    }

    public void removeScrollbar() {
        //delete m_Scroll;
    	m_Scroll.destroy();
    	m_Scroll = null;

        m_TopLine = 1;
    }

    public void setSelectionPointWidth(int width) {
        m_SelectionPointWidth = width;
    }

    public int getSelectionPointWidth() {
        return m_SelectionPointWidth;
    }

    public void setTransparency(int transparency) {
        //Widget::
        super.setTransparency(transparency);

        if (m_Scroll != null) {
            m_Scroll.setTransparency(m_Opacity);
        }
    }

    public void leftMousePressed(float x, float y) {
        if (m_Loaded == false) {
            return;
        }

        m_MouseDown = true;

        boolean clickedOnTextBox = true;

        if (m_Scroll != null) {
            int oldValue = m_Scroll.getValue();

            m_Scroll.setPosition(getPosition().x + m_Size.x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, getPosition().y + this._WidgetBordersImpl.m_TopBorder);

            if (m_Scroll.mouseOnWidget(x, y)) {
                m_Scroll.leftMousePressed(x, y);
                clickedOnTextBox = false;
            }

            m_Scroll.setPosition(0, 0);

            if (oldValue != m_Scroll.getValue()) {
                updateDisplayedText();
            }
        }

        if (clickedOnTextBox) {
            if (m_LineHeight == 0) {
                return;
            }

            int selectionPointPosition = findSelectionPointPosition(
            	x - getPosition().x - this._WidgetBordersImpl.m_LeftBorder - 4, 
            	y - getPosition().y - this._WidgetBordersImpl.m_TopBorder);

            if ((m_PossibleDoubleClick) && 
            	(m_SelChars == 0) && 
            	(selectionPointPosition == m_SelEnd)) {
                m_PossibleDoubleClick = false;

                m_SelStart = 0;
                m_SelEnd = m_Text.length();
                m_SelChars = m_Text.length();

                m_SelectionTextsNeedUpdate = true;
                updateDisplayedText();

                if (m_Scroll != null) {
                    int newlines = 0;
                    int newlinesAdded = 0;
                    int totalLines = 0;

                    for (int i = 0; i<m_SelEnd; ++i) {
                        if (m_Text.charAt(i) != '\n') {
                            if (m_DisplayedText.charAt(i + newlinesAdded) == '\n') {
                                ++newlinesAdded;
                                ++totalLines;

                                if (i < m_SelEnd) {
                                    ++newlines;
                                }
                            }
                        } else {
                            ++totalLines;

                            if (i < m_SelEnd) {
                                ++newlines;
                            }
                        }
                    }

                    if ((newlines < m_TopLine - 1) || 
                    	((newlines < m_TopLine) && (m_Scroll.getValue() % m_LineHeight > 0))) {
                        m_Scroll.setValue(newlines * m_LineHeight);
                        updateDisplayedText();
                    } else if (newlines > m_TopLine + m_VisibleLines - 2) {
                        m_Scroll.setValue((newlines - m_VisibleLines + 1) * m_LineHeight);
                        updateDisplayedText();
                    } else if ((newlines > m_TopLine + m_VisibleLines - 3) && 
                    	(m_Scroll.getValue() % m_LineHeight > 0)) {
                        m_Scroll.setValue((newlines - m_VisibleLines + 2) * m_LineHeight);
                        updateDisplayedText();
                    }
                }
            } else {
                setSelectionPointPosition(selectionPointPosition);

                m_PossibleDoubleClick = true;
            }

            m_MouseDown = true;

            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();
        }
    }

    public void leftMouseReleased(float x, float y) {
        if (m_Loaded == false) {
            return;
        }

        if (m_Scroll != null) {
            if (m_Scroll.m_MouseDown == true) {
                if (m_LineHeight == 0) {
                    return;
                }

                int oldValue = m_Scroll.getValue();

                m_Scroll.setPosition(getPosition().x + m_Size.x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, 
                		getPosition().y + this._WidgetBordersImpl.m_TopBorder);

                m_Scroll.leftMouseReleased(x, y);

                m_Scroll.setPosition(0, 0);

                if (oldValue != m_Scroll.getValue()) {
                    updateDisplayedText();

                    if (m_Scroll.getValue() == oldValue + 1) {
                        m_Scroll.setValue(m_Scroll.getValue() - 1);

                        m_Scroll.setValue(m_Scroll.getValue() + m_LineHeight - (m_Scroll.getValue() % m_LineHeight));
                    } else if (m_Scroll.getValue() == oldValue - 1) {
                        m_Scroll.setValue(m_Scroll.getValue() + 1);

                        if (m_Scroll.getValue() % m_LineHeight > 0) {
                            m_Scroll.setValue(m_Scroll.getValue() - (m_Scroll.getValue() % m_LineHeight));
                    	} else {
                            m_Scroll.setValue(m_Scroll.getValue() - m_LineHeight);
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

        m_PossibleDoubleClick = false;

        if (m_Scroll != null) {
            m_Scroll.setPosition(
            	getPosition().x + m_Size.x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, 
            	getPosition().y + this._WidgetBordersImpl.m_TopBorder);

            if ((m_Scroll.m_MouseDown) && 
            	(m_Scroll.m_MouseDownOnThumb)) {
                int oldValue = m_Scroll.getValue();

                m_Scroll.mouseMoved(x, y);

                if (oldValue != m_Scroll.getValue()) {
                    updateDisplayedText();
                }
            } else {
                if (m_Scroll.mouseOnWidget(x, y)) {
                    m_Scroll.mouseMoved(x, y);
                }

                if (m_MouseDown) {
                    selectText(x, y);
                }
            }

            m_Scroll.setPosition(0, 0);
        } else {
            if (m_MouseDown) {
                selectText(x, y);
            }
        }
    }
    
    public void keyPressed(Keyboard.Key key) {
        if (m_Loaded == false) {
            return;
        }

        if (key == Keyboard.Key.Left) {
            if (m_SelChars > 0) {
                if (m_SelStart < m_SelEnd) {
                    setSelectionPointPosition(m_SelStart);
                } else {
                    setSelectionPointPosition(m_SelEnd);
                }
            } else {
                if (m_SelEnd > 0) {
                    setSelectionPointPosition(m_SelEnd - 1);
                }
            }

            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();
        } else if (key == Keyboard.Key.Right) {
            if (m_SelChars > 0) {
                if (m_SelStart < m_SelEnd) {
                    setSelectionPointPosition(m_SelEnd);
                } else {
                    setSelectionPointPosition(m_SelStart);
                }
            } else {
                if (m_SelEnd < m_Text.length()) {
                    setSelectionPointPosition(m_SelEnd + 1);
                }
            }

            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();
        } else if (key == Keyboard.Key.Up) {
            Text tempText = new Text(m_TextBeforeSelection);
            tempText.setString(m_DisplayedText);

            boolean newlineAdded = false;
            int newlinesAdded = 0;

            int character;
            int newTopPosition = 0;
            Vector2u newPosition = new Vector2u(0, 0);

            int distanceX;
            int previousdistanceX = m_Size.x;

            for (int i = 0; i < m_SelEnd; ++i) {
                if (m_Text.charAt(i) != '\n') {
                    if (m_DisplayedText.charAt(i + newlinesAdded) == '\n') {
                        ++newlinesAdded;
                        newlineAdded = true;
                    }
                } else { 
                	newlineAdded = false;
                }
            }

            Vector2u originalPosition = new Vector2u(tempText.findCharacterPos(m_SelEnd + newlinesAdded));

            for (character = m_SelEnd; character > 0; --character) {
                newTopPosition = (int)(tempText.findCharacterPos(character + newlinesAdded - 1).y);

                if (newTopPosition < originalPosition.y) {
                    break;
                }
            }

            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();

            if (character > 0) {
                for ( ; character > 0; --character) {
                    newPosition = new Vector2u(tempText.findCharacterPos(character + newlinesAdded - 1));

                    if (newPosition.y < newTopPosition) {
                        setSelectionPointPosition(character + (newlineAdded ? 1 : 0));
                        return;
                    }

                    distanceX = newPosition.x - originalPosition.x;

                    if ((int)(Math.abs(distanceX)) > previousdistanceX) {
                        setSelectionPointPosition(character + (newlineAdded ? 1 : 0));
                        return;
                    }

                    previousdistanceX = Math.abs(distanceX);
                }

                if (originalPosition.x > previousdistanceX) {
                    setSelectionPointPosition(character + (newlineAdded ? 1 : 0));
                    return;
                }

                setSelectionPointPosition(0);
            }
        } else if (key == Keyboard.Key.Down) {
            Text tempText = new Text(m_TextBeforeSelection);
            tempText.setString(m_DisplayedText);

            boolean newlineAdded = false;
            int newlinesAdded = 0;

            int character;
            int newTopPosition = 0;
            Vector2u newPosition = new Vector2u(0, 0);

            int distanceX;
            int previousdistanceX = m_Size.x;

            for (int i = 0; i < m_Text.length(); ++i) {
                if (m_Text.charAt(i) != '\n') {
                    if (m_DisplayedText.charAt(i + newlinesAdded) == '\n') {
                        ++newlinesAdded;
                        newlineAdded = true;

                        if (i > m_SelEnd) {
                            break;
                        }
                    }
                } else {
                    newlineAdded = false;

                    if (i > m_SelEnd) {
                        break;
                    }
                }
            }

            Vector2u originalPosition = new Vector2u(tempText.findCharacterPos(m_SelEnd + newlinesAdded - (newlineAdded ? 1 : 0)));

            for (character = m_SelEnd; character < m_Text.length(); ++character) {
                newTopPosition = (int)(tempText.findCharacterPos(character + newlinesAdded - (newlineAdded ? 1 : 0) + 1).y);

                if (newTopPosition > originalPosition.y) {
                    break;
                }
            }

            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();

            if (character < m_Text.length()) {
            	for (; character < m_Text.length() + 1; ++character) {
                    newPosition = new Vector2u(tempText.findCharacterPos(character + newlinesAdded - (newlineAdded ? 1 : 0) + 1));

                    if (newPosition.y > newTopPosition) {
                        setSelectionPointPosition(character - (newlineAdded ? 1 : 0));
                        return;
                    }

                    distanceX = newPosition.x - originalPosition.x;

                    if (Math.abs(distanceX) > previousdistanceX) {
                        setSelectionPointPosition(character - (newlineAdded ? 1 : 0));
                        return;
                    }

                    previousdistanceX = Math.abs(distanceX);
                }

                setSelectionPointPosition(m_Text.length());
            }
        } else if (key == Keyboard.Key.Home) {
            setSelectionPointPosition(0);

            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();
        } else if (key == Keyboard.Key.End) {
            setSelectionPointPosition(m_Text.length());

            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();
        } else if (key == Keyboard.Key.Return) {
            textEntered('\n');
        } else if (key == Keyboard.Key.BackSpace) {
            if (m_SelChars == 0) {
                if (m_SelEnd == 0) {
                    return;
                }

                //m_Text.erase(m_SelEnd-1, 1);
                m_Text = m_Text.substring(0, m_SelEnd - 1) + 
                	m_Text.substring(m_SelEnd - 1 + 1);
                
                setSelectionPointPosition(m_SelEnd - 1);

                if (m_Scroll != null) {
                    if (m_Scroll.getValue() > m_Scroll.getMaximum() - m_Scroll.getLowValue()) {
                        m_Scroll.setValue(m_Scroll.getValue());

                        m_SelectionTextsNeedUpdate = true;
                        updateDisplayedText();
                    }
                }
            } else {
            	deleteSelectedCharacters();
            }

            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();

            if (this.getCallbackManager().m_CallbackFunctions
            		.get(TextBoxCallbacks.TextChanged.value())
            		.isEmpty() == false) {
                this.getCallbackManager().m_Callback.trigger = TextBoxCallbacks.TextChanged.value();
                this.getCallbackManager().m_Callback.text = m_Text;
                addCallback();
            }
        } else if (key == Keyboard.Key.Delete) {
            if (m_SelChars == 0) {
                if (m_SelEnd == m_Text.length()) {
                    return;
                }

                //m_Text.erase(m_SelEnd, 1);
                m_Text = m_Text.substring(0, m_SelEnd) + 
                	m_Text.substring(m_SelEnd + 1);
                
                setSelectionPointPosition(m_SelEnd);

                if (m_Scroll != null) {
                    if ((m_Scroll.getValue() == m_Scroll.getMaximum() - m_Scroll.getLowValue()) || 
                    	(m_Scroll.getValue() > m_Scroll.getMaximum() - m_Scroll.getLowValue() - m_LineHeight)) {
                        m_Scroll.setValue(m_Scroll.getValue());

                        m_SelectionTextsNeedUpdate = true;
                        updateDisplayedText();
                    }
                }
            } else { 
            	deleteSelectedCharacters();
            }
            
            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();

            if (this.getCallbackManager().m_CallbackFunctions
            		.get(TextBoxCallbacks.TextChanged.value()).isEmpty() == false) {
                this.getCallbackManager().m_Callback.trigger = TextBoxCallbacks.TextChanged.value();
                this.getCallbackManager().m_Callback.text = m_Text;
                addCallback();
            }
        }
    }

    public void textEntered(int key) {
        if (m_Loaded == false) {
            return;
        }

        deleteSelectedCharacters();

        if ((m_MaxChars > 0) && 
        	(m_Text.length() + 1 > m_MaxChars)) {
        	return;
        }

        if (m_Scroll == null) {
            if (m_LineHeight == 0) {
                return;
            }

            float maxLineWidth = (float)(m_Size.x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_RightBorder - 4);

            if (maxLineWidth < 0) {
                maxLineWidth = 0;
            }

            Text tempText = new Text(m_TextBeforeSelection);
            int beginChar = 0;
            int newlinesAdded = 0;

            String text = m_Text;
            //text.insert(m_SelEnd, key);
            text = text.substring(0, m_SelEnd) + 
            	Character.toString((char)key) + 
            	text.substring(m_SelEnd);
            
            String displayedText = text;
            int lines = 1;

            for (int i = 1; i < text.length() + 1; ++i) {
                if (text.charAt(i - 1) != '\n') {
                    tempText.setString(text.substring(beginChar, i - beginChar + beginChar));

                    if (tempText.findCharacterPos(i).x > maxLineWidth) {
                        //displayedText.insert(i + newlinesAdded - 1, '\n');
                    	displayedText = displayedText.substring(0, i + newlinesAdded - 1) + 
                    		'\n' + 
                    		displayedText.substring(i + newlinesAdded - 1);
                    	
                        beginChar = i - 1;
                        ++newlinesAdded;
                        ++lines;
                    }
                } else {
                    beginChar = i;
                    ++lines;
                }

                if (lines > (m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder) / m_LineHeight) {
                    return;
                }
            }
        }

        //m_Text.insert(m_SelEnd, key);
        m_Text = m_Text.substring(0, m_SelEnd) + 
        	Character.toString((char)key) + 
        	m_Text.substring(m_SelEnd);
        
        setSelectionPointPosition(m_SelEnd + 1);

        m_SelectionPointVisible = true;
        m_AnimationTimeElapsed = new Time();

        if (this.getCallbackManager().m_CallbackFunctions.get(TextBoxCallbacks.TextChanged.value()).isEmpty() == false) {
        	this.getCallbackManager().m_Callback.trigger = TextBoxCallbacks.TextChanged.value();
        	this.getCallbackManager().m_Callback.text = m_Text;
            addCallback();
        }
    }

    public void mouseWheelMoved(int delta, int x, int y) {
        if (m_Scroll != null) {
            if (m_Scroll.getLowValue() < m_Scroll.getMaximum()) {
                if (delta < 0) {
                    m_Scroll.setValue(m_Scroll.getValue() + ((int)(-delta) * (m_LineHeight / 2)));
                } else {
                    int change = (int)(delta) * (m_LineHeight / 2);

                    if (change < m_Scroll.getValue()) {
                        m_Scroll.setValue(m_Scroll.getValue() - change);
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

    public void widgetUnfocused() {
        if (m_SelChars != 0) {
            setSelectionPointPosition(m_SelEnd);
        }
        
        //Widget::
        super.widgetUnfocused();
    }

    public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            load(value);
        } else if ("text".equals(property)) {
            String[] text = new String[1];
            Defines.decodeString(value, text);
            setText(text[0]);
        } else if ("textsize".equals(property)) {
            setTextSize(Integer.parseInt(value.trim()));
        } else if ("maximumcharacters".equals(property)) {
            setMaximumCharacters(Integer.parseInt(value.trim()));
        } else if ("borders".equals(property)) {
            Borders borders = new Borders();
            if (Defines.extractBorders(value, borders)) {
                setBorders(borders.left, borders.top, borders.right, borders.bottom);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Borders' property.");
            }
        } else if ("backgroundcolor".equals(property)) {
            setBackgroundColor(Defines.extractColor(value));
        } else if ("textcolor".equals(property)) {
            setTextColor(Defines.extractColor(value));
        } else if ("selectedtextcolor".equals(property)) {
            setSelectedTextColor(Defines.extractColor(value));
        } else if ("selectedtextbackgroundcolor".equals(property)) {
            setSelectedTextBackgroundColor(Defines.extractColor(value));
        } else if ("bordercolor".equals(property)) {
            setBorderColor(Defines.extractColor(value));
        } else if ("selectionpointcolor".equals(property)) {
            setSelectionPointColor(Defines.extractColor(value));
        } else if ("selectionpointwidth".equals(property)) {
            setSelectionPointWidth(Integer.parseInt(value.trim()));
        } else if ("callback".equals(property)) {
            //Widget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("TextChanged".equals(it)) || 
                	("textchanged".equals(it))) {
                    this.getCallbackManager().bindCallback(TextBoxCallbacks.TextChanged.value());
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
        } else if ("text".equals(property)) {
            Defines.encodeString(getText(), value);
        } else if ("textsize".equals(property)) {
            value[0] = Integer.toString(getTextSize());
    	} else if ("maximumcharacters".equals(property)) {
            value[0] = Integer.toString(getMaximumCharacters());
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
        } else if ("textcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getTextColor().r)) + "," + 
            	Integer.toString((int)(getTextColor().g)) + "," + 
            	Integer.toString((int)(getTextColor().b)) + "," + 
            	Integer.toString((int)(getTextColor().a)) + ")";
        } else if ("selectedtextcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getSelectedTextColor().r)) + "," + 
            	Integer.toString((int)(getSelectedTextColor().g)) + "," + 
            	Integer.toString((int)(getSelectedTextColor().b)) + "," + 
            	Integer.toString((int)(getSelectedTextColor().a)) + ")";
        } else if ("selectedtextbackgroundcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getSelectedTextBackgroundColor().r)) + "," + 
            	Integer.toString((int)(getSelectedTextBackgroundColor().g)) + "," + 
            	Integer.toString((int)(getSelectedTextBackgroundColor().b)) + "," + 
            	Integer.toString((int)(getSelectedTextBackgroundColor().a)) + ")";
        } else if ("bordercolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getBorderColor().r)) + "," + 
            	Integer.toString((int)(getBorderColor().g)) + "," + 
            	Integer.toString((int)(getBorderColor().b)) + "," + 
            	Integer.toString((int)(getBorderColor().a)) + ")";
        } else if ("selectionpointcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getSelectionPointColor().r)) + "," + 
            	Integer.toString((int)(getSelectionPointColor().g)) + "," + 
            	Integer.toString((int)(getSelectionPointColor().b)) + "," + 
            	Integer.toString((int)(getSelectionPointColor().a)) + ")";
        } else if ("selectionpointwidth".equals(property)) {
            value[0] = Integer.toString(getSelectionPointWidth());
        } else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //Widget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(TextBoxCallbacks.TextChanged.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(TextBoxCallbacks.TextChanged.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(TextBoxCallbacks.TextChanged.value()).get(0) == null)) {
                callbacks.add("TextChanged");
			}

            Defines.encodeList(callbacks, value);

            if (value[0].length() == 0) {
                value[0] = tempValue[0];
        	} else if (tempValue[0].length() != 0) {
                value[0] += "," + tempValue;
            }
        } else { 
        	//Widget::
        	return super.getProperty(property, value);
        }
        
        // You pass here when one of the properties matched
        return true;
    }

    public List<Pair<String, String>> getPropertyList() {
    	//Widget::
    	List<Pair<String, String>> list = super.getPropertyList();
        list.add(new Pair<String, String>("ConfigFile", "string"));
        list.add(new Pair<String, String>("Text", "string"));
        list.add(new Pair<String, String>("TextSize", "uint"));
        list.add(new Pair<String, String>("MaximumCharacters", "uint"));
        list.add(new Pair<String, String>("Borders", "borders"));
        list.add(new Pair<String, String>("BackgroundColor", "color"));
        list.add(new Pair<String, String>("TextColor", "color"));
        list.add(new Pair<String, String>("SelectedTextColor", "color"));
        list.add(new Pair<String, String>("SelectedTextBackgroundColor", "color"));
        list.add(new Pair<String, String>("BorderColor", "color"));
        list.add(new Pair<String, String>("SelectionPointColor", "color"));
        list.add(new Pair<String, String>("SelectionPointWidth", "uint"));
        return list;
    }

    protected int findSelectionPointPosition(float posX, float posY) {
        if (m_Text.isEmpty()) {
            return 0;
        }

        if (m_LineHeight == 0) {
            return 0;
        }

        int line;

        if (m_Scroll == null) {
            if (posY < 0) {
                return 0;
            } else {
                line = (int)(posY / m_LineHeight + 1);
            }
        } else {
            if (posY + m_Scroll.getValue() < 0) {
                return 0;
            } else {
                line = (int)((posY + m_Scroll.getValue()) / m_LineHeight + 1);
            }
        }

        Text fullText = new Text(m_TextBeforeSelection);
        fullText.setString(m_DisplayedText);

        if ((line > m_Lines) || 
        	((line == m_Lines) && (posX > fullText.findCharacterPos(m_DisplayedText.length()).x))) {
            return m_Text.length();
        } else {
            String tempString = m_DisplayedText;
            int newlinePos = 0;

            if (line > 1) {
                int i = 1;

                if (m_Text.charAt(0) == '\n') {
                    ++i;
                }

                for (; i<line; ++i) {
                    newlinePos = tempString.indexOf('\n', newlinePos + 1);
                }
                
                //tempString.erase(0, newlinePos + 1);
                tempString = tempString.substring(newlinePos + 1);
            }

            int newlinePos2 = tempString.indexOf('\n');
            if (newlinePos2 != -1) {
                //tempString.erase(newlinePos2, sf::String::InvalidPos);
            	tempString = tempString.substring(0, newlinePos2);
            }
            
            Text tempText = new Text(m_TextBeforeSelection);

            int newlinesAdded = 0;
            int totalNewlinesAdded = 0;
            int beginChar = 0;
            String tempString2 = m_Text;

            float maxLineWidth;

            if (m_Scroll == null) {
                maxLineWidth = m_Size.x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_RightBorder - 4.f;
            } else {
                maxLineWidth = m_Size.x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_RightBorder - 4.f - m_Scroll.getSize().x;
            }
            
            if (maxLineWidth < 0) {
                maxLineWidth = 0;
            }

            for (int i = 1; (i < m_Text.length() + 1) && (totalNewlinesAdded != line - 1); ++i) {
                if (m_Text.charAt(i - 1) != '\n') {
                    tempText.setString(m_Text.substring(beginChar, i - beginChar + beginChar));

                    if (tempText.findCharacterPos(i).x > maxLineWidth) {
                        //tempString2.insert(i + newlinesAdded - 1, '\n');
                    	tempString2 = tempString2.substring(0, i + newlinesAdded - 1) + 
                    		'\n' + 
                    		tempString2.substring(i + newlinesAdded - 1);
                    	
                        beginChar = i - 1;
                        ++newlinesAdded;
                        ++totalNewlinesAdded;
                    }
                } else {
                    beginChar = i;
                    ++totalNewlinesAdded;
                }
            }

            tempText.setString(tempString);

            if (tempString.length() == 0) {
                if (line > 1) {
                    return newlinePos - newlinesAdded + 1;
            	} else {
                    return newlinePos;
                }
            }

            if ((tempString.length() == 1) && 
            	(posX < (tempText.findCharacterPos(1).x / 2.f))) {
                if (line > 1) {
                    return newlinePos - newlinesAdded;
                } else {
                    if (newlinePos > 0) {
                        return newlinePos - 1;
                    } else {
                        return 0;
                    }
                }
            }

            for (int i = 1; i <= tempString.length(); ++i) {
                if (posX < (tempText.findCharacterPos(i-1).x + tempText.findCharacterPos(i).x) / 2.f) {
                    if (line > 1) {
                        return newlinePos + i - newlinesAdded;
                    } else {
                        return newlinePos + i - 1;
                    }
                }
            }

            if (tempText.findCharacterPos(tempString.length()).x - 
            		(((3.f * tempText.findCharacterPos(tempString.length() - 1).x) + 
            		tempText.findCharacterPos(tempString.length()).x) / 2.f) != 0) {
                if (line > 1) {
                    return newlinePos + tempString.length() + 1 - newlinesAdded;
                } else {
                    return newlinePos + tempString.length();
                }
            }
        }

        return m_SelEnd;
    }

    protected void selectText(float posX, float posY) {
        if (m_LineHeight == 0) {
            return;
        }

        m_SelEnd = findSelectionPointPosition(
        	posX - getPosition().x - this._WidgetBordersImpl.m_LeftBorder - 4, 
        	posY - getPosition().y - this._WidgetBordersImpl.m_TopBorder);

        if (m_SelEnd < m_SelStart) {
            m_SelChars = m_SelStart - m_SelEnd;
        } else {
            m_SelChars = m_SelEnd - m_SelStart;
        }
        
        m_SelectionTextsNeedUpdate = true;
        updateDisplayedText();

        if (m_Scroll != null) {
            int newlines = 0;
            int newlinesAdded = 0;
            int totalLines = 0;

            for (int i = 0; i < m_SelEnd; ++i) {
                if (m_Text.charAt(i) != '\n') {
                    if (m_DisplayedText.charAt(i + newlinesAdded) == '\n') {
                        ++newlinesAdded;
                        ++totalLines;

                        if (i < m_SelEnd) {
                            ++newlines;
                        }
                    }
                } else {
                    ++totalLines;

                    if (i < m_SelEnd) {
                        ++newlines;
                    }
                }
            }

            if ((newlines < m_TopLine - 1) || 
            	((newlines < m_TopLine) && (m_Scroll.getValue() % m_LineHeight > 0))) {
                m_Scroll.setValue(newlines * m_LineHeight);
                updateDisplayedText();
            } else if (newlines > m_TopLine + m_VisibleLines - 2) {
                m_Scroll.setValue((newlines - m_VisibleLines + 1) * m_LineHeight);
                updateDisplayedText();
            } else if ((newlines > m_TopLine + m_VisibleLines - 3) && (m_Scroll.getValue() % m_LineHeight > 0)) {
                m_Scroll.setValue((newlines - m_VisibleLines + 2) * m_LineHeight);
                updateDisplayedText();
            }
        }
    }

    protected void deleteSelectedCharacters() {
        //m_Text.erase(Defines.TGUI_MINIMUM(m_SelStart, m_SelEnd), m_SelChars);
    	int pos = (int)Defines.TGUI_MINIMUM(m_SelStart, m_SelEnd);
    	m_Text = m_Text.substring(0, pos) + m_Text.substring(pos + m_SelChars);
    	
        setSelectionPointPosition((int)Defines.TGUI_MINIMUM(m_SelStart, m_SelEnd));
    }


    protected void updateDisplayedText() {
        if (m_Loaded == false) {
            return;
        }

        if (m_LineHeight == 0) {
            return;
        }

        float maxLineWidth;

        if (m_Scroll == null) {
            maxLineWidth = m_Size.x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_RightBorder - 4.f;
        } else {
            maxLineWidth = m_Size.x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_TopBorder - m_Scroll.getSize().x - 4.f;
        }
        
        if (maxLineWidth < 0) {
            maxLineWidth = 0;
        }

        Text tempText = new Text(m_TextBeforeSelection);
        int beginChar = 0;
        int newlinesAdded = 0;
        int newlinesAddedBeforeSelection = 0;

        m_DisplayedText = m_Text;
        m_Lines = 1;

        for (int i = 1; i < m_Text.length() + 1; ++i) {
            if (m_Text.charAt(i - 1) != '\n') {
                tempText.setString(m_Text.substring(beginChar, i - beginChar + beginChar));

                if (tempText.findCharacterPos(i).x > maxLineWidth) {
                    //m_DisplayedText.insert(i + newlinesAdded - 1, '\n');
                	m_DisplayedText = m_DisplayedText.substring(0, i + newlinesAdded - 1) +
                		'\n' +
                		m_DisplayedText.substring(i + newlinesAdded - 1);
                	
                    beginChar = i - 1;
                    ++newlinesAdded;
                    ++m_Lines;
                }
            } else {
                beginChar = i;
                ++m_Lines;
            }

            if (m_SelEnd == i - 1) {
                newlinesAddedBeforeSelection = newlinesAdded;
            }
            
            if (m_Scroll == null) {
                if (m_Lines > (m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder) / m_LineHeight) {
                    //m_DisplayedText.erase(i + newlinesAdded - 1, sf::String::InvalidPos);
                	m_DisplayedText = m_DisplayedText.substring(0, i + newlinesAdded - 1);
                	//m_Text.erase(i-1, sf::String::InvalidPos);
                    m_Text = m_Text.substring(0, i - 1);
                    
                    --m_Lines;

                    break;
                }
            }
        }

        if (m_SelEnd == m_Text.length()) {
            newlinesAddedBeforeSelection = newlinesAdded;
        }
        
        if (m_Scroll != null) {
            m_Scroll.setMaximum(m_Lines * m_LineHeight);

            m_TopLine = m_Scroll.getValue() / m_LineHeight + 1;

            if ((m_Scroll.getValue() % m_LineHeight) == 0) {
                m_VisibleLines = (int)Defines.TGUI_MINIMUM((m_Size.y - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_TopBorder) / m_LineHeight, m_Lines);
        	} else {
                m_VisibleLines = (int)Defines.TGUI_MINIMUM(((m_Size.y - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_TopBorder) / m_LineHeight) + 1, m_Lines);
        	}
        } else {
            m_TopLine = 1;

            m_VisibleLines = (int)Defines.TGUI_MINIMUM((m_Size.y - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_TopBorder) / m_LineHeight, m_Lines);
        }

        tempText.setString(m_DisplayedText);

        m_SelectionPointPosition = new Vector2u(tempText.findCharacterPos(m_SelEnd + newlinesAddedBeforeSelection));

        if ((m_SelEnd > 0) && 
        	(m_SelEnd + newlinesAddedBeforeSelection > 0)) {
            if ((m_Text.charAt(m_SelEnd - 1) != '\n') && 
            	(m_DisplayedText.charAt(m_SelEnd + newlinesAddedBeforeSelection - 1) == '\n')) {
                m_SelectionPointPosition = new Vector2u(tempText.findCharacterPos(m_SelEnd + newlinesAddedBeforeSelection - 1));
            }
        }

        if (m_SelectionTextsNeedUpdate) {
            updateSelectionTexts(maxLineWidth);
        }
    }

    protected void updateSelectionTexts(float maxLineWidth) {
        if (m_SelChars == 0) {
            m_TextBeforeSelection.setString(m_DisplayedText);
            m_TextSelection1.setString("");
            m_TextSelection2.setString("");
            m_TextAfterSelection1.setString("");
            m_TextAfterSelection2.setString("");

            m_MultilineSelectionRectWidth.clear();
        } else {
            int i;
            int selectionStart = (int)Defines.TGUI_MINIMUM(m_SelEnd, m_SelStart);
            int selectionEnd = (int)Defines.TGUI_MAXIMUM(m_SelEnd, m_SelStart);

            Text tempText = new Text(m_TextBeforeSelection);
            int beginChar = 0;
            int newlinesAddedBeforeSelection = 0;
            int newlinesAddedInsideSelection = 0;
            int lastNewlineBeforeSelection = 0;
            boolean newlineFoundInsideSelection = false;

            // Clear the list of selection rectangle sizes
            m_MultilineSelectionRectWidth.clear();

            for (i = 0; i < selectionStart; ++i) {
                if (m_Text.charAt(i) != '\n') {
                    tempText.setString(m_Text.substring(beginChar, i - beginChar + 1 + beginChar));

                    if (tempText.findCharacterPos(i+1).x > maxLineWidth) {
                        beginChar = i;
                        lastNewlineBeforeSelection = i;
                        ++newlinesAddedBeforeSelection;
                    }
                } else {
                    beginChar = i+1;
                    lastNewlineBeforeSelection = i;
                }
            }

            beginChar = lastNewlineBeforeSelection;

            for (i = selectionStart; i < selectionEnd; ++i) {
                if (m_Text.charAt(i) != '\n') {
                    tempText.setString(m_Text.substring(beginChar, i - beginChar + 1 + beginChar));

                    if (tempText.findCharacterPos(i+1).x > maxLineWidth) {
                        beginChar = i;
                        ++newlinesAddedInsideSelection;
                    }
                } else { 
                	beginChar = i+1;
                }
            }

            if (m_SelEnd < m_SelStart) {
                selectionStart = m_SelEnd + newlinesAddedBeforeSelection;
                selectionEnd = m_SelStart + newlinesAddedBeforeSelection + newlinesAddedInsideSelection;
            } else {
                selectionStart = m_SelStart + newlinesAddedBeforeSelection;
                selectionEnd = m_SelEnd + newlinesAddedBeforeSelection + newlinesAddedInsideSelection;
            }

            Text tempText2 = new Text(m_TextBeforeSelection);
            tempText2.setString(m_DisplayedText);

            for (i = selectionStart; i < selectionEnd; ++i) {
                if (m_DisplayedText.charAt(i) == '\n') {
                    if (newlineFoundInsideSelection == true) {
                        if (tempText2.findCharacterPos(i).x > 0) {
                            m_MultilineSelectionRectWidth.add(tempText2.findCharacterPos(i).x);
                    	} else {
                            m_MultilineSelectionRectWidth.add(2.0f);
                    	}
                    } else { 
                    	newlineFoundInsideSelection = true;
                    }
                }
            }

            m_MultilineSelectionRectWidth.add(tempText2.findCharacterPos(i).x);

            m_TextBeforeSelection.setString(m_DisplayedText.substring(0, selectionStart));

            m_TextSelection1.setString(m_DisplayedText.substring(selectionStart, m_SelChars + selectionStart));
            m_TextSelection2.setString("");

            for (i = selectionStart; i < selectionEnd; ++i) {
                if (m_DisplayedText.charAt(i) == '\n') {
                    m_TextSelection1.setString(m_DisplayedText.substring(selectionStart, i - selectionStart + selectionStart));
                    m_TextSelection2.setString(m_DisplayedText.substring(i + 1, m_SelChars + newlinesAddedInsideSelection + selectionStart - i - 1 + i + 1));
                    break;
                }
            }

            m_TextAfterSelection1.setString(m_DisplayedText.substring(selectionEnd, m_DisplayedText.length() - selectionEnd + selectionEnd));
            m_TextAfterSelection2.setString("");

            for (i = selectionEnd; i < m_DisplayedText.length(); ++i) {
                if (m_DisplayedText.charAt(i) == '\n') {
                    m_TextAfterSelection1.setString(m_DisplayedText.substring(selectionEnd, i - selectionEnd + selectionEnd));
                    m_TextAfterSelection2.setString(m_DisplayedText.substring(i + 1, m_DisplayedText.length() - i - 1 + i + 1));
                    break;
                }
            }
        }
    }

    protected void initialize(Container parent) {
        m_Parent = parent;
        setTextFont(m_Parent.getGlobalFont());
    }

    public void update() {
        if (Time.lesserThan(m_AnimationTimeElapsed, Time.milliseconds(500))) {
            return;
        }

        m_AnimationTimeElapsed = new Time();

        if (m_Visible == false) {
            return;
        }

        m_SelectionPointVisible = !m_SelectionPointVisible;

        m_PossibleDoubleClick = false;
    }

    public void draw(RenderTarget target, RenderStates states) {
        if (m_Loaded == false) {
            return;
        }

        float scaleViewX = target.getSize().x / target.getView().getSize().x;
        float scaleViewY = target.getSize().y / target.getView().getSize().y;

        Vector2f topLeftPosition = states.transform.transformPoint(
        	Vector2f.plus(
	        	Vector2f.minus(
	        		Vector2f.plus(getPosition(),
	        			new Vector2f((float)(this._WidgetBordersImpl.m_LeftBorder), (float)(this._WidgetBordersImpl.m_TopBorder))),  
	        		target.getView().getCenter()),
	        	Vector2f.devide(target.getView().getSize(), 2.f)));
        Vector2f bottomRightPosition = states.transform.transformPoint(
        	Vector2f.plus(
	        	Vector2f.minus(
	        		Vector2f.minus(
		        		Vector2f.plus(getPosition(),
		        				new Vector2f(m_Size)),
		        		new Vector2f((float)(this._WidgetBordersImpl.m_RightBorder), (float)(this._WidgetBordersImpl.m_BottomBorder))),
		        		target.getView().getCenter()),
		        Vector2f.devide(target.getView().getSize(), 2.f)));

        Transform.multiplyEqual(states.transform, getTransform());

        Transform origTransform = states.transform;

        {
            RectangleShape border = new RectangleShape(new Vector2f((float)(this._WidgetBordersImpl.m_LeftBorder), (float)(m_Size.y)));
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

        RectangleShape front = new RectangleShape(new Vector2f(
        		(float)(m_Size.x - this._WidgetBordersImpl.m_LeftBorder - this._WidgetBordersImpl.m_RightBorder), 
        		(float)(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder)));
        front.setFillColor(m_BackgroundColor);
        target.draw(front, states);

        if (m_Scroll != null) {
            states.transform.translate(2, -(float)(m_Scroll.getValue()));
        } else {
            states.transform.translate(2, 0);
        }
        
        Transform oldTransform = states.transform;

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

        target.draw(m_TextBeforeSelection, states);

        if (m_SelChars > 0) {
            int textBeforeSelectionLength = m_TextBeforeSelection.getString().length() + 1;
            int textSelection1Length = m_TextSelection1.getString().length() + 1;
            int textSelection2Length = m_TextSelection2.getString().length() + 1;

            states.transform.translate(m_TextBeforeSelection.findCharacterPos(textBeforeSelectionLength).x, m_TextBeforeSelection.findCharacterPos(textBeforeSelectionLength).y);

            if (textBeforeSelectionLength > 1) {
                states.transform.translate(
                	(float)(m_TextBeforeSelection.getFont().getKerning(m_DisplayedText.charAt(textBeforeSelectionLength - 2), 
                		m_DisplayedText.charAt(textBeforeSelectionLength - 1), 
                		m_TextSize)), 0);
            }
            
            RectangleShape selectionBackground1 = new RectangleShape(new Vector2f(
            	m_TextSelection1.findCharacterPos(textSelection1Length).x, 
            	(float)(m_LineHeight)));
            selectionBackground1.setFillColor(m_SelectedTextBgrColor);

            target.draw(selectionBackground1, states);

            target.draw(m_TextSelection1, states);

            if (m_TextSelection2.getString().length() > 0) {
                states.transform.translate(
                	-m_TextBeforeSelection.findCharacterPos(textBeforeSelectionLength).x, (float)(m_LineHeight));

                if (textBeforeSelectionLength > 1) {
                    states.transform.translate(
                    	(float)(-m_TextBeforeSelection.getFont().getKerning(
                    		m_DisplayedText.charAt(textBeforeSelectionLength - 2), 
                    		m_DisplayedText.charAt(textBeforeSelectionLength - 1), 
                    		m_TextSize)), 0);
                }
                
                RectangleShape selectionBackground2 = new RectangleShape();
                selectionBackground2.setFillColor(m_SelectedTextBgrColor);

                for (int i = 0; i < m_MultilineSelectionRectWidth.size(); ++i) {
                    selectionBackground2.setSize(new Vector2f(
                    	m_MultilineSelectionRectWidth.get(i), 
                    	(float)(m_LineHeight)));
                    target.draw(selectionBackground2, states);
                    selectionBackground2.move(0, (float)(m_LineHeight));
                }

                target.draw(m_TextSelection2, states);

                states.transform.translate(m_TextSelection2.findCharacterPos(textSelection2Length));

                if (m_DisplayedText.length() > textBeforeSelectionLength + textSelection1Length + textSelection2Length - 2) {
                    states.transform.translate(
                    	(float)(m_TextBeforeSelection.getFont().getKerning(
                    		m_DisplayedText.charAt(textBeforeSelectionLength + textSelection1Length + textSelection2Length - 3), 
                    		m_DisplayedText.charAt(textBeforeSelectionLength + textSelection1Length + textSelection2Length - 2), 
                    		m_TextSize)), 0);
                }
            } else {
                states.transform.translate(m_TextSelection1.findCharacterPos(textSelection1Length).x, 0);

                if ((m_DisplayedText.length() > textBeforeSelectionLength + textSelection1Length - 2) && (textBeforeSelectionLength + textSelection1Length > 2)) {
                    states.transform.translate(
                    	(float)(m_TextBeforeSelection.getFont().getKerning(
                    		m_DisplayedText.charAt(textBeforeSelectionLength + textSelection1Length - 3), 
                    		m_DisplayedText.charAt(textBeforeSelectionLength + textSelection1Length - 2), 
                    		m_TextSize)), 0);
                }
            }

            target.draw(m_TextAfterSelection1, states);

            if (m_TextAfterSelection2.getString().length() > 0) {
                if (m_TextSelection2.getString().length() > 0) {
                    states.transform.translate(-m_TextSelection2.findCharacterPos(textSelection2Length).x, (float)(m_LineHeight));

                    if (m_DisplayedText.length() > textBeforeSelectionLength + textSelection1Length + textSelection2Length - 2) {
                        states.transform.translate((float)(-m_TextBeforeSelection.getFont().getKerning(
                        	m_DisplayedText.charAt(textBeforeSelectionLength + textSelection1Length + textSelection2Length - 3), 
                        	m_DisplayedText.charAt(textBeforeSelectionLength + textSelection1Length + textSelection2Length - 2), 
                        	m_TextSize)), 0);
                    }
                } else {
                    states.transform.translate(-m_TextSelection1.findCharacterPos(textSelection1Length).x - m_TextBeforeSelection.findCharacterPos(textBeforeSelectionLength).x, (float)(m_LineHeight));

                    if (textBeforeSelectionLength > 1) {
                        states.transform.translate((float)(-m_TextBeforeSelection.getFont().getKerning(
                        	m_DisplayedText.charAt(textBeforeSelectionLength - 2), 
                        	m_DisplayedText.charAt(textBeforeSelectionLength - 1), 
                        	m_TextSize)), 0);
                    }
                    
                    if ((m_DisplayedText.length() > textBeforeSelectionLength + textSelection1Length - 2) && (textBeforeSelectionLength + textSelection1Length > 2)) {
                        states.transform.translate(
                        	(float)(-m_TextBeforeSelection.getFont().getKerning(
                        		m_DisplayedText.charAt(textBeforeSelectionLength + textSelection1Length - 3), 
                        		m_DisplayedText.charAt(textBeforeSelectionLength + textSelection1Length - 2), 
                        		m_TextSize)), 0);
                    }
                }

                target.draw(m_TextAfterSelection2, states);
            }
        }

        if (m_SelectionPointWidth > 0) {
            if ((m_Focused) && (m_SelectionPointVisible)) {
                states.transform = oldTransform;

                RectangleShape selectionPoint = new RectangleShape(
                		new Vector2f((float)(m_SelectionPointWidth), (float)(m_LineHeight)));
                selectionPoint.setPosition(m_SelectionPointPosition.x - (m_SelectionPointWidth * 0.5f), (float)(m_SelectionPointPosition.y));
                selectionPoint.setFillColor(m_SelectionPointColor);

                target.draw(selectionPoint, states);
            }
        }

        GL.glScissor(scissor[0], scissor[1], scissor[2], scissor[3]);

        if (m_Scroll != null) {
            states.transform = origTransform;
            states.transform.translate(
            	m_Size.x - this._WidgetBordersImpl.m_RightBorder - m_Scroll.getSize().x, 
            	(float)(this._WidgetBordersImpl.m_TopBorder));

            target.draw(m_Scroll, states);
        }
    }

	@Override
	public void setBorders(int leftBorder, int topBorder,
			int rightBorder, int bottomBorder) {
        if (m_Loaded == false) {
            return;
        }

        if (m_LineHeight == 0) {
            return;
        }

        this._WidgetBordersImpl.m_LeftBorder = leftBorder;
        this._WidgetBordersImpl.m_TopBorder = topBorder;
        this._WidgetBordersImpl.m_RightBorder = rightBorder;
        this._WidgetBordersImpl.m_BottomBorder = bottomBorder;

        if (m_Size.x < (50 + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder)) {
            m_Size.x = 50 + this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder;
        }

        if (m_Scroll == null) {
            if (m_Text.length() > 0) {
                if (m_Size.y < ((m_Lines * m_LineHeight) - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder)) {
                    m_Size.y = (m_Lines * m_LineHeight) - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder;
                }
            } else {
                if (m_Size.y < (m_LineHeight - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder)) {
                    m_Size.y = m_LineHeight - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder;
                }
            }
        }

        if (m_Scroll != null) {
            m_Scroll.setLowValue(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder);
            m_Scroll.setSize(m_Scroll.getSize().x, (float)(m_Size.y - this._WidgetBordersImpl.m_TopBorder - this._WidgetBordersImpl.m_BottomBorder));
        }

        m_SelectionTextsNeedUpdate = true;
        updateDisplayedText();
	}

	@Override
	public boolean mouseOnWidget(float x, float y) {
        if (m_Loaded == false) {
            return false;
        }

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
            if (m_MouseHover) {
                mouseLeftWidget();
            }
            
            m_MouseHover = false;
            return false;
        }
	}
	
	
}
