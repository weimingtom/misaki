package com.iteye.weimingtom.tgui.widget;

import java.util.List;

import com.iteye.weimingtom.tgui.Borders;
import com.iteye.weimingtom.tgui.SharedWidgetPtr;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20151011
 * @author Administrator
 *
 */
public class Grid extends Container {
	public static enum GridCallbacks {
	    AllGridCallbacks(WidgetCallbacks.WidgetCallbacksCount.value() - 1), 
	    GridCallbacksCount(WidgetCallbacks.WidgetCallbacksCount.value());
	
        int value;
        
        GridCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
	}
		
    public static enum Layout {
        UpperLeft, 
        Up, 
        UpperRight, 
        Right,    
        BottomRight, 
        Bottom,   
        BottomLeft,  
        Left,      
        Center  
    }
	
	protected List<List<SharedWidgetPtr<? extends Widget>>> m_GridWidgets;
	protected List<List<Borders>> m_ObjBorders;
	protected List<List<Layout>> m_ObjLayout;

	protected List<Integer> m_RowHeight;
	protected List<Integer> m_ColumnWidth;

	protected Vector2f m_Size; 
	protected Vector2f m_IntendedSize; 

    public Grid() {
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_Grid;

        m_Loaded = true;
    }
	
	public Grid(Grid gridToCopy) {
	    super(gridToCopy);
	    m_Size = gridToCopy.m_Size;
	    m_IntendedSize = gridToCopy.m_IntendedSize;
	    
	    List<SharedWidgetPtr<? extends Widget>> widgets = gridToCopy.m_Widgets;

        for (int row = 0; row < gridToCopy.m_GridWidgets.size(); ++row) {
            for (int col = 0; col < gridToCopy.m_GridWidgets.get(row).size(); ++col) {
                for (int i = 0; i < widgets.size(); ++i) {
                    if (widgets.get(i) == gridToCopy.m_GridWidgets.get(row).get(col)) {
                        addWidget(widgets.get(i), 
                        	row, col, 
                        	gridToCopy.m_ObjBorders.get(row).get(col), 
                        	gridToCopy.m_ObjLayout.get(row).get(col));
                    }
                }
            }
        }
	}
	
	public void destroy() {
		super.destroy();
	}
	
	public Grid assign(Grid right) {
        if (this != right) {
            Grid temp = new Grid(right);
            //this->Container::operator=(right);
            super.assign(right);
            
            m_GridWidgets = temp.m_GridWidgets;
            m_ObjBorders = temp.m_ObjBorders;
            m_ObjLayout = temp.m_ObjLayout;
            m_RowHeight = temp.m_RowHeight;
            m_ColumnWidth = temp.m_ColumnWidth;
            m_Size = temp.m_Size;
            m_IntendedSize = temp.m_IntendedSize;
        }

        return this;
	}
	
	public Grid cloneObj() {
        return new Grid(this);
	}
	
	public void setSize(float width, float height) {
        if (width  < 0) {
        	width  = -width;
        }
        if (height < 0) {
        	height = -height;
        }

        m_IntendedSize.x = width;
        m_IntendedSize.y = height;

        updatePositionsOfAllWidgets();
	}
	
	public Vector2f getSize() {
        return m_Size;
	}

	public void remove(SharedWidgetPtr<? extends Widget> widget) {
        remove(widget.get());
	}

	public void remove(Widget widget) {
	    for (int row = 0; row < m_GridWidgets.size(); ++row) {
            for (int col = 0; col < m_GridWidgets.get(row).size(); ++col) {
                if (m_GridWidgets.get(row).get(col).get() == widget) {
                    m_GridWidgets.get(row).remove(col);
                    m_ObjBorders.get(row).remove(col);
                    m_ObjLayout.get(row).remove(col);

                    if (m_ColumnWidth.size() == m_GridWidgets.get(row).size() + 1) {
                        boolean rowFound = false;
                        for (int i = 0; i < m_GridWidgets.size(); ++i) {
                            if (m_GridWidgets.get(i).size() >= m_ColumnWidth.size()) {
                                rowFound = true;
                                break;
                            }
                        }

                        if (rowFound == false) {
                            m_ColumnWidth.remove(m_ColumnWidth.size() - 1); //FIXME:
                        }
                    }

                    if (m_GridWidgets.get(row).isEmpty()) {
                        m_GridWidgets.remove(row);
                        m_ObjBorders.remove(row);
                        m_ObjLayout.remove(row);
                        m_RowHeight.remove(row);
                    }

                    updatePositionsOfAllWidgets();
                }
            }
        }

        //Container::
        super.remove(widget);
	}	
	
	public void removeAllWidgets() {
        m_GridWidgets.clear();
        m_ObjBorders.clear();
        m_ObjLayout.clear();

        m_RowHeight.clear();
        m_ColumnWidth.clear();

        //Container::
        super.removeAllWidgets();

        m_Size.x = 0;
        m_Size.y = 0;
	}
	
	public void addWidget(SharedWidgetPtr<? extends Widget> widget,
	               int row,
	               int col,
	               Borders borders,
	               Layout layout) {
        if (m_GridWidgets.size() < row + 1) {
        	m_GridWidgets.add(row + 1, null);
            m_ObjBorders.add(row + 1, null);
            m_ObjLayout.add(row + 1, null);
        }

        if (m_GridWidgets.get(row).size() < col + 1) {
            m_GridWidgets.get(row).add(col + 1, null);
            m_ObjBorders.get(row).add(col + 1, null);
            m_ObjLayout.get(row).add(col + 1, null);
        }

        if (m_RowHeight.size() < row + 1) {
            m_RowHeight.add(row + 1, 0);
        }

        if (m_ColumnWidth.size() < col + 1) {
            m_ColumnWidth.add(col + 1, 0);
        }

        m_GridWidgets.get(row).set(col, widget);
        m_ObjBorders.get(row).set(col, borders);
        m_ObjLayout.get(row).set(col, layout);

        updateWidgets();
	}
	
	public SharedWidgetPtr<? extends Widget> getWidget(int row, int col) {
        if ((m_GridWidgets.size() > row) && 
        	(m_GridWidgets.get(row).size() > col)) {
            return m_GridWidgets.get(row).get(col);
        } else {
            return null;
        }
	}
	
	public void updateWidgets() {
        for (int i = 0; i < m_ColumnWidth.size(); ++i) {
        	m_ColumnWidth.set(i, 0);
        }

        for (int row = 0; row < m_GridWidgets.size(); ++row) {
            m_RowHeight.set(row, 0);

            for (int col = 0; col < m_GridWidgets.get(row).size(); ++col) {
                if (m_GridWidgets.get(row).get(col).get() == null) {
                    continue;
                }

                if (m_ColumnWidth.get(col) < m_GridWidgets.get(row).get(col).get().getSize().x + m_ObjBorders.get(row).get(col).left + m_ObjBorders.get(row).get(col).right) {
                    m_ColumnWidth.set(col, (int)(m_GridWidgets.get(row).get(col).get().getSize().x + m_ObjBorders.get(row).get(col).left + m_ObjBorders.get(row).get(col).right));
                }
                
                if (m_RowHeight.get(row) < m_GridWidgets.get(row).get(col).get().getSize().y + m_ObjBorders.get(row).get(col).top + m_ObjBorders.get(row).get(col).bottom) {
                    m_RowHeight.set(row, (int)(m_GridWidgets.get(row).get(col).get().getSize().y + m_ObjBorders.get(row).get(col).top + m_ObjBorders.get(row).get(col).bottom));
                }
            }
        }

        updatePositionsOfAllWidgets();
	}
	
	public void changeWidgetBorders(SharedWidgetPtr<Widget> widget, Borders borders) {
        for (int row = 0; row < m_GridWidgets.size(); ++row) {
            for (int col = 0; col < m_GridWidgets.get(row).size(); ++col) {
                if (m_GridWidgets.get(row).get(col) == widget) {
                    m_ObjBorders.get(row).set(col, borders);

                    updateWidgets();
                }
            }
        }
	}
	
	public void changeWidgetLayout(SharedWidgetPtr<Widget> widget, Layout layout) {
        for (int row = 0; row < m_GridWidgets.size(); ++row) {
            for (int col = 0; col < m_GridWidgets.get(row).size(); ++col) {
                if (m_GridWidgets.get(row).get(col) == widget) {
                    m_ObjLayout.get(row).set(col, layout);

                    {
                        Vector2f availableSpace = new Vector2f();
                        Vector2f minSize = getMinSize();

                        if (m_Size.x > minSize.x) {
                            availableSpace.x = m_Size.x - minSize.x;
                        }
                        if (m_Size.y > minSize.y) {
                            availableSpace.y = m_Size.y - minSize.y;
                        }
                        
                        Vector2f availSpaceOffset = new Vector2f(0.5f * availableSpace.x / m_ColumnWidth.size(),
                                                      0.5f * availableSpace.y / m_RowHeight.size());

                        float left = 0;
                        float top = 0;

                        for (int i = 0; i < row; ++i) {
                            top += (float)(m_RowHeight.get(i)) + 2 * availSpaceOffset.y;
                        }
                        
                        for (int i = 0; i < col; ++i) {
                            left += (float)(m_ColumnWidth.get(i)) + 2 * availSpaceOffset.x;
                        }
                        
                        switch (m_ObjLayout.get(row).get(col)) {
                        case UpperLeft:
                            left += (float)(m_ObjBorders.get(row).get(col).left) + 
                            		availSpaceOffset.x;
                            top += (float)(m_ObjBorders.get(row).get(col).top) + 
                            		availSpaceOffset.y;
                            break;

                        case Up:
                            left += m_ObjBorders.get(row).get(col).left + 
                            		(((m_ColumnWidth.get(col) - 
                            		m_ObjBorders.get(row).get(col).left - 
                            		m_ObjBorders.get(row).get(col).right) - 
                            		m_GridWidgets.get(row).get(col).get().getSize().x) / 2.f) + 
                            		availSpaceOffset.x;
                            top += (float)(m_ObjBorders.get(row).get(col).top) + availSpaceOffset.y;
                            break;

                        case UpperRight:
                            left += m_ColumnWidth.get(col) - 
                            	m_ObjBorders.get(row).get(col).right - 
                            	m_GridWidgets.get(row).get(col).get().getSize().x + 
                            	availSpaceOffset.x;
                            top += (float)(m_ObjBorders.get(row).get(col).top) + availSpaceOffset.y;
                            break;

                        case Right:
                            left += m_ColumnWidth.get(col) - 
                            	m_ObjBorders.get(row).get(col).right - 
                            	m_GridWidgets.get(row).get(col).get().getSize().x + 
                            	availSpaceOffset.x;
                            top += m_ObjBorders.get(row).get(col).top + 
                            		(((m_RowHeight.get(row) - 
                            		m_ObjBorders.get(row).get(col).top - 
                            		m_ObjBorders.get(row).get(col).bottom) - 
                            		m_GridWidgets.get(row).get(col).get().getSize().y) / 2.f) + 
                            		availSpaceOffset.y;
                            break;

                        case BottomRight:
                            left += m_ColumnWidth.get(col) - 
                            	m_ObjBorders.get(row).get(col).right - 
                            	m_GridWidgets.get(row).get(col).get().getSize().x + 
                            	availSpaceOffset.x;
                            top += m_RowHeight.get(row) - 
                            	m_ObjBorders.get(row).get(col).bottom - 
                            	m_GridWidgets.get(row).get(col).get().getSize().y + 
                            	availSpaceOffset.y;
                            break;

                        case Bottom:
                            left += m_ObjBorders.get(row).get(col).left + 
                            	(((m_ColumnWidth.get(col) - 
                            	m_ObjBorders.get(row).get(col).left - 
                            	m_ObjBorders.get(row).get(col).right) - 
                            	m_GridWidgets.get(row).get(col).get().getSize().x) / 2.f) + 
                            	availSpaceOffset.x;
                            top += m_RowHeight.get(row) - 
                            	m_ObjBorders.get(row).get(col).bottom - 
                            	m_GridWidgets.get(row).get(col).get().getSize().y + 
                            	availSpaceOffset.y;
                            break;

                        case BottomLeft:
                            left += (float)(m_ObjBorders.get(row).get(col).left) + 
                            	availSpaceOffset.x;
                            top += m_RowHeight.get(row) - 
                            	m_ObjBorders.get(row).get(col).bottom - 
                            	m_GridWidgets.get(row).get(col).get().getSize().y + 
                            	availSpaceOffset.y;
                            break;

                        case Left:
                            left += (float)(m_ObjBorders.get(row).get(col).left) + 
                            	availSpaceOffset.x;
                            top += m_ObjBorders.get(row).get(col).top + 
                            	(((m_RowHeight.get(row) - 
                            	m_ObjBorders.get(row).get(col).top - 
                            	m_ObjBorders.get(row).get(col).bottom) - 
                            	m_GridWidgets.get(row).get(col).get().getSize().y) / 2.f) + 
                            	availSpaceOffset.y;
                            break;

                        case Center:
                            left += m_ObjBorders.get(row).get(col).left + 
                            	(((m_ColumnWidth.get(col) - 
                            	m_ObjBorders.get(row).get(col).left - 
                            	m_ObjBorders.get(row).get(col).right) -
                            	m_GridWidgets.get(row).get(col).get().getSize().x) / 2.f) + availSpaceOffset.x;
                            top += m_ObjBorders.get(row).get(col).top + 
                            	(((m_RowHeight.get(row) - 
                            	m_ObjBorders.get(row).get(col).top - 
                            	m_ObjBorders.get(row).get(col).bottom) - 
                            	m_GridWidgets.get(row).get(col).get().getSize().y) / 2.f) + availSpaceOffset.y;
                            break;
                        }

                        m_GridWidgets.get(row).get(col).get().setPosition(left, top);
                    }
                }
            }
        }
	}
	
	protected Vector2f getMinSize() {
        Vector2f minSize = new Vector2f();

        for (Integer it : m_RowHeight) {
            minSize.y += (float)(it);
        }

        for (Integer it : m_ColumnWidth) {
            minSize.x += (float)(it);
        }

        return minSize;
	}
	
	protected void updatePositionsOfAllWidgets() {
        Vector2f position = new Vector2f();
        Vector2f previousPosition = new Vector2f();

        Vector2f availableSpace = new Vector2f();
        m_Size = m_IntendedSize;
        Vector2f minSize = getMinSize();

        if (m_IntendedSize.x > minSize.x) {
            availableSpace.x = m_IntendedSize.x - minSize.x;
        } else {
            m_Size.x = minSize.x;
        }
        
        if (m_IntendedSize.y > minSize.y) {
            availableSpace.y = m_IntendedSize.y - minSize.y;
        } else {
            m_Size.y = minSize.y;
        }
        
        Vector2f availSpaceOffset = new Vector2f(0.5f * availableSpace.x / m_ColumnWidth.size(),
                                      0.5f * availableSpace.y / m_RowHeight.size());

        for (int row = 0; row < m_GridWidgets.size(); ++row) {
            previousPosition = position;

            for (int col = 0; col < m_GridWidgets.get(row).size(); ++col) {
                if (m_GridWidgets.get(row).get(col).get() == null) {
                    position.x += m_ColumnWidth.get(col) + 2 * availSpaceOffset.x;
                    continue;
                }

                Vector2f cellPosition = new Vector2f(position);

                switch (m_ObjLayout.get(row).get(col)) {
                case UpperLeft:
                    cellPosition.x += (float)(m_ObjBorders.get(row).get(col).left) + 
                    	availSpaceOffset.x;
                    cellPosition.y += (float)(m_ObjBorders.get(row).get(col).top) + 
                    	availSpaceOffset.y;
                    break;

                case Up:
                    cellPosition.x += m_ObjBorders.get(row).get(col).left + 
                    	(((m_ColumnWidth.get(col) - 
                    	m_ObjBorders.get(row).get(col).left - 
                    	m_ObjBorders.get(row).get(col).right) - 
                    	m_GridWidgets.get(row).get(col).get().getSize().x) / 2.f) + 
                    	availSpaceOffset.x;
                    cellPosition.y += (float)(m_ObjBorders.get(row).get(col).top) + 
                    	availSpaceOffset.y;
                    break;

                case UpperRight:
                    cellPosition.x += m_ColumnWidth.get(col) - 
                    	m_ObjBorders.get(row).get(col).right - 
                    	m_GridWidgets.get(row).get(col).get().getSize().x + 
                    	availSpaceOffset.x;
                    cellPosition.y += (float)(m_ObjBorders.get(row).get(col).top) + 
                    	availSpaceOffset.y;
                    break;

                case Right:
                    cellPosition.x += m_ColumnWidth.get(col) - 
                    	m_ObjBorders.get(row).get(col).right - 
                    	m_GridWidgets.get(row).get(col).get().getSize().x + 
                    	availSpaceOffset.x;
                    cellPosition.y += m_ObjBorders.get(row).get(col).top + 
                    	(((m_RowHeight.get(row) - 
                    	m_ObjBorders.get(row).get(col).top - 
                    	m_ObjBorders.get(row).get(col).bottom) - 
                    	m_GridWidgets.get(row).get(col).get().getSize().y) / 2.f) + 
                    	availSpaceOffset.y;
                    break;

                case BottomRight:
                    cellPosition.x += m_ColumnWidth.get(col) - 
                    	m_ObjBorders.get(row).get(col).right - 
                    	m_GridWidgets.get(row).get(col).get().getSize().x + 
                    	availSpaceOffset.x;
                    cellPosition.y += m_RowHeight.get(row) - 
                    	m_ObjBorders.get(row).get(col).bottom - 
                    	m_GridWidgets.get(row).get(col).get().getSize().y + 
                    	availSpaceOffset.y;
                    break;

                case Bottom:
                    cellPosition.x += m_ObjBorders.get(row).get(col).left + 
                    	(((m_ColumnWidth.get(col) - 
                    	m_ObjBorders.get(row).get(col).left - 
                    	m_ObjBorders.get(row).get(col).right) - 
                    	m_GridWidgets.get(row).get(col).get().getSize().x) / 2.f) + 
                    	availSpaceOffset.x;
                    cellPosition.y += m_RowHeight.get(row) - 
                    	m_ObjBorders.get(row).get(col).bottom - 
                    	m_GridWidgets.get(row).get(col).get().getSize().y + 
                    	availSpaceOffset.y;
                    break;

                case BottomLeft:
                    cellPosition.x += (float)(m_ObjBorders.get(row).get(col).left) + 
                    	availSpaceOffset.x;
                    cellPosition.y += m_RowHeight.get(row) - 
                    	m_ObjBorders.get(row).get(col).bottom - 
                    	m_GridWidgets.get(row).get(col).get().getSize().y + 
                    	availSpaceOffset.y;
                    break;

                case Left:
                    cellPosition.x += (float)(m_ObjBorders.get(row).get(col).left) + 
                    	availSpaceOffset.x;
                    cellPosition.y += m_ObjBorders.get(row).get(col).top + 
                    	(((m_RowHeight.get(row) - 
                    	m_ObjBorders.get(row).get(col).top - 
                    	m_ObjBorders.get(row).get(col).bottom) - 
                    	m_GridWidgets.get(row).get(col).get().getSize().y) / 2.f) + 
                    	availSpaceOffset.y;
                    break;

                case Center:
                    cellPosition.x += m_ObjBorders.get(row).get(col).left + 
                    	(((m_ColumnWidth.get(col) - 
                    	m_ObjBorders.get(row).get(col).left - 
                    	m_ObjBorders.get(row).get(col).right) - 
                    	m_GridWidgets.get(row).get(col).get().getSize().x) / 2.f) + 
                    	availSpaceOffset.x;
                    cellPosition.y += m_ObjBorders.get(row).get(col).top + 
                    	(((m_RowHeight.get(row) - 
                    	m_ObjBorders.get(row).get(col).top - 
                    	m_ObjBorders.get(row).get(col).bottom) - 
                    	m_GridWidgets.get(row).get(col).get().getSize().y) / 2.f) + 
                    	availSpaceOffset.y;
                    break;
                }

                m_GridWidgets.get(row).get(col).get().setPosition(cellPosition);
                position.x += m_ColumnWidth.get(col) + 2 * availSpaceOffset.x;
            }

            position = previousPosition;
            position.y += m_RowHeight.get(row) + 2 * availSpaceOffset.y;
        }
	}
	
	public void draw(RenderTarget target, RenderStates states) {
		Transform.multiplyEqual(states.transform, getTransform());

        for (int row = 0; row < m_GridWidgets.size(); ++row) {
            for (int col = 0; col < m_GridWidgets.get(row).size(); ++col) {
                if (m_GridWidgets.get(row).get(col).get() != null) {
                    target.draw(m_GridWidgets.get(row).get(col).get(), states);
                }
            }
        }
	}

	@Override
	public boolean mouseOnWidget(float x, float y) {
        if (m_Loaded == false) {
            return false;
        }

        Vector2f position = getPosition();

        if ((x > position.x) && 
        	(y > position.y)) {
            if ((x < position.x + m_Size.x) && 
            	(y < position.y + m_Size.y)) {
                return true;
            }
        }

        if (m_MouseHover) {
            mouseLeftWidget();

            for (int i = 0; i < m_Widgets.size(); ++i) {
                m_Widgets.get(i).get().mouseNotOnWidget();
            }

            m_MouseHover = false;
        }

        return false;
	}

}
