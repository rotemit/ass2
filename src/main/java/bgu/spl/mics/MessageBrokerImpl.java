package bgu.spl.mics;

import bgu.spl.mics.application.messages.TerminateBroadcast;

import java.util.Iterator;
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
	//for each instance of subscriber, we have a queue
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> subsPersonQueue;
	//for each type of message there is a queue of subscribers
	private ConcurrentHashMap<Class<? extends Event> , ConcurrentLinkedQueue<Subscriber>> eventSubsMapOfQueues;// a queue of Subscriber for each type of Event

	private ConcurrentHashMap<Class<? extends Broadcast> , LinkedList<Subscriber>> broadSubsMapOfLists;	// a list of Subscribers for each type of BroadCast

	//each event that's released goes into this map
	private ConcurrentHashMap<Event, Future> eventFuturesMap; // map of matching Events(key) and their Futures(value)

	private static class instanceHolder {
		private static MessageBrokerImpl instance = new MessageBrokerImpl();
	}

	//******** private constructor
	private MessageBrokerImpl(){
		subsPersonQueue = new ConcurrentHashMap<>();
		eventSubsMapOfQueues = new ConcurrentHashMap<>();
		broadSubsMapOfLists = new ConcurrentHashMap<>();
		eventFuturesMap = new ConcurrentHashMap<>();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return instanceHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		// add m to to linkedQueue of event class
		// if the event class doesnt exist create a new LinkedQueue
		synchronized (eventSubsMapOfQueues){
			eventSubsMapOfQueues.computeIfAbsent(type, k->new ConcurrentLinkedQueue<>()).add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		synchronized (broadSubsMapOfLists){
			broadSubsMapOfLists.computeIfAbsent(type, k->new LinkedList<>()).add(m);
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

		//if there are no subscribers to this broadcast
		if(broadSubsMapOfLists.get(b.getClass())==null || broadSubsMapOfLists.get(b.getClass()).isEmpty()){
			//TODO check what to return (exception?)
			return;
		}
		//add the broadcast to all subscribers' personal queue of this broadcast class
		synchronized(broadSubsMapOfLists){
			Iterator<Subscriber> iterator = broadSubsMapOfLists.get(b.getClass()).iterator();
			while (iterator.hasNext()){
				Subscriber currSub = iterator.next();
				LinkedBlockingQueue<Message> currSubQueue = subsPersonQueue.get(currSub);
				currSubQueue.add(b);
			}
//			subsPersonQueue.get(iterator.next()).add(b);
//			for(Subscriber s : broadSubsMapOfQueues.get(b.getClass())){
//				subsPersonQueue.get(s).add(b);
//			}
//			this.notifyAll();
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> future = new Future<>();
		synchronized (eventSubsMapOfQueues){
			//in case no such event class, add it to the map
			if(eventSubsMapOfQueues.get(e.getClass())==null)
				eventSubsMapOfQueues.put(e.getClass(), new ConcurrentLinkedQueue<>());
			//in case no Subscriber has subscribed to {@code e.getClass()
			if(eventSubsMapOfQueues.get(e.getClass()).isEmpty()){
				future.resolve(null);
				return null;
			}
			//Retrieves and removes the subscriber at the head of the T class eventQueue
			Subscriber currSubscriber = eventSubsMapOfQueues.get(e.getClass()).poll();
			subsPersonQueue.get(currSubscriber).add(e);// add the event message to the personal queue of the current subscriber
			//push back the subscriber at the tail of the event queue (round robin)
			eventSubsMapOfQueues.get(e.getClass()).add(currSubscriber);
		}
		synchronized (eventFuturesMap){
			eventFuturesMap.put(e, future);// add the future object to eventFuturesMap
			eventFuturesMap.notifyAll();//wakes up all the subscribers that called wait() on the same future
		}
		return future;
   }

	@Override
	public void register(Subscriber m) {
		//add the new subscriber to the map of subscribers and create a new personal queue
		synchronized (subsPersonQueue){
			subsPersonQueue.put(m, new LinkedBlockingQueue<>());
		}

	}

	@Override
	public void unregister(Subscriber m) {
		//remove m from the events it subscribed to
		synchronized(eventSubsMapOfQueues){
			eventSubsMapOfQueues.forEach((element, event)-> event.remove(m));
		}
		//remove m from the broadcast it subscribed to
        synchronized (broadSubsMapOfLists){
			broadSubsMapOfLists.forEach((element, broadcast)-> broadcast.remove(m));
		}

		//move unprocessed events and remove m from the map of subcribers personnal queues
		synchronized (subsPersonQueue){
			for(Message message : subsPersonQueue.get(m)){
				if(message instanceof Event){
					moveEvent((Event<?>) message);
				}
			}
			subsPersonQueue.remove(m);
		}
	}

	private void moveEvent(Event<?> event) {
		synchronized (eventSubsMapOfQueues){
			if (eventSubsMapOfQueues.get(event.getClass()).isEmpty()){
				complete(event, null);
				return;
			}
			Subscriber next = eventSubsMapOfQueues.get(event.getClass()).poll();
			subsPersonQueue.get(next).add(event);
			eventSubsMapOfQueues.get(event.getClass()).add(next);

		}

	}


	@Override
	// m get a message from its personnal queue
	// If the queue is empty then it will wait until a message becomes available
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		return subsPersonQueue.get(m).take();
	}


}
