/**
 * @author appsm
 * @date 17 November 2018
 * @description the user is given a set of mazes to choose from and may choose to solve it themself or watch the program solve them
 */

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MazeSolver {

	public static void main ( String [ ] args ) {
		String mazeFile = getMazeFile ( ); // get maze number
		while ( ! ( mazeFile.equals ( "maze0.txt" ) ) ) { // while user hasn't quit
			char [ ] [ ] maze = readFile ( mazeFile ); // assign maze to array
			String method = getSearchType ( ); // get search method, self/stack/queue
			search ( method, maze ); // search through the maze with the correspondong method
			mazeFile = getMazeFile ( ); // repeat and get maze number/quit
		}
		System.out.print ( "\n" + "Goodbye!" );
	}
	
	/**
	 * prompts the user for which difficulty they'd like to play at and returns the corresponding .txt file
	 * @return .txt file that the user wants to play
	 */
	public static String getMazeFile ( ) { 
		int mazeCount = 4; // change this if you add more mazes 
		System.out.print ( "Maze Runner" );
		System.out.print ( "\n" + "0. Quit" );
		for ( int i = 1; i < mazeCount + 1; i++ ) {
			System.out.print ( "\n" + i + ". Level " + i );
		}
		System.out.print ( "\n" + "Enter an option: " ); // prompt
		String mazeNumber = Integer.toString ( CheckInput.getIntRange ( 0, mazeCount ) ); // gets a num 1-4 and converts to string
		return "maze" + mazeNumber + ".txt"; 
		// return file name of wanted maze, ie "maze" + "1" + ".txt" = "maze1.txt"
	} 
	
	/**
	 * reads in the .txt file and returns the maze within a 2d array
	 * @param mazeFile .txt file of the maze the user wants to play
	 * @return maze a 2d array with the maze they asked for
	 */
	public static char [ ] [ ] readFile ( String mazeFile ) {
		try {
			Scanner read = new Scanner ( new File ( mazeFile ) ); // read in file
			// the .txt file's first two inputs are the height and width of the maze, check the file
			int height = Integer.parseInt ( read.next ( ) ); // convert string to int
			int width = Integer.parseInt ( read.next ( ) ); 
			char [ ] [ ] maze = new char [ height ] [ width ]; // create a 2d array with the size of the maze
			String line = read.nextLine ( ); // skip a line because it doesn't read the first line of the maze for some reason
			
			// fill the array with the contents of the array
			for ( int y = 0; y < height; y++ ) { // read in line by line
				line = read.nextLine ( ); // read in the next line of the maze
				for ( int x = 0; x < width; x++ ) { // iterate through all the values in the line one at a time
					maze [ y ] [ x ] = line.charAt ( x ); // add the maze values to the corresponding point in the array
				}
			}
			
			return maze; // return the aray with the maze inside
		} catch ( FileNotFoundException fnf ) { // should never get here though cause of the getmaze function checks already
			System.out.print ( "That maze does not exist." );
			return new char [ 0 ] [ 0 ]; // need a return here or error
		}
	}
	
	/** 
	 * prompts the user for what first search they'd like 
	 * @return the first search the user requested
	 */
	public static String getSearchType ( ) {
		System.out.print ( "\n" + "Search Menu " ); // prompt
		System.out.print ( "\n" + "1. Self Search: " ); // prompt
		System.out.print ( "\n" + "2. Depth First Search: " ); // prompt
		System.out.print ( "\n" + "3. Breadth First Search: " ); // prompt
		System.out.print ( "\n" + "Enter a search option: " ); // prompt
		int search = CheckInput.getIntRange ( 1, 3 ); // get search option
		if ( search == 1 ) { // self expl
			return "self";
		} else if ( search == 2 ) {
			return "stack";
		} else {
			return "queue";
		}
	}
	
	/**
	 * displays the maze with the starting, finishing, and all steps taken
	 * @param maze map to print out
	 */
	public static void displayMaze ( char [ ] [ ] maze ) {
		int height = maze.length; // gets height
		int width = maze [ 0 ].length; // width
		
		System.out.print ( "\n" );
		
		// print out contents
		for ( int y = 0; y < height; y++ ) { 
			for ( int x = 0; x < width; x++ ) {
				System.out.print ( maze [ y ] [ x ] );
			}
			System.out.print ( "\n" );
		}
		System.out.print ( "\n" );
	}
	
	/**
	 * gets the starting point of the maze
	 * @param maze maze to find the starting point in
	 * @return the starting point in the maze
	 */
	public static Point getStarting ( char [ ] [ ] maze ) {
		Point startingPoint = new Point ( );
		int height = maze.length;
		int width = maze [ 0 ].length;
		
		// iterate through the array looking for 'S'
		for ( int y = 0; y < height; y++ ) {
			for ( int x = 0; x < width; x++ ) {
				if ( maze [ y ] [ x ] == 'S' ) { // if 'S' is found, that's the starting point
					startingPoint = new Point ( x, y ); // current coordinate is 'S' location
				}
			}
		}
		return startingPoint; // return location of the starting point
	}
	
	/**
	 * leaves a trail of '·'s behind to see the path taken
	 * @param maze	  maze you're traversing
	 * @param stepped if this is before finishing the step
	 * @param x		  x-coordinate
	 * @param y		  y-coordinate
	 */
	public static void leaveTrail ( char [ ] [ ] maze, boolean stepped, int x, int y ) {
		if ( maze [ y ] [ x ] != 'F' && maze [ y ] [ x ] != 'S' ) { // don't leave a footprint if you're on the S/F
			if ( stepped ) { // assigns x to the user's current position, meant for self search
				maze [ y ] [ x ] = 'x';
			} else { // assigns '·' to trail
				maze [ y ] [ x ] = '·';
			}
		}
	}
	
	/**
	 * adds all open surrounding points to the stack, and makes sure it wasn't already previously added
	 * @param maze   maze to check from
	 * @param stack  stack to add points to
	 * @param x		 current x-position to check
	 * @param y		 current y-position to check
	 */
	public static void checkArea ( char [ ] [ ] maze, LinkedStack openPoints, int x, int y ) {
		int up = y - 1;
		int down = y + 1;
		int left = x - 1;
		int right = x + 1;
		
		// check each direction to see if it's open, and ifso, add to the checklater
		if ( maze [ up ] [ x ] == ' ' || maze [ up ] [ x ] == 'F' ) { // if up is an open space or the finish
			if ( ! ( openPoints.contains ( new Point ( x, up ) ) ) ) { // checks to make sure the point wasn't added alraedy
				openPoints.push ( new Point ( x, up ) ); // add to the stack to check
			}
		}
		if ( maze [ down ] [ x ] == ' ' || maze [ down ] [ x ] == 'F' ) { // if down is open
			if ( ! ( openPoints.contains ( new Point ( x, down ) ) ) ) { // checks to make sure the point wasn't added alraedy
				openPoints.push ( new Point ( x, down ) ); 
			}
		}
		if ( maze [ y ] [ left ] == ' ' || maze [ y ] [ left ] == 'F' ) { // left open
			if ( ! ( openPoints.contains ( new Point ( left, y ) ) ) ) { // checks to make sure the point wasn't added alraedy
				openPoints.push ( new Point ( left, y ) );
			}
		}
		if ( maze [ y ] [ right ] == ' ' || maze [ y ] [ right ] == 'F' ) { // right open
			if ( ! ( openPoints.contains ( new Point ( right, y ) ) ) ) { // checks to make sure the point wasn't added alraedy
				openPoints.push ( new Point ( right, y ) );
			}
		}
	}
	
	/**
	 * adds open surrounding points to the queue to check, makes sure the point doesn't already exist in the queue
	 * @param maze   maze to check from
	 * @param queue  queue to add points to 
	 * @param x		 current x-position to check
	 * @param y		 current y-position to check
	 */
	public static void checkArea ( char [ ] [ ] maze, LinkedQueue queue, int x, int y ) {
		int up = y - 1;
		int down = y + 1;
		int left = x - 1;
		int right = x + 1;
		
		// check each direction to see if it's open, and ifso, add to the checklater
		if ( maze [ up ] [ x ] == ' ' || maze [ up ] [ x ] == 'F' ) { // if up is an open space or the finish
			if ( ! ( queue.contains ( new Point ( x, up ) ) ) ) { // checks to make sure the point wasn't added alraedy
				queue.add ( new Point ( x, up ) ); // add to the queue to check
			}
		}
		if ( maze [ down ] [ x ] == ' ' || maze [ down ] [ x ] == 'F' ) { // if down is open
			if ( ! ( queue.contains ( new Point ( x, down ) ) ) ) {
				queue.add ( new Point ( x, down ) ); 
			}
		}
		if ( maze [ y ] [ left ] == ' ' || maze [ y ] [ left ] == 'F' ) { // left open
			if ( ! ( queue.contains ( new Point ( left, y ) ) ) ) {
				queue.add ( new Point ( left, y ) );
			}
		}
		if ( maze [ y ] [ right ] == ' ' || maze [ y ] [ right ] == 'F' ) { // right open
			if ( ! ( queue.contains ( new Point ( right, y ) ) ) ) {
				queue.add ( new Point ( right, y ) );
			}
		}
	}
	
	/**
	 * for self search; allows you to backtrack over spaces, only makes sure doesn't hit walls
	 * program uses this to check if certain directions are open
	 * @param maze   maze you're going through
	 * @param valid  used to check if you surrounding space are open
	 * @param x 	 x-coordinate and surrounding to check
	 * @param y 	 y-coordinate and surrounding to check
	 */
	public static void checkArea ( char [ ] [ ] maze, boolean [ ] valid, int x, int y ) {
		int up = y - 1;
		int down = y + 1;
		int left = x - 1;
		int right = x + 1;

		// check each direction to see if it's open, and ifso, add to the checklater
		if ( maze [ up ] [ x ] != '*' ) { // as long as up is not a wall, it is open
			valid [ 0 ] = true; // if the space is open, add it to the checker array
		}
		if ( maze [ down ] [ x ] != '*' ) { // down not a wall
			valid [ 1 ] = true;
		}
		if ( maze [ y ] [ left ] != '*' ) { // left not wall
			valid [ 2 ] = true; 
		}
		if ( maze [ y ] [ right ] != '*' ) { // right not wall
			valid [ 3 ] = true;
		}
	}
	
	/**
	 * get the next point in the stack
	 * @param stack  stack to add point to
	 * @return the new point
	 */
	public static Point updatePoint ( LinkedStack openPoints ) {
		Point upd = openPoints.pop ( ); // get the newest point in the queue
		return upd; // return the new point
	}
	
	/**
	 * get the next point in the queue
	 * @param queue  queue to add point to
	 * @return the new point
	 */
	public static Point updatePoint ( LinkedQueue queue ) {
		Point upd = queue.remove ( ); // gets the next point in the queue
		return upd; // return the new point
	}
	
	/**
	 * updates the current point with the given direction choice
	 * @param choice direction the user wants to move
	 * @param current current position 
	 * @return point with the updated coordniates
	 */
	public static Point updatePoint ( int choice, Point current ) {
		if ( choice == 0 ) { // chose to move up
			current.translate ( 0, -1 );
		} else if ( choice == 1 ) { // down
			current.translate ( 0, 1 );
		} else if ( choice == 2 ) { // left
			current.translate ( -1, 0 );
		} else if ( choice == 3 ) { // right
			current.translate ( 1, 0 );
		}
		return current;
	}
	
	/**
	 * facade function that passed in the maze and the starting point 
	 * @param method method to search by
	 * @param maze	 maze to work with and search through
	 */
	public static void search ( String method, char [ ] [ ] maze ) {
		Point startingPoint = getStarting ( maze );
		if ( method.equals ( "self" ) ) { // if they self searched
			selfSolve ( maze, startingPoint ); // run self search program
		} else if ( method.equals ( "stack" ) ) { // if chose DFS
			stackSolve ( maze, new LinkedStack ( ), startingPoint );
		} else { // if chose BFS
			queueSolve ( maze, startingPoint );
		}
	}
	
	/**
	 * converts letter direction to corresponding number
	 * @param choice letter direction user chose
	 * @return corresponding number to the direftion
	 */
	public static int convert ( String choice ) {
		if ( choice.equalsIgnoreCase ( "w" ) ) {
			return 0;
		} else if ( choice.equalsIgnoreCase ( "s" ) ) {
			return 1;
		} else if ( choice.equalsIgnoreCase ( "a" ) ) {
			return 2;
		} else { // if choice is dz nutz
			return 3;
		}
	}
	
	/**
	 * self search menu, asks for WASD directions and gets a valid direction
	 * @param maze maze to traverse through
	 * @param x	   curent x position
	 * @param y	   current y position
	 * @return valid direction to travel to
	 */
	public static int getWASD ( char [ ] [ ] maze, int x, int y ) {
		boolean [ ] valid = new boolean [ 4 ]; // create array to see if the direction is valid
		System.out.print ( "Self Search Menu" ); // display menu
		System.out.print ( "\n" + "0. Quit" );
		System.out.print ( "\n" + "W. Up" );
		System.out.print ( "\n" + "S. Down" );
		System.out.print ( "\n" + "A. Left" );
		System.out.print ( "\n" + "D. Right" );
		System.out.print ( "\n" + "Enter a direction (WASD) or quit (0): " ); // prompt
		
		int direction = 0; // number to pass to the self solve program that updates the point
		String choice = CheckInput.getString ( );
		
		while ( true ) { 
			if ( choice.equals ( "0" ) ) {
				return 4; // cant return 0 because 0 represents up
				// but its 0 in the menu because that's what it is in the main menu, so it's easier to remember
			} else if ( choice.equalsIgnoreCase ( "w" ) || choice.equalsIgnoreCase ( "a" ) || choice.equalsIgnoreCase ( "s" ) || choice.equalsIgnoreCase ( "d" ) ) {
				direction = convert ( choice ); // returns 0-3 depending on which direction they chose
				// number is used to check the array index if it's an open space
				// valid[0] checks if up is open, valid[1] checks down, [2] left, [3] right
				
				checkArea ( maze, valid, x, y ); // this will add true's & false's to the array
				
				if ( ( valid [ direction ] ) ) { // if the user's direction is valid, return the direction
					return direction;
				}
			}
			System.out.print ( "Enter a valid choice: " );
			choice = CheckInput.getString ( );
		}
	}
	
	/**
	 * solve puzzle by DFS (stacks)
	 * @param maze	 maze to solve
	 * @param startingPoint where to start the maze
	 */
	public static void stackSolve ( char [ ] [ ] maze, LinkedStack stack, Point startingPoint ) {
		LinkedStack openPoints = stack; // initialize
		Point current = startingPoint; // get starting point
		int x = current.x; // attributes of point
		int y = current.y;
		
		while ( maze [ y ] [ x ] != 'F' ) { // loop until you finish
			leaveTrail ( maze, false, x, y ); // leaves a '·'
			checkArea ( maze, openPoints, x, y ); // check surrounding area and add hits to the stack
			current = updatePoint ( stack ); // move to the next position
			x = current.x; // update x-coordinate
			y = current.y; // y
		}
		
		displayMaze ( maze );
	}
	
	/**
	 * solve puzzle by BFS (queues)
	 * @param maze	 maze to solve
	 * @param startingPoint where to start the maze
	 */
	public static void queueSolve ( char [ ] [ ] maze, Point startingPoint ) {
		LinkedQueue queue = new LinkedQueue ( ); // initialize
		Point current = startingPoint; // get starting point
		int x = current.x; // attributes of point
		int y = current.y;
		
		while ( maze [ y ] [ x ] != 'F' ) { // loop until you finish
			leaveTrail ( maze, false, x, y ); // leaves a '·'
			checkArea ( maze, queue, x, y ); // check surrounding area and add hits to the stack/queue
			current = updatePoint ( queue ); // get the new point
			x = current.x; // update point attributes
			y = current.y;
		}
		
		displayMaze ( maze );
	}
	
	/**
	 * allows user to move along the maze by their own choices
	 * @param maze maze to traverse
	 * @param startingPoint where to start the maze
	 */
	public static void selfSolve ( char [ ] [ ] maze, Point startingPoint ) {
		displayMaze ( maze ); // maze to show user so they know what direction they wanna move next
		LinkedStack openPoints = new LinkedStack ( ); // keep track of all open points; need this or PC might hit a dead end with no other points to opt out to
		Point current = startingPoint; // point to keep track of current position
		int x = current.x; // current position's x coordinate
		int y = current.y; // y
		checkArea ( maze, openPoints, x, y ); // add available points to the stack
		int choice = getWASD ( maze, x, y ); // get validated menu choice, valid direction or quit
		
		while ( choice != 4 && maze [ y ] [ x ] != 'F' ) { // as long as the user hasn't quit and hasn't hit the finish
			leaveTrail ( maze, false, x, y ); // leaves a '·'
			current = updatePoint ( choice, current ); // move to new, desired position
			x = current.x; // update x-coordinate
			y = current.y; // y
			leaveTrail ( maze, true, x, y ); // leaves an 'x' representing the current position of the user
			if ( maze [ y ] [ x ] != 'F' ) { // if the user makes it to the end, they shouldn't be able to choose another option
				displayMaze ( maze ); // will only show the maze if they're not at the end yet
				choice = getWASD ( maze, x, y ); // ask again to move/leave
			} 
			checkArea ( maze, openPoints, x, y ); // add all open points at the new position
			openPoints.remove ( current ); // removes the current point from the stack of open points
		}
		
		if ( maze [ y ] [ x ] == 'F' ) { // if the user finishes the map
			displayMaze ( maze );
			System.out.print ( "Good job! You reached the end." + "\n" + "\n" );
		}

		if ( choice == 4 ) { // gets here if the user quit; finishes the maze with stacks
			if ( maze [ y ] [ x ] != 'S' ) { // remove the user's cursor but not if the cursor is on the starting point
				maze [ y ] [ x ] = '·';
			}
			current = openPoints.pop ( ); // goes to the newest, open point
			stackSolve ( maze, openPoints, current ); // then finish maze with stacks
		}
	}
}