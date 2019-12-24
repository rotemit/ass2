package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;

import java.util.List;

public class MissionReceivedEvent implements Event {
    private String missionName;
    private String gadget;
    private List<String> agentsList; //TODO check if this needs to be a list or a single agent (according to forum we can decide)

    public MissionReceivedEvent(String missionName, String gadget, List<String> agentsList) {
        this.missionName = missionName;
        this.gadget = gadget;
        this.agentsList = agentsList;
    }

    public String getMissionName() {
        return missionName;
    }

    public String getGadget(){
        return gadget;
    }

    public List<String> getAgentsList(){
        return agentsList;
    }



}