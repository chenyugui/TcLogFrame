package com.taichuan.code.tclog.extracter;

import java.io.File;

/**
 * @author gui
 * @date 2020/5/19
 */
public interface LogExtracter {
    interface ExtractCallBack {
        void onSuccess(File logFile);

        void onFail(String errMsg);
    }

    void extract(ExtractCallBack extractCallBack);
}
