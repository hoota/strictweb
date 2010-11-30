package ru.yandex.strictweb.scriptjava.test;

import ru.yandex.strictweb.scriptjava.base.NativeCode;

class SuperClass {
	int number = 0;
	
	public SuperClass() {
		number += 1;
	}
	
	void inc() {
		number += 10;
	}
}

class SuperClass1 extends SuperClass {
	
	public SuperClass1() {
		
	}
	
	public SuperClass1(int i) {
		number += i;
	}
	
	void inc() {
		super.inc();
		number += 1000;
	}
}

@SkipTest
public class TestInheritance extends SuperClass1 {
	
//	int n = 10;

	public TestInheritance() {
		super(100);
		number += 10000;
	}
	
	void inc() {
		super.inc();
		number += 100000;
	}
	
	public boolean test() {
		inc();
		
		UnitTest.println(number + " :: ");
		
		return number == 1111;
	}
	
	@NativeCode("{}")
	public static void main(String[] args) {
		System.out.println(new TestInheritance().test());
	}
}
