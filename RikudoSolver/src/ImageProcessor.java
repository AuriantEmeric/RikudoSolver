import java.awt.Color;
import java.lang.reflect.Array;
import java.util.Random;

public class ImageProcessor {
	
	private int n; //number of vertices in the graph
	private Graph graph;
	
	private int[][] grid; //base grid of the puzzle
	private int gridWidth;
	private int gridHeight;
	private int[] posInGrid; //position of each vertex on the grid
	
	private boolean vertical; //whether we draw vertical or horizontal hexagons 
	
	private Image2d display; //display of the puzzle
	private int displayWidth;
	private int displayHeight;
	private int displayRadius; //distance from the center of an hexagon to one of its vertex on the display
	private int displayDistance; //distance from the center of an hexagon to one of its sides on the display
	
	private BinaryImage pattern; //B&W picture giving the shape of the puzzle, see the display for explanations on distance and radius
	private int patternWidth; 
	private int patternHeight;
	private int patternRadius;
	private int patternDistance;
	
	ImageProcessor(int displayRadius, boolean vertical) {
		this.displayRadius = displayRadius;
		this.displayDistance = (int) Math.round(Math.sqrt(3) * displayRadius / 2);
		this.vertical = vertical;
	}
	
	void createGraph(BinaryImage pattern, int patternRadius) {
		this.pattern = pattern;
		this.patternHeight = pattern.getHeight();
		this.patternWidth = pattern.getWidth();
		this.patternRadius = patternRadius;
		this.patternDistance = (int) Math.round(Math.sqrt(3) * patternRadius / 2);
		
		this.displayHeight = Math.round(this.patternHeight * this.displayRadius/this.patternRadius);
		this.displayWidth = Math.round( this.patternWidth * this.displayRadius/this.patternRadius);
		
		if (this.vertical) {
			this.gridWidth = (int) Math.ceil(2.0*this.displayWidth/(3.0*this.displayRadius));
			this.gridHeight = (int) Math.ceil(this.displayHeight/(2.0*this.displayDistance));
		}
		else {
			this.gridHeight = (int) Math.ceil(2.0*this.displayHeight/(3.0*this.displayRadius));
			this.gridWidth = (int) Math.ceil(this.displayWidth/(2.0*this.displayDistance));
		}
		
		
		this.grid = new int[this.gridHeight][this.gridWidth];		
		
		this.display = new Image2d(this.displayWidth , this.displayHeight,  this.displayDistance);
		
		
		this.n = 0;
		//Keep only the hexagons that correspond to a black pixel
		for (int i = 0; i < this.gridHeight; i++) {
			for (int j = 0; j < this.gridWidth; j++) {
				int patternCoords[] = this.patternPos(i, j);
				if (patternCoords[0] < this.patternWidth && patternCoords[1] < this.patternHeight &&  this.pattern.isBlack(patternCoords)) {
					this.grid[i][j] = 0;
					this.n ++;
				}
				else {
					this.grid[i][j] = -1;
				}
			}
		}
		
		//Eliminate vertices with less than 2 edges
		for (int i = 0; i < this.gridHeight; i++) {
			for (int j = 0; j < this.gridWidth; j++) {
				if (this.grid[i][j] == 0) {
					this.removeIfSingle(i, j);
				}
			}
		}	
		
		this.graph = new Graph(n);
		this.posInGrid = new int[n];
		
		int c = 0;
		// give a number to each vertex
		for (int i = 0; i < this.gridHeight; i++) {
			for (int j = 0; j < this.gridWidth; j++) {
				if (this.grid[i][j] == 0) {
					this.grid[i][j] = c;
					int coords[] = {i, j};
					this.posInGrid[c] = this.gridCoordsToInt(coords);
					c++;
				}
			}
		}
		
		assert (c == this.n);
		
		//add edges to the graph
		for (int i = 0; i < this.gridHeight; i++) {
			for (int j = 0; j < this.gridWidth; j++) {
				if (this.grid[i][j] != -1) {
					intList l = this.neighbours(i, j);
					for (intList cur = l; !cur.isEmpty(); cur = cur.next()) {
						int coords[] = this.intToGridCoords(cur.head());
						this.graph.addEdge(this.grid[i][j], this.grid[coords[0]][coords[1]]);
					}
				}
			}
		}	
	}
	
	private int gridCoordsToInt(int[] coords) {
		if (Array.getLength(coords) != 2) {
			throw new IllegalArgumentException("Exactly two coordinates must be given");
		}
		return coords[0] * this.gridWidth + coords[1];
	}
	
	private int[] intToGridCoords(int linear) {
		if (linear < 0 || linear >= this.gridWidth * this.gridHeight) {
			throw new IndexOutOfBoundsException();
		}
		int coords[] = new int[2];
		coords[0] = linear/this.gridWidth;
		coords[1] = linear % this.gridWidth;
		return coords;
	}
	
	private int[] displayPos(int i, int j) {
		int coord[] = new int[2];
		if (this.vertical) {
			coord[0] = (1 + j%2 + 2*i) * this.displayDistance;
			coord[1] = (int) ((1.5*j + 1) * this.displayRadius);
		} else {
			coord[0] = (int) ((1.5*i + 1) * this.displayRadius);
			coord[1] = (1 + i%2 + 2*j) * this.displayDistance;
		}
		return coord;
	}
	
	private int[] displayPos(int linear) {
		int coords[] = intToGridCoords(linear);
		return displayPos(coords[0], coords[1]);
	}
	
	private int[] patternPos(int i, int j) {
		int coord[] = new int[2];
		if (this.vertical) {
			coord[0] = (1 + j%2 + 2*i) * this.patternDistance;
			coord[1] = (int) ((1.5*j + 1) * this.patternRadius);
		} else {
			coord[0] = (int) ((1.5*i + 1) * this.patternRadius);
			coord[1] = (1 + i%2 + 2*j) * this.patternDistance;
		}
		return coord;
	}
	
	private boolean inGridBounds(int i, int j) {
		return (0 <= i && i < this.gridHeight && 0<= j && j < this.gridWidth);
	}
	
	private boolean inGraph(int i, int j) {
		return inGridBounds(i, j) && this.grid[i][j] > -1;
	}
	
	private intList neighbours(int i, int j) {
		intList l = new intList();
		int candidates[][] = {{i+1, j}, {i - 1, j}, {i, j - 1}, {i, j + 1}, {i + 1, j - 1}, {i + 1, j + 1}};
		if (this.vertical && j%2 == 0) {
			candidates[4][0] = i - 1;
			candidates[5][0] = i - 1;
		} else if (!this.vertical && i%2 == 1) {
			candidates[4][0] = i - 1;
			candidates[4][1] = j + 1;
		} else if (!this.vertical && i%2 == 0) {
			candidates[5][0] = i - 1;
			candidates[5][1] = j - 1;
		}
		for (int v[] : candidates) {
			if (inGraph(v[0], v[1])) {
				l.insert(this.gridCoordsToInt(v));
			}
		}
		return l;
	}
	
	private void removeIfSingle(int i, int j) {
		if (this.grid[i][j] == -1) {
			throw new IllegalArgumentException("Can only be called on grid point that belongs to the graph");
		}
		intList l = this.neighbours(i, j);
		int leng = l.length();
		if (leng <= 1 ) {
			this.grid[i][j] = -1;
			this.n--;
		}
		if (leng == 1) {
			this.removeIfSingle(l.head());
		}
	}
	
	private void removeIfSingle(int linear) {
		int[] coords = this.intToGridCoords(linear);
		this.removeIfSingle(coords[0], coords[1]);
	}
	
	void createRikudo() {
		if (this.graph == null) {
			throw new NullPointerException("Create graph first.");
		}
		Random rand = new Random();
		int s = rand.nextInt(this.n), t;
		do {
			t = rand.nextInt(this.n);
			} while (t == s);
		createRikudo(s, t);
	}
	
	void createRikudo(int s, int t) {
		if (this.graph == null) {
			throw new NullPointerException("Create graph first.");
		}
		
		this.graph.generateRikudo(s, t);
		
		for (int i = 0; i < this.n; i++) {
			if (this.graph.vertexIsLabeled(i)) {
				this.drawNumberedTile(this.posInGrid[i], this.graph.getLabel(i));
			}
			else {
				this.drawBlankTile(this.posInGrid[i]);
			}
		}
		
		for (int i =this.n - 1; i > -1; i--) {
			intList l = this.graph.getDiamondsList(i);
			for (intList cur = l; !cur.isEmpty() && cur.head() < i; cur = cur.next()) {
				this.drawDiamond(this.posInGrid[i], this.posInGrid[cur.head()]);
			}
		}
	}

	private void drawNumberedTile(int i, int label) {
		this.display.addPolygon(RikudoFigures.hexagon(this.displayPos(i), this.displayRadius, this.vertical, Color.YELLOW));
		if (label == 1 || label == this.n) {
			this.display.addPolygon(RikudoFigures.hexagon(this.displayPos(i), this.displayRadius*0.8f, this.vertical, Color.YELLOW));
		}
		this.display.addNumber(new PrintedNumber(this.displayPos(i), label));
		
	}
	
	private void drawBlankTile(int i) {
		this.display.addPolygon(RikudoFigures.hexagon(this.displayPos(i), this.displayRadius, this.vertical, Color.WHITE));
		
	}

	
	private void drawDiamond(int i, int j) {
		int icoords[] = this.displayPos(i), jcoords[] = this.displayPos(j);
		float x = (icoords[0] + jcoords[0]) /2.0f;
		float y = (icoords[1] + jcoords[1]) /2.0f;
		if (icoords[0] == jcoords[0] || icoords[1] == jcoords[1]) {
			this.display.addPolygon(RikudoFigures.slantedSquare(x, y, this.displayDistance / 3));
		} else {
			this.display.addPolygon(RikudoFigures.straightSquare(x, y, this.displayDistance / 3));
		}
	}
	
	void solveRikudo() {
		if (this.graph == null) {
			throw new NullPointerException("Create graph first.");
		}
		this.graph.checkRikudo();
		HamiltonianSAT ham = new HamiltonianSAT(this.graph, this.graph.getPathVertex(1), this.graph.getPathVertex(this.n));
		//HamiltonianBT ham = new HamiltonianBT(graph, 0, 8);
		int[] SATPath = ham.solve();
		for (int i = 0; i < this.n; i ++) {
			int v = SATPath[i];
			if (!this.graph.vertexIsLabeled(v)) {
				this.display.addNumber(new PrintedNumber(this.displayPos(this.posInGrid[v]), i + 1));
			}
		}
	}

	void showPicture() {
		if (this.display == null) {
			throw new NullPointerException("There is no representation to display.");
		}
		new Image2dViewer(this.display);
	}

	public static void main(String[] args) {
		ImageProcessor IP = new ImageProcessor(20, true);
		IP.createGraph(new BinaryImage("binaryImages/christmasTree.png"), 18);
		IP.createRikudo();
		IP.solveRikudo();
		IP.showPicture();
		/*
		System.out.println(IP.graph);
		IP.createGraph(new BinaryImage("binaryImages/christmasTree.png"), 25);
		IP.createRikudo();
		IP.solveRikudo();
		IP.showPicture();
		System.out.println(IP.graph);
		*/
	}

}
