import java.awt.Point;

/**
 * @author joey pham
 * @date 17 November 2018
 * @description this is a stack that will keep track of open points
 */
public class LinkedStack {
	
	private static class Node {
		/**
		 * this represents the data of the node, the point
		 */
		private Point data;
		
		/**
		 * this represents the next node to refer to
		 */
		private Node next;
		
		/**
		 * constructor
		 * @param p point to set the data to
		 * @param n node to set the next to
		 */
		public Node ( Point p, Node n ) {
			data = p;
			next = n;
		}
		
		/**
		 * displays the point's information
		 * @return string of the point's data
		 */
		public String toString ( ) {
			return "(" + data.x + "," + data.y + ")";
		}
	}
	
	/**
	 * this represents the first node of the stack
	 */
	private Node top;
	
	/**
	 * constructor
	 */
	public LinkedStack ( ) {
		top = null;
	}
	
	/**
	 * checks if the stack is empty
	 * @return true or false if the stack is empty or not
	 */
	public boolean isEmpty ( ) {
		return top == null;
	}
	
	/**
	 * adds a point to the beginning of the stack
	 * @param s point to add to the stack
	 */
	public void push ( Point p ) {
		top = new Node ( p, top );
	}
	
	/**
	 * removes the first point in the stack
	 * @return the removed point
	 */
	public Point pop ( ) {
		Point rem = new Point ( );
		if ( isEmpty ( ) ) {
			System.out.println ( "Nothing to Remove" );
		} else {
			rem = top.data;
			top = top.next;
		}
		return rem;
	}
	
	/**
	 * shows the first point in the stack
	 * @return the first point
	 */
	public Point peek ( ) {
		Point ret = new Point ( );
		if ( isEmpty ( ) ) {
			System.out.println ( "Stack is Empty" );
		} else {
			ret = top.data;
		}
		return ret;
	}
	
	/**
	 * returns a string that contains all of the points' data in the stack
	 * @return string of the points in the stack
	 */
	public String toString ( ) {
		String s = "";
		Node n = top;
		while ( n != null ) {
			s = s + n.data + " ";
			n = n.next;
		}
		return s;
	}
	
	/**
	 * check if a target point exists in the queue
	 * @param p point that you're checknig for
	 * @return true or false if it exists
	 */
	public boolean contains ( Point p ) {
		Node n = top;
		while ( n != null ) {
			if ( p.equals ( n.data ) ) {
				return true;
			}
			n = n.next;
		}
		return false;
	}
	
	/**
	 * removes the given point in the stack
	 * @param rem point to be removed
	 */
	public void remove ( Point rem ) {
		if ( !isEmpty ( ) ) { // if not empty
			if ( rem.equals( top.data ) ) { // check if first is the target
				top = top.next; // replace the first point and everything else shifts
			} 
			Node n = top;
			while ( n.next != null ) { // keep going until you find a match, or you hit the end
				n = n.next;
				if ( n.next != null ) { // if you found a match
					n.next = n.next.next; // replace the current point with the next one
				}
			}
		}
	}
}
