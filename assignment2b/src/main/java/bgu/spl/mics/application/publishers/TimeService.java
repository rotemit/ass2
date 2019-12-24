package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	//***************fields
	Timer timer;
	int totalTicks;
	int currentTimeTick;

	public TimeService() {
		super("TimeService");
		// TODO Implement this
	}

	public TimeService(int ticks){
		super("TimeService");
		totalTicks = ticks;
		timer = new Timer();
	}

	@Override
	protected void initialize() {
		TimerTask task = new TimerTask(){
			@Override
			public void run(){
				if (currentTimeTick<totalTicks){
					TickBroadcast tb = new TickBroadcast(currentTimeTick);
					getSimplePublisher().sendBroadcast(tb);
					currentTimeTick++;
				}
				else {
					//terminate
					timer.cancel();
//					task.cancel();
					//TODO maybe more terminations are required
				}
			}
		};
		timer.scheduleAtFixedRate(task, 0, 100); //delay 0, 100 milliseconds between successive executions
	}

	@Override
	public void run() {
		initialize();
	}

}
