import geometry.Vector3d;
import model.Model;
import model.Model.Face;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Andrey Kokorev
 *         Created on 25.01.2015.
 */
public class ViewPanel extends JPanel
{
    private BufferedImage bufferedImage, textureImg;
    private Dimension size;
    private Vector3d lightDirection = new Vector3d(0, 0, -1);
    private double[][] zbuffer;

    @Override
    public void paint(Graphics g)
    {
        Dimension size = this.getSize();
        g.setColor(Color.ORANGE);
        g.clearRect(0, 0, size.width, size.height);
        if(bufferedImage != null)
        {
            //flip vertically
            g.drawImage(bufferedImage, 0, 0, size.width, size.height, 0, size.height,size.width, 0, null);
        }
    }

    public void drawModel(Model model)
    {
        if(bufferedImage == null)
        {
            size = this.getSize();

            bufferedImage = new BufferedImage(size.width + 1, size.height + 1, BufferedImage.TYPE_INT_ARGB);
            textureImg = model.getTextureImg();
            zbuffer = new double[size.width + 1][size.height + 1];
            for(int i = 0; i <= size.width; i++)
            {
                Arrays.fill(zbuffer[i], -Double.MAX_VALUE);
            }
        }

        //test(bufferedImage);

        Map<Integer, Vector3d> modelVertices = model.getVertices();
        Map<Integer, Vector3d> modelTVertices = model.getTexture();

        for(Face f : model.getFaces())
        {
            int[] vertices = f.getVertices();
            int[] tVertices = f.getTexture();
            Vector3d v0 = modelVertices.get(vertices[0]);
            Vector3d v1 = modelVertices.get(vertices[1]);
            Vector3d v2 = modelVertices.get(vertices[2]);

            Vector3d t0 = modelTVertices.get(tVertices[0]);
            Vector3d t1 = modelTVertices.get(tVertices[1]);
            Vector3d t2 = modelTVertices.get(tVertices[2]);

            Vector3d p0 = new Vector3d(
                    (int) ((v0.getX() + 1) * size.getWidth() / 2),
                    (int) ((v0.getY() + 1) * size.getHeight() / 2),
                    (int) v0.getZ()
            );
            Vector3d p1 = new Vector3d(
                    (int) ((v1.getX() + 1) * size.getWidth() / 2),
                    (int) ((v1.getY() + 1) * size.getHeight() / 2),
                    (int) v1.getZ()
            );
            Vector3d p2 = new Vector3d(
                    (int) ((v2.getX() + 1) * size.getWidth() / 2),
                    (int) ((v2.getY() + 1) * size.getHeight() / 2),
                    (int) v2.getZ()
            );


            Vector3d n = new Vector3d(v0, v2).product(new Vector3d(v0, v1));
            n.normalize();
            double intensity = n.dot(lightDirection);
            
            if(intensity > 0)
            {
                GUtils.drawTriangle(
                        bufferedImage, textureImg,
                        p0, p1, p2,
                        t0, t1, t2,
                        intensity, zbuffer
                );
            }

        }

        this.repaint();
    }

    private void test(BufferedImage img)
    {
        Vector3d c  = new Vector3d(100, 100, 0);
        Vector3d e  = new Vector3d(0, 100, 0);
        Vector3d w  = new Vector3d(200, 100, 0);
        Vector3d n  = new Vector3d(100, 200, 0);
        Vector3d s  = new Vector3d(100, 0, 0);
        Vector3d ne = new Vector3d(50, 150, 0);
        Vector3d nw = new Vector3d(150, 150, 0);
        Vector3d se = new Vector3d(50, 50, 0);
        Vector3d sw = new Vector3d(150, 50, 0);

        GUtils.drawLine(img, c, ne, GUtils.RED);
        GUtils.drawLine(img, c, nw, GUtils.RED);
        GUtils.drawLine(img, c, se, GUtils.RED);
        GUtils.drawLine(img, c, sw, GUtils.RED);
        GUtils.drawLine(img, c, e, GUtils.GREEN);
        GUtils.drawLine(img, c, w, GUtils.GREEN);
        GUtils.drawLine(img, c, n, GUtils.BLUE);
        GUtils.drawLine(img, c, s, GUtils.BLUE);

        int cx = 500, cy = 500, R = 50;
        Vector3d c1 = new Vector3d(cx, cy, 0);
        double alpha = 0, da = Math.PI / 32;
        for(int i = 0; i < 64; i++)
        {
            Vector3d p = new Vector3d((int) (cx + R * Math.sin(alpha)), (int) (cx + R * Math.cos(alpha)), 0);
            GUtils.drawLine(img, c1, p, GUtils.GREEN);
            alpha += da;
        }


        GUtils.drawLine(img, new Vector3d(0, 400, 0), new Vector3d(10, 400, 0), GUtils.BLACK);

        Vector3d t0 = new Vector3d(300, 120, 0);
        Vector3d t1 = new Vector3d(350, 60, 0);
        Vector3d t2 = new Vector3d(280, 280, 0);

        //GUtils.drawTriangle(img, t0, t1, t2, GUtils.RED, zbuffer);

        Vector3d q0 = new Vector3d(189, 477, 0);
        Vector3d q1 = new Vector3d(609, 413, 0);
        Vector3d q2 = new Vector3d(483, 137, 0);

//        GUtils.drawTriangle(img, q0, q1, q2, GUtils.GREEN, zbuffer);

        Vector3d z0 = new Vector3d(0, 150, 0);
        Vector3d z1 = new Vector3d(0, 0, 0);
        Vector3d z2 = new Vector3d(150, 0, 0);

//        GUtils.drawTriangle(img, z2, z0, z2, GUtils.BLACK, zbuffer);
    }
}
