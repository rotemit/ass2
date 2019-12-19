package bgu.spl.mics.application.passiveObjects;
import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;
	private static Squad instance = null;
	private int numOfAgents;

	//**********************contructors
	private Squad(){
		agents = null;
		numOfAgents = 0;
	}

	//methods for testing
	public boolean findSpecificAgent(String serialNumber){
		return agents.containsKey(serialNumber);
	}

	public Agent getSpecificAgent(String serialNumber){
		return agents.get(serialNumber);
	}

	public int getNumOfAgents(){
		return numOfAgents;
	}
	//end of testing methods

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		if (instance == null)
			instance =  new Squad();
		return instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (int i =0; i < agents.length; i++){
			this.agents.put(agents[i].getSerialNumber(), agents[i]);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for (String serial : serials){
			agents.get(serial).release();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time) throws InterruptedException {
		Thread.sleep(time);
		for (String serial : serials){
			agents.get(serial).release();
		}
		//TODO : check sleep method wtf
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) throws InterruptedException {
		boolean noMissing = true;
		for (String serial : serials){
			if (!agents.containsKey(serial)){
				noMissing =  false;
			}
			if (!agents.get(serial).isAvailable()){
				wait(); //TODO : check how wait works
			}
			agents.get(serial).acquire();
		}
		return noMissing;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getNames(List<String> serials){
        List<String> agentsName = new ArrayList<>();
        for(String serial : serials){
        	agentsName.add(agents.get(serial).getName());
		}
	    return agentsName;
    }


}
