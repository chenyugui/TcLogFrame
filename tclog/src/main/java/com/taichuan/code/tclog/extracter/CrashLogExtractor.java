package com.taichuan.code.tclog.extracter;

import android.text.TextUtils;

import com.taichuan.code.tclog.config.LogConfig;
import com.taichuan.code.tclog.thread.TcLogGlobalThreadManager;
import com.taichuan.code.tclog.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author gui
 * @date 2020/5/19
 * 奔溃日志提取器
 */
public class CrashLogExtractor extends BaseLogExtractor {
    private LogConfig logConfig;
    private int extracterCount;

    public CrashLogExtractor(int extracterCount, LogConfig logConfig) {
        this.extracterCount = extracterCount;
        this.logConfig = logConfig;
    }

    @Override
    public void extract(final ExtractCallBack extractCallBack) {
        if (logConfig == null) {
            extractFail("logConfig is null", extractCallBack);
            return;
        }
        if (!logConfig.isUseDiskSave()) {
            extractFail("Useless disk", extractCallBack);
            return;
        }
        final String dirPath = logConfig.getCrashPath();
        if (TextUtils.isEmpty(dirPath)) {
            extractFail("dir err", extractCallBack);
            return;
        }
        TcLogGlobalThreadManager.getInstance().addRun(new Runnable() {
            @Override
            public void run() {
                File logDirFile = new File(dirPath);
                if (!logDirFile.exists()) {
                    extractFail("no log", extractCallBack);
                    return;
                }
                File[] logFileArray = logDirFile.listFiles();
                if (logFileArray == null || logFileArray.length == 0) {
                    extractFail("no log", extractCallBack);
                    return;
                }
                List<File> logFileList = new ArrayList<>(Arrays.asList(logFileArray));
                if (logFileList.size() == 0) {
                    extractFail("no log", extractCallBack);
                    return;
                }
                FileUtil.sortFileByNameDecrease(logFileList);
                List<File> targetLogList;
                if (extracterCount == 0 || extracterCount >= logFileList.size()) {
                    targetLogList = logFileList;
                } else {
                    targetLogList = new ArrayList<>();
                    for (int i = 0; i < logFileList.size(); i++) {
                        if (extracterCount > i) {
                            targetLogList.add(logFileList.get(i));
                        } else {
                            break;
                        }
                    }
                }
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
                    tempLogFile = new File(TEMP_LOG_DIR, TEMP_LOG_FILE);
                    if (tempLogFile.exists()) {
                        tempLogFile.delete();
                    }
                    fos = new FileOutputStream(tempLogFile, true);

                    for (File logFile : targetLogList) {
                        if (!logFile.exists()) {
                            continue;
                        }
                        //获取输入流
                        buffRead = new BufferedReader(new FileReader(logFile));
                        // 开始写
                        String str;
                        while ((str = buffRead.readLine()) != null) {
                            fos.write(str.getBytes(StandardCharsets.UTF_8));
                            //换行
                            fos.write(System.getProperty("line.separator").getBytes());
                        }
                        buffRead.close();
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
