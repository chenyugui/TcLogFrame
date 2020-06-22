package com.taichuan.code.tclog.extracter;

import java.io.File;

/**
 * @author gui
 * @date 2020/5/19
 */
public interface LogExtractor {
    interface ExtractCallBack {
        /**
         * 提取日志成功
         * @param logFile 提取到的日志文件
         */
        void onSuccess(File logFile);

        void onFail(String errMsg);
    }

    /**
     * 提取日志
     *
     * @param extractCallBack 提取日志回调
     */
    void extract(ExtractCallBack extractCallBack);
}
