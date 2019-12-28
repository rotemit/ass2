/*
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
import java.io.FileNotFoundException;
import java.io.FileReader;

// This is the Main class of the application. You should parse the input file,
// create the different instances of the objects, and run the system.
//  In the end, you should output serialized objects.

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

//            JsonArray jsonServices = input.getAsJsonArray("services");
//            Integer mInstances = Integer.parseInt(String.valueOf(jsonServices.get(0)));
//            Integer mpInstances = Integer.parseInt(String.valueOf(jsonServices.get(1)));


            JsonObject jsonServices = input.getAsJsonObject("services");
            Integer mInstances = jsonServices.get("M").getAsInt();
            Integer mpInstances = jsonServices.get("Moneypenny").getAsInt();


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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
*/


package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.*;

import bgu.spl.mics.example.Creator;
import bgu.spl.mics.example.publishers.ExampleMessageSender;
import bgu.spl.mics.example.subscribers.ExampleBroadcastSubscriber;
import bgu.spl.mics.example.subscribers.ExampleEventHandlerSubscriber;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sun.corba.se.pept.encoding.InputObject;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

// This is the Main class of the application. You should parse the input file,
//  create the different instances of the objects, and run the system.
//  In the end, you should output serialized objects.

public class MI6Runner {
    static public CountDownLatch latch;

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("missing parameters");
            return;
        }
        MI6Runner runner = new MI6Runner();
        String inputInJson = null;
        try {
            //getting the input string, which is currently in Json format
            inputInJson = runner.readFile(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type founderListType = new TypeToken<InputInfo>(){}.getType();
        //converting the string containing the input as Json to an object, which we can retrieve the info from easily
        InputInfo info = gson.fromJson(inputInJson,founderListType);
        int numOfThreads = getNumOfThreads(info);
        latch = new CountDownLatch(numOfThreads);
        Inventory inventory = Inventory.getInstance();
        inventory.load(info.getInventory());   //init the singleton inventory with gadgets

        Squad squad = Squad.getInstance();
        squad.load(info.getSquad()); //init the squad singleton with agents

        TimeService timeService = new TimeService(info.getServices().getTime());

        //init all the subscribers
        runner.executeSubscribers(info);
        //waiting for all subscribers to initialize before starting timeService
        try {
            latch.await();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }


        Thread timeThread = new Thread(timeService);
        timeThread.run();
        Vector<Thread> vectorOfThreads = new Vector<>(numOfThreads);

        for (Thread thread:vectorOfThreads){
            try{
                thread.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        //writing outputs to file
        inventory.printToFile(args[1]);
        Diary diary= Diary.getInstance();
        diary.printToFile(args [2]);

    }

    /**
     * @param info the json object to retrieve the info from
     * @return the num of threads needed to be initialized
     */
    private static int getNumOfThreads(InputInfo info) {
        int numOfThreads = 1; //+1 because of Q
        numOfThreads += info.getServices().getM();
        numOfThreads += info.getServices().getMoneypenny();
        numOfThreads += info.getServices().getIntelligence().length;
        return numOfThreads;
    }

    /**
     * @param info the json object to pull relevant info from

     */

    private void executeSubscribers(InputInfo info) {
        //init Ms according to the amount of Ms given in inputInfo
        for (int i = 0; i < info.getServices().getM(); i++) {
            M m = new M(i);
            Thread mthread = new Thread(m);
            mthread.start();
        }
        //init moneypennies according to the amount of them given in inputInfo
        for (int i = 0; i < info.getServices().getMoneypenny(); i++) {
            Moneypenny mp = new Moneypenny(i);
            Thread mpthread = new Thread(mp);
            mpthread.start();
        }
        //init Intelligences
        for (int i = 0; i < info.getServices().getIntelligence().length; i++) {
            List<MissionInfo> missionInfos = info.getServices().getIntelligence()[i].getMissions();
            Intelligence intelligence = new Intelligence(i, missionInfos);
            Thread ithread = new Thread(intelligence);
            ithread.start();
        }
          //init Q
        Q q = new Q();
        Thread qthread = new Thread(q);
        qthread.start();
    }



//      @param fileName the name of the file which we should read from
//      @return a String which is the text of the file filename
//      @throws IOException


    public String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }


}


