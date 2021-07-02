package aStar;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class AStar {
    private final List<Node> open;
    private final List<Node> closed;
    private final List<Node> path;
    private final int[][] maze;
    private Node current;
    private final int startX;
    private final int startY;
    private int endX;
    private int endY;
    
    public AStar(int[][] maze, int startX, int startY) {
        this.open = new ArrayList<Node>();
        this.closed = new ArrayList<Node>();
        this.path = new ArrayList<Node>();
        this.maze = maze;
        this.current = new Node(null, startX, startY, 0, 0);
        this.startX = startX;
        this.startY = startY;
    }
    
    // returns path to (endX, endY); returns null if no path exists
    public List<Node> findPathTo(int endX, int endY) {
    	this.closed.add(this.current);
    	this.endX = endX;
        this.endY = endY;
        
        
        while (true) {
        	addNeigborsToOpenList();
        	
        	if(!(this.current.x != this.endX || this.current.y != this.endY)) break;
        	
            if (this.open.isEmpty()) { // Nothing to examine
                return null;
            }
            
            this.current = this.open.get(0); // get first node (lowest f score)
            this.open.remove(0); // remove it
            this.closed.add(this.current); // and add to the closed
        }
        
        // after arrived
        this.path.add(0, this.current);
        while (this.current.x != this.startX || this.current.y != this.startY) {
            this.current = this.current.parent;
            this.path.add(0, this.current);
        }
        return this.path;
    }
    
    // returns true if the given node is in the given array; returns false otherwise
    private static boolean findNeighborInList(List<Node> array, Node node) {
        return array.stream().anyMatch((n) -> (n.x == node.x && n.y == node.y));
    }
    
    private void addNeigborsToOpenList() {
        Node neighbor;
        
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // skips if diagonal
            	if (dx != 0 && dy != 0) {
                    continue;
                }
            	
            	// if not diagonal
            	int neighborX = this.current.x + dx;
            	int neighborY = this.current.y + dy;
                neighbor = new Node(this.current, neighborX, neighborY,
                				this.current.g, this.distance(neighborX, neighborY));
                
                if (
                	(dx != 0 || dy != 0) // if not this.current
	                && 0 <= neighborX && neighborX < this.maze[0].length // if within maze boundaries
	                && 0 <= neighborY && neighborY < this.maze.length
	                && this.maze[neighborY][neighborX] != 0 // if this neighbor is walkable
	                && !findNeighborInList(this.open, neighbor) // if not already done
	                && !findNeighborInList(this.closed, neighbor)
	            ) {
                    neighbor.g = neighbor.parent.g + 1.; // horizontal/vertical cost = 1.0
                    neighbor.g += maze[neighborY][neighborX]; // add movement cost for this neighbor
                    
                    this.open.add(neighbor);
                }
            }
        }
        
        Collections.sort(this.open);
    }
    
    // returns the distance between this.current and the given (x,y) node
    private double distance(int x, int y) {
        return Math.abs(this.endX - x) + Math.abs(this.endY - y);
    }
    
   	// inner class: Node
   	public static class Node implements Comparable<Node> {
        public Node parent;
        public int x;
        public int y;
        public double g;
        public double h;
        
        public Node(Node parent, int x, int y, double g, double h) {
            this.parent = parent;
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
        }
        
        // compare by f value (f == g + h)
        @Override
        public int compareTo(Node node) {
        	Node other = (Node) node;
        	return (int)((this.g + this.h) - (other.g + other.h));
        }
    }
}