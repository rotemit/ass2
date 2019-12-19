package bgu.spl.mics.application.messages;


import bgu.spl.mics.Event;

import java.util.List;

//TODO : not sure its necessary
public class TakeAgentsEvent implements Event {
    List<String> agentsName;

    public  TakeAgentsEvent(List<String> agentsName) {
        this.agentsName = agentsName;
    }

    public List<String> getAgentsName() {
        return agentsName;
    }
}
