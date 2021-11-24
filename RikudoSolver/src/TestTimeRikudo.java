
public class TestTimeRikudo {
	public static void main(String[] args) {
		
		//Test pour une grille
		for (int i = 3; i < 6; i += 2) {
			long tempsDebut = System.currentTimeMillis();
			Graph graph = GraphGenerator.grid(i);
			graph.generateRikudo(0, i * i - 1);
			long tempsFin = System.currentTimeMillis();
		    float seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("Création de Rikudo aléatoire effectuée en: "+ Float.toString(seconds) + " secondes pour un rikudo en forme de grille de taille " + i);
			
			tempsDebut = System.currentTimeMillis();
			HamiltonianSAT hamSAT = new HamiltonianSAT(graph);
			hamSAT.solve();
			tempsFin = System.currentTimeMillis();
		    seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("SAT effectué en: "+ Float.toString(seconds) + " secondes pour un rikudo en forme de grille de taille " + i);
			
			tempsDebut = System.currentTimeMillis();
			HamiltonianBT hamBT = new HamiltonianBT(graph);
			hamBT.showHamiltonianPath();
			tempsFin = System.currentTimeMillis();
		    seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("BT effectué en: "+ Float.toString(seconds) + " secondes pour un rikudo en forme une grille de taille " + i);
		    System.out.println();
		}
		
		//Test pour un cycle
		for (int i = 1; i <= 3; i++) {
			long tempsDebut = System.currentTimeMillis();
			Graph graph = GraphGenerator.cycle(15 * i);
			graph.generateRikudo(0, 1);
			long tempsFin = System.currentTimeMillis();
		    float seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("Création de Rikudo aléatoire effectuée en: "+ Float.toString(seconds) + " secondes pour un rikudo en forme de cycle de taille " + 15 * i);
			
			
			tempsDebut = System.currentTimeMillis();
			HamiltonianSAT hamSAT = new HamiltonianSAT(graph);
			hamSAT.solve();
			tempsFin = System.currentTimeMillis();
		    seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("SAT effectué en: "+ Float.toString(seconds) + " secondes pour un rikudo en forme de cycle de taille " + 15 * i);
			
			tempsDebut = System.currentTimeMillis();
			HamiltonianBT hamBT = new HamiltonianBT(graph);
			hamBT.showHamiltonianPath();
			tempsFin = System.currentTimeMillis();
		    seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("BT effectué en: "+ Float.toString(seconds) + " secondes pour un rikudo en forme de cycle de taille " + 15 * i);
		    System.out.println();
		}
		
		//Test pour un graphe complet
		for (int i = 1; i <= 3; i++) {
			long tempsDebut = System.currentTimeMillis();
			Graph graph = GraphGenerator.complete(5 * i);
			graph.generateRikudo(0, 1);
			long tempsFin = System.currentTimeMillis();
		    float seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("Création de Rikudo aléatoire effectuée en: "+ Float.toString(seconds) + " secondes pour un rikudo en forme de graphe complet de taille " + 5 * i);
			
			
			tempsDebut = System.currentTimeMillis();
			HamiltonianSAT hamSAT = new HamiltonianSAT(graph);
			hamSAT.solve();
			tempsFin = System.currentTimeMillis();
		    seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("SAT effectué en: "+ Float.toString(seconds) + " secondes pour un rikudo en forme de graphe complet de taille " + 5 * i);
			
			tempsDebut = System.currentTimeMillis();
			HamiltonianBT hamBT = new HamiltonianBT(graph);
			hamBT.showHamiltonianPath();
			tempsFin = System.currentTimeMillis();
		    seconds = (tempsFin - tempsDebut) / 1000F;
		    System.out.println("BT effectué en: "+ Float.toString(seconds) + " secondes pour un  rikudo en forme de graphe complet de taille " + 5 * i);
		    System.out.println();
		}
		
	}
}
