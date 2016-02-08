package com.iteye.weimingtom.tgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallbackManager {
    public /*protected*/ Map<Integer, List<Call0>> m_CallbackFunctions = 
    		new HashMap<Integer, List<Call0>>();
    public /*protected*/ Callback m_Callback = new Callback();

    public CallbackManager() {
    	//TODO:
    }
    
    public CallbackManager(CallbackManager copy) {
    	//TOOD:
    }
    
	public void bindCallback(Call0 func, int trigger) {
		mapCallback(func, trigger);
	}

	public /*protected*/ void bindCallback(Call0 func, Object classPtr, int trigger) {
        //mapCallback(std::bind(func, classPtr), trigger);
		mapCallback(func, trigger);
    }
	
    public void bindCallbackEx(Call0 func, int trigger) {
    	//FIXME:
    	//mapCallback(std::bind(func, std::ref(m_Callback)), trigger);
    	mapCallback(func, trigger);
    }

    public /*public?*/ void bindCallbackEx(Call0 func, Object classPtr, int trigger) {
    	//FIXME:
    	//mapCallback(std::bind(func, classPtr, std::ref(m_Callback)), trigger);
    	mapCallback(func, trigger);
    }

    public void bindCallback(int trigger) {
        mapCallback(null, trigger);    	
    }
    
    public void unbindCallback(int trigger) {
        int counter = 1;
        while (counter * 2 <= trigger) {
            counter *= 2;
        }

        while (counter > 0) {
            if (trigger >= counter) {
                m_CallbackFunctions.remove(counter);
                trigger -= counter;
            }

            counter /= 2;
        }
    }

    public void unbindAllCallback() {
    	m_CallbackFunctions.clear();
    }

    protected void mapCallback(Call0 function, int trigger) {
        int counter = 1;
        while (counter * 2 <= trigger) {
            counter *= 2;
        }

        while (counter > 0) {
            if (trigger >= counter) {
            	List<Call0> functionList = m_CallbackFunctions.get(counter);
            	if (functionList == null) {
            		functionList = new ArrayList<Call0>();
            		m_CallbackFunctions.put(counter, functionList);
            	}
            	functionList.add(function);
                trigger -= counter;
            }

            counter /= 2;
        }
    }
}
