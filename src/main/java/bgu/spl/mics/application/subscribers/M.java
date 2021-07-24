package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.Message;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	private static AtomicInteger serialNumbers=new AtomicInteger(0);
	private static int countActiveMs=0;
	private int serialNumber;
	private AtomicInteger currentTick;
	private Queue<Integer> ticks= new LinkedList<>();
	private Diary diary;
	private Stack<MonneyPenneyAnswer> uncompletedAnswers;
	private Stack<AgentsAvailableEvent> messagesIsent;
	public M() {
		super("M");
		serialNumber=serialNumbers.incrementAndGet();
		diary=Diary.getInstance();
		currentTick=new AtomicInteger(0);
		uncompletedAnswers=new Stack<MonneyPenneyAnswer>();
		messagesIsent=new Stack<>();
	}

	@Override
	protected void initialize() {

		//System.out.println("Initializing M: "+this.serialNumber);

		subscribeEvent(MissionReceivedEvent.class,theEvent->
		{
			long a=System.currentTimeMillis();
			Thread t= Thread.currentThread();
			Squad s= Squad.getInstance();
			diary.incrementTotal();
			MonneyPenneyAnswer monneyPenneyAnswer;
			int timeSpeed=100; //milliseconds
			//System.out.println("\nM "+serialNumber+" got new Event\n");
			//System.out.println("\nM "+serialNumber+" sending new AgentsAvailableEvent\n");
			AgentsAvailableEvent newEventToSend= new AgentsAvailableEvent(theEvent.getTheMission().getSerialAgentsNumbers(),theEvent.getTheMission().getTimeExpired(),theEvent.getTheMission().getDuration());

			Future<MonneyPenneyAnswer> agentsReady= this.getSimplePublisher().sendEvent(newEventToSend);
			messagesIsent.push(newEventToSend);
			if(agentsReady!=null) {
				monneyPenneyAnswer = agentsReady.get((theEvent.getTheMission().getTimeExpired()- currentTick.get()) * timeSpeed, TimeUnit.MILLISECONDS);
				if (monneyPenneyAnswer != null) {
					messagesIsent.pop();
					uncompletedAnswers.push(monneyPenneyAnswer);
					List<String> names = monneyPenneyAnswer.getAgentsNames();
					if (names != null) {
						//System.out.println("\nM " + serialNumber + " got approve that the agents are aquired for the mission from moneypenny: " + monneyPenneyAnswer.getMonneyPennySerialNumber() + "\n");
						//System.out.println("\nM " + serialNumber + " sending request for gadget: " + theEvent.getTheMission().getGadget() + "\n");
						Future<Integer> getGadget = this.getSimplePublisher().sendEvent(new GadgetAvailableEvent(theEvent.getTheMission().getGadget()));
						if (getGadget != null) {
							//if(theEvent.getTheMission().getMissionName().equals("Thunderball2"))
								//System.out.println("");
							Integer ans = getGadget.get((theEvent.getTheMission().getTimeExpired() - currentTick.get()) * timeSpeed, TimeUnit.MILLISECONDS);
							//System.out.println("\nM " + serialNumber + " got approve that the gadeget is ready for the mission");
							if (ans != -1) {
								//Both gadget and agents are avialable
								if (this.currentTick.get() <= theEvent.getTheMission().getTimeExpired()) {
									monneyPenneyAnswer.completeStatus(1);
									this.uncompletedAnswers.pop();

									//Create report
									Report report = new Report();
									MissionInfo missionInfo = theEvent.getTheMission();
									report.setAgentsNames(names);
									report.setAgentsSerialNumbers(missionInfo.getSerialAgentsNumbers());
									report.setM(this.serialNumber);
									report.setGadgetName(missionInfo.getGadget());
									report.setMoneypenny(monneyPenneyAnswer.getMonneyPennySerialNumber());
									report.setMissionName(missionInfo.getMissionName());
									report.setTimeCreated(theEvent.getTimeCreated());// I think it is not
									report.setTimeIssued(missionInfo.getTimeIssued());
									report.setQTime(ans);
									diary.addReport(report);
								} else {

									complete(theEvent, null);
									//System.out.println("A");
									monneyPenneyAnswer.completeStatus(-1);
									//System.out.println("Aa");
								}
							} else {
								//The gadget is not available
								//System.out.println("B");
								complete(theEvent, null);
								monneyPenneyAnswer.completeStatus(-1);
								//System.out.println("Bb");

							}
						} else {
							//System.out.println("\nM \"+serialNumber+\": Problem with the gadget: "+theEvent.getTheMission().getGadget()+"\n");
							//System.out.println("C");
							complete(theEvent, null);
							monneyPenneyAnswer.completeStatus(-1);
							//System.out.println("Cc");
						}
					}
					else
					{
						//names is null
						//System.out.println("\nM \"+serialNumber+\": Problem with the agents, event wasn't clsoed successfuly\n");
						//the agents aren't available
						//System.out.println("D");
						complete(theEvent, null);
						monneyPenneyAnswer.completeStatus(-1);
						//System.out.println("Dd");
					}

				}
				else {
					//monneyPennyAnswer is null
					//System.out.println("E");
					//System.out.println("\nM \"+serialNumber+\": Problem with the agents, event wasn't clsoed successfuly\n");
					//the agents aren't available
					complete(theEvent, null);
					//System.out.println("Ee");
				}
			}

			else
			{
				//agentsReady is null
				//System.out.println("F");
				//System.out.println("\nM "+serialNumber+": Problem with the agents, event wasn't clsoed successfuly\n");
				complete(theEvent,null);
				//System.out.println("Ff");
			}

			long b=System.currentTimeMillis();
			//System.out.println(t.getName()+" event handle: "+(b-a));
		});

		subscribeBroadcast(TickBroadcast.class,theBroad->
		{

			int oldVal;
			do {
				oldVal=this.currentTick.get();
			}while(!this.currentTick.compareAndSet(oldVal,theBroad.getTick()));
			ticks.add(theBroad.getTick());

			if(this.currentTick.get()==theBroad.getMaxTicks()) {
				//System.out.println(Thread.currentThread().getName()+ " - Uncompleted monneypenny answers: "+uncompletedAnswers.size());
				while (!this.uncompletedAnswers.empty())
					this.uncompletedAnswers.pop().completeStatus(null);
				while (!this.messagesIsent.empty())
					this.messagesIsent.pop().MisDead();
				terminate();
			}
		});

		
	}

	@Override
	protected void print() {
		//System.out.println("\nM "+this.serialNumber+" thread initialized. starting thread\n");
	}
}
