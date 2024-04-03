package ubc.cosc322;

import java.util.Timer;
import java.util.TimerTask;

public class SearchTimer {
	GameTree tree;
	public SearchTimer(GameTree tree) {
		this.tree=tree;
		// TODO Auto-generated constructor stub
	}
	
	public Timer timer = new Timer();
	//public boolean timerInterrupt;
	
	public void startSearching() {
		tree.timerInterrupt=false;
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				System.out.println("Time is up! Interrupting & executing task");
				tree.timerInterrupt = true;
				
			}
		}, 27000); // 27 seconds!
	}
	
	public void timerCancel() {
		timer.cancel();
		
	}
	
}
