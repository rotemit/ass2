package bgu.spl.mics.application.passiveObjects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {

		private static class InventoryHolder {
			private static Inventory instance = new Inventory();
		}
		// with the key (the name of the gadget) the hashMap returns the number left of this gadget
		private ConcurrentHashMap<String, Gadget> inventory;

		/**
		 * Retrieves the single instance of this class.
		 */
		public static Inventory getInstance() {
			return InventoryHolder.instance;
		}

		private Inventory() {
			inventory = new ConcurrentHashMap<>();
		}

		/**
		 * Initializes the store inventory. This method adds all the items given to the store
		 * inventory.
		 * <p>
		 * @param inventory 	Data structure containing all data necessary for initialization
		 * 						of the inventory.
		 */
		public void load (String[] inventory ) {
			for (String gadget : inventory)
				this.inventory.put(gadget, new Gadget(gadget));
		}

	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public boolean getItem(String gadget){
		if ((inventory.get(gadget))==null || !inventory.get(gadget).isAvailable()){
			return false;
		}
		inventory.get(gadget).acquire();
		return true;
	}

	public Result checkIfAvailable(String gadget){
		if ((inventory.get(gadget))==null || !inventory.get(gadget).isAvailable()){
			return Result.NOT_AVAILABLE;
		}
		return Result.AVAILABLE;
	}

	public Gadget takeItem(String gadget){
		inventory.get(gadget).acquire();
		return inventory.get(gadget);
	}



	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<String> which is a
	 * list of all the of the gadgeds.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename) {
		JsonObject jsonObject = new JsonObject();
		JsonArray inventoryArray = new JsonArray();
		inventory.forEach((name, gadget)->inventoryArray.add(name));

		jsonObject.add("Inventory", inventoryArray);
		try {
			FileWriter file = new FileWriter(filename);
			file.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
			file.close();
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}




		/*
		List<String> toPrint = new LinkedList<>();
		// enters the name and amount of each entry in name_bookInfo
		inventory .forEach((name,gadget)-> toPrint.add(name));
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


}



