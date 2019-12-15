package bgu.spl.mics;

import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {

    MessageBrokerImpl messageBroker;


    @BeforeEach
    public void setUp() {
        messageBroker = new MessageBrokerImpl();
    }

    @Test
    public void getInstance() {
        MessageBrokerImpl messBroker = new MessageBrokerImpl();
        assertSame(messageBroker, messBroker.getInstance());
    }


    @Test
    public void testSubscribeEvent() throws InterruptedException {
        Subscriber q = new Q();
        Event<String> event = new ExampleEvent("it's raining outside");
        messageBroker.sendEvent(event);
        assertTrue((messageBroker.awaitMessage(q) == null));
        messageBroker.subscribeEvent(ExampleEvent.class, q);
        messageBroker.sendEvent(event);
        assertFalse(messageBroker.awaitMessage(q) == null);
    }


    @Test
    public void subscribeBroadcast() throws InterruptedException {
        Subscriber q = new Q();
        ExampleBroadcast broadcast = new ExampleBroadcast("the diner is ready");
        messageBroker.sendBroadcast(broadcast);
        assertTrue((messageBroker.awaitMessage(q) == null));
        messageBroker.subscribeBroadcast(ExampleBroadcast.class, q);
        messageBroker.sendBroadcast(broadcast);
        assertFalse(messageBroker.awaitMessage(q) == null);
    }


    @Test
    public void complete() {
        Future future = new Future();
        future.resolve(true);
        Event<String> event = new ExampleEvent("My name is Bond, Jame Bond");
        assertTrue((future.get() == null));
        messageBroker.complete(event, "true");
        assertFalse(future.get() == null);
    }


    @Test
    public void sendEvent() throws InterruptedException {
        Subscriber q = new Q();
        Subscriber m = new M();
        Event<String> event = new ExampleEvent("who are you ?");
        messageBroker.subscribeEvent(ExampleEvent.class, q);
        messageBroker.subscribeEvent(ExampleEvent.class, m);
        assertTrue((messageBroker.awaitMessage(q) == null) & (messageBroker.awaitMessage(m) == null));
        messageBroker.sendEvent(event);
        assertFalse(((messageBroker.awaitMessage(q) == null) & !(messageBroker.awaitMessage(m) == null)) ||
                (!(messageBroker.awaitMessage(q) == null) & (messageBroker.awaitMessage(m) == null)));
        ;
    }


    @Test
    public void sendBroadcast() throws InterruptedException {
        Subscriber q = new Q();
        Broadcast broadcast = new ExampleBroadcast("Hello beautiful world");
        messageBroker.subscribeBroadcast(ExampleBroadcast.class, q);
        assertTrue(messageBroker.awaitMessage(q) == null);
        messageBroker.sendBroadcast(broadcast);
        assertFalse(messageBroker.awaitMessage(q) == null);
    }


    @Test
    public void register() throws InterruptedException {
        Subscriber q = new Q();
        messageBroker.subscribeEvent(ExampleEvent.class, q);
        Event<String> event = new ExampleEvent("who are you ?");
        messageBroker.sendEvent(event);
        assertTrue(messageBroker.awaitMessage(q) == null);
        messageBroker.register(q);
        messageBroker.subscribeEvent(ExampleEvent.class, q);
        Event<String> event2 = new ExampleEvent("who are you guy?");
        messageBroker.sendEvent(event);
        assertFalse(messageBroker.awaitMessage(q) == null);
    }


    @Test
    public void unregisterTest() throws InterruptedException {
        Subscriber q = new Q();
        messageBroker.subscribeEvent(ExampleEvent.class, q);
        messageBroker.register(q);
        Event <String> event1 = new ExampleEvent("hello");
        messageBroker.sendEvent(event1);
        Event <String> event2 = new ExampleEvent("hello hello");
        messageBroker.sendEvent(event2);
        Event <String> event3 = new ExampleEvent("hello hello hello");
        messageBroker.sendEvent(event3);
        messageBroker.unregister(q);
        assertEquals(null, messageBroker.awaitMessage(q));

    }

    @Test
    public void awaitMessageTest() throws InterruptedException {
        Subscriber q = new Q();
        try{
            messageBroker.awaitMessage(q);
        }
        catch (IllegalStateException e){
            fail("Unexpected expression: "+e.getMessage());
        }
        messageBroker.register(q);
        Event <String> event = new ExampleEvent("hello");
        messageBroker.sendEvent(event);
        assertEquals(event, messageBroker.awaitMessage(q));
    }

}


