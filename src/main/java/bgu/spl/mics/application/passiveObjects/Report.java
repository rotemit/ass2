package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {
	//******************fields
	private String missionName;
	private int mID;
	private int moneypennyID;
	private List<String> agentsSerialNumbers;
	private List<String> agentsNames;
	private String gadgetName;
	private int qTime;
	private int timeIssued;
	private int timeCreated;

	//*********************constructors
	public Report(String missionName, int mID,int moneypennyID,List<String> agentsSerialNumbers,
				  List<String> agentsNames,String gadgetName,int qTime, int timeIssued,int timeCreated){
		this.missionName = missionName;
		this.mID= mID;
		this.moneypennyID = moneypennyID;
		this.agentsSerialNumbers = agentsSerialNumbers ;
		this.agentsNames = agentsNames;
		this.gadgetName=  gadgetName;
		this.qTime = qTime;
		this.timeIssued = timeIssued;
		this.timeCreated = timeCreated;
	}

	public String toString() {
		String agentsSerialNum = "";
		for (String serialNum : agentsSerialNumbers)
			agentsSerialNum += "\"" + serialNum + "\"" + "\n";

		String agentsName = "";
		for (String name : agentsNames)
			agentsName += "\"" + name + "\"" + "\n";

		String toreturn = "{" + "\n" + "\"missionName\" : " + "\"" + this.missionName + "\"" + "\n" +
				"\"m\" :" + "\"" + this.mID + "\"" + "\n" +
				"\"moneypenny\" : " + "\"" + this.moneypennyID + "\"" + "\n" +
				"\"agentsSerialNumbers\" : [" + "\n" + agentsSerialNum + "\n" + "]," + "\n" +
				"\"agentsNames\" : [" + "\n" + agentsName + "\n" + "]," + "\n" +
				"\"gadgetName\" : " + "\"" + this.gadgetName + "\"" + "\n" +
				"\"timeCreated\" : " + "\"" + this.timeCreated + "\"" + "\n" +
				"\"timeIssued\" : " + "\"" + this.timeIssued + "\"" + "\n" +
				"\"qTime\" : " + "\"" + this.qTime + "\"" + "\n" + "}" + "\n";

		return toreturn;
	}



	/**
	 * Retrieves the mission name.
	 */
	public String getMissionName() {
		return missionName;
	}

	/**
	 * Sets the mission name.
	 */
	public void setMissionName(String missionName) {
		this.missionName = missionName;
	}

	/**
	 * Retrieves the M's id.
	 */
	public int getM() {
		return mID;
	}

	/**
	 * Sets the M's id.
	 */
	public void setM(int m) {
		mID = m;
	}

	/**
	 * Retrieves the Moneypenny's id.
	 */
	public int getMoneypenny() {
		return moneypennyID;
	}

	/**
	 * Sets the Moneypenny's id.
	 */
	public void setMoneypenny(int moneypenny) {
		moneypennyID = moneypenny;
	}

	/**
	 * Retrieves the serial numbers of the agents.
	 * <p>
	 * @return The serial numbers of the agents.
	 */
	public List<String> getAgentsSerialNumbers() {
		return agentsSerialNumbers;
	}

	/**
	 * Sets the serial numbers of the agents.
	 */
	public void setAgentsSerialNumbers(List<String> agentsSerialNumbersNumber) {
		for (int i=0; i<agentsSerialNumbersNumber.size(); i++){
			agentsSerialNumbers.add(agentsSerialNumbersNumber.get(i));
		}
	}

	/**
	 * Retrieves the agents names.
	 * <p>
	 * @return The agents names.
	 */
	public List<String> getAgentsNames() {
		return agentsNames;
	}

	/**
	 * Sets the agents names.
	 */
	public void setAgentsNames(List<String> agentsNames) {
		for (int i=0; i<agentsNames.size(); i++){
			this.agentsNames.add(agentsNames.get(i));
		}
	}

	/**
	 * Retrieves the name of the gadget.
	 * <p>
	 * @return the name of the gadget.
	 */
	public String getGadgetName() {
		return gadgetName;
	}

	/**
	 * Sets the name of the gadget.
	 */
	public void setGadgetName(String gadgetName) {
		this.gadgetName = gadgetName;
	}

	/**
	 * Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public int getQTime() {
		return qTime;
	}

	/**
	 * Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public void setQTime(int qTime) {
		this.qTime = qTime;
	}

	/**
	 * Retrieves the time when the mission was sent by an Intelligence Publisher.
	 */
	public int getTimeIssued() {
		return timeIssued;
	}

	/**
	 * Sets the time when the mission was sent by an Intelligence Publisher.
	 */
	public void setTimeIssued(int timeIssued) {
		this.timeIssued = timeIssued;
	}

	/**
	 * Retrieves the time-tick when the report has been created.
	 */
	public int getTimeCreated() {
		return timeCreated;
	}

	/**
	 * Sets the time-tick when the report has been created.
	 */
	public void setTimeCreated(int timeCreated) {
		this.timeCreated = timeCreated;
	}

	/*
	public JsonObject toJson() {

		JsonArray serialsNumber = new JsonArray();
		for ( String serial : agentsSerialNumbers)
			serialsNumber.add(serial);

		JsonArray names = new JsonArray();
		for ( String name : agentsNames)
			names.add(name);

		JsonObject report = new JsonObject();
		report.addProperty("missionName", missionName);
		report.addProperty("M", mID);
		report.addProperty("MoneyPenny", moneypennyID);
		report.add("agentsSerialNumbers", serialsNumber);
		report.add("agentsName", names);
		report.addProperty("timeIssued", timeIssued);
		report.addProperty("QTime", qTime);
		report.addProperty("timeCreated", timeCreated);

		return report;
	} */

}