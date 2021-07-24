package bgu.spl.mics;

import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    private MessageBroker messageBroker;
    private Subscriber[] subscribers;
    private Event[] events;
    private Broadcast[] broadcasts;
    private Future<List<String>> [] futures;

    @BeforeEach
    public void setUp(){
        messageBroker=MessageBrokerImpl.getInstance();
        subscribers= new Subscriber[10];
        events= new Event[10];
        broadcasts = new Broadcast[10];
        futures = new Future[10];

        subscribers[0] = new M();
        subscribers[1]= new M();
        subscribers[2]= new Moneypenny();
        subscribers[3]= new Moneypenny();
        subscribers[4]= new Moneypenny();

        //Event<List<String>> e = new AgentsAvailableEvent() ;
        //Event<List<String>> e1 = new AgentsAvailableEvent() ;
        //Event<List<String>> e2= new AgentsAvailableEvent() ;
        //events[0]=e;
        //events[1]=e1;
        //events[2]=e2;
//
        //Broadcast b = new TickBroadcast() ;
        //Broadcast b1 = new TickBroadcast() ;
        //Broadcast b2 = new TickBroadcast() ;
        //broadcasts[0]=b;
        //broadcasts[1]=b1;
        //broadcasts[2]=b2;
    }

    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");

        //messageBroker.awaitMessage();
        //messageBroker.register();
        //messageBroker.sendBroadcast();
        //messageBroker.sendEvent();
        //messageBroker.subscribeBroadcast();
        //messageBroker.unregister();
        //messageBroker.subscribeEvent();
        //messageBroker.complete();

    }

    @Test
    public void testRegister()
    {



        messageBroker.register(subscribers[0]);
        messageBroker.register(subscribers[1]);
        messageBroker.register(subscribers[2]);
        messageBroker.register(subscribers[3]);

    }

    @Test
    public void testSubscribeEvent()
    {
        messageBroker.subscribeEvent(AgentsAvailableEvent.class,subscribers[0]);
        messageBroker.subscribeEvent(AgentsAvailableEvent.class,subscribers[1]);
        messageBroker.subscribeEvent(AgentsAvailableEvent.class,subscribers[2]);
    }

    @Test
    public void testSubsribeBroadcast()
    {
        messageBroker.subscribeBroadcast(TickBroadcast.class,subscribers[0]);
        messageBroker.subscribeBroadcast(TickBroadcast.class,subscribers[1]);
        messageBroker.subscribeBroadcast(TickBroadcast.class,subscribers[3]);
    }

    @Test
    public void testSendEvent()
    {


        Future <List<String>> f_l1=messageBroker.sendEvent(events[0]);
        Future <List<String>> f_l2=messageBroker.sendEvent(events[1]);
        Future <List<String>> f_l3= messageBroker.sendEvent(events[2]);

        futures[0]=f_l1;
        futures[1]=f_l2;
        futures[2]=f_l3;

    }

    @Test
    public void testSendBroadcast()
    {


        try {
            messageBroker.sendBroadcast(broadcasts[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            messageBroker.sendBroadcast(broadcasts[1]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            messageBroker.sendBroadcast(broadcasts[2]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testComplete()
    {
        List<String> lst= new LinkedList<>();
        List<String> lst1= new LinkedList<>();
        messageBroker.complete(events[0],lst);


        List<String> lst_resolved=this.futures[0].get();
        for (int i = 0; i < lst_resolved.size(); i++) {
            assertEquals(lst.get(i),lst_resolved.get(i));
        }
        assertFalse(futures[1].isDone());

        messageBroker.complete(events[1],lst);
        assertTrue(futures[1].isDone());


    }

    @Test
    public void testUnregister()
    {
        messageBroker.unregister(subscribers[0]);
        messageBroker.unregister(subscribers[1]);
    }

    public void testAwaitMessage()
    {

        try {
            Message message = messageBroker.awaitMessage(subscribers[0]);
            assertTrue(true);

        }catch (InterruptedException e)
        {
            assertFalse(false);
        }

        try {
            Message message2=messageBroker.awaitMessage(subscribers[4]);
            assertFalse(false);
        }catch (IllegalStateException e)
        {
            assertTrue(true);
        }
        catch (InterruptedException e1) {
            assertFalse(false);
        }

    }



}
