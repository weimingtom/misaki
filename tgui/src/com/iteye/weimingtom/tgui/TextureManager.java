package com.iteye.weimingtom.tgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iteye.weimingtom.tgui.sf.Image;
import com.iteye.weimingtom.tgui.sf.IntRect;

public class TextureManager {
    protected class ImageMapData {
    	public Image image = new Image();
        public List<TextureData> data = new ArrayList<TextureData>();
    }

	protected Map<String, ImageMapData> m_ImageMap = 
        	new HashMap<String, ImageMapData>();
    
	public boolean getTexture(String filename, Texture texture) {
		return getTexture(filename, texture, new IntRect(0, 0, 0, 0));
	}
	
	public boolean getTexture(String filename, Texture texture, IntRect rect) {
		ImageMapData imageIt = m_ImageMap.get(filename);
        if (imageIt != null) {
            for (TextureData it : imageIt.data) {
                if (IntRect.isEqual(it.rect, rect)) {
                    ++it.users;

                    texture.data = it;

                    texture.sprite.setTexture(it.texture, true);

                    return true;
                }
            }
        } else {
        	ImageMapData newData = new ImageMapData();
            m_ImageMap.put(filename, newData);
            imageIt = newData;
        }

        imageIt.data.add(new TextureData());
        texture.data = imageIt.data.get(imageIt.data.size() - 1);
        texture.data.image = imageIt.image;
        texture.data.rect.assign(rect);

        if (texture.data.image.loadFromFile(filename)) {
            boolean success = false;
            if (IntRect.isEqual(rect, new IntRect(0, 0, 0, 0))) {
                success = texture.data.texture.loadFromImage(texture.data.image);
            } else {
                success = texture.data.texture.loadFromImage(texture.data.image, rect);
            }
            
            if (success) {
                texture.sprite.setTexture(texture.data.texture, true);

                texture.data.filename = filename;
                texture.data.users = 1;
                return true;
            }
        }

        m_ImageMap.remove(imageIt);
        texture.data = null;
        return false;
	}

	public boolean copyTexture(Texture textureToCopy, Texture newTexture) {
        if (textureToCopy.data == null) {
            newTexture.data = null;
            return true;
        }

        for (Map.Entry<String,TextureManager.ImageMapData> imageIt : m_ImageMap.entrySet()) {
            for (TextureData dataIt : imageIt.getValue().data) {
                if (dataIt == textureToCopy.data) {
                    ++(dataIt.users);
                    newTexture = textureToCopy;
                    return true;
                }
            }
        }

        Defines.TGUI_OUTPUT("TGUI warning: Can't copy texture that wasn't loaded by TextureManager.");
        return false;
	}

    public void removeTexture(Texture textureToRemove) {
    	for (Map.Entry<String,TextureManager.ImageMapData> imageIt : m_ImageMap.entrySet()) {
            for (TextureData dataIt : imageIt.getValue().data) {
                if (dataIt == textureToRemove.data) {
                    if (--(dataIt.users) == 0) {
                    	int usage = 0;
                    	for (TextureData data : imageIt.getValue().data) {
                    		if (data.image == dataIt.image) {
                    			usage++;
                    		}
                    	}
                    	if (usage == 1) {
                            m_ImageMap.remove(imageIt);
                        } else {
                            imageIt.getValue().data.remove(dataIt);
                        }
                    }

                    textureToRemove.data = null;
                    return;
                }
            }
        }

        Defines.TGUI_OUTPUT("TGUI warning: Can't remove texture that wasn't loaded by TextureManager.");
        return;
    }
}
