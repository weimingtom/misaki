package com.iteye.weimingtom.tgui;

public enum WidgetPhase {
    WidgetPhase_Hover(1),
    WidgetPhase_MouseDown(2),
    WidgetPhase_Focused(4),
    WidgetPhase_Selected(8);
    
    private int value;
    
    private WidgetPhase(int value) {
    	this.value = value;
    }
    
    public int value() {
    	return value;
    }
}
