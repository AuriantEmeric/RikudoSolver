public class intList {
	private Node content;
	
	intList() {
		this.content = null;
	}
	
	intList(Node content) {
		this.content = content;
	}
	
	int length() {
		return Node.length(this.content);
	}
	
	@Override
	public String toString() {
		return Node.printNodes(this.content);
	}
	
	
	void insert(int v) {
		this.content = Node.insert(v, this.content);
	}
	
	boolean contains(int v) {
		return Node.contains(v, this.content);
	}
	
	void delete(int v) {
		this.content = Node.delete(v,  this.content);
	}
	
	boolean isEmpty()  {
		return (Node.isEmpty(this.content));
	}
	
	int getMax() {
		return Node.getMax(this.content);
	}
	
	int count() {
		return Node.count(this.content);
	}
	
	int countBelow(int n) {
		return Node.countBelow(this.content, n);
	}
	
	int[] toArray() {
		return Node.toArray(this.content);
	}
	
	int[] complementArray(int n) {
		return Node.complementArray(this.content, n);
	}
	
	int head() {
		return Node.head(this.content);
	}
	
	intList next() {
		return new intList(Node.next(this.content));
	}
	
	int pick() {
		return Node.pick(this.content);
	}
	
	public static void main(String[] args) {
		Node test = null;
		test = Node.insert(1, test);
		test = Node.insert(5, test);
		test = Node.insert(3, test);
		intList testList = new intList(test);
		for (int i : testList.toArray()) System.out.println(i);
		System.out.println();
		for (int i : testList.complementArray(4)) System.out.println(i);
		
	}
}