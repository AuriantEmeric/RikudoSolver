import java.util.Random;

public class Node {
	private int head;
	private Node next;
	
	Node(int head, Node next) {
		this.head = head;
		this.next = next;
	}
	
	static int length(Node l) {
		int s = 0;
		for (Node cur = l; cur != null; cur = cur.next)
			s += 1;
		return s;			
	}
	
	static String printNodes(Node l) {
		if (l == null)
			return "[]";
		String chaine = "[";
		Node cur = l;
		while (cur.next != null) {
			chaine += cur.head + ", ";
			cur = cur.next;
			}
		chaine += cur.head + "]";
		return chaine;
	}
	
	static Node copy(Node the) {
		if (the == null) {
			Node nouv = null;
			return nouv;
		}
		Node curThe = the.next;
		Node nouv = new Node(the.head, null);
		Node curNouv = nouv;
		while (curThe != null) {
			curNouv.next = new Node(curThe.head, null);
			curThe = curThe.next;
			curNouv = curNouv.next;
		}
		return nouv;
	}
	
	static Node insert(int v, Node l) {
		if (l == null || l.head > v) {
			return new Node(v, l);
		} else if (v == l.head) {
			return l;
		} else {
			Node cur = l;
			while (cur.next != null && cur.next.head < v )
				cur = cur.next;
			if (cur.next == null || cur.next.head > v) {
				cur.next = new Node (v, cur.next);
			}
			return l;
		}
	}
	
	static boolean contains(int v, Node l) {
		Node cur = l;
		while (cur != null && cur.head < v )
			cur = cur.next;
		if (cur == null || cur.head > v) {
			return false;
		} else {
			return true;
		}
	}
	
	static Node delete(int v, Node l) {
		if (l == null || l.head > v) {
			return l;
		} else if (l.head == v ) {
			return l.next;
		} else {
			Node cur = l;
			while (cur.next != null && cur.next.head < v )
				cur = cur.next;
			if (cur.next != null && cur.next.head == v) {
				cur.next = cur.next.next;
			}
			return l;
		}
	}
	
	static boolean isEmpty(Node l) {
		return (l == null);
	}
	
	static void checkNotEmpty(Node l) {
		if (l == null) {
			throw new NullPointerException("Empty list");
		}
	}
	
	static int head(Node l) {
		Node.checkNotEmpty(l);
		return l.head;
	}
	
	static Node next(Node l) {
		Node.checkNotEmpty(l);
		return l.next;
	}
	
	static int getMax(Node l) {
		Node.checkNotEmpty(l);
		Node cur = l;
		while (cur.next != null) {
			cur = cur.next;
		}
		return cur.head;
	}
	
	static int count(Node l) {
		int count = 0;
		Node cur = l;
		while (cur != null) {
			cur = cur.next;
			count ++;
		}
		return count;
	}
	
	static int countBelow(Node l, int n) {
		int count = 0;
		Node cur = l;
		while (cur != null && cur.head <= n) {
			cur = cur.next;
			count ++;
		}
		return count;
	}
	
	static int pick(Node l) {
		int n = length(l);
		Random rand = new Random();
		int k = rand.nextInt(n);
		return elementAt(l, k);	
	}
	
	static int elementAt(Node l, int k) {
		if (k < 0 || l == null) {
			throw new IndexOutOfBoundsException();
		}
		if (k == 0) {
			return l.head;
		} else {
			return elementAt(l.next, k - 1);
		}

	}
	
	static int[] toArray(Node l) {
		int n = length(l);
		int[] t = new int[n];
		Node cur = l;
		for (int k = 0; k < n; k++) {
			t[k] = cur.head;
			cur = cur.next;
		}
		return t;
	}
	
	public static int[] complementArray(Node l, int n) {
		//returns the ints in [0, n] which are not in l
		int m = countBelow(l, n);;
		int[] t = new int[n+ 1 - m];
		Node cur = l;
		int i = 0;
		for (int k = 0; k < n + 1 && i < n+ 1 - m; k ++ ) {
			if (cur != null && cur.head == k) {
				cur = cur.next;
			}
			else {
				t[i] = k;
				i ++;
			}
		}
		return t;
	}
	
	
	public static void main(String[] args) {
		Node test = null;
		test = insert(0, test);
		test = insert(5, test);
		System.out.println(printNodes(test));
		test = insert(3, test);
		System.out.println(printNodes(test));
		test = insert(0, test);
		System.out.println(printNodes(test));
		System.out.println(contains(5, test));
		System.out.println(contains(0, test));
		System.out.println(contains(3, test));
		System.out.println(contains(4, test));
		test = delete(4, test);
		test = delete(3, test);
		System.out.println(printNodes(test));
		
	}

}