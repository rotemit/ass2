package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Result;
import bgu.spl.mics.application.passiveObjects.Squad;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
	private int currentTick;

	public Moneypenny(int serialNumber) {
		super("MoneyPenny");
		this.serialNumber = serialNumber;
		squad = Squad.getInstance();
	}


	@Override
	protected void initialize() {
		Callback<TickBroadcast> tickCallback = new Callback<TickBroadcast>() {
			@Override
			public void call(TickBroadcast c) throws InterruptedException {
				currentTick =  c.getTime();
			}
		};
		this.subscribeBroadcast(TickBroadcast.class, tickCallback);

		Callback<TerminateBroadcast> terminateCallback = new Callback<TerminateBroadcast>() {
			@Override
			public void call(TerminateBroadcast c) throws InterruptedException {
				terminated = true;
			}
		};
		this.subscribeBroadcast(TerminateBroadcast.class, terminateCallback);

		// check if the agents exist in squad
		Callback<AgentsAvailableEvent> callback = new Callback<AgentsAvailableEvent>(){
			@Override
			public void call(AgentsAvailableEvent e) throws InterruptedException {
				System.out.println("MoneyPenny "+serialNumber+" processing callback for "+e.toString());
				List<String> agentsSerials = e.getAgentsSerialNumbers();
				Result result = squad.checkIfAvailableAgents(agentsSerials);
				Pair<Result,Integer> available = new Pair<>(result, serialNumber);
				complete(e, available);
			}
		};
		this.subscribeEvent(AgentsAvailableEvent.class, callback);

		//take agents from the squad
		Callback<TakeAgentsEvent> callback2 = new Callback<TakeAgentsEvent>() {
			@Override
			public void call(TakeAgentsEvent c) throws InterruptedException {
				List<String> agents = c.getAgentsName();
				squad.sendAgents(agents, currentTick);
				complete(c, agents);
			}
		};
		this.subscribeEvent(TakeAgentsEvent.class, callback2);

		//release agents from the squad
		Callback<ReleaseAgentEvent> callback3 = new Callback<ReleaseAgentEvent>() {
			@Override
			public void call(ReleaseAgentEvent c) throws InterruptedException {
				List<String> agents = c.getAgentsName();
				squad.releaseAgents(agents);
				complete(c, agents);
			}
		};
		this.subscribeEvent(ReleaseAgentEvent.class, callback3);


		initialized = true;
	}

	public boolean isInitialized() { return initialized; }
}
