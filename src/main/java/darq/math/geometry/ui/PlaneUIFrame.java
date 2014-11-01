package darq.math.geometry.ui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author craig.webster
 */
public class PlaneUIFrame extends JFrame {
	public static void main(String[] arguments) {
		new PlaneUIFrame();
	}
	
	JPanel panel;
	
	public PlaneUIFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setPreferredSize(new Dimension(500, 500));
		
		panel = HexManhattan2DPlaneUI.constructWithRadius(15, HexManhattan2DPlaneUI.Orientation.Y);
		add(panel);
		panel.setPreferredSize(this.getPreferredSize());
		
		pack();
		setVisible(true);
	}
}
