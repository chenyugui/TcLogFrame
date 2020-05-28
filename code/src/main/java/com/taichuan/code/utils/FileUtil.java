package com.taichuan.code.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * Created by gui on 2017/7/21.
 * 文件操作工具类
 */
public class FileUtil {
    private static final String TAG = "FileUtil";
    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getPath();

    private static final String DEFAULT_DIR = SDCARD_DIR + "/downloads/";

    public static void sortFileByName(List<File> fileList) {
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile()) {
                    return -1;
                }
                if (o1.isFile() && o2.isDirectory()) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    /**
     * 根据扩展名生成File对象，例如（PNG_yyyy_MM_dd_HH_mm_ss)
     *
     * @param dir       文件目录
     * @param extension 文件扩展名，可为空
     */
    private static File createFileByTime(String dir, String extension) {
        // 去掉点
        if (extension != null && extension.indexOf(".") == 0) {
            extension = extension.substring(1, extension.length());
        }
        StringBuilder sb = new StringBuilder();
        if (extension == null || extension.isEmpty()) {
            sb.append("FILE")// 前缀
                    .append("_")
                    .append(TimeUtil.dateToyyyyMMdd_HHmmss(new Date()));
        } else {
            sb.append(extension.toUpperCase())// 前缀
                    .append("_")
                    .append(TimeUtil.dateToyyyyMMdd_HHmmss(new Date()))
                    .append(".")
                    .append(extension);// 后缀
        }
        return new File(dir, sb.toString());
    }

    /**
     * 检查目录是否存在，不存在则创建
     *
     * @return 是否创建成功
     */
    @SuppressWarnings({"SimplifiableIfStatement", "WeakerAccess"})
    public static boolean createDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    /**
     * 获取文件的后缀名
     */
    @SuppressWarnings("WeakerAccess")
    public static String getExtension(String filePath) {
        String suffix = "";
        final File file = new File(filePath);
        final String name = file.getName();
        final int idx = name.lastIndexOf('.');
        if (idx > 0) {
            suffix = name.substring(idx + 1);
        }
        return suffix;
    }

    /**
     * 将字符串写入一个新的文件里
     *
     * @param content  要写的内容
     * @param fileDir  文件的目录
     * @param fileName 文件的名称
     * @return
     */
    public static boolean writeStringToNewFile(String content, String fileDir, String fileName) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(fileDir) || TextUtils.isEmpty(fileName))
            return false;
        if (FileUtil.createDir(fileDir)) {// 创建父文件夹
            File file = new File(fileDir, fileName);
            try {
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                //写入相关Log到文件
                bw.write(content);
                bw.newLine();
                bw.close();
                fw.close();
                return true;
            } catch (IOException e) {
                Log.e(TAG, "an error occured while writing file...", e);
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 读取文字型文件的内容
     */
    public static String readFileString(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String buffer;
            while ((buffer = bufferedReader.readLine()) != null) {
                sb.append(buffer);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public interface WriteToDiskCallBack {
        void onProgressUpdate(int progress);
    }

    /**
     * 将崩溃写入文件系统
     */
    public static void writeCrashInfoToFile(Throwable ex, String fileDir, String fileName) {
        StringBuilder sb = new StringBuilder();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        //这里把刚才异常堆栈信息写入SD卡的Log日志里面
        FileUtil.writeStringToNewFile(sb.toString(), fileDir, fileName);
    }

    /**
     * 通过输入流，写入文件到磁盘
     *
     * @param is         输入流
     * @param dir        文件保存路径，为空则使用默认的目录
     * @param extension  文件扩展名（当fileName不为空时才生效）
     * @param fileName   文件全名，为空则自动使用"扩展名_yyyyMMdd_HHmmss.扩展名" 的形式命名
     * @param fileLength 文件大小
     */
    public static File writeToDisk(InputStream is, String dir, String extension, String fileName, long fileLength, WriteToDiskCallBack writeToDiskCallBack) {
        if (is == null)
            return null;
        if (dir == null || dir.isEmpty()) {
            Log.w(TAG, "writeToDisk: dir is empty, use default dir");
            dir = DEFAULT_DIR;
        }
        boolean createDirResult = createDir(dir);
        LogUtil.d(TAG, "writeToDisk: createDirResult=" + createDirResult);
        File file;
        if (fileName == null || fileName.isEmpty()) {// 文件名为空，使用扩展名_yyyyMMdd_HHmmss.扩展名的形式
            file = createFileByTime(dir, extension);
        } else {
            file = new File(dir, fileName);
        }
        return writeToDisk(is, file, fileLength, writeToDiskCallBack);
    }

    @SuppressWarnings("WeakerAccess")
    public static File writeToDisk(InputStream is, File file, long fileLength, WriteToDiskCallBack writeToDiskCallBack) {
        if (is == null || file == null)
            return null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte data[] = new byte[1024 * 4];

            int length;
            int receivedAllLength = 0;//已接收的大小
            while ((length = bis.read(data)) != -1) {
                bos.write(data, 0, length);
                receivedAllLength = receivedAllLength + length;
                if (fileLength > 0) {
                    if (writeToDiskCallBack != null) {
                        writeToDiskCallBack.onProgressUpdate(receivedAllLength);
                    }
                }
            }
            bos.flush();
            fos.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 安装APK
     *
     * @param file        apk文件
     * @param authorities 清单文件注册的FileProvider的authorities
     */
    public static void installApk(File file, Context context, String authorities) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) {//7.0及以上
            //参数1 上下文
            //参数2 Provider主机地址 和配置文件中保持一致
            //参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, authorities, file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {// 7.0以下
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            Log.d(TAG, "autoInstallApk: ");
        }
        context.startActivity(intent);
    }

    public static boolean isApk(File file) {
        String extension = getExtension(file.getPath());
        return extension.equals("apk") || extension.equals("APK");
    }

    /**
     * 获取单个文件的MD5值！
     */
    @SuppressWarnings("WeakerAccess")
    public static String getFileMD5(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        String md5 = bigInt.toString(16);
        return (md5.length() == 31) ? ("0" + md5) : md5;
    }

    /**
     * 获取文件夹中所有文件的MD5值
     *
     * @param listChild 是否递归子目录中的文件
     * @return Map的Key为文件的路径
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static Map<String, String> getDirMD5(File file, boolean listChild) {
        if (!file.isDirectory()) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        String md5;
        File files[] = file.listFiles();
        for (File f : files) {
            if (f.isDirectory() && listChild) {
                Map<String, String> subMap = getDirMD5(f, true);
                if (subMap != null) {
                    map.putAll(subMap);

                }
            } else {
                md5 = getFileMD5(f);
                if (md5 != null) {
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }

    /**
     * 校验文件MD5
     *
     * @param file      要检验的文件
     * @param targetMD5 判断文件是否是这个MD5值
     */
    @SuppressWarnings("unused")
    public static boolean checkFileMD5(File file, String targetMD5) {
        // 若存在，验证MD5
        String md5 = getFileMD5(file);
        Log.v(TAG, "local file's md5 = " + md5);
        if (TextUtils.isEmpty(md5)) {
            return false;
        }
        if (md5.equalsIgnoreCase(targetMD5)) {
            // 验证通过
            Log.v(TAG, "check md5 pass");
            return true;
        } else {
            // 验证不通过
            Log.v(TAG, "check md5 unPass, del file");
            return false;
        }
    }


    /**
     * 解压本地文件
     *
     * @param isDeleteZipFile 解压完毕后是否删除压缩文件
     * @return 是否解压成功
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean unzipFile(String dir, String fileName, boolean isDeleteZipFile) {
        try {
            if (dir.lastIndexOf("/") != dir.length() - 1)
                dir = dir + "/";
            InputStream in = new FileInputStream(dir + fileName);
            ZipInputStream zipInputStream = new ZipInputStream(in);
            ZipUtil.uncompress(zipInputStream, dir);
            if (isDeleteZipFile) {
                File file = new File(dir, fileName);
                if (file.exists()) {
                    file.delete();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean deleteDirWithFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return false;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWithFile(file); // 递规的方式删除文件夹
        }
        return dir.delete();// 删除目录本身
    }

    /**
     * 删除文件夹和文件夹里面的文件
     */
    public static boolean deleteDir(String filePath) {
        File dir = new File(filePath);
        return deleteDirWithFile(dir);
    }

    /**
     * 删除文件夹和文件夹里面的文件
     */
    public static boolean deleteDir(File file) {
        return deleteDirWithFile(file);
    }

    /**
     * @param isDeleteDir 是否是删除文件夹
     */
    private static boolean deleteFile(File file, boolean isDeleteDir) {
        if (file.exists()) {
            if (isDeleteDir) {
                return deleteDirWithFile(file);
            } else {
                return file.delete();
            }
        } else {
            LogUtil.d(TAG, "deleteFile: 文件不存在");
            return false;
        }
    }

    public static boolean deleteFile(String filePath) {
        if (filePath != null && !filePath.equals("")) {
            File file = new File(filePath);
            return deleteFile(file, false);
        }
        return false;
    }

    public static boolean deleteFile(String dir, String fileName) {
        if (dir != null && !dir.equals("") && fileName != null && !fileName.equals("")) {
            File file = new File(dir, fileName);
            return deleteFile(file, false);
        }
        return false;
    }

    /***
     * 获取指定目录下的所有的文件（不包括文件夹），采用了递归
     *
     * @param obj File or FilePath
     */
    @SuppressWarnings("WeakerAccess")
    public static ArrayList<File> getListFiles(Object obj) {
        File directory;
        if (obj instanceof File) {
            directory = (File) obj;
        } else {
            directory = new File(obj.toString());
        }
        ArrayList<File> files = new ArrayList<>();
        if (directory.isFile()) {
            files.add(directory);
            return files;
        } else if (directory.isDirectory()) {
            File[] fileArr = directory.listFiles();
            if (fileArr != null && fileArr.length > 0) {
                for (File fileOne : fileArr) {
                    files.addAll(getListFiles(fileOne));
                }
            }
        }
        return files;
    }

    /***
     * 获取指定目录下的所有的文件夹
     * @param obj File or FilePath
     */
    public static ArrayList<File> getListDirectory(Object obj) {
        File directory;
        if (obj instanceof File) {
            directory = (File) obj;
        } else {
            directory = new File(obj.toString());
        }
        ArrayList<File> files = new ArrayList<>();
        if (directory.isDirectory()) {
            File[] fileArr = directory.listFiles();
            for (File subFile : fileArr) {
                if (subFile.isDirectory()) {
                    files.add(subFile);
                }
            }
        }
        return files;
    }
}
