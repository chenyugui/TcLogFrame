package com.taichuan.code.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public  class HttpDownloadUtil {
    private static URL url = null;

    /**
     * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容 1.创建一个URL对象
     * 2.通过URL对象，创建一个HttpURLConnection对象 3.得到InputStram 4.从InputStream当中读取数据
     *
     * @param urlStr
     * @return
     */
    public String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        try {
            // 创建一个URL对象
            url = new URL(urlStr);
            // 创建一个Http连接
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            // 使用IO流读取数据
            buffer = new BufferedReader(new InputStreamReader(
                    urlConn.getInputStream()));
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            System.out.println("download:" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
            } catch (Exception e) {
                System.out.println("download buffer.close():" + e.getMessage());
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 该函数返回整形 -1：代表下载文件出错 0：代表下载文件成功 1：代表文件已经存在
     */
    public static int downFile(String urlStr, String path, String fileName) {
        InputStream inputStream = null;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            File file = new File(path + fileName);
            if (file.exists()) {
                return 1;
            } else {
                inputStream = getInputStreamFromUrl(urlStr);
                File resultFile =write2SDFromInput(path, fileName,
                        inputStream);
                if (resultFile == null) {
                    return -1;
                }
            }
        } catch (Exception e) {
            System.out.println("downFile:" + e.getMessage());
            e.printStackTrace();
            return -1;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                System.out.println("downFile close:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static InputStream getInputStreamFromUrl(String urlStr)
            throws MalformedURLException, IOException {
        url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConn.getInputStream();
        return inputStream;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public static File write2SDFromInput(String path, String fileName, InputStream input){

//        File file = null;
//        OutputStream output = null;
//        try{
//            file = new File(path + fileName);
//            output = new FileOutputStream(file);
//            byte buffer [] = new byte[4 * 1024];
//            while((input.read(buffer)) != -1){
//                output.write(buffer);
//            }
//            output.flush();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        finally{
//            try{
//                output.close();
//            }
//            catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//        return file;
        return null;
    }



}
