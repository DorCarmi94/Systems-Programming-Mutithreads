package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private int tick;
    private int maxTicks;
    public TickBroadcast(int tick, int maxTicks)
    {
        this.tick=tick;
        this.maxTicks=maxTicks;
    }

    public int getTick() {
        return tick;
    }

    public int getMaxTicks() {
        return maxTicks;
    }
}
