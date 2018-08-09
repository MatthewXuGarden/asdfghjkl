package com.carel.supervisor.presentation.polling;



public class ThreadController {

	private static ThreadController myinstance = new ThreadController();
	private StartThread st;
	private Thread t;
	
	
	private ThreadController(){
		st = new StartThread();
		t = new Thread(st);
	}
	
	public static ThreadController getInstance(){
		return myinstance;
	}
	
	/**
	 * Ritorna  lo StartThread in esecuzione
	 * 	@return StartThread
	 */
	public StartThread getStartedThread(){
		return st;
	}
	
	/**
	 * Creo nuovo thread di esecuzione
	 */
	public void newThread(){
		st = new StartThread();
		t = new Thread(st);
	}
	
	/**
	 * Faccio partire il thread
	 */
	public void startThread(){
		t.start();
	}
	
	/**
	 * Stoppo il thread in esecuzione
	 */
	public void stopThread(){
		if(t.isAlive())
			st.stopStartThread();
	}
	
	/**
	 * Thread correntemente in esecuzione
	 * @return thread
	 */
	public Thread getThreadStarted(){
		return t;
	}
}
