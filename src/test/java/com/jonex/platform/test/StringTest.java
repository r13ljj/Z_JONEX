package com.jonex.platform.test;

public class StringTest {
    private String val1="a";
    private static String val2=StringTest.class.getName()+"b";

    public static void main(String args[]){
        StringTest st=new StringTest();
        String a="a";
        String d="a";
        String b=a+"b";
        String c="a"+"b";
        String e="ab";
        System.out.println(a+b+c+d+e);
    }
}