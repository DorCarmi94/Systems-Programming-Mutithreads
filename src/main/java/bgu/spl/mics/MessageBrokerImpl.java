package bgu.spl.mics;

import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	/**
	 * Retrieves the single instance of this class.
	 */
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> messagesQueue; //	Subscriber to its queue
	private ConcurrentHashMap<Subscriber, Object> lockingSubscribersObjects;	//locking objects for each subscriber

	private ConcurrentHashMap<Class<? extends Event>, ConcurrentLinkedQueue<Subscriber>> eventSubscribesList; // From event -> all subscribers that can act on it
	private ConcurrentHashMap<Class<? extends Broadcast>, ConcurrentLinkedQueue<Subscriber>> broadcastSubscribesList; // From broadcast -> all subscribers that can act on it

	private ConcurrentHashMap<Message, Future> futureHashMap; // From message to its future object, so that in the end we can resolve the future object
	private final static MessageBrokerImpl theMessageBrokerSingleton = new MessageBrokerImpl();

	private MessageBrokerImpl()
	{
		messagesQueue= new ConcurrentHashMap<>();
		lockingSubscribersObjects= new ConcurrentHashMap<>();
		eventSubscribesList = new ConcurrentHashMap<>();
		broadcastSubscribesList = new ConcurrentHashMap<>();
		futureHashMap= new ConcurrentHashMap<>();

	}

	public static MessageBroker getInstance() {
		return theMessageBrokerSingleton;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		long a=System.currentTimeMillis();
		synchronized (type)
		{
			//This should be sync because it is illegal to add the same type twice to the map
			if(!this.eventSubscribesList.containsKey(type))
			{
				eventSubscribesList.put(type,new ConcurrentLinkedQueue<Subscriber>());
			}
		}
		//this can happen concurrent cause its just adding to a queue
		eventSubscribesList.get(type).add(m);

		long b=System.currentTimeMillis();
		//System.out.println("subscribeEvent:"+(b-a));
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		long a=System.currentTimeMillis();

		synchronized (type)
		{
			if(!this.broadcastSubscribesList.containsKey(type))
			{
				ConcurrentLinkedQueue<Subscriber> q = new ConcurrentLinkedQueue<>();
				broadcastSubscribesList.put(type,q);
			}
		}
		ConcurrentLinkedQueue<Subscriber> q=broadcastSubscribesList.get(type);
		q.add(m);
		long b=System.currentTimeMillis();
		//System.out.println("subscribeBroadcast:"+(b-a));

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Thread t= Thread.currentThread();
		Future<T> future=futureHashMap.get(e);
		//if(result==null)
			//System.out.printf("thread t result null: "+t.getName());
		this.futureHashMap.get(e).resolve(result);

	}

	@Override
	public void sendBroadcast(Broadcast b){
		long a=System.currentTimeMillis();
		for (Subscriber s: this.broadcastSubscribesList.get(b.getClass())) {
			try {
				synchronized (this.lockingSubscribersObjects.get(s)) {
					this.messagesQueue.get(s).put(b);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long bb=System.currentTimeMillis();
		//System.out.println("sendBroadcast:"+(bb-a));
	}
	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		long a=System.currentTimeMillis();
		//Looking for the queue of all the subscribers that can handle this kind of event-> syb_queue
		ConcurrentLinkedQueue<Subscriber> sub_queue= this.eventSubscribesList.get(e.getClass());
		if(sub_queue==null)
		{
			//No such message respond
			return null;
		}
		//looking for the first subscriber, pulling him out and adding again
		Subscriber s;
		synchronized (sub_queue) {
			s= sub_queue.poll();
			if (s == null) {
				//no suitable subscribers
				return null;
			}
			sub_queue.add(s);
		}
		Future<T> newFuture = new Future<T>();
		this.futureHashMap.put(e, newFuture);
		try {
			//enter to subscriber's queue
			//Should be sync because on SendEvent we don't want two publishers to add an event to the same subscriber's queue

			synchronized (this.lockingSubscribersObjects.get(s)) {
				LinkedBlockingQueue<Message> currentMessagesQueue=this.messagesQueue.get(s);
				currentMessagesQueue.put(e);

			}

		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		long bb=System.currentTimeMillis();
		//System.out.println("sendEvent:"+(bb-a));
		return newFuture;

	}

	@Override
	public void register(Subscriber m) {
		this.messagesQueue.put(m,new LinkedBlockingQueue<Message>());
		this.lockingSubscribersObjects.put(m,new Object());
	}

	@Override
	public void unregister(Subscriber m) {
		long a= System.currentTimeMillis();
			synchronized (this.lockingSubscribersObjects.get(m)) {
				if (this.messagesQueue.get(m).size() > 0) {
					for (Message message : this.messagesQueue.get(m)) {
						if (futureHashMap.contains(message)) {
							futureHashMap.get(message).resolve(null);
							futureHashMap.remove(message);
						}
					}
				}
				//System.out.println("deleting m's queue");
				messagesQueue.remove(m);

			}

		//System.out.println("deleting m's locking object");
			lockingSubscribersObjects.remove(m);



		//Remove From EventList

		for (Class<? extends Event> e: this.eventSubscribesList.keySet() ) {
			synchronized (e)
			{
				for (Subscriber sub:eventSubscribesList.get(e)) {
					if(sub==m)
					{
						//System.out.println("removing: "+m.getName()+" from event subscribeList");
						eventSubscribesList.get(e).remove(m);
					}
				}
			}
		}

		//Remove From Broadcast
		for (Class<? extends Broadcast> e: this.broadcastSubscribesList.keySet() ) {
			synchronized (e)
			{
				for (Subscriber sub:broadcastSubscribesList.get(e)) {
					if(sub==m)
					{
						broadcastSubscribesList.get(e).remove(m);
					}
				}
			}
		}
		long bb=System.currentTimeMillis();
		//System.out.println("unregister:"+(bb-a));
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		Thread thread=Thread.currentThread();
		boolean ans= this.messagesQueue.contains(m);
		if(!this.messagesQueue.containsKey(m))
		{
			throw new IllegalStateException("Subscriber didn't register");
		}

		Message message=this.messagesQueue.get(m).take(); //Waiting inside the poll until there is a message to get


		return message;

	}

//	public void clear(){
//		this.broadcastSubscribesList.clear();
//		this.eventSubscribesList.clear();
//		this.futureHashMap.clear();
//		this.lockingSubscribersObjects.clear();
//		this.messagesQueue.clear();
//		synchronized (this) {
//			notifyAll();
//		}
//	}

}
