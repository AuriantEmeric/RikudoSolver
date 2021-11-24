import java.awt.Color;

public class testImage2d {
	public static void main(String[] args) {
		Image2d img = new Image2d(256, 40);
		img.addPolygon(RikudoFigures.hexagon(100, 100, 25, true, Color.YELLOW));
		img.addSegment(50, 200, 150, 100, 10, Color.GREEN);
		new Image2dViewer(img);		
	}
}
