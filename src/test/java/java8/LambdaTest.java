package java8;

import java.util.Arrays;

public class LambdaTest {
	
	public static void main(String[] args) throws Exception{
		Arrays.asList("a", "b", "e").forEach(e -> System.out.println(e));
		Thread.sleep(5000);
	}

}
