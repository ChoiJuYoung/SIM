package mapProcessor;

import mapInfo.Map;
import mapInfo.OrderedPair;

public class UpdateMapController {
	// fields
	private Map map;
	
	// singleton
	public UpdateMapController() {
		this.map = Map.getInstance();
	}
	    
	// methods
	
	// updates the map info based on the given str
	// str format:
	//			"rotate"
	//			"hazard:true" / "hazard:false"
	//			"position:x,y"
	//			"color:x,y,x,y,..."
	//			"visit:x,y"
	public void updateMap(String str) {
		String[] list = str.split("[:,]");
		String type = list[0];
		
		if (type.equals("rotate")) {
			map.rotateRob();
		} else if (type.equals("hazard")) {
			Boolean hasHazard = Boolean.parseBoolean(list[1]);
			if (hasHazard)
				updateHazards();
		} else {
			int[] xyList = new int[list.length - 1];
			for (int i = 1; i < list.length; i ++) {
				xyList[i - 1] = Integer.parseInt(list[i]);
			}
			switch (type) {
			case "position":
				updatePosition(xyList); break;
			case "color":
				updateColors(xyList); break;
			}
		}
		

	}
	
	private void updatePosition(int[] xyList) {
		int x = xyList[0];
		int y = xyList[1];
		
		map.setCurrRobPos(x, y);
		
		if(map.getSpotsLeft().contains(new OrderedPair(x, y)))
			updateSpotsVisited(xyList);
	}
	
	private void updateColors(int[] xyList) {
		for (int i = 0; i < xyList.length; i += 2) {
			int x = xyList[i];
			int y = xyList[i + 1];
			
			map.addColor(x,  y);
		}
	}
	
	private void updateHazards() {
		int x = map.getCurrRobPos().getX();
		int y = map.getCurrRobPos().getY();
		String currDir = map.getCurrRobDir();
		switch (currDir) {
		case "S":
			y--;
			break;
		case "N":
			y++;
			break;
		case "W":
			x--;
			break;
		case "E":
			x++;
			break;
		}
		map.addHazard(x, y);
	}
	
	private void updateSpotsVisited(int[] xyList) {
		int x = xyList[0];
		int y = xyList[1];
		
		map.markSpotVisited(x, y);
	}
}