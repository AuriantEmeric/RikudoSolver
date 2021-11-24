import java.lang.reflect.Array;

public class PrintedNumber {
	int x;
	int y;
	int value;
	String label;
	
	PrintedNumber(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
		this.label = Integer.toString(value);
	}
	
	PrintedNumber(int coords[], int value) {
		if (Array.getLength(coords) != 2) {
			throw new IllegalArgumentException("Exactly two coordinates must be given");
		}
		this.x = coords[0];
		this.y = coords[1];
		this.value = value;
		this.label = Integer.toString(value);
	}

}
