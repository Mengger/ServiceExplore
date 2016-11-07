package com.test;

public class TestSynchronizedWord {
	
	public  void foo1(){
		synchronized(this){
			System.out.println("*************foo1************");
			System.out.println("*************foo1************");
			System.out.println("*************foo1************");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("*************foo1************");
			System.out.println("*************foo1************");
			System.out.println("*************foo1************");
			System.out.println();
			System.out.println();
		}
	}

	
	public synchronized void foo2(){
		System.out.println("*************foo2************");
		System.out.println("*************foo2************");
		System.out.println("*************foo2************");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("*************foo2************");
		System.out.println("*************foo2************");
		System.out.println("*************foo2************");
		System.out.println();
		System.out.println();

	}
	
	public static void main(String[] args) {
		for(int i=0;i<3;i++){
			TestSynchronizedWord A=new TestSynchronizedWord();
			new Thread(new ThreadFoo1(A)).start();
			new Thread(new ThreadFoo2(A)).start();
			//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$");
		}
	}
}
