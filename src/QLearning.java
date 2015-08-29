import java.util.Random;


public class QLearning {
	
	GridBlock[][] gridQValues;
	
	enum directions {UP,DOWN,LEFT,RIGHT};
	
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
	

	private int numberOfRows = 5;
	private int numberOfColumns = 4;
	
	private final double GAMMA = 0.9;
	private final double ALPHA = 0.1;
	private double EPSILON = 0.5;
	Random random;
	
	int next_x = 0; 
	int next_y = 0;
	

	QLearning()
	{
		random = new Random();
		gridQValues = new GridBlock[numberOfRows][numberOfColumns];
		
		initializeGrid();
		
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
		REGULAR_REWARD = 0;

	}
	

	private void initializeGrid()
	{
		for(int row = 0; row < 5; row++)
		{
			for(int column = 0; column< 4; column++)
			{
				gridQValues[row][column] = new GridBlock();
			}
		}
	}
	
	
	private void printGrid()
	{
		System.out.println();
		
		for(int row = 0; row < 5; row++)
		{
			for(int column = 0; column< 4; column++)
			{
				System.out.println("FOR ROW COLUMN VALUES " + row + " " + column);
				System.out.println("Up: " +gridQValues[row][column].getUp());
				System.out.println("Down: " +gridQValues[row][column].getDown());
				System.out.println("Left: " +gridQValues[row][column].getLeft());
				System.out.println("Right: " +gridQValues[row][column].getRight());
				//System.out.println("Max: " +gridQValues[row][column].getMaximum());
				System.out.println();

			}
			System.out.println();
		}
		
	}
	
	private double getReward(int y, int x) {
		
		if(hasPit(y, x))
			return PIT_REWARD;
		
		if(isGoal(y, x))
			return GOAL_REWARD;
		
		return REGULAR_REWARD;
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
		if(x < numberOfColumns-1 && !hasBlock(y,x+1))
			return true;
		
		return false;
	}
	
	private double goUpAction(int y, int x)
	{
		double probUp = 0.8;
		double probLeft = 1 - probUp;
		
		double randNumber = random.nextDouble();
		
		if(randNumber <= probUp)
		{
			//GO UP
			boolean canGoUp = canGoUp(y, x);
			
			if (canGoUp) next_y = y - 1;
			else next_y = y;
			
			next_x = x;
		}
		
		else
		{
			//GO LEFT
			boolean canGoLeft = canGoLeft(y, x);
			
			if (canGoLeft) next_x = x - 1;
			else next_x = x;
			
			next_y = y;
		}
				
		return gridQValues[next_y][next_x].getMaximum();
	}
	
	private double goDownAction(int y, int x)
	{
		boolean canGoDown = canGoDown(y, x);
		
		if (canGoDown) next_y = y + 1;
		else next_y = y;
		
		next_x = x;
		
		return gridQValues[next_y][next_x].getMaximum();
	}
	
	private double goLeftAction(int y, int x)
	{
		boolean canGoLeft = canGoLeft(y, x);
		
		if (canGoLeft) next_x = x - 1;
		else next_x = x;
		
		next_y = y;
		
		return gridQValues[next_y][next_x].getMaximum();
	}
	
	private double goRightAction(int y, int x)
	{
		double probRight = 0.8;
		double probDown = 1 - probRight;
		
		double randNumber = random.nextDouble();
		
		if(randNumber <= probRight)
		{
			//GO RIGHT
			boolean canGoRight = canGoRight(y, x);
			if (canGoRight) next_x = x + 1;
			else next_x = x;
			
			next_y = y;
		}
		
		else
		{
			//GO DOWN
			boolean canGoDown = canGoDown(y, x);
			if (canGoDown) next_y = y + 1;
			else next_y = y;
			
			next_x = x;
		}
				
		return gridQValues[next_y][next_x].getMaximum();
		
	}
	
	private directions chooseRandomAction()
	{
		int number = random.nextInt(4);
		
		if(number == 0) return directions.UP;
		else if(number == 1) return directions.DOWN;
		else if(number == 2) return directions.LEFT;
		else return directions.RIGHT;

	}
	
	private directions chooseBestAction(int y, int x)
	{
		GridBlock g = gridQValues[y][x];
		double maxGridValue = g.getMaximum();
		
		if(g.getUp() == maxGridValue) return directions.UP;
		else if(g.getDown() == maxGridValue) return directions.DOWN;
		else if(g.getLeft() == maxGridValue) return directions.LEFT;
		else return directions.RIGHT;
		
	}
	
	private directions chooseEpsilonGreedyAction(int y, int x, int episodes)
	{	
		double number = random.nextDouble();
		
		if(number > EPSILON) return chooseBestAction(y,x);
		
		else
		{
			return chooseRandomAction();
		}
	}
	
	public void calculateQValues()
	{
		
		boolean notConverged = true;
		int episodes = 1;
		while(notConverged && episodes <= 10000)
		{
			int current_x = 0;
			int current_y = 0;
			
	
			random = new Random();
			current_x = random.nextInt(numberOfColumns);
			current_y = random.nextInt(numberOfRows);
				
			if(hasBlock(current_y, current_x))
			{
				continue;
			}
				
			//System.out.println("START " + current_y +" " +current_x);

			boolean isGoal = false;
			
			while(!isGoal)
			{
				double currentQValue; 
				double nextStateQValue;
				
				double qValue;
				
				directions s = chooseEpsilonGreedyAction(current_y,current_x, episodes);
				double currentReward = getReward(current_y, current_x);
				
				if(s == directions.UP)
				{
					if(isGoal(current_y, current_x)) isGoal = true;
					currentQValue = gridQValues[current_y][current_x].getUp();
					
					nextStateQValue = goUpAction(current_y, current_x);
					
					qValue = currentQValue + ALPHA * (currentReward + (GAMMA * nextStateQValue) - currentQValue);
					gridQValues[current_y][current_x].updateUp(qValue);
					
					current_y = next_y;
					current_x = next_x;
				}
				
				else if(s == directions.DOWN)
				{
					if(isGoal(current_y, current_x)) isGoal = true;
					currentQValue = gridQValues[current_y][current_x].getDown();
					
					nextStateQValue = goDownAction(next_y,current_x);
					
					qValue = currentQValue + ALPHA * (currentReward + (GAMMA * nextStateQValue) - currentQValue);
					gridQValues[current_y][current_x].updateDown(qValue);
					
					current_y = next_y;
					current_x = next_x;
					
				}
				
				else if(s == directions.LEFT)
				{
					if(isGoal(current_y, current_x)) isGoal = true;
					currentQValue = gridQValues[current_y][current_x].getLeft();
					
					nextStateQValue = goLeftAction(next_y,current_x);
										
					qValue = currentQValue + ALPHA * (currentReward + (GAMMA * nextStateQValue) - currentQValue);
					gridQValues[current_y][current_x].updateLeft(qValue);
					
					current_y = next_y;
					current_x = next_x;
				}
				
				else if(s == directions.RIGHT)
				{
					if(isGoal(current_y, current_x)) isGoal = true;
					currentQValue = gridQValues[current_y][current_x].getRight();
					
					nextStateQValue = goRightAction(next_y,current_x);
					
					qValue = currentQValue + ALPHA * (currentReward + (GAMMA * nextStateQValue) - currentQValue);
					gridQValues[current_y][current_x].updateRight(qValue);
					
					current_y = next_y;
					current_x = next_x;
				}
			}
			episodes++;
			System.out.println("EPISODES: " + episodes);
			//System.out.println("EPSILON: " +EPSILON);
			
			if(episodes % 1500 == 0)
			{
				EPSILON = EPSILON / (1 + EPSILON);
				//System.out.println(episodes);
			}
					
		}
	}
	

	public static void main(String args[])
	{
		QLearning qLearning = new QLearning();
		qLearning.calculateQValues();
		qLearning.printGrid();
	}

}
