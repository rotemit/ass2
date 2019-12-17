package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;


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
	List<Report> reports;
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
		reports = new LinkedList<>();
		totalNumberOfReceivedMissions = 0;
	}


	public List<Report> getReports() {
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		reports.add(reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	//should print all the reports and the number of received missions
	public void printToFile(String filename){
		Gson gson = new Gson();
		// need to open a file to write in

		//"file.write(s1)"+same for s2
		String s1 = gson.toJson(reports);
		String s2 = gson.toJson(totalNumberOfReceivedMissions);
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
