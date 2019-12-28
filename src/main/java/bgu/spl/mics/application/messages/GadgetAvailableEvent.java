package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;

public class GadgetAvailableEvent implements Event {


    private String gadgetName;

    public GadgetAvailableEvent(String gadgetName) {
        this.gadgetName = gadgetName;
    }

    public String getGadgetName() {
        return gadgetName;
    }


}
