package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TakeAgentsEvent;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.ArrayList;
import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private Squad squad;
	private boolean initialized;
	private int serialNumber;

	public Moneypenny(int serialNumber) {
		super("MonneyPonney");
		this.serialNumber = serialNumber;
		squad = Squad.getInstance();
	}

	@Override
	protected void initialize() {
		//TODO subscribe broadcast

		// check if the agents exist in squad
		Callback<AgentsAvailableEvent> callback = new Callback<AgentsAvailableEvent>(){
			@Override
			public void call(AgentsAvailableEvent e) throws InterruptedException {
				boolean available = squad.getAgents(e.getAgentsSerialNumbers());;
				complete(e,available);
			}
		};
		this.subscribeEvent(AgentsAvailableEvent.class, callback);
/*
//TODO : not sure its necessary
		//take agents from the squad
		Callback<TakeAgentsEvent> callback2 = (takeAgentsEvent)-> {
			List<String> agents = squad.takeAgents(takeAgentsEvent.getName());
			complete(takeAgentsEvent, agents);
		};
		subscribeEvent(TakeGadgetEvent.class, callback2);


 */
		initialized = true;
	}

	public boolean isInitialized() { return initialized; }
}
