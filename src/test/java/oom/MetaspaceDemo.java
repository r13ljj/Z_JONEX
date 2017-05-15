package oom;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

import java8.LambdaTest;

public class MetaspaceDemo {
	
	public static void main(String[] args)throws Exception {
		Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", 
				new Class[]{String.class, byte[].class, int.class, int.class});
		defineClassMethod.setAccessible(true);
		File file = new File("E:\\github\\Z_JONEX\\target\\test-classes\\java8\\LambdaTest.class");
		byte[] bcs = new byte[(int)file.length()];
		FileInputStream in = null;
		try{
			in = new FileInputStream(file);
			while(in.read(bcs) != -1){
				
			}
		}catch(Exception e){
			
		}finally{
			if(in != null){
				in.close();
			}
		}
		while(true){
			try{
				defineClassMethod.invoke(LambdaTest.class.getClassLoader(), new Object[]{"BBBB", bcs, 0, bcs.length});
			}catch(Exception e){
				
			}
		}
	}

}
