package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Gadget;
//TODO : not sure its necessary

public class TakeGadgetEvent  implements Event {
    private String gadgetName;

    public  TakeGadgetEvent(String gadgetName) {
        this.gadgetName = gadgetName;
    }

    public String getName() {
        return gadgetName;
    }
}
