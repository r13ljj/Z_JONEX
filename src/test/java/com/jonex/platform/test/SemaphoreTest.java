package com.jonex.platform.test;

public class SemaphoreTest {
	
	public static void main(String[] args) throws Exception{
		Semaphore semaphore = new Semaphore();

		SendingThread sender = new SendingThread(semaphore);

		ReceivingThread receiver = new ReceivingThread(semaphore);

		receiver.start();

		sender.start();
	}
	
}

class SendingThread extends Thread{
	Semaphore semaphore = null;
	
	public SendingThread(Semaphore semaphore){
		this.semaphore = semaphore;
	}

	public void run(){
		while(true){
			try {
				System.out.println("SendingThread run");
				//do something, then signal
				this.semaphore.take();
				System.out.println("SendingThread run take");
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}


class ReceivingThread extends Thread {

	Semaphore semaphore = null;

	public ReceivingThread(Semaphore semaphore){
		this.semaphore = semaphore;
	}

	public void run(){
		while(true){
			try {
				System.out.println("ReceivingThread run");
				this.semaphore.release();
				System.out.println("ReceivingThread run release");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//receive signal, then do something...
		}
	}

}