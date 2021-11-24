# AuriantVergnolleRikudo

INF421 (Design and Analysis of algorithms) programming project : Hamiltonian paths and rikudo solver

#### Auteurs : Emeric Auriant et Mathéo Vergnolle

## Dépendances

Ce projet utilise [sat4j](http://www.sat4j.org/) (déjà inclus normalement), ainsi que des bibliothèques fournies par Vincent Pilaud pour l'[interface graphique](https://www.lix.polytechnique.fr/~pilaud/enseignement/TP/DIX/INF421/2021/graphicalInterface.html) et le [traitement d'image binaires](https://www.lix.polytechnique.fr/~pilaud/enseignement/TP/DIX/INF421/2021/binaryImages.html).

## Description des différentes classes

- `Node` et `intList` : implémentation de listes chaînées ordonées et sans doublons.
- `Graph` : classe représentant un graphe qui peut devenir un Rikudo en appelant la méthode `makeRikudo` et en précisant la source et la cible (`target`). Contient toutes les fonctions qui permettent de manipuler les arêtes, les labels et les diamants. La fonction `generateRikudo`, appelée sur ce qui doit devenir la `source` et ce qui doit devenir la `target`, permet de générer un Rikudo minimal à solution unique dont le support est ce graphe.
- `GraphGenerator` : permet de générer des graphes cycliques, complets, vides, en forme de grille ou alétoires dont la taille est le premier argument de la méthode correspondante. Pour un graphe aléatoire, on ajoute en deuxième argument le nombre d'arête maximal qu'on veut avoir.
- `HamiltonianBT` et `HamiltonianSAT`: permet de trouver un chemin hamitlonien dans un graphe ou de résoudre un Rikudo par BackTracking et en passant par un Solveur SAT, respectivement. Les deux renvoient un tableau des sommets visités dans l'ordre du chemin, ou un tableau vide s'il n'y a pas de solution.
  - `HamiltonianSAT` résoud le problème avec la méthode `solve`
  - `HamiltonianBT` renvoie la solution avec la méthode `showHamiltonianPath`, mais contient aussi d'autres méthodes permettant notamment de tester si une solution est minimale (`isMinimal`), ou de compter le nombre de solutions (`numberOfHamiltonianPaths`)
- `RikudoFigures` et `PrintedNumbers` : éléments à afficher dans la représentation d'un Rikudo. `RikudoFigures` contient les méthodes générant les tuiles hexagonales et les diamants, `PrintedNumbers` représente un nombre et sa position
- `ImageProcessor`: permet de générer un Rikudo à partir d'une image en noir et blanc. Un `ImageProcessor` est initialisé avec le rayon que doit avoir un hexagone sur l'affichage, et un booléen représentant l'orientation des hexagones. On appelle ensuite la méthode `createGraph` sur une `binaryImage` (le modèle) et un entier (le rayon d'un hexagone sur le modèle). On génère un Rikudo sur le graphe obtenu avec `createRikudo`. On eput l'afficher avec `showPicture`, et générer sa solution avec `solveRikudo`
