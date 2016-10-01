package com.iteye.weimingtom.tgui.widget;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.iteye.weimingtom.tgui.Callback;
import com.iteye.weimingtom.tgui.CallbackFunction;
import com.iteye.weimingtom.tgui.Defines;
import com.iteye.weimingtom.tgui.SharedWidgetPtr;
import com.iteye.weimingtom.tgui.WidgetTypes;
import com.iteye.weimingtom.tgui.sf.Event;
import com.iteye.weimingtom.tgui.sf.Font;
import com.iteye.weimingtom.tgui.sf.Keyboard;
import com.iteye.weimingtom.tgui.sf.Mouse;
import com.iteye.weimingtom.tgui.sf.RenderStates;
import com.iteye.weimingtom.tgui.sf.RenderTarget;
import com.iteye.weimingtom.tgui.sf.Time;
import com.iteye.weimingtom.tgui.sf.Window;

/**
 * 20151004
 * @author Administrator
 *
 */
public abstract class Container extends Widget {
    protected List<SharedWidgetPtr<? extends Widget>> m_Widgets = new ArrayList<SharedWidgetPtr<? extends Widget>>();
    protected List<String> m_ObjName = new ArrayList<String>();

    protected int m_FocusedWidget;

    protected Font m_GlobalFont = new Font();

    /*protected*/public boolean m_ContainerFocused;

    protected List<CallbackFunction> m_GlobalCallbackFunctions = new ArrayList<CallbackFunction>();

	public Container() {
        m_FocusedWidget = 0;
        
        m_ContainerWidget = true;
        m_AnimatedWidget = true;
        m_AllowFocus = true;
	}

	public Container(Container containerToCopy) {
        super(containerToCopy);
        m_FocusedWidget = 0;
        m_GlobalFont = containerToCopy.m_GlobalFont;
        m_ContainerFocused  = false;
        m_GlobalCallbackFunctions = containerToCopy.m_GlobalCallbackFunctions;
	
        for (int i = 0; i < containerToCopy.m_Widgets.size(); ++i) {
            m_Widgets.add(containerToCopy.m_Widgets.get(i).cloneObj());
            m_ObjName.add(containerToCopy.m_ObjName.get(i));

            //back()
            m_Widgets.get(m_Widgets.size() - 1).get().m_Parent = this;
        }
	}
	
	public void destroy() {
		super.destroy();
        removeAllWidgets();
	}

	public Container assign(Container right) {
	       // Make sure it is not the same widget
        if (this != right) {
            //Widget::operator=(right);
        	super.assign(right);
        	
            m_FocusedWidget = 0;
            m_GlobalFont = right.m_GlobalFont;
            m_ContainerFocused = false;
            m_GlobalCallbackFunctions = right.m_GlobalCallbackFunctions;

            removeAllWidgets();

            for (int i = 0; i < right.m_Widgets.size(); ++i) {
                m_Widgets.add(right.m_Widgets.get(i).cloneObj());
                m_ObjName.add(right.m_ObjName.get(i));

                //back()
                m_Widgets.get(m_Widgets.size() - 1).get().m_Parent = this;
            }
        }

        return this;
	}

	public boolean setGlobalFont(String filename) {
		return m_GlobalFont.loadFromFile(filename);
	}

	public void setGlobalFont(Font font) {
        m_GlobalFont = font;
	}

	public Font getGlobalFont() {
        return m_GlobalFont;
	}

	public List<SharedWidgetPtr<? extends Widget>> getWidgets() {
        return m_Widgets;
	}

	public List<String> getWidgetNames() {
        return m_ObjName;
	}

    public void add(SharedWidgetPtr<? extends Widget> widgetPtr) {
    	add(widgetPtr, "");
    }
	
    public void add(SharedWidgetPtr<? extends Widget> widgetPtr, String widgetName) {
        assert(widgetPtr != null);

        widgetPtr.get().initialize(this);
        m_Widgets.add(widgetPtr);
        m_ObjName.add(widgetName);
    }

    public SharedWidgetPtr<? extends Widget> get(String widgetName) {
        for (int i = 0; i < m_ObjName.size(); ++i) {
            if (m_ObjName.get(i).equals(widgetName)) {
                return m_Widgets.get(i);
            }
        }
        return null; //FIXME:
    }

    //FIXME:not necessary?
    public SharedWidgetPtr<Widget> getWidget(String widgetName) {
        return (SharedWidgetPtr<Widget>)get(widgetName);
    }

    public SharedWidgetPtr<? extends Widget> copy(SharedWidgetPtr<? extends Widget> oldWidget) {
    	return copy(oldWidget, "");
    }
    
    public SharedWidgetPtr<? extends Widget> copy(SharedWidgetPtr<? extends Widget> oldWidget, String newWidgetName) {
    	SharedWidgetPtr<? extends Widget> newWidget = oldWidget.cloneObj();
        m_Widgets.add(newWidget);
        m_ObjName.add(newWidgetName);
        return newWidget;
    }

    public void remove(SharedWidgetPtr<? extends Widget> widget) {
        remove(widget.get());
    }
    
    public void remove(Widget widget) {
        for (int i = 0; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).get() == widget) {
                if (widget.isFocused()) {
                    unfocusWidgets();
                }

                m_Widgets.remove(i); //FIXME:
                
                m_ObjName.remove(i); //FIXME:

                break;
            }
        }
    }

    public void removeAllWidgets() {
        m_Widgets.clear();
        m_ObjName.clear();

        m_FocusedWidget = 0;
    }

    public boolean setWidgetName(SharedWidgetPtr<? extends Widget> widget, String name) {
        for (int i = 0; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).equals(widget)) {
                m_ObjName.set(i, name);
                return true;
            }
        }

        return false;
    }

    public boolean getWidgetName(SharedWidgetPtr<? extends Widget> widget, String[] name) {
        for (int i = 0; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).equals(widget)) {
                name[0] = m_ObjName.get(i);
                return true;
            }
        }
        return false;
    }

    public void focusWidget(SharedWidgetPtr<? extends Widget> widget) {
        focusWidget(widget.get());
    }
    
    public void focusWidget(Widget widget) {
        for (int i = 0; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).get() == widget) {
                if (m_FocusedWidget != i + 1) {
                    if (m_FocusedWidget != 0) {
                        m_Widgets.get(m_FocusedWidget - 1).get().m_Focused = false;
                        m_Widgets.get(m_FocusedWidget - 1).get().widgetUnfocused();
                    }
                    m_FocusedWidget = i+1;
                    widget.m_Focused = true;
                    widget.widgetFocused();
                }
                break;
            }
        }
    }

    public void focusNextWidget() {
        for (int i = m_FocusedWidget; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).get().m_AllowFocus == true) {
                // Make sure that the widget is visible and enabled
                if ((m_Widgets.get(i).get().m_Visible) && 
                	(m_Widgets.get(i).get().m_Enabled)) {
                    if (m_FocusedWidget != 0) {
                        m_Widgets.get(m_FocusedWidget - 1).get().m_Focused = false;
                        m_Widgets.get(m_FocusedWidget - 1).get().widgetUnfocused();
                    }
                    m_FocusedWidget = i+1;
                    m_Widgets.get(i).get().m_Focused = true;
                    m_Widgets.get(i).get().widgetFocused();
                    return;
                }
            }
        }

        if (m_FocusedWidget != 0) {
            for (int i = 0; i < m_FocusedWidget - 1; ++i) {
                if (m_Widgets.get(i).get().m_AllowFocus == true) {
                    if ((m_Widgets.get(i).get().m_Visible) && 
                    	(m_Widgets.get(i).get().m_Enabled)) {
                        m_Widgets.get(m_FocusedWidget - 1).get().m_Focused = false;
                        m_Widgets.get(m_FocusedWidget - 1).get().widgetUnfocused();

                        m_FocusedWidget = i + 1;
                        m_Widgets.get(i).get().m_Focused = true;
                        m_Widgets.get(i).get().widgetFocused();

                        return;
                    }
                }
            }
        }
    }

    public void focusPreviousWidget() {
        if (m_FocusedWidget != 0) {
            for (int i = m_FocusedWidget - 1; i > 0; --i) {
                if (m_Widgets.get(i - 1).get().m_AllowFocus == true) {
                    if ((m_Widgets.get(i - 1).get().m_Visible) && 
                    	(m_Widgets.get(i - 1).get().m_Enabled)) {
                        m_Widgets.get(m_FocusedWidget - 1).get().m_Focused = false;
                        m_Widgets.get(m_FocusedWidget - 1).get().widgetUnfocused();

                        m_FocusedWidget = i;
                        m_Widgets.get(i - 1).get().m_Focused = true;
                        m_Widgets.get(i - 1).get().widgetFocused();

                        return;
                    }
                }
            }
        }

        for (int i = m_Widgets.size(); i > m_FocusedWidget; --i) {
            if (m_Widgets.get(i - 1).get().m_AllowFocus == true) {
                if ((m_Widgets.get(i - 1).get().m_Visible) && 
                	(m_Widgets.get(i - 1).get().m_Enabled)) {
                    if (m_FocusedWidget != 0) {
                        m_Widgets.get(m_FocusedWidget - 1).get().m_Focused = false;
                        m_Widgets.get(m_FocusedWidget - 1).get().widgetUnfocused();
                    }

                    m_FocusedWidget = i;
                    m_Widgets.get(i - 1).get().m_Focused = true;
                    m_Widgets.get(i - 1).get().widgetFocused();
                    return;
                }
            }
        }
    }

    public void unfocusWidgets() {
        if (m_FocusedWidget != 0) {
            m_Widgets.get(m_FocusedWidget - 1).get().m_Focused = false;
            m_Widgets.get(m_FocusedWidget - 1).get().widgetUnfocused();
            m_FocusedWidget = 0;
        }
    }

    public void uncheckRadioButtons() {
        for (int i = 0; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).get()
            		.getCallbackManager().m_Callback.widgetType == 
            				WidgetTypes.Type_RadioButton) {
            	((SharedWidgetPtr<RadioButton>)m_Widgets.get(i)).get().forceUncheck();
            }
        }
    }

    public void moveWidgetToFront(Widget widget) {
        for (int i = 0; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).get() == widget) {
                m_Widgets.add(m_Widgets.get(i));
                m_ObjName.add(m_ObjName.get(i));

                if ((m_FocusedWidget == 0) || (m_FocusedWidget == i+1)) {
                    m_FocusedWidget = m_Widgets.size()-1;
                } else if (m_FocusedWidget > i+1) {
                    --m_FocusedWidget;
                }

                // Remove the old widget
                m_Widgets.remove(i);
                m_ObjName.remove(i);

                break;
            }
        }
    }

    public void moveWidgetToBack(Widget widget) {
        for (int i = 0; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).get() == widget) {
            	SharedWidgetPtr<? extends Widget> obj = m_Widgets.get(i);
                String name = m_ObjName.get(i);
                m_Widgets.add(0, obj);
                m_ObjName.add(0, name);

                if (m_FocusedWidget == i + 1) {
                    m_FocusedWidget = 1;
                } else if (m_FocusedWidget != 0) {
                    ++m_FocusedWidget;
                }

                m_Widgets.remove(i + 1);
                m_ObjName.remove(i + 1);

                break;
            }
        }
    }

    public void setTransparency(int transparency) {
        //Widget::
        super.setTransparency(transparency);

        for (int i = 0; i < m_Widgets.size(); ++i) {
            m_Widgets.get(i).get().setTransparency(transparency);
        }
    }

    public void bindGlobalCallback(CallbackFunction func) {
        m_GlobalCallbackFunctions.add(func);
    }

    public <T> void bindGlobalCallback(Object func, T classPtr) {
        //m_GlobalCallbackFunctions.add(std::bind(func, classPtr, std::placeholders::_1));
    }

    public void unbindGlobalCallback() {
        m_GlobalCallbackFunctions.clear();
    }

//    #define COMPARE_WIDGET(length, name, widgetName) \
//    if (line.substr(0, length).compare(name) == 0) \
//    { \
//        line.erase(0, length); \
//      \
//        widgetPtr.push_back(widgetName::Ptr(*static_cast<Container*>(widgetPtr.back()), line).get()); \
//        progress.push(0); \
//    }
    
    public boolean loadWidgetsFromFile(String filename) {
    	/*
    	Stack<Integer> progress = new Stack<Integer>();
    	List<Widget> widgetPtr = new ArrayList<Widget>();

    	// Open the file
    	InputStream m_File = null;
    	InputStreamReader reader = null;
    	BufferedReader buf = null;
    	try {
    		m_File = new FileInputStream(filename);
    		reader = new InputStreamReader(m_File, "utf-8");
    		buf = new BufferedReader(reader);
    	} catch (Throwable e) {
    		
    	}
    	
    	if (m_File == null) {
    		return false;
    	}
	
	    boolean failed = false;
	    String line = null;
        while (((line = buf.readLine()) != null) && !failed) {	
	        if (line.length() > 0) {
	            int quotePos1 = line.indexOf('"');
	
	            if (quotePos1 == -1) {
	                line = line.replace(" ", "");
	                line = line.replace("\t", "");
	                line = line.replace("\r", "");//FIXME:	
	                line = Defines.toLower(line);
	            } else {
	                // Only remove spaces and tabs until the quote
	                line.erase(std::remove(line.begin(), line.begin() + quotePos1, ' '), line.begin() + quotePos1);
	                quotePos1 = line.find('"');
	                line.erase(std::remove(line.begin(), line.begin() + quotePos1, '\t'), line.begin() + quotePos1);
	                quotePos1 = line.find('"');
	
	                // Convert the part before the quote to lowercase
	                line = toLower(line.substr(0, quotePos1)) + line.substr(quotePos1);
	
	                // Search for a second quote
	                std::string::size_type quotePos2 = line.find('"', quotePos1 + 1);
	
	                // There must always be a second quote
	                if (quotePos2 != std::string::npos)
	                {
	                    // Remove all spaces and tabs after the quote
	                    line.erase(std::remove(line.begin() + quotePos2, line.end(), ' '), line.end());
	                    line.erase(std::remove(line.begin() + quotePos2, line.end(), '\t'), line.end());
	                    line.erase(std::remove(line.begin() + quotePos2, line.end(), '\r'), line.end());
	
	                    // Search for the quote again, because the position might have changed
	                    quotePos2 = line.find('"', quotePos1 + 1);
	
	                    // Search for backslashes between the quotes
	                    std::string::size_type backslashPos = line.find('\\', quotePos1);
	                    while (backslashPos < quotePos2)
	                    {
	                        // Check for special characters
	                        if (line[backslashPos + 1] == 'n')
	                        {
	                            line[backslashPos] = '\n';
	                            line.erase(backslashPos + 1, 1);
	                            --quotePos2;
	                        }
	                        else if (line[backslashPos + 1] == 't')
	                        {
	                            line[backslashPos] = '\t';
	                            line.erase(backslashPos + 1, 1);
	                            --quotePos2;
	                        }
	                        else if (line[backslashPos + 1] == '\\')
	                        {
	                            line.erase(backslashPos + 1, 1);
	                            --quotePos2;
	                        }
	                        else if (line[backslashPos + 1] == '"')
	                        {
	                            line[backslashPos] = '"';
	                            line.erase(backslashPos + 1, 1);
	
	                            // Find the next quote
	                            quotePos2 = line.find('"', backslashPos + 1);
	                            if (quotePos2 == std::string::npos)
	                            {
	                                failed = true;
	                                break;
	                            }
	                        }
	
	                        // Find the next backslash
	                        backslashPos = line.find('\\', backslashPos + 1);
	                    }
	
	                    // There may never be more than two quotes
	                    if (line.find('"', quotePos2 + 1) != std::string::npos)
	                        failed = true;
	
	                    // Convert the part behind the quote to lowercase
	                    line = line.substr(0, quotePos2 + 1) + toLower(line.substr(quotePos2 + 1));
	
	                    // Remove the quotes from the string
	                    line.erase(quotePos1, 1);
	                    line.erase(quotePos2 - 1, 1);
	                }
	                else // The second quote is missing
	                    failed = true;
	            }
	        }
	
	        // Only continue when the line hasn't become empty and nothing went wrong so far
	        if (!line.empty() && !failed)
	        {
	            // Check if this is the first line
	            if (progress.empty())
	            {
	                // The first line should contain the beginning of the window section
	                if (line.substr(0, 7).compare("window:") == 0)
	                {
	                    widgetPtr.push_back(this);
	                    progress.push(0);
	                    continue;
	                }
	                else // The first line is wrong
	                {
	                    failed = true;
	                    break;
	                }
	            }
	
	            // Check for opening and closing brackets
	            if (progress.top() == 0)
	            {
	                if (line.compare("{") == 0)
	                {
	                    progress.pop();
	                    progress.push(1);
	                    continue;
	                }
	                else
	                {
	                    failed = true;
	                    break;
	                }
	            }
	            else
	            {
	                if (line.compare("}") == 0)
	                {
	                    widgetPtr.pop_back();
	                    progress.pop();
	                    continue;
	                }
	            }
	
	            // The line doesn't contain a '}', so check if it contains another widget
	            bool widgetFound = true;
	            COMPARE_WIDGET(4, "tab:", Tab)
	            else COMPARE_WIDGET(5, "grid:", Grid)
	            else COMPARE_WIDGET(6, "panel:", Panel)
	            else COMPARE_WIDGET(6, "label:", Label)
	            else COMPARE_WIDGET(7, "button:", Button)
	            else COMPARE_WIDGET(7, "slider:", Slider)
	            else COMPARE_WIDGET(8, "picture:", Picture)
	            else COMPARE_WIDGET(8, "listbox:", ListBox)
	            else COMPARE_WIDGET(8, "editbox:", EditBox)
	            else COMPARE_WIDGET(8, "textbox:", TextBox)
	            else COMPARE_WIDGET(8, "chatbox:", ChatBox)
	            else COMPARE_WIDGET(8, "menubar:", MenuBar)
	            else COMPARE_WIDGET(9, "checkbox:", Checkbox)
	            else COMPARE_WIDGET(9, "combobox:", ComboBox)
	            else COMPARE_WIDGET(9, "slider2d:", Slider2d)
	            else COMPARE_WIDGET(10, "scrollbar:", Scrollbar)
	            else COMPARE_WIDGET(11, "loadingbar:", LoadingBar)
	            else COMPARE_WIDGET(11, "spinbutton:", SpinButton)
	            else COMPARE_WIDGET(12, "radiobutton:", RadioButton)
	            else COMPARE_WIDGET(12, "childwindow:", ChildWindow)
	            else COMPARE_WIDGET(12, "spritesheet:", SpriteSheet)
	            else COMPARE_WIDGET(16, "animatedpicture:", AnimatedPicture)
	            else
	                widgetFound = false;
	
	            // The line didn't contain a bracket or a new widget, so it must contain a property
	            if (!widgetFound)
	            {
	                std::string::size_type equalSignPosition = line.find('=');
	                if (equalSignPosition == std::string::npos)
	                {
	                    failed = true;
	                    break;
	                }
	
	                if (!widgetPtr.back()->setProperty(line.substr(0, equalSignPosition), line.substr(equalSignPosition + 1)))
	                    failed = true;
	            }
	        }
	    }
	    
	    m_File.close();
	    
	    if (failed) {
	        return false;
	    }
	    return true;
	    */
    	return true;
    }

//	class Container_my_func {
//		//[this, &m_File, &tabs, &saveWidgets] 
//		Container & container;
//		std::ofstream& m_File;
//		std::string & tabs;
//		std::function< void (std::vector<sf::String>&, std::vector<Widget::Ptr>&) > & saveWidgets;
//		Container_my_func(Container &c, std::ofstream& f, std::string & t, std::function< void (std::vector<sf::String>&, std::vector<Widget::Ptr>&) > & s ) : container(c), m_File(f), tabs(t), saveWidgets(s){}  
//		void operator()(std::vector<sf::String>& widgetName, std::vector<Widget::Ptr>& widgets)
//        {
//            auto nameIt = widgetName.cbegin();
//            for (auto widgetIt = widgets.cbegin(); widgetIt != widgets.cend(); ++widgetIt, ++nameIt)
//            {
//                switch ((*widgetIt)->getWidgetType())
//                {
//                    case Type_Tab:              m_File << tabs << "Tab: "; break;
//                    case Type_Grid:             m_File << tabs << "Grid: "; break;
//                    case Type_Panel:            m_File << tabs << "Panel: "; break;
//                    case Type_Label:            m_File << tabs << "Label: "; break;
//                    case Type_Button:           m_File << tabs << "Button: "; break;
//                    case Type_Slider:           m_File << tabs << "Slider: "; break;
//                    case Type_Picture:          m_File << tabs << "Picture: "; break;
//                    case Type_ListBox:          m_File << tabs << "ListBox: "; break;
//                    case Type_EditBox:          m_File << tabs << "EditBox: "; break;
//                    case Type_TextBox:          m_File << tabs << "TextBox: "; break;
//                    case Type_ChatBox:          m_File << tabs << "ChatBox: "; break;
//                    case Type_MenuBar:          m_File << tabs << "MenuBar: "; break;
//                    case Type_Checkbox:         m_File << tabs << "Checkbox: "; break;
//                    case Type_ComboBox:         m_File << tabs << "ComboBox: "; break;
//                    case Type_Slider2d:         m_File << tabs << "Slider2d: "; break;
//                    case Type_Scrollbar:        m_File << tabs << "Scrollbar: "; break;
//                    case Type_LoadingBar:       m_File << tabs << "LoadingBar: "; break;
//                    case Type_SpinButton:       m_File << tabs << "SpinButton: "; break;
//                    case Type_RadioButton:      m_File << tabs << "RadioButton: "; break;
//                    case Type_ChildWindow:      m_File << tabs << "ChildWindow: "; break;
//                    case Type_SpriteSheet:      m_File << tabs << "SpriteSheet: "; break;
//                    case Type_AnimatedPicture:  m_File << tabs << "AnimatedPicture: "; break;
//                    default:
//                        continue;
//                }
//                m_File << "\"" << nameIt->toAnsiString() << "\"" << std::endl;
//
//                m_File << tabs << "{" << std::endl;
//                tabs += "\t";
//
//                std::string value;
//                if ((*widgetIt)->getProperty("Filename", value))
//                {
//                    m_File << tabs << "Filename = \"" << value << "\"" << std::endl;
//                }
//                else if ((*widgetIt)->getProperty("ConfigFile", value))
//                {
//                    m_File << tabs << "ConfigFile = \"" << value << "\"" << std::endl;
//                }
//
//                auto properties = (*widgetIt)->getPropertyList();
//                for (auto propertyIt = properties.cbegin(); propertyIt != properties.cend(); ++propertyIt)
//                {
//                    if ((propertyIt->first != "Filename") && (propertyIt->first != "ConfigFile"))
//                    {
//                        (*widgetIt)->getProperty(propertyIt->first, value);
//
//                        if (propertyIt->second == "string")
//                            m_File << tabs << propertyIt->first << " = \"" << value << "\"" << std::endl;
//                        else
//                        {
//                            if (!value.empty())
//                                m_File << tabs << propertyIt->first << " = " << value << std::endl;
//                        }
//                    }
//                }
//
//                if ((*widgetIt)->m_ContainerWidget)
//                {
//                    m_File << std::endl;
//                    saveWidgets(Container::Ptr(*widgetIt)->getWidgetNames(), Container::Ptr(*widgetIt)->getWidgets());
//                }
//
//                tabs.erase(tabs.length()-1);
//                m_File << tabs << "}" << std::endl << std::endl;
//            }
//        }
//	};
    
    public boolean saveWidgetsToFile(String filename) {
        String tabs = "";

        PrintStream m_File = null;
        try {
        	m_File = new PrintStream(filename);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
        if (m_File == null) {
            return false;
        }

        m_File.println(tabs + "Window:");
        m_File.println(tabs + "{");
        tabs += "\t";

//        std::function< void (std::vector<sf::String>&, std::vector<Widget::Ptr>&) > saveWidgets;
//		saveWidgets = Container_my_func(*this, m_File, tabs, saveWidgets);
//        saveWidgets(m_ObjName, m_Widgets);

        tabs = tabs.substring(0, tabs.length() - 1 - 1); //FIXME:
        m_File.println(tabs + "}");

        return true;
    }

    public void addChildCallback(Callback callback) {
        if (m_GlobalCallbackFunctions.isEmpty()) {
            m_Parent.addChildCallback(callback);
        } else {
        	//FIXME:cbegin???
            for (CallbackFunction it : m_GlobalCallbackFunctions) {
                it.call(callback);
            }
        }
    }

    public abstract boolean mouseOnWidget(float x, float y);

    public void leftMousePressed(float x, float y) {
        Event event = new Event();
        event.type = Event.EventType.MouseButtonPressed;
        event.mouseButton.button = Mouse.Button.Left;
        event.mouseButton.x = (int)(x - getPosition().x);
        event.mouseButton.y = (int)(y - getPosition().y);

        // Let the event manager handle the event
        handleEvent(event);
    }

    public void leftMouseReleased(float x, float y) {
        Event event = new Event();
        event.type = Event.EventType.MouseButtonReleased;
        event.mouseButton.button = Mouse.Button.Left;
        event.mouseButton.x = (int)(x - getPosition().x);
        event.mouseButton.y = (int)(y - getPosition().y);

        // Let the event manager handle the event
        handleEvent(event);
    }

    public void mouseMoved(float x, float y) {
        Event event = new Event();
        event.type = Event.EventType.MouseMoved;
        event.mouseMove.x = (int)(x - getPosition().x);
        event.mouseMove.y = (int)(y - getPosition().y);

        // Let the event manager handle the event
        handleEvent(event);
    }

    public void keyPressed(Keyboard.Key key) {
        Event event = new Event();
        event.type = Event.EventType.KeyPressed;
        event.key.code = key;

        // Let the event manager handle the event
        handleEvent(event);
    }

    public void textEntered(int key) {
        Event event = new Event();
        event.type = Event.EventType.TextEntered;
        event.text.unicode = key;

        // Let the event manager handle the event
        handleEvent(event);
    }

    public void mouseWheelMoved(int delta, int x, int y) {
        Event event = new Event();
        event.type = Event.EventType.MouseWheelMoved;
        event.mouseWheel.delta = delta;
        event.mouseWheel.x = (int)(x - getPosition().x);
        event.mouseWheel.y = (int)(y - getPosition().y);

        // Let the event manager handle the event
        handleEvent(event);
    }

    public void mouseNotOnWidget() {
        if (m_MouseHover == true) {
            mouseLeftWidget();

            for (int i = 0; i < m_Widgets.size(); ++i) {
                m_Widgets.get(i).get().mouseNotOnWidget();
            }
            m_MouseHover = false;
        }
    }

    public void mouseNoLongerDown() {
        //Widget::
        super.mouseNoLongerDown();

        for (int i = 0; i < m_Widgets.size(); ++i) {
            m_Widgets.get(i).get().mouseNoLongerDown();
        }
    }

    public void widgetUnfocused() {
        unfocusWidgets();
    }

    protected void initialize(Container parent) {
        m_Parent = parent;
        setGlobalFont(m_Parent.getGlobalFont());
    }

    public void update() {
        for (int i = 0; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).get().m_AnimatedWidget) {
            	Time.plusEqual(m_Widgets.get(i).get().m_AnimationTimeElapsed,
                	m_AnimationTimeElapsed);
                m_Widgets.get(i).get().update();
            }
        }

        m_AnimationTimeElapsed = new Time();
    }

    public /*protected*/ boolean handleEvent(Event event) {
        if (event.type == Event.EventType.MouseMoved) {
            for (int i = 0; i < m_Widgets.size(); ++i) {
                if (m_Widgets.get(i).get().m_MouseDown) {
                    if ((m_Widgets.get(i).get().m_DraggableWidget) || 
                    	(m_Widgets.get(i).get().m_ContainerWidget)) {
                        m_Widgets.get(i).get().mouseMoved(
                        	(float)(event.mouseMove.x), 
                        	(float)(event.mouseMove.y));
                        return true;
                    }
                }
            }

            SharedWidgetPtr<? extends Widget> widget = mouseOnWhichWidget(
            		(float)(event.mouseMove.x), 
            		(float)(event.mouseMove.y));
            if (widget != null) {
                widget.get().mouseMoved(
                	(float)(event.mouseMove.x), 
                	(float)(event.mouseMove.y));
                return true;
            }

            return false;
        } else if (event.type == Event.EventType.MouseButtonPressed) {
            if (event.mouseButton.button == Mouse.Button.Left) {
            	SharedWidgetPtr<? extends Widget> widget = mouseOnWhichWidget(
                	(float)(event.mouseButton.x), 
                	(float)(event.mouseButton.y));
                if (widget != null) {
                    focusWidget(widget.get());

                    if (widget.get().m_ContainerWidget) {
                    	//FIXME: != widget
                        if ((m_FocusedWidget != 0) && 
                        	(m_Widgets.get(m_FocusedWidget - 1) != widget)) {
                            m_Widgets.get(m_FocusedWidget - 1).get().m_Focused = false;
                            m_Widgets.get(m_FocusedWidget - 1).get().widgetUnfocused();
                            m_FocusedWidget = 0;
                        }
                    }

                    widget.get().leftMousePressed(
                    	(float)(event.mouseButton.x), 
                    	(float)(event.mouseButton.y));
                    return true;
                } else { 
                	unfocusWidgets();
                }
            }

            return false;
        } else if (event.type == Event.EventType.MouseButtonReleased) {
            if (event.mouseButton.button == Mouse.Button.Left) {
            	SharedWidgetPtr<? extends Widget> widget = mouseOnWhichWidget(
                	(float)(event.mouseButton.x), 
                	(float)(event.mouseButton.y));
                if (widget != null) {
                    widget.get().leftMouseReleased(
                    	(float)(event.mouseButton.x), 
                    	(float)(event.mouseButton.y));
                }
                for (SharedWidgetPtr<? extends Widget> it : m_Widgets) {
                	if (it != widget) {
                        it.get().mouseNoLongerDown();
                	}
                }

                if (widget != null) {
                    return true;
                }
            }

            return false;
        } else if (event.type == Event.EventType.KeyPressed) {
            if (event.key.code != Keyboard.Key.Unknown) {
                if (m_FocusedWidget != 0) {
                    // Check the pressed key
                    if ((event.key.code == Keyboard.Key.Left) || 
                    	(event.key.code == Keyboard.Key.Right) || 
                     	(event.key.code == Keyboard.Key.Up) || 
                     	(event.key.code == Keyboard.Key.Down) || 
                     	(event.key.code == Keyboard.Key.BackSpace) || 
                     	(event.key.code == Keyboard.Key.Delete) || 
                     	(event.key.code == Keyboard.Key.Space) || 
                     	(event.key.code == Keyboard.Key.Return)) {
                        m_Widgets.get(m_FocusedWidget - 1).get()
                        	.keyPressed(event.key.code);
                    }
                    return true;
                }
            }

            return false;
        } else if (event.type == Event.EventType.KeyReleased) {
            if (event.key.code == Keyboard.Key.Tab) {
                return tabKeyPressed();
            } else {
                return false;
            }
        } else if (event.type == Event.EventType.TextEntered) {
            // Check if the character that we pressed is allowed
            if ((event.text.unicode >= 30) && (event.text.unicode != 127))
            {
                // Tell the widget that the key was pressed
                if (m_FocusedWidget != 0)
                {
                    m_Widgets.get(m_FocusedWidget - 1).get()
                    	.textEntered(event.text.unicode);
                    return true;
                }
            }

            return false;
        } else if (event.type == Event.EventType.MouseWheelMoved) {
        	SharedWidgetPtr<? extends Widget> widget = mouseOnWhichWidget(
        		(float)(event.mouseWheel.x), 
        		(float)(event.mouseWheel.y));
            if (widget != null) {
                widget.get().mouseWheelMoved(event.mouseWheel.delta, event.mouseWheel.x,  event.mouseWheel.y);
                return true;
            }

            return false;
        } else {
            return false;
        }
    }

    protected boolean focusNextWidgetInContainer() {
        if (Defines.tabKeyUsageEnabled == false) {
            return false;
        }

        for (int i = m_FocusedWidget; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).get().m_AllowFocus == true) {
                if (m_Widgets.get(i).get().m_Visible && 
                	m_Widgets.get(i).get().m_Enabled) {
                    if (!m_Widgets.get(i).get().m_ContainerWidget || 
                    	(((SharedWidgetPtr<Container>)m_Widgets.get(i)).get()
                    		.focusNextWidgetInContainer())) {
                        if (m_FocusedWidget > 0) {
                            m_Widgets.get(m_FocusedWidget - 1).get().m_Focused = false;
                            m_Widgets.get(m_FocusedWidget - 1).get().widgetUnfocused();
                        }
                        m_FocusedWidget = i + 1;
                        m_Widgets.get(i).get().m_Focused = true;
                        m_Widgets.get(i).get().widgetFocused();

                        return true;
                    }
                }
            }
        }

        unfocusWidgets();
        return false;
    }

    protected boolean tabKeyPressed() {
        if (Defines.tabKeyUsageEnabled == false) {
            return false;
        }

        if (m_FocusedWidget != 0) {
            if (m_Widgets.get(m_FocusedWidget - 1).get().m_ContainerWidget) {
                if (((SharedWidgetPtr<Container>)m_Widgets.get(m_FocusedWidget - 1))
                		.get().focusNextWidgetInContainer()) {
                    return true;
                }
            }
        }

        for (int i = m_FocusedWidget; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).get().m_AllowFocus == true) {
                if ((m_Widgets.get(i).get().m_Visible) && 
                	(m_Widgets.get(i).get().m_Enabled)) {
                    if (m_FocusedWidget != 0) {
                        m_Widgets.get(m_FocusedWidget - 1).get().m_Focused = false;
                        m_Widgets.get(m_FocusedWidget - 1).get().widgetUnfocused();
                    }

                    m_FocusedWidget = i + 1;
                    m_Widgets.get(i).get().m_Focused = true;
                    m_Widgets.get(i).get().widgetFocused();
                    return true;
                }
            }
        }

        if (m_FocusedWidget != 0) {
            for (int i = 0; i < m_FocusedWidget - 1; ++i) {
                if (m_Widgets.get(i).get().m_AllowFocus == true) {
                    if (m_Widgets.get(i).get().m_Visible && 
                    	m_Widgets.get(i).get().m_Enabled) {
                        m_Widgets.get(m_FocusedWidget - 1).get().m_Focused = false;
                        m_Widgets.get(m_FocusedWidget - 1).get().widgetUnfocused();

                        m_FocusedWidget = i + 1;
                        m_Widgets.get(i).get().m_Focused = true;
                        m_Widgets.get(i).get().widgetFocused();
                        return true;
                    }
                }
            }
        }

        if (m_FocusedWidget != 0 && 
        	(m_Widgets.get(m_FocusedWidget - 1).get().m_ContainerWidget)) {
            ((SharedWidgetPtr<Container>)m_Widgets.get(m_FocusedWidget - 1)).get().tabKeyPressed();
            return true;
        }
        
        return false;
    }

    protected SharedWidgetPtr<? extends Widget> mouseOnWhichWidget(float x, float y) {
        boolean widgetFound = false;
        SharedWidgetPtr<? extends Widget> widget = null;

        //.rbegin() FIXME:
        for (SharedWidgetPtr<? extends Widget> it : m_Widgets) {
            if (it.get().m_Visible && it.get().m_Enabled) {
                if (widgetFound == false) {
                    if (it.get().mouseOnWidget(x, y)) {
                        widget = it;
                        widgetFound = true;
                    }
                } else { 
                	it.get().mouseNotOnWidget();
                }
            }
        }

        return widget;
    }

    public/*protected*/ void drawWidgetContainer(RenderTarget target, RenderStates states) {
        for (int i = 0; i < m_Widgets.size(); ++i) {
            if (m_Widgets.get(i).get().m_Visible) {
                m_Widgets.get(i).get().draw(target, states);
            }
        }
    }
    
    
}