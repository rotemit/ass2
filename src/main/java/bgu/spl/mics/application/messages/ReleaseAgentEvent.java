package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class ReleaseAgentEvent implements Event {
    List<String> agentsName;

    public  ReleaseAgentEvent(List<String> agentsName) {
        this.agentsName = agentsName;
    }

    public List<String> getAgentsName() {
        return agentsName;
    }
}
