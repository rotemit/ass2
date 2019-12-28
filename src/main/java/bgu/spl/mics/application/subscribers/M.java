package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;
import javafx.util.Pair;

import java.awt.image.DirectColorModel;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

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
				currentTick = c.getTime();
			}
		};
		this.subscribeBroadcast(TickBroadcast.class, tickCallback);

		Callback<MissionReceivedEvent> callback = new Callback<MissionReceivedEvent>() {
			@Override
			public void call(MissionReceivedEvent e) throws InterruptedException {
				System.out.println("M "+serialNumber+" received event "+e.getMission().getMissionName());
				Diary.getInstance().increment();
				//check if agents are available
				MissionInfo mission = e.getMission();
				String missionName = mission.getMissionName();
				List<String> agents = mission.getSerialAgentsNumbers();
				Future<Pair<Result, Integer>> areAvailablesAgents = simplePublisher.sendEvent(new AgentsAvailableEvent(agents));
				// if agents not available ..
				System.out.println("M "+serialNumber+" event "+e.getMission().getMissionName()+" PRINT 1");
				if (areAvailablesAgents.get().getKey() == Result.NOT_AVAILABLE) {
					//TODO maybe here we need to write something in the report
					System.out.println("M "+serialNumber+" event "+e.getMission().getMissionName()+" PRINT 2");
					return;
				}

				// now check if the gadget is available
				String gadgetName = mission.getGadget();
				Future<Result> isAvailableGadget = simplePublisher.sendEvent(new GadgetAvailableEvent(gadgetName));
				//if gadget not available ..
				System.out.println("M "+serialNumber+"event "+e.getMission().getMissionName()+" PRINT 3");
				if (isAvailableGadget.get() == Result.NOT_AVAILABLE) {

					return;
				}
				System.out.println("M "+serialNumber+"event "+e.getMission().getMissionName()+" PRINT 4");
				if (mission.getTimeExpired() < currentTick) {
					Future<List<String>> releaseAgents = simplePublisher.sendEvent(new ReleaseAgentEvent(agents));
					return;
				}
				System.out.println("M "+serialNumber+"event "+e.getMission().getMissionName()+" PRINT 5");
				Future<List<String>> takeAgents = simplePublisher.sendEvent(new TakeAgentsEvent(agents));
				Future<Pair<Gadget, Integer>> takeGadget = simplePublisher.sendEvent(new TakeGadgetEvent(gadgetName));
				Report report = new Report(mission.getMissionName(), serialNumber, areAvailablesAgents.get().getValue(), mission.getSerialAgentsNumbers(),
						takeAgents.get(), mission.getGadget(), takeGadget.get().getValue(), mission.getTimeIssued(), currentTick);
				diary.addReport(report);
				System.out.println("M "+serialNumber+" finishing callback for event "+e.getMission().getMissionName());
			}
		};

		this.subscribeEvent(MissionReceivedEvent.class, callback);

		initialized = true;
	}


}
