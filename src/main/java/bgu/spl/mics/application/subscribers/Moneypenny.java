package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.ReleaseAgentsEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MonneyPenneyAnswer;
import bgu.spl.mics.application.passiveObjects.Pair;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;


import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private int currentTimeTick;
	protected static AtomicInteger AllSerialsCount=new AtomicInteger(0);
	private int serialNumber;
	private Squad squad_instance;

	public Moneypenny() {
		super("MoneyPenny");
		serialNumber=AllSerialsCount.incrementAndGet();
		squad_instance=Squad.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeEvent(AgentsAvailableEvent.class, agentsSerialsEvent->
		{
			long start=System.currentTimeMillis();
			long afterGettingAgents=0;
			long afterGettingAgentsNames=0;
			long afterComplitingGettingAgents=0;
			long afterGettingAnswerFromMtoSendAgents=0;

			Thread t= Thread.currentThread();
			//System.out.println("MoneyPeny "+serialNumber+" got new message");
			int timeSpeed=100;
			if(agentsSerialsEvent.isMAlie()) {
				boolean ans = this.squad_instance.getAgents(agentsSerialsEvent.getAgentsList());
				afterGettingAgents = System.currentTimeMillis();
				if (ans) {
					List<String> theNames = this.squad_instance.getAgentsNames(agentsSerialsEvent.getAgentsList());
					afterGettingAgentsNames = System.currentTimeMillis();
					MonneyPenneyAnswer monneyPenneyAnswer = new MonneyPenneyAnswer(theNames, this.serialNumber);
					complete(agentsSerialsEvent, monneyPenneyAnswer);
					afterComplitingGettingAgents = System.currentTimeMillis();
					Integer status = null;

					try {
						if(agentsSerialsEvent.isMAlie()) {
							status = monneyPenneyAnswer.getStatus((agentsSerialsEvent.getExpiredTime() - this.currentTimeTick) * timeSpeed, TimeUnit.MILLISECONDS);
							afterGettingAnswerFromMtoSendAgents = System.currentTimeMillis();
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (status == null || status == -1||!agentsSerialsEvent.isMAlie()) {
						this.squad_instance.releaseAgents(agentsSerialsEvent.getAgentsList());
					} else {
						this.squad_instance.sendAgents(agentsSerialsEvent.getAgentsList(), agentsSerialsEvent.getMissionDuration() * timeSpeed);
					}
				} else {
					complete(agentsSerialsEvent, null);
				}
			}
			else
			{
				complete(agentsSerialsEvent, null);
			}
			long end=System.currentTimeMillis();
			//System.out.println(t.getName()+" afterGettingAgents; "+					(end-afterGettingAgents));
			//System.out.println(t.getName()+" afterGettingAgentsNames; "+			(end-afterGettingAgentsNames));
			//System.out.println(t.getName()+" afterComplitingGettingAgents; "+		(end-afterComplitingGettingAgents));
			//System.out.println(t.getName()+" afterGettingAnswerFromMtoSendAgents; "+(end-afterGettingAnswerFromMtoSendAgents));
			//System.out.println(t.getName()+" end "+									(end-start));
		});

		subscribeBroadcast(TickBroadcast.class,theBroad->
		{
			this.currentTimeTick=theBroad.getTick();
			//System.out.println("Moneypenny "+this.serialNumber+" ,Tick: "+this.currentTimeTick);
			if(currentTimeTick==theBroad.getMaxTicks())
			{
				terminate();
			}
		});
		
	}

	private void  saveDontUse()
	{
		subscribeEvent(AgentsAvailableEvent.class, agentsSerialsEvent->
		{
			boolean ans= this.squad_instance.getAgents(agentsSerialsEvent.getAgentsList());
			List<String> theNames= this.squad_instance.getAgentsNames(agentsSerialsEvent.getAgentsList());
			Pair<List<String>,Integer> newPair= new Pair(theNames,this.serialNumber);

			//complete(agentsSerialsEvent,newPair);
		});
		subscribeEvent(SendAgentsEvent.class, theEvent->
		{

			this.squad_instance.sendAgents(theEvent.getAgentsToSend(),theEvent.getTime());

			complete(theEvent,1);
		});
		subscribeEvent(ReleaseAgentsEvent.class, theEvet->
		{
			this.squad_instance.releaseAgents(theEvet.getagentsToRelease());
			complete(theEvet,1);
		});

	}

	@Override
	protected void print() {
		//System.out.println("MoneyPenny"+this.serialNumber+" thread initialized. starting thread");
	}


}
