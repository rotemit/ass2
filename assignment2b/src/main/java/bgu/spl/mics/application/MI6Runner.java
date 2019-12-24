package bgu.spl.mics.application;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {

        try {
            String filename = args[0];
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(filename));
            JsonObject input = gson.fromJson(reader, JsonObject.class);


            //initialize Inventory
            JsonArray jsonInventory = input.getAsJsonArray("inventory");
            String[] inventoryArrayFromJson = gson.fromJson(jsonInventory, String[].class);
//            String[] inventoryArrayFromJson = gson.fromJson("inventory", String[].class);
            Inventory.getInstance().load(inventoryArrayFromJson);

            //initialize Ms and Moneypennys
            //TODO check which strategy works for reading the data inside the "services" JsonObject
            /*
            JsonArray jsonServices = input.getAsJsonArray("services");
            Integer mInstances = Integer.parseInt(String.valueOf(jsonServices.get(0)));
            Integer mpInstances = Integer.parseInt(String.valueOf(jsonServices.get(1)));
            */

            JsonObject jsonServices = input.getAsJsonObject("services");
            Integer mInstances = jsonServices.get("M").getAsInt();
            Integer mpInstances = jsonServices.get("MoneyPenny").getAsInt();


            //initialize Ms
            for (int i = 0; i < mInstances; i++) {
                M newm = new M(i);
                MessageBrokerImpl.getInstance().register(newm);
//                newm.run();
            }
            //init moneypennys
            for (int i = 0; i < mpInstances; i++) {
                Moneypenny newmp = new Moneypenny(i);
                MessageBrokerImpl.getInstance().register(newmp);
            }

            //initialize intelligences according to json data\
            //create an array of intelligences, according to the data inside "services"
            JsonArray intelligenceInServices = jsonServices.getAsJsonArray("intelligence");

            //TODO next line might be buggy, maybe the needed command is getasjsonarray
            //different option for creating intelligenceInServices array
//            JsonArray intelligenceInServices = (JsonArray) jsonServices.get(2);



            for (int i = 0; i < intelligenceInServices.size(); i++) {
                Intelligence currIntelligence = new Intelligence();
//                JsonArray currIntelligenceData = (JsonArray) intelligenceInServices.get(i);
                JsonObject currIntelligenceData = intelligenceInServices.get(i).getAsJsonObject();
//                JsonArray missionsArrayInCurr = (JsonArray) currIntelligenceData.get(0);
                JsonArray missionsArrayInCurr = currIntelligenceData.getAsJsonArray("missions");
                for (int j = 0; j < missionsArrayInCurr.size(); j++) {
                    JsonArray currMission = (JsonArray) missionsArrayInCurr.get(j);

                    //initialize new MissionInfo object to be added to currIntelligence
                    MissionInfo currMissionInfo = new MissionInfo();
                    //init serialagentsnumbers

                    currMissionInfo.setDuration(currMission.get(1).getAsInt());
                    currMissionInfo.setGadget(currMission.get(2).getAsString());
                    currMissionInfo.setMissionName(currMission.get(3).getAsString());
                    currMissionInfo.setTimeExpired(currMission.get(4).getAsInt());
                    currMissionInfo.setTimeIssued(currMission.get(5).getAsInt());
                    //add currMissionInfo to currIntelligence
                    currIntelligence.addMission(currMissionInfo);

                }
            }
            //initialize Intelligence
//            JsonArray intelligencesArray = jsonServices.getAsJsonArray("intelligence");
            for (int i = 0; i < jsonServices.size(); i++) {
                JsonObject currIntelligence = jsonServices.get(i).getAsJsonObject();
                Intelligence
                for (int j = 0; j)
            }

            JsonObject jsonIntelligences = jsonServices.get(0).getAsJsonObject();
            JsonArray intelligencesArray
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




