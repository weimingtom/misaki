package com.iteye.weimingtom.tgui.sf;

public class Glyph {
    public int advance;
    public IntRect bounds;
    public IntRect textureRect; 

    public Glyph() {
		this.advance = 0;
		this.bounds = new IntRect();
		this.textureRect = new IntRect();
	}
}
