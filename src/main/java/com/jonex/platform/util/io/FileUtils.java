package com.jonex.platform.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.jonex.platform.util.StringUtils;
import com.jonex.platform.util.exception.PlatformException;

import filter.CSRF.config.Configuration;

public class FileUtils {

    private static Logger log = Logger.getLogger(FileUtils.class);

    /**
     * 判断该文件路径是否真实存在
     * 
     * @param filePath
     */
    public static boolean isExists(String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            return file.exists();
        }
        return false;
    }

    /**
     * 统一文件拼接的路径
     * 
     * @return
     */
    public static String controlPath(String appendPath) {
        String path = "";
        if (!StringUtils.isEmpty(appendPath)) {
            if (appendPath.startsWith("/") || appendPath.startsWith("\\")) {
                path = appendPath.substring(1);
            } else {
                path = appendPath;
            }
        }
        return path;
    }

    /**
     * 如果文件目录不存在则创建
     * 
     * @param filePath
     */
    public static void createNoExists(String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    /**
     * 根据文件路径解析文件名
     * 
     * @param filePath
     * @return
     */
    public static String parseFileName(String filePath) {
        String fileName = filePath;
        if (!StringUtils.isEmpty(filePath)) {
            filePath = filePath.replace(File.separator, "/");
            String[] parts = filePath.split("/");
            if(parts!=null && parts.length > 0){
                fileName = parts[parts.length-1];
            }
        }

        return fileName;
    }

    /**
     * 获取文件后缀名
     * @return
     */
    public static String getFileFormatter(String filename){
        String fileFormat = "";
        if(!StringUtils.isEmpty(filename) ){
            int x = filename.lastIndexOf(".");
            if(x >0){
                fileFormat = filename.substring(x);
            }
        }    
        return fileFormat;
    }
    
    /**
     * 删除文件
     * 
     * @param filePath
     */
    public static void deleteFile(String filePath) throws PlatformException {
        File file = new File(filePath);
        boolean flag = false;
        if (file.isFile() && file.exists()) {
            flag = file.delete();
            log.info("删除文件:" + (flag ? "成功" : "失败"));
        } else {
            log.error("文件[" + filePath + "]不存在或者不是文件,无法删除");
        }
    }

    /**
     * 根据 字节数组 和 文件路径 创建文件
     * 
     * @param btyes
     * @param filePath
     * @throws PlatformException
     */
    public static void createFile(byte[] btyes, String filePath) throws PlatformException {
        if (btyes != null && !StringUtils.isEmpty(filePath)) {
            BufferedOutputStream bops = null;
            try {
                bops = new BufferedOutputStream(new FileOutputStream(filePath));
                bops.write(btyes);
                bops.flush();
            } catch (Exception e) {
                e.printStackTrace();
                throw new PlatformException("创建文件到路径[" + filePath + "]出错:", e);
            } finally {
                CloseUtils.closeOutputStream(bops);
            }
        } else {
            log.error("无法用空的字节数组 或者 空的路径 创建文件！");
        }
    }

    /**
     * 根据 输入流 和 文件路径 创建文件
     * 
     * @param btyes
     * @param filePath
     * @throws PlatformException
     */
    public static void createFile(InputStream ins, String filePath) throws PlatformException {
        if (ins != null && !StringUtils.isEmpty(filePath)) {
            BufferedOutputStream bops = null;
            BufferedInputStream bips = null;
            try {
                bips = new BufferedInputStream(ins);
                bops = new BufferedOutputStream(new FileOutputStream(filePath));
                int byts = 0;
                while ((byts = bips.read()) != -1) {
                    bops.write(byts);
                }
                bops.flush();
            } catch (Exception e) {
                e.printStackTrace();
                throw new PlatformException("创建文件到路径[" + filePath + "]出错:", e);
            } finally {
                CloseUtils.closeInputStream(bips);
                CloseUtils.closeOutputStream(bops);
            }
        } else {
            log.error("无法用空的输入流 或者 空的路径 创建文件！");
        }
    }

    /**
     * 将文件路径的内容指向具体的输出流
     * 
     * @param filePath
     * @param outs
     * @throws PlatformException
     */
    public static void putFileToStream(String filePath, OutputStream outs) throws PlatformException {
        if (outs != null && !StringUtils.isEmpty(filePath)) {
            BufferedOutputStream bops = null;
            BufferedInputStream bips = null;
            try {
                bips = new BufferedInputStream(new FileInputStream(filePath));
                bops = new BufferedOutputStream(outs);
                int byts = 0;
                while ((byts = bips.read()) != -1) {
                    bops.write(byts);
                }
                bops.flush();
            } catch (Exception e) {
                e.printStackTrace();
                throw new PlatformException("读取文件[" + filePath + "]内容出错:", e);
            } finally {
                CloseUtils.closeInputStream(bips);
                CloseUtils.closeOutputStream(bops);
            }
        } else {
            log.error("无法用空的输出流 或者 空的路径 读取文件！");
        }
    }

    /**
     * 根据文件路径获取文件内容
     * 
     * @param filePath
     * @return
     */
    public static String getFileContext(String filePath) throws PlatformException {
        BufferedInputStream ins = null;
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                ins = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[(int) file.length()];
                int length = ins.read(buffer);
                return new String(buffer, 0, length);
            }
            throw new PlatformException("文件不存在 或者 不是[" + filePath + "]文件路径");
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
            throw new PlatformException("获取文件[" + filePath + "]的内容出错：", e);
        } finally {
            CloseUtils.closeInputStream(ins);
        }
    }
    
    /**
     * 用字符流读取文件内容
     * @param filePath
     * @param encoding
     * @return
     * @throws PlatformException
     */
    public static String getFileContext(String filePath,String encoding)throws PlatformException{
        InputStream  ins        = null;
        StringBuffer buffer     = new StringBuffer();  
        int n ;
        try{
             BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),encoding));
             while((n = reader.read()) != -1){  
                 buffer.append((char)n);  
             }  
             return buffer.toString();
        }catch(Exception e){
            e.printStackTrace();
            throw new PlatformException("获取文件["+filePath+"]的内容出错：",e);
        }finally{
            CloseUtils.closeInputStream(ins);
        }
    }

    /**
     * 根据文件路径判断文件是否可以直接在浏览器中打开
     * 
     * @param filePath
     * @return
     */
    public static boolean isDirectOpen(String filePath) {
        boolean openflag = false;
        if (!StringUtils.isEmpty(filePath)) {
            String filesuffixal = "";
            Pattern pattern = Pattern.compile("\\.[a-zA-Z0-9]{2,4}$");
            Matcher matcher = pattern.matcher(filePath);
            if (matcher.find()) {
                filesuffixal = matcher.group();
            }
            List<String> canDirectOpenFileFormat = Configuration.getPropertyList(
                    "system.downfile.directopen.format", ".txt,.jpg,.png,.gif,.html,.htm", ",");
            openflag = canDirectOpenFileFormat.contains(filesuffixal);
        }
        return openflag;
    }

    /**
     * 文件过滤器
     * 
     * @param filterString
     *            过滤的内容
     * @return
     */
    public static FileFilterHelper getFileFilter(String[] filterString) {
        return new FileUtils().new FileFilterHelper(filterString);
    }

    /**
     * 重载文件过滤器过单个限制
     * 
     * @return
     */
    public static FileFilterHelper getFileFilter(String filterString) {
        return getFileFilter(new String[] { filterString });
    }

    /**
     * 获取该路径的相关文件名
     * 
     * @param filePath
     * @param filterString
     * @return
     */
    public static String[] listFileNames(String filePath, String[] filterString) {
        File file = new File(filePath);
        if (file.exists()) {
            String[] fileNames = file.list(getFileFilter(filterString));
            return fileNames;
        }
        return null;
    }

    /**
     * 文件过滤器
     * 
     * @author 刘剑
     * 
     */
    class FileFilterHelper implements FileFilter, FilenameFilter {

        private String[] filterStr = null;

        public FileFilterHelper(String[] filterString) {
            this.filterStr = filterString;

        }

        /**
         * 当文件名称 包含 filterStr[]内容遍历出来
         */
        public boolean accept(File pathname) {
            return isAccept(pathname.getName());
        }

        public boolean accept(File dir, String name) {
            return isAccept(name);
        }

        private boolean isAccept(String fileName) {
            if (this.filterStr != null && this.filterStr.length > 0) {
                for (String filter : this.filterStr) {
                    if (fileName.toLowerCase().indexOf(filter.toLowerCase()) >= 0) {
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }
    }
    
    /**
     * 将文件转成Base64位的字符串
     * @throws Exception
     */
    public static String convertFiletoBase64String(String filePath)throws Exception{
        String content = "";
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
                content = Base64.encodeBase64String(outputStream.toByteArray());
            }catch(Exception e){
                e.printStackTrace();
                throw new PlatformException(e);
            }finally{
                CloseUtils.closeOutputStream(outputStream);
                CloseUtils.closeInputStream(inpus);
            }
        }
        return content;
    }
    
    /**
     * Base64位的字符串转成具体文件
     * @param conntent
     * @param filePath
     * @throws Exception
     */
    public static void convertBase64StringToFile(String conntent,String filePath)throws Exception{
        try{
            createFile(Base64.decodeBase64(conntent.getBytes()), filePath);
        }catch(Exception e){
            e.printStackTrace();
            throw new PlatformException(e);
        }
    }
}
