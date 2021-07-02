package mapProcessor;

import mapInfo.*;

import java.util.ArrayList;
import java.util.List;

import aStar.AStar;
import aStar.AStar.Node;

public class PathManager {
	private Map map;
	
	public PathManager() {
		this.map = Map.getInstance();
	}
	
	/*
	 *  determines the next move and returns it as a String
	 *  	"forward"  : move forward
	 *  	"rotate:#" : rotate # times
	 *  	null	   : no possible move
	 */
	public String getNextMove() {
		int startX = map.getCurrRobPos().getX();
		int startY = map.getCurrRobPos().getY();
		int endX = map.getNextClosestDest().getX();
		int endY = map.getNextClosestDest().getY();
		
		if ((startX == endX) && (startY == endY)) {
			return "arrived";
		} else {
			AStar as = new AStar(getMaze(), startX, startY);
	        List<Node> path = as.findPathTo(endX, endY);
	        
	        // determines the next move
	        if (path != null) {
	        	int dx = path.get(1).x - startX;
	        	int dy = path.get(1).y - startY;
	        	
	        	String nextDir = "";
	        	if (dx == 0) {
	        		if (dy == -1)
	        			nextDir = "S";
	        		else if (dy == 1)
	        			nextDir = "N";
	        	} else if (dy == 0) {
	        		if (dx == -1)
	        			nextDir = "W";
	        		else if (dx == 1)
	        			nextDir = "E";
	        	}
	        	
	        	return determineMove(map.getCurrRobDir(), nextDir);
	        } else {
	        	return null;
	        }			
		}
	}
	
	private int[][] getMaze() {
		int sizeX = map.getSize().getX();
		int sizeY = map.getSize().getY();
		int[][] maze = new int[sizeY][sizeX];
		
		for (int row = 0; row < sizeY; row++) {
			for (int col = 0; col < sizeX; col++) {
				if (map.getHazards().contains(new OrderedPair(col, row))) {
					maze[row][col] = 0;
				} else {
					maze[row][col] = 1;
				}
			}
		}
		
		return maze;
	}
	
	private String determineMove(String before, String after) {
		ArrayList<String> directions = new ArrayList<String>();
		directions.add("N");
		directions.add("E");
		directions.add("S");
		directions.add("W");
		
		int afterDir = directions.indexOf(after);
		int beforeDir = directions.indexOf(before);
		
		// given parameter(s) is(are) not a direction
		if (afterDir == -1 || beforeDir == -1) {
			return "err";
		}
		
		int diff = directions.indexOf(after) - directions.indexOf(before);
		
		if (diff == 0) {
			return "forward";
		} else {
			if (diff < 0)
				diff = directions.size() + diff;
			
			return "rotate:" + diff;
		}
	}
}