package com.test.testStartSequence;

public class ParentClass {

	public ParentClass(){
		System.out.println("start parent constructor ……");
	}
	
	static {
		System.out.println("load parent static area……");
	}
	
	static void method(){
		System.out.println("load parentClass static method ……");
	}
}
