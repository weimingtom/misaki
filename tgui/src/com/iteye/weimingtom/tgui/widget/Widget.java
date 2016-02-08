package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.Call0;
import com.iteye.weimingtom.tgui.Callback;
import com.iteye.weimingtom.tgui.CallbackManager;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.Transformable;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Drawable;
import com.iteye.weimingtom.tgui.sf.Keyboard;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Time;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20151004
 * @author Administrator
 *
 */
public abstract class Widget extends Transformable implements Drawable {
   public static enum WidgetCallbacks {
        None(0),                 
        Focused(1),              
        Unfocused(2),            
        MouseEntered(4),         
        MouseLeft(8),            
        WidgetCallbacksCount(16);
        
        int value;
        
        WidgetCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    };    

	protected boolean m_Enabled;
    protected boolean m_Visible;
    protected boolean m_Loaded;
    protected int m_WidgetPhase;
    protected Container m_Parent;
    protected int/*byte*/ m_Opacity;
    protected boolean m_MouseHover;
    protected boolean m_MouseDown;
    protected boolean m_Focused;
    protected boolean m_AllowFocus;
    protected boolean m_AnimatedWidget;
    public/*protected*/ Time m_AnimationTimeElapsed;
    protected boolean m_DraggableWidget;
    protected boolean m_ContainerWidget;
    
    //----------------------
	
	//多重继承
	private CallbackManager _callbackManager = new CallbackManager();
	
	public CallbackManager getCallbackManager() {
		return _callbackManager;
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		
	}

	@Override
	public void setSize(float width, float height) {
		
	}

	@Override
	public Vector2f getSize() {
		return null;
	}
	
	//------------------------
	
    public Widget() {
        m_Enabled = true;
        m_Visible = true;
        m_Loaded = false;
        m_WidgetPhase = 0;
        m_Parent = null;
        m_Opacity = 255;
        m_MouseHover = false;
        m_MouseDown = false;
        m_Focused = false;
        m_AllowFocus = false;
        m_AnimatedWidget = false;
        m_DraggableWidget = false;
        m_ContainerWidget = false;
        
        this.getCallbackManager().m_Callback.widget = null;
        this.getCallbackManager().m_Callback.widgetType = WidgetTypes.Type_Unknown;
        this.getCallbackManager().m_Callback.id = 0;
    }

    public Widget(Widget copy) {
        //sf::Drawable     (copy),
        //Transformable    (copy),
        super(copy);
        _callbackManager = new CallbackManager(copy._callbackManager);
        m_Enabled = copy.m_Enabled;
        m_Visible = copy.m_Visible;
        m_Loaded = copy.m_Loaded;
        m_WidgetPhase = copy.m_WidgetPhase;
        m_Parent = copy.m_Parent;
        m_Opacity = copy.m_Opacity;
        m_MouseHover = false;
        m_MouseDown = false;
        m_Focused = false;
        m_AllowFocus = copy.m_AllowFocus;
        m_AnimatedWidget = copy.m_AnimatedWidget;
        m_DraggableWidget = copy.m_DraggableWidget;
        m_ContainerWidget = copy.m_ContainerWidget;
        
        this.getCallbackManager().m_Callback.widget = null;
    }

    public void destroy() {
    	super.destroy();
    }
    
    public Widget assign(Widget right) {
        if (this != right) {
            //this->sf::Drawable::operator=(right);
            //this->Transformable::operator=(right);
            //this->CallbackManager::operator=(right);
            super.assign(right);
            m_Enabled = right.m_Enabled;
            m_Visible = right.m_Visible;
            m_Loaded = right.m_Loaded;
            m_WidgetPhase = right.m_WidgetPhase;
            m_Parent = right.m_Parent;
            m_Opacity = right.m_Opacity;
            m_MouseHover = false;
            m_MouseDown = false;
            m_Focused = false;
            m_AllowFocus  = right.m_AllowFocus;
            m_AnimatedWidget = right.m_AnimatedWidget;
            m_DraggableWidget = right.m_DraggableWidget;
            m_ContainerWidget = right.m_ContainerWidget;
            this.getCallbackManager().m_Callback = new Callback();
            this.getCallbackManager().m_Callback.widget = null;
            this.getCallbackManager().m_Callback.widgetType = right.getCallbackManager().m_Callback.widgetType;
            this.getCallbackManager().m_Callback.id = right.getCallbackManager().m_Callback.id;
        }

        return this;
    }

    //FIXME: =clone
    public Widget cloneObj() {
    	return null;
    }

    public void show() {
        m_Visible = true;
    }

    public void hide() {
        m_Visible = false;

        // If the widget is focused then it must be unfocused
        unfocus();
    }
    
    public boolean isVisible() {
        return m_Visible;
    }

    public void enable() {
        m_Enabled = true;
    }

    public void disable() {
        m_Enabled = false;

        // Change the mouse button state.
        m_MouseHover = false;
        m_MouseDown = false;

        // If the widget is focused then it must be unfocused
        unfocus();
    }

    public boolean isEnabled() {
        return m_Enabled;
    }

    public boolean isDisabled() {
        return !m_Enabled;
    }

    public boolean isLoaded() {
        return m_Loaded;
    }

    public void focus() {
        if (m_Parent != null) {
            m_Parent.focusWidget(this);
        }
    }
    
    public void unfocus() {
        if (m_Focused) {
            m_Parent.unfocusWidgets();
        }
    }

    public boolean isFocused() {
    	return m_Focused;
    }

    public WidgetTypes getWidgetType() {
    	return this.getCallbackManager().m_Callback.widgetType;
    }

    public Container getParent() {
        return m_Parent;
    }

    public void setTransparency(int transparency) {
        m_Opacity = transparency;
    }

    public int getTransparency() {
        return m_Opacity;
    }

    public void moveToFront() {
        m_Parent.moveWidgetToFront(this);
    }

    public void moveToBack() {
        m_Parent.moveWidgetToBack(this);
    }

    public void setCallbackId(int callbackId) {
        this.getCallbackManager().m_Callback.id = callbackId;    	
    }

    public int getCallbackId() {
        return this.getCallbackManager().m_Callback.id;
    }

    public void update() {
    	//do nothing
    }

    public void addCallback() {
        // Loop through all callback functions
    	List<Call0> functions = this.getCallbackManager().m_CallbackFunctions.get(this.getCallbackManager().m_Callback.trigger);
        //.cbegin();
    	for (Call0 func : functions) {
            if (func != null) {
            	func.call();
            } else {
                m_Parent.addChildCallback(this.getCallbackManager().m_Callback);
            }
        }
    }

    public void mouseEnteredWidget() {
        if (this.getCallbackManager().m_CallbackFunctions.get(WidgetCallbacks.MouseEntered.value()).isEmpty() == false) {
            this.getCallbackManager().m_Callback.trigger = WidgetCallbacks.MouseEntered.value();
            addCallback();
        }
    }
    
    public void mouseLeftWidget() {
        if (this.getCallbackManager().m_CallbackFunctions.get(WidgetCallbacks.MouseLeft.value()).isEmpty() == false) {
        	this.getCallbackManager().m_Callback.trigger = WidgetCallbacks.MouseLeft.value();
            addCallback();
        }
    }

    public abstract boolean mouseOnWidget(float x, float y);

    public void leftMousePressed(float x, float y) {
    	//do nothing
    }
    
    public void leftMouseReleased(float x, float y) {
    	//do nothing
    }

    public void mouseMoved(float x, float y) {
        if (m_MouseHover == false)
            mouseEnteredWidget();

        m_MouseHover = true;
    }

    public void keyPressed(Keyboard.Key key) {
    	//do nothing
    }

    public void textEntered(int key) {
    	//do nothing
    }

    public void mouseWheelMoved(int delta, int x, int y) {
    	//do nothing
    }

    public void widgetFocused() {
        if (this.getCallbackManager().m_CallbackFunctions.get(WidgetCallbacks.Focused.value()).isEmpty() == false) {
        	this.getCallbackManager().m_Callback.trigger = WidgetCallbacks.Focused.value();
            addCallback();
        }

        if (m_Parent != null) {
            m_Parent.focus();
        }
    }

    public void widgetUnfocused() {
        if (this.getCallbackManager().m_CallbackFunctions.get(WidgetCallbacks.Unfocused.value()).isEmpty() == false) {
            this.getCallbackManager().m_Callback.trigger = WidgetCallbacks.Unfocused.value();
            addCallback();
        }    	
    }

    public void mouseNotOnWidget() {
        if (m_MouseHover == true) {
            mouseLeftWidget();
        }

        m_MouseHover = false;
    }

    public void mouseNoLongerDown() {
        m_MouseDown = false;    	
    }

    public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("left".equals(property)) {
            setPosition((float)(Float.parseFloat(value)), 
            	getPosition().y);
        } else if ("top".equals(property)) {
            setPosition(getPosition().x, 
            	(float)(Float.parseFloat(value)));
        } else if ("width".equals(property)) {
            setSize((float)(Float.parseFloat(value)), 
            	getSize().y);
        } else if ("height".equals(property)) {
            setSize(getSize().x,
            	(float)(Float.parseFloat(value)));
        } else if ("visible".equals(property)) {
            if (("true".equals(value)) || ("True".equals(value))) {
                m_Visible = true;
            } else if (("false".equals(value)) || ("False".equals(value))) {
                m_Visible = false;
        	} else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Visible' property.");
            }
       } else if ("enabled".equals(property)) {
            if (("true".equals(value)) || ("True".equals(value))) {
                m_Enabled = true;
            } else if (("false".equals(value)) || ("False".equals(value))) {
                m_Enabled = false;
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Enabled' property.");
            }
        } else if ("transparency".equals(property)) {
            setTransparency((int)(Integer.parseInt(value.trim())));
        } else if ("callbackid".equals(property)) {
            this.getCallbackManager().m_Callback.id = 
            	(int)(Integer.parseInt(value.trim()));
        } else if ("callback".equals(property)) {
            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("Focused".equals(it)) || ("focused".equals(it))) {
                    this.getCallbackManager().bindCallback(WidgetCallbacks.Focused.value());
            	} else if (("Unfocused".equals(it)) || ("unfocused".equals(it))) {
            		this.getCallbackManager().bindCallback(WidgetCallbacks.Unfocused.value());
        		} else if (("MouseEntered".equals(it)) || ("mouseentered".equals(it))) {
        			this.getCallbackManager().bindCallback(WidgetCallbacks.MouseEntered.value());
    			} else if (("MouseLeft".equals(it)) || ("mouseleft".equals(it))) {
    				this.getCallbackManager().bindCallback(WidgetCallbacks.MouseLeft.value());
    			}
            }
        } else {
        	return false;
        }
        return true;
    }

    public boolean getProperty(String property, String[] value) {
        property = Defines.toLower(property);

        if ("left".equals(property)) {
            value[0] = Float.toString(getPosition().x);
        } else if ("top".equals(property)) {
            value[0] = Float.toString(getPosition().y);
    	} else if ("width".equals(property)) {
            value[0] = Float.toString(getSize().x);
		} else if ("height".equals(property)) {
            value[0] = Float.toString(getSize().y);
		} else if ("visible".equals(property)) {
            value[0] = m_Visible ? "true" : "false";
		} else if ("enabled".equals(property)) {
            value[0] = m_Enabled ? "true" : "false";
		} else if ("transparency".equals(property)) {
            value[0] = Integer.toString((int)(getTransparency()));
		} else if ("callbackid".equals(property)) {
            value[0] = Integer.toString(this.getCallbackManager().m_Callback.id);
		} else if ("callback".equals(property)) {
            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.Focused.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.Focused.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.Focused.value()).get(0) == null)) {
                callbacks.add("Focused");
			}
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.Unfocused.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.Unfocused.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.Unfocused.value()).get(0) == null)) {
                callbacks.add("Unfocused");
			}
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.MouseEntered.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.MouseEntered.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.MouseEntered.value()).get(0) == null)) {
                callbacks.add("MouseEntered");
			}
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.MouseLeft.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.MouseLeft.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(WidgetCallbacks.MouseLeft.value()).get(0) == null)) {
                callbacks.add("MouseLeft");
			}
            Defines.encodeList(callbacks, value);
        } else {
            return false;
        }
        return true;
    }

    public List<Pair<String, String>> getPropertyList() {
    	List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        list.add(new Pair<String, String>("Left", "int"));
        list.add(new Pair<String, String>("Top", "int"));
        list.add(new Pair<String, String>("Width", "uint"));
        list.add(new Pair<String, String>("Height", "uint"));
        list.add(new Pair<String, String>("Visible", "bool"));
        list.add(new Pair<String, String>("Enabled", "bool"));
        list.add(new Pair<String, String>("Transparency", "byte"));
        list.add(new Pair<String, String>("Callback", "custom"));
        list.add(new Pair<String, String>("CallbackId", "uint"));
        return list;
    }

    //FIXME:
    /*private*/ void initialize(Container parent) {
        m_Parent = parent;
    }
}
