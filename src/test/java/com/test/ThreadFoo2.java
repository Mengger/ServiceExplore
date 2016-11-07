package com.test;

public class ThreadFoo2 implements Runnable {

	private TestSynchronizedWord a;
	ThreadFoo2 (TestSynchronizedWord aa){
		this.a=aa;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		a.foo2();
	}

}
