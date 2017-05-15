package classloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.WeakHashMap;

public class TestLoader extends URLClassLoader{
	
	public static WeakHashMap<TestLoader, Object> map = new WeakHashMap<TestLoader, Object>();
	private static int count = 0;
	
	
	
	public TestLoader(URL[] urls) {
		super(urls);
		map.put(this, new Object());
	}
	
	@SuppressWarnings("resource")
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if("AAB".equals(name) && count == 0){
			try {
				count = 1;
				URL[] urls = new URL[1];
				urls[0] = new URL("");
				return new TestLoader(urls).loadClass("AAB");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}else{
			return super.loadClass(name);
		}
		return super.loadClass(name);
	}
	
}
