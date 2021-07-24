package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.publishers.TimeService;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private Inventory inventory;
	private int Qtime;
	public Q() {
		super("Q");
		inventory=Inventory.getInstance();

	}

	@Override
	protected void initialize() {
		subscribeEvent(GadgetAvailableEvent.class,theEvent->
		{
			long a=System.currentTimeMillis();
			Thread t= Thread.currentThread();
			boolean ans= this.inventory.getItem(theEvent.getGadgetName());
			if(ans)
			{
				complete(theEvent,Qtime);
			}
			else
			{
				complete(theEvent,-1);
			}
			long b=System.currentTimeMillis();
			//System.out.println(t.getName()+" event handle: "+(b-a));
		});


		subscribeBroadcast(TickBroadcast.class,theBroad->
		{
			this.Qtime=theBroad.getTick();
			if(this.Qtime==theBroad.getMaxTicks())
				terminate();
		});
		
	}

	@Override
	protected void print() {
		//System.out.println("Q thread initialized. starting thread");
	}
}
