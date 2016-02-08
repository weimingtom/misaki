package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.GL;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.RectangleShape;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Text;
import com.iteye.weimingtom.tgui.sf.Vector2f;
/**
 * 20151005
 * @author Administrator
 *
 */
public class Label extends ClickableWidget {
	public static enum LabelCallbacks {
        AllLabelCallbacks(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() - 1),
        LabelCallbacksCount(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value());
        
        int value;
        
        LabelCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }

	protected String m_LoadedConfigFile = "";

	protected RectangleShape m_Background = new RectangleShape();

	protected Text m_Text = new Text();

	protected boolean m_AutoSize;	
	
	public Label() {
	    m_AutoSize = true;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_Label;
        m_Loaded = true;

        m_Background.setFillColor(Color.Transparent);
	}
	
	public Label(Label copy) {
		//FIXME:
	}

	public void destroy() {
		super.destroy();
	}

	public Label cloneObj() {
		return new Label(this);
	}

	public boolean load(String configFileFilename) {
        m_LoadedConfigFile = configFileFilename;

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("Label", properties, values)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to parse " + configFileFilename + ".");
            return false;
        }

        configFile.close();

        for (int i = 0; i < properties.size(); ++i) {
            String property = properties.get(i);
            String value = values.get(i);

            if ("textcolor".equals(property)) {
                setTextColor(Defines.extractColor(value));
            } else {
                Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section Label in " + configFileFilename + ".");
            }
        }

        return false;
	}

	public String getLoadedConfigFile() {
        return m_LoadedConfigFile;
	}

	public void setSize(float width, float height) {
        if (width < 0) {
        	width = -width;
        }
        if (height < 0) {
        	height = -height;
        }

        m_Size.x = width;
        m_Size.y = height;

        m_Background.setSize(m_Size);

        m_AutoSize = false;
	}

	public void setPosition(float x, float y) {
        //Transformable::
        super.setPosition(x, y);

        m_Text.setPosition(
        	(float)Math.floor(x - m_Text.getLocalBounds().left + 0.5f), 
        	(float)Math.floor(y - m_Text.getLocalBounds().top + 0.5f));
        m_Background.setPosition(x, y);
	}
	
    //using Transformable::setPosition;

	public void setText(String string) {
        m_Text.setString(string);

        setPosition(getPosition());

        if (m_AutoSize) {
            m_Size = new Vector2f(m_Text.getLocalBounds().width, m_Text.getLocalBounds().height);
            m_Background.setSize(m_Size);
        }
	}

	public String getText() {
        return m_Text.getString();
	}

	public void setTextFont(Font font) {
        m_Text.setFont(font);
        setText(getText());
	}

	public Font getTextFont() {
        return m_Text.getFont();
	}

	public void setTextColor(Color color) {
        m_Text.setColor(color);
	}

	public Color getTextColor() {
		return m_Text.getColor();
	}

	public void setTextSize(int size) {
        m_Text.setCharacterSize(size);

        setPosition(getPosition());

        if (m_AutoSize) {
            m_Size = new Vector2f(m_Text.getLocalBounds().width, m_Text.getLocalBounds().height);
            m_Background.setSize(m_Size);
        }
	}

	public int getTextSize() {
        return m_Text.getCharacterSize();
	}

	public void setBackgroundColor(Color backgroundColor) {
        m_Background.setFillColor(backgroundColor);
	}
	
	public Color getBackgroundColor() {
        return m_Background.getFillColor();
	}

	public void setAutoSize(boolean autoSize) {
        m_AutoSize = autoSize;

        if (m_AutoSize) {
            m_Size = new Vector2f(m_Text.getLocalBounds().width, m_Text.getLocalBounds().height);
            m_Background.setSize(m_Size);
        }
	}

	public boolean getAutoSize() {
        return m_AutoSize;
	}

	public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            load(value);
        } else if ("text".equals(property)) {
            String[] text = new String[1];
            Defines.decodeString(value, text);
            setText(text[0]);
        } else if ("textcolor".equals(property)) {
            setTextColor(Defines.extractColor(value));
        } else if ("textsize".equals(property)) {
            setTextSize(Integer.parseInt(value.trim()));
        } else if ("backgroundcolor".equals(property)) {
            setBackgroundColor(Defines.extractColor(value));
        } else if ("autosize".equals(property)) {
            if (("true".equals(value)) || ("True".equals(value))) {
                setAutoSize(true);
        	} else if (("false".equals(value)) || ("False".equals(value))) {
                setAutoSize(false);
        	} else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'AutoSize' property.");
            }
        } else {
        	//ClickableWidget::
            return super.setProperty(property, value);
        }
        // You pass here when one of the properties matched
        return true;
	}

	public boolean getProperty(String property, String[] value) {
        property = Defines.toLower(property);

        if ("configfile".equals(property)) {
            value[0] = getLoadedConfigFile();
		} else if ("text".equals(property)) {
            Defines.encodeString(getText(), value);
		} else if ("textcolor".equals(property)) {
            value[0] = "(" + 
            		Integer.toString((int)(getTextColor().r)) + "," + 
            		Integer.toString((int)(getTextColor().g)) + "," + 
            		Integer.toString((int)(getTextColor().b)) + "," + 
            		Integer.toString((int)(getTextColor().a)) + ")";
		} else if ("textsize".equals(property)) {
            value[0] = Integer.toString(getTextSize());
		} else if ("backgroundcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getBackgroundColor().r)) + "," + 
            	Integer.toString((int)(getBackgroundColor().g)) + "," + 
            	Integer.toString((int)(getBackgroundColor().b)) + "," + 
            	Integer.toString((int)(getBackgroundColor().a)) + ")";
		} else if ("autosize".equals(property)) {
            value[0] = m_AutoSize ? "true" : "false";
		} else {
        	//ClickableWidget::
        	return super.getProperty(property, value);
        }
        // You pass here when one of the properties matched
        return true;
	}

	public List<Pair<String, String>> getPropertyList() {
        //ClickableWidget::
		List<Pair<String, String>> list = super.getPropertyList();
        list.add(new Pair<String, String>("ConfigFile", "string"));
        list.add(new Pair<String, String>("Text", "string"));
        list.add(new Pair<String, String>("TextColor", "color"));
        list.add(new Pair<String, String>("TextSize", "uint"));
        list.add(new Pair<String, String>("BackgroundColor", "color"));
        list.add(new Pair<String, String>("AutoSize", "bool"));
        return list;
	}

	protected void initialize(Container parent) {
        m_Parent = parent;
        setTextFont(m_Parent.getGlobalFont());
	}

	public void draw(RenderTarget target, RenderStates states) {
        if (m_Text.getString().isEmpty()) {
            return;
        }

        float scaleViewX = target.getSize().x / target.getView().getSize().x;
        float scaleViewY = target.getSize().y / target.getView().getSize().y;

        Vector2f topLeftPosition = states.transform.transformPoint(Vector2f.plus(Vector2f.minus(getPosition(), target.getView().getCenter()), (Vector2f.devide(target.getView().getSize(), 2.f))));
        Vector2f bottomRightPosition = states.transform.transformPoint(Vector2f.plus(Vector2f.minus(Vector2f.plus(getPosition(), m_Size), target.getView().getCenter()), (Vector2f.devide(target.getView().getSize(), 2.f))));

        int[] scissor = new int[4];
        GL.glGetIntegerv(GL.GL_SCISSOR_BOX, scissor);

        int scissorLeft = (int)Defines.TGUI_MAXIMUM((int)(topLeftPosition.x * scaleViewX), scissor[0]);
        int scissorTop = (int)Defines.TGUI_MAXIMUM((int)(topLeftPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1] - scissor[3]);
        int scissorRight = (int)Defines.TGUI_MINIMUM((int)(bottomRightPosition.x * scaleViewX), scissor[0] + scissor[2]);
        int scissorBottom = (int)Defines.TGUI_MINIMUM((int)(bottomRightPosition.y * scaleViewY), (int)(target.getSize().y) - scissor[1]);

        if (scissorRight < scissorLeft) {
            scissorRight = scissorLeft;
        } else if (scissorBottom < scissorTop) {
            scissorTop = scissorBottom;
        }

        GL.glScissor(scissorLeft, target.getSize().y - scissorBottom, scissorRight - scissorLeft, scissorBottom - scissorTop);

        if (m_Background.getFillColor() != Color.Transparent) {
            target.draw(m_Background, states);
        }

        target.draw(m_Text, states);

        GL.glScissor(scissor[0], scissor[1], scissor[2], scissor[3]);
	}
}
