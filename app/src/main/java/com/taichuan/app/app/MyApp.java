package com.taichuan.app.app;

import android.app.Application;

import com.taichuan.app.R;
import com.taichuan.app.utils.LogConfigUtil;
import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.tclog.TcLogger;
import com.taichuan.code.tclog.config.LogConfig;

import me.yokeyword.fragmentation.Fragmentation;

/**
 * @author gui
 * @date 2020/5/6
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppGlobal.init(this)
                .withApiHost("http://www.baidu.com/")
                .withThemeColor(getResources().getColor(R.color.themeColor))
                .configure();
        Fragmentation.builder()
                .debug(true)
//                .stackViewMode(Fragmentation.BUBBLE)
                .install();
        LogConfig logConfig = LogConfigUtil.createLogConfig();
        TcLogger.init(logConfig);

        CrashHandler crashHandler = new CrashHandler(this);
    }
}
