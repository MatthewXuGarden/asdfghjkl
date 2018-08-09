package com.carel.supervisor.presentation.polling;


import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.dataaccess.event.EventMgr;

public class StartThread implements Runnable {

	private PollingStatusMrg poll = PollingStatusMrg.getInstance();
	private SimpleDateFormat formato = new SimpleDateFormat("HH:mm:00");
	private Time startedThread = Time.valueOf(formato.format(Calendar.getInstance().getTime()));
	private int pausa = 2;	//pausa di 2 minuti
	private boolean stop = false;
	
	public StartThread(){}
	
	public void run(){
		//EventMgr.getInstance().info(new Integer(1), "System", "Polling",
        //        "POL03", null);
		while(!stop){
				poll.run();
			 try {
				Thread.sleep(pausa*60*1000);
			} catch (InterruptedException e) {
				EventMgr.getInstance().error(new Integer(1), "System", "Polling",
		                "POL04", null);
			}
		}
		//EventMgr.getInstance().info(new Integer(1), "System", "Polling",
        //        "POL04", null);
	}
	
	public Time getTimeStartedThread() {
		return startedThread;
	}
	
	public void stopStartThread(){
		stop = true;
	}
		
}
