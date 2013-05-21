package com.jonex.platform.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import com.jonex.platform.util.exception.PlatformException;
import com.jonex.platform.util.io.FileUtils;

/**
 * 对字符串的一些常用方法 
 * @author 
 * 2009-3-25
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

    private StringUtils() {
    }

    /**
     * 测试字符串是否为null或者字符串
     * @param str 被测试的字符串
     * @return boolean 如果为null或者空字符串，返回true，否则返回false
     * @author jonex
     * 2009-3-25
     */
    public static boolean isEmpty(String str) {
        return isBlank(str);
    }
    
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 判断字符串是否有效数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
    	return (!isEmpty(str))&&(org.apache.commons.lang.StringUtils.isNumeric(str));
    }
    
    public static boolean isNotNumeric(String str) {
    	return !isNumeric(str);
    }
    
    /**
     * 去除字符串首尾空格
     * @param s
     * @return String 如果字符串为null，则返回空字符串
     * @author jonex
     * 2009-3-26
     */
    public static String trim(String s) {
        return trimToEmpty(s);
    }
    
    /**
     * 去除字符串右边的空格
     * @param s
     * @return String
     * @author jonex
     * 2009-3-26
     */
    public static String rtrim(String s) {
        return stripEnd(s, null);
    }
    
    /**
     * 去除字符串左边的空格
     * @param s
     * @return String
     * @author jonex
     * 2009-3-26
     */
    public static String ltrim(String s) {
        return stripStart(s, null);
    }

    /**
     * 去除掉从页面传递过来的作为查询条件的字符串中具有SQL特殊的字符，会先去掉空格
     * 仅用于查询页面作为避免SQL注入的手段,如果仅仅是需要去掉空格不需要使用这个方法
     * @param mess
     * @return
     */
    public static String checkSQL(String mess) {
        mess = trim(mess);
        mess = mess.replaceAll("'", "''");
        return mess;
    }

    /**
     * 将换行符、制表符和空格转化成html用的格式
     * @param message 被转化的字符串
     * @return String 转化的字符串
     * @author jonex
     * 2009-3-25
     */
    public static String convertToHtml(String message) {
        if (isEmpty(message)) {
            return "&nbsp;";
        }
        message = message.replaceAll("\n", "<br/>");
        message = message.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        message = message.replaceAll(" ", "&nbsp;");
        return message;
    }

    /**
     * 转化HTML格式<与>符号
     * @param message
     * @return String 转化的字符串
     * @author jonex
     * 2009-3-25
     */
    public static String convertToWord(String message) {
        if (isEmpty(message)) {
            return "";
        }
        message = message.replaceAll("<", "&lt;");
        message = message.replaceAll(">", "&gt;");
        return message;
    }
    
    /**
     * 获取异常错误具体信息
     * @param e
     * @return （String）错误信息
     * @author jonex
     * 2009-3-25
     */
    public static String getExcepitonInfo(Throwable e){
        StringWriter errorInfo = new StringWriter();
        if(e!=null){
            PrintWriter print = new PrintWriter(errorInfo);
            e.printStackTrace(print);
        }
        return errorInfo.toString();
    }
    
    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }
    
    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString();
    }
    
    
    public static void main(String[] args) {
        String content = "";
        String filePath = "D:\\logo.gif";
        File file = new File(filePath);
        if(file.exists()){
            InputStream inpus = null;
            ByteArrayOutputStream outputStream = null;
            try{
                inpus = new FileInputStream(file);
                outputStream = new ByteArrayOutputStream();
                
                int in = 0;
                while((in = inpus.read())!=-1){
                    outputStream.write(in);
                }
                content = toHexString(outputStream.toByteArray());
            }catch(Exception e){
                e.printStackTrace();
                throw new PlatformException(e);
            }finally{
                CloseUtils.closeOutputStream(outputStream);
                CloseUtils.closeInputStream(inpus);
            }
        }
        System.out.println("xx:\n"+content);
        
        byte[] btts = convertHexString(content);
        
        FileUtils.createFile(btts, "C:\\23.gif");
        
    }
}
