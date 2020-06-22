package com.taichuan.code.tclog.extracter;

import com.taichuan.code.tclog.thread.TcLogGlobalThreadManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gui
 * @date 2020/5/19
 * <p>
 * logcat log提取器。 通过Logcat获取最近的log。
 */
public class LogcatExtractor extends BaseLogExtractor {
    @Override
    public void extract(final ExtractCallBack extractCallBack) {
        TcLogGlobalThreadManager.getInstance().addRun(new Runnable() {
            @Override
            public void run() {
                File dir = new File(TEMP_LOG_DIR);
                if (!dir.exists()) {
                    boolean b = dir.mkdirs();
                    if (!b) {
                        extractFail("create file fail,please check permission", extractCallBack);
                        return;
                    }
                }
                FileOutputStream fos = null;
                BufferedReader buffRead = null;
                boolean isSuccess = false;
                File tempLogFile = null;
                try {
                    //命令的准备*/
                    List<String> getLog = new ArrayList<>();
                    getLog.add("logcat");
                    getLog.add("-d");
                    getLog.add("-v");
                    getLog.add("time");
                    //抓取当前的缓存日志
                    Process process = Runtime.getRuntime().exec(getLog.toArray(new String[getLog.size()]));
                    //获取输入流
                    buffRead = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String str;
                    tempLogFile = new File(TEMP_LOG_DIR, TEMP_LOG_FILE);
                    fos = new FileOutputStream(tempLogFile);
                    while ((str = buffRead.readLine()) != null) {
                        fos.write(str.getBytes(StandardCharsets.UTF_8));
                        //换行
                        fos.write(System.getProperty("line.separator").getBytes());
                    }
                    isSuccess = true;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (buffRead != null) {
                        try {
                            buffRead.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (isSuccess) {
                    extractSuccess(tempLogFile, extractCallBack);
                } else {
                    if (tempLogFile != null && tempLogFile.exists()) {
                        tempLogFile.delete();
                    }
                    extractFail("extract log fail,IOException", extractCallBack);
                }
            }
        });
    }

    private void extractSuccess(File file, ExtractCallBack extractCallBack) {
        if (extractCallBack != null) {
            extractCallBack.onSuccess(file);
        }
    }

    private void extractFail(String msg, ExtractCallBack extractCallBack) {
        if (extractCallBack != null) {
            extractCallBack.onFail(msg);
        }
    }
}
