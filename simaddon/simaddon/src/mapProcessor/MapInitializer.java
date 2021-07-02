package mapProcessor;

import mapInfo.Map;

public class MapInitializer {
	private Map map;
	
	public MapInitializer() {
		map = Map.getInstance();
	}

	public boolean enterMapSize(int x, int y) {
		return map.setSize(x + 1, y + 1);
	}
	
	public boolean enterCurrPos(int x, int y) {
		return map.setCurrRobPos(x, y);
	}
	
	public boolean enterSpot(int x, int y) {
		return map.addSpotLeft(x, y);
	}
	
	public boolean enterHazard(int x, int y) {
		return map.addHazard(x,  y);
	}
	
	public boolean enterColor(int x, int y)
	{
		return map.addColor(x, y);
	}
}