package bgu.spl.mics;

import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.example.CallbackImpl;
import bgu.spl.mics.example.messages.ExampleEvent;
import jdk.nashorn.internal.codegen.CompilerConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.plugin2.jvm.RemoteJVMLauncher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {
    Subscriber m;
    Subscriber q;
    Subscriber moneypenny;
    MessageBroker messageBroker;
    Event<GadgetAvailableEvent> gadgetsAvailableEventEvent;


    @BeforeEach
    public void setUp(){
        m = new M();
        q = new Q();
        moneypenny = new Moneypenny();
        messageBroker = new MessageBrokerImpl();
        gadgetsAvailableEventEvent = new GadgetAvailableEvent();
    }

    @Test
    public void testSubscribeEvent(){
//        Event<GadgetAvailableEvent> gadgetsAvailableEventEvent = new GadgetAvailableEvent();
        messageBroker.subscribeEvent(Event<gadgetsAvailableEventEvent>, q);

    }

    <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber s);

  

    @Test
    public void unregisterTest(){
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
    public void awaitMessageTest(){
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
