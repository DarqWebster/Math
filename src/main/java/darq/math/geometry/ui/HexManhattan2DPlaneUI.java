package darq.math.geometry.ui;

import darq.math.geometry.Hexagon;
import darq.math.geometry.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * @author craig.webster
 */
public class HexManhattan2DPlaneUI extends JPanel {
	public enum Orientation {
		Y,
		X
	}
	
	private double hexRadius;
	private double hexVertex;
	private Orientation orientation;
	
	/**
	 * Centre pixel, y value, relative to which everything else is drawn.
	 */
	private int centreY;
	/**
	 * Centre pixel, x value, relative to which everything else is drawn.
	 */
	private int centreX;
	
	private Pixel cursorPixel;
	private Point cursorPoint;
	
	// For converting Pixels to Points and Points to Pixels.
	private int nStep;
	private int lStep;
	private int sStep;
	
	// For drawing background hexagons.
	private int hexRadiusPx;
	private int hexVertexPx;
	private int hexHlfSdePx;
	
	private HexManhattan2DPlaneUI() {
		centreY = 250;
		centreX = 250;
		cursorPixel = new Pixel(0, 0);
		cursorPoint = new Point(0, 0);
		
		HexMouseListener mouseListener = new HexMouseListener();
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		
		setBackground(Color.WHITE);
		setVisible(true);
	}
	
	public static HexManhattan2DPlaneUI constructWithRadius(double hexRadius, Orientation orientation) {
		HexManhattan2DPlaneUI ui = new HexManhattan2DPlaneUI();
		ui.setHexRadius(hexRadius);
		ui.setOrientation(orientation);
		return ui;
	}
	
	public static HexManhattan2DPlaneUI constructWithVertex(double hexVertex, Orientation orientation) {
		HexManhattan2DPlaneUI ui = new HexManhattan2DPlaneUI();
		ui.setHexVertex(hexVertex);
		ui.setOrientation(orientation);
		return ui;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public double getHexRadius() {
		return hexRadius;
	}

	public void setHexRadius(double hexRadius) {
		this.hexRadius = hexRadius;
		hexVertex = Hexagon.getRadiusVertFromRadiusEdge(hexRadius);
		pixelise();
	}

	public double getHexVertex() {
		return hexVertex;
	}

	public void setHexVertex(double hexVertex) {
		this.hexVertex = hexVertex;
		hexRadius = Hexagon.getRadiusEdgeFromRadiusVert(hexVertex);
		pixelise();
	}
	
	private void pixelise() {
		// Conversion.
		nStep = (int) (2 * hexRadius);
		lStep = (int) (hexVertex * 1.5);
		sStep = (int) hexRadius;
		
		// Drawing.
		hexRadiusPx = (int) hexRadius;
		hexVertexPx = (int) hexVertex;
		hexHlfSdePx = (int) (hexVertex / 2);
	}
	
	private void getViewCentreHex(Graphics2D g2d) {
		int height = getHeight();
		int width = getWidth();
		
		int viewCentreY = height / 2;
		int viewCentreX = width / 2;
		
		g2d.setColor(Color.red);
		
		int yCount;
		int xCount;
		int yOffset;
		int xOffset;
		if (orientation == Orientation.Y) {
			yCount = height / (nStep * 2);
			xCount = width / (lStep * 2);
			yOffset = (viewCentreY - centreY) % hexRadiusPx;
			xOffset = (viewCentreX - centreX) % hexVertexPx;
			
			for (int y = -yCount; y <= +yCount; y++) {
				int x1 = y > 0 ? (-xCount + y) : -xCount;
				int x2 = y < 0 ? (+xCount + y) : +xCount;
				for (int x = x1; x <= x2; x++) {
					int yPx = viewCentreY + yOffset + (y * nStep) + (x * sStep);
					int xPx = viewCentreX + xOffset + (x * lStep);
					g2d.draw(getBackgroundHexShape(yPx, xPx));
				}
			}
		} else {
			yCount = height / (lStep * 2);
			xCount = width / (nStep * 2);
			yOffset = (viewCentreY - centreY) % hexVertexPx;
			xOffset = (viewCentreX - centreX) % hexRadiusPx;
			
			for (int y = -yCount; y <= +yCount; y++) {
				int x1 = y > 0 ? (-xCount + y) : -xCount;
				int x2 = y < 0 ? (+xCount + y) : +xCount;
				for (int x = x1; x <= x2; x++) {
					int yPx = viewCentreY + yOffset + (y * lStep);
					int xPx = viewCentreX + xOffset + (x * nStep) - (y * sStep);
					g2d.draw(getBackgroundHexShape(yPx, xPx));
				}
			}
		}
	}
	
	private Pixel getPixel(double yD, double xD) {
		int y;
		int x;
		if (orientation == Orientation.Y) {
			y = (int) (centreY - (yD * nStep) + (xD * sStep));
			x = (int) (centreX + (xD * lStep));
		} else {
			y = (int) (centreY - (yD * lStep));
			x = (int) (centreX + (xD * nStep) - (yD * sStep));
		}
		return new Pixel(y, x);
	}
	
	private Point getPoint(int y, int x) {
		double yD;
		double xD;
		if (orientation == Orientation.Y) {
			xD = ((double) x - centreX) / lStep;
			yD = -(((double) y - centreY) + (xD * sStep)) / nStep;
		} else {
			yD = ((double) y - centreY) / lStep;
			xD = (((double) x - centreX) + (yD * sStep)) / nStep;
		}
		
		return new Point(yD, xD);
	}
	
	private Shape getBackgroundHexShape(int y, int x) {
		Polygon hex = new Polygon();
		
		if (orientation == Orientation.Y) {
			hex.addPoint(x - hexHlfSdePx, y - hexRadiusPx);
			hex.addPoint(x + hexHlfSdePx, y - hexRadiusPx);
			hex.addPoint(x + hexVertexPx, y);
			hex.addPoint(x + hexHlfSdePx, y + hexRadiusPx);
			hex.addPoint(x - hexHlfSdePx, y + hexRadiusPx);
			hex.addPoint(x - hexVertexPx, y);
		} else {
			hex.addPoint(x, y - hexVertexPx);
			hex.addPoint(x + hexRadiusPx, y - hexHlfSdePx);
			hex.addPoint(x + hexRadiusPx, y + hexHlfSdePx);
			hex.addPoint(x, y + hexVertexPx);
			hex.addPoint(x - hexRadiusPx, y + hexHlfSdePx);
			hex.addPoint(x - hexRadiusPx, y - hexHlfSdePx);
		}
		
		return hex;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		// Paint the axis.
//		Pixel y1 = getPixel(10, 0);
//		Pixel y2 = getPixel(-10, 0);
//		Pixel x1 = getPixel(0, 10);
//		Pixel x2 = getPixel(0, -10);
//		g2d.drawLine(y1.x, y1.y, y2.x, y2.y);
//		g2d.drawLine(x1.x, x1.y, x2.x, x2.y);
//		paintAxis(g2d);
		
		// Highlight the origin.
		g2d.fill(getBackgroundHexShape(centreY, centreX));
		
		// Paint the grid.
		getViewCentreHex(g2d);
		
		g2d.drawString(cursorPixel.toString(), 0, this.getHeight() - 20);
		g2d.drawString(cursorPoint.toString(), 0, this.getHeight() - 5);
	}
	
	private void paintAxis(Graphics2D g2d) {
		if (orientation == Orientation.Y) {
			if (centreX >= 0 && centreX < getWidth()) {
				g2d.drawLine(centreX, 0, centreX, getHeight());
			}
			
			int yS = -centreY / nStep;
			int xS = -centreX / lStep;
			int yC = getHeight() / nStep;
			int xC = getWidth() / lStep;
			
			for (int y = yS; y < yS + yC; y++) {
				for (int x = xS + (y / 2); x < xS + (y / 2) + xC; x++) {
					Pixel pixel = getPixel(y, x);
					g2d.draw(getBackgroundHexShape(pixel.y, pixel.x));
				}
			}
		} else {
			if (centreY >= 0 && centreY < getHeight()) {
				g2d.drawLine(0, centreY, getWidth(), centreY);
			}
			int yS = -centreY / lStep;
			int xS = -centreX / nStep;
			int yC = getHeight() / lStep;
			int xC = getWidth() / nStep;
			
			for (int y = yS; y < yS + yC; y++) {
				for (int x = xS + (y / 2); x < xS + (y / 2) + xC; x++) {
					Pixel pixel = getPixel(y, x);
					g2d.draw(getBackgroundHexShape(pixel.y, pixel.x));
				}
			}
		}
	}
	
	private static class Pixel {
		public final int y;
		public final int x;

		public Pixel(int y, int x) {
			this.y = y;
			this.x = x;
		}

		@Override
		public String toString() {
			return "Pixel{y=" + y + ", x=" + x + '}';
		}
	}
	
	private class HexMouseListener implements MouseListener, MouseMotionListener {
		private int dragStartY;
		private int dragStartX;
		private int dragCentreY;
		private int dragCentreX;

		//MouseListener
		
		public void mouseClicked(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			// Prepare for drag.
			dragStartY = e.getY();
			dragStartX = e.getX();
			dragCentreY = centreY;
			dragCentreX = centreX;
		}

		public void mouseReleased(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}
		
		//MouseMotionListener
		
		public void mouseDragged(MouseEvent e) {
			centreY = dragCentreY + (e.getY() - dragStartY);
			centreX = dragCentreX + (e.getX() - dragStartX);
			repaint();
		}

		public void mouseMoved(MouseEvent e) {
			cursorPixel = new Pixel(e.getY(), e.getX());
			cursorPoint = getPoint(cursorPixel.y, cursorPixel.x);
			repaint();
		}
	}
	
	private class HexComponentListener implements ComponentListener {
		public void componentResized(ComponentEvent e) {
			
		}

		public void componentMoved(ComponentEvent e) {
			
		}

		public void componentShown(ComponentEvent e) {
			
		}

		public void componentHidden(ComponentEvent e) {
			
		}
	}
}
