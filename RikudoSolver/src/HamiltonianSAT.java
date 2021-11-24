import java.lang.reflect.Array;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class HamiltonianSAT {
	static final int SOLVER_TIMEOUT = 180;
	private int n;
	private Graph graph;
	private int source;
	private int target;
	private ISolver solver;
	
	HamiltonianSAT(Graph g) {
		if (!g.isRikudo()) {
			throw new IllegalStateException("This graph is not a Rikudo, please precise the source and target");
		}
		this.graph = g;
		this.n = g.length();
		this.initializeSolver();
	}
	
	HamiltonianSAT(Graph g, int s, int t) {
		this.graph = g;
		g.checkVertexInBounds(s);
		g.checkVertexInBounds(t);
		this.n = g.length();
		if (g.isRikudo()) {
			if (g.getPathVertex(1) != s || g.getPathVertex(this.n) != t) {
				throw new IllegalArgumentException("Chosen source ("+ s + ") and target (" + t + ") do not match the partial labeling"
						+ " (s = " + g.getPathVertex(1) + "; t = " + g.getPathVertex(this.n) + ")." );
			}
		} else {
			this.source = s;
			this.target = t;
		}
		this.initializeSolver();
	}
	
	private void initializeSolver() {
		this.solver = SolverFactory.newDefault();
		this.solver.setTimeout(SOLVER_TIMEOUT);
	}
	
	private int SATvar(int i, int v) {
		this.graph.checkLabelInBounds(i);
		this.graph.checkVertexInBounds(v);
		return n*i + v;
	}
	
	private int[] coupleFromSATVar(int var) {
		int i = var / n;
		int  v = var % n ;
		return new int[] {i, v};
	}
	
	boolean feedSourceTarget() {
		try {
			this.solver.addClause(new VecInt(new int[] { SATvar(1, this.source) })); //source
			this.solver.addClause(new VecInt(new int[] { SATvar(this.n, this.target) })); //target
			return true;
		} catch (ContradictionException e1) {
			return false;
		}
	}

	boolean feedBase() {
		// Feed the solver using arrays of int in Dimacs format
		//We will use n*n variables, with x_{i, v} being variable number n*i + v 
		try {			
			//each vertex appears exactly once
			for (int v = 0; v < this.n; v++ ) {
				VecInt vars = new VecInt(n - 2);
				for (int k = 1; k <= n; k ++) {
					vars.push(SATvar(k, v));
				}
				this.solver.addExactly(vars, 1);
			}
			
			//each path number appears exactly once
			for (int i = 1; i <= this.n; i++ ) {
				VecInt vars = new VecInt(n - 2);
				for (int v = 0; v < n; v ++) {
					vars.push(SATvar(i, v));
				}
				this.solver.addExactly(vars, 1);
			}
			
			//if (v, w) is not an edge, then x_{i, v} and x_{i + 1, w} can't be both true
			for (int v = this.n - 1; v > -1; v-- ) {
				int [] notNeighbours = this.graph.notNeighboursBelow(v, v -1); //second parameter avoids counting an edge twice
				for (int w : notNeighbours) {
					for (int i = 1; i < this.n; i++) {
						this.solver.addClause(new VecInt(new int[] {-SATvar(i, v), -SATvar(i + 1, w)}));
						this.solver.addClause(new VecInt(new int[] {-SATvar(i, w), -SATvar(i + 1, v)}));
					}
				}
			}
			return true;
			
		} catch (ContradictionException e1) {
			return false;
		}
		
	}
	
	boolean feedRikudo() {
		try {
			//Add the partial labelling
			for (int v = 0; v < this.n; v++) {
				if (this.graph.vertexIsLabeled(v)) {
					this.solver.addClause(new VecInt(new int[] { SATvar(this.graph.getLabel(v), v) }));
				}
			}
			
			//add the diamonds constraints : if there is a diamond between u and v, then for all (i, j) with |i - j| > 1, one can't have x_{i, u} and x_{j, v}
			for (int u = 0; u < this.n; u ++) {
				for (intList l = this.graph.getDiamondsList(u); !l.isEmpty(); l = l.next()) {
					for (int i = 1; i < this.n - 1; i ++) {
						for (int j = i + 2; j <= this.n; j++) {
							this.solver.addClause(new VecInt(new int[] {-SATvar(i, u), -SATvar(j, l.head())}));
						}
					}
				}
			}
			return true;
		} catch (ContradictionException e1) {
			return false;
		}
	}

	int[] solve() {
		//Feed the solver
		boolean success;
		if (this.graph.isRikudo()) {
			success = this.feedRikudo();
		}
		else {
			success = this.feedSourceTarget();
		}
		success = success && this.feedBase();
		
		// Solve the problem
		try {
			success = success && this.solver.isSatisfiable();
			if (success) {
				//System.out.println("Satisfiable problem!");
				int[] solution = this.solver.model();
				int solutionPath[] = new int[this.n];
				for (int k = 0; k < Array.getLength(solution); k ++) {
					if (solution[k] >= 0) {
						int[] iv = coupleFromSATVar(solution[k]);
						solutionPath[iv[0] - 1] = iv[1] ;
					}
				}
				return solutionPath;
			} else {
				//System.out.println("Unsatisfiable problem!");
				return new int[0];
			}
		} catch (TimeoutException e) {
			System.out.println("Timeout, sorry!");
			return new int[0];
		}
	}

	public static void main(String[] args) {
	}

}
