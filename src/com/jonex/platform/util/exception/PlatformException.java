package com.jonex.platform.util.exception;


/**    
 *     
 * 平台异常通用类
 * @author: jonex 
 * 2013-5-22       
 * @version     
 *     
 */
public class PlatformException extends RuntimeException {

	private String code;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PlatformException() {
		super();
	}
	
	public PlatformException(String message) {
		super(message);
	}
	
	public PlatformException1(String message, String code) {
		super(message);
		this.code = code;
	}
	
	public PlatformException(Throwable cause) {
		super(cause);
	}
	
	public PlatformException(String message, Throwable cause) {
		super(cause);
		this.code = code;
	}

	
	public PlatformException(String message, Throwable cause, String code) {
		super(message, cause);
		this.code = code;
	}
	
	/**    
	 * 处理异常  
	 * @param   name   
	 * @param  @return    设定文件      
	 * @return String    DOM对象       
	 * @author jonex　
	 * 2013-5-22  
	 *  
	*/
	public static RuntimeException processException(Throwable e){
		if(e instanceof RuntimeException){
			return (RuntimeException)e;
		}else{
			return new PlatformException1(e);
		}
	}
}
