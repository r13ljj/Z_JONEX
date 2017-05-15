package com.jonex.platform.test;

/**
 * 
 * 给你一个数组A[0..n]，请你在O(n)的时间里构造一个新的数组B[0..n]，使得B[i]=A[0]*A[1]*A[2]*…*A[n]/A[i]，但是不能使用除法运算。
 *
 */
public class ArrayTest {
	
	public static void main(String[] args) {
		int[] source = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		//打印源数组
		for(int i=0; i<source.length; i++){
			System.out.println("源数组:"+source[i]);
		}
		//计算目标数组
		int[] target = calculateArray(source);
		for(int i=0; i<target.length; i++){
			System.out.println("目标数组:"+target[i]);
		}
	}
	
	
	public static int[] calculateArray(int[] source){
		int length = source.length;
		int[] temp = new int[length];
		
		for(int i=0; i<length; i++){
			if(i==0){
				temp[i] = source[i];
			}else{
				temp[i] = temp[i-1] * source[i];
			}
		}
		return temp;
	}

}
