package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private static MessageBrokerImpl instance = null;
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> subsPersoQueue;
	private ConcurrentHashMap<Class<? extends Event> , ConcurrentLinkedQueue<Subscriber>> eventSubsQueue;// a queue of Subscriber for each type of Event
	private ConcurrentHashMap<Class<? extends Broadcast> , LinkedList<Subscriber>> broadSubsQueue;	// a queue of Subscriber for each type of BroadCast
	private ConcurrentHashMap<Event, Future> eventFuturesMap; // map of matching Events(key) and their Futures(value)


	//******** private constructor
	private MessageBrokerImpl(){
		subsPersoQueue = new ConcurrentHashMap<>();
		eventSubsQueue = new ConcurrentHashMap<>();
		broadSubsQueue = new ConcurrentHashMap<>();
		eventFuturesMap = new ConcurrentHashMap<>();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		if(instance == null) {
			instance = new MessageBrokerImpl();
		}
		return instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		// add m to to linkedQueue of event class
		// if the event class doesnt exist create a new LinkedQueue
		synchronized (eventSubsQueue){
			eventSubsQueue.computeIfAbsent(type,k->new ConcurrentLinkedQueue<>()).add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		synchronized (broadSubsQueue){
			broadSubsQueue.computeIfAbsent(type,k->new LinkedList<>()).add(m);
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		synchronized (eventFuturesMap){
			while(eventFuturesMap.get(e)==null){
				try{
					eventFuturesMap.wait();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			eventFuturesMap.get(e).resolve(result);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		//if there is no subscribers to this broadcast
		if(broadSubsQueue.get(b.getClass())==null || broadSubsQueue.get(b.getClass()).isEmpty()){
			//TODO check what to return (exception?)
			return;
		}
		//add the broadcast to all subscriber's personnal queue of this broadcast class
		synchronized(subsPersoQueue){
			for(Subscriber s : broadSubsQueue.get(b.getClass())){
				subsPersoQueue.get(s).add(b);
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> future = new Future<>();
		synchronized (eventSubsQueue){
			//in case no such event class, add it to the map
			if(eventSubsQueue.get(e.getClass())==null)
				eventSubsQueue.put(e.getClass(), new ConcurrentLinkedQueue<>());
			//in case no Subscriber has subscribed to {@code e.getClass()
			if(eventSubsQueue.get(e.getClass()).isEmpty()){
				future.resolve(null);
			}
			//Retrieves and removes the subscriber at the head of the T class eventQueue
			Subscriber currSubcriber = eventSubsQueue.get(e.getClass()).poll();
			subsPersoQueue.get(currSubcriber).add(e);// add the event message to the personnal queue of the current subscriber
			//push back the subscriber at the tail of the event queue (round robin)
			eventSubsQueue.get(e.getClass()).add(currSubcriber);
		}
		synchronized (eventFuturesMap){
			eventFuturesMap.put(e, future);// add the future object to eventFuturesMap
			eventFuturesMap.notifyAll();//wakes up all the subscribers that called wait() on the same future
		}
		return future;
   }

	@Override
	public void register(Subscriber m) {
		//add the new subscriber to the map of subscribers and create a new personnal queue
		synchronized (subsPersoQueue){
			subsPersoQueue.put(m, new LinkedBlockingQueue<>());
		}

	}

	@Override
	public void unregister(Subscriber m) {
		//remove m from the events it subscribed to
		synchronized(eventSubsQueue){
			eventSubsQueue.forEach((element,event)-> event.remove(m));
		}
		//remove m from the broadcast it subscribed to
        synchronized (broadSubsQueue){
			broadSubsQueue.forEach((element,broadcast)-> broadcast.remove(m));
		}
		//remove m from the map of subcribers personnal queues
		synchronized (subsPersoQueue){
			subsPersoQueue.remove(m);
		}
	}





	@Override
	// m get a message from its personnal queue
	// If the queue is empty then it will wait until a message becomes available
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		return subsPersoQueue.get(m).take();
	}


}
