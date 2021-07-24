package bgu.spl.mics.application.publishers;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	private AtomicInteger currentTimeTick;
	private int duration;
	private int speed;
	private Timer timer;
	Thread t;
	public TimeService(int speed,int duration) {
		super("TimeService");
		this.currentTimeTick= new AtomicInteger();
		this.currentTimeTick.set(1);
		//this.timer=new Timer("timer");
		this.speed=speed;
		this.duration=duration;

	}

	@Override
	protected void initialize() {
		//t= Thread.currentThread();

		/*
		TimerTask task= new TimerTask() {
			@Override
			public void run() {
				t.run();
				if(currentTimeTick.get()<=duration)
				{
					getSimplePublisher().sendBroadcast(new TickBroadcast(currentTimeTick.get(),duration));
					System.out.println("\n");
					System.out.println("Tick: "+currentTimeTick);
					System.out.println();
					currentTimeTick.incrementAndGet();
				}
				else
				{
					timer.cancel();
					timer.purge();

					t.interrupt();
				}
			}
		};
		timer.scheduleAtFixedRate(task,0,speed);
		*/
		
	}

	@Override
	public void run() {
		initialize();
		//System.out.println("\nClock thread finished initialization. Starting the clock count\n");
		//Thread newThread= new Thread();
		while(this.currentTimeTick.get()<=duration) {
			this.getSimplePublisher().sendBroadcast(new TickBroadcast(currentTimeTick.get(), duration));
			try {
				t=Thread.currentThread();
				//System.out.println("\nTick: " +currentTimeTick+"\n");
				t.sleep(speed);
				this.currentTimeTick.incrementAndGet();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//newThread.interrupt();
		MessageBroker messageBroker= MessageBrokerImpl.getInstance();
		//System.out.println("////////Clock finished////////////");
	}

}
