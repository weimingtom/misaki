package com.iteye.weimingtom.tgui.widget;

import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.RenderWindow;
import com.iteye.weimingtom.tgui.sf.Vector2f;

public class GuiContainer extends Container {
	/*protected*/public RenderWindow m_Window;
	
	public void unbindGlobalCallback() {
        //FIXME:
		//m_GlobalCallbackFunctions.erase(++m_GlobalCallbackFunctions.begin(), m_GlobalCallbackFunctions.end());
        m_GlobalCallbackFunctions.clear();
	}
	
	public void setSize(float width, float height) {
		//do nothing
	}

	public Vector2f getSize() {
        return new Vector2f(m_Window.getSize());
	}

	public GuiContainer cloneObj() {
		return null;
	}

	public boolean mouseOnWidget(float x, float y) {
		return false;
	}

	public void draw(RenderTarget target, RenderStates states) {
		//do nothing
	}
	
	/*
	public void bindGlobalCallback(Object obj1, Object obj2) {
		//FIXME:
	}
	*/
	
	public String get(Object obj) {
		//FIXME:
		return null;
	}
}