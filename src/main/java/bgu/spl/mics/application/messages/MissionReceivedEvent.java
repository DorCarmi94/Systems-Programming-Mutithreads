package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

public class MissionReceivedEvent implements Event {
    private MissionInfo theMission;
    private int timeCreated;
    public MissionReceivedEvent(MissionInfo missionInfo)
    {
        this.theMission=missionInfo;
    }

    public MissionInfo getTheMission()
    {
        return this.theMission;
    }

    public int getTimeCreated() {
        return timeCreated;
    }
}
