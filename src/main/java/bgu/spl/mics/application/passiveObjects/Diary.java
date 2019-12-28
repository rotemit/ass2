package bgu.spl.mics.application.passiveObjects;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {

	//************************fields
	private static Diary instance = null;
	private HashMap<String ,Report> diary;
	private int totalNumberOfReceivedMissions;

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		if (instance == null){
			instance = new Diary();
		}
		return instance;
	}

	//*********************private constructor
	private Diary(){
		diary = new HashMap<>();
		totalNumberOfReceivedMissions = 0;

	}


	public List<Report> getDiary() {
		List<Report> reportsList = new LinkedList<>();
		for(Report r : diary.values()){
			reportsList.add(r);
		}
		return reportsList;
	}

	public List<String> getMissionAgents (String missionName){
		Report curr = diary.get(missionName);
		List<String> agentsSerialsNumbers = curr.getAgentsSerialNumbers();
		return agentsSerialsNumbers;
	}


	public String getMissionGadget(String mission) {
		return diary.get(mission).getGadgetName();
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		diary.put(reportToAdd.getMissionName(), reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	//should print all the reports and the number of received missions

	public void printToFile(String filename) throws IOException {
		/*
		JsonArray reports = new JsonArray();
		for (Map.Entry<String, Report> entry : diary.entrySet())
			reports.add(String.valueOf(entry.getValue()));

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("total", totalNumberOfReceivedMissions);
		jsonObject.add("reports", reports);

		FileWriter file = new FileWriter(filename);
		file.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
		file.close(); */


		JsonObject toPrint = new JsonObject();
		toPrint.addProperty("total", totalNumberOfReceivedMissions);
		JsonArray reportArray = new JsonArray();


		for (Map.Entry<String, Report> e: diary.entrySet()){
			Report currReport = e.getValue();
			JsonArray agentsSerials = new JsonArray();
			for ( String serial : currReport.getAgentsSerialNumbers()) {
				agentsSerials.add(serial);
			}
			JsonArray names = new JsonArray();
			for ( String name : currReport.getAgentsNames()) {
				names.add(name);
			}
			//create a jsonobject containing current report
			JsonObject currReportObject = new JsonObject();
			//add the jsonobject to jsonarray
			currReportObject.addProperty("missionName", currReport.getMissionName());
			currReportObject.addProperty("M", currReport.getM());
			currReportObject.addProperty("MoneyPenny", currReport.getMoneypenny());
			currReportObject.add("agentsSerialNumbers", agentsSerials);
			currReportObject.add("agentsName", names);
			currReportObject.addProperty("timeIssued", currReport.getTimeIssued());
			currReportObject.addProperty("QTime", currReport.getQTime());
			currReportObject.addProperty("timeCreated", currReport.getTimeCreated());
			reportArray.add(currReportObject);
		}
		toPrint.add("reports", reportArray);
		String toJson = toPrint.toString();
		try {
//			FileWriter fileWriter = new FileWriter(filename);
//			fileWriter.write(toPrint.toJSONString());
//			fileWriter.flush();
//			fileWriter.write(toJson);
			FileWriter file = new FileWriter(filename);
			file.write(new GsonBuilder().setPrettyPrinting().create().toJson(toPrint));
			file.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}

//		file.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
//
//
//		for (Map.Entry<String, Report> entry : diary.entrySet()){
//			String key = entry.getKey();
//			Report value = entry.getValue();
//			reportArray.add(value);
//		}
//		diary.forEach((k,v)->reportArray.add(v));
//
//		inventory.forEach((name, gadget)->inventoryArray.add(name));
//		for ( Report report : diary)
//			reports.add(report.toJson());
//
//
//
//		FileWriter file = new FileWriter(filename);
//		file.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
//		file.close();


		/*
		List<String> toPrint = new LinkedList<>();
		// enters the name and amount of each entry in name_bookInfo
		diary.forEach((name,report)-> toPrint.add(report.toString()));
		try
		{
			FileOutputStream fos =
					new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(toPrint);
			oos.close();
			fos.close();
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		} */
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return totalNumberOfReceivedMissions;
	}

	public void increment(){
		totalNumberOfReceivedMissions++;
	}

}