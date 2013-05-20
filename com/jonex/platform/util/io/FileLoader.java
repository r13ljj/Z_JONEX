package com.szboanda.platform.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;

public class FileLoader {

	
	/**
	 * 读取文件
	 * @param path
	 * @return
	 */
	public static InputStream loadFile(String path){
		URL url = FileLoader2.class.getResource(path);
		if(url == null){
			url = FileLoader2.class.getResource("/" + path);
		}
		if(url != null){
			try{
				return url.openStream();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return null;
	} 
	
	/**
	 * 读取web下的文件
	 * @param ctx
	 * @param configPath
	 * @return
	 */
	private static InputStream loadFile(ServletContext ctx, String configPath){
		ServletContext APPLICATION = ctx;
		String path = ctx.getRealPath(configPath);
		InputStream is = null;
		File configFile = null;
		if(path == null){
			is = APPLICATION.getResourceAsStream(configPath);
		}else{
			configFile = new File(path);
			try {
				is = new FileInputStream(configFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return is;
	}

}
