import java.util.*;
import java.io.*;
/**
 *	Population - Prints out a list of 50 states, 
 *  decided by sorting based off of a statistic, which is chosen by user
 *
 *	Requires FileUtils and Prompt classes.
 *
 *	@author	
 *	@since	
 */
public class Population {
    // List of cities
    private List<City> cities;	
    // US data file
    private final String DATA_FILE = "usPopData2017.txt";	
	/**
	 * main - starts the program
	 * @param args
	 */
	public static void main(String[] args) {
		Population p = new Population();
		p.run();
	}
	/**
	 * run - runs the entire program, calling on helper methods and using City.java
	 */
	public void run() {
		cities = new ArrayList<>();
		printIntroduction();
		Scanner io = FileUtils.openToRead(DATA_FILE);
		io.useDelimiter("[\t\n]");
		int count = 0;
		while (io.hasNext()) {
			String state = io.next();
			if (state.equals("New") || state.equals("North") || state.equals("South")
			 || state.equals("West") || state.equals("Rhode"))
				state += " "+io.next();
			if (state.equals("District"))
				state += " "+io.next()+" "+io.next();
			String name = io.next();
			String type = "";
			boolean next = true;
			while (io.hasNext() && next) {
				String temp = io.next();
				if (temp.equals("city") || temp.equals("town") || temp.equals("township")
				 || temp.equals("village") || temp.equals("government")) {
					type = temp;
					next = false;
				} else 
					name += " "+temp;
			}
			int population = io.nextInt();
			cities.add(new City(state, name, type, population));
			count++;
		}
		System.out.println(count+" cities in database");
		String[] states = new String[50];
		int temp2 = 0;
		for (int i=0; i<cities.size(); i++) {
			if (i==0 || !cities.get(i).getState().equals(cities.get(i-1).getState())) {
				states[temp2] = cities.get(i).getState();
				temp2++;
			}
		}
		printMenu();
		int choice = Prompt.getInt("\nEnter selection");
		while (choice<1 || choice>9)
			choice = Prompt.getInt("Enter selection");
		while (choice != 9) {
			long start = System.currentTimeMillis();
			if (choice==1) {
				ascendSelection();
				System.out.println("Fifty least populous cities");
				System.out.printf("\n%10s%25s%25s%20s\n", "State", "City", "Type", "Population");
				for (int i=1; i<=50; i++) {
					City temp = cities.get(i-1);
					System.out.printf("%3d: %-25s%-26s%-14s%10s\n", i, temp.getState(),
					 temp.getName(), temp.getType(), printNum(temp.getPop()));
				}
			}
			if (choice==2) {
				descendMerge(0, cities.size()-1);
				System.out.println("Fifty most populous cities");
				System.out.printf("\n%10s%25s%25s%20s\n", "State", "City", "Type", "Population");
				for (int i=1; i<=50; i++) {
					City temp = cities.get(i-1);
					System.out.printf("%3d: %-25s%-26s%-14s%10s\n", i, temp.getState(),
					 temp.getName(), temp.getType(), printNum(temp.getPop()));
				}
			if (choice==3) {
				ascendInsertion();
				System.out.println("Fifty cities sorted by name");
				System.out.printf("\n%10s%25s%25s%20s\n", "State", "City", "Type", "Population");
				for (int i=1; i<=50; i++) {
					City temp = cities.get(i-1);
					System.out.printf("%3d: %-25s%-26s%-14s%10s\n", i, temp.getState(),
					 temp.getName(), temp.getType(), printNum(temp.getPop()));
				}
			}
			if (choice==4) {
				descendMerge2(0, cities.size()-1);
				System.out.println("Fifty cities sorted by name descending");
				System.out.printf("\n%10s%25s%25s%20s\n", "State", "City", "Type", "Population");
				for (int i=1; i<=50; i++) {
					City temp = cities.get(i-1);
					System.out.printf("%3d: %-25s%-26s%-14s%10s\n", i, temp.getState(),
					 temp.getName(), temp.getType(), printNum(temp.getPop()));
				}
			}
			long end = System.currentTimeMillis();
			if (choice <5)
				System.out.println("\nElapsed Time "+(end-start) +" milliseconds\n");
			if (choice==5) {
				String state = Prompt.getString("Enter state name (i.e. Alabama)");
				boolean works = false;
				for (int i=0; i<50; i++)
					if (states[i].equals(state))
						works = true;
				while (!works) {
					System.out.println("ERROR: "+state+" is not valid");
					state = Prompt.getString("Enter state name (i.e. Alabama)");
					works = false;
					for (int i=0; i<50; i++)
						if (states[i].equals(state))
						works = true;
				}
				mergeState(state, 0, cities.size()-1);
				System.out.println("Fifty most populous cities in "+state);
				System.out.printf("\n%10s%25s%25s%20s\n", "State", "City", "Type", "Population");
				for (int i=1; i<=50; i++) {
					City temp = cities.get(i-1);
					System.out.printf("%3d: %-25s%-26s%-14s%10s\n", i, temp.getState(),
					 temp.getName(), temp.getType(), printNum(temp.getPop()));
				}
				System.out.println();
			}
			if (choice==6) {
				String city = Prompt.getString("\nEnter city name");
				selectCity(city);
				System.out.println("City "+city + " by population");
				System.out.printf("\n%10s%25s%25s%20s\n", "State", "City", "Type", "Population");
				for (int i=1; i<=cities.size() && cities.get(i-1).getName().equals(city); i++) {
					City temp = cities.get(i-1);
					System.out.printf("%3d: %-25s%-26s%-14s%10s\n", i, temp.getState(),
					 temp.getName(), temp.getType(), printNum(temp.getPop()));
				}
				System.out.println();
			}
			printMenu();
			choice = Prompt.getInt("\nEnter selection");
			while (choice<1 || choice>9)
				choice = Prompt.getInt("Enter selection");
		}		
		System.out.println("\nThanks for using population!\n");	
	}
    }
	/**
	 * printNum - converts integer to a string, with commas placed
	 * @param num - number to be converted
	 * @return num as a string with commas
	 */
	public String printNum(int num) {
		String out = Integer.toString(num);
		int l = out.length();
		for (int i=3; i<l; i+=3)
			out = out.substring(0, l-i)+","+out.substring(l-i);
		return out;
	}
	
	/**
	 * ascendSelection - sorts cities in ascending order by population
	 * using selection sort
	 */
	public void ascendSelection() {
		for (int i=0; i<cities.size(); i++) {
			//search for min
			int min = i;
			for (int j=i+1; j<cities.size(); j++) {
				if (cities.get(j).compareTo(cities.get(min))<0)
					min = j;
			}
			//swap
			City temp = cities.get(min);
			cities.set(min, cities.get(i));
			cities.set(i, temp);
		}
	}
	/**
	 * descendMerge - sorts cities in descending order by population
	 * using merge sort
	 * @param start - starting index
	 * @param end - ending index
	 */
	public void descendMerge(int start, int end) {
		int mid = (start+end)/2;
		if (start==end) // base case
			return;

		// recursively sort
		descendMerge(start, mid);
		descendMerge(mid+1, end);

		// keep track of sorted version
		City[] temp = new City[end-start+1];
		int l = start;
		int r = mid+1;
		int i = 0;
		while (l<=mid || r<=end) {
			if (l>mid) {
				temp[i] = cities.get(r);
				r++;
			} else if (r>end) {
				temp[i] = cities.get(l);
				l++;
			} else if (cities.get(l).compareTo(cities.get(r))>0) {
				temp[i] = cities.get(l);
				l++;
			} else {
				temp[i] = cities.get(r);
				r++;
			}
			i++;
		}
		// apply sorted version
		for (i=0; i<temp.length; i++)
			cities.set(start+i, temp[i]);
	}	
	/**
	 * ascendInsertion - sorts cities in ascending order by name
	 * using insertion sort
	 */
	public void ascendInsertion() {
		for (int i=0; i<cities.size(); i++) {
			int a;
			// find location to insert
			for (a=i; a>=0 && cities.get(a).compareTo2(cities.get(i))>=0; a--);
			a++;
			// insert
			for (int j=a; j<i; j++) {
				City temp = cities.get(j);
				cities.set(j, cities.get(i));
				cities.set(i, temp);
			}
		}
	}
	/**
	 * descendMerge2 - sorts cities in descending order by name
	 * using merge sort
	 * @param start - starting index
	 * @param end - ending index
	 */
	public void descendMerge2(int start, int end) {
		int mid = (start+end)/2;
		if (start==end) // base case
			return;

		// recursively sort
		descendMerge2(start, mid);
		descendMerge2(mid+1, end);
		// keep track of sorted version
		City[] temp = new City[end-start+1];
		int l = start;
		int r = mid+1;
		int i = 0;
		while (l<=mid || r<=end) {
			if (l>mid) {
				temp[i] = cities.get(r);
				r++;
			} else if (r>end) {
				temp[i] = cities.get(l);
				l++;
			} else if (cities.get(l).compareTo2(cities.get(r))>0) {
				temp[i] = cities.get(l);
				l++;
			} else {
				temp[i] = cities.get(r);
				r++;
			}
			i++;
		}
		// apply sorted version
		for (i=0; i<temp.length; i++)
			cities.set(start+i, temp[i]);
	}
	/**
	 * mergeState - sorts by descending population, cities with stated state having priority
	 * using merge sort
	 * @param state - which state has priority
	 * @param start - starting index
	 * @param end - ending index
	 */
	public void mergeState(String state, int start, int end) {
		int mid = (start+end)/2;
		if (start==end) // base case
			return;
		// recursion
		mergeState(state, start, mid);
		mergeState(state, mid+1, end);
		// sort into temp array
		City[] temp = new City[end-start+1];
		int l = start;
		int r = mid+1;
		int i = 0;
		while (l<=mid || r<=end) {
			// if city's state is same as input state, top of the list
			if (l<=mid && cities.get(l).getState().equals(state) &&
			 (r>end || !cities.get(r).getState().equals(state))) {
				temp[i] = cities.get(l);
				l++;
			} else if ((l>mid || !cities.get(l).getState().equals(state))
			 && r<=end && cities.get(r).getState().equals(state)) {
				temp[i] = cities.get(r);
				r++;
			} else if (l>mid) {
				temp[i] = cities.get(r);
				r++;
			} else if (r>end) {
				temp[i] = cities.get(l);
				l++;
			} else if (cities.get(l).compareTo(cities.get(r))>0) {
				temp[i] = cities.get(l);
				l++;
			} else {
				temp[i] = cities.get(r);
				r++;
			}
			i++;
		}
		// apply sorted array
		for (i=0; i<temp.length; i++)
			cities.set(start+i, temp[i]);
	}
	/**
	 * selectCity - sorts by population using selection sort
	 * stated city gets priority
	 * @param city - city with priority
	 */
	public void selectCity(String city) {
		for (int i=0; i<cities.size(); i++) {
			// find cities with name, compare with population next
			City temp = null;
			int index = 0;
			for (int j=i; j<cities.size(); j++) {
				if (cities.get(j).getName().equals(city) && (temp==null ||
				 cities.get(j).compareTo(temp)>0)) {
					temp = cities.get(j);
					index = j;
				}
			}
			// if no more cities with name, done
			if (temp == null)
				return;
			// swap
			cities.set(index, cities.get(i));
			cities.set(i, temp);
		}
	}	
	/**	Prints the introduction to Population */
	public void printIntroduction() {
		System.out.println("   ___                  _       _   _");
		System.out.println("  / _ \\___  _ __  _   _| | __ _| |_(_) ___  _ __ ");
		System.out.println(" / /_)/ _ \\| '_ \\| | | | |/ _` | __| |/ _ \\| '_ \\ ");
		System.out.println("/ ___/ (_) | |_) | |_| | | (_| | |_| | (_) | | | |");
		System.out.println("\\/    \\___/| .__/ \\__,_|_|\\__,_|\\__|_|\\___/|_| |_|");
		System.out.println("           |_|");
		System.out.println();
	}	
	/**	Print out the choices for population sorting */
	public void printMenu() {
		System.out.println("1. Fifty least populous cities in USA (Selection Sort)");
		System.out.println("2. Fifty most populous cities in USA (Merge Sort)");
		System.out.println("3. First fifty cities sorted by name (Insertion Sort)");
		System.out.println("4. Last fifty cities sorted by name descending (Merge Sort)");
		System.out.println("5. Fifty most populous cities in named state");
		System.out.println("6. All cities matching a name sorted by population");
		System.out.println("9. Quit");
	}
}