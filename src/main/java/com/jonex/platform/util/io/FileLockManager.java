package com.jonex.platform.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import com.jonex.platform.util.exception.PlatformException;

public class FileLockManager {
	
	public static final String FILE_LOCK_MODE = "rw";
	private String filePath = null;
	private FileLock fileLock = null;
	
	public FileLockManager(String filePath){
		this.filePath = filePath;
	}
	
	public boolean tryLock(){
		if(filePath == null || fileLock.isValid()){
			release();
		}
		File fileToLock = new File(this.filePath);
		RandomAccessFile raf = null;
		
		//预处理-创建目标文件
		if(!fileToLock.exists()){
			try{ 
				fileToLock.createNewFile();
			}catch(Exception f){
				throw new PlatformException("文件锁使用的文件[" + this.filePath + "]不存在并且无法创建!",f);
			} 
		}
		
		try {
			raf = new RandomAccessFile(fileToLock,"rw");
		} catch (FileNotFoundException e) { 
			throw new PlatformException("文件锁使用的文件[" + this.filePath + "]不存在!",e);
		}finally{
			try {
				raf.close();
			} catch (IOException e) {
			}
		}
		
		FileChannel fc = raf.getChannel();
		try {
			this.fileLock = fc.tryLock();
			if(this.isLock()){
				System.out.println("加锁[" + this.filePath + "]成功");  
			}else{
				System.out.println("加锁[" + this.filePath + "]失败"); 
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.isLock();
	}
	
	/**
	 * 释放文件锁
	 * @return
	 */
	public boolean release(){
		try{
			if((fileLock != null) && (fileLock.isValid())){
				fileLock.release();   
				System.out.println("释放文件锁[" + this.filePath + "]");
			} 
			return true; 
		}catch(Exception f){
			f.printStackTrace();    
			return false;
		}finally{
			try{
				fileLock.channel().close();
			}catch(Exception f){
				//f.printStackTrace();
			}
		}
	}
	
	/**
	 *  是否锁定
	 * @return
	 */
	public boolean isLock(){
		return (this.fileLock != null) && (this.fileLock.isValid());
	}
	
	/**
	 * 解锁
	 */
	public void unlock(){
		release();
	}
	
	/**
	 * gc
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		try{
			release();  
		}catch(Exception e){   
			e.printStackTrace();
		}  
		super.finalize();  
	}

}
