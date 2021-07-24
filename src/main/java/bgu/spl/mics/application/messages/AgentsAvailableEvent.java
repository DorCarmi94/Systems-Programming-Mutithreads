package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.MonneyPenneyAnswer;
import bgu.spl.mics.application.passiveObjects.Pair;

import java.util.List;

public class AgentsAvailableEvent implements Event<MonneyPenneyAnswer> {

    private List<String> agnetsSerials;
    private int expiredTime;
    private int missionDuration;
    private boolean isMAlie;

    public AgentsAvailableEvent(List<String> agnetsSerials, int expiredTimetime, int missionDuration)
    {
        this.agnetsSerials= agnetsSerials;
        this.expiredTime=expiredTimetime;
        this.missionDuration=missionDuration;
        isMAlie=true;
    }

    public List<String> getAgentsList()
    {
        return this.agnetsSerials;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public int getMissionDuration() {
        return missionDuration;
    }

    public boolean isMAlie() {
        return isMAlie;
    }

    public void MisDead()
    {
        this.isMAlie=false;
    }
}
