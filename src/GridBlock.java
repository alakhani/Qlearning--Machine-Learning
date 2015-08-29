import java.util.HashMap;


public class GridBlock {
	
	private enum directions {UP,DOWN,LEFT,RIGHT};
	private HashMap<directions, Double> state;
	private double maximum;
	private directions maximumDirection; 
	
	GridBlock()
	{
		state = new HashMap<>();
		state.put(directions.UP, 0.0);
		state.put(directions.DOWN, 0.0);
		state.put(directions.LEFT, 0.0);
		state.put(directions.RIGHT, 0.0);
		
		maximum = 0;
		//maximumDirection = null;
	}
	
	public void updateUp(double d)
	{
		state.put(directions.UP, d);
		if(d > maximum)	maximum = d;
	
	}
	
	public void updateDown(double d)
	{
		state.put(directions.DOWN, d);
		if(d > maximum) maximum = d;
	}
	
	public void updateLeft(double d)
	{
		state.put(directions.LEFT, d);
		if(d > maximum) maximum = d;
	}
	
	public void updateRight(double d)
	{
		state.put(directions.RIGHT, d);
		if(d > maximum) maximum = d;
	}
	
	public double getUp()
	{
		return state.get(directions.UP);
	}
	
	public double getDown()
	{
		return state.get(directions.DOWN);
	}
	
	public double getLeft()
	{
		return state.get(directions.LEFT);
	}
	
	public double getRight()
	{
		return state.get(directions.RIGHT);
	}
	
	public double getMaximum()
	{
		return maximum;
	}
	

}
