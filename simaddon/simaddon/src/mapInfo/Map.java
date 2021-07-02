package mapInfo;

import java.util.Vector;

public class Map {
    // fields
    private OrderedPair size;
    private Vector<OrderedPair> spotsLeft;
    private Vector<OrderedPair> spotsVisited;
    private Vector<OrderedPair> hazards;
    private Vector<OrderedPair> colors;
    private OrderedPair currRobPos;
    private String currRobDir;
    private static Map map;
	
    // singleton
    private Map() {
    	this.size = new OrderedPair();
    	this.spotsLeft = new Vector<OrderedPair>();
    	this.spotsVisited = new Vector<OrderedPair>();
    	this.hazards = new Vector<OrderedPair>();
    	this.colors = new Vector<OrderedPair>();
    	this.currRobPos = new OrderedPair();
    	this.currRobDir = "N";
    }

    public static Map getInstance() {
        if(map == null)
        	map = new Map();
        
        return map;
    }
    
    // methods
    public OrderedPair getSize() {
        return size;
    }
    
    public boolean setSize(int x, int y) {
        if (x <= 0 || y <= 0) {
            return false;
        }

        size.setX(x);
        size.setY(y);
        return true;
    }

    public OrderedPair getCurrRobPos() {
        return currRobPos;
    }

    public boolean setCurrRobPos(int x, int y) {
        if (!withinMap(x, y)) {
        	return false;
        }
        
    	currRobPos.setX(x);
        currRobPos.setY(y);
        return true;
    }

    public String getCurrRobDir() {
        return currRobDir;
    }

    public void rotateRob() {
        switch (currRobDir) {
        case "S":
        	currRobDir = "W"; break;
        case "N":
        	currRobDir = "E"; break;
        case "W":
        	currRobDir = "N"; break;
        case "E":
        	currRobDir = "S"; break;
        }
    }
    
    public Vector<OrderedPair> getSpotsLeft() {
        return spotsLeft;
    }

    public Vector<OrderedPair> getSpotsVisited() {
        return spotsVisited;
    }
    
    public boolean addSpotLeft(int x, int y) {
    	if (!withinMap(x, y)) {
    		return false;
    	}
    	
    	OrderedPair spot = new OrderedPair(x, y);
    	if (!spotsLeft.contains(spot)) {
    		spotsLeft.add(spot);
    	}
        return true;
    }
    
    public void markSpotVisited(int x, int y) {
    	OrderedPair spot = new OrderedPair(x, y);
    	
    	spotsLeft.remove(spot);
    	spotsVisited.add(spot);
    }
    
    
    public Vector<OrderedPair> getColors() {
    	return colors;
    }
    
    public boolean addColor(int x, int y)
    {
    	if (!withinMap(x, y)) {
    		return false;
    	}
    	
    	OrderedPair color = new OrderedPair(x, y);
    	if (!colors.contains(color)) {
    		colors.add(color);
    	}
        return true;
    }
    
    public Vector<OrderedPair> getHazards() {
    	return hazards;
    }
    
    public boolean addHazard(int x, int y) {
    	if (!withinMap(x, y)) {
    		return false;
    	}
    	
    	OrderedPair hazard = new OrderedPair(x, y);
    	if (currRobPos.equals(hazard) || spotsLeft.contains(hazard)) {
    		return false;
    	}
    	
    	if (!hazards.contains(hazard)) {
    		hazards.add(new OrderedPair(x, y));
    	}
    	return true;
    }
    
    public OrderedPair getNextClosestDest() {
        OrderedPair next = null;
        
	    int oldDist = Integer.MAX_VALUE;
	    int newDist;
	       
	    for (int i = 0; i < spotsLeft.size(); i++) {
	    	newDist = getDistance(currRobPos, spotsLeft.get(i));
	    	
	    	if (newDist < oldDist) {
	    		next = spotsLeft.get(i);
	    		oldDist = newDist;
	    	}
	    }
        
        return next;
    }

    private int getDistance(OrderedPair op1, OrderedPair op2) {
        return  Math.abs(op1.getX() - op1.getX())
        		+ Math.abs(op1.getY() - op2.getY());
    }
    
    private boolean withinMap(int x, int y) {
    	return (0 <= x && x < size.getX() && 0 <= y && y < size.getY());
    }	
}