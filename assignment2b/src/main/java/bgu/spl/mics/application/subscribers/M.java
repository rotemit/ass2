package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Gadget;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	private Diary diary;
	private int serialNumber;
	private boolean initialized;

	public M(int serialNumber) {
		super("M");
		this.serialNumber = serialNumber;
		this.diary = Diary.getInstance();
		this.initialized = false;
	}

	@Override
	protected void initialize() {

		Callback<MissionReceivedEvent> callback = new Callback<MissionReceivedEvent>(){
			@Override
			public void call(MissionReceivedEvent e) throws InterruptedException {
				//check if agents are available
				String mission = e.getMissionName();
				List<String> agents = diary.getMissionAgents(mission);
				Future<Agent> areAvailablesAgents = simplePublisher.sendEvent(new AgentsAvailableEvent(agents));
				// if agents not available ..
				if (areAvailablesAgents.get() == null){
					return;
				}
				// now check if the gadget is available
				String gadgetName = diary.getMissionGadget(mission);
				Future<Gadget> isAvailableGadget = simplePublisher.sendEvent(new GadgetAvailableEvent(gadgetName));
				//if gadget not available ..
				if(isAvailableGadget.get()==null){
					return;
				}
				//TODO to continue ..



			}
		};
		this.subscribeEvent(MissionReceivedEvent.class, callback);
		initialized = true;
	}

}
