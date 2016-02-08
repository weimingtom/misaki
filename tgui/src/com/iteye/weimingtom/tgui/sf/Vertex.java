package com.iteye.weimingtom.tgui.sf;

public class Vertex {
    public Vector2f position;
    public Color color;
    public Vector2f texCoords;
    
    public Vertex() {
    	position = new Vector2f(0, 0);
    	color = new Color(255, 255, 255);
    	texCoords = new Vector2f(0, 0);
    }
    
    public Vertex(Vector2f thePosition) {
    	position = new Vector2f(thePosition);
    	color = new Color(255, 255, 255);
    	texCoords = new Vector2f(0, 0);
    }
    
    public Vertex(Vector2f thePosition, Color theColor) {
    	position = new Vector2f(thePosition);
    	color = new Color(theColor);
    	texCoords = new Vector2f(0, 0);
    }
    
    public Vertex(Vector2f thePosition, Vector2f theTexCoords) {
    	position = new Vector2f(thePosition);
    	color = new Color(255, 255, 255);
    	texCoords = new Vector2f(theTexCoords);
    }
    
    public Vertex(Vector2f thePosition, Color theColor, Vector2f theTexCoords) {
    	position = new Vector2f(thePosition);
    	color = new Color(theColor);
    	texCoords = new Vector2f(theTexCoords);
    }
    
    public String toString() {
    	return "Vertex => {" + 
    		"position : " + position + "," +
    		"color : " + color + "," +
    		"texCoords : " + texCoords + "}";
    }
}
