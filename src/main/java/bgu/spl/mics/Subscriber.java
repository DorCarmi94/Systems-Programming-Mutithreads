package bgu.spl.mics;

import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.HashMap;

/**
 * The Subscriber is an abstract class that any subscriber in the system
 * must extend. The abstract Subscriber class is responsible to get and
 * manipulate the singleton {@link MessageBroker} instance.
 * <p>
 * Derived classes of Subscriber should never directly touch the MessageBroker.
 * the derived class also supplies a {@link Callback} that should be called when
 * a message of the subscribed type was taken from the Subscriber
 * message-queue (see {@link MessageBroker#register(Subscriber)}
 * method). The abstract Subscriber stores this callback together with the
 * type of the message is related to.
 * 
 * Only private fields and methods may be added to this class.
 * <p>
 */
public abstract class Subscriber extends RunnableSubPub {
    private boolean terminated = false;
    private MessageBroker instace;
    private HashMap<Class<? extends Message>,Callback> messageCallBack;
    private boolean subscribedEvent;

    /**
     * @param name the Subscriber name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public Subscriber(String name) {
        super(name);
        subscribedEvent=false;
        instace= MessageBrokerImpl.getInstance();
        messageCallBack= new HashMap<>();
    }

    /**
     * Subscribes to events of type {@code type} with the callback
     * {@code callback}. This means two things:
     * 1. Subscribe to events in the singleton MessageBroker using the supplied
     * {@code type}
     * 2. Store the {@code callback} so that when events of type {@code type}
     * are received it will be called.
     * <p>
     * For a received message {@code m} of type {@code type = m.getClass()}
     * calling the callback {@code callback} means running the method
     * {@link Callback#call(java.lang.Object)} by calling
     * {@code callback.call(m)}.
     * <p>
     * @param <E>      The type of event to subscribe to.
     * @param <T>      The type of result expected for the subscribed event.
     * @param type     The {@link Class} representing the type of event to
     *                 subscribe to.
     * @param callback The callback that should be called when messages of type
     *                 {@code type} are taken from this Subscriber message
     *                 queue.
     */
    protected final <T, E extends Event<T>> void subscribeEvent(Class<E> type, Callback<E> callback) {
        subscribedEvent=true;
        instace.subscribeEvent(type,this);
        this.messageCallBack.put(type,callback);
    }

    /**
     * Subscribes to broadcast message of type {@code type} with the callback
     * {@code callback}. This means two things:
     * 1. Subscribe to broadcast messages in the singleton MessageBroker using the
     * supplied {@code type}
     * 2. Store the {@code callback} so that when broadcast messages of type
     * {@code type} received it will be called.
     * <p>
     * For a received message {@code m} of type {@code type = m.getClass()}
     * calling the callback {@code callback} means running the method
     * {@link Callback#call(java.lang.Object)} by calling
     * {@code callback.call(m)}.
     * <p>
     * @param <B>      The type of broadcast message to subscribe to
     * @param type     The {@link Class} representing the type of broadcast
     *                 message to subscribe to.
     * @param callback The callback that should be called when messages of type
     *                 {@code type} are taken from this Subscriber message
     *                 queue.
     */
    protected final <B extends Broadcast> void subscribeBroadcast(Class<B> type, Callback<B> callback) {
        subscribedEvent=true;
        instace.subscribeBroadcast(type,this);
        this.messageCallBack.put(type,callback);
    }

    /**
     * Completes the received request {@code e} with the result {@code result}
     * using the MessageBroker.
     * <p>
     * @param <T>    The type of the expected result of the processed event
     *               {@code e}.
     * @param e      The event to complete.
     * @param result The result to resolve the relevant Future object.
     *               {@code e}.
     */
    protected final <T> void complete(Event<T> e, T result) {
        //System.out.println("Subscriber complete: ");
        //System.out.println("Event: "+e.getClass().getName() + " ,Result: "+result.toString());//TODO: Delete me
        instace.complete(e,result);
    }

    /**
     * Signals the event loop that it must terminate after handling the current
     * message.
     */
    protected final void terminate() {
        if(subscribedEvent) {
            //System.out.println("//////////////////////////////");
            //System.out.println("Unregistering: "+ Thread.currentThread().getName());
            this.instace.unregister(this);

        }
        this.terminated = true;
    }

    /**
     * The entry point of the Subscriber.
     * otherwise you will end up in an infinite loop.
     */
    @Override
    public final void run() {

        initialize();
        print();
        this.instace.register(this);


        while (!terminated) {

            try {
                Message m=instace.awaitMessage(this);
                //System.out.println(this.getName() + " calling callback of: "+m.getClass().getName());//TODO: Delete me
                if(m.getClass()== TickBroadcast.class) {
                    TickBroadcast tickBroadcast=(TickBroadcast)m;
                    //if(tickBroadcast.getTick()==tickBroadcast.getMaxTicks())
                        //System.out.printf("LastTick");
                }
                this.messageCallBack.get(m.getClass()).call(m);
            } catch (InterruptedException e) {
                e.printStackTrace(); //TODO: to erase
                terminate();
            }


        }

    }

    protected void print()
    {

    }

    protected boolean isTerminated() {
        return terminated;
    }

    protected void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }
}
