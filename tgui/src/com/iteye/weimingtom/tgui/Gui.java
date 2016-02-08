package com.iteye.weimingtom.tgui;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.iteye.weimingtom.tgui.sf.Clock;
import com.iteye.weimingtom.tgui.sf.Event;
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderWindow;
import com.iteye.weimingtom.tgui.sf.Time;
import com.iteye.weimingtom.tgui.sf.Vector2f;
import com.iteye.weimingtom.tgui.sf.Vector2i;
import com.iteye.weimingtom.tgui.sf.View;
import com.iteye.weimingtom.tgui.widget.GuiContainer;
import com.iteye.weimingtom.tgui.widget.Widget;

public class Gui {
    protected Queue<Callback> m_Callback = new LinkedList<Callback>();
    protected Clock m_Clock = new Clock();
    protected RenderWindow m_Window;
    protected boolean m_Focused;
    protected GuiContainer m_Container = new GuiContainer();

	public Gui() {
	    m_Window = null;
	    m_Focused = false;
	    
	    //&Gui::addChildCallback
        m_Container.bindGlobalCallback("addChildCallback", this);

        m_Container.m_ContainerFocused = true;
	}

	public Gui(RenderWindow window) {
	    m_Window = window; //new RenderWindow(window);
	    m_Focused = false;
		
        m_Container.m_Window = window;
        //&Gui::addChildCallback
        m_Container.bindGlobalCallback("addChildCallback", this);

        m_Container.m_ContainerFocused = true;
	}
	
	public void setWindow(RenderWindow window) {
        m_Window = window;
        m_Container.m_Window = window;
	}

	public RenderWindow getWindow() {
		return m_Window;
	}

	public boolean handleEvent(Event event) {
        if (event.type == Event.EventType.MouseMoved) {
            Vector2f mouseCoords = m_Window.mapPixelToCoords(
            	new Vector2i(event.mouseMove.x, event.mouseMove.y), m_Window.getView());

            event.mouseMove.x = (int)(mouseCoords.x + 0.5f);
            event.mouseMove.y = (int)(mouseCoords.y + 0.5f);
        } else if ((event.type == Event.EventType.MouseButtonPressed) || 
        	(event.type == Event.EventType.MouseButtonReleased)) {
            Vector2f mouseCoords = m_Window.mapPixelToCoords(
            	new Vector2i(event.mouseButton.x, event.mouseButton.y), m_Window.getView());

            event.mouseButton.x = (int)(mouseCoords.x + 0.5f);
            event.mouseButton.y = (int)(mouseCoords.y + 0.5f);
        } else if (event.type == Event.EventType.MouseWheelMoved) {
            Vector2f mouseCoords = m_Window.mapPixelToCoords(
            	new Vector2i(event.mouseWheel.x, event.mouseWheel.y), m_Window.getView());

            event.mouseWheel.x = (int)(mouseCoords.x + 0.5f);
            event.mouseWheel.y = (int)(mouseCoords.y + 0.5f);
        } else if (event.type == Event.EventType.LostFocus) {
            m_Focused = false;
        } else if (event.type == Event.EventType.GainedFocus) {
            m_Focused = true;
        }

        return m_Container.handleEvent(event);
	}

	public void draw() {
		draw(false);
	}
	
    public void draw(boolean resetView) {
        View oldView = m_Window.getView();

        if (resetView) {
            m_Window.setView(m_Window.getDefaultView());
        }
        
        if (m_Focused) {
            updateTime(m_Clock.restart());
        } else {
            m_Clock.restart();
        }
        
        boolean clippingEnabled = GL.glIsEnabled(GL.GL_SCISSOR_TEST);
        int[] scissor = new int[4];

        if (clippingEnabled) {
            GL.glGetIntegerv(GL.GL_SCISSOR_BOX, scissor);
        } else {
            GL.glEnable(GL.GL_SCISSOR_TEST);
        	GL.glScissor(0, 0, m_Window.getSize().x, m_Window.getSize().y);
        }

        m_Container.drawWidgetContainer(m_Window, RenderStates.Default);

        if (clippingEnabled) {
            GL.glScissor(scissor[0], scissor[1], scissor[2], scissor[3]);
    	} else {
    		GL.glDisable(GL.GL_SCISSOR_TEST);
        }
        
        m_Window.setView(oldView);
    }

    public boolean pollCallback(Callback callback) {
        if (m_Callback.isEmpty()) {
            return false;
        } else {
            callback = m_Callback.peek();

            m_Callback.remove();

            return true;
        }
    }

    public boolean hasFocus() {
    	return m_Focused;
    }

    public Vector2f getSize() {
    	return new Vector2f(m_Window.getSize());
    }

    public boolean setGlobalFont(String filename) {
    	return m_Container.setGlobalFont(filename);
    }

    public void setGlobalFont(Font font) {
    	m_Container.setGlobalFont(font);
    }

    public Font getGlobalFont() {
    	return m_Container.getGlobalFont();
    }

    public List<SharedWidgetPtr<? extends Widget>> getWidgets() {
    	return m_Container.getWidgets();
    }

    public List<String> getWidgetNames() {
    	return m_Container.getWidgetNames();
    }

    public void add(SharedWidgetPtr<? extends Widget> widgetPtr) {
    	add(widgetPtr, "");
    }
    
    public void add(SharedWidgetPtr<? extends Widget> widgetPtr, String widgetName) {
    	m_Container.add(widgetPtr, widgetName);
    }

    public SharedWidgetPtr<? extends Widget> get(String widgetName) {
    	return m_Container.get(widgetName);
    }

    //FIXME:not necessary?
    public SharedWidgetPtr<Widget> getWidget(String widgetName) {
        return (SharedWidgetPtr<Widget>)m_Container.get(widgetName);
    }

    public SharedWidgetPtr<? extends Widget> copy(SharedWidgetPtr<? extends Widget> oldWidget) {
    	return copy(oldWidget, "");
    }
    
    public SharedWidgetPtr<? extends Widget> copy(SharedWidgetPtr<? extends Widget> oldWidget, String newWidgetName) {
    	return m_Container.copy(oldWidget, newWidgetName);
    }

    public void remove(SharedWidgetPtr<? extends Widget> widget) {
    	m_Container.remove(widget);
    }

    public void removeAllWidgets() {
    	m_Container.removeAllWidgets();
    }

    public boolean setWidgetName(SharedWidgetPtr<? extends Widget> widget, String name) {
    	return m_Container.setWidgetName(widget, name);
    }

    public boolean getWidgetName(SharedWidgetPtr<? extends Widget> widget, String[] name) {
    	return m_Container.getWidgetName(widget, name);
    }

    public void focusWidget(SharedWidgetPtr<? extends Widget> widget) {
    	m_Container.focusWidget(widget.get());
    }

    public void focusNextWidget() {
    	m_Container.focusNextWidget();
    }

    public void focusPreviousWidget() {
    	m_Container.focusPreviousWidget();
    }

    public void unfocusWidgets() {
    	m_Container.unfocusWidgets();
    }

    public void uncheckRadioButtons() {
    	m_Container.uncheckRadioButtons();
    }

    public void moveWidgetToFront(SharedWidgetPtr<? extends Widget> widget) {
    	m_Container.moveWidgetToFront(widget.get());
    }

    public void moveWidgetToBack(SharedWidgetPtr<? extends Widget> widget) {
    	m_Container.moveWidgetToBack(widget.get());
    }

    public void bindGlobalCallback(CallbackFunction func) {
    	m_Container.bindGlobalCallback(func);
    }

    public void bindGlobalCallback(Object func, Object classPtr) {
        m_Container.bindGlobalCallback(func, classPtr);
    }

    public void unbindGlobalCallback() {
    	m_Container.unbindGlobalCallback();
    }

    public boolean loadWidgetsFromFile(String filename) {
    	return m_Container.loadWidgetsFromFile(filename);
    }

    public boolean saveWidgetsToFile(String filename) {
    	return m_Container.saveWidgetsToFile(filename);
    }

    public void updateTime(Time elapsedTime) {
        m_Container.m_AnimationTimeElapsed = elapsedTime;
        m_Container.update();
    }

    public void addChildCallback(Callback callback) {
        m_Callback.add(callback);
    }
}
