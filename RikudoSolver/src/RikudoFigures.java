import java.awt.Color;
import java.lang.reflect.Array;

public class RikudoFigures {
	
	static ColoredPolygon hexagon(int xcenter, int ycenter, float radius, boolean vertical, Color insideColor) {
		
		int[] x = new int[6];
		int[] y = new int[6];
		
		double phase = 0;
		if (vertical) {
			phase = Math.PI / 6;
		}
		
		for (int i =0; i < 6; i ++) {
			x[i] = xcenter + (int) Math.ceil(Math.cos(Math.PI/3 * i + phase)*radius);
			y[i] = ycenter + (int) Math.ceil(Math.sin(Math.PI/3 * i + phase)*radius);
		}
		return new ColoredPolygon(x, y, insideColor, Color.BLACK);
	}
	
	static ColoredPolygon hexagon(int[] coords, float radius, boolean vertical, Color insideColor) {
		if (Array.getLength(coords) != 2) {
			throw new IllegalArgumentException("Exactly two coordinates must be given");
		}
		return hexagon(coords[0], coords[1], radius, vertical, insideColor);
	}
	
	static ColoredPolygon straightSquare(float xcenter, float ycenter, float size) {
		int[] x = {(int) (xcenter + (size/2)), (int) (xcenter + (size/2)), (int) (xcenter - (size/2)), (int) (xcenter - (size/2))};
		int[] y = {(int) (ycenter + (size/2)), (int) (ycenter - (size/2)), (int) (ycenter - (size/2)), (int) (ycenter + (size/2))};
		
		return new ColoredPolygon(x, y, Color.BLACK, Color.BLACK);
	}
	
	static ColoredPolygon straightSquare(float coords[], float size) {
		if (Array.getLength(coords) != 2) {
			throw new IllegalArgumentException("Exactly two coordinates must be given");
		}
		return straightSquare(coords[0], coords[1], size);
	}
	
	static ColoredPolygon slantedSquare(float xcenter, float ycenter, float size) {
		float offset = (float) (size / Math.sqrt(2));
		int[] x = {(int) xcenter, (int) (xcenter + offset), (int) xcenter, (int) (xcenter - offset)};
		int[] y = {(int) (ycenter + offset), (int) ycenter, (int) (ycenter - offset), (int) ycenter};
		
		return new ColoredPolygon(x, y, Color.BLACK, Color.BLACK);
	}
	
	static ColoredPolygon slantedSquare(float coords[], float size) {
		if (Array.getLength(coords) != 2) {
			throw new IllegalArgumentException("Exactly two coordinates must be given");
		}
		return slantedSquare(coords[0], coords[1], size);
	}
}
