package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class GadgetAvailableEvent implements Event<Integer> {
    private String gadget;
    public GadgetAvailableEvent(String gadgetName)
    {
        this.gadget=gadgetName;
    }
    public String getGadgetName()
    {
        return this.gadget;
    }


}
