package System;
import MapInfo.Map;

public class Main 
{
	static Map map;
	static Communicator com;
	static RobotMovement robot;
	static ReadSensor sensor;
	
	
	public static void main(String[] args) 
	{
		map = Map.getInstance();
		com = new Communicator();
		robot = new RobotMovement();
		sensor = new ReadSensor();
		
		rcvMsg();
	}
	
	public static void rcvMsg()
	{
		while(true)
		{
			try
			{
				String msg = com.rcvMsg();
				
				if(msg == null) continue;
				
				if(msg.equals("REGAME")) map.init();
				
				if(msg.equals("END")) 
				{
					com.reconnect();
					map.init();
				}
				
				System.out.println(msg);
				
				if(msg.indexOf(":") == -1)
				{
					com.sendMsg(msg);
					continue;
				}
				
				String[] sep = msg.split(":");
				
				switch(sep[0])
				{
				case "move":
					if(sep[1].equals("forward"))
					{
						if(robot.forward())
							com.sendMsg("ok");
						else
							com.sendMsg("err");					
					}
					else if(sep[1].equals("rotate"))
					{
						if(robot.rotate())
							com.sendMsg("ok");
						else
							com.sendMsg("err");
					}				
					break;
				case "sensor":
					com.sendMsg(sensor.readSensor(sep[1]));
					break;
				case "end":
					com.reconnect();
					break;
				default:
					if(initialize(sep))
						com.sendMsg("ok");
					else
						com.sendMsg("err");
					break;
				}
				
				// 여기부터는 임의로 color blob이나 hazard 생성.
				int randMax = 100;
				int rand = (int)(Math.random() * (randMax + 1));
				
				int hazardPerc = 5;
				int colorPerc = 5;
				
				if(rand < hazardPerc)
					makeHazard();
				else if(rand > randMax - colorPerc)
					makeColor();
			}
			catch (Exception e)
			{
				
			}
		}
	}
	
	private static void makeHazard()
	{
		int i = 0;
		while(true)
		{
			i++;
			int x = (int)(Math.random() * map.GetMapSize().getX());
			int y = (int)(Math.random() * map.GetMapSize().getY());
			if(i == 4) break;
			
			if(map.checkPoint(x, y, "hazard")) continue;
			if(map.checkPoint(x, y, "spot")) continue;
			if(map.checkPoint(x, y, "color")) continue;
			if(map.checkPoint(x, y, "position")) continue;
			
			System.out.println("NEW HAZARD : " + x + " // " + y);
			
			map.EnterHazard(x, y);
			break;
		}
	}
	
	private static void makeColor()
	{
		int i = 0;
		while(true)
		{
			i++;
			int x = (int)(Math.random() * map.GetMapSize().getX());
			int y = (int)(Math.random() * map.GetMapSize().getY());
			if(i == 4) break;
			
			if(map.checkPoint(x, y, "hazard")) continue;
			if(map.checkPoint(x, y, "spot")) continue;
			if(map.checkPoint(x, y, "color")) continue;
			if(map.checkPoint(x, y, "position")) continue;

			System.out.println("NEW SPOT : " + x + " // " + y);
			map.enterColor(x, y);
			break;
		}
	}
	
	public static boolean initialize(String[] input)
	{
		String[] tmp;
		if(input.length != 5) return false;
		
		// Map Size
		if(input[0].indexOf(",") == -1) return false;
		tmp = input[0].split(",");
		if(tmp.length != 2) return false;
		map.EnterMapSize(getInt(tmp[0]), getInt(tmp[1]));
		
		// Init Position
		if(input[1].indexOf(",") == -1) return false;
		tmp = input[1].split(",");
		if(tmp.length != 2) return false;
		map.EnterCurrentPos(getInt(tmp[0]),  getInt(tmp[1]));
		
		// Hazard
		if(input[2].indexOf(",") == -1)
		{
			// init hazard doesn't exist
		}
		else
		{
			tmp = input[2].split(",");
			if(tmp.length % 2 != 0) return false;
			
			for(int i = 0; i < tmp.length; i += 2)
				map.EnterHazard(getInt(tmp[i]), getInt(tmp[i + 1]));
		}
		
		// Spot
		if(input[3].indexOf(",") == -1)
		{
			// init spot doesn't exist
		}
		else
		{
			tmp = input[3].split(",");
			if(tmp.length % 2 != 0) return false;
			
			for(int i = 0; i < tmp.length; i += 2)
				map.EnterSpot(getInt(tmp[i]), getInt(tmp[i + 1]));
		}
		
		if(input[4].indexOf(",") == -1)
		{
			// init color doesn't exist
		}
		else
		{
			tmp = input[4].split(",");
			if(tmp.length % 2 != 0) return false;
			
			for(int i = 0; i < tmp.length; i += 2)
				map.enterColor(getInt(tmp[i]), getInt(tmp[i + 1]));
		}
		
		return true;
	}
	
	public static int getInt(String s)
	{
		return Integer.parseInt(s);
	}

}
