package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.GL;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.FloatRect;
import com.iteye.weimingtom.tgui.sf.RectangleShape;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.SFTexture;
import com.iteye.weimingtom.tgui.sf.Sprite;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20151005
 * @author Administrator
 *
 */
public class Panel extends Container {
	public static enum PanelCallbacks {
	    LeftMousePressed(WidgetCallbacks.WidgetCallbacksCount.value() * 1),      
	    LeftMouseReleased(WidgetCallbacks.WidgetCallbacksCount.value() * 2),    
	    LeftMouseClicked(WidgetCallbacks.WidgetCallbacksCount.value() * 4),
	    AllPanelCallbacks(WidgetCallbacks.WidgetCallbacksCount.value() * 8 - 1),
	    PanelCallbacksCount(WidgetCallbacks.WidgetCallbacksCount.value() * 8);
	    
	    int value;
        
	    PanelCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
	}
	
	protected Vector2f m_Size = new Vector2f();

    protected Color m_BackgroundColor = new Color();

    protected SFTexture m_Texture = null;
    protected Sprite m_Sprite = new Sprite();
	
    public Panel() {
        m_Size = new Vector2f(100, 100);
        m_BackgroundColor = Color.Transparent;
        m_Texture = null;
        
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_Panel;
        m_Loaded = true;
    }
	
	public Panel(Panel panelToCopy) {
	    //Container
	    super(panelToCopy);
	    m_Size = panelToCopy.m_Size;
	    m_BackgroundColor = panelToCopy.m_BackgroundColor;
	    m_Texture = panelToCopy.m_Texture;
		
        if (m_Texture != null) {
            m_Sprite.setTexture(m_Texture);
            m_Sprite.setScale(m_Size.x / m_Texture.getSize().x, m_Size.y / m_Texture.getSize().y);
            m_Sprite.setColor(new Color(255, 255, 255, m_Opacity));
        }
	}
	
	public void destroy() {
		super.destroy();
	}
	
	public Panel assign(Panel right) {
        if (this != right) {
            Panel temp = new Panel(right);
            //this->Container::operator=(right);
            super.assign(right);
            
            m_Size = temp.m_Size;
            m_BackgroundColor = temp.m_BackgroundColor;
            m_Texture = temp.m_Texture;
            m_Sprite = temp.m_Sprite;
        }

        return this;
	}
	
	public Panel cloneObj() {
        return new Panel(this);
	}
	
	public void setSize(float width, float height) {
        if (width  < 0) {
        	width  = -width;
        }
        if (height < 0) {
        	height = -height;
        }

        m_Size.x = width;
        m_Size.y = height;

        if (m_Texture != null) {
            m_Sprite.setScale(
            	m_Size.x / m_Texture.getSize().x, 
            	m_Size.y / m_Texture.getSize().y);
        }
	}
	
	public Vector2f getSize() {
        return new Vector2f(m_Size.x, m_Size.y);
    }
	
	public void setBackgroundTexture(SFTexture texture) {
        m_Texture = texture;

        if (m_Texture != null) {
            m_Sprite.setTexture(m_Texture, true);
            m_Sprite.setScale(
            	m_Size.x / m_Texture.getSize().x, 
            	m_Size.y / m_Texture.getSize().y);
        }
	}
	
	public SFTexture getBackgroundTexture() {
        return m_Texture;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
        m_BackgroundColor = backgroundColor;
	}
	
	public Color getBackgroundColor() {
        return m_BackgroundColor;
	}
	
	public void setTransparency(int transparency) {
        //Container::
        super.setTransparency(transparency);

        m_Sprite.setColor(new Color(255, 255, 255, m_Opacity));
	}
	
	public void leftMousePressed(float x, float y) {
        if (mouseOnWidget(x, y)) {
            m_MouseDown = true;

            if (!this.getCallbackManager().m_CallbackFunctions.get(PanelCallbacks.LeftMousePressed.value()).isEmpty()) {
                this.getCallbackManager().m_Callback.trigger = PanelCallbacks.LeftMousePressed.value();
                this.getCallbackManager().m_Callback.mouse.x = (int)(x - getPosition().x);
                this.getCallbackManager().m_Callback.mouse.y = (int)(y - getPosition().y);
                addCallback();
            }
        }

        //Container::
        super.leftMousePressed(x, y);		
	}
	
	public void leftMouseReleased(float x, float y) {
        if (mouseOnWidget(x, y)) {
            if (!this.getCallbackManager().m_CallbackFunctions
            		.get(PanelCallbacks.LeftMouseReleased.value()).isEmpty()) {
                this.getCallbackManager().m_Callback.trigger = 
                	PanelCallbacks.LeftMouseReleased.value();
                this.getCallbackManager().m_Callback.mouse.x = (int)(x - getPosition().x);
                this.getCallbackManager().m_Callback.mouse.y = (int)(y - getPosition().y);
                addCallback();
            }

            if (m_MouseDown) {
                if (!this.getCallbackManager().m_CallbackFunctions.get(PanelCallbacks.LeftMouseClicked.value()).isEmpty()) {
                	this.getCallbackManager().m_Callback.trigger = PanelCallbacks.LeftMouseClicked.value();
                	this.getCallbackManager().m_Callback.mouse.x = (int)(x - getPosition().x);
                	this.getCallbackManager().m_Callback.mouse.y = (int)(y - getPosition().y);
                    addCallback();
                }
            }
        }

        m_MouseDown = false;

        //Container::
        super.leftMouseReleased(x, y);
	}
	
	public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("backgroundcolor".equals(property)) {
            setBackgroundColor(Defines.extractColor(value));
        } else if ("callback".equals(property)) {
            //Container::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("LeftMousePressed".equals(it)) || ("leftmousepressed".equals(it))) {
                    this.getCallbackManager().bindCallback(PanelCallbacks.LeftMousePressed.value());
            	} else if (("LeftMouseReleased".equals(it)) || ("leftmousereleased".equals(it))) {
            		this.getCallbackManager().bindCallback(PanelCallbacks.LeftMouseReleased.value());
        		} else if (("LeftMouseClicked".equals(it)) || ("leftmouseclicked".equals(it))) {
        			this.getCallbackManager().bindCallback(PanelCallbacks.LeftMouseClicked.value());
				}
            }
        } else {
        	//Container::
        	return super.setProperty(property, value);
        }
        
        return true;
	}
	
	public boolean getProperty(String property, String[] value) {
		property = Defines.toLower(property);

        if ("backgroundcolor".equals(property)) {
            value[0] = "(" + 
            	Integer.toString((int)(getBackgroundColor().r)) + "," + 
            	Integer.toString((int)(getBackgroundColor().g)) + "," + 
            	Integer.toString((int)(getBackgroundColor().b)) + "," + 
            	Integer.toString((int)(getBackgroundColor().a)) + ")";
        } else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //Container::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();

            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(PanelCallbacks.LeftMousePressed.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(PanelCallbacks.LeftMousePressed.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(PanelCallbacks.LeftMousePressed.value()).get(0) == null)) {
                callbacks.add("LeftMousePressed");
			}
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(PanelCallbacks.LeftMouseReleased.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(PanelCallbacks.LeftMouseReleased.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(PanelCallbacks.LeftMouseReleased.value()).get(0) == null)) {
                callbacks.add("LeftMouseReleased");
			}
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(PanelCallbacks.LeftMouseClicked.value()) != null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(PanelCallbacks.LeftMouseClicked.value()).size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(PanelCallbacks.LeftMouseClicked.value()).get(0) == null)) {
                callbacks.add("LeftMouseClicked");
			}
            
            Defines.encodeList(callbacks, value);

            if (value[0].length() == 0 || tempValue[0].length() == 0) {
                value[0] += tempValue;
            } else {
                value[0] += "," + tempValue;
            }
        } else {
        	//Container::
        	return super.getProperty(property, value);
        }
        
        // You pass here when one of the properties matched
        return true;
	}
	
	public List<Pair<String, String>> getPropertyList() {
        //Container::
		List<Pair<String, String>> list = super.getPropertyList();
        list.add(new Pair<String, String>("BackgroundColor", "color"));
        return list;
	}
	
	public void draw(RenderTarget target, RenderStates states) {
        if (m_Loaded == false) {
            return;
        }

        float scaleViewX = target.getSize().x / target.getView().getSize().x;
        float scaleViewY = target.getSize().y / target.getView().getSize().y;

        Vector2f topLeftPosition = states.transform.transformPoint(Vector2f.plus(Vector2f.minus(getPosition(), target.getView().getCenter()), Vector2f.devide(target.getView().getSize(), 2.f)));
        Vector2f bottomRightPosition = states.transform.transformPoint(Vector2f.plus(Vector2f.minus(Vector2f.plus(getPosition(), m_Size), target.getView().getCenter()), Vector2f.devide(target.getView().getSize(), 2.f)));

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

        Transform.multiplyEqual(states.transform, getTransform());

        if (m_BackgroundColor != Color.Transparent) {
            RectangleShape background = new RectangleShape(m_Size);
            background.setFillColor(m_BackgroundColor);
            target.draw(background, states);
        }

        if (m_Texture != null) {
            target.draw(m_Sprite, states);
        }
        
        drawWidgetContainer(target, states);

        GL.glScissor(scissor[0], scissor[1], scissor[2], scissor[3]);
	}
	
	@Override
	public boolean mouseOnWidget(float x, float y) {
        if (m_Loaded == false) {
            return false;
        }

        if (getTransform().transformRect(
        		new FloatRect(0, 0, m_Size.x, m_Size.y)).contains(x, y)) {
            return true;
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
