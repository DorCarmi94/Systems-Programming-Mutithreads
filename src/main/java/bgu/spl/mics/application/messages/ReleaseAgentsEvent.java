package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class ReleaseAgentsEvent implements Event {
    private List<String> agentsToRelease;
    public ReleaseAgentsEvent(List<String> agentsToRelease)
    {
        this.agentsToRelease=agentsToRelease;
    }

    public List<String> getagentsToRelease() {
        return agentsToRelease;
    }

}
