package javaagent.asm;


/**
 * Created by xubai on 2018/10/17 下午1:50.
 */
public class MyClassLoader extends ClassLoader {

    public MyClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }

    public Class<?> defineClassForName(String className, byte[] classBytes){
        return super.defineClass(className, classBytes, 0, classBytes.length);
    }


}
