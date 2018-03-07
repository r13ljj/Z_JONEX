/**
 * &lt;pre&gt;
 * <p>
 * File: ${file_name}
 * <p>
 * Copyright (c) ${year}, globalegrow.com All Rights Reserved.
 * <p>
 * Description:
 * ${todo}
 * <p>
 * Revision History
 * Date,					Who,					What;
 * ${date}				${user}				Initial.
 * <p>
 * &lt;/pre&gt;
 */
public class InitialTest {

    public static void main(String[] args) {
        Child child = new Child("jonex");
    }

}

class Child extends Parent{
    private static final String CHILD_CLASS_FIELD = "child class filed";

    private String childName;

    //inner block
    {
        System.out.println("child inner block");
    }

    //static inner block
    static{
        System.out.println("child static inner block");
    }

    public Child(){
        System.out.println("Child default constructor");
    }

    public Child(String name){
        this.childName = name;
        System.out.println("Child constructor name:"+name);
    }
}


class Parent{

    private static final String PARENT_CLASS_FIELD = "parent class filed";

    private String parentName;

    //inner block
    {
        System.out.println("parent inner block");
    }

    //static inner block
    static{
        System.out.println("parent static inner block");
    }

    public Parent(){
        System.out.println("parent default constructor");
    }

    public Parent(String name){
        this.parentName = name;
        System.out.println("parent constructor name:"+name);
    }



}
