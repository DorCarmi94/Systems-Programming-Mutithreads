package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentsEvent implements Event {
    private List<String> agentsToSend;
    private int time;
    public SendAgentsEvent(List<String> agentsToSend, int time)
    {
        this.agentsToSend=agentsToSend;
        this.time=time;
    }

    public List<String> getAgentsToSend() {
        return agentsToSend;
    }

    public int getTime() {
        return time;
    }
}
