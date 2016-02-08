package com.iteye.weimingtom.tgui;

import com.iteye.weimingtom.tgui.sf.Image;
import com.iteye.weimingtom.tgui.sf.IntRect;
import com.iteye.weimingtom.tgui.sf.SFTexture;

public class TextureData {
    public Image image = null;
    public SFTexture texture = new SFTexture();
    public IntRect rect = new IntRect();
    public String filename = "";
    public int users = 0;

	public TextureData()  {
    	image = null;
    }
}
