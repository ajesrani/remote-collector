package com.bmc.collector.K8S;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bmc.patrol.common.CollectorContext;

public class K8S extends CollectorContext {
	
	private ConcurrentHashMap<String, Constructor<?>> _cmdCtorMap = new ConcurrentHashMap<String, Constructor<?>>();
	private	ExecutorService _threadPool = Executors.newFixedThreadPool(10);
	
	public K8S()
	{
		System.out.println("Created K8S extends CollectorContext");
		
		String[] classes = new String[] {
				"Connect", "Discovery", "Collection"};
		for (String cls: classes) {
			addCtorForCmd(cls);
		}
		this.start();
	}

	private void addCtorForCmd(String cls) {
		Constructor<?> ctor = null;
		String pkgName = this.getClass().getPackage().getName();
		
		try {
			Class<?> c = Class.forName(pkgName + "." + cls);
			ctor = c.getConstructor( String.class );
			_cmdCtorMap.put(cls, ctor);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void runRequestHandler() {
		
		String input = null;
		while (true) 
		{
			try {
				input = _requestQueue.take();
				String[] iArr = input.split("&");
				
				Runnable task = null;
				Constructor<?> ctor = _cmdCtorMap.get(iArr[0]);
				task = (Runnable)ctor.newInstance(iArr[1]);
				_threadPool.submit(task);
				
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

}
