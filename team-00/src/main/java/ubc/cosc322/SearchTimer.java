package ubc.cosc322;

import java.util.Timer;
import java.util.TimerTask;

public class SearchTimer {
	GameTree tree;
	private boolean started = false;
	public SearchTimer(GameTree tree) {
		this.tree=tree;
		// TODO Auto-generated constructor stub
	}
	
	public Timer timer = new Timer();
	//public boolean timerInterrupt;
	
	public void startSearching() {
		timer = new Timer();
		tree.timerInterrupt=false;
		started = true;
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				System.out.println("Time is up! Interrupting & executing task");
				tree.timerInterrupt = true;
				started = false;
				
			}
		}, 27000); // 27 seconds!
	}
	
	public void timerCancel() {
		if(started) 
		{
			timer.cancel();
			started = false;
		}
		
	}
	
}
