package com.jonex.platform.test;

public class ClassLoaderTest {
	
	public static void main(String[] args) {
		System.out.println(ClassLoader.getSystemResource("java/lang/String.class"));
		System.out.println(ClassLoader.getSystemClassLoader());
		System.out.println(Thread.currentThread().getContextClassLoader());
		ClassLoader currentClassLoader = ClassLoaderTest.class.getClassLoader();
		System.out.println(currentClassLoader);
		System.out.println(currentClassLoader.getParent());
		System.out.println(currentClassLoader.getParent().getParent());
		
	}

}
