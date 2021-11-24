import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JFrame;

// Manipulation for images
public class Image2d {
	private int width; // width of the image
	private int height; // height of the image
	private int fontSize;
	private java.util.List<ColoredPolygon> coloredPolygons; // colored polygons in the image
	private java.util.List<ColoredSegment> coloredSegments; // colored segments in the image
	private java.util.List<PrintedNumber> printedNumbers;

	// Constructor that instantiates an image of a specified width and height
	public Image2d(int width, int height, int fontSize) {
		this.width = width;
		this.height = height;
		this.fontSize = fontSize;
		this.coloredPolygons = Collections.synchronizedList(new LinkedList<ColoredPolygon>());
		this.coloredSegments = Collections.synchronizedList(new LinkedList<ColoredSegment>());
		this.printedNumbers = Collections.synchronizedList(new LinkedList<PrintedNumber>());
	}

	// Constructor that instantiates an image of a specified size
	public Image2d(int size, int fontSize) {
		this.width = size;
		this.height = size;
		this.fontSize = fontSize;
		this.coloredPolygons = Collections.synchronizedList(new LinkedList<ColoredPolygon>());
		this.coloredSegments = Collections.synchronizedList(new LinkedList<ColoredSegment>());
		this.printedNumbers = Collections.synchronizedList(new LinkedList<PrintedNumber>());
	}

	// Return the width of the image
	public int getWidth() {
		return width;
	}

	// Return the height of the image
	public int getHeight() {
		return height;
	}
	
	public int getFontSize() {
		return this.fontSize;
	}

	// Return the colored polygons of the image
	public java.util.List<ColoredPolygon> getColoredPolygons() {
		return coloredPolygons;
	}

	// Return the colored segments of the image
	public java.util.List<ColoredSegment> getColoredSegments() {
		return coloredSegments;
	}
	
	// Return the printed numbers of the image
	public java.util.List<PrintedNumber> getPrintedNumbers() {
			return printedNumbers;
	}

	// Create the polygon with xcoords, ycoords and colors insideColor, boundaryColor
	public void addPolygon(int[] xcoords, int[] ycoords, Color insideColor, Color boundaryColor) {
		coloredPolygons.add(new ColoredPolygon(xcoords, ycoords, insideColor, boundaryColor));
	}
	
	public void addPolygon(ColoredPolygon poly) {
		coloredPolygons.add(poly);
	}
	
	// Create the segment from (x1,y1) to (x2,y2) with some given width and color 
	public void addSegment(int x1, int y1, int x2, int y2, int width, Color color) {
		coloredSegments.add(new ColoredSegment(x1, y1, x2, y2, width, color));
	}
	
	//Create the printed number
	public void addNumber(int x, int y, int value) {
		printedNumbers.add(new PrintedNumber(x, y, value));
	}
	
	public void addNumber(PrintedNumber numb) {
		printedNumbers.add(numb);
	}
	
	// Clear the picture
	public void clear() {
		coloredPolygons = Collections.synchronizedList(new LinkedList<ColoredPolygon>());
		coloredSegments = Collections.synchronizedList(new LinkedList<ColoredSegment>());
		printedNumbers = Collections.synchronizedList(new LinkedList<PrintedNumber>());
	}
}

//Frame for the vizualization
class Image2dViewer extends JFrame {

	private static final long serialVersionUID = -7498525833438154949L;
	static int xLocation = 0;
	public Image2d img;

	public Image2dViewer(Image2d img) {
		this.img = img;
		this.setLocation(xLocation, 0);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		add(new Image2dComponent(img));
		pack();
		setVisible(true);
		xLocation += img.getWidth();
	}
}

//Image2d component
class Image2dComponent extends JComponent {

	private static final long serialVersionUID = -7710437354239150390L;
	private Image2d img;

	public Image2dComponent(Image2d img) {
		this.img = img;
		setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// set the background color
		Dimension d = getSize();
		g2.setBackground(Color.white);
		g2.clearRect(0, 0, d.width, d.height);

		// draw the polygons
		synchronized (img.getColoredPolygons()) {
			for (ColoredPolygon coloredPolygon : img.getColoredPolygons()) {
				g2.setColor(coloredPolygon.insideColor);
				g2.fillPolygon(coloredPolygon.polygon);
				g2.setColor(coloredPolygon.boundaryColor);
				g2.drawPolygon(coloredPolygon.polygon);
			}
		}
		
		
		//draw the numbers
		Font font = new Font("Arial", Font.PLAIN, img.getFontSize());
	    g2.setFont(font);
	    FontMetrics fm = g2.getFontMetrics();
		synchronized (img.getPrintedNumbers()) {
			for (PrintedNumber number : img.getPrintedNumbers()) {
				g2.setColor(Color.BLACK);
				int x = number.x - fm.stringWidth(number.label) / 2;
			    int y = number.y - fm.getHeight() / 2 + fm.getAscent();
				g2.drawString(number.label, x, y);
			}
		}
		
		// draw the edges
		synchronized (img.getColoredSegments()) {
			for (ColoredSegment coloredSegment : img.getColoredSegments()) {
				g2.setColor(coloredSegment.color);
				g2.setStroke(new BasicStroke(coloredSegment.width));
				g2.drawLine(coloredSegment.x1, coloredSegment.y1, coloredSegment.x2, coloredSegment.y2);
			}
		}

	}
}