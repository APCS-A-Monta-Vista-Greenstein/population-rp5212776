/**
 *	City data - the city name, state name, location designation,
 *				and population est. 2017
 *
 *	@author	
 *	@since	
 */
public class City implements Comparable<City> {
	private String name, state, type;
	int population;
	// fields
	
	// constructor
	public City(String s, String c, String t, int p) {
		name = c;
		state = s;
		type = t;
		population = p;
	} 
	
	/**	Compare two cities populations
	 *	@param other		the other City to compare
	 *	@return				the following value:
	 *		If populations are different, then returns (this.population - other.population)
	 *		else if states are different, then returns (this.state - other.state)
	 *		else returns (this.name - other.name)
	 */
	public int compareTo(City other) {
		if (population!=other.population)
			return population-other.population;
		else if (state!=other.state)
			return state.compareTo(other.state);
		else 
			return name.compareTo(other.name);
	}

	/**
	 * Compares two cities by name
	 * @param other - the city comparing to
	 * @return compare value
	 */
	public int compareTo2(City other) {
		if (!name.equals(other.name)) 
			return name.compareTo(other.name);
		else if (population!=other.population)
			return other.population-population;
		else 
			return state.compareTo(other.state);
	}
	
	/**	Equal city name and state name
	 *	@param other		the other City to compare
	 *	@return				true if city name and state name equal; false otherwise
	 */
	 public boolean equals(City other) {
		if (name.equals(other.name) && state.equals(other.state))
			return true;
		return false;
	}
	
	/**	Accessor methods */
	public String getName() {return name;}
	
	public String getState() {return state;}
	
	public String getType() {return type;}
	
	public int getPop() {return population;}
	
	/**	toString */
	@Override
	public String toString() {
		return String.format("%-22s %-22s %-12s %,12d", state, name, type,
						population);
	}
}