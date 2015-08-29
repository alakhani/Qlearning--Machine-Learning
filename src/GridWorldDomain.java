
public class GridWorldDomain {
	
	double[][] gridStateValues;
	
	private final int GOAL_X;
	private final int GOAL_Y;
	
	private final int PIT_X;
	private final int PIT_Y;
	
	private final int BLOCK_1_X;
	private final int BLOCK_1_Y;
	
	private final int BLOCK_2_X;
	private final int BLOCK_2_Y;
	
	private final int GOAL_REWARD;
	private final int PIT_REWARD;
	private final int REGULAR_REWARD;
	
	private final double GAMMA = 0.9;
	private int numberOfRows = 5;
	private int numberOfColumns = 4;
	
	GridWorldDomain()
	{
		gridStateValues = new double[numberOfRows][numberOfColumns];
		
		GOAL_X = 3;
		GOAL_Y = 0;
		
		PIT_X = 1;
		PIT_Y = 1;
		
		BLOCK_1_X = 1;
		BLOCK_1_Y = 3;
		
		BLOCK_2_X = 3;
		BLOCK_2_Y = 3;
		
		GOAL_REWARD = 10;
		PIT_REWARD = -50;
		REGULAR_REWARD = -1;
		
		gridStateValues[PIT_Y][PIT_X] = PIT_REWARD;
		gridStateValues[GOAL_Y][GOAL_X] = GOAL_REWARD;
		
	}
	
	private boolean hasPit(int y, int x) {
		if(y == PIT_Y && x == PIT_X)
			return true;
		
		return false;
		
	}
	
	private boolean isGoal(int y, int x) {
		if(y == GOAL_Y && x == GOAL_X)
			return true;
		
		return false;
		
	}

	private boolean hasBlock(int y, int x) {
		if(y == BLOCK_1_Y && x == BLOCK_1_X)
			return true;
		
		else if(y == BLOCK_2_Y && x == BLOCK_2_X)
			return true;
		
		else return false;
	}
	
	private double maxValue(double upValue, double downValue, double leftValue,
			double rightValue) {
		
		double maxValue = upValue;
		
		if(downValue > maxValue)
			maxValue = downValue;
		
		if(leftValue > maxValue)
			maxValue = leftValue;
		
		if(rightValue > maxValue)
			maxValue = rightValue;
		
		return maxValue;
	}
	
	private boolean canGoUp(int y, int x)
	{
		if(y > 0 && !hasBlock(y-1,x))
			return true;
		return false;
	}
	
	private boolean canGoDown(int y, int x)
	{
		if(y < numberOfRows-1 && !hasBlock(y+1,x))
			return true;
		
		return false;
	}
	
	private boolean canGoLeft(int y, int x)
	{
		if(x > 0 && !hasBlock(y,x-1))
			return true;
		
		return false;
	}
	
	private boolean canGoRight(int y, int x)
	{
		if(x < numberOfColumns-1 && !hasBlock(y+1,x))
			return true;
		
		return false;
	}
	
	private double getReward(int y, int x) {
		
		if(hasPit(y, x))
			return PIT_REWARD;
		
		if(isGoal(y, x))
			return GOAL_REWARD;
		
		return REGULAR_REWARD;
	}
	
	private double getTransitionValue(double prob, int ys1, int xs1)
	{
		return prob * gridStateValues[ys1][xs1];
	}
	
	private double goUpAction(int y, int x)
	{
		double probUp = 0.8;
		double probLeft = 0.2;
		
		double stateValue = 0;
		
		boolean canGoUp = canGoUp(y, x);
		boolean canGoLeft = canGoLeft(y, x);
		
		if(canGoUp)
		{
			stateValue += getTransitionValue(probUp,y-1,x);
		}
		
		if(canGoLeft)
		{
			stateValue += getTransitionValue(probLeft,y,x-1);
		}
		
		stateValue *= GAMMA;
		stateValue += getReward(y,x);
		return stateValue;
	}
	
	private double goDownAction(int y, int x)
	{
		double probDown = 1;
		double stateValue = 0;
		
		boolean canGoDown = canGoDown(y, x);
		
		if(canGoDown)
		{
			stateValue += getTransitionValue(probDown,y+1,x);
		}
		
		stateValue *= GAMMA;
		stateValue += getReward(y,x);
		return stateValue;
	}
	
	private double goLeftAction(int y, int x)
	{
		double probLeft = 1;
		double stateValue = 0;
		
		boolean canGoLeft = canGoLeft(y, x);
		
		if(canGoLeft)
		{
			stateValue += getTransitionValue(probLeft,y,x-1);
		}
		
		stateValue *= GAMMA;
		stateValue += getReward(y,x);
		return stateValue;
	}
	
	private double goRightAction(int y, int x)
	{
		double probRight = 0.8;
		double probDown = 0.2;
		
		double stateValue = 0;
		
		boolean canGoRight = canGoRight(y, x);
		boolean canGoDown = canGoDown(y, x);
		
		if(canGoRight)
		{
			stateValue += getTransitionValue(probRight,y,x+1);
		}
		
		if(canGoDown)
		{
			stateValue+= getTransitionValue(probDown,y+1,x);
		}
		
		stateValue *= GAMMA;
		stateValue += getReward(y,x);
		return stateValue;
	}
	
	public void calculateStateValues()
	{
		boolean stop = false;
		
		int count = 1;
		while(!stop)
		{
			boolean sameAsPrevious = true;
			
			for(int row = 0; row < 5; row++)
			{
				for(int column = 0; column< 4; column++)
				{
					if(hasBlock(row, column)) continue;
					
					double maxValueOfState = 0;
					
					double upValue = goUpAction(row,column);
					double downValue = goDownAction(row,column);
					double leftValue = goLeftAction(row,column);
					double rightValue = goRightAction(row,column);
					
					maxValueOfState = maxValue(upValue, downValue, leftValue, rightValue);
					
					sameAsPrevious = sameAsPrevious & (gridStateValues[row][column] == maxValueOfState);
						
					gridStateValues[row][column] = maxValueOfState;

				}
			}
			if(sameAsPrevious == true)
				stop = true;
			
			System.out.println("ITERATION NO: " + count);
			count++;
			printGrid();
			System.out.println();
			
		}
		
	}
	
	private void printGrid()
	{
		for(int row = 0; row < 5; row++)
		{
			for(int column = 0; column< 4; column++)
			{
				System.out.print(gridStateValues[row][column] + "\t");

			}
			System.out.println();
		}
		
	}

	public static void main(String args[])
	{
		GridWorldDomain gridWorldDomain = new GridWorldDomain();
		gridWorldDomain.calculateStateValues();
	}

}
