package com.iteye.weimingtom.tgui;

import com.iteye.weimingtom.tgui.widget.Container;
import com.iteye.weimingtom.tgui.widget.Widget;

public class SharedWidgetPtr<T extends Widget> {
    private T m_WidgetPtr;
    private Integer m_RefCount;
	
    public T get() {
    	return m_WidgetPtr;
    }
    
    public SharedWidgetPtr(T instance) {
    	m_WidgetPtr = null;
    	init(instance);
    }

    public SharedWidgetPtr(T instance, Gui gui) {
    	this(instance, gui, "");
    }
    public SharedWidgetPtr(T instance, Gui gui, String widgetName) {
    	m_WidgetPtr = null;
    	init(instance);
    	gui.m_Container.add(this, widgetName);
	}

    public SharedWidgetPtr(T instance, Container container) {
    	this(instance, container, "");
    }
    public SharedWidgetPtr(T instance, Container container, String widgetName) {
    	m_WidgetPtr = null;
    	init(instance);
    	container.add(this, widgetName);
	}    
    
	private void init(T instance) {
	    reset();

        m_RefCount = new Integer(1);
        
        m_WidgetPtr = instance;
        m_WidgetPtr.getCallbackManager().m_Callback.widget = get();
	}
	
	private void reset() {
        if (m_WidgetPtr != null) {
            if (m_RefCount == 1) {
                m_WidgetPtr.destroy();
                m_WidgetPtr = null;
                m_RefCount = null;
            } else {
                m_RefCount -= 1;
            }
        }
	}
	
    public SharedWidgetPtr(SharedWidgetPtr<T> copy) {
        if (copy.get() != null) {
            m_WidgetPtr = copy.get();

            m_RefCount = copy.getRefCount();
            m_RefCount += 1;
        } else {
            m_WidgetPtr = null;
            m_RefCount = null;
        }
    }
    
    public Integer getRefCount() {
        return m_RefCount;
    }
    
    public SharedWidgetPtr<T> cloneObj() {
        if (m_WidgetPtr != null) {
            SharedWidgetPtr<T> pointer = new SharedWidgetPtr<T>((T)null);

            pointer.m_RefCount = new Integer(1);

            pointer.m_WidgetPtr = (T)m_WidgetPtr.cloneObj();
            pointer.m_WidgetPtr.getCallbackManager().m_Callback.widget = pointer.get();
            return pointer;
        } else {
            return null;
        }
    }
}
