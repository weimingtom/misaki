package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.FloatRect;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20150926
 * @author Administrator
 *
 */
public class ClickableWidget extends Widget {
	public static enum ClickableWidgetCallbacks {
        LeftMousePressed(WidgetCallbacks.WidgetCallbacksCount.value() * 1),
        LeftMouseReleased(WidgetCallbacks.WidgetCallbacksCount.value() * 2),
        LeftMouseClicked(WidgetCallbacks.WidgetCallbacksCount.value() * 4),
        AllClickableWidgetCallbacks(WidgetCallbacks.WidgetCallbacksCount.value() * 8 - 1),
        ClickableWidgetCallbacksCount(WidgetCallbacks.WidgetCallbacksCount.value() * 8);
        
        int value;
        
        ClickableWidgetCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }

    protected Vector2f m_Size;
    
	public ClickableWidget() {
		m_Size = new Vector2f(0, 0);
		this.getCallbackManager().m_Callback.widgetType = 
			WidgetTypes.Type_ClickableWidget;
	}

	public ClickableWidget(ClickableWidget copy) {
	    //Widget(copy),
	    super(copy);
		this.m_Size = new Vector2f(copy.m_Size);
	}

	public void destroy() {
		super.destroy();
	}

	public ClickableWidget assign(ClickableWidget right) {
        if (this != right) {
            ClickableWidget temp = new ClickableWidget(right);
            //this->Widget::operator=(right);
            super.assign(right);
            
            m_Size = temp.m_Size;
        }
        return this;
	}

	public ClickableWidget cloneObj() {
		return new ClickableWidget(this);
	}

	public void setSize(float width, float height) {
        m_Size.x = width;
        m_Size.y = height;
	}

	public Vector2f getSize() {
        return m_Size;
	}

	public void leftMousePressed(float x, float y) {
        m_MouseDown = true;

        if (this.getCallbackManager()
        		.m_CallbackFunctions
        		.get(ClickableWidgetCallbacks.LeftMousePressed.value())
        		.isEmpty() == false) {
            this.getCallbackManager().m_Callback.trigger = ClickableWidgetCallbacks.LeftMousePressed.value();
            this.getCallbackManager().m_Callback.mouse.x = (int)(x - getPosition().x);
            this.getCallbackManager().m_Callback.mouse.y = (int)(y - getPosition().y);
            addCallback();
        }
	}

	public void leftMouseReleased(float x, float y) {
        if (this.getCallbackManager()
        		.m_CallbackFunctions
        		.get(ClickableWidgetCallbacks.LeftMouseReleased.value())
        		.isEmpty() == false) {
            this.getCallbackManager().m_Callback.trigger = ClickableWidgetCallbacks.LeftMouseReleased.value();
            this.getCallbackManager().m_Callback.mouse.x = (int)(x - getPosition().x);
            this.getCallbackManager().m_Callback.mouse.y = (int)(y - getPosition().y);
            addCallback();
        }

        if (m_MouseDown == true) {
        	if (this.getCallbackManager()
            		.m_CallbackFunctions
            		.get(ClickableWidgetCallbacks.LeftMouseClicked.value())
            		.isEmpty() == false) {
            	this.getCallbackManager().m_Callback.trigger = ClickableWidgetCallbacks.LeftMouseClicked.value();
            	this.getCallbackManager().m_Callback.mouse.x = (int)(x - getPosition().x);
            	this.getCallbackManager().m_Callback.mouse.y = (int)(y - getPosition().y);
                addCallback();
            }

            m_MouseDown = false;
        }
	}

	public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("callback".equals(property)) {
            //Widget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("LeftMousePressed".equals(it)) || ("leftmousepressed".equals(it))) {
                    this.getCallbackManager().bindCallback(ClickableWidgetCallbacks.LeftMousePressed.value()); 
                } else if (("LeftMouseReleased".equals(it)) || ("leftmousereleased".equals(it))) {
                	this.getCallbackManager().bindCallback(ClickableWidgetCallbacks.LeftMouseReleased.value());
                } else if (("LeftMouseClicked".equals(it)) || ("leftmouseclicked".equals(it))) {
                	this.getCallbackManager().bindCallback(ClickableWidgetCallbacks.LeftMouseClicked.value());
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

        if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //Widget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();;

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(ClickableWidgetCallbacks.LeftMousePressed.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ClickableWidgetCallbacks.LeftMousePressed.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ClickableWidgetCallbacks.LeftMousePressed.value()).get(0) == null)) {
            	callbacks.add("LeftMousePressed");
        	}
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(ClickableWidgetCallbacks.LeftMouseReleased.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ClickableWidgetCallbacks.LeftMouseReleased.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ClickableWidgetCallbacks.LeftMouseReleased.value()).get(0) == null)) {
            	callbacks.add("LeftMouseReleased");
        	}
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(ClickableWidgetCallbacks.LeftMouseClicked.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ClickableWidgetCallbacks.LeftMouseClicked.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(ClickableWidgetCallbacks.LeftMouseClicked.value()).get(0) == null)) {
                callbacks.add("LeftMouseClicked");
        	}
            Defines.encodeList(callbacks, value);

            if (value[0] == null || value[0].length() == 0 || 
            	tempValue[0] == null || tempValue[0].length() == 0) {
                value[0] += tempValue;
        	} else {
                value[0] += "," + tempValue;
            }
        } else {
        	//Widget::
            return super.getProperty(property, value);
        }
        return true;
	}

	public void draw(RenderTarget target, RenderStates states) {
		
	}

	@Override
	public boolean mouseOnWidget(float x, float y) {
        if (getTransform().transformRect(
        		new FloatRect(0, 0, getSize().x, getSize().y))
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
