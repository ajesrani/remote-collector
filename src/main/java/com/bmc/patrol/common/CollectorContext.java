package com.bmc.patrol.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class CollectorContext extends Thread {

	protected BlockingQueue<String> _requestQueue = new LinkedBlockingQueue<String>();
	public static ResponseHandler _rqueue = null;
	
	public CollectorContext() {
		_rqueue = new ResponseHandler();
	}
	
	public void execute(String entry) {
		try {
			this._requestQueue.put(entry);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	public void pushResponse(Object obj) {
		_rqueue.push(obj);
	}
	
	public void run() {
		runRequestHandler();
	}

	abstract public void runRequestHandler();
}
