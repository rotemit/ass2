package bgu.spl.mics;

import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
