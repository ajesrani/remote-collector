package com.bmc.patrol.common;

import java.io.PrintWriter;

public class ResponseHandler {

	private PrintWriter _outWriter = null;
	
	public ResponseHandler() {
		_outWriter = new PrintWriter(System.out);
	}
	
	public void push(Object obj) {
		_outWriter.write(obj.toString());
		_outWriter.write('\n');
		_outWriter.flush();
	}

}
