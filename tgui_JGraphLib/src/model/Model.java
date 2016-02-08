package model;

import geometry.Vector3d;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * @author Andrey Kokorev
 *         Created on 31.01.2015.
 */
public class Model {
    private Map<Integer, Vector3d> vertex;
    private List<Face> face;
    private Map<Integer, Vector3d> texture;

    private BufferedImage textureImg;

    public Model(Map<Integer, Vector3d> v, Map<Integer, Vector3d> texture, List<Face> f, BufferedImage textureImg)
    {
        this.face = f;
        this.vertex = v;
        this.texture = texture;
        this.textureImg = textureImg;
    }

    public Map<Integer, Vector3d> getVertices() {
        return vertex;
    }

    public List<Face> getFaces() {
        return face;
    }

    public Map<Integer, Vector3d> getTexture() {
        return texture;
    }

    public BufferedImage getTextureImg() {
        return textureImg;
    }

    public static class Face
    {
        private int[] vertex;

        private int[] texture;

        public Face(int v1, int v2, int v3, int t0, int t1, int t2)
        {
            vertex = new int[3];
            vertex[0] = v1;
            vertex[1] = v2;
            vertex[2] = v3;

            texture = new int[3];
            texture[0] = t0;
            texture[1] = t1;
            texture[2] = t2;
        }

        public int[] getVertices() {
            return vertex;
        }

        public int[] getTexture() {
            return texture;
        }
    }
}
