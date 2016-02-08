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
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.Keyboard;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Text;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20150912
 * @author Administrator
 *
 */
public class Checkbox extends ClickableWidget {
	public static enum CheckboxCallbacks {
	    Checked(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 1),
	    Unchecked(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 2),
	    SpaceKeyPressed(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 8),
	    ReturnKeyPressed(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 16),
	    AllCheckboxCallbacks(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 32 - 1),
	    CheckboxCallbacksCount(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 32);
	
        int value;
        
        CheckboxCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
	}
	
	protected String m_LoadedConfigFile;
	
	protected boolean m_Checked;
	
	protected boolean m_AllowTextClick;
	
	protected Text m_Text;
	
	protected int m_TextSize;
	
	protected Texture m_TextureUnchecked;
	protected Texture m_TextureChecked;
	protected Texture m_TextureHover;
	protected Texture m_TextureFocused;
	
	public Checkbox() {
		super();
	    m_Checked = false;
	    m_AllowTextClick = true;
	    m_TextSize = 0;
	    
	    this.getCallbackManager().m_Callback.widgetType = 
	    	WidgetTypes.Type_Checkbox;
        m_Text.setColor(Color.Black);
	}

	public Checkbox(Checkbox copy) {
	    super(copy);
	    m_LoadedConfigFile = copy.m_LoadedConfigFile;
	    m_Checked = copy.m_Checked;
	    m_AllowTextClick = copy.m_AllowTextClick;
	    m_Text = copy.m_Text;
	    m_TextSize = copy.m_TextSize;
	    
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureUnchecked, m_TextureUnchecked);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureChecked, m_TextureChecked);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureHover, m_TextureHover);
        Defines.TGUI_TextureManager.copyTexture(copy.m_TextureFocused, m_TextureFocused);
	}
	
	public void destroy() {
		super.destroy();
        if (m_TextureUnchecked.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureUnchecked);
        }
        if (m_TextureChecked.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureChecked);
        }
        if (m_TextureHover.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover);
        }
        if (m_TextureFocused.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused);
        }
	}
	
	public Checkbox cloneObj() {
		return new Checkbox(this);
	}
	
	public Checkbox assign(Checkbox right) {
        if (this != right) {
        	Checkbox temp = new Checkbox(right);
        	super.assign(right);

            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_Checked = temp.m_Checked;
            m_AllowTextClick = temp.m_AllowTextClick;
            m_Text = temp.m_Text;
            m_TextSize = temp.m_TextSize;
            m_TextureUnchecked = temp.m_TextureUnchecked;
            m_TextureChecked = temp.m_TextureChecked;
            m_TextureHover = temp.m_TextureHover;
            m_TextureFocused = temp.m_TextureFocused;
        }

        return this;
    }
	
	public boolean load(String configFileFilename) {
        m_LoadedConfigFile = configFileFilename;

        m_Loaded = false;

        if (m_TextureUnchecked.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureUnchecked);
        }
        if (m_TextureChecked.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureChecked);
        }
        if (m_TextureHover.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover);
        }
        if (m_TextureFocused.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused);
        }

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("Checkbox", properties, values)) {
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

            if ("textcolor".equals(property)) {
                m_Text.setColor(configFile.readColor(value));
            } else if ("checkedimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureChecked)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for CheckedImage in section Checkbox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("uncheckedimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureUnchecked)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for UncheckedImage in section Checkbox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("hoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureHover)) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for HoverImage in section Checkbox in " + configFileFilename + ".");
                    return false;
                }
            } else if ("focusedimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFocused)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FocusedImage in section Checkbox in " + configFileFilename + ".");
                    return false;
                }
            } else {
            	Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section Checkbox in " + configFileFilename + ".");
            }
        }

        if ((m_TextureChecked.data != null) && 
        	(m_TextureUnchecked.data != null)) {
            m_Loaded = true;
            setSize((float)(m_TextureUnchecked.getSize().x), 
            	(float)(m_TextureUnchecked.getSize().y));
        } else {
            Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the checkbox. Is the Checkbox section in " + configFileFilename + " complete?");
            return false;
        }

        if (m_TextureFocused.data != null) {
            m_AllowFocus = true;
            m_WidgetPhase |= WidgetPhase.WidgetPhase_Focused.value();
        }
        if (m_TextureHover.data != null) {
            m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();
        }
        
        return true;
	}
	
	public String getLoadedConfigFile() {
		return m_LoadedConfigFile;
	}
	
	public void setPosition(float x, float y) {
        //Transformable::
        super.setPosition(x, y);

        m_TextureUnchecked.sprite.setPosition(x, y);
        m_TextureChecked.sprite.setPosition(x, y + m_TextureUnchecked.getSize().y - m_TextureChecked.getSize().y);
        m_TextureFocused.sprite.setPosition(x, y);
        m_TextureHover.sprite.setPosition(x, y);

        FloatRect textBounds = m_Text.getLocalBounds();
        m_Text.setPosition(
        	(float)(x + Math.floor(m_Size.x * 11.0f / 10.0f - textBounds.left)),
            (float)(y + Math.floor(((m_Size.y - textBounds.height) / 2.0f) - textBounds.top)));		
	}
	
	//using Transformable::setPosition;
	
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

        m_Size.x = width;
        m_Size.y = height;

        if (m_TextSize == 0) {
            setText(m_Text.getString());
        }
        
        Vector2f scaling = new Vector2f(m_Size.x / m_TextureUnchecked.getSize().x, m_Size.y / m_TextureUnchecked.getSize().y);
        m_TextureChecked.sprite.setScale(scaling);
        m_TextureUnchecked.sprite.setScale(scaling);
        m_TextureFocused.sprite.setScale(scaling);
        m_TextureHover.sprite.setScale(scaling);

        setPosition(getPosition());		
	}
	
	public Vector2f getSize() {
        if (m_Text.getString().isEmpty()) {
            return m_Size;
        } else {
            return new Vector2f(
            	(float)((m_Size.x * 11.0 / 10.0) + m_Text.getLocalBounds().left + m_Text.getLocalBounds().width), 
            	m_Size.y);
        }
	}
	
	public void check() {
        if (m_Checked == false) {
            m_Checked = true;

            if (this.getCallbackManager().m_CallbackFunctions.get(CheckboxCallbacks.Checked.value()).isEmpty() == false) {
                this.getCallbackManager().m_Callback.trigger = CheckboxCallbacks.Checked.value();
                this.getCallbackManager().m_Callback.checked = true;
                addCallback();
            }
        }		
	}
	
	public void uncheck() {
        if (m_Checked) {
            m_Checked = false;

            if (this.getCallbackManager().m_CallbackFunctions.get(CheckboxCallbacks.Unchecked.value()).isEmpty() == false) {
                this.getCallbackManager().m_Callback.trigger = CheckboxCallbacks.Unchecked.value();
                this.getCallbackManager().m_Callback.checked = false;
                addCallback();
            }
        }			
	}
	
	public boolean isChecked() {
		return m_Checked;
	}
	
	public void setText(String text) {
	    if (m_Loaded == false) {
            return;
	    }

        m_Text.setString(text);

        if (m_TextSize == 0) {
            m_Text.setCharacterSize((int)(m_Size.y));
            m_Text.setCharacterSize((int)(m_Text.getCharacterSize() - m_Text.getLocalBounds().top));
        } else  {
            m_Text.setCharacterSize(m_TextSize);
        }

        setPosition(getPosition());
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
	
	public void allowTextClick(boolean acceptTextClick) {
		m_AllowTextClick = acceptTextClick;
	}
	
	public void setTransparency(int transparency) {
        //ClickableWidget::
        super.setTransparency(transparency);

        m_TextureChecked.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureUnchecked.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureHover.sprite.setColor(new Color(255, 255, 255, m_Opacity));
        m_TextureFocused.sprite.setColor(new Color(255, 255, 255, m_Opacity));
	}
	
	public boolean mouseOnWidget(float x, float y) {
        if (m_Loaded == false) {
            return false;
        }

        if (getTransform().transformRect(
        	new FloatRect(0, 0, m_Size.x, m_Size.y))
        	.contains(x, y)) {
            return true;
		} else {
            if (m_AllowTextClick) {
                FloatRect bounds = m_Text.getLocalBounds();
                if (new FloatRect(bounds.left, bounds.top, bounds.width, bounds.height)
                		.contains(
                			x - (getPosition().x + ((m_Size.x * 11.0f / 10.0f))), 
                			y - getPosition().y - ((m_Size.y - bounds.height) / 2.0f) + bounds.top))
                    return true;
            }
        }

        if (m_MouseHover == true) {
            mouseLeftWidget();
        }

        m_MouseHover = false;
        return false;
	}
	
	public void leftMouseReleased(float x, float y) {
	    if (this.getCallbackManager().m_CallbackFunctions
	    		.get(ClickableWidgetCallbacks.LeftMouseReleased.value())
	    		.isEmpty() == false) {
	    	this.getCallbackManager().m_Callback.trigger = 
	    		ClickableWidgetCallbacks.LeftMouseReleased.value();
	    	this.getCallbackManager().m_Callback.checked = m_Checked;
	    	this.getCallbackManager().m_Callback.mouse.x = 
	    		(int)(x - getPosition().x);
	    	this.getCallbackManager().m_Callback.mouse.y = 
	    		(int)(y - getPosition().y);
            addCallback();
        }

        if (m_MouseDown == true) {
            if (m_Checked) {
                uncheck();
            } else {
                check();
            }
            
            if (this.getCallbackManager().m_CallbackFunctions
            		.get(ClickableWidgetCallbacks.LeftMouseClicked.value())
            			.isEmpty() == false) {
            	this.getCallbackManager().m_Callback.trigger = 
            		ClickableWidgetCallbacks.LeftMouseClicked.value();
            	this.getCallbackManager().m_Callback.checked = m_Checked;
            	this.getCallbackManager().m_Callback.mouse.x = (int)(x - getPosition().x);
            	this.getCallbackManager().m_Callback.mouse.y = (int)(y - getPosition().y);
                addCallback();
            }

            m_MouseDown = false;
        }
	}
	
	public void keyPressed(Keyboard.Key key) {
        if (key == Keyboard.Key.Space) {
            if (m_Checked) {
                uncheck();
            } else {
                check();
            }
            
            if (this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.SpaceKeyPressed.value())
            		.isEmpty() == false) {
            	this.getCallbackManager().m_Callback.trigger = 
            			CheckboxCallbacks.SpaceKeyPressed.value();
            	this.getCallbackManager().m_Callback.checked = m_Checked;
                addCallback();
            }
        } else if (key == Keyboard.Key.Return) {
            if (m_Checked) {
                uncheck();
            } else {
                check();
            }
            
            if (this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.ReturnKeyPressed.value())
            		.isEmpty() == false) {
            	this.getCallbackManager().m_Callback.trigger = 
            		CheckboxCallbacks.ReturnKeyPressed.value();
                this.getCallbackManager().m_Callback.checked = m_Checked;
                addCallback();
            }
        }
	}
	
	public void widgetFocused() {
        if ((m_WidgetPhase & WidgetPhase.WidgetPhase_Focused.value()) == 0) {
            unfocus();
		} else {
            //Widget::
            super.widgetFocused();
        }
	}
	
	public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            load(value);
        } else if ("checked".equals(property)) {
            if (("true".equals(value)) || ("True".equals(value))) {
                check();
            } else if (("false".equals(value)) || ("False".equals(value))) {
                uncheck();
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Checked' property.");
            }
        } else if ("text".equals(property)) {
            setText(value);
        } else if ("textcolor".equals(property)) {
            setTextColor(Defines.extractColor(value));
        } else if ("textsize".equals(property)) {
        	try {
        		setTextSize(Integer.parseInt(value.trim()));
        	} catch (NumberFormatException e) {
        		e.printStackTrace();
        	}
        } else if (property == "allowtextclick") {
            if (("true".equals(value)) || ("True".equals(value))) {
                allowTextClick(true);
            } else if (("false".equals(value)) || ("False".equals(value))) {
                allowTextClick(false);
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'AllowTextClick' property.");
            }
        } else if ("callback".equals(property)) {
            //ClickableWidget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("Checked".equals(it)) || ("checked".equals(it))) {
                    this.getCallbackManager().bindCallback(CheckboxCallbacks.Checked.value());
                } else if (("Unchecked".equals(it)) || ("unchecked".equals(it))) {
                	this.getCallbackManager().bindCallback(CheckboxCallbacks.Unchecked.value());
                } else if (("SpaceKeyPressed".equals(it)) || ("spacekeypressed".equals(it))) {
                	this.getCallbackManager().bindCallback(CheckboxCallbacks.SpaceKeyPressed.value());
            	} else if (("ReturnKeyPressed".equals(it)) || ("returnkeypressed".equals(it))) {
            		this.getCallbackManager().bindCallback(CheckboxCallbacks.ReturnKeyPressed.value());
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

        if ("width".equals(property)) {
            value[0] = Float.toString(m_TextureUnchecked.sprite.getGlobalBounds().width);
        } else if ("height".equals(property)) {
            value[0] = Float.toString(m_TextureUnchecked.sprite.getGlobalBounds().height);
		} else if ("configfile".equals(property)) {
            value[0] = getLoadedConfigFile();
		} else if ("checked".equals(property)) {
            value[0] = m_Checked ? "true" : "false";
		} else if ("text".equals(property)) {
            value[0] = getText();
		} else if ("textcolor".equals(property)) {
            value[0] = "(" + Integer.toString((int)(getTextColor().r)) + 
            		"," + Integer.toString((int)(getTextColor().g)) + 
            		"," + Integer.toString((int)(getTextColor().b)) + 
            		"," + Integer.toString((int)(getTextColor().a)) + 
            		")";
		} else if ("textsize".equals(property)) {
            value[0] = Integer.toString(getTextSize());
		} else if ("allowtextclick".equals(property)) {
            value[0] = m_AllowTextClick ? "true" : "false";
		} else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //ClickableWidget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.Checked.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.Checked.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.Checked.value()).get(0) == null)) {
                callbacks.add("Checked");
            }
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.Unchecked.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.Unchecked.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.Unchecked.value()).get(0) == null)) {
                callbacks.add("Unchecked");
            }
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.SpaceKeyPressed.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.SpaceKeyPressed.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.SpaceKeyPressed.value()).get(0) == null)) {
                callbacks.add("SpaceKeyPressed");
            }
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.ReturnKeyPressed.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.ReturnKeyPressed.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(CheckboxCallbacks.ReturnKeyPressed.value()).get(0) == null)) {
                callbacks.add("ReturnKeyPressed");
            }

            Defines.encodeList(callbacks, value);

            if (value[0] == null || value[0].length() == 0) {
                value[0] = tempValue[0];
            } else if (tempValue[0] != null && tempValue[0].length() > 0) {
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
        list.add(new Pair<String, String>("Checked", "bool"));
        list.add(new Pair<String, String>("Text", "string"));
        list.add(new Pair<String, String>("TextColor", "color"));
        list.add(new Pair<String, String>("TextSize", "uint"));
        list.add(new Pair<String, String>("AllowTextClick", "bool"));
        return list;
	}
	
	protected void initialize(Container parent) {
        m_Parent = parent;
        m_Text.setFont(m_Parent.getGlobalFont());
	}
	
	public void draw(RenderTarget target, RenderStates states) {
        if (m_Checked) {
            target.draw(m_TextureChecked.getSprite(), states);
        } else {
            target.draw(m_TextureUnchecked.getSprite(), states);
        }
        
        if ((m_Focused) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Focused.value()) != 0)) {
            target.draw(m_TextureFocused.getSprite(), states);
        }

        if ((m_MouseHover) && ((m_WidgetPhase & WidgetPhase.WidgetPhase_Hover.value()) != 0)) {
            target.draw(m_TextureHover.getSprite(), states);
        }

        target.draw(m_Text, states);
	}
}
