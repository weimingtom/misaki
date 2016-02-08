package com.iteye.weimingtom.tgui;

public class WidgetBordersImpl {
	public /*protected*/ int m_LeftBorder;
	public /*protected*/ int m_TopBorder;
	public /*protected*/ int m_RightBorder;
	public /*protected*/ int m_BottomBorder;

    public WidgetBordersImpl() {
        m_LeftBorder = 0;
        m_TopBorder = 0;
        m_RightBorder = 0;
        m_BottomBorder = 0;
    }

    public WidgetBordersImpl(WidgetBordersImpl copy) {
        m_LeftBorder = copy.m_LeftBorder;
        m_TopBorder = copy.m_TopBorder;
        m_RightBorder = copy.m_RightBorder;
        m_BottomBorder = copy.m_BottomBorder;
    }
    
    public Borders getBorders() {
    	return new Borders(m_LeftBorder, m_TopBorder, m_RightBorder, m_BottomBorder);
    }
}
