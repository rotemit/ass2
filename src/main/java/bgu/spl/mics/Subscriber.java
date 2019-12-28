package bgu.spl.mics;

import bgu.spl.mics.application.MI6Runner;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

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
    /* herited
    private final String name;
    private final SimplePublisher simplePublisher;
    protected MessageBroker messageBroker;
    private boolean terminated = false;
    private ConcurrentHashMap<Class ,Callback> callbackMap;
 */
//    private CountDownLatch latch;

    /**
     * @param name the Subscriber name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public Subscriber(String name) {
        super(name);
    }

    //rotem
    /*
    public Subscriber(String name, CountDownLatch latch){
        super(name);
        this.latch = latch;
    } */

    /**
     * Subscribes to events of type {@code type} with the callback
     * {@code callback}. This means two things:
     * 1. Subscribe to events in the singleton MessageBroker using the supplied
     * {@code type}
     * 2. Store the {@code callback} so that when events of type {@code type}
     * are received it will be called.
     * <p>
     *     //TODO : check where callback function is implemented
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
        messageBroker.subscribeEvent(type, this);
        callbackMap.put(type, callback);
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
        messageBroker.subscribeBroadcast(type, this);
        callbackMap.put(type, callback);
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
        messageBroker.complete(e, result);
    }

    /**
     * Signals the event loop that it must terminate after handling the current
     * message. // TODO : check
     */


    /**
     * The entry point of the Subscriber. TODO: you must complete this code
     * otherwise you will end up in an infinite loop.
     */
    @Override
    public final void run() {

        messageBroker.register(this) ;
        //subscribe to terminateBroadcast and provide the corresponding callback
        /*Callback<TerminateBroadcast> terminateCallback = new Callback<TerminateBroadcast>() {
            @Override
            public void call(TerminateBroadcast c) throws InterruptedException {
                System.out.println("subscriber "+this.getClass()+" entered terminate callback");
                terminated = true;
            }
        };
        this.subscribeBroadcast(TerminateBroadcast.class, terminateCallback);*/
//        System.out.println("subscriber "+this.getName()+" subscribed to terminate broadcast");
        initialize();
        MI6Runner.latch.countDown();
        while (!terminated) {
//            System.out.println("subscriber "+this.getName()+ " entered !terminated loop");
            try {
                Message m = messageBroker.awaitMessage(this);
                (callbackMap.get(m.getClass())).call(m);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        messageBroker.unregister(this);
    }

}
