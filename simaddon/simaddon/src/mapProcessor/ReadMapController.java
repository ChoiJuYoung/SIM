package mapProcessor;

import java.util.Vector;

import mapInfo.*;

public class ReadMapController {
	private Map map;
	
	public ReadMapController()
	{
		map = Map.getInstance();
	}
	
	public OrderedPair getSize()
	{
		return map.getSize();
	}
	
	public OrderedPair getCurrRobPos()
	{
		return map.getCurrRobPos();
	}
	
	public String getCurrRobDir()
	{
		return map.getCurrRobDir();
	}
	
	public Vector<OrderedPair> getSpotsLeft()
	{
		return map.getSpotsLeft();
	}
	
	public Vector<OrderedPair> getSpotsVisited()
	{
		return map.getSpotsVisited();
	}
	
	public Vector<OrderedPair> getHazards()
	{
		return map.getHazards();
	}
	
	public Vector<OrderedPair> getColors()
	{
		return map.getColors();
	}
}
