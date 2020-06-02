package com.taichuan.code.tclog.extracter;

import android.text.TextUtils;

import com.taichuan.code.tclog.config.LogConfig;
import com.taichuan.code.tclog.thread.TcLogGlobalThreadManager;
import com.taichuan.code.tclog.util.FileUtil;
import com.taichuan.code.tclog.util.TimeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gui
 * @date 2020/5/19
 * 按时间区间提取器。 时间具体到天
 */
public class TimeLogExtracter extends BaseLogExtracter {
    private String beginDayString;
    private String endDayString;
    private LogConfig logConfig;

    public TimeLogExtracter(String beginDayString, LogConfig logConfig) {
        this(beginDayString, null, logConfig);
    }

    public TimeLogExtracter(String beginDayString, String endDayString, LogConfig logConfig) {
        this.beginDayString = beginDayString;
        this.endDayString = endDayString;
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
        final String dirPath = logConfig.getDirPath();
        if (TextUtils.isEmpty(dirPath)) {
            extractFail("dir err", extractCallBack);
            return;
        }
        if (beginDayString == null || beginDayString.length() != "yyyy-MM-dd".length()) {
            extractFail("beginDayString err", extractCallBack);
            return;
        }
        if (endDayString != null && endDayString.length() != "yyyy-MM-dd".length()) {
            extractFail("endDayString err", extractCallBack);
            return;
        }
        if (endDayString == null) {
            endDayString = TimeUtil.dateToyyyy_MM_dd(new Date());
        }
        if (endDayString.compareTo(beginDayString) < 0) {
            extractFail("beginDayString > endDayString", extractCallBack);
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
                File[] logFileList = logDirFile.listFiles();
                if (logFileList == null || logFileList.length == 0) {
                    extractFail("no log", extractCallBack);
                    return;
                }
                List<File> targetFileList = new ArrayList<>();
                for (File logFile : logFileList) {
                    if (!logFile.isFile()) {
                        continue;
                    }
                    if (logFile.getName().length() <= "yyyy-MM-dd".length()) {
                        continue;
                    }
                    String fileDay = logFile.getName().substring(0, "yyyy-MM-dd".length());
                    if (fileDay.compareTo(beginDayString) >= 0
                            && fileDay.compareTo(endDayString) <= 0) {
                        targetFileList.add(logFile);
                    }
                }
                if (targetFileList.size() == 0) {
                    extractFail("no log", extractCallBack);
                    return;
                }
                FileUtil.sortFileByNameIncrease(targetFileList);


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
                    for (File logFile : targetFileList) {
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
