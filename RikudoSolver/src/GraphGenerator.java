import java.util.Random;

public class GraphGenerator {
	
	public static Graph complete(int n) {
		Graph graph = new Graph(n);
		for (int i = n - 1; i > - 1; i--) { //we start from the end because we use sorted list in the adjacency table
			for (int j = i -1 ; j > -1; j--) { 
				if (j != i) {
					graph.addEdge(i, j);
				}
			}
		}
		return graph;
	}
	
	public static Graph empty(int n) {
		Graph graph = new Graph(n);
		return graph;
	}
	
	public static Graph cycle(int n) {
		Graph graph = new Graph(n);
		for (int i = 0; i < n - 1; i ++) {
			graph.addEdge(i, i + 1);
		}
		graph.addEdge(n- 1, 0);
		return graph;
	}
	
	public static Graph random(int n, int edgeGoal) {
		Random random = new Random();
		Graph graph = new Graph(n);
		for (int i = 0; i < edgeGoal; i++) {
			graph.addEdge(random.nextInt(n), random.nextInt(n));
		}
		return graph;
	}
	
	public static Graph grid(int n) {
		Graph graph = new Graph(n * n);
		for (int i = 0; i < n- 1; i++) {
			for (int j = 0; j < n; j ++ ) {
				graph.addEdge(i * n + j, (i + 1)*n + j);
			}
		}
		for (int j = 0; j < n- 1; j++) {
			for (int i = 0; i < n; i ++ ) {
				graph.addEdge(i * n + j, i*n + (j + 1));
			}
		}
		return graph;
	}

	public static void main(String[] args) {
		System.out.println(complete(4));
		//System.out.println(cycle(4));
		//System.out.println(random(4, 8));
		//System.out.println(grid(2));
	}

}
