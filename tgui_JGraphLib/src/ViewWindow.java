
import model.Model;
import model.ModelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * @author Andrey Kokorev
 *         Created on 25.01.2015.
 */
public class ViewWindow extends JFrame
{
    public static final Dimension size = new Dimension(800, 600);

    public ViewWindow()
    {
        super("View window");

        this.setLayout(new BorderLayout());
        final ViewPanel viewPanel = new ViewPanel();
        this.add(viewPanel, BorderLayout.CENTER);
        JButton button = new JButton("Draw");
        button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
	        	try {
	                Model m = ModelLoader.fromFile(
	                    "models/african_head.obj",
	                    "models/african_head_diffuse.tga"
	//                    Paths.get("models", "sqr_diffuse.tga")
	                );
	                viewPanel.drawModel(m);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
			}
        });
        this.add(button, BorderLayout.WEST);

        this.setMinimumSize(size);
        this.setMaximumSize(size);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
