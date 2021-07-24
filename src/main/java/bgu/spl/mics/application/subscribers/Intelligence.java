package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private static int SerialNumers=1;
	private int currentTick;
	private int serialNumber;
	private List<MissionInfo> missionInfos;
	public Intelligence(List<MissionInfo> missionInfos) {
		super("Intelligence");
		this.serialNumber=SerialNumers;
		SerialNumers++;
		this.missionInfos=missionInfos;
	}


	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class,theBroad->
		{
			this.currentTick=theBroad.getTick();
			//System.out.println("///*** intelligence got tick "+this.currentTick+" ***///");

			if(this.currentTick==theBroad.getMaxTicks())
			{
				terminate();
			}
			sendNextEvent();
		});
	}

	private void sendNextEvent() {
		long a=System.currentTimeMillis();
		Thread t= Thread.currentThread();
		for (int i = 0;!super.isTerminated() && i < missionInfos.size(); i++) {
			if(missionInfos.get(i).getTimeIssued()==this.currentTick)
				this.getSimplePublisher().sendEvent(new MissionReceivedEvent(missionInfos.get(i)));
		}
		long b=System.currentTimeMillis();

		//for (MissionInfo mission: this.missionInfos) {
			//System.out.println("here- intelisendevent");
			//if(mission.getTimeIssued()==this.currentTick) {

				//System.out.println("Intelligence "+serialNumber+" sending new event");
				//System.out.println("Mission name: "+mission.getMissionName());
			//}

		//}
	}

	@Override
	protected void print() {
		//System.out.println("\nIntelligence "+this.serialNumber+" was initialised\n");
	}
}
