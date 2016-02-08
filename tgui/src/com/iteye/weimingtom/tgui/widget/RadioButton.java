package com.iteye.weimingtom.tgui.widget;

import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.ConfigFile;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.WidgetPhase;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Vector2f;

/**
 * 20151004
 * @author Administrator
 *
 */
public class RadioButton extends Checkbox {
	public static enum RadioButtonCallbacks {
        AllRadioButtonCallbacks(CheckboxCallbacks.CheckboxCallbacksCount.value() - 1), 
        RadioButtonCallbacksCount(CheckboxCallbacks.CheckboxCallbacksCount.value());
        
        int value;
        
        RadioButtonCallbacks(int value) {
        	this.value = value;
        }
        
        public int value() {
        	return value;
        }
	}
	
	public RadioButton() {
        this.getCallbackManager().m_Callback.widgetType = 
    		WidgetTypes.Type_RadioButton;
	}

	public RadioButton(RadioButton copy) {
	    //Checkbox(copy)
		super(copy);
	}

	public RadioButton assign(RadioButton right) {
        if (this != right) {
            //this->Checkbox::operator=(right);
        	super.assign(right);
        }

        return this;
	}

	public RadioButton cloneObj() {
        return new RadioButton(this);
	}

	public boolean load(String configFileFilename) {
        m_LoadedConfigFile = configFileFilename;

        m_Loaded = false;

        if (m_TextureUnchecked.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureUnchecked);
        }
        if (m_TextureChecked.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureChecked);
        }
        if (m_TextureHover.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureHover);
        }
        if (m_TextureFocused.data != null) {
        	Defines.TGUI_TextureManager.removeTexture(m_TextureFocused);
        }

        ConfigFile configFile = new ConfigFile();
        if (!configFile.open(configFileFilename)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to open " + configFileFilename + ".");
            return false;
        }

        List<String> properties = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if (!configFile.read("RadioButton", properties, values)) {
            Defines.TGUI_OUTPUT("TGUI error: Failed to parse " + configFileFilename + ".");
            return false;
        }

        configFile.close();

        String configFileFolder = "";
        int slashPos = configFileFilename.lastIndexOf("/");
        if (slashPos == -1) {
        	slashPos = configFileFilename.lastIndexOf("\\"); 
        }
        if (slashPos != -1) {
            configFileFolder = configFileFilename.substring(0, slashPos+1);
        }
        for (int i = 0; i < properties.size(); ++i) {
            String property = properties.get(i);
            String value = values.get(i);

            if ("textcolor".equals(property)) {
                m_Text.setColor(configFile.readColor(value));
            } else if ("checkedimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureChecked)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for CheckedImage in section RadioButton in " + configFileFilename + ".");
                    return false;
                }
            } else if ("uncheckedimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureUnchecked)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for UncheckedImage in section RadioButton in " + configFileFilename + ".");
                    return false;
                }
            } else if ("hoverimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureHover)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for HoverImage in section RadioButton in " + configFileFilename + ".");
                    return false;
                }
            } else if ("focusedimage".equals(property)) {
                if (!configFile.readTexture(value, configFileFolder, m_TextureFocused)) {
                	Defines.TGUI_OUTPUT("TGUI error: Failed to parse value for FocusedImage in section RadioButton in " + configFileFilename + ".");
                    return false;
                }
            } else {
            	Defines.TGUI_OUTPUT("TGUI warning: Unrecognized property '" + property + "' in section RadioButton in " + configFileFilename + ".");
            }
        }

        if ((m_TextureChecked.data != null) && 
        	(m_TextureUnchecked.data != null)) {
            m_Loaded = true;
            m_Size = new Vector2f(m_TextureChecked.getSize());
        } else {
            Defines.TGUI_OUTPUT("TGUI error: Not all needed images were loaded for the radio button. Is the RadioButton section in " + configFileFilename + " complete?");
            return false;
        }

        if (m_TextureFocused.data != null) {
            m_AllowFocus = true;
            m_WidgetPhase |= WidgetPhase.WidgetPhase_Focused.value();
        }
        if (m_TextureHover.data != null) {
            m_WidgetPhase |= WidgetPhase.WidgetPhase_Hover.value();
        }

        return true;
	}

	public void check() {
        if (m_Checked == false) {
            m_Parent.uncheckRadioButtons();

            //Checkbox::check();
            super.check();
        }
	}

	public void uncheck() {
		//do nothing
        // The radio button can't be unchecked, 
		// so we override the original function with an empty one.
	}

	public void forceUncheck() {
		//Checkbox::uncheck();
		super.uncheck();
	}
}
