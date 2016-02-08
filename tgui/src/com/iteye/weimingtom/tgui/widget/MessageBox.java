package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.Call0;
import com.iteye.weimingtom.tgui.Callback;
import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.SharedWidgetPtr;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.Text;

/**
 * 20151005
 * @author Administrator
 *
 */
public class MessageBox extends ChildWindow {
	public static enum MessageBoxCallbacks {
	    ButtonClicked(ChildWindowCallbacks.ChildWindowCallbacksCount.value() * 1),
	    AllMessageBoxCallbacks(ChildWindowCallbacks.ChildWindowCallbacksCount.value() * 2 - 1),
	    MessageBoxCallbacksCount(ChildWindowCallbacks.ChildWindowCallbacksCount.value() * 2);
	    
        int value;
        
        MessageBoxCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
	}
	
	protected String m_LoadedConfigFile;
	protected String m_ButtonConfigFileFilename;

	protected List<SharedWidgetPtr<Button>> m_Buttons = new ArrayList<SharedWidgetPtr<Button>>();

    protected SharedWidgetPtr<Label> m_Label;

    protected int m_TextSize;
	
	public MessageBox() {	
	    m_TextSize = 16;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_MessageBox;

        add(m_Label, "MessageBoxText");
        m_Label.get().setTextSize(m_TextSize);
	}
	
	public MessageBox(MessageBox messageBoxToCopy) {
	    super(messageBoxToCopy);
	    m_LoadedConfigFile = messageBoxToCopy.m_LoadedConfigFile;
	    m_ButtonConfigFileFilename = messageBoxToCopy.m_ButtonConfigFileFilename;
	    m_TextSize = messageBoxToCopy.m_TextSize;
	    
        removeAllWidgets();

        m_Label = (SharedWidgetPtr<Label>)copy(messageBoxToCopy.m_Label, "MessageBoxText");

        for (SharedWidgetPtr<Button> it : messageBoxToCopy.m_Buttons) {
        	SharedWidgetPtr<Button> button = (SharedWidgetPtr<Button>)copy(it);
            button.get().getCallbackManager().unbindAllCallback();
            //FIXME:
            button.get().getCallbackManager().bindCallbackEx(
            	new Call0() {
            		@Override
					public void call() {
            			//FIXME:
						ButtonClickedCallbackFunction(null);
					}
            	}, this, ClickableWidget.ClickableWidgetCallbacks.LeftMouseClicked.value() | 
            	Button.ButtonCallbacks.SpaceKeyPressed.value() | 
            	Button.ButtonCallbacks.ReturnKeyPressed.value());
            
            //Button::LeftMouseClicked | Button::SpaceKeyPressed | Button::ReturnKeyPressed);

            m_Buttons.add(button);
        }
	}

	public void destroy() {
		super.destroy();
	}
	
	public MessageBox assign(MessageBox right) {
        if (this != right) {
            MessageBox temp = new MessageBox(right);
            super.assign(right);

            m_LoadedConfigFile = temp.m_LoadedConfigFile;
            m_ButtonConfigFileFilename = temp.m_ButtonConfigFileFilename;
            m_Buttons = temp.m_Buttons;
            m_Label = temp.m_Label;
            m_TextSize = temp.m_TextSize;
        }

        return this;
	}
	
	public MessageBox cloneObj() {
        return new MessageBox(this);
	}
	
	public boolean load(String configFileFilename) {
        m_LoadedConfigFile = configFileFilename;

        m_Loaded = false;

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("MessageBox", properties, values)) {
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
        boolean childWindowPropertyFound = false;
        boolean buttonPropertyFound = false;

        for (int i = 0; i < properties.size(); ++i) {
            String property = properties.get(i);
            String value = values.get(i);

            if ("textcolor".equals(property)) {
                m_Label.get().setTextColor(configFile.readColor(value));
            } else if ("childwindow".equals(property)) {
                if ((value.length() < 3) || 
                	(value.charAt(0) != '"') || 
                	(value.charAt(value.length() - 1) != '"')) {
                    Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for ChildWindow in section MessageBox in " + configFileFilename + ".");
                    return false;
                }

                //ChildWindow::
                //FIXME:
                if (!super.load(configFileFolder + value.substring(1, value.length() - 2 + 1))) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to load the internal ChildWindow for MessageBox.");
                } else {
                    childWindowPropertyFound = true;
                }
            } else if ("button".equals(property)) {
                if ((value.length() < 3) || 
                	(value.charAt(0) != '"') || 
                	(value.charAt(value.length() - 1) != '"')) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for Button in section MessageBox in " + configFileFilename + ".");
                    return false;
                }

                m_ButtonConfigFileFilename = configFileFolder + value.substring(1, value.length() - 2 + 1);
                buttonPropertyFound = true;
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section MessageBox in " + configFileFilename + ".");
            }
        }

        if (!childWindowPropertyFound) {
            Defines.TGUI_OUTPUT("TGUI error: Missing a ChildWindow property in section MessageBox in " + configFileFilename + ".");
            return false;
        }
        if (!buttonPropertyFound) {
        	Defines.TGUI_OUTPUT("TGUI error: Missing a Button property in section MessageBox in " + configFileFilename + ".");
            return false;
        }

        return m_Loaded = true;
	}
	
	public void setText(String text) {
        if (m_Loaded) {
            m_Label.get().setText(text);

            rearrange();
        } else {
            Defines.TGUI_OUTPUT("TGUI error: Failed to set the text. MessageBox was not loaded completely.");
        }
	}
	
	public String getText() {
        if (m_Loaded) {
            return m_Label.get().getText();
		} else {
            Defines.TGUI_OUTPUT("TGUI error: Failed to set the text. MessageBox was not loaded completely.");
            return "";
        }
	}
	
	public void setTextFont(Font font) {
        m_Label.get().setTextFont(font);
	}
	
	public Font getTextFont() {
        return m_Label.get().getTextFont();
	}
	
	public void setTextColor(Color color) {
        m_Label.get().setTextColor(color);
	}
	
	public Color getTextColor() {
        return m_Label.get().getTextColor();
	}
	
	public void setTextSize(int size) {
        m_TextSize = size;

        if (m_Loaded) {
            m_Label.get().setTextSize(size);

            for (int i = 0; i < m_Buttons.size(); ++i) {
                m_Buttons.get(i).get().setTextSize(m_TextSize);
            }

            rearrange();
        }
	}
	
	public int getTextSize() {
        return m_TextSize;
	}
	
	public void addButton(String caption) {
        if (m_Loaded) {
        	SharedWidgetPtr<Button> button = new SharedWidgetPtr<Button>(new Button(), this);
            button.get().load(m_ButtonConfigFileFilename);
            button.get().setTextSize(m_TextSize);
            button.get().setText(caption);
            button.get().getCallbackManager()
            	.bindCallbackEx(new Call0() {
					@Override
					public void call() {
						//FIXME:
						ButtonClickedCallbackFunction(null);
					}
            	}, this, ClickableWidget.ClickableWidgetCallbacks.LeftMouseClicked.value() | 
            	Button.ButtonCallbacks.SpaceKeyPressed.value() | 
            	Button.ButtonCallbacks.ReturnKeyPressed.value());
            
            m_Buttons.add(button);

            rearrange();
        } else {
            Defines.TGUI_OUTPUT("TGUI error: Could not add a button. MessageBox was not loaded completely.");
        }
	}
	
	public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if (property == "text") {
            setText(value);
        } else if (property == "textcolor") {
            setTextColor(Defines.extractColor(value));
        } else if (property == "textsize") {
            setTextSize(Integer.parseInt(value.trim()));
        } else if (property == "buttons") {
            removeAllWidgets();
            m_Buttons.clear();
            add(m_Label);

            List<String> buttons = new ArrayList<String>();
            Defines.decodeList(value, buttons);

            for (String it : buttons) {
                addButton(it);
            }
        } else if (property == "callback") {
            //Widget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("ButtonClicked".equals(it)) || ("buttonclicked".equals(it))) {
                    this.getCallbackManager().bindCallback(MessageBoxCallbacks.ButtonClicked.value());
                }
            }
        } else {
        	//ChildWindow::
            return super.setProperty(property, value);
        }
        return true;
	}
	
	public boolean getProperty(String property, String[] value) {
        property = Defines.toLower(property);

        if (property == "text") {
            value[0] = getText();
        } else if (property == "textcolor") {
            value[0] = "(" + 
            	Integer.toString((int)(getTextColor().r)) + "," + 
            	Integer.toString((int)(getTextColor().g)) + "," + 
            	Integer.toString((int)(getTextColor().b)) + "," + 
            	Integer.toString((int)(getTextColor().a)) + ")";
        } else if (property == "textsize") {
            value[0] = Integer.toString(getTextSize());
        } else if (property == "buttons") {
            List<String> buttons = new ArrayList<String>();
            for (SharedWidgetPtr<Button> it : m_Buttons) {
                buttons.add(it.get().getText());
            }
            Defines.encodeList(buttons, value);
        } else if (property == "callback") {
            String[] tempValue = new String[1];
            //Widget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(MessageBoxCallbacks.ButtonClicked.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(MessageBoxCallbacks.ButtonClicked.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(MessageBoxCallbacks.ButtonClicked.value()).get(0) == null)) {
                callbacks.add("ButtonClicked");
			}

            Defines.encodeList(callbacks, value);

            if (value[0].length() == 0) {
                value[0] = tempValue[0];
        	} else if (tempValue[0].length() != 0) {
                value[0] += "," + tempValue[0];
            }
        } else {
        	//ChildWindow::
            return super.getProperty(property, value);
        }
        // You pass here when one of the properties matched
        return true;
	}
	
	public List<Pair<String, String>> getPropertyList() {
        //ChildWindow::
		List<Pair<String, String>> list = super.getPropertyList();
        list.add(new Pair<String, String>("Text", "string"));
        list.add(new Pair<String, String>("TextColor", "color"));
        list.add(new Pair<String, String>("TextSize", "uint"));
        list.add(new Pair<String, String>("Buttons", "string"));
        return list;
	}
	
	protected void rearrange() {
        if (!m_Loaded) {
            return;
        }

        float buttonWidth = 5.0f * m_TextSize;
        float buttonHeight = m_TextSize * 10.0f / 8.0f;
        for (int i = 0; i < m_Buttons.size(); ++i) {
            float width = new Text(m_Buttons.get(i).get().getText(), 
            	m_Buttons.get(i).get().getTextFont(), m_TextSize)
            		.getLocalBounds().width;
            if (buttonWidth < width * 10.0f / 9.0f) {
                buttonWidth = width * 10.0f / 9.0f;
            }
        }

        float distance = buttonHeight * 2.0f / 3.0f;
        float buttonsAreaWidth = distance;
        for (int i = 0; i < m_Buttons.size(); ++i) {
            m_Buttons.get(i).get().setSize(buttonWidth, buttonHeight);
            buttonsAreaWidth += m_Buttons.get(i).get().getSize().x + distance;
        }

        float width = 2*distance + m_Label.get().getSize().x;
        float height = 3*distance + m_Label.get().getSize().y + buttonHeight;

        if (buttonsAreaWidth > width) {
            width = buttonsAreaWidth;
        }

        setSize(width, height);

        m_Label.get().setPosition(distance, distance);

        float leftPosition = 0;
        float topPosition = 2 * distance + m_Label.get().getSize().y;
        for (int i = 0; i < m_Buttons.size(); ++i) {
            leftPosition += distance + ((width - buttonsAreaWidth) / (m_Buttons.size()+1));
            m_Buttons.get(i).get().setPosition(leftPosition, topPosition);
            leftPosition += m_Buttons.get(i).get().getSize().x;
        }
	}
	
	protected void ButtonClickedCallbackFunction(Callback callback) {
        if (this.getCallbackManager().m_CallbackFunctions.get(MessageBoxCallbacks.ButtonClicked.value()).isEmpty() == false) {
        	this.getCallbackManager().m_Callback.trigger = MessageBoxCallbacks.ButtonClicked.value();
        	this.getCallbackManager().m_Callback.text = ((Button)(callback.widget)).getText();
            addCallback();
        }
	}
}
