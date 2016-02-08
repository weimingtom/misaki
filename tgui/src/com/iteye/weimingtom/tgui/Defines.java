package com.iteye.weimingtom.tgui;

import java.util.List;

import com.iteye.weimingtom.tgui.sf.Color;
import com.iteye.weimingtom.tgui.sf.Vector2f;
import com.iteye.weimingtom.tgui.sf.Vector2u;

/**
 * TGUI.cpp
 * TODO: !!!!add log!!!!
 * @author Administrator
 *
 */
public class Defines {
	//新增，调试用
	public static boolean DEBUG = true;
	public static boolean DEBUG_JGRAPHLIB = false;
	
    public static TextureManager TGUI_TextureManager = new TextureManager();

    public static boolean tabKeyUsageEnabled = true;
    
    public static Color extractColor(String string) {
    	System.out.println(">>>extractColor : " + string);
    	
        int red;
        int green;
        int blue;
        int alpha = 255;

        if (string.length() != 0) {
            if ((string.charAt(0) == '(') && 
            	(string.charAt(string.length() - 1) == ')')) {
                //string.erase(0, 1);
                //string.erase(string.length()-1);
            	string = string.substring(1, string.length() - 1);
            	
                int commaPos = string.indexOf(',');
                if (commaPos != -1) {
                    red = Integer.parseInt(string.substring(0, commaPos).trim());
                    //string.erase(0, commaPos + 1);
                    string = string.substring(commaPos + 1);
                    
                    commaPos = string.indexOf(',');
                    if (commaPos != -1) {
                        green = Integer.parseInt(string.substring(0, commaPos).trim());
                        //string.erase(0, commaPos+1);
                        string = string.substring(commaPos + 1);

                        commaPos = string.indexOf(',');
                        if (commaPos != -1) {
                            blue = Integer.parseInt(string.substring(0, commaPos).trim());
                            //string.erase(0, commaPos+1);
                            string = string.substring(commaPos + 1);
                            
                            alpha = Integer.parseInt(string.trim());
                        } else {
                        	//FIXME: full string, commaPos don't point to the end
                            blue = Integer.parseInt(string.trim());
                        }

                        return new Color(red & 0xff,
	                         green & 0xff,
	                         blue & 0xff,
	                         alpha & 0xff);
                    }
                }
            }
        }

        return Color.Black;
    }

    public static String convertColorToString(Color color) {
        if (color.a < 255) {
            return "(" + 
            	Integer.toString((int)color.r) + "," + 
            	Integer.toString((int)color.g) + "," + 
            	Integer.toString((int)color.b) + "," + 
            	Integer.toString((int)color.a) + ")";
        } else {
            return "(" + 
            	Integer.toString((int)color.r) + "," + 
            	Integer.toString((int)color.g) + "," + 
            	Integer.toString((int)color.b) + ")";
        }
    }

    public static boolean extractVector2f(String string, Vector2f vector) {
    	System.out.println(">>>extractVector2f : " + string);
    	
    	if (string.length() != 0) {
            if ((string.charAt(0) == '(') && 
            	(string.charAt(string.length() - 1) == ')')) {
                //string.erase(0, 1);
                //string.erase(string.length()-1);
            	string = string.substring(1, string.length() - 1);
            	
                int commaPos = string.indexOf(',');
                if (commaPos != -1) {
                    vector.x = (float)(Float.parseFloat(string.substring(0, commaPos)));
                    //string.erase(0, commaPos+1);
                    string = string.substring(commaPos + 1);
                    
                    vector.y = (float)(Float.parseFloat(string));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean extractVector2u(String string, Vector2u vector) {
    	System.out.println(">>>extractVector2u : " + string);
    	
    	if (string.length() != 0) {
            if ((string.charAt(0) == '(') && 
            	(string.charAt(string.length() - 1) == ')')) {
                //string.erase(0, 1);
                //string.erase(string.length()-1);
            	string = string.substring(1, string.length() - 1);
            	
                int commaPos = string.indexOf(',');
                if (commaPos != -1) {
                    vector.x = (int)(Integer.parseInt(string.substring(0, commaPos).trim()));
                    //string.erase(0, commaPos+1);
                    string = string.substring(commaPos + 1);
                    
                    vector.y = (int)(Integer.parseInt(string.trim()));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean extractBorders(String string, Borders borders) {
    	System.out.println(">>>extractBorders : " + string);
    	
    	if (string.length() != 0) {
            if ((string.charAt(0) == '(') && 
            	(string.charAt(string.length() - 1) == ')')) {
                //string.erase(0, 1);
                //string.erase(string.length()-1);
            	string = string.substring(1, string.length() - 1);
            	
                int commaPos = string.indexOf(',');
                if (commaPos != -1) {
                    borders.left = Integer.parseInt(string.substring(0, commaPos).trim());
                    //string.erase(0, commaPos+1);
                    string = string.substring(commaPos + 1);
                    
                    commaPos = string.indexOf(',');
                    if (commaPos != -1) {
                        borders.top = Integer.parseInt(string.substring(0, commaPos).trim());
                        //string.erase(0, commaPos+1);
                        string = string.substring(commaPos + 1);
                        
                        commaPos = string.indexOf(',');
                        if (commaPos != -1) {
                            borders.right = Integer.parseInt(string.substring(0, commaPos).trim());
                            //string.erase(0, commaPos+1);
                            string = string.substring(commaPos + 1);
                            
                            borders.bottom = Integer.parseInt(string.trim());

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static void encodeString(String origString, String[] encodedString) {
        encodedString[0] = origString;

        encodedString[0] = encodedString[0].replace("\\", "\\\\");
        encodedString[0] = encodedString[0].replace("\n", "\\n");
        encodedString[0] = encodedString[0].replace("\t", "\\t");
        encodedString[0] = encodedString[0].replace("\"", "\\\"");
        encodedString[0] = encodedString[0].replace(",", "\\,"); //FIXME:
    }

    public static void decodeString(String encodedString, String[] decodedString) {
        decodedString[0] = "";

        for (int i = 0; i < encodedString.length(); i++)  {
        	char it = encodedString.charAt(i);
        	if (it == '\\') {
                char next = 0;
                if (i + 1 < encodedString.length()) {
                	encodedString.charAt(i + 1);
                }
                if (i + 1 >= encodedString.length()) {
                    Defines.TGUI_OUTPUT("TGUI warning: Escape character at the end of the string. Ignoring character.");
                    continue;
                } else if ((next == '\\') || (next == '\"')) {
                    decodedString[0] += next;
                    ++it;
                } else if (next == 'n') {
                    decodedString[0] += '\n';
                    ++it;
                } else if (next == 't') {
                    decodedString[0] += '\t';
                    ++it;
                } else {
                    Defines.TGUI_OUTPUT("TGUI warning: Escape character in front of '" + next + "'. Ignoring escape character.");
                    continue;
                }
            } else {
            	decodedString[0] += it;
            }
        }
    }

    public static void encodeList(List<String> list, String[] encodedString) {
        encodedString[0] = "";

        for (int i = 0; i < list.size(); ++i) {
            String item = list.get(i);

            item = item.replace("\\", "\\\\");
            item = item.replace("\n", "\\n");
            item = item.replace("\t", "\\t");
            item = item.replace("\"", "\\\"");
            item = item.replace(",", "\\,"); //FIXME:

            if (i == 0) {
                encodedString[0] += item;
            } else {
                encodedString[0] += "," + item;
            }
        }
    }

    public static void decodeList(String encodedString, List<String> list) {
        String item = "";

        for (int i = 0; i < encodedString.length(); ++i) {
        	char it = encodedString.charAt(i);
        	if (it == '\\') {
                char next = 0;
                if (i + 1 < encodedString.length()) {
                	encodedString.charAt(i + 1);
                }
                
                if (i + 1 >= encodedString.length()) {
                    TGUI_OUTPUT("TGUI warning: Escape character at the end of the string. Ignoring character.");
                    continue;
                } else if ((next == '\\') || (next == '\"') || (next == ',')) {
                    item += next;
                    ++it;
                } else if (next == 'n') {
                    item += '\n';
                    ++it;
                } else if (next == 't') {
                    item += '\t';
                    ++it;
                } else {
                    Defines.TGUI_OUTPUT("TGUI warning: Escape character in front of '" + next + "'. Ignoring escape character.");
                    continue;
                }
            } else {
                if (it == ',') {
                    list.add(item);
                    item = "";
                } else { 
                	item += it;
                }
            }
        }

        if (item.length() != 0) {
            list.add(item);
        }
    }

    public static String toLower(String str) {
        //for (std::string::iterator i = str.begin(); i != str.end(); ++i)
        //   *i = static_cast<char>(std::tolower(*i));
        return str.toLowerCase();
    }
    
    public static void TGUI_OUTPUT(String x) {
    	System.err.println(x);
    }

    public static double TGUI_MINIMUM(double x, double y) {
    	return (x < y) ? x : y;
    }

    public static double TGUI_MAXIMUM(double x, double y) {   
    	return (x > y) ? x : y;
    }
}
