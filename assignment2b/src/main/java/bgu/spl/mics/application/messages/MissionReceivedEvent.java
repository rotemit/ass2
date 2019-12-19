package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;

public class MissionReceivedEvent implements Event {
    private String missionName;

    public MissionReceivedEvent(String missionName) {
        this.missionName = missionName;
    }

    public String getMissionName() {
        return missionName;
    }

}
