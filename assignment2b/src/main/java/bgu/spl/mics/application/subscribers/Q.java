package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TakeGadgetEvent;
import bgu.spl.mics.application.passiveObjects.Gadget;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	/* fields herited
	private final String name;
	private final SimplePublisher simplePublisher;
	protected MessageBroker messageBroker;
	private boolean terminated = false;
	private ConcurrentHashMap<Class ,Callback> callbackMap;
 */
	private Inventory inventory;
	private boolean initialized;

	public Q() {
		super("Q");
		inventory = Inventory.getInstance();
		initialized = false;
	}


	@Override
	protected void initialize() {
		//TODO subscribe broadcast

        // check if the gadget exist in inventory
		Callback<GadgetAvailableEvent> callback = new Callback<GadgetAvailableEvent>(){
			@Override
			public void call(GadgetAvailableEvent e) throws InterruptedException {
				boolean available = inventory.getItem(e.getGadgetName());
				complete(e ,available);
			}
		};
		this.subscribeEvent(GadgetAvailableEvent.class, callback);



/*
//TODO not sur its necessary
		//take a gadget from inventory
		Callback<TakeGadgetEvent> callback2 = (takeGadgetEvent)-> {
			Gadget gadget = inventory.takeItem(takeGadgetEvent.getName());
			complete(takeGadgetEvent, gadget);
		};
		subscribeEvent(TakeGadgetEvent.class, callback2);

 */
		initialized = true;
	}

	public boolean isInitialized(){
		return initialized;
	}

}
