package com.test.testStartSequence;

public class SonClass extends ParentClass {

	public SonClass (){
		System.out.println("start sonClass constructor method ……");
	}
	
	static {
		System.out.println("load sonClass STATIC area ……");
	}
	
	static void method(){
		System.out.println("load sonClass static method ……");
	}
}
