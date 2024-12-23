import java.io.*;
import java.util.*;

/*
k=3 boarded cars
partial solution x=(0,1,0)
s=2000cm: the space available in the left/port lane
build x=(0,1,0) as (k=3,s=2000)
 */
class Main  {

	int L;
	ArrayList<Integer> cars;
	int [] vehicle;


    //**** many other member variables will be added by you ******/
	//**** many other member methods will be added by you  *******/
	int carNum;
	int bestK;
	int[] currX;
	int[] bestX;
	boolean USE_HASH_TABLE = false;//flag to control use big table or hash table
	boolean SHOW_INFO = false;
//--------------------big table begin
	boolean[][] visited;

	void resetBigTable() {//create and initialize visited
		visited = new boolean[carNum+1][L+1];
		for (int i = 0; i < carNum+1; i++)
			for (int j = 0; j < L+1; j++)
				visited[i][j] = false;
	}
//--------------------hash table begin
	static final int SIZE = 512;// the size of hash table
	int probeCount=0;

	//liner probe function
	private int liner_probe(int k, int j) {
		return (k % SIZE +j) % SIZE;
	}

	//Quadratic probe function
	private int q_probe(int k, int j) {
		return (k % SIZE + j*j) % SIZE;
	}

	//Double probe function
	private int d_probe(int k, int j) {
		return (k%SIZE + j * (7-(k % 7))) % SIZE;
	}

	//Quadratic Double probe function
	private int dq_probe(int k, int j) {
		return ((3*k + 1)%SIZE + j*j) % SIZE;
	}

	//call individual probel function
	private int probe(int k, int j) {
		probeCount++;
		return dq_probe (k,j);
	}

	//use array as hash table
	int[] hash_table;


	//check key and state in hash table
	private boolean contains(int key, int state) {
		for (int j= 0; j < SIZE; j++){
			int pos = probe(key,j);
			if (hash_table[pos] == state)
				return true;
			if (hash_table[pos] == -1)
				return false;
		}
		return false;
	}

	//add key and state into hash table
	private boolean put(int key, int value) {
		int j;
		for (j= 0; j < SIZE; j++) {
			int pos = probe(key,j);
			if (hash_table[pos] == -1) {
				hash_table[pos] = value;
				return true;
			}
		}
		throw new RuntimeException  ("couldn't find a position.");
	}

	//create new hashtable and initialize each position as unoccupied (-1)
	void resetHashtable() {
		hash_table = new int[SIZE];
		for (int i = 0; i < SIZE; i++) {
			hash_table[i] = -1;
		}
		int probeCount=0;
	}

//--------------------hash table end

	//reset table (big table or hash table)
	void reset( ) {
		vehicle = new int[carNum];
		for (int i=0;i<carNum;i++) {
			vehicle[i]=cars.get(i);
		}
		if (USE_HASH_TABLE)
			resetHashtable();
		else {
			resetBigTable();
		}
	}

	//print table size and running info
	void info() {
		if (USE_HASH_TABLE)
			System.err.println("hashtable size: " + SIZE + " probeCount: " + probeCount);
		else {
			System.err.println("big table size:[" + (carNum) +"][" +(L) +"]  total " + (carNum)*(L));
		}
	}

	//calculate the free space on right lane
	int getRS(int k, int s) {
		int t = 0;
		for (int i = 0; i < k; i++) {
			t += vehicle[i];
		}
		return L-(t-(L-s));
	}

	//save visited flag, to big or hash table
	void setVisited(int k, int s, boolean b) {
		if (USE_HASH_TABLE)
			put(k, s);
		else {
			visited[k][s] = b;
		}
	}

	//get visited flag, from big or hash table
	boolean getVisited(int k, int s) {
		if (USE_HASH_TABLE)
			return contains(k,s);
		else {
			return visited[k][s];
		}

	}

	// currK cars have been added; currS space remains at the left side
	void BacktrackSolve(int currK, int currS) {
		if (currK > bestK) {
				//bestX[bestK] = currX[bestK];
			System.arraycopy(currX, 0, bestX, 0, currK);
			bestK = currK;
		}
		if (currK < carNum ) {// then there are cars left to consider
			int newS=currS-vehicle[currK];
			int RS=getRS(currK,currS);
			int newRS=RS - vehicle[currK];

			// new car to left lane if it fits.
			if (newS >= 0 && !getVisited(currK + 1, newS)) {
				currX[currK] = 1;
				BacktrackSolve(currK + 1, newS);
				setVisited(currK + 1, newS, true);
			}
			// new car to right lane if it fits.
			if (newRS >= 0  && !getVisited(currK + 1, currS)) {
				currX[currK] = 0;
				BacktrackSolve(currK + 1, currS);
				setVisited(currK + 1, currS, true);
			}


		}
	}

	//**** you are allowed to create a new class, but it must be added to this file  as the online judge accepts a single java file **/
	// reads each problem from input file and call method to solve and print output
	public void process() throws FileNotFoundException {
		long startTime = 0;
		if (SHOW_INFO) {
			startTime = System.nanoTime();
		}
		Scanner scanner = new Scanner(System.in);

		if (scanner.hasNextInt()) {
	        int numTests=scanner.nextInt(); // reads the number of test cases
		    for (int i=0; i< numTests; i++) {

		        if (i>0)
				   System.out.println(); // printing line to standard output to separate outputs for problems
				cars=new ArrayList<Integer>();
				bestK = -1;
		        int cLen=scanner.nextInt(); // this line contains the length of the ferry (L) in meters
			    L=0;
		        if (cLen !=0) {
		    	   L=cLen*100; // convert L from meters to centimeters
       		       while ((cLen=scanner.nextInt()) !=0) // this reads final line containing 0
					   cars.add(cLen); // this reads the length of each car
				   carNum= cars.size();
				   currX = new int[carNum+1];
				   bestX = new int[carNum+1];
		        }
				reset();
		        // **** call a method do solve the current problem:
			    // at this point L contains the length of the ferry and integers contains the length of the cars
				BacktrackSolve(0,L);

				// **** call a method to print solution of the current problem
				System.out.println(bestK); //* delete this line ! */
			   	for (int j = 0; j < bestK; j++) {
					System.out.println(bestX[j]==1?"port":"starboard");
				}

				//show running info
				if (SHOW_INFO)
					info();

		    }

        }

		if (SHOW_INFO) {
			long endTime = System.nanoTime();
			long duration = endTime - startTime; // duration in nanoseconds
			double seconds = duration / 1_000_000.0; // convert to seconds
			double mili = duration / 1_000_000_000.0; // convert to seconds

			System.err.println("Execution time in miliseconds: " + mili);
			System.err.println("Execution time in seconds: " + seconds);
		}
	}



	public static void main(String[] args) {

        Main solver= new Main();

        try {
        solver.process();
        }
        catch(FileNotFoundException e) {
        	System.err.println("Error file not found!");
        }
		catch(Exception e) {
			System.err.println("Some error exception found: " + e.toString());
		}

	}

}