package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
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
	private int currentTick;

	public M(int serialNumber) {
		super("M");
		this.serialNumber = serialNumber;
		this.diary = Diary.getInstance();
		this.initialized = false;
	}

	@Override
	protected void initialize() {
		Callback<TickBroadcast> tickCallback = new Callback<TickBroadcast>() {
			@Override
			public void call(TickBroadcast c) throws InterruptedException {
				currentTick = c.getTime();
			}
		};
		this.subscribeBroadcast(TickBroadcast.class, tickCallback);

		Callback<MissionReceivedEvent> callback = new Callback<MissionReceivedEvent>() {
			@Override
			public void call(MissionReceivedEvent e) throws InterruptedException {
				//check if agents are available
				String mission = e.getMissionName();
				List<String> agents = e.getAgentsList();
				Future<Agent> areAvailablesAgents = simplePublisher.sendEvent(new AgentsAvailableEvent(agents));
				// if agents not available ..
				if (areAvailablesAgents.get() == null) {
					return;
				}
				Future<String> takeAgents = simplePublisher.sendEvent(new TakeAgentsEvent(agents));
				//TODO : write in the diary. If time expired released agents
				if (e.getTimeExpired() < currentTick) {
					Future<Agent> releaseAgents = simplePublisher.sendEvent(new ReleaseAgentEvent(agents));
					return;
				}
				// now check if the gadget is available
				String gadgetName = e.getGadget();
				Future<Gadget> isAvailableGadget = simplePublisher.sendEvent(new GadgetAvailableEvent(gadgetName));
				//if gadget not available ..
				if (isAvailableGadget.get() == null) {
					return;
				}

			}
		};
		this.subscribeEvent(MissionReceivedEvent.class, callback);
		initialized = true;
	}


}
