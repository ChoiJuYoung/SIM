package MapInfo;

public class OrderedPair 
{
	private int x, y;
	
	public OrderedPair(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public OrderedPair()
	{
		x = y = 0;
	}

	public int getX() 
	{
		return x;
	}

	public void setX(int x) 
	{
		this.x = x;
	}

	public int getY() 
	{
		return y;
	}

	public void setY(int y) 
	{
		this.y = y;
	}
	
	
}
