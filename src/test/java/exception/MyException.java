package exception;

/**
 * <pre>
 *
 *  File: MyException.java
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
public class MyException extends RuntimeException {

    private int code;
    //private String message;

    public MyException(int code){
        super();
    }

    public MyException(String message, int code) {
        super(message);
        this.code = code;
    }

    public MyException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public MyException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
