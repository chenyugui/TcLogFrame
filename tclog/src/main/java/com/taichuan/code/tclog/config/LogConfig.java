package com.taichuan.code.tclog.config;

import android.os.Environment;

import java.io.File;

/**
 * @author gui
 * @date 2020/5/9
 */
public class LogConfig {
    /*** 存储log的文件夹的最大容量(单位字节) */
    private long dirMaxSize;
    /*** log内存缓存的最大容量(单位字节) */
    private long cacheMaxSize;
    /*** 存储异常日志的文件夹的最大容量(单位字节) */
    private long crashMaxSize;
    /*** 存储log的文件夹的路径 */
    private String dirPath;
    /*** 存储奔溃日志的文件夹的路径 */
    private String crashPath;
    /*** 是否使用内存缓存 */
    private boolean isUseCache;
    /*** 是否使用磁盘存储 */
    private boolean isUseDiskSave;
    /*** 是否存储奔溃日志 */
    private boolean isUseCrashSave;

    public long getDirMaxSize() {
        return dirMaxSize;
    }

    public long getCacheMaxSize() {
        return cacheMaxSize;
    }

    public String getDirPath() {
        return dirPath;
    }

    public boolean isUseCache() {
        return isUseCache;
    }

    public boolean isUseDiskSave() {
        return isUseDiskSave;
    }

    public long getCrashMaxSize() {
        return crashMaxSize;
    }

    public String getCrashPath() {
        return crashPath;
    }

    public boolean isUseCrashSave() {
        return isUseCrashSave;
    }

    private LogConfig(boolean isUseCache, boolean isUseDiskSave, boolean isUseCrashSave, long dirMaxSize, long cacheMaxSize, long crashMaxSize, String dirPath, String crashPath) {
        this.isUseCache = isUseCache;
        this.isUseDiskSave = isUseDiskSave;
        this.isUseCrashSave = isUseCrashSave;
        this.dirMaxSize = dirMaxSize;
        this.cacheMaxSize = cacheMaxSize;
        this.crashMaxSize = crashMaxSize;
        this.dirPath = dirPath;
        this.crashPath = crashPath;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean isUseCache;
        private boolean isUseDiskSave;
        private boolean isUseCrashSave;
        private long dirMaxSize;
        private long cacheMaxSize;
        private long crashMaxSize;
        private String dirPath;
        private String crashDirPath;

        /**
         * @param isUseCache   是否使用内存缓存
         * @param cacheMaxSize log内存缓存的最大容量(单位字节)
         */
        public Builder useCache(boolean isUseCache, long cacheMaxSize) {
            this.isUseCache = isUseCache;
            this.cacheMaxSize = cacheMaxSize;
            return this;
        }

        /**
         * @param isUseDiskSave 是否使用内磁盘缓存
         * @param dirMaxSize    log磁盘缓存的最大容量(单位字节)
         */
        public Builder useDiskSave(boolean isUseDiskSave, long dirMaxSize, String dirPath) {
            this.isUseDiskSave = isUseDiskSave;
            this.dirMaxSize = dirMaxSize;
            this.dirPath = dirPath;
            return this;
        }

        /**
         * @param isUseCrashSave 是否需要存储奔溃日志
         * @param crashMaxSize   奔溃日志log磁盘缓存的最大容量(单位字节)
         */
        public Builder useCrashSave(boolean isUseCrashSave, long crashMaxSize, String crashDirPath) {
            this.isUseCrashSave = isUseCrashSave;
            this.crashMaxSize = crashMaxSize;
            this.crashDirPath = crashDirPath;
            return this;
        }


        public LogConfig build() {
            if (isUseDiskSave) {
                if (dirPath == null) {
                    dirPath = Environment.getExternalStorageDirectory().getPath() + File.pathSeparator + "taichuan" + File.pathSeparator + "log" + File.pathSeparator;
                }
            }
            if (isUseCrashSave) {
                if (crashDirPath == null) {
                    crashDirPath = Environment.getExternalStorageDirectory().getPath() + File.pathSeparator + "taichuan" + File.pathSeparator + "crashLog" + File.pathSeparator;
                }
            }
            if (isUseDiskSave && isUseCrashSave && dirPath.equals(crashDirPath)) {
                throw new RuntimeException("奔溃日志和普通日志存储路径不能一样");
            }
            return new LogConfig(isUseCache, isUseDiskSave, isUseCrashSave, dirMaxSize, cacheMaxSize, crashMaxSize, dirPath, crashDirPath);
        }
    }
}
