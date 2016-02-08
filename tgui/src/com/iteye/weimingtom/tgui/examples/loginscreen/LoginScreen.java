package com.iteye.weimingtom.tgui.examples.loginscreen;

import com.iteye.weimingtom.tgui.Callback;
import com.iteye.weimingtom.tgui.Gui;
import com.iteye.weimingtom.tgui.SharedWidgetPtr;
import com.iteye.weimingtom.tgui.sf.Event;
import com.iteye.weimingtom.tgui.sf.RenderWindow;
import com.iteye.weimingtom.tgui.sf.VideoMode;
import com.iteye.weimingtom.tgui.widget.Button;
import com.iteye.weimingtom.tgui.widget.EditBox;
import com.iteye.weimingtom.tgui.widget.Label;
import com.iteye.weimingtom.tgui.widget.Picture;

public class LoginScreen {
	private static void loadWidgets(Gui gui) {
	    SharedWidgetPtr<Picture> picture = 
	    	new SharedWidgetPtr<Picture>(new Picture(), gui);
	    picture.get().load("./examples/xubuntu_bg_aluminium.jpg");
	    picture.get().setSize(800, 600);

	    SharedWidgetPtr<Label> labelUsername = 
		    new SharedWidgetPtr<Label>(new Label(), gui);
	    labelUsername.get().setText("Username:");
	    labelUsername.get().setPosition(200, 100);

	    SharedWidgetPtr<Label> labelPassword = 
		    new SharedWidgetPtr<Label>(new Label(), gui);
	    labelPassword.get().setText("Password:");
	    labelPassword.get().setPosition(200, 250);

	    SharedWidgetPtr<EditBox> editBoxUsername = 
		    new SharedWidgetPtr<EditBox>(new EditBox(), gui, "Username");
	    editBoxUsername.get().load("./widgets/Black.conf");
	    editBoxUsername.get().setSize(400, 40);
	    editBoxUsername.get().setPosition(200, 140);

	    SharedWidgetPtr<EditBox> editBoxPassword = 
		    new SharedWidgetPtr<EditBox>(new EditBox(), gui, "Password");
	    editBoxPassword.get().setPosition(200, 290);
	    editBoxPassword.get().setPasswordCharacter('*');

	    SharedWidgetPtr<Button> button = 
		    new SharedWidgetPtr<Button>(new Button(), gui);
	    button.get().load("./widgets/Black.conf");
	    button.get().setSize(260, 60);
	    button.get().setPosition(270, 440);
	    button.get().setText("Login");
	    button.get().getCallbackManager().bindCallback(
	    	Button.ClickableWidgetCallbacks.LeftMouseClicked.value());
	    button.get().setCallbackId(1);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    RenderWindow window = new RenderWindow(
	    	new VideoMode(800, 600), "TGUI window");
	    Gui gui = new Gui(window);

	    gui.setGlobalFont("./fonts/DejaVuSans.ttf");
	    
	    loadWidgets(gui);
	    
	    while (window.isOpen()) {
	        Event event = new Event();
	        while (window.pollEvent(event)) {
	            if (event.type == Event.EventType.Closed) {
	                window.close();
	            }

	            gui.handleEvent(event);
	        }

	        Callback callback = new Callback();
	        while (gui.pollCallback(callback)) {
	            if (callback.id == 1) {
	            	SharedWidgetPtr<EditBox> editBoxUsername = (SharedWidgetPtr<EditBox>)gui.get("Username");
	            	SharedWidgetPtr<EditBox> editBoxPassword = (SharedWidgetPtr<EditBox>)gui.get("Password");

	                String username = editBoxUsername.get().getText();
	                String password = editBoxPassword.get().getText();
	                
	                System.out.println("username : " + username);
	                System.out.println("password : " + password);
	            }
	        }

	        window.clear();

	        gui.draw();

	        window.display();
	    }
	}

}
