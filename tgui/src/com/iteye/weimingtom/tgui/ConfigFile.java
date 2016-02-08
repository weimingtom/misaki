package com.iteye.weimingtom.tgui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.IntRect;

public class ConfigFile {
	//FIXME: use java.util.Scanner instead of BufferedReader
    private BufferedReader m_File;

    public void destroy() {
    	if (m_File != null) {
            try {
				m_File.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
            m_File = null;
        }
    }
    
    public boolean open(String filename) {
        if (m_File != null) {
            try {
				m_File.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
            m_File = null;
        }

        try {
			m_File = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(filename), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

        if (m_File != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean read(String section, List<String> properties, 
    	List<String> values) {
        boolean error = false;
        boolean sectionFound = false;
        int lineNumber = 0;

        String line = null;
        try {//FIXME:
			while ((line = m_File.readLine()) != null) {
			    lineNumber++;

			    int[] c = new int[1];
			    c[0] = 0;
			    int[] cc = new int[]{0}; //FIXME: readonly
			    
			    String[] sectionName = new String[1];
			    if (isSection(line, cc, sectionName)) {
			        if (sectionFound) {
			            return !error;
			        }

			        section = Defines.toLower(section);
			        sectionName[0] = Defines.toLower(sectionName[0]);

			        if ((section + ":").equals(sectionName[0])) {
			            sectionFound = true;
			        }
			    } else {
			        if (!sectionFound) {
			            continue;
			        }

			        String property;
			        String value;

			        if (!removeWhitespace(line, c)) {
			            continue; // empty line
			        }

			        property = readWord(line, c);
			        property = Defines.toLower(property);

			        if (!removeWhitespace(line, c)) {
			            Defines.TGUI_OUTPUT("TGUI error: Failed to parse line " + Integer.toString(lineNumber) + ".");
			            error = true;
			        }

			        if (line.charAt(c[0]) == '=') {
			            ++c[0];
			    	} else {
			            Defines.TGUI_OUTPUT("TGUI error: Failed to parse line " + Integer.toString(lineNumber) + ".");
			            error = true;
			        }

			        if (!removeWhitespace(line, c)) {
			            Defines.TGUI_OUTPUT("TGUI error: Failed to parse line " + Integer.toString(lineNumber) + ".");
			            error = true;
			        }

			        int pos = c[0] - 0;
			        value = line.substring(pos, line.length() - pos + pos);

			        properties.add(property);
			        values.add(value);
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

        if (!sectionFound) {
            Defines.TGUI_OUTPUT("TGUI error: Section '" + section + "' was not found in the config file.");
            error = true;
        }

        return !error;
    }

    public boolean readBool(String value, boolean defaultValue) {
        if (("true".equals(value)) || 
        	("True".equals(value)) || 
        	("TRUE".equals(value)) || 
        	("1".equals(value))) {
            return true;
        } else if (("false".equals(value)) || 
        	("False".equals(value)) || 
        	("FALSE".equals(value)) || 
        	("0".equals(value))) {
            return false;
        } else {
            return defaultValue;
        }
    }

    public Color readColor(String value) {
        return Defines.extractColor(value);
    }

    public boolean readIntRect(String value, IntRect rect) {
        if (value.length() != 0) {
            if ((value.charAt(0) == '(') && 
            	(value.charAt(value.length() - 1) == ')')) {
                //value.erase(0, 1);
                //value.erase(value.length()-1);
            	value = value.substring(1, value.length() - 1);
            	
                int commaPos = value.indexOf(',');
                if (commaPos != -1) {
                    rect.left = Integer.parseInt(value.substring(0, commaPos).trim());
                    //value.erase(0, commaPos+1);
                    value = value.substring(commaPos + 1);
                    
                    commaPos = value.indexOf(',');
                    if (commaPos != -1) {
                        rect.top = Integer.parseInt(value.substring(0, commaPos).trim());
                        //value.erase(0, commaPos+1);
                        value = value.substring(commaPos + 1);
                        
                        commaPos = value.indexOf(',');
                        if (commaPos != -1) {
                            rect.width =  Integer.parseInt(value.substring(0, commaPos).trim());
                            //value.erase(0, commaPos+1);
                            value = value.substring(commaPos + 1);
                            
                            // Get the height value
                            rect.height = Integer.parseInt(value.trim());

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean readTexture(String value, String rootPath, 
    	Texture texture) {
    	//value.begin()
        int[] c = new int[1];
        c[0] = 0;
        
        if (!removeWhitespace(value, c)) {
            return false;
        }

        if (value.charAt(c[0]) == '"') {
            ++c[0];
        } else {
            return false;
        }
        
        String filename = "";
        char prev = '\0';

        boolean filenameFound = false;
        while (c[0] < value.length()) {
            char cc = value.charAt(c[0]);
        	if ((cc != '"') || (prev == '\\')) {
                prev = cc;
                filename += cc;
                ++c[0];
            } else {
                ++c[0];
                filenameFound = true;
                break;
            }
        }

        if (!filenameFound) {
            return false;
        }

        IntRect rect = new IntRect();
        if (removeWhitespace(value, c)) {
//        	System.out.println("value == " + value + " ||| " + 
//        			"c[0] ==" + c[0] + " ||| value.length() ==" + value.length() + " ||| " +
//        			value.substring(c[0] - 0, value.length() - (c[0] - 0) + (c[0] - 0)));
            if (!readIntRect(value.substring(c[0] - 0, value.length() - (c[0] - 0) + (c[0] - 0)), rect)) {
                return false;
            }
        }
        return Defines.TGUI_TextureManager.getTexture(rootPath + filename, texture, rect);
    }

    public void close() {
        if (m_File != null) {
            try {
				m_File.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
            m_File = null;
        }
    }

    private boolean removeWhitespace(String line, int[] c) {
        while (c[0] < line.length()) {
        	char cc = line.charAt(c[0]);
            if ((cc == ' ') || (cc == '\t') || (cc == '\r')) {
                ++c[0];
            } else {
                return true;
            }
        }
        return false;
    }

    public String readWord(String line, int[] c) {
        String word = "";
        while (c[0] < line.length()) {
            char cc = line.charAt(c[0]);
        	if ((cc != ' ') && (cc != '\t') && (cc != '\r')) {
                word += (cc);
                ++c[0];
            } else {
                return word;
            }
        }
        return word;
    }

    public boolean isSection(String line, int[] c, String[] sectionName) {
        if (!removeWhitespace(line, c)) {
            return false;
        }

        sectionName[0] = readWord(line, c);

        removeWhitespace(line, c);

        if (c[0] < line.length()) {
            return false;
        }

        if (sectionName[0].charAt(sectionName[0].length() - 1) == ':') {
            return true;
        } else {
            return false;
        }
    }
}
