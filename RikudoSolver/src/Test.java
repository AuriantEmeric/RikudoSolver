
public class Test {

	public static void main(String[] args) {
		
		Graph graph = GraphGenerator.grid(15);
		//graph.makeRikudo(45, 42);
		//graph.setLabel(3, 2);
		//graph.setDiamond(2, 1);
		System.out.println("Graphe généré!");
		HamiltonianSAT hamSAT = new HamiltonianSAT(graph,0, 2);
		int[] SATPath = hamSAT.solve();
		System.out.println(Graph.pathToString(SATPath));

		HamiltonianBT hamBT = new HamiltonianBT(graph, 0, 2);
		int[] BTPath = hamBT.showHamiltonianPath();
		System.out.println(Graph.pathToString(BTPath));
		//System.out.println(hamBT.findHamiltonianPath());
		//
		//
		//hamBT.numberOfHamiltonianPathsWithMaximum(1);
		//System.out.println(graph.labelIsUsed(1));
		//System.out.println(hamBT.isMinimal());
		//graph.generateRikudo(0, 8);
		//System.out.println(graph.uniqueSolutionBT());
		
		//HamiltonianBT randomRikudo = new HamiltonianBT(graph);
		//System.out.println(Graph.pathToString(randomRikudo.showHamiltonianPath()));
	}
}
