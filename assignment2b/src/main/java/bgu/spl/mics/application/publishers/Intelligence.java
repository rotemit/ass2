package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.Comparator;
import java.util.List;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
/* herited
    private final String name;
    private final SimplePublisher simplePublisher;
    protected MessageBroker messageBroker;
     */


	//***********************fields
	private List<MissionInfo> missionInfoList;
	private int nextSendingTimeTick;
	private int currentTimeTick;
	private int nextMissionToSend; //by index in missioninfolist
	private boolean anymoreMissions;

	//init
	public Intelligence() {
		super("Intelligence");
		//fill missioninfolist




		// TODO Implement this according to json?
	}


	@Override
	protected void initialize() {
		sortMissionInfoList();
		if (!missionInfoList.isEmpty()){
			anymoreMissions = true;
			nextMissionToSend = 0;
			nextSendingTimeTick = missionInfoList.get(0).getTimeIssued();
		}
		//TODO subscribe broadcast


		Callback<MissionReceivedEvent> callback = new Callback<MissionReceivedEvent>(){
			@Override //TODO do we need to throw exception?
			public void call(MissionReceivedEvent e) throws InterruptedException {

				complete(e,);
			}
		};
		this.subscribeEvent(AgentsAvailableEvent.class, callback);

//		TickBroadcast type = new TickBroadcast();

		this.subscribeBroadcast(TickBroadcast.class, callback);
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

	}




	//sort missionInfoList according to timeIssued
	private void sortMissionInfoList(){
		missionInfoList.sort(Comparator.comparing(MissionInfo::getTimeIssued));
	}

	public void addMission(MissionInfo mi) {
		missionInfoList.add(mi);
	}

}
