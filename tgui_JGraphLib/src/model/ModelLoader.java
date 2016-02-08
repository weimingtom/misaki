package model;

import geometry.Vector3d;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andrey Kokorev
 *         Created on 31.01.2015.
 */
public class ModelLoader {
    public static Model fromFile(String model, String textureImg) throws IOException {
        Map<Integer, Vector3d> vertices = new HashMap<Integer, Vector3d>();
        Map<Integer, Vector3d> texture = new HashMap<Integer, Vector3d>();
        List<Model.Face> faces = new ArrayList<Model.Face>();
        AtomicInteger vCounter = new AtomicInteger(0);
        AtomicInteger tCounter = new AtomicInteger(0);

        BufferedImage tImg = TGALoader.loadTGA(textureImg);
        int textureWidth  = tImg.getWidth();
        int textureHeight = tImg.getHeight();

        InputStream istr = new FileInputStream(model);
        Reader reader = new InputStreamReader(istr, "utf8");
        BufferedReader buf = new BufferedReader(reader);
        String str;
        while ((str = buf.readLine()) != null) { 	
            String[] spt = str.split("\\s+");
            if ("v".equals(spt[0])) {
                    vertices.put(vCounter.incrementAndGet(), new Vector3d(
                            Double.parseDouble(spt[1]),
                            Double.parseDouble(spt[2]),
                            Double.parseDouble(spt[3])
                    ));
            } else if ("vt".equals(spt[0])) {
                    texture.put(tCounter.incrementAndGet(), new Vector3d(
                            Double.parseDouble(spt[1]) * textureWidth,
                            (1 - Double.parseDouble(spt[2])) * textureHeight,
                            0
                    ));
            } else if ("f".equals(spt[0])) {
                faces.add(new Model.Face(
                        Integer.parseInt(spt[1].split("/")[0]), //vertices
                        Integer.parseInt(spt[2].split("/")[0]),
                        Integer.parseInt(spt[3].split("/")[0]),
                        Integer.parseInt(spt[1].split("/")[1]),  //texture
                        Integer.parseInt(spt[2].split("/")[1]),
                        Integer.parseInt(spt[3].split("/")[1])
                ));
            }
        }
        buf.close();
        reader.close();
        istr.close();
        
        return new Model(vertices, texture, faces, tImg);
    }
}
