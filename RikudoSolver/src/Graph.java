import java.lang.reflect.Array;
import java.util.Random;

public class Graph {
	private int n;
	private intList[] adj;
	private int[] label; //partial labeling
	private int[] pathVertices; //reverse of the "label" table
	private intList[] diamonds;
	private boolean isRikudo;
	
	Graph(int n) {
		this.graphInit(n);
		this.isRikudo = false;
	}
	
	Graph(int n, int source, int target) {
		this.graphInit(n);
		this.makeRikudo(source, target);
	}
	
	private void graphInit(int n) {
		this.n = n;
		this.adj = new intList[n];
		for (int k = 0; k < n; k ++) {
			this.adj[k] = new intList();
		}
		this.isRikudo = false;
	}
	
	boolean isRikudo() {
		return this.isRikudo;
	}
	
	void makeRikudo(int source, int target) {
		this.diamonds = new intList[this.n];
		for (int k = 0; k < n; k ++) {
			this.diamonds[k] = new intList();
		}
		
		this.label = new int[this.n];
		this.label[source] = 1;
		this.label[target] = this.n;
		
		this.pathVertices = new int[this.n + 1];
		for (int i =0; i <= this.n; i++ ) {
			this.pathVertices[i] = -1;
		}
		this.pathVertices[1] = source;
		this.pathVertices[this.n] = target;
		
		this.isRikudo = true;
	}
	
	void removeRikudo() {
		this.diamonds = null;
		this.pathVertices = null;
		this.label = null;
		this.isRikudo = false;
	}
	
	void checkRikudo() {
		if (!this.isRikudo) {
			throw new IllegalStateException("This graph is not a Rikudo");
		}
	}
	
	void checkVertexInBounds(int vertex) {
		if (vertex >= this.n || vertex < 0) {
			throw new IndexOutOfBoundsException("Vertice ids go from 0 to " + (this.n - 1) + ", you tried " + vertex);
		}
	}
	
	void checkLabelInBounds(int label) {
		if (label > this.n || label <= 0) {
			throw new IndexOutOfBoundsException("Labels go from 1 to " + this.n + ", you tried " + label);
		}
	}

	void addEdge(int x, int y) {
		this.checkVertexInBounds(x);
		this.checkVertexInBounds(y);
		this.adj[x].insert(y);
		this.adj[y].insert(x);
	}
	
	boolean hasEdge(int x, int y) {
		this.checkVertexInBounds(x);
		this.checkVertexInBounds(y);
		return (this.adj[x].contains(y));
	}
	
	void removeEdge(int x, int y) {
		this.checkVertexInBounds(x);
		this.checkVertexInBounds(y);
		this.adj[x].delete(y);
		this.adj[y].delete(x);
	}
	
	void setLabel(int vertex, int label) {
		this.checkRikudo();
		this.checkVertexInBounds(vertex);
		this.checkLabelInBounds(label);
		if (this.vertexIsLabeled(vertex)) {
			throw new IllegalArgumentException("Vertex " + vertex + " already has label " + this.getLabel(vertex));
		}
		if (this.labelIsUsed(label)) {
			throw new IllegalArgumentException("Label " + label + " is already assigned to vertex " + this.getPathVertex(label));
		}
		this.label[vertex] = label;
		this.pathVertices[label] = vertex;
	}
	
	void removeLabel(int vertex) {
		this.checkRikudo();
		this.checkVertexInBounds(vertex);
		int label = this.getLabel(vertex);
		this.label[vertex] = 0;
		this.pathVertices[label] = -1;
	}
	
	boolean vertexIsLabeled(int vertex) {
		this.checkRikudo();
		this.checkVertexInBounds(vertex);
		return (this.label[vertex] != 0);
	}
	
	int getLabel(int vertex) {
		this.checkRikudo();
		this.checkVertexInBounds(vertex);
		if (!this.vertexIsLabeled(vertex)) {
			throw new RuntimeException("Vertex " + vertex + " is not labeled.");
		}
		return this.label[vertex];
	}
	
	boolean labelIsUsed(int label) {
		this.checkLabelInBounds(label);
		return (this.pathVertices[label] != -1);
	}
	
	int getPathVertex(int label) {
		this.checkLabelInBounds(label);
		if (!this.labelIsUsed(label)) {
			throw new RuntimeException("There is no vertex with label " + label);
		}
		return this.pathVertices[label];
	}
	
	void setDiamond(int v1, int v2) {
		this.checkRikudo();
		if (!this.hasEdge(v1, v2)) {
			throw new IllegalArgumentException("Can only place diamonds on existing edge");
		}
		if (this.diamonds[v1].count() > 1 || this.diamonds[v2].count() > 1) {
			throw new IllegalArgumentException("There can be at must two diamonds on each tile");
		}
		this.diamonds[v1].insert(v2);
		this.diamonds[v2].insert(v1);
	}
	
	boolean hasDiamond(int vertex) {
		this.checkRikudo();
		this.checkVertexInBounds(vertex);
		return !this.diamonds[vertex].isEmpty();
	}
	
	boolean hasDiamond(int v1, int v2) {
		this.checkRikudo();
		this.checkVertexInBounds(v1);
		this.checkVertexInBounds(v2);
		return this.diamonds[v1].contains(v2);
	}
	
	intList getDiamondsList(int vertex) {
		this.checkRikudo();
		this.checkVertexInBounds(vertex);
		return this.diamonds[vertex];
	}
	
	int[] getDiamondsArray(int vertex) {
		return this.getDiamondsList(vertex).toArray();
	}
	
	void removeDiamond(int v1, int v2) {
		this.checkRikudo();
		this.checkVertexInBounds(v1);
		this.checkVertexInBounds(v2);
		this.diamonds[v1].delete(v2);
		this.diamonds[v2].delete(v1);
	}
	
	intList neighboursList(int v) {
		this.checkVertexInBounds(v);
		return this.adj[v];
	}
	
	int[] neighboursArray(int v) {
		return this.neighboursList(v).toArray();
	}
	
	int[] notNeighboursBelow(int v, int m) {
		//vertices with number equal to or below m and having no edge with v
		return this.neighboursList(v).complementArray(m);
	}
	
	int[] notNeighboursArray(int v) {
		return this.notNeighboursBelow(v, this.n - 1);
	}
	
	@Override
	public String toString() {
		String chaine = "Number of vertices : " + this.n + "\n";
		chaine += "Edges : " + Graph.multiLineArraytoString(this.adj) + "\n";
		if (this.isRikudo) {
			chaine += "Labels : " + Graph.singleLineArraytoString(this.label) + "\n";
			chaine += "Diamonds : " + Graph.multiLineArraytoString(this.diamonds) + "\n";
		}
		return chaine;
	}
	
	int length() {
		return this.n;
	}
	
	
	static String generalArrayToString(Object[] t, String start, String end, String sep) {
		String s = start;
		int n = Array.getLength(t);
		if (n == 0) {
			return "(Empty array)";
		}
		for (int i = 0; i < n - 1; i ++) {
			s += t[i] + sep;
		}
		s += t[n - 1] + end;
		return s;
	}
	
	static String generalArrayToString(int[] t, String start, String end, String sep) {
		String s = start;
		int n = Array.getLength(t);
		if (n == 0) {
			return "(Empty array)";
		}
		for (int i = 0; i < n - 1; i ++) {
			s += t[i] + sep;
		}
		s += t[n - 1] + end;
		return s;
	}
	
	static String pathToString(int[] t) {
		return generalArrayToString(t, "", "", " -> ");
	}
	
	static String singleLineArraytoString(int[] t) {
		return generalArrayToString(t, "[", "]", ", ");
	}
	
	static String multiLineArraytoString(Object[] t) {
		return generalArrayToString(t, "[\n\t", "]", ",\n\t");
	}
	
	boolean uniqueSolutionBT() {
		this.checkRikudo();
		HamiltonianBT hamBT = new HamiltonianBT(this);
		return hamBT.uniqueSolution();
	}
	
	boolean solvableBT() {
		this.checkRikudo();
		HamiltonianBT hamBT = new HamiltonianBT(this);
		return hamBT.findHamiltonianPath();
	}
	
	boolean solvableSAT() {
		this.checkRikudo();
		HamiltonianSAT hamSAT = new HamiltonianSAT(this);
		return !(Array.getLength(hamSAT.solve()) == 0);
	}
	
	intList notLabeled() {
		intList list = new intList();
		for (int i = this.n - 1; i > 0; i--) {
			if (!this.vertexIsLabeled(i)) {
				list.insert(i);
			}
		}
		return list;
	}
	
	intList labelNotUsed() {
		intList list = new intList();
		for (int i = this.n - 1; i > 0; i--) {
			if (!this.labelIsUsed(i)) {
				list.insert(i);
			}
		}
		return list;
	}
	
	void generateRikudo(int s, int t) {
		this.makeRikudo(s, t);
		Random rand = new Random();
		intList verticesNotLabeled = this.notLabeled();
		intList labelsNotUsed = this.labelNotUsed();
		
		if (!this.solvableSAT()) {
			throw new IllegalStateException("No hamiltonian path");
		}
		
		while(!this.uniqueSolutionBT()) {
			if(Math.random() < 0.5) { //Add a label
				int vertex = verticesNotLabeled.pick(); 
				int label = labelsNotUsed.pick();
				
				this.setLabel(vertex, label);
				if (!this.solvableSAT()) this.removeLabel(vertex);
				else {
					verticesNotLabeled.delete(vertex);
					labelsNotUsed.delete(label);
				}
			}
			else { //Add a diamond
				int vertex = rand.nextInt(this.n);
				int[] diamondArray = this.getDiamondsArray(vertex);
				int[] neighbourArray = this.neighboursArray(vertex);
				int neighbour;
				
				if (diamondArray.length < 2) {
					int i  = 0;
					do {
						neighbour = neighbourArray[i];
						i++;
					} while (i < 6 && (this.hasDiamond(vertex, neighbour) || this.getDiamondsArray(neighbour).length == 2));
					if (i < 6) {
						this.setDiamond(vertex, neighbour);
					}
					if (!this.solvableSAT()) this.removeDiamond(vertex, neighbour);
				}
			}
			
			//Clean the solution 
			//Test if you can remove a label
			for (int i = 0; i < this.n; i++) {
				if (this.vertexIsLabeled(i)) {
					int memory = this.getLabel(i);
					if (memory != 1 && memory != this.n) {
						this.removeLabel(i);
						if(!this.uniqueSolutionBT()) {
							this.setLabel(i, memory);
						}
					}
				}
			}
			
			//Test if you can remove a diamond
			for (int i = 0; i < this.n; i++) {
				if (this.hasDiamond(i)) {
					int[] memory = this.getDiamondsArray(i);
					for (int v2 : memory) {
						this.removeDiamond(i, v2);
						if (!this.uniqueSolutionBT()) {
							this.setDiamond(i, v2);
						}
						
					}
				}
			}
		}
	}
}
