package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.Pair;
import com.iteye.weimingtom.tgui.Texture;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Time;
import com.iteye.weimingtom.tgui.sf.Transform;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20150830
 * @author Administrator
 *
 */
public class AnimatedPicture extends ClickableWidget {
	public static enum AnimatedPictureCallbacks {
        AnimationFinished(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 1),
        AllAnimatedPictureCallbacks(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 2 - 1),
        AnimatedPictureCallbacksCount(ClickableWidgetCallbacks.ClickableWidgetCallbacksCount.value() * 2);
        
        int value;
        
        AnimatedPictureCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
    }

	protected List<Texture> m_Textures;
    protected List<Time> m_FrameDuration;

    protected int m_CurrentFrame;

    protected boolean m_Playing;
    protected boolean m_Looping;
	
	public AnimatedPicture() {
	    m_Textures = new ArrayList<Texture>();
	    m_FrameDuration = new ArrayList<Time>();
	    m_CurrentFrame = -1;
	    m_Playing = false;
	    m_Looping = false;
	    
        this.getCallbackManager().m_Callback.widgetType = 
        	WidgetTypes.Type_AnimatedPicture;
        m_AnimatedWidget = true;
	}

	public AnimatedPicture(AnimatedPicture copy) {
	    super(copy);
	    m_FrameDuration = copy.m_FrameDuration;
	    m_CurrentFrame = copy.m_CurrentFrame;
	    m_Playing = copy.m_Playing;
	    m_Looping = copy.m_Looping;
	    
        for (int i = 0; i < copy.m_Textures.size(); ++i) {
            m_Textures.add(new Texture());
            Defines.TGUI_TextureManager.copyTexture(
            	copy.m_Textures.get(i), m_Textures.get(m_Textures.size() - 1));
        }
	}

	public void destroy() {
		super.destroy();
        for (int i = 0; i < m_Textures.size(); ++i) {
            Defines.TGUI_TextureManager
            	.removeTexture(m_Textures.get(i));
        }
        m_Textures.clear();
        m_FrameDuration.clear();	
	}

	public AnimatedPicture assign(AnimatedPicture right) {
        if (this != right) {
            AnimatedPicture temp = new AnimatedPicture(right);
            super.assign(right);
            for (int i = 0; i < m_Textures.size(); ++i) {
                Defines.TGUI_TextureManager
                	.removeTexture(m_Textures.get(i));
            }
            m_Textures = temp.m_Textures;
            m_FrameDuration = temp.m_FrameDuration;
            m_CurrentFrame = temp.m_CurrentFrame;
            m_Playing = temp.m_Playing;
            m_Looping = temp.m_Looping;
        }
        return this;
	}

	public AnimatedPicture cloneObj() {
		return new AnimatedPicture(this);
	}

	public boolean addFrame(String filename, Time frameDuration) {
	    if (filename == null || filename.length() == 0) {
            return false;
	    }

        Texture tempTexture = new Texture();

        if (Defines.TGUI_TextureManager
        		.getTexture(filename, tempTexture)) {
            if (m_Textures.isEmpty()) {
                m_CurrentFrame = 0;

                m_Size = new Vector2f(tempTexture.getSize());
            }

            tempTexture.sprite.setColor(new Color(255, 255, 255, m_Opacity));
            m_Textures.add(tempTexture);

            m_FrameDuration.add(frameDuration);

            return m_Loaded = true;
        } else {
            return m_Loaded = false;
        }
	}

	public void setSize(float width, float height) {
        m_Size.x = width;
        m_Size.y = height;		
	}

	public Vector2f getSize() {
        if (m_Textures.isEmpty() == false) {
            return new Vector2f(m_Size.x, m_Size.y);
        } else {
            return new Vector2f(0, 0);
        }
	}

	public void play() {
        if (m_Textures.isEmpty()) {
            return;
        }

        m_Playing = true;

        m_AnimationTimeElapsed = new Time();		
	}

	public void pause() {
		m_Playing = false;
	}

	public void stop() {
        m_Playing = false;

        if (m_Textures.isEmpty()) {
            m_CurrentFrame = -1;
        } else {
            m_CurrentFrame = 0;
        }
	}

	public boolean setFrame(int frame) {
        if (m_Textures.isEmpty() == true) {
            m_CurrentFrame = -1;
            return false;
        }

        if (frame >= m_Textures.size()) {
            m_CurrentFrame = m_Textures.size()-1;
            return false;
        }

        m_CurrentFrame = frame;
        return true;
	}

	public int getCurrentFrame() {
		return m_CurrentFrame;
	}

	public Time getCurrentFrameDuration() {
        if (!m_FrameDuration.isEmpty()) {
            return m_FrameDuration.get(m_CurrentFrame);
        } else {
            Defines.TGUI_OUTPUT("TGUI warning: Can't get duration of current frame: no frames available.");
            return new Time();
        }
	}

	public int getFrames() {
		return m_Textures.size();
	}

	public boolean removeFrame(int frame) {
        if (frame >= m_Textures.size()) {
            return false;
        }

        Defines.TGUI_TextureManager.removeTexture(m_Textures.get(frame));
        m_Textures.remove(frame);
        m_FrameDuration.remove(frame);

        if (m_CurrentFrame >= (int)(frame)) {
            --m_CurrentFrame;
        }

        if (m_Textures.size() == 1) {
            m_Loaded = false;
        }

        return true;
	}

	public void removeAllFrames() {
        for (int i = 0; i < m_Textures.size(); ++i) {
            Defines.TGUI_TextureManager.removeTexture(
            	m_Textures.get(i));
        }
        
        m_Textures.clear();
        m_FrameDuration.clear();

        stop();
        m_Loaded = false;		
	}

	public void setLooping(boolean loop) {
		m_Looping = loop;
	}

	public boolean getLooping() {
		return m_Looping;
	}

	public boolean isPlaying() {
		return m_Playing;
	}

	public void setTransparency(int transparency) {
        //ClickableWidget::
        super.setTransparency(transparency);

        for (int i = 0; i < m_Textures.size(); ++i) {
            m_Textures.get(i).sprite.setColor(
            	new Color(255, 255, 255, m_Opacity));
        }
	}

	public boolean setProperty(String property, String value) {
        property = Defines.toLower(property);

        if ("playing".equals(property)) {
            if (("true".equals(value)) || ("True".equals(value))) {
                m_Playing = true;
            } else if (("false".equals(value)) || ("False".equals(value))) {
                m_Playing = false;
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Playing' property.");
            }
        } else if (property == "looping") {
            if (("true".equals(value)) || ("True".equals(value))) {
                m_Looping = true;
            } else if (("false".equals(value)) || ("False".equals(value))) {
                m_Looping = false;
            } else {
                Defines.TGUI_OUTPUT("TGUI error: Failed to parse 'Looping' property.");
            }
        } else if (property == "callback") {
            //ClickableWidget::
            super.setProperty(property, value);

            List<String> callbacks = new ArrayList<String>();
            Defines.decodeList(value, callbacks);

            for (String it : callbacks) {
                if (("AnimationFinished".equals(it)) || 
                	("animationfinished".equals(it))) {
                    this.getCallbackManager().bindCallback(
                    	AnimatedPictureCallbacks.AnimationFinished.value());
                }
            }
        } else {
        	//ClickableWidget::
            return super.setProperty(property, value);
        }
        
        return true;
	}

	public boolean getProperty(String property, String[] value) {
		property = Defines.toLower(property);

        if ("playing".equals(property)) {
            value[0] = m_Playing ? "true" : "false";
        } else if ("looping".equals(property)) {
            value[0] = m_Looping ? "true" : "false";
        } else if ("callback".equals(property)) {
            String[] tempValue = new String[1];
            //ClickableWidget::
            super.getProperty(property, tempValue);

            List<String> callbacks = new ArrayList<String>();
            
            if ((this.getCallbackManager().m_CallbackFunctions
            		.get(AnimatedPictureCallbacks.AnimationFinished.value()) 
            		!= null) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(AnimatedPictureCallbacks.AnimationFinished.value())
            		.size() == 1) && 
            	(this.getCallbackManager().m_CallbackFunctions
            		.get(AnimatedPictureCallbacks.AnimationFinished.value()).get(0) == null)) {
                callbacks.add("AnimationFinished");
            }

            Defines.encodeList(callbacks, value);

            if (value[0] == null || value[0].length() == 0) {
                value[0] = tempValue[0];
        	} else if (!(tempValue[0] == null || tempValue[0].length() == 0)) {
                value[0] += "," + tempValue[0];
            }
        } else {
        	//ClickableWidget::
            return super.getProperty(property, value);
        }
        
        return true;
	}

	public List<Pair<String, String>> getPropertyList() {
		//ClickableWidget::
		List<Pair<String, String>> list = super.getPropertyList();
        list.add(new Pair<String, String>("Playing", "bool"));
        list.add(new Pair<String, String>("Looping", "bool"));
        return list;
	}

	public void update() {
	    if (m_Playing == false) {
            return;
	    }
	    
        while (Time.largerThan(m_AnimationTimeElapsed, m_FrameDuration.get(m_CurrentFrame))) {
            if (m_FrameDuration.get(m_CurrentFrame).asMicroseconds() > 0) {
            	Time.minusEqual(m_AnimationTimeElapsed, m_FrameDuration.get(m_CurrentFrame));

                if ((int)(m_CurrentFrame + 1) < m_Textures.size()) {
                    ++m_CurrentFrame;
            	} else {
                    if (m_Looping == true) {
                        m_CurrentFrame = 0;
            		} else {
                        m_Playing = false;
                        m_AnimationTimeElapsed = new Time();
                    }

                    if (this.getCallbackManager().m_CallbackFunctions
                    		.get(AnimatedPictureCallbacks.AnimationFinished.value())
                    		.isEmpty() == false) {
                    	this.getCallbackManager().m_Callback.trigger 
                    		= AnimatedPictureCallbacks.AnimationFinished.value();
                        addCallback();
                    }
                }
            } else {
                m_AnimationTimeElapsed = new Time();
            }
        }		
	}

	public void draw(RenderTarget target, RenderStates states) {
        if (m_Loaded) {
        	Transform.multiplyEqual(states.transform, getTransform());
            states.transform.scale(
            		m_Size.x / m_Textures.get(m_CurrentFrame).getSize().x, 
            		m_Size.y / m_Textures.get(m_CurrentFrame).getSize().y);
            target.draw(m_Textures.get(m_CurrentFrame).getSprite(), states);
        }	
	}
}
