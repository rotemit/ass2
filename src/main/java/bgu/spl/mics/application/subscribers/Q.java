package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Gadget;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Result;
import javafx.util.Pair;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private Inventory inventory;
	private boolean initialized;
	private int qTime;// eventReceivedTime

	public Q() {
		super("Q");
		inventory = Inventory.getInstance();
		initialized = false;
		qTime = -1;
	}


	@Override
	protected void initialize() {

		Callback<TerminateBroadcast> terminateCallback = new Callback<TerminateBroadcast>() {
			@Override
			public void call(TerminateBroadcast c) throws InterruptedException {
				terminated = true;
			}
		};
		this.subscribeBroadcast(TerminateBroadcast.class, terminateCallback);

		Callback<TickBroadcast> tickCallback = new Callback<TickBroadcast>() {
			@Override
			public void call(TickBroadcast c) throws InterruptedException {
				qTime = c.getTime();
			}
		};
		this.subscribeBroadcast(TickBroadcast.class, tickCallback);


        // check if the gadget exists in inventory
		Callback<GadgetAvailableEvent> callback = new Callback<GadgetAvailableEvent>(){
			@Override
			public void call(GadgetAvailableEvent e) throws InterruptedException {
				Result available = inventory.checkIfAvailable(e.getGadgetName());
				complete(e ,available);
			}
		};
		this.subscribeEvent(GadgetAvailableEvent.class, callback);


		//take a gadget from inventory
		Callback<TakeGadgetEvent> callback2 = new Callback<TakeGadgetEvent>() {
			@Override
			public void call(TakeGadgetEvent takeGadgetEvent) throws InterruptedException {
				Gadget gadget = inventory.takeItem(takeGadgetEvent.getName());
				Pair<Gadget, Integer> gadgetIntegerPair = new Pair<>(gadget, qTime);
				complete(takeGadgetEvent, gadgetIntegerPair);
			}
		};
		subscribeEvent(TakeGadgetEvent.class, callback2);



		initialized = true;

	}

	public boolean isInitialized(){
		return initialized;
	}

}
