package com.jonex.platform.test;

public class Semaphore {
	
	private boolean signal = false;
	
	public synchronized void take(){
		this.signal = true;
		this.notify();
	}
	
	public synchronized void release() throws InterruptedException{
		while(!this.signal)
			this.wait();
		this.signal = false;
	}

}
