package com.iteye.weimingtom.tgui.sf;

public class Event {
    public class KeyEvent {
        public Keyboard.Key code;    
        public boolean alt;
        public boolean control; 
        public boolean shift; 
        public boolean system; 
    }

    public class TextEvent {
    	public int unicode; 
    }

    public class MouseButtonEvent {
        public Mouse.Button button; 
        public int x; 
        public int y; 
    }
	
    public class MouseMoveEvent {
        public int x; 
        public int y; 
    }
    
    public class MouseWheelEvent {
        public int delta; 
        public int x; 
        public int y; 
    }

    public enum EventType {
        Closed, 
        Resized,
        LostFocus,
        GainedFocus, 
        TextEntered,
        KeyPressed,
        KeyReleased, 
        MouseWheelMoved,
        MouseButtonPressed,
        MouseButtonReleased,
        MouseMoved,   
        MouseEntered,    
        MouseLeft,    
        JoystickButtonPressed, 
        JoystickButtonReleased, 
        JoystickMoved, 
        JoystickConnected,
        JoystickDisconnected, 

        Count               
    }
	
	public EventType type;
	
    //union
//	SizeEvent size;     
    public KeyEvent key;   
    public TextEvent text;   
    public MouseMoveEvent mouseMove;  
    public MouseButtonEvent mouseButton; 
    public MouseWheelEvent mouseWheel;   
//    JoystickMoveEvent joystickMove; 
//    JoystickButtonEvent joystickButton; 
//    JoystickConnectEvent joystickConnect; 
}
