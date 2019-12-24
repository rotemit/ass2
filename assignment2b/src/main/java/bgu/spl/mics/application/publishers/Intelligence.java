package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Gadget;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
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
	//private int nextSendingTimeTick;
	private int currentGlobalTime;
	private int currentIndex; //by index in missioninfolist
	private boolean anymoreMissions;
	private MissionInfo currentMission;

	//init
	public Intelligence() {
		super("Intelligence");
		missionInfoList = new ArrayList<>();
		anymoreMissions = true;
		currentIndex = 0;
		currentGlobalTime = 0;
		currentMission = missionInfoList.get(currentIndex);


		// TODO Implement this according to json?
	}

	protected void initialize() {
		sortMissionInfoList();
		Callback<TickBroadcast> tickBroadcastCallback = new TickCallback();
		subscribeBroadcast(TickBroadcast.class, tickBroadcastCallback); //subscribing to tick broadcast
	}


	/**
	 * the callback function of selling service that should be called when receiving a tick broadcast.
	 * send a BookOrderEvent if the schedule contains an order with the same tick received from the broadcast.
	 */
	private class TickCallback implements Callback<TickBroadcast> {
		@Override
		public void call(TickBroadcast tb) {
			currentGlobalTime = tb.getTime();

			//loop in 'while' because some orders have the same tick
			while (anymoreMissions) {
				while (currentMission.getTimeIssued() == currentGlobalTime) {  //its the time to send the order for this tick
					String missionName = currentMission.getMissionName();
					List<String> agents = currentMission.getSerialAgentsNumbers();
					String gadget = currentMission.getGadget();
					MissionReceivedEvent missionToSend = new MissionReceivedEvent(missionName, gadget, agents);
					simplePublisher.sendEvent(missionToSend); //TODO make sure its okay that we ignore the returned Future
					currentIndex++;
					if (currentIndex + 1 == missionInfoList.size()) {
						anymoreMissions = false;
						break;
					}
					currentMission = missionInfoList.get(currentIndex);

				}
			}
		}
	}


		//sort missionInfoList according to timeIssued
		private void sortMissionInfoList() {
			missionInfoList.sort(Comparator.comparing(MissionInfo::getTimeIssued));
		}

		public void addMission(MissionInfo mi) {
			missionInfoList.add(mi);
		}

	}


