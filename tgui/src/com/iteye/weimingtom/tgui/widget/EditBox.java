package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.Borders;
import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.GL;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.Texture;
import com.iteye.weimingtom.tgui.WidgetBorders;
import com.iteye.weimingtom.tgui.WidgetBordersImpl;
import com.iteye.weimingtom.tgui.WidgetPhase;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.IntRect;
import com.iteye.weimingtom.tgui.sf.Keyboard;
import com.iteye.weimingtom.tgui.sf.RectangleShape;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Text;
import com.iteye.weimingtom.tgui.sf.Time;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20151024
 * @author Administrator
 *
 */
public class EditBox extends ClickableWidget implements WidgetBorders {
	public WidgetBordersImpl _WidgetBordersImpl = new WidgetBordersImpl();
    //FIXME:
	public static enum EditBoxCallbacks {
	    TextChanged(ClickableWidget.ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 1),  
	    ReturnKeyPressed(ClickableWidget.ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 2),
	    AllEditBoxCallbacks(ClickableWidget.ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 4 - 1),
	    EditBoxCallbacksCount(ClickableWidget.ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 4);
	    
        int value;
        
        EditBoxCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
	}
	
	public static enum Alignments {
        Left,
        Center,
        Right
    }
	
	protected String m_LoadedConfigFile = "";
	
	protected boolean m_SelectionPointVisible;
	
	protected boolean m_LimitTextWidth;
	
	protected String m_DisplayedText = "";
	protected String m_Text = "";
	
	protected int m_TextSize;
	
	protected Alignments m_TextAlignment;
	
	protected int m_SelChars;
	protected int m_SelStart;
	protected int m_SelEnd;
	
	protected char m_PasswordChar;
	
	protected int m_MaxChars;
	
	protected boolean m_SplitImage;
	
	protected int m_TextCropPosition;
	
	protected RectangleShape m_SelectedTextBackground = new RectangleShape();
	
	protected RectangleShape m_SelectionPoint = new RectangleShape();
	
	protected Text m_TextBeforeSelection = new Text();
	protected Text m_TextSelection = new Text();
	protected Text m_TextAfterSelection = new Text();
	protected Text m_TextFull = new Text();
	
	protected Texture m_TextureNormal_L = new Texture();
	protected Texture m_TextureNormal_M = new Texture();
	protected Texture m_TextureNormal_R = new Texture();
	
	protected Texture m_TextureHover_L = new Texture();
	protected Texture m_TextureHover_M = new Texture();
	protected Texture m_TextureHover_R = new Texture();
	
	protected Texture m_TextureFocused_L = new Texture();
	protected Texture m_TextureFocused_M = new Texture();
	protected Texture m_TextureFocused_R = new Texture();
	
	protected boolean m_PossibleDoubleClick;
	
	protected boolean m_NumbersOnly;
	protected boolean m_SeparateHoverImage;
	
	
	
	public EditBox() {
	    m_SelectionPointVisible = true;
	    m_LimitTextWidth = false;
	    m_DisplayedText = "";
	    m_Text = "";
	    m_TextSize = 0;
	    m_TextAlignment = Alignments.Left;
	    m_SelChars = 0;
	    m_SelStart = 0;
	    m_SelEnd = 0;
	    m_PasswordChar = '\0';
	    m_MaxChars = 0;
	    m_SplitImage = false;
	    m_TextCropPosition = 0;
	    m_PossibleDoubleClick = false;
	    m_NumbersOnly = false;
	    m_SeparateHoverImage = false;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_EditBox;
        m_AnimatedWidget = true;
        m_DraggableWidget = true;
        m_AllowFocus = true;

        m_SelectionPoint.setSize(new Vector2f(1, 0));

        changeColors();
	}
	
	public EditBox(EditBox copy) {
	    //ClickableWidget
	    super(copy);
	    //WidgetBorders           (copy),
	    m_LoadedConfigFile = copy.m_LoadedConfigFile;
	    m_SelectionPointVisible = copy.m_SelectionPointVisible;
	    m_LimitTextWidth = copy.m_LimitTextWidth;
	    m_DisplayedText = copy.m_DisplayedText;
	    m_Text = copy.m_Text;
	    m_TextSize = copy.m_TextSize;
	    m_TextAlignment = copy.m_TextAlignment;
	    m_SelChars = copy.m_SelChars;
	    m_SelStart = copy.m_SelStart;
	    m_SelEnd = copy.m_SelEnd;
	    m_PasswordChar = copy.m_PasswordChar;
	    m_MaxChars = copy.m_MaxChars;
	    m_SplitImage = copy.m_SplitImage;
	    m_TextCropPosition = copy.m_TextCropPosition;
	    m_SelectedTextBackground = copy.m_SelectedTextBackground;
	    m_SelectionPoint = copy.m_SelectionPoint;
	    m_TextBeforeSelection = copy.m_TextBeforeSelection;
	    m_TextSelection = copy.m_TextSelection;
	    m_TextAfterSelection = copy.m_TextAfterSelection;
	    m_TextFull = copy.m_TextFull;
	    m_PossibleDoubleClick = copy.m_PossibleDoubleClick;
	    m_NumbersOnly = copy.m_NumbersOnly;
	    m_SeparateHoverImage = copy.m_SeparateHoverImage;
	    
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureNormal_L, m_TextureNormal_L);
	    Defines.TGUI_TextureManager.copyTexture(copy.m_TextureNormal_M, m_TextureNormal_M);
	    Defines.TGUI_TextureManager.copyTexture(copy.m_TextureNormal_R, m_TextureNormal_R);
	    Defines.TGUI_TextureManager.copyTexture(copy.m_TextureHover_L, m_TextureHover_L);
	    Defines.TGUI_TextureManager.copyTexture(copy.m_TextureHover_M, m_TextureHover_M);
	    Defines.TGUI_TextureManager.copyTexture(copy.m_TextureHover_R, m_TextureHover_R);
	    Defines.TGUI_TextureManager.copyTexture(copy.m_TextureFocused_L, m_TextureFocused_L);
	    Defines.TGUI_TextureManager.copyTexture(copy.m_TextureFocused_M, m_TextureFocused_M);
	    Defines.TGUI_TextureManager.copyTexture(copy.m_TextureFocused_R, m_TextureFocused_R);
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

        if (m_TextureHover_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_L);
        }
        if (m_TextureHover_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_M);
        }
        if (m_TextureHover_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_R);
        }

        if (m_TextureFocused_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_L);
        }
        if (m_TextureFocused_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_M);
        }
        if (m_TextureFocused_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_R);
        }
	}
	
	public EditBox assign(EditBox right) {
		if (this != right) {
            EditBox temp = new EditBox(right);
            //this->ClickableWidget::operator=(right);
            //this->WidgetBorders::operator=(right);
            super.assign(right);
            
            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_SelectionPointVisible = temp.m_SelectionPointVisible;
            m_LimitTextWidth = temp.m_LimitTextWidth;
            m_DisplayedText = temp.m_DisplayedText;
            m_Text = temp.m_Text;
            m_TextSize = temp.m_TextSize;
            m_TextAlignment = temp.m_TextAlignment;
            m_SelChars = temp.m_SelChars;
            m_SelStart = temp.m_SelStart;
            m_SelEnd = temp.m_SelEnd;
            m_PasswordChar = temp.m_PasswordChar;
            m_MaxChars = temp.m_MaxChars;
            m_SplitImage = temp.m_SplitImage;
            m_TextCropPosition = temp.m_TextCropPosition;
            m_SelectedTextBackground = temp.m_SelectedTextBackground;
            m_SelectionPoint = temp.m_SelectionPoint;
            m_TextBeforeSelection = temp.m_TextBeforeSelection;
            m_TextSelection = temp.m_TextSelection;
            m_TextAfterSelection = temp.m_TextAfterSelection;
            m_TextFull = temp.m_TextFull;
            m_TextureNormal_L = temp.m_TextureNormal_L;
            m_TextureNormal_M = temp.m_TextureNormal_M;
            m_TextureNormal_R = temp.m_TextureNormal_R;
            m_TextureHover_L = temp.m_TextureHover_L;
            m_TextureHover_M = temp.m_TextureHover_M;
            m_TextureHover_R = temp.m_TextureHover_R;
            m_TextureFocused_L = temp.m_TextureFocused_L;
            m_TextureFocused_M = temp.m_TextureFocused_M;
            m_TextureFocused_R = temp.m_TextureFocused_R;
            m_PossibleDoubleClick = temp.m_PossibleDoubleClick;
            m_NumbersOnly = temp.m_NumbersOnly;
            m_SeparateHoverImage = temp.m_SeparateHoverImage;
        }

        return this;
	}
	
	public EditBox cloneObj() {
        return new EditBox(this);
	}
	
	public boolean load(String configFileFilename) {
		m_LoadedConfigFile = configFileFilename;

        m_Loaded = false;
        m_Size.x = 0;
        m_Size.y = 0;

        if (m_TextureNormal_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureNormal_L);
        }
        if (m_TextureNormal_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureNormal_M);
        }
        if (m_TextureNormal_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureNormal_R);
        }
        if (m_TextureHover_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_L);
        }
        if (m_TextureHover_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_M);
        }
        if (m_TextureHover_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover_R);
        }
        if (m_TextureFocused_L.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_L);
        }
        if (m_TextureFocused_M.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_M);
        }
        if (m_TextureFocused_R.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused_R);
        }

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
        	Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("EditBox", properties, values)) {
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
            } else if ("textcolor".equals(property)) {
                Color color = Defines.extractColor(value);
                m_TextBeforeSelection.setColor(color);
                m_TextAfterSelection.setColor(color);
            } else if ("selectedtextcolor".equals(property)) {
                m_TextSelection.setColor(Defines.extractColor(value));
            } else if ("selectedtextbackgroundcolor".equals(property)) {
                m_SelectedTextBackground.setFillColor(Defines.extractColor(value));
            } else if ("selectionpointcolor".equals(property)) {
                m_SelectionPoint.setFillColor(Defines.extractColor(value));
            } else if ("selectionpointwidth".equals(property)) {
                m_SelectionPoint.setSize(
                	new Vector2f(
                		(float)(Float.parseFloat(value)), 
                		m_SelectionPoint.getSize().y));
            } else if ("borders".equals(property)) {
                Borders borders = new Borders() {
                	
                };
                if (Defines.extractBorders(value, borders)) {
                    setBorders(borders.left, borders.top, borders.right, borders.bottom);
                }
            } else if ("normalimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage in section EditBox in " + configFileFilename + ".");
                    return false;
                }

                m_SplitImage = false;
            } else if ("hoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureHover_M)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for HoverImage in section EditBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("focusedimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFocused_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FocusedImage in section EditBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("normalimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_L)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage_L in section EditBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("normalimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage_M in section EditBox in " + configFileFilename + ".");
                    return false;
                }

                m_SplitImage = true;
            } else if ("normalimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureNormal_R)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for NormalImage_R in section EditBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("hoverimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureHover_L)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for HoverImage_L in section EditBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("hoverimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureHover_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for HoverImage_M in section EditBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("hoverimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureHover_R)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for HoverImage_R in section EditBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("focusedimage_l".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFocused_L)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FocusedImage_L in section EditBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("focusedimage_m".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFocused_M)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FocusedImage_M in section EditBox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("focusedimage_r".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFocused_R)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FocusedImage_R in section EditBox in " + configFileFilename + ".");
                    return false;
                }
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Unrecognized property '" + property + "' in section EditBox in " + configFileFilename + ".");
            }
        }

        if (m_SplitImage) {
            if ((m_TextureNormal_L.data != null) && 
            	(m_TextureNormal_M.data != null) && 
            	(m_TextureNormal_R.data != null)) {
                m_Loaded = true;
                setSize((float)(m_TextureNormal_L.getSize().x + m_TextureNormal_M.getSize().x + m_TextureNormal_R.getSize().x),
                        (float)(m_TextureNormal_M.getSize().y));

                m_TextureNormal_M.data.texture.setRepeated(true);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the edit box. Is the EditBox section in " + configFileFilename + " complete?");
                return false;
            }

            if ((m_TextureFocused_L.data != null) && 
            	(m_TextureFocused_M.data != null) && 
            	(m_TextureFocused_R.data != null)) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Focused.value();

                m_TextureFocused_M.data.texture.setRepeated(true);
            }
            if ((m_TextureHover_L.data != null) && 
            	(m_TextureHover_M.data != null) && 
            	(m_TextureHover_R.data != null)) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();

                m_TextureHover_M.data.texture.setRepeated(true);
            }
        } else {
            if (m_TextureNormal_M.data != null) {
                m_Loaded = true;
                setSize((float)(m_TextureNormal_M.getSize().x), (float)(m_TextureNormal_M.getSize().y));
            } else {
                Defines.TGUI_OUTPUT("TGUI error: NormalImage wasn't loaded. Is the EditBox section in " + configFileFilename + " complete?");
                return false;
            }

            if (m_TextureFocused_M.data != null) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Focused.value();
            }
            if (m_TextureHover_M.data != null) {
                m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();
            }
        }

        setTextSize(0);

        return true;
	}
	
	public String getLoadedConfigFile() {
        return m_LoadedConfigFile;
	}
	
	public void setPosition(float x, float y) {
        //Transformable::
        super.setPosition(x, y);

        if (m_SplitImage) {
            m_TextureHover_L.sprite.setPosition(x, y);
            m_TextureNormal_L.sprite.setPosition(x, y);
            m_TextureFocused_L.sprite.setPosition(x, y);

            if ((m_TextureNormal_M.sprite.getScale().y * (m_TextureNormal_L.getSize().x + m_TextureNormal_R.getSize().x)) < m_Size.x) {
                float scalingY = m_Size.y / m_TextureNormal_M.getSize().y;

                m_TextureHover_M.sprite.setPosition(x + (m_TextureHover_L.getSize().x * m_TextureHover_L.sprite.getScale().x), y);
                m_TextureNormal_M.sprite.setPosition(x + (m_TextureNormal_L.getSize().x * m_TextureNormal_L.sprite.getScale().x), y);
                m_TextureFocused_M.sprite.setPosition(x + (m_TextureFocused_L.getSize().x * m_TextureFocused_L.sprite.getScale().x), y);

                m_TextureHover_R.sprite.setPosition(m_TextureHover_M.sprite.getPosition().x + (m_TextureHover_M.sprite.getTextureRect().width * scalingY), y);
                m_TextureNormal_R.sprite.setPosition(m_TextureNormal_M.sprite.getPosition().x + (m_TextureNormal_M.sprite.getTextureRect().width * scalingY), y);
                m_TextureFocused_R.sprite.setPosition(m_TextureFocused_M.sprite.getPosition().x + (m_TextureFocused_M.sprite.getTextureRect().width * scalingY), y);
            } else {
                m_TextureHover_R.sprite.setPosition(x + (m_TextureHover_L.getSize().x * m_TextureHover_L.sprite.getScale().x), y);
                m_TextureNormal_R.sprite.setPosition(x + (m_TextureNormal_L.getSize().x * m_TextureNormal_L.sprite.getScale().x), y);
                m_TextureFocused_R.sprite.setPosition(x + (m_TextureFocused_L.getSize().x * m_TextureFocused_L.sprite.getScale().x), y);
            }
        } else {
            m_TextureHover_M.sprite.setPosition(x, y);
            m_TextureNormal_M.sprite.setPosition(x, y);
            m_TextureFocused_M.sprite.setPosition(x, y);
        }

        recalculateTextPositions();
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

        if (m_TextSize == 0) {
            setText(m_Text);
        }

        if (m_SplitImage) {
            float scalingY = m_Size.y / m_TextureNormal_M.getSize().y;
            float minimumWidth = (m_TextureNormal_L.getSize().x + m_TextureNormal_R.getSize().x) * scalingY;

            if (m_Size.x < minimumWidth) {
                m_Size.x = minimumWidth;
            }

            m_TextureHover_L.sprite.setScale(scalingY, scalingY);
            m_TextureNormal_L.sprite.setScale(scalingY, scalingY);
            m_TextureFocused_L.sprite.setScale(scalingY, scalingY);

            m_TextureHover_M.sprite.setTextureRect(new IntRect(0, 0, (int)((m_Size.x - minimumWidth) / scalingY), m_TextureHover_M.getSize().y));
            m_TextureNormal_M.sprite.setTextureRect(new IntRect(0, 0, (int)((m_Size.x - minimumWidth) / scalingY), m_TextureNormal_M.getSize().y));
            m_TextureFocused_M.sprite.setTextureRect(new IntRect(0, 0, (int)((m_Size.x - minimumWidth) / scalingY), m_TextureFocused_M.getSize().y));

            m_TextureHover_M.sprite.setScale(scalingY, scalingY);
            m_TextureNormal_M.sprite.setScale(scalingY, scalingY);
            m_TextureFocused_M.sprite.setScale(scalingY, scalingY);

            m_TextureHover_R.sprite.setScale(scalingY, scalingY);
            m_TextureNormal_R.sprite.setScale(scalingY, scalingY);
            m_TextureFocused_R.sprite.setScale(scalingY, scalingY);
        } else {
            m_TextureHover_M.sprite.setScale(m_Size.x / m_TextureHover_M.getSize().x, m_Size.y / m_TextureHover_M.getSize().y);
            m_TextureNormal_M.sprite.setScale(m_Size.x / m_TextureNormal_M.getSize().x, m_Size.y / m_TextureNormal_M.getSize().y);
            m_TextureFocused_M.sprite.setScale(m_Size.x / m_TextureFocused_M.getSize().x, m_Size.y / m_TextureFocused_M.getSize().y);
        }

        m_SelectionPoint.setSize(new Vector2f((float)(m_SelectionPoint.getSize().x),
                                              m_Size.y - ((this._WidgetBordersImpl.m_BottomBorder + this._WidgetBordersImpl.m_TopBorder) * (m_Size.y / m_TextureNormal_M.getSize().y))));

        setPosition(getPosition());
	}
	
	public void setText(String text) {
        if (m_Loaded == false) {
            return;
        }

        if (m_TextSize == 0) {
            m_TextFull.setString("kg");
            m_TextFull.setCharacterSize((int)(m_Size.y - ((this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder) * (m_Size.y / m_TextureNormal_M.getSize().y))));
            m_TextFull.setCharacterSize((int)(m_TextFull.getCharacterSize() - m_TextFull.getLocalBounds().top));
            m_TextFull.setString(m_DisplayedText);

            m_TextBeforeSelection.setCharacterSize(m_TextFull.getCharacterSize());
            m_TextSelection.setCharacterSize(m_TextFull.getCharacterSize());
            m_TextAfterSelection.setCharacterSize(m_TextFull.getCharacterSize());
        } else {
            m_TextBeforeSelection.setCharacterSize(m_TextSize);
            m_TextSelection.setCharacterSize(m_TextSize);
            m_TextAfterSelection.setCharacterSize(m_TextSize);
            m_TextFull.setCharacterSize(m_TextSize);
        }

        m_Text = text;
        m_DisplayedText = text;

        if (m_NumbersOnly) {
            setNumbersOnly(true);
        }
        
        if ((m_MaxChars > 0) && (m_DisplayedText.length() > m_MaxChars)) {
            //m_Text.erase(m_MaxChars, sf::String::InvalidPos);
        	m_Text = m_Text.substring(0, m_MaxChars);
        	//m_DisplayedText.erase(m_MaxChars, sf::String::InvalidPos);
        	m_DisplayedText = m_DisplayedText.substring(m_MaxChars);
        }

        if (m_PasswordChar != '\0') {
            String str = "";
        	for (int i = 0; i < m_Text.length(); ++i) {
                //m_DisplayedText[i] = m_PasswordChar;
        		str += m_PasswordChar;
            }
        	//FIXME:
            m_DisplayedText = str + m_DisplayedText.substring(m_Text.length());
        }

        m_TextBeforeSelection.setString(m_DisplayedText);
        m_TextSelection.setString("");
        m_TextAfterSelection.setString("");
        m_TextFull.setString(m_DisplayedText);

        float width;
        if (m_SplitImage) {
            width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.y / m_TextureNormal_M.getSize().y));
		} else {
            width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.x / m_TextureNormal_M.getSize().x));
		}
	
        if (width < 0) {
            width = 0;
        }

        if (m_LimitTextWidth) {
            while (m_TextBeforeSelection.findCharacterPos(m_TextBeforeSelection.getString().length()).x > width) {
                //m_Text.erase(m_Text.getSize() - 1);
            	m_Text = m_Text.substring(0, m_Text.length() - 1) + 
            		m_Text.substring(m_Text.length());
            	//m_DisplayedText.erase(m_DisplayedText.getSize()-1);
            	m_DisplayedText = m_DisplayedText.substring(0, m_DisplayedText.length() - 1) + 
            		m_DisplayedText.substring(m_DisplayedText.length());
            	
                m_TextBeforeSelection.setString(m_DisplayedText);
            }

            m_TextFull.setString(m_DisplayedText);
        } else {
            float textWidth = m_TextFull.findCharacterPos(m_DisplayedText.length()).x;

            if (textWidth > width) {
                if (textWidth - m_TextCropPosition < width) {
                    m_TextCropPosition = (int)(textWidth - width);
                }
            } else {
                m_TextCropPosition = 0;
            }
        }

        setSelectionPointPosition(m_DisplayedText.length());
	}
	
	public String getText() {
        return m_Text;
	}
	
	public void setTextSize(int size) {
        m_TextSize = size;

        setText(m_Text);
	}
	
	public int getTextSize() {
        return m_TextFull.getCharacterSize();
	}
	
	public void setTextFont(Font font) {
        m_TextBeforeSelection.setFont(font);
        m_TextSelection.setFont(font);
        m_TextAfterSelection.setFont(font);
        m_TextFull.setFont(font);

        recalculateTextPositions();
	}
	
	public Font getTextFont() {
        return m_TextFull.getFont();
	}
	
	public void setPasswordCharacter(char passwordChar) {
        if (m_Loaded == false) {
            return;
        }
        
        m_PasswordChar = passwordChar;

        setText(m_Text);
	}
	
	public char getPasswordCharacter() {
        return m_PasswordChar;
	}
	
	public void setMaximumCharacters(int maxChars) {
        m_MaxChars = maxChars;

        if ((m_MaxChars > 0) && (m_DisplayedText.length() > m_MaxChars)) {
            //m_Text.erase(m_MaxChars, sf::String::InvalidPos);
        	m_Text = m_Text.substring(0, m_MaxChars);
        	//m_DisplayedText.erase(m_MaxChars, sf::String::InvalidPos);
        	m_DisplayedText = m_DisplayedText.substring(0, m_MaxChars);
        	
            m_TextBeforeSelection.setString(m_DisplayedText);
            m_TextSelection.setString("");
            m_TextAfterSelection.setString("");
            m_TextFull.setString(m_DisplayedText);

            setSelectionPointPosition(m_DisplayedText.length());
        }
	}
	
	public int getMaximumCharacters() {
        return m_MaxChars;
	}
	
	public void changeColors() {
		changeColors(new Color(0, 0, 0),
	        new Color(255, 255, 255),
	        new Color( 10, 110, 255),
	        new Color(110, 110, 255));
	}
	
	public void changeColors(Color color,
	                  		 Color selectedColor,
	                  		 Color selectedBgrColor,
	                  		 Color selectionPointColor) {
        m_TextBeforeSelection.setColor(color);
        m_TextSelection.setColor(selectedColor);
        m_TextAfterSelection.setColor(color);

        m_SelectionPoint.setFillColor(selectionPointColor);
        m_SelectedTextBackground.setFillColor(selectedBgrColor);
	}
	
	public void setTextColor(Color textColor) {
        m_TextBeforeSelection.setColor(textColor);
        m_TextAfterSelection.setColor(textColor);
	}
	
	public void setSelectedTextColor(Color selectedTextColor) {
        m_TextSelection.setColor(selectedTextColor);
	}
	
	public void setSelectedTextBackgroundColor(Color selectedTextBackgroundColor) {
        m_SelectedTextBackground.setFillColor(selectedTextBackgroundColor);
	}
	
	public void setSelectionPointColor(Color selectionPointColor) {
        m_SelectionPoint.setFillColor(selectionPointColor);
	}
	
	public Color getTextColor() {
        return m_TextBeforeSelection.getColor();
	}
	
	public Color getSelectedTextColor() {
        return m_TextSelection.getColor();
	}
	
	public Color getSelectedTextBackgroundColor() {
        return m_SelectedTextBackground.getFillColor();
	}
	
	public Color getSelectionPointColor() {
        return m_SelectionPoint.getFillColor();
	}
	
	public void limitTextWidth(boolean limitWidth) {
        m_LimitTextWidth = limitWidth;

        if (m_LimitTextWidth == true) {
            float width;
            if (m_SplitImage) {
                width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.y / m_TextureNormal_M.getSize().y));
        	} else {
                width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.x / m_TextureNormal_M.getSize().x));
            }
            
            if (width < 0) {
                width = 0;
            }

            while (m_TextBeforeSelection.findCharacterPos(m_DisplayedText.length()).x > width) {
                //m_Text.erase(m_Text.getSize() - 1);
            	m_Text = m_Text.substring(0, m_Text.length() - 1);
            	//m_DisplayedText.erase(m_DisplayedText.getSize() - 1);
            	m_DisplayedText = m_DisplayedText.substring(0, m_DisplayedText.length() - 1);
                m_TextBeforeSelection.setString(m_DisplayedText);
            }

            m_TextFull.setString(m_DisplayedText);

            m_TextCropPosition = 0;

            if (m_SelEnd > m_DisplayedText.length()) {
                setSelectionPointPosition(m_SelEnd);
            }
        }
	}
	
	public void setSelectionPointPosition(int charactersBeforeSelectionPoint) {
        if (charactersBeforeSelectionPoint > m_Text.length()) {
            charactersBeforeSelectionPoint = m_Text.length();
        }

        m_SelChars = 0;
        m_SelStart = charactersBeforeSelectionPoint;
        m_SelEnd = charactersBeforeSelectionPoint;

        m_TextBeforeSelection.setString(m_DisplayedText);
        m_TextSelection.setString("");
        m_TextAfterSelection.setString("");
        m_TextFull.setString(m_DisplayedText);

        if (m_LimitTextWidth == false) {
            float width;
            if (m_SplitImage) {
                width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.y / m_TextureNormal_M.getSize().y));
        	} else {
                width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.x / m_TextureNormal_M.getSize().x));
        	}
        
            if (width < 0) {
                width = 0;
            }

            // Find out the position of the selection point
            float selectionPointPosition = m_TextFull.findCharacterPos(m_SelEnd).x;

            if (m_SelEnd == m_DisplayedText.length()) {
                selectionPointPosition += m_TextFull.getCharacterSize() / 10.f;
            }
            
            if (m_TextCropPosition + width < selectionPointPosition) {
                m_TextCropPosition = (int)(selectionPointPosition - width);
            }
            
            if (m_TextCropPosition > selectionPointPosition) {
                m_TextCropPosition = (int)(selectionPointPosition);
            }
        }

        recalculateTextPositions();
	}
	
	public void setSelectionPointWidth(int width) {
        m_SelectionPoint.setPosition(m_SelectionPoint.getPosition().x + ((m_SelectionPoint.getSize().x - width) / 2.0f), m_SelectionPoint.getPosition().y);
        m_SelectionPoint.setSize(new Vector2f(
        	(float)(width),
            m_Size.y - ((this._WidgetBordersImpl.m_BottomBorder + this._WidgetBordersImpl.m_TopBorder) * (m_Size.y / m_TextureNormal_M.getSize().y))));
        
	}
	
	public int getSelectionPointWidth() {
        return (int)(m_SelectionPoint.getSize().x);
	}
	
	public void setNumbersOnly(boolean numbersOnly) {
        m_NumbersOnly = numbersOnly;

        if (numbersOnly && !m_Text.isEmpty()) {
            String newText = "";
            boolean commaFound = false;

            if ((m_Text.charAt(0) == '+') || (m_Text.charAt(0) == '-')) {
                newText += m_Text.charAt(0);
            }

            for (int i = 0; i < m_Text.length(); ++i) {
                if (!commaFound) {
                    if ((m_Text.charAt(i) == ',') || 
                    	(m_Text.charAt(i) == '.')) {
                        newText += m_Text.charAt(i);
                        commaFound = true;
                    }
                }

                if ((m_Text.charAt(i) >= '0') && (m_Text.charAt(i) <= '9')) {
                    newText += m_Text.charAt(i);
                }
            }

            if (newText != m_Text) {
                setText(newText);
            }
        }
	}
	
	public void setTransparency(int transparency) {
        //ClickableWidget::
        super.setTransparency(transparency);

        if (m_SplitImage) {
            m_TextureNormal_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
            m_TextureHover_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));
            m_TextureFocused_L.sprite.setColor(new Color(255, 255, 255, m_Opacity));

            m_TextureNormal_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
            m_TextureHover_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
            m_TextureFocused_R.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        }

        m_TextureNormal_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureHover_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureFocused_M.sprite.setColor(new Color(255, 255, 255, m_Opacity));
	}
	
	public void leftMousePressed(float x, float y) {
	    float width;
        if (m_SplitImage) {
            width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.y / m_TextureNormal_M.getSize().y));
        } else {
            width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.x / m_TextureNormal_M.getSize().x));
        }
        
        if (width < 0) {
            width = 0;
        }

        float positionX;
        if (m_SplitImage) {
            positionX = x - getPosition().x - (this._WidgetBordersImpl.m_LeftBorder * (m_Size.y / m_TextureNormal_M.getSize().y));
        } else {
            positionX = x - getPosition().x - (this._WidgetBordersImpl.m_LeftBorder * (m_Size.x / m_TextureNormal_M.getSize().x));
        }
        
        int selectionPointPosition = findSelectionPointPosition(positionX);

        if ((positionX < 0) && (selectionPointPosition > 0)) {
            --selectionPointPosition;
        } else if ((positionX > width) && (selectionPointPosition < m_DisplayedText.length())) {
            ++selectionPointPosition;
        }

        if ((m_PossibleDoubleClick) && 
        	(m_SelChars == 0) && 
        	(selectionPointPosition == m_SelEnd)) {
            m_PossibleDoubleClick = false;

            setSelectionPointPosition(m_DisplayedText.length());

            m_SelStart = 0;
            m_SelEnd = m_Text.length();
            m_SelChars = m_Text.length();

            m_TextBeforeSelection.setString("");
            m_TextSelection.setString(m_DisplayedText);
            m_TextAfterSelection.setString("");
        } else {
            setSelectionPointPosition(selectionPointPosition);

            m_PossibleDoubleClick = true;
        }

        m_MouseDown = true;

        if (this.getCallbackManager().m_CallbackFunctions.get(ClickableWidgetCallbacks.LeftMousePressed.value()).isEmpty() == false) {
        	this.getCallbackManager().m_Callback.trigger = ClickableWidgetCallbacks.LeftMousePressed.value();
        	this.getCallbackManager().m_Callback.mouse.x = (int)(x - getPosition().x);
        	this.getCallbackManager().m_Callback.mouse.y = (int)(y - getPosition().y);
            addCallback();
        }

        recalculateTextPositions();

        m_SelectionPointVisible = true;
        m_AnimationTimeElapsed = new Time();
	}
	
	public void mouseMoved(float x, float y) {
        if (m_MouseHover == false) {
            mouseEnteredWidget();
        }

        m_MouseHover = true;

        m_PossibleDoubleClick = false;

        if (m_MouseDown) {
            if (m_LimitTextWidth) {
                if (m_SplitImage) {
                    m_SelEnd = findSelectionPointPosition(x - getPosition().x - (this._WidgetBordersImpl.m_LeftBorder * (m_Size.y / m_TextureNormal_M.getSize().y)));
                } else {
                    m_SelEnd = findSelectionPointPosition(x - getPosition().x - (this._WidgetBordersImpl.m_LeftBorder * (m_Size.x / m_TextureNormal_M.getSize().x)));
                }
            } else {
                float scalingX;
                if (m_SplitImage) {
                    scalingX = m_Size.y / m_TextureNormal_M.getSize().y;
                } else {
                    scalingX = m_Size.x / m_TextureNormal_M.getSize().x;
                }
                
                float width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * scalingX);

                if (width < 0) {
                    width = 0;
                }

                if (x - getPosition().x < this._WidgetBordersImpl.m_LeftBorder * scalingX) {
                    if (m_TextFull.getCharacterSize() > 10) {
                        if (m_TextCropPosition > m_TextFull.getCharacterSize() / 10) {
                            m_TextCropPosition -= (int)(Math.floor(m_TextFull.getCharacterSize() / 10.f + 0.5f));
                    	} else {
                            m_TextCropPosition = 0;
                        }
                    } else {
                        if (m_TextCropPosition != 0) {
                            --m_TextCropPosition;
                        }
                    }
                } else if ((x - getPosition().x > (this._WidgetBordersImpl.m_LeftBorder * scalingX) + width) && 
                	(m_TextFull.findCharacterPos(m_DisplayedText.length()).x > width)) {
                    if (m_TextFull.getCharacterSize() > 10) {
                        if (m_TextCropPosition + width < m_TextFull.findCharacterPos(m_DisplayedText.length()).x + (m_TextFull.getCharacterSize() / 10)) {
                            m_TextCropPosition += (int)(Math.floor(m_TextFull.getCharacterSize() / 10.f + 0.5f));
                        } else {
                            m_TextCropPosition = (int)(m_TextFull.findCharacterPos(m_DisplayedText.length()).x + (m_TextFull.getCharacterSize() / 10) - width);
                        }
                    } else  {
                        if (m_TextCropPosition + width < m_TextFull.findCharacterPos(m_DisplayedText.length()).x) {
                            ++m_TextCropPosition;
                        }
                    }
                }

                m_SelEnd = findSelectionPointPosition(x - getPosition().x - (this._WidgetBordersImpl.m_LeftBorder * scalingX));
            }

            if (m_SelEnd > m_SelStart) {
                if (m_SelChars != (m_SelEnd - m_SelStart)) {
                    m_SelChars = m_SelEnd - m_SelStart;

                    m_TextBeforeSelection.setString(m_DisplayedText.substring(0, m_SelStart));
                    m_TextSelection.setString(m_DisplayedText.substring(m_SelStart, m_SelChars + m_SelStart));
                    m_TextAfterSelection.setString(m_DisplayedText.substring(m_SelEnd));

                    recalculateTextPositions();
                }
            } else if (m_SelEnd < m_SelStart) {
                if (m_SelChars != (m_SelStart - m_SelEnd)) {
                    m_SelChars = m_SelStart - m_SelEnd;

                    m_TextBeforeSelection.setString(m_DisplayedText.substring(0, m_SelEnd));
                    m_TextSelection.setString(m_DisplayedText.substring(m_SelEnd, m_SelChars + m_SelEnd));
                    m_TextAfterSelection.setString(m_DisplayedText.substring(m_SelStart));

                    recalculateTextPositions();
                }
            } else if (m_SelChars > 0) {
                m_SelChars = 0;

                m_TextBeforeSelection.setString(m_DisplayedText);
                m_TextSelection.setString("");
                m_TextAfterSelection.setString("");

                recalculateTextPositions();
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
                if (m_SelEnd < m_DisplayedText.length()) {
                    setSelectionPointPosition(m_SelEnd + 1);
                }
            }

            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();
        } else if (key == Keyboard.Key.Home) {
            setSelectionPointPosition(0);

            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();
        } else if (key == Keyboard.Key.End) {
            setSelectionPointPosition(m_Text.length());

            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();
        } else if (key == Keyboard.Key.Return) {
            if (this.getCallbackManager().m_CallbackFunctions.get(EditBoxCallbacks.ReturnKeyPressed.value()).isEmpty() == false) {
            	this.getCallbackManager().m_Callback.trigger = EditBoxCallbacks.ReturnKeyPressed.value();
            	this.getCallbackManager().m_Callback.text = m_Text;
                addCallback();
            }
        } else if (key == Keyboard.Key.BackSpace) {
            if (m_SelChars == 0) {
                if (m_SelEnd == 0) {
                    return;
                }

                //m_Text.erase(m_SelEnd-1, 1);
                m_Text = m_Text.substring(0, m_SelEnd - 1) + m_Text.substring(m_SelEnd);
                //m_DisplayedText.erase(m_SelEnd-1, 1);
                m_DisplayedText = m_DisplayedText.substring(0, m_SelEnd - 1) + m_DisplayedText.substring(m_SelEnd);

                setSelectionPointPosition(m_SelEnd - 1);

                float width;
                if (m_SplitImage) {
                    width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.y / m_TextureNormal_M.getSize().y));
            	} else {
                    width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.x / m_TextureNormal_M.getSize().x));
                } 
                
                if (width < 0) {
                    width = 0;
                }

                float textWidth = m_TextFull.findCharacterPos(m_DisplayedText.length()).x;

                if (textWidth > width) {
                    if (textWidth - m_TextCropPosition < width) {
                        m_TextCropPosition = (int)(textWidth - width);
                    }
                } else {
                    m_TextCropPosition = 0;
                }
            } else { 
            	deleteSelectedCharacters();
            }
            
            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();

            if (this.getCallbackManager().m_CallbackFunctions.get(EditBoxCallbacks.TextChanged.value()).isEmpty() == false) {
            	this.getCallbackManager().m_Callback.trigger = EditBoxCallbacks.TextChanged.value();
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
                //m_DisplayedText.erase(m_SelEnd, 1);
                m_DisplayedText = m_DisplayedText.substring(0, m_SelEnd) + 
                		m_DisplayedText.substring(m_SelEnd + 1);
                
                setSelectionPointPosition(m_SelEnd);

                float width;
                if (m_SplitImage) {
                    width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.y / m_TextureNormal_M.getSize().y));
            	} else {
                    width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.x / m_TextureNormal_M.getSize().x));
            	}
            
                if (width < 0) {
                    width = 0;
                }

                float textWidth = m_TextFull.findCharacterPos(m_DisplayedText.length()).x;

                if (textWidth > width) {
                    if (textWidth - m_TextCropPosition < width) {
                        m_TextCropPosition = (int)(textWidth - width);
                    }
                } else {
                    m_TextCropPosition = 0;
                }
            } else { 
            	deleteSelectedCharacters();
            }
            
            m_SelectionPointVisible = true;
            m_AnimationTimeElapsed = new Time();

            if (this.getCallbackManager().m_CallbackFunctions.get(EditBoxCallbacks.TextChanged.value()).isEmpty() == false) {
                this.getCallbackManager().m_Callback.trigger = EditBoxCallbacks.TextChanged.value();
                this.getCallbackManager().m_Callback.text = m_Text;
                addCallback();
            }
        }
	}
	
	public void textEntered(int key) {
        if (m_Loaded == false) {
            return;
        }

        if (m_NumbersOnly) {
            if ((key < '0') || (key > '9')) {
                if ((key == '-') || (key == '+')) {
                    if ((m_SelStart == 0) || (m_SelEnd == 0)) {
                        if (!m_Text.isEmpty()) {
                            if ((m_Text.charAt(0) == '-') || 
                            	(m_Text.charAt(0) == '+')) {
                                return;
                            }
                        }
                    } else { 
                    	return;
                    }
                } else if ((key == ',') || (key == '.')) {
                    for (int i = 0; i < m_Text.length(); i++) {
                    	char it = m_Text.charAt(i);
                        if ((it == ',') || (it == '.')) {
                            return;
                        }
                    }
                } else { 
                	return;
                }
            }
        }

        if (m_SelChars > 0) {
            deleteSelectedCharacters();
        }
        
        if ((m_MaxChars > 0) && (m_Text.length() + 1 > m_MaxChars)) {
            return;
        }

        //m_Text.insert(m_SelEnd, key);
        m_Text = m_Text.substring(0, m_SelEnd) + 
        	Character.toString((char)key) + 
        	m_Text.substring(m_SelEnd);
        
        if (m_PasswordChar != '\0') {
            //m_DisplayedText.insert(m_SelEnd, m_PasswordChar);
            m_Text = m_Text.substring(0, m_SelEnd) + 
                	Character.toString(m_PasswordChar) + 
                	m_Text.substring(m_SelEnd);
		} else {
            //m_DisplayedText.insert(m_SelEnd, key);
            m_Text = m_Text.substring(0, m_SelEnd) + 
                	Character.toString((char)key) + 
                	m_Text.substring(m_SelEnd);
        }
        
        m_TextFull.setString(m_DisplayedText);

        float width;
        if (m_SplitImage) {
            width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.y / m_TextureNormal_M.getSize().y));
        } else {
            width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.x / m_TextureNormal_M.getSize().x));
        }
        
        if (m_LimitTextWidth) {
            if (m_TextFull.findCharacterPos(m_DisplayedText.length()).x > width) {
                //m_Text.erase(m_SelEnd, 1);
            	m_Text = m_Text.substring(0, m_SelEnd) + 
            		m_Text.substring(m_SelEnd + 1);
            	//m_DisplayedText.erase(m_SelEnd, 1);
            	m_DisplayedText = m_DisplayedText.substring(0, m_SelEnd) + 
            		m_DisplayedText.substring(m_SelEnd + 1);
                return;
            }
        }

        setSelectionPointPosition(m_SelEnd + 1);

        m_SelectionPointVisible = true;
        m_AnimationTimeElapsed = new Time();

        if (this.getCallbackManager().m_CallbackFunctions.get(EditBoxCallbacks.TextChanged.value()).isEmpty() == false) {
        	this.getCallbackManager().m_Callback.trigger = EditBoxCallbacks.TextChanged.value();
        	this.getCallbackManager().m_Callback.text = m_Text;
            addCallback();
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
            setText(value);
        } else if ("textsize".equals(property)) {
            setTextSize(Integer.parseInt(value.trim()));
        } else if ("passwordcharacter".equals(property)) {
            if (value.length() != 0) {
                if (value.length() == 1) {
                    setPasswordCharacter(value.charAt(0));
                } else {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'PasswordCharacter' propery.");
                }
            } else {
                setPasswordCharacter('\0');
            }
        } else if ("maximumcharacters".equals(property)) {
            setMaximumCharacters(Integer.parseInt(value.trim()));
        } else if ("borders".equals(property)) {
            Borders borders = new Borders();
            if (Defines.extractBorders(value, borders)) {
                setBorders(borders.left, borders.top, borders.right, borders.bottom);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Borders' property.");
            }
        } else if ("textcolor".equals(property)) {
            setTextColor(Defines.extractColor(value));
        } else if ("selectedtextcolor".equals(property)) {
            setSelectedTextColor(Defines.extractColor(value));
        } else if ("selectedtextbackgroundcolor".equals(property)) {
            setSelectedTextBackgroundColor(Defines.extractColor(value));
        } else if ("selectionpointcolor".equals(property)) {
            setSelectionPointColor(Defines.extractColor(value));
        } else if ("limittextwidth".equals(property)) {
            if (("true".equals(value)) || ("True".equals(value))) {
                limitTextWidth(true);
        	} else if (("false".equals(value)) || ("False".equals(value))) {
                limitTextWidth(false);
         	} else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'LimitTextWidth' property.");
         	}
        } else if ("selectionpointwidth".equals(property)) {
            setSelectionPointWidth(Integer.parseInt(value.trim()));
        } else if ("numbersonly".equals(property)) {
            if (("true".equals(value)) || ("True".equals(value))) {
                setNumbersOnly(true);
        	} else if (("false".equals(value)) || ("False".equals(value))) {
                setNumbersOnly(false);
        	} else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'NumbersOnly' property.");
            }
        } else if ("callback".equals(property)) {
            //ClickableWidget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("TextChanged".equals(it)) || 
                	("textchanged".equals(it))) {
                    this.getCallbackManager().bindCallback(EditBoxCallbacks.TextChanged.value());
            	} else if (("ReturnKeyPressed".equals(it)) || 
                	("returnkeypressed".equals(it))) {
            		this.getCallbackManager().bindCallback(EditBoxCallbacks.ReturnKeyPressed.value());
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
        } else if ("text".equals(property)) {
            value[0] = getText();
        } else if ("textsize".equals(property)) {
            value[0] = Integer.toString(getTextSize());
        } else if ("passwordcharacter".equals(property)) {
            if (getPasswordCharacter() != 0) {
                value[0] = Character.toString(getPasswordCharacter());
            } else {
                value[0] = "";
            }
        } else if ("maximumcharacters".equals(property)) {
            value[0] = Integer.toString(getMaximumCharacters());
		} else if ("borders".equals(property)) {
            value[0] = "(" + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().left) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().top) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().right) + "," + 
            	Integer.toString(this._WidgetBordersImpl.getBorders().bottom) + ")";
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
		} else if ("selectionpointcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getSelectionPointColor().r)) + "," + 
            	Integer.toString((int)(getSelectionPointColor().g)) + "," + 
            	Integer.toString((int)(getSelectionPointColor().b)) + "," + 
            	Integer.toString((int)(getSelectionPointColor().a)) + ")";
		} else if ("limittextwidth".equals(property)) {
            value[0] = m_LimitTextWidth ? "true" : "false";
		} else if ("selectionpointwidth".equals(property)) {
            value[0] = Integer.toString(getSelectionPointWidth());
		} else if ("numbersonly".equals(property)) {
            value[0] = m_NumbersOnly ? "true" : "false";
		} else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //ClickableWidget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(EditBoxCallbacks.TextChanged.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(EditBoxCallbacks.TextChanged.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(EditBoxCallbacks.TextChanged.value()).get(0) == null)) {
                callbacks.add("TextChanged");
			}
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(EditBoxCallbacks.ReturnKeyPressed.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(EditBoxCallbacks.ReturnKeyPressed.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(EditBoxCallbacks.ReturnKeyPressed.value()).get(0) == null)) {
                callbacks.add("ReturnKeyPressed");
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
        list.add(new Pair<String, String>("Text", "string"));
        list.add(new Pair<String, String>("TextSize", "uint"));
        list.add(new Pair<String, String>("PasswordCharacter", "char"));
        list.add(new Pair<String, String>("MaximumCharacters", "uint"));
        list.add(new Pair<String, String>("Borders", "borders"));
        list.add(new Pair<String, String>("TextColor", "color"));
        list.add(new Pair<String, String>("SelectedTextColor", "color"));
        list.add(new Pair<String, String>("SelectedTextBackgroundColor", "color"));
        list.add(new Pair<String, String>("SelectionPointColor", "color"));
        list.add(new Pair<String, String>("LimitTextWidth", "bool"));
        list.add(new Pair<String, String>("SelectionPointWidth", "uint"));
        list.add(new Pair<String, String>("NumbersOnly", "bool"));
        return list;
	}
	
	protected int findSelectionPointPosition(float posX) {
	    if (m_DisplayedText.isEmpty()) {
            return 0;
        }

        int firstVisibleChar;
        if (m_TextCropPosition != 0) {
            firstVisibleChar = m_SelEnd;

            while (m_TextFull.findCharacterPos(firstVisibleChar-1).x > m_TextCropPosition) {
                --firstVisibleChar;
            }
        } else { 
        	firstVisibleChar = 0;
        }
        
        String tempString;
        float textWidthWithoutLastChar;
        float fullTextWidth;
        float halfOfLastCharWidth;
        int lastVisibleChar;

        float width;
        if (m_SplitImage) {
            width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.y / m_TextureNormal_M.getSize().y));
		} else {
            width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.x / m_TextureNormal_M.getSize().x));
        }
        
        if (width < 0) {
            width = 0;
        }

        float pixelsToMove = 0;
        if (m_TextAlignment != Alignments.Left) {
            float textWidth = m_TextFull.findCharacterPos(m_DisplayedText.length()).x;

            if (textWidth < width) {
                if (m_TextAlignment == Alignments.Center) {
                    pixelsToMove = (width - textWidth) / 2.f;
            	} else { 
                	pixelsToMove = width - textWidth;
                }
            }
        }

        lastVisibleChar = m_SelEnd;

        while (m_TextFull.findCharacterPos(lastVisibleChar+1).x < m_TextCropPosition + width) {
            if (lastVisibleChar == m_DisplayedText.length()) {
                break;
            }

            ++lastVisibleChar;
        }

        tempString = m_DisplayedText.substring(0, firstVisibleChar);
        m_TextFull.setString(tempString);

        fullTextWidth = m_TextFull.findCharacterPos(firstVisibleChar).x;

        for (int i = firstVisibleChar; i < lastVisibleChar; ++i) {
            tempString += m_DisplayedText.charAt(i);
            m_TextFull.setString(tempString);

            textWidthWithoutLastChar = fullTextWidth;
            fullTextWidth = m_TextFull.findCharacterPos(i + 1).x;
            halfOfLastCharWidth = (fullTextWidth - textWidthWithoutLastChar) / 2.0f;

            if (posX < textWidthWithoutLastChar + pixelsToMove + halfOfLastCharWidth - m_TextCropPosition) {
                m_TextFull.setString(m_DisplayedText);
                return i;
            }
        }

        m_TextFull.setString(m_DisplayedText);
        return lastVisibleChar;
	}
	
	protected void deleteSelectedCharacters() {
	    if (m_SelStart < m_SelEnd) {
            //m_Text.erase(m_SelStart, m_SelChars);
	    	m_Text = m_Text.substring(0, m_SelStart) + 
	    		m_Text.substring(m_SelStart + m_SelChars);
            //m_DisplayedText.erase(m_SelStart, m_SelChars);
	    	m_DisplayedText = m_DisplayedText.substring(0, m_SelStart) + 
	    		m_DisplayedText.substring(m_SelStart + m_SelChars);
	    	
            setSelectionPointPosition(m_SelStart);
        } else {
            //m_Text.erase(m_SelEnd, m_SelChars);
	    	m_Text = m_Text.substring(0, m_SelEnd) + 
		    		m_Text.substring(m_SelEnd + m_SelChars);
        	//m_DisplayedText.erase(m_SelEnd, m_SelChars);
	    	m_DisplayedText = m_DisplayedText.substring(0, m_SelEnd) + 
		    		m_DisplayedText.substring(m_SelEnd + m_SelChars);
	    	
            setSelectionPointPosition(m_SelEnd);
        }

        float width;
        if (m_SplitImage) {
            width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.y / m_TextureNormal_M.getSize().y));
        } else {
            width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * (m_Size.x / m_TextureNormal_M.getSize().x));
        }
        
        if (width < 0) {
            width = 0;
        }

        float textWidth = m_TextFull.findCharacterPos(m_DisplayedText.length()).x;

        if (textWidth > width) {
            if (textWidth - m_TextCropPosition < width) {
                m_TextCropPosition = (int)(textWidth - width);
            }
        } else {
            m_TextCropPosition = 0;
        }
	}
	
	protected void recalculateTextPositions() {
		float textX = getPosition().x;
        float textY = getPosition().y;

        Vector2f scaling = new Vector2f(m_Size.x / m_TextureNormal_M.getSize().x, m_Size.y / m_TextureNormal_M.getSize().y);

        float borderScale;
        if (m_SplitImage) {
            borderScale = scaling.y;
		} else {
            borderScale = scaling.x;
        }
        
        textX += this._WidgetBordersImpl.m_LeftBorder * borderScale - m_TextCropPosition;
        textY += this._WidgetBordersImpl.m_TopBorder * scaling.y;

        if (m_TextAlignment != Alignments.Left) {
            float width = m_Size.x - ((this._WidgetBordersImpl.m_LeftBorder + this._WidgetBordersImpl.m_RightBorder) * borderScale);

            float textWidth = m_TextFull.findCharacterPos(m_DisplayedText.length()).x;

            if (textWidth < width) {
                if (m_TextAlignment == Alignments.Center) {
                    textX += (width - textWidth) / 2.f;
            	} else { 
                	textX += width - textWidth;
                }
            }
        }

        float selectionPointLeft = textX;

        Text tempText = new Text(m_TextFull);
        tempText.setString("kg");
        textY += (((m_Size.y - ((this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder) * scaling.y)) - tempText.getLocalBounds().height) * 0.5f) - tempText.getLocalBounds().top;

        m_TextBeforeSelection.setPosition(
        		(float)Math.floor(textX + 0.5f), 
        		(float)Math.floor(textY + 0.5f));

        if (m_SelChars != 0) {
            if (m_TextBeforeSelection.getString().length() > 0) {
                textX += m_TextBeforeSelection.getFont().getKerning(
                	m_DisplayedText.charAt(m_TextBeforeSelection.getString().length() - 1), 
                	m_DisplayedText.charAt(m_TextBeforeSelection.getString().length()), 
                	m_TextBeforeSelection.getCharacterSize());
            }
            
            textX += m_TextBeforeSelection.findCharacterPos(m_TextBeforeSelection.getString().length()).x - 
            		m_TextBeforeSelection.getPosition().x;

            m_SelectedTextBackground.setSize(
            		new Vector2f(m_TextSelection.findCharacterPos(m_TextSelection.getString().length()).x - m_TextSelection.getPosition().x,
            				(m_Size.y - ((this._WidgetBordersImpl.m_TopBorder + this._WidgetBordersImpl.m_BottomBorder) * scaling.y))));
            m_SelectedTextBackground.setPosition(
            		(float)Math.floor(textX + 0.5f), 
            		(float)Math.floor(getPosition().y + (this._WidgetBordersImpl.m_TopBorder * scaling.y) + 0.5f));

            m_TextSelection.setPosition(
            		(float)Math.floor(textX + 0.5f), 
            		(float)Math.floor(textY + 0.5f));

            if (m_DisplayedText.length() > m_TextBeforeSelection.getString().length() + m_TextSelection.getString().length()) {
                textX += m_TextBeforeSelection.getFont().getKerning(
                		m_DisplayedText.charAt(m_TextBeforeSelection.getString().length() + m_TextSelection.getString().length() - 1), 
                		m_DisplayedText.charAt(m_TextBeforeSelection.getString().length() + m_TextSelection.getString().length()), 
                		m_TextBeforeSelection.getCharacterSize());
            }
            
            textX += m_TextSelection.findCharacterPos(m_TextSelection.getString().length()).x  - m_TextSelection.getPosition().x;
            m_TextAfterSelection.setPosition(
            		(float)Math.floor(textX + 0.5f), 
            		(float)Math.floor(textY + 0.5f));
        }

        selectionPointLeft += m_TextFull.findCharacterPos(m_SelEnd).x - (m_SelectionPoint.getSize().x * 0.5f);
        m_SelectionPoint.setPosition(
        		(float)Math.floor(selectionPointLeft + 0.5f), 
        		(float)Math.floor((this._WidgetBordersImpl.m_TopBorder * scaling.y) + getPosition().y + 0.5f));
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
		if (m_SplitImage) {
            if (m_SeparateHoverImage) {
                if ((m_MouseHover) && 
                	((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureHover_L.getSprite(), states);
                    target.draw(m_TextureHover_M.getSprite(), states);
                    target.draw(m_TextureHover_R.getSprite(), states);
                } else {
                    target.draw(m_TextureNormal_L.getSprite(), states);
                    target.draw(m_TextureNormal_M.getSprite(), states);
                    target.draw(m_TextureNormal_R.getSprite(), states);
                }
            } else {
                target.draw(m_TextureNormal_L.getSprite(), states);
                target.draw(m_TextureNormal_M.getSprite(), states);
                target.draw(m_TextureNormal_R.getSprite(), states);

                if ((m_MouseHover) && 
                    ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureHover_L.getSprite(), states);
                    target.draw(m_TextureHover_M.getSprite(), states);
                    target.draw(m_TextureHover_R.getSprite(), states);
                }
            }

            if ((m_Focused) && 
            	((m_WidgetPhase & WidgetPhase.WidgetPhase_Focused.value()) != 0)) {
                target.draw(m_TextureFocused_L.getSprite(), states);
                target.draw(m_TextureFocused_M.getSprite(), states);
                target.draw(m_TextureFocused_R.getSprite(), states);
            }
        } else {
            if (m_SeparateHoverImage) {
                if ((m_MouseHover) && 
                    ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureHover_M.getSprite(), states);
                } else {
                    target.draw(m_TextureNormal_M.getSprite(), states);
                }
            } else {
                target.draw(m_TextureNormal_M.getSprite(), states);

                if ((m_MouseHover) && 
                    ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
                    target.draw(m_TextureHover_M.getSprite(), states);
                }
            }

            if ((m_Focused) && 
                ((m_WidgetPhase & WidgetPhase.WidgetPhase_Focused.value()) != 0)) {
                target.draw(m_TextureFocused_M.getSprite(), states);
            }
        }

        Vector2f scaling = new Vector2f(m_Size.x / m_TextureNormal_M.getSize().x, m_Size.y / m_TextureNormal_M.getSize().y);

        float borderScale;
        if (m_SplitImage) {
            borderScale = scaling.y;
		} else {
            borderScale = scaling.x;
        }
        
        float scaleViewX = target.getSize().x / target.getView().getSize().x;
        float scaleViewY = target.getSize().y / target.getView().getSize().y;

        Vector2f viewPosition = Vector2f.minus(Vector2f.devide(target.getView().getSize(), 2.f), target.getView().getCenter());

        Vector2f topLeftPosition = states.transform.transformPoint(
        		getPosition().x + (this._WidgetBordersImpl.m_LeftBorder * borderScale) + viewPosition.x,
            	getPosition().y + (this._WidgetBordersImpl.m_TopBorder * scaling.y) + viewPosition.y);
        Vector2f bottomRightPosition = states.transform.transformPoint(
        		getPosition().x + (m_Size.x - (this._WidgetBordersImpl.m_RightBorder * borderScale)) + viewPosition.x,
                getPosition().y + (m_Size.y - (this._WidgetBordersImpl.m_BottomBorder * scaling.y)) + viewPosition.y);

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

        if (m_TextSelection.getString().isEmpty() == false) {
            target.draw(m_SelectedTextBackground, states);

            target.draw(m_TextSelection, states);
            target.draw(m_TextAfterSelection, states);
        }

        if ((m_Focused) && (m_SelectionPointVisible)) {
            target.draw(m_SelectionPoint, states);
        }

        GL.glScissor(scissor[0], scissor[1], scissor[2], scissor[3]);
	}
	
	@Override
	public void setBorders(int borderLeft, int borderTop,
			int borderRight, int borderBottom) {
		this._WidgetBordersImpl.m_LeftBorder = borderLeft;
        this._WidgetBordersImpl.m_TopBorder = borderTop;
        this._WidgetBordersImpl.m_RightBorder = borderRight;
        this._WidgetBordersImpl.m_BottomBorder = borderBottom;

        setText(m_Text);

        m_SelectionPoint.setSize(
        	new Vector2f((float)(m_SelectionPoint.getSize().x),
        		m_Size.y - ((this._WidgetBordersImpl.m_BottomBorder + this._WidgetBordersImpl.m_TopBorder) * (m_Size.y / m_TextureNormal_M.getSize().y))));
	}

	@Override
	public boolean mouseOnWidget(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

}
