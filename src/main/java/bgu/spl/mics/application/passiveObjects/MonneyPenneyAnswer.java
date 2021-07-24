package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MonneyPenneyAnswer {
    private Future<Integer> status;
    private List<String> agentsNames;
    private int MonneyPennySerialNumber;

    public MonneyPenneyAnswer(List<String> agentsNames, int MonneypennySerial)
    {
        this.agentsNames=agentsNames;
        this.status=new Future<>();
        this.MonneyPennySerialNumber=MonneypennySerial;
    }

    public Integer getStatus(int timeOut,TimeUnit timeUnit) throws InterruptedException {

        Thread t= Thread.currentThread();
        //System.out.println(t.getName()+" is waiting");

        return status.get(timeOut, timeUnit);


    }

    public synchronized void completeStatus(Integer integer)
    {
        this.status.resolve(integer);

    }

    public int getMonneyPennySerialNumber() {
        return MonneyPennySerialNumber;
    }

    public List<String> getAgentsNames() {
        return agentsNames;
    }
}
