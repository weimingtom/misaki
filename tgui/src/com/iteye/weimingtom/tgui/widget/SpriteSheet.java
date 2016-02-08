package com.iteye.weimingtom.tgui.widget;

import java.util.List;

import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.IntRect;
import com.iteye.weimingtom.tgui.sf.Vector2f;
import com.iteye.weimingtom.tgui.sf.Vector2u;

/**
 * 20151004
 * @author Administrator
 *
 */
public class SpriteSheet extends Picture {
    public static enum SpriteSheetCallbacks {
        AllSpriteSheetCallbacks(PictureCallbacks.PictureCallbacksCount.value() - 1), 
        SpriteSheetCallbacksCount(PictureCallbacks.PictureCallbacksCount.value());
        
        int value;
        
        SpriteSheetCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    };

    protected int m_Rows;
    protected int m_Columns;

    protected Vector2u m_VisibleCell;
	
	public SpriteSheet() {
	    m_Rows = 1;
	    m_Columns = 1;
	    m_VisibleCell = new Vector2u(1, 1);
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_SpriteSheet;
	}

	public SpriteSheet(SpriteSheet copy) {
	    super(copy);
	    m_Rows = copy.m_Rows;
	    m_Columns = copy.m_Columns;
	    m_VisibleCell = copy.m_VisibleCell; //FIXME:
	}

	public SpriteSheet assign(SpriteSheet right) {
        if (this != right) {
            SpriteSheet temp = new SpriteSheet(right);
            //this->Picture::operator=(right);
            super.assign(right);

            m_Rows = temp.m_Rows;
            m_Columns = temp.m_Columns;
            m_VisibleCell = temp.m_VisibleCell;
        }

        return this;
	}

	public SpriteSheet cloneObj() {
        return new SpriteSheet(this);
	}

	public void setSize(float width, float height) {
        if (m_Loaded == false) {
            return;
        }
        
        m_Size.x = width;
        m_Size.y = height;

        m_Texture.sprite.setScale(
        	(m_Size.x * m_Columns) / m_Texture.data.texture.getSize().x, 
        	(m_Size.y * m_Rows) / m_Texture.data.texture.getSize().y);
	}

	public Vector2f getSize() {
        if (m_Loaded) {
            return m_Size;
        } else {
            return new Vector2f(0, 0);
        }
	}

	public void setCells(int rows, int columns) {
	    if (m_Loaded == false) {
            return;
	    }
	    
        if (rows == 0) {
            rows = 1;
        }

        if (columns == 0) {
            columns = 1;
        }

        m_Rows = rows;
        m_Columns = columns;

        m_Texture.sprite.setTextureRect(
        	new IntRect((m_VisibleCell.x - 1) * m_Texture.data.texture.getSize().x / m_Columns,
                (m_VisibleCell.y - 1) * m_Texture.data.texture.getSize().y / m_Rows,
                (int)(m_Texture.data.texture.getSize().x / m_Columns),
                (int)(m_Texture.data.texture.getSize().y / m_Rows)));

        m_Texture.sprite.setScale(
        	(m_Size.x * m_Columns) / m_Texture.data.texture.getSize().x, 
        	(m_Size.y * m_Rows) / m_Texture.data.texture.getSize().y);
	}

	public void setRows(int rows) {
        setCells(rows, m_Columns);		
	}

	public int getRows() {
        return m_Rows;
	}

	public void setColumns(int columns) {
        setCells(m_Rows, columns);
	}

	public int getColumns() {
        return m_Columns;
	}

	public void setVisibleCell(int row, int column) {
        if (m_Loaded == false) {
            return;
        }

        if (row > m_Rows) {
            row = m_Rows;
        } else if (row == 0) {
            row = 1;
        }

        if (column > m_Columns) {
            column = m_Columns;
        } else if (column == 0) {
            column = 1;
        }

        m_VisibleCell.x = column;
        m_VisibleCell.y = row;

        m_Texture.sprite.setTextureRect(
        	new IntRect(
    			(m_VisibleCell.x - 1) * m_Texture.data.texture.getSize().x / m_Columns,
                (m_VisibleCell.y - 1) * m_Texture.data.texture.getSize().y / m_Rows,
                (int)(m_Texture.data.texture.getSize().x / m_Columns),
                (int)(m_Texture.data.texture.getSize().y / m_Rows)));
    }

	public Vector2u getVisibleCell() {
        return m_VisibleCell;
	}

	public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("rows".equals(property)) {
            setRows(Integer.parseInt(value.trim()));
        } else if ("columns".equals(property)) {
            setColumns(Integer.parseInt(value.trim()));
        } else {
        	//Picture::
        	return super.setProperty(property, value);
        }
        return true;
	}

    public boolean getProperty(String property, String[] value) {
        property = Defines.toLower(property);

        if ("rows".equals(property)) {
            value[0] = Integer.toString(getRows());
        } else if ("columns".equals(property)) {
            value[0] = Integer.toString(getColumns());
        } else {
        	//Picture::
            return super.getProperty(property, value);
        }
        return true;
    }

    public List<Pair<String, String>> getPropertyList() {
        //Picture::
    	List<Pair<String, String>> list = super.getPropertyList();
        list.add(new Pair<String, String>("Rows", "uint"));
        list.add(new Pair<String, String>("Columns", "uint"));
        return list;
    }
}
