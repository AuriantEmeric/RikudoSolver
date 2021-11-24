
public class TestTimeHamiltonianPath {
	public static void main(String[] args) {
		//Test pour une grille
		for (int i = 1; i < 4; i++) {
			Graph graph = GraphGenerator.grid(2 * i);
			
			long tempsDebut = System.currentTimeMillis();
			HamiltonianSAT hamSAT = new HamiltonianSAT(graph,0, 1);
			hamSAT.solve();
			long tempsFin = System.currentTimeMillis();
		    float seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("SAT effectué en: "+ Float.toString(seconds) + " secondes pour une grille de taille " + 2 * i);
			
			tempsDebut = System.currentTimeMillis();
			HamiltonianBT hamBT = new HamiltonianBT(graph, 0, 1);
			hamBT.showHamiltonianPath();
			tempsFin = System.currentTimeMillis();
		    seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("BT effectué en: "+ Float.toString(seconds) + " secondes pour une grille de taille " + 2 * i);
		    System.out.println();
		}
		
		//Test pour un cycle
		for (int i = 1; i <= 3; i++) {
			Graph graph = GraphGenerator.cycle(10 * i);
			
			long tempsDebut = System.currentTimeMillis();
			HamiltonianSAT hamSAT = new HamiltonianSAT(graph,0, 1);
			hamSAT.solve();
			long tempsFin = System.currentTimeMillis();
		    float seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("SAT effectué en: "+ Float.toString(seconds) + " secondes pour un cycle de taille " + 10 * i);
			
			tempsDebut = System.currentTimeMillis();
			HamiltonianBT hamBT = new HamiltonianBT(graph, 0, 1);
			hamBT.showHamiltonianPath();
			tempsFin = System.currentTimeMillis();
		    seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("BT effectué en: "+ Float.toString(seconds) + " secondes pour un cycle de taille " + 10 * i);
		    System.out.println();
		}
		
		//Test pour un graphe complet
		for (int i = 1; i <= 3; i++) {
			Graph graph = GraphGenerator.complete(4 * i);
			
			long tempsDebut = System.currentTimeMillis();
			HamiltonianSAT hamSAT = new HamiltonianSAT(graph,0, 1);
			hamSAT.solve();
			long tempsFin = System.currentTimeMillis();
		    float seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("SAT effectué en: "+ Float.toString(seconds) + " secondes pour un graphe complet de taille " + 4 * i);
			
			tempsDebut = System.currentTimeMillis();
			HamiltonianBT hamBT = new HamiltonianBT(graph, 0, 1);
			hamBT.showHamiltonianPath();
			tempsFin = System.currentTimeMillis();
		    seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("BT effectué en: "+ Float.toString(seconds) + " secondes pour un graphe complet de taille " + 4 * i);
		    System.out.println();
		}
		
	}
}
