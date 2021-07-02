package System;
import MapInfo.Map;

public class RobotMovement 
{
	Map map;
	
	public RobotMovement()
	{
		map  = Map.getInstance();
	}
	
	public boolean forward()
	{
		String dir = map.GetCurrentDir();
		int moveX, moveY;
		moveX = moveY = 0;
		
		if(dir.equals("N"))
			moveY = 1;
		else if(dir.equals("E"))
			moveX = 1;
		else if(dir.equals("S"))
			moveY = -1;
		else if(dir.equals("W"))
			moveX = -1;
		
		return moveRobot(moveX, moveY);
	}
	
	private boolean moveRobot(int moveX, int moveY)
	{
		int randMax = 100;
		int rand = (int)(Math.random() * (randMax +1));
		
		int x = map.GetCurrentPos().getX();
		int y = map.GetCurrentPos().getY();
		
		int sizeX = map.GetMapSize().getX();
		int sizeY = map.GetMapSize().getY();
		
		if(x + moveX >= sizeX || x + moveX < 0 || y + moveY >= sizeY || y + moveY < 0) // 잘못된 이동 명령
			return false;
		
		if(x + 2 * moveX >= sizeX ||  x + 2 * moveX < 0 || y + 2 * moveY >= sizeY || y + 2 * moveY < 0) { } // 벽까지 거리가 한 칸 밖에 없는 경우에는 2칸 전진하지 않음
		else
		{
			int twicePerc = 5; // 2배로 움직일 확률
			
			if(rand < twicePerc) // twicePerc만큼의 확률로
			{
				moveX *= 2;
				moveY *= 2;
			} // 2배로 움직임
		}

		int zeroPerc = 5; // 안움직일 확률
		
		if(rand > randMax - zeroPerc) // zeroPerc확률로
		{
			moveX = 0;
			moveY = 0;
		} // 안움직임

		map.EnterCurrentPos(x + moveX, y + moveY);
		
		return true;
	}
	
	public boolean rotate()
	{
		String dir, next;
		dir = map.GetCurrentDir();
		
		switch(dir)
		{
		case "N":
			next = "E";
			break;
		case "E":
			next = "S";
			break;
		case "S":
			next = "W";
			break;
		case "W":
			next = "N";
			break;
		default:
			return false;
		}
		
		map.EnterCurrentDir(next);
		return true;
	}
}
