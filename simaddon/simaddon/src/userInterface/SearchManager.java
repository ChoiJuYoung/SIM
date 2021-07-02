package userInterface;

import java.util.Vector;

import mapInfo.OrderedPair;
import mapProcessor.*;
import simController.SIMCommunicator;

public class SearchManager implements Runnable{
	private final int delay; // ms
	private SIMCommunicator com;
	private DisplayMapUI dm;
	private UpdateMapController uc;
	private ReadMapController rmc;
	
	public SearchManager(DisplayMapUI dm) {
		this.dm = dm;
		delay = 700;
		com = SIMCommunicator.getInstance();
		uc = new UpdateMapController();
		rmc = new ReadMapController();
	}
	
	public void run()
	{
		startSearch();
	}
	
	private void startSearch() {
		syncSIM();
		PathManager path = new PathManager();
		
		while (true) {
			updateMapInfo();
			
			if(!(rmc.getSpotsLeft().size() > 0)) break;
			
			String move = path.getNextMove(); // "type(:rotationCount)"
			
			if(move == null) {
				dm.displayMsg("NO PATH");
				break;
			}
			
			String[] list = move.split(":");
			String type = list[0];
			
			if (type.equals("forward")) {
				com.moveRobot(type);
			} else if (type.equals("rotate")) {
				rotateMultiple(Integer.parseInt(list[1]));
			}
			
			waitMs(delay);
		}
		
		waitMs(delay);
		dm.displayMsg("COMPLETED");
		
		com.close();
	}
	
	private String syncSIM() {
		return com.initSIM(getInitSIMMsg());
	}
	
	private String getInitSIMMsg() {
		String siz = rmc.getSize().toString();
		String pos = rmc.getCurrRobPos().toString();
		
		String haz = "";
		Vector<OrderedPair> hazards = rmc.getHazards();
		if (hazards.size() > 0) {
			for (int i = 0; i < rmc.getHazards().size() - 1; i++) {
				haz += hazards.get(i) + ",";
			}
			
			haz += hazards.get(hazards.size() - 1);
		}
		
		String spt = "";
		Vector<OrderedPair> spots = rmc.getSpotsLeft();
		if (spots.size() > 0) {
			for (int i = 0; i < rmc.getSpotsLeft().size() - 1; i++) {
				spt += spots.get(i) + ",";
			}
			
			spt += spots.get(spots.size() - 1);
		}
		
		String col = "";
		Vector<OrderedPair> colors = rmc.getColors();
		if (colors.size() > 0) {
			for (int i = 0; i < rmc.getColors().size() - 1; i++) {
				col += colors.get(i) + ",";
			}
			col += colors.get(colors.size() - 1);
		}
		
		return String.join(":", siz, pos, haz, spt, col);
	}
	
	private void updateMapInfo() {
		String position = com.readSensor("position");
		String colors = com.readSensor("color");
		String hazards = com.readSensor("hazard");
		
		uc.updateMap(position);
		uc.updateMap(colors);
		uc.updateMap(hazards);

		dm.updatePosition();
	}
	
	private void rotateMultiple(int count) {
		for (int i = 0; i < count; i++) {
			com.moveRobot("rotate");
			uc.updateMap("rotate");
			
			waitMs(delay);
		}
	}
	
	private void waitMs(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}