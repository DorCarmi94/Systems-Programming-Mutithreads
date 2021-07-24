package bgu.spl.mics.application.passiveObjects;


import java.util.*;
import java.util.stream.Collectors;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;
	private Map<String, String> agentsNames;
	private Map<String, Object> agentsLockingObjects;
	private static Squad instance= new Squad();
	private int numberOfAgents;
	private int numberOfAvailableAgents;

	/**
	 * Retrieves the single instance of this class.
	 */

	private Squad()
	{
		numberOfAgents=0;
		numberOfAvailableAgents=0;
		this.agents=new HashMap<>();
		this.agentsNames= new HashMap<>();
		agentsLockingObjects=new HashMap<>();

	}
	public static Squad getInstance() {
		return instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (Agent a:agents) {
			numberOfAgents++;
			numberOfAvailableAgents++;
			this.agents.put(a.getSerialNumber(),a);
			this.agentsNames.put(a.getSerialNumber(),a.getName());
			this.agentsLockingObjects.put(a.getSerialNumber(),new Object());

		}

		HashMap result = this.agents.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));
		this.agents=result;
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for (String str: serials) {
			if(this.agents.containsKey(str)) {
				this.agents.get(str).release();
				this.numberOfAvailableAgents++;
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time-ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean  getAgents(List<String> serials){
		Thread t= Thread.currentThread();
		//Sorting the accepted list
		Collections.sort(serials);
		for (int i = 0; i < serials.size(); i++) {
			//if(serials.get(i).equals("008"))
				//System.out.println("008");
			if(!this.agents.containsKey(serials.get(i)))
				return false;
		}
		for (String str:serials) {
			try {
				this.agents.get(str).acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
		//Sorting the accepted list
		Collections.sort(serials);
		List<String> namesToReturn= new Vector<>();
    	for (String str: serials) {
    		if(this.agentsNames.containsKey(str))
				namesToReturn.add(this.agentsNames.get(str));
		}
	    return namesToReturn;
    }

}
