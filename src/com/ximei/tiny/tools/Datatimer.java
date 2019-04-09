package com.ximei.tiny.tools;

import java.util.Timer;
import java.util.TimerTask;


public class Datatimer {
	
	 
		Timer timer;
		int flag; //标志什么时候结束
		double index; //发送数据自增标志
		double sec; //自增量
		String over; //结束标志
		int seconds;
		
		public Datatimer(int flag, int seconds) {
			this.timer = new Timer();
			this.flag = flag;
			this.sec = (double)seconds/10;
			this.seconds = seconds;
			
		}
		
		public void  starttimer(){
			
			timer.schedule(new testTimerTask(), 0*1000, seconds*100);
			
			
		}
		
		class testTimerTask extends TimerTask {
			@Override
			public void run() {
				index += sec;
				
				if(index >= flag) {
					timer.cancel();
					setOver("over");
					
				}
			}
		}
		
		public String getOver() {
			return over;
		}
		public void setOver(String over) {
			this.over = over;
		}
	
	

}
