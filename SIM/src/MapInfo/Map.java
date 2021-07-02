package MapInfo;

import java.util.Vector;

public class Map {
	private OrderedPair size;
	private OrderedPair currentPos;
	private String currentDir;
	private Vector<OrderedPair> spot;
	private Vector<OrderedPair> hazard;
	private Vector<OrderedPair> color;

	private static Map map = new Map();

	private Map() {
		init();
	}

	public void init() {
		size = new OrderedPair();
		currentPos = new OrderedPair();
		currentDir = "N";
		spot = new Vector<OrderedPair>();
		hazard = new Vector<OrderedPair>();
		color = new Vector<OrderedPair>();
	}

	public static Map getInstance() {
		return map;
	}

	public OrderedPair GetMapSize() {
		return size;
	}

	public void EnterMapSize(int x, int y) {
		this.size = new OrderedPair(x, y);
	}

	public OrderedPair GetCurrentPos() {
		return currentPos;
	}

	public void EnterCurrentPos(int x, int y) {
		this.currentPos = new OrderedPair(x, y);
	}

	public String GetCurrentDir() {
		return currentDir;
	}

	public void EnterCurrentDir(String dir) {
		this.currentDir = dir;
	}

	 public int GetSpotSize() 
	 { 
		 return spot.size(); 
	 }
	 
	 public Vector<OrderedPair> GetSpot() 
	 { 
		 return spot; 
	 }
	 
	 public OrderedPair GetSpot(int index) 
	 { 
		 return spot.elementAt(index); 
	 }
	 
	 public void EnterSpot(int x, int y) 
	 { 
		 OrderedPair spot = new OrderedPair(x, y); 
		 this.spot.add(spot); 
	 }
	 

	public Vector<OrderedPair> getColor() {
		return color;
	}

	public void enterColor(int x, int y) {
		OrderedPair color = new OrderedPair(x, y);
		this.color.add(color);
	}

	public int GetHazardSize() {
		return hazard.size();
	}

	public Vector<OrderedPair> GetHazard() {
		return hazard;
	}

	public OrderedPair GetHazard(int index) {
		return hazard.elementAt(index);
	}

	public void EnterHazard(int x, int y) {
		OrderedPair hazard = new OrderedPair(x, y);
		this.hazard.add(hazard);
	}

	private boolean CheckDuplicated(int x, int y, OrderedPair op) {
		int opx = op.getX();
		int opy = op.getY();

		if (x == opx && y == opy)
			return true;
		return false;
	}

	private boolean CheckDuplicated(int x, int y, Vector<OrderedPair> opv) {
		for (int i = 0; i < opv.size(); i++) {
			OrderedPair op = opv.elementAt(i);

			if (CheckDuplicated(x, y, op))
				return true;
		}

		return false;
	}

	public boolean checkPoint(int x, int y, String type) {
		if (type.equals("hazard"))
			return CheckDuplicated(x, y, GetHazard());
		 if(type.equals("spot")) 
			 return CheckDuplicated(x, y, GetSpot());
		if (type.equals("position"))
			return CheckDuplicated(x, y, GetCurrentPos());
		if (type.equals("color"))
			return CheckDuplicated(x, y, getColor());

		return false;
	}

}
