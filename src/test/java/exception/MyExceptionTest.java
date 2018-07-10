package exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *
 *  File: MyExceptionTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/7/10				lijunjun				Initial.
 *
 * </pre>
 */
public class MyExceptionTest {

    public static void main(String[] args) {

        Action test = new Action();
        try {
            test.doSome();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("============");
            e.printStackTrace();
        }
    }

    static class Action {
        public int doSome(){
            int a = 1;
            int b = 0;
            int result = 0;
            try {
                result = a / b;
            } catch (Exception e) {
                //throw new MyException(1000);
                //throw new MyException("doSome error", 1000);
                //throw new MyException("doSome error", e);
                //throw new MyException(1001, e);
                //throw new MyException("doSome error", e);
                throw new MyException("doSome error", 1001, e);
            }
            return result;
        }
    }

}
