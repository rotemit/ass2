package bgu.spl.mics;

import java.util.concurrent.ConcurrentHashMap;

abstract class RunnableSubPub implements Runnable {
    protected boolean terminated = false;
    protected ConcurrentHashMap<Class ,Callback> callbackMap;
    private final String name;
    protected final SimplePublisher simplePublisher;
    protected MessageBroker messageBroker;

    /**
     * this method is called once when the event loop starts.
     */
    protected abstract void initialize();

    /**
     * @param name the Publisher/Subscriber name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    RunnableSubPub(String name) {
        this.name = name;
        simplePublisher = new SimplePublisher();
        messageBroker = MessageBrokerImpl.getInstance();
        callbackMap = new ConcurrentHashMap<>();
        //TODO maybe to be change maybe not
    }

    /**
     * @return the name of the Publisher/Subscriber - the Publisher/Subscriber name is given to it in the
     *         construction time and is used mainly for debugging purposes.
     */
    public final String getName() {
        return name;
    }

    /**
     * The entry point of the publisher/subscriber. TODO: you must complete this code
     * otherwise you will end up in an infinite loop.
     */
    @Override
    public abstract void run();


    /**
     * @return the simple publisher
     */
    public SimplePublisher getSimplePublisher() {
        return simplePublisher;
    }
}
