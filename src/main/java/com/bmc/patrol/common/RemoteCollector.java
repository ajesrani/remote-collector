package com.bmc.patrol.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

public class RemoteCollector 
{
	public static Hashtable<String, CollectorContext> _contextMap = new Hashtable<String, CollectorContext>();

	public static void main(String[] args) 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Enter Requests and wait for response.");
		while (true) {
			try {
				String inpStr = br.readLine();
				String[] iArr = inpStr.split(":");
				
				CollectorContext ctxObj = getContext(iArr[0]);
				ctxObj.execute(iArr[1]);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static CollectorContext getContext(String ctx) {
		CollectorContext ctxObj = null;
		
		ctxObj = _contextMap.get(ctx);
		if (ctxObj != null) {
			return ctxObj;
		}
		
		try {
			Class<?> cls = Class.forName("com.bmc.collector." + ctx + "." + ctx);
			Constructor<?> ctor = cls.getConstructor();
			ctxObj = (CollectorContext)ctor.newInstance();
			_contextMap.put(ctx, ctxObj);
			
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return ctxObj;
	}
}
