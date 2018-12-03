import java.awt.Point;

/**
 * @author joey pham
 * @date 17 november 2018
 * @description this is a queue that will keep track of open points
 */
public class LinkedQueue {
	
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
		 */
		public Node ( Point p ) {
			data = p;
			next = null;
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
	 * this represents the first point in the queue
	 */
	private Node first;
	
	/**
	 * this represents the last point in the queue
	 */
	private Node last;
	
	/** 
	 * constructor
	 */
	public LinkedQueue ( ) {
		first = null;
		last = null;
	}
	
	/**
	 * check if the queue is empty
	 * @return true or false if the queue is empty
	 */
	public boolean isEmpty ( ) {
		return first == null;
	}
	
	/**
	 * adds a point to the end of the queue 
	 * @param s point to add
	 */
	public void add ( Point p ) {
		if ( isEmpty ( ) ) {
			first = new Node ( p );
			last = first;
		} else {
			Node n = new Node ( p );
			last.next = n;
			last = n;
		}
	}
	
	/**
	 * removes the first point in the queue
	 * @return the removed point
	 */
	public Point remove ( ) {
		Point rem = new Point ( );
		if ( isEmpty ( ) ) {
			System.out.println ( "Nothing to Remove" );
		} else {
			rem = first.data;
			first = first.next;
			if ( first == null ) {
				last = null;
			}
		} return rem;
	}
	
	/**
	 * displays the first point in the queue
	 * @return the first point
	 */
	public Point peek ( ) {
		Point ret = new Point ( );
		if ( isEmpty ( ) ) {
			System.out.println("Queue is Empty");
		} else {
			ret = first.data;
		}
		return ret;
	}
	
	/**
	 * calculates the size of the queue
	 * @return the size of the queue
	 */
	public int size ( ) {
		int count = 0;
		Node n = first;
		while ( n != null ) {
			count++;
			n = n.next;
		}
		return count;
	}
	
	/**
	 * displays the points' information in the stack
	 * @return the points' information
	 */
	public String toString ( ) {
		String s = "";
		Node n = first;
		while ( n != null ) {
			s = s + n.data;
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
		Node n = first;
		while ( n != null ) {
			if ( p.equals ( n.data ) ) {
				return true;
			}
			n = n.next;
		}
		return false;
	}
}