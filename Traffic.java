import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;

/*****************************************************************************/

//program simulates traffic dynamics
public class Traffic
{
	private final int EMPTY = 0;	//empty location
	private final int PLUS = 1;		//type one vehicle moves down
	private final int MINUS = 2;	//type two vehicle moves right
	
	private int[][] array;			//array of vehicles
	private int size;				//size of array
	private int iterations;			//number iterations
	private double density;			//density of vehicles
	private Random random;			//random number generator
	private TrafficDrawer drawer;	//drawing object
	private int seed;				//seed value for printing later
	
	/*****************************************************************************/

	//Constructor of traffic class
	public Traffic(int size, int iterations, double density, int seed)
	{
		this.array = new int[size][size];		//create array
		this.size = size;						//set size
		this.iterations = iterations;			//set iterations
		this.density = density;					//set density
		this.random = new Random(seed);			//create random number generator
		this.seed = seed;						//store seed value for output later
		this.drawer = new TrafficDrawer(array, size);	//create drawing object
	}
	
	/*****************************************************************************/

	//Method runs simulation
	public void run(String fileLoc) throws IOException
	{
		//*****************GLOBAL TEMP SWAP****************//
		// after every 8 iterations, the cars swap directions for 2 iterations
		//and then swap back, the idea is to jumble up the gridlock to see if it
		//will break loose
		
		
		DecimalFormat df = new DecimalFormat(".00");
		df.setRoundingMode(RoundingMode.DOWN);
		
		PrintWriter outFile = new PrintWriter(new FileWriter(fileLoc));
		
		outFile.println("***********************************");
		outFile.println("* Size       = " + size); 
		outFile.println("* Iterations = " + iterations);
		outFile.println("* Density    = " + density);
		outFile.println("* Seed       = " + seed);
		outFile.println("***********************************");
		
		
		//initialize vehicles
		initialize();
		
		//keeps track of how many times a car goes to move but cant
		//in this particular vehicle update
		int tempGridLock = 0;
		
		//keeps track of how many times a car moves without impediment
		//used for calculating gridlock percentage later
		int nonGridLock = 0;
		
		//keeps track of gridlock percentage
		double gridLockPercent = 0;
		
		//run iterations
		for (int n = 0; n < iterations; n++)
		{
			
			//reset variables for iteration re-use
			tempGridLock = 0;
			nonGridLock = 0;
			gridLockPercent = 0;
			
			//if the iteration loop is divisible by 5, swap car directions for one iteration
			if (n%15 == 0)
			{
				for (int d = 0; d < size; d ++)
				{
					for (int f = 0; f < size; f++)
					{
						if (array[d][f] == PLUS)
						{
							array[d][f] = MINUS;
						}
						else if (array[d][f] == MINUS)
						{
							array[d][f] = PLUS;
						}							
					}
				}
			}
			else if (n%10 == 0)
			{
				
			}
			else if (n%5 == 0)
			{
				for (int d = 0; d < size; d ++)
				{
					for (int f = 0; f < size; f++)
					{
						if (array[d][f] == PLUS)
						{
							array[d][f] = MINUS;
						}
						else if (array[d][f] == MINUS)
						{
							array[d][f] = PLUS;
						}							
					}
				}
			}
			
			//draw array
			draw();

			
			//update vehicles
			for (int m = 0; m < size * size; m++)
			{
				//pick a location randomly
				int i = random.nextInt(size);
				int j = random.nextInt(size);
				
				//if down moving vehicle
				if (array[i][j] == PLUS)
				{
					if     (n%15 == 0)
					{
						if (array[(i+size-1)%size][j] == EMPTY)	//if up if empty
						{
							array[i][j] = EMPTY;			//move up
							array[(i+size-1)%size][j] = PLUS;
							nonGridLock = nonGridLock + 1;
						}
						else if (array[(i+size-1)%size][j] == MINUS || array[(i+size-1)%size][j] == PLUS)
						{
							tempGridLock = tempGridLock + 1;
						}
					}
					else if (n%10 == 0)
					{
						if (array[(i+size-1)%size][j] == EMPTY)	//if up if empty
						{
							array[i][j] = EMPTY;			//move up
							array[(i+size-1)%size][j] = PLUS;
							nonGridLock = nonGridLock + 1;
						}
						else if (array[(i+size-1)%size][j] == MINUS || array[(i+size-1)%size][j] == PLUS)
						{
							tempGridLock = tempGridLock + 1;
						}
					}
					else // handles both regular and %5 cases as values have already swapped
					{
						if (array[(i+1)%size][j] == EMPTY)	//if down is empty
						{
							array[i][j] = EMPTY;			//move down
							array[(i+1)%size][j] = PLUS;
							nonGridLock = nonGridLock + 1;
						}
						else if (array[(i+1)%size][j] == MINUS || array[(i+1)%size][j] == PLUS)
						{
							tempGridLock = tempGridLock + 1;
						}
					}
					
					
				}
				
				//if right moving vehicle
				else if (array[i][j] == MINUS)
				{
					if (n%15 == 0)
					{
						if (array[i][(j+size-1)%size] == EMPTY)	//if left is empty
						{
							array[i][j] = EMPTY;			//move left
							array[i][(j+size-1)%size] = MINUS;
							nonGridLock = nonGridLock + 1;
						}
						else if (array[i][(j+size-1)%size] == MINUS || array[i][(j+size-1)%size] == PLUS)
						{
							tempGridLock = tempGridLock + 1;
						}
					}
					else if (n%10 == 0)
					{
						if (array[i][(j+size-1)%size] == EMPTY)	//if left is empty
						{
							array[i][j] = EMPTY;			//move left
							array[i][(j+size-1)%size] = MINUS;
							nonGridLock = nonGridLock + 1;
						}
						else if (array[i][(j+size-1)%size] == MINUS || array[i][(j+size-1)%size] == PLUS)
						{
							tempGridLock = tempGridLock + 1;
						}
					}
					else // handles both regular and %5 cases as values have already swapped
					{
						if (array[i][(j+1)%size] == EMPTY)	//if right is empty
						{
							array[i][j] = EMPTY;			//move right
							array[i][(j+1)%size] = MINUS;
							nonGridLock = nonGridLock + 1;
						}
						else if (array[i][(j+1)%size] == MINUS || array[i][(j+1)%size] == PLUS)
						{
							tempGridLock = tempGridLock + 1;
						}
					}
					
					
				}
				
				//if location is empty do nothing
				
			}
			
			
			//if the iteration loop is divisible by ten, revert to original vehicle types
			if (n%15 == 0)
			{
				for (int d = 0; d < size; d ++)
				{
					for (int f = 0; f < size; f++)
					{
						if (array[d][f] == PLUS)
						{
							array[d][f] = MINUS;
						}
						else if (array[d][f] == MINUS)
						{
							array[d][f] = PLUS;
						}							
					}
				}
			}
			else if (n%10 == 0)
			{
				
			}
			else if (n%5 == 0)
			{
				for (int d = 0; d < size; d ++)
				{
					for (int f = 0; f < size; f++)
					{
						if (array[d][f] == PLUS)
						{
							array[d][f] = MINUS;
						}
						else if (array[d][f] == MINUS)
						{
							array[d][f] = PLUS;
						}							
					}
				}
			}
			
			
			
			gridLockPercent = tempGridLock + nonGridLock;
			gridLockPercent = tempGridLock / gridLockPercent;
			gridLockPercent = gridLockPercent * 100;
			
			outFile.println(df.format(gridLockPercent));
		}
		
		outFile.close();
	}
	
	/*****************************************************************************/

	//Method initializes vehicles
	private void initialize()
	{
		//go through all locations
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (random.nextDouble() < density)
				{
					if (random.nextDouble() < 0.5)	//assign down vehicle
						array[i][j] = PLUS;
					else
						array[i][j] = MINUS;		//assign right vehicle
				}
				else
					array[i][j] = EMPTY;			//assign empty location
			}
		}
	}
	
	/*****************************************************************************/

	//method draws array of vehicles
	private void draw()
	{
		drawer.repaint();		//repaint array
		
		//try {Thread.sleep(1);}	catch(Exception e){}	//pause
	}
	
	/*****************************************************************************/

}