
public class HamiltonianBT {
	final Graph graph;
	private int n;
	private int[] markedCell; //preceding cell in the path, -2 := not visited, -1 := source
	private int source;
	private int target;
	
	HamiltonianBT(Graph g) {
		if (!g.isRikudo()) {
			throw new IllegalStateException("This graph is not a Rikudo, please precise the source and target");
		}
		this.graph = g;
		this.n = g.length();
		this.initializeMarkedCell();
		
		this.source = g.getPathVertex(1);
		this.target = g.getPathVertex(this.n);
		
	}
	
	HamiltonianBT(Graph g, int s, int t) {
		this.graph = g;
		g.checkVertexInBounds(s);
		g.checkVertexInBounds(t);
		this.n = g.length();
		if (g.isRikudo() && (g.getPathVertex(1) != s || g.getPathVertex(this.n) != t)) {
			throw new IllegalArgumentException("Chosen source ("+ s + ") and target (" + t + ") do not match the partial labeling"
					+ " (s = " + g.getPathVertex(1) + "; t = " + g.getPathVertex(this.n) + ")." );
		}
		this.source = s;
		this.target = t;
		this.initializeMarkedCell();
	}

	void initializeMarkedCell() {
		this.markedCell = new int[this.n];
		for (int i = 0; i < this.n; i++)
			this.markedCell[i] = -2;
	}
	
	boolean checkDiamonds(int previous, int actual, int neighbour) {
		int[] diamonds = this.graph.getDiamondsArray(actual);
		int diamondsNumber = diamonds.length;
		
		if (diamondsNumber == 0) {
			return true;
		}
		else if (diamondsNumber == 1) {
			return (diamonds[0] == previous || diamonds[0] == neighbour);
		}
		else {
			return((diamonds[0] == previous && diamonds[1] == neighbour) || (diamonds[1] == previous && diamonds[0] == neighbour));
		}
	}
	
	boolean checkLabel(int neighbour, int visited) {
		return !this.graph.vertexIsLabeled(neighbour) || this.graph.getLabel(neighbour) == visited + 1;
	}
	
	//Check if there is a solution
	boolean findHamiltonianPath(int actual, int previous, int visited){
		
		//System.out.println(actual + " " + previous + " " + visited + " " + this.n + " " + this.target);
		this.markedCell[actual] = previous;
		
		if(actual == this.target && visited == this.n)
			return true;
	
        for (int neighbour : this.graph.neighboursArray(actual)) {
			if (this.markedCell[neighbour] == -2 
					&& (!this.graph.isRikudo() ||(this.checkDiamonds(previous, actual, neighbour) && this.checkLabel(neighbour, visited)))
					&& findHamiltonianPath(neighbour, actual, visited+1))
				//If the graph is a rikudo we have to check the vertices and the labels
				return true;
			}
		this.markedCell[actual] = -2;
        return false;
	}	
	
	boolean findHamiltonianPath() {
		this.initializeMarkedCell();
		return findHamiltonianPath(this.source, -1, 1);
	}
	
	//Return a path if there is a solution
	int[] showHamiltonianPath() {
		if(findHamiltonianPath()) {;
			int actual = target;
			int[] path = new int[this.n];
			for (int i = graph.length() - 1; i  > 0; i--) {
				path[i] = actual;
				actual = markedCell[actual];
			}
			return path;
		}
		return new int[0];
	}
	
	int numberOfHamiltonianPaths(int actual, int previous, int visited) {
		int nb = 0;
		
		if(actual == this.target && visited == this.n) {
			return 1;
		}
    	 for (int neighbour : this.graph.neighboursArray(actual)) {
			if (this.markedCell[neighbour] == -2 
					&& (!this.graph.isRikudo() || (this.checkDiamonds(previous, actual, neighbour) && this.checkLabel(neighbour, visited)))) {
				this.markedCell[neighbour] = actual;
        		nb += numberOfHamiltonianPaths(neighbour, actual, visited + 1);
            	this.markedCell[neighbour] = -2;
			}
        }
        return nb;
	}
	
	int numberOfHamiltonianPaths() {
		this.initializeMarkedCell();
		this.markedCell[this.source] = -1;
		return numberOfHamiltonianPaths(this.source, -1, 1);
	}
	
	//Return a path if there are more than numberMaximum solution
	int numberOfHamiltonianPathsWithMaximum(int actual, int previous, int visited, int numberMaximum) {
		int nb = 0;
		
		if(actual == this.target && visited == this.n) {
			return 1;
		}
    	 for (int neighbour : this.graph.neighboursArray(actual)) {
 			if (nb > numberMaximum) 
				return nb;
 			
    		if (this.markedCell[neighbour] == -2 
					&& (!this.graph.isRikudo() || (this.checkDiamonds(previous, actual, neighbour) && this.checkLabel(neighbour, visited)))) {
				this.markedCell[neighbour] = actual;
        		nb += numberOfHamiltonianPaths(neighbour, actual, visited + 1);
            	this.markedCell[neighbour] = -2;
			}
        }
        return nb;
	}

	//Is there more than k paths ? 
	boolean moreThanKPaths(int k) {
		this.initializeMarkedCell();
		this.markedCell[this.source] = -1;
		int numberWithMaximum = numberOfHamiltonianPathsWithMaximum(this.source, -1, 1, k + 1);
		
		if (numberWithMaximum > k) {
			//System.out.println("There are stricly more than " + k + " Hamiltonians paths");
			return true;
		}
		
		//System.out.println("There are less than " + k + " Hamiltonians paths");
		return false;
			
	}
	
	boolean uniqueSolution() {
		return (this.moreThanKPaths(0) == true && this.moreThanKPaths(1) == false);
	}
	
	boolean isMinimal() {
		
		if (!this.findHamiltonianPath() || !this.uniqueSolution()) {
			System.out.println("No solution or multiple solutions");
			return false;
		}
		
		//Test if you can remove a label
		for (int i = 0; i < this.n; i++) {
			if (this.graph.vertexIsLabeled(i)) {
				int memory = this.graph.getLabel(i);
				if (memory != 1 && memory != this.n) {
					this.graph.removeLabel(i);
					if(this.uniqueSolution()) {
						System.out.println("Not minimal, can remove the label of vertex " + i);
						return false;
					}
					this.graph.setLabel(i, memory);
				}
			}
		}
		
		//Test if you can remove a diamond
		for (int i = 0; i < this.n; i++) {
			if (this.graph.hasDiamond(i)) {
				int[] memory = this.graph.getDiamondsArray(i);
				for (int v2 : memory) {
					this.graph.removeDiamond(i, v2);
					if (this.uniqueSolution()) {
						System.out.println("Not minimal, can remove the diamond between vertex" + i + " and vertex " + v2);
						return false;
					}
					this.graph.setDiamond(i, v2);
				}
			}
		}
		System.out.println("It is minimal");
		return true;
	}
	
}
