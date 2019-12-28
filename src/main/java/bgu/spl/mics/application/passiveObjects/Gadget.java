package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;

public class Gadget implements Serializable {
    private String gadgetName;
    private boolean available;

    public Gadget(String gadgetName){
        this.gadgetName = gadgetName;
        this.available = true;
    }

    public String getGadgetName(){
        return gadgetName;
    }

    public void acquire(){
        available = false;
    }

    public void release(){
        available = false;
    }

    public boolean isAvailable(){
        return available;
    }
}
