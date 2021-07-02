package mapInfo;

public class OrderedPair {
    private int x;
    private int y;
    
    public OrderedPair() {
    	this(0, 0);
    }
    
    public OrderedPair(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o != null && this.getX() == ((OrderedPair) o).getX()
    			&& this.getY() == ((OrderedPair) o).getY())
    		return true;
    	else
    		return false;
    }
    
    @Override
    public String toString() {
    	return x + "," + y;
    }
}