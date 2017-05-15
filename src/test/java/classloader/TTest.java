package classloader;

import java.lang.reflect.Method;
import java.net.URL;

public class TTest {
	
	private Object aaa;
	
	public static void main(String[] args) {
		try {
			TTest tt = new TTest();
			test(tt);
			System.gc();
			System.out.println("finished");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void test(TTest tt){
		try {
			URL[] urls = new URL[1];
			urls[0] = new URL("file://data/classloader");
			tt.aaa = new TestLoader(urls).loadClass("AAA").newInstance();
			for(int i=0; i<10; i++){
				System.gc();
				Thread.sleep(1000);
			}
			Method[] methods = tt.aaa.getClass().getDeclaredMethods();
			for(Method m : methods){
				if(m.getName().equals("clear")){
					m.invoke(tt.aaa);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
