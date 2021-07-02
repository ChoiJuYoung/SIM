package System;
import MapInfo.Map;

public class ReadSensor 
{
	Map map;
	
	public ReadSensor()
	{
		map = Map.getInstance();
	}
	
	public String readSensor(String type)
	{
		String msg = "err";
		int x = map.GetCurrentPos().getX();
		int y = map.GetCurrentPos().getY();
		
		if(type.equals("position"))
			msg = "position:" + Integer.toString(x) + "," + Integer.toString(y);
		else if(type.equals("hazard"))
		{
			String dir = map.GetCurrentDir();
			int nextX, nextY;
			nextX = nextY = 0;
			
			if(dir.equals("N"))
				nextY = 1;
			else if(dir.equals("E"))
				nextX = 1;
			else if(dir.equals("S"))
				nextY = -1;
			else if(dir.equals("W"))
				nextX = -1;
			
			if(map.checkPoint(x + nextX, y + nextY, "hazard"))
				msg = "hazard:true";
			else
				msg = "hazard:false";
		}
		else if(type.equals("color"))
		{
			msg = "";
			
			int nextX, nextY;
			
			nextY = 1;
			nextX = 0;
			if(map.checkPoint(x + nextX, y + nextY, "color"))
			{
				if(!msg.equals("")) msg += ",";
				msg += Integer.toString(x + nextX) + "," + Integer.toString(y + nextY);
			}

			nextY = 0;
			nextX = 1;
			if(map.checkPoint(x + nextX, y + nextY, "color"))
			{
				if(!msg.equals("")) msg += ",";
				msg += Integer.toString(x + nextX) + "," + Integer.toString(y + nextY);
			}

			nextY = -1;
			nextX = 0;
			if(map.checkPoint(x + nextX, y + nextY, "color"))
			{
				if(!msg.equals("")) msg += ",";
				msg += Integer.toString(x + nextX) + "," + Integer.toString(y + nextY);
			}

			nextY = 0;
			nextX = -1;
			if(map.checkPoint(x + nextX, y + nextY, "color"))
			{
				if(!msg.equals("")) msg += ",";
				msg += Integer.toString(x + nextX) + "," + Integer.toString(y + nextY);
			}
			
			msg = "color:" + msg;
		}
		
		return msg;
	}

}
