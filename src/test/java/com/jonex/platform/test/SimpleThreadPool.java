package com.jonex.platform.test;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jetty.util.thread.ThreadPool;

public final class SimpleThreadPool {
	private final int worker_num;
	private WorkerThread[] workerThrads;
	private List<Runnable> taskQueue = new LinkedList<Runnable>();
	private static ThreadPool threadPool;

	public SimpleThreadPool(int worker_num) { 
		this.worker_num = worker_num; 
		workerThrads = new WorkerThread[worker_num]; 
		for (int i = 0; i < worker_num; i++) { 
			workerThrads[i] = new WorkerThread(); 
			workerThrads[i].start(); 
		} 
	}

	public void execute(Runnable task) {
		synchronized (taskQueue) {
			taskQueue.add(task);
		}
	}

	private class WorkerThread extends Thread {
		public void run() {
			Runnable r = null;
			while (true) {
				synchronized (taskQueue) {
					if (!taskQueue.isEmpty()) {
						r = taskQueue.remove(0);
						r.run();
					}
				}
			}
		}
	}
}
