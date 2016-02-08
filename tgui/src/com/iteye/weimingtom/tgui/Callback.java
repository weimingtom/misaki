package com.iteye.weimingtom.tgui;

import com.iteye.weimingtom.tgui.sf.Vector2f;
import com.iteye.weimingtom.tgui.sf.Vector2i;
import com.iteye.weimingtom.tgui.widget.Widget;

public class Callback {
    public int id;
    public int trigger;
    public Widget widget;
    public WidgetTypes widgetType = WidgetTypes.Type_Unknown; //FIXME:
    public Vector2i mouse = new Vector2i();
    public String text = "";
    public boolean checked;
    public int value;
    public Vector2f value2d = new Vector2f();
    public Vector2f position = new Vector2f();
    public Vector2f size = new Vector2f();
    public int index;
    
    public Callback() {
    	widget = null;
    }
}
