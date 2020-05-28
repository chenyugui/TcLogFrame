package com.taichuan.code.tclog.write;

import android.support.annotation.NonNull;

import com.taichuan.code.tclog.bean.Log;
import com.taichuan.code.tclog.enums.LogVersion;
import com.taichuan.code.tclog.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author gui
 * @date 2020/5/22
 */
public class DiskWriteLogic {
    private final String TAG = getClass().getSimpleName();
    private String dirPath;
    private String dirName;
    private String dateFormat;
    private long dirMaxSize;

    public DiskWriteLogic(String dirPath, String dirName, String dateFormat, long dirMaxSize) {
        this.dirPath = dirPath;
        this.dirName = dirName;
        this.dateFormat = dateFormat;
        this.dirMaxSize = dirMaxSize;
    }

    public void write(final Log log) {
        final File dir = new File(dirPath, dirName);
        if (dir.exists()) {
            checkCleanLog(dir, dirMaxSize / 2);
        }
        writeToDisk(dir, log);
    }

    private void writeToDisk(File dir, Log log) {
        if (log == null) {
            android.util.Log.e(TAG, "writeToDisk err: log=null");
            return;
        }
        if (!dir.exists()) {
            boolean hasFile = dir.mkdirs();
            if (hasFile) {
                System.out.println("dir not exists, create new file");
            } else {
                System.out.println("create dir fail");
            }
        }
        String fileName = getLogFileName(log);
        File logFile = new File(dir.getAbsolutePath(), fileName);
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            if (!logFile.exists()) {
                fileOutputStream = new FileOutputStream(logFile);
            } else {
                fileOutputStream = new FileOutputStream(logFile, true);
            }
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            outputStreamWriter.write(getLogFullContent(log));
            System.out.println("writed log");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getLogFileName(@NonNull Log log) {
        if (dateFormat == null) {
            return log.getTime() + ".log";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String logTimeStr = simpleDateFormat.format(new Date(log.getTime()));
        return logTimeStr + ".log";
    }

    /**
     * @param dir          存放log的文件夹
     * @param targetLength 目标剩余大小
     */
    private void checkCleanLog(File dir, long targetLength) {
        if (dir == null || !dir.exists() || targetLength < 0) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        // 文件夹大小
        long dirLength = 0;
        for (File file : files) {
            if (file.isFile()) {
                dirLength = dirLength + file.length();
            }
        }
        if (dirLength < targetLength) {
            System.out.println("本来就已经比目标大小小");
            return;
        }
        // 先按文件名排序
        List<File> fileList = Arrays.asList(files);
        FileUtil.sortFileByNameIncrease(fileList);
        List<File> fileListToClean = new ArrayList<>();
        long length = dirLength;
        for (File file : fileList) {
            fileListToClean.add(file);
            long fileLength = file.length();
            System.out.println(file.getName() + "  " + fileLength);
            length = length - fileLength;
            if (length <= targetLength) {
                break;
            }
        }
        for (File file : fileListToClean) {
            if (file.exists()) {
                file.delete();
            }
        }
//        cleanSuccess(cleanLogCallBack);
    }


    private String getLogFullContent(Log log) {
        // log例子： 2020-05-15 12:12:12.555/MainThread/D/TAG:/Content
        String time = dateToyyyy_MM_dd_HH_mm_ss_SSS(new Date(log.getTime()));
        String threadName = log.getThreadName();
        String tag = log.getTag();
        String content = log.getContent();
        String version = getVersionStr(log.getLogVersion());
        return String.format("%s %s/-%s-/%s: %s", time, threadName, version, tag, content) + "\n";
    }

    private String getVersionStr(@LogVersion int version) {
        if (version == LogVersion.VERBOSE) {
            return "V";
        }
        if (version == LogVersion.DEBUG) {
            return "D";
        }
        if (version == LogVersion.INFO) {
            return "I";
        }
        if (version == LogVersion.WARN) {
            return "W";
        }
        if (version == LogVersion.ERROR) {
            return "E";
        }
        return "UNKNOWN VERSION";
    }

    private String dateToyyyy_MM_dd_HH_mm_ss_SSS(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(date);
    }


    private static void cleanSuccess(CleanLogCallBack cleanLogCallBack) {
        if (cleanLogCallBack != null) {
            cleanLogCallBack.cleanSuccess();
        }
    }

    private interface CleanLogCallBack {
        void cleanSuccess();
    }
}
