package com.test;

public class ThreadFoo1 implements Runnable {

	private TestSynchronizedWord a;
	ThreadFoo1 (TestSynchronizedWord aa){
		this.a=aa;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		a.foo1();
	}

}
