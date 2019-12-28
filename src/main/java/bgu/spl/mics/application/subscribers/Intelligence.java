package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Gadget;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
	private List<MissionInfo> missions;
	//private int nextSendingTimeTick;
	private int currentGlobalTime;
	private int currentIndex; //by index in missioninfolist
	private boolean anymoreMissions;
	private MissionInfo currentMission;
	private int serialNumber;
	private boolean initialized;



	public Intelligence(int serialNumber, List<MissionInfo> missions){
		super("Intelligence");
		this.missions = missions;
		this.serialNumber = serialNumber;
		anymoreMissions = true;
		currentIndex = 0;
		currentGlobalTime = 0;
		currentMission = missions.get(currentIndex);
		initialized = false;
	}


	public void setMissions(List<MissionInfo> missions){
		this.missions = missions;
	}

	protected void initialize() {
		sortMissionInfoList();
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
					currentGlobalTime = c.getTime();

					//loop in 'while' because some orders have the same tick

						while (anymoreMissions && currentMission.getTimeIssued() == currentGlobalTime) {  //its the time to send the order for this tick
							MissionReceivedEvent missionToSend = new MissionReceivedEvent(currentMission);
							System.out.println("Intelligence "+serialNumber+" sending event");
							simplePublisher.sendEvent(missionToSend); //TODO make sure its okay that we ignore the returned Future
							System.out.println("event sent by Intelligence "+serialNumber);
							currentIndex++;
							//if Intelligence ran out of missions to send, update fields
							if (currentIndex == missions.size()) {
								anymoreMissions = false;
								break;
							}
							currentMission = missions.get(currentIndex);

						}

				}
			};

		this.subscribeBroadcast(TickBroadcast.class, tickCallback);
		initialized = true;

	}


	/**
	 * the callback function of Intelligence that should be called when receiving a tick broadcast.
	 * send a MissionReceivedEvent if the schedule contains an order with the same tick received from the broadcast.
	 */
	/*
	private class TickCallback implements Callback<TickBroadcast> {
		@Override
		public void call(TickBroadcast tb) {
			currentGlobalTime = tb.getTime();

			//loop in 'while' because some orders have the same tick
			while (anymoreMissions) {
				while (currentMission.getTimeIssued() == currentGlobalTime) {  //its the time to send the order for this tick
					MissionReceivedEvent missionToSend = new MissionReceivedEvent(currentMission);
					simplePublisher.sendEvent(missionToSend); //TODO make sure its okay that we ignore the returned Future
					currentIndex++;
					if (currentIndex + 1 == missions.size()) {
						anymoreMissions = false;
						break;
					}
					currentMission = missions.get(currentIndex);

				}
			}
		}
	} */


		//sort missionInfoList according to timeIssued
		private void sortMissionInfoList() {
			missions.sort(Comparator.comparing(MissionInfo::getTimeIssued));
		}

		public void addMission(MissionInfo mi) {
			missions.add(mi);
		}

	}


