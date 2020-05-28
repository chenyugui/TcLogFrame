package com.taichuan.code.app;

import android.content.Context;
import android.os.Handler;

/**
 * Created by gui on 2017/9/28.
 * 全局管理类，提供常用的
 */
public class AppGlobal {
    private static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    public static Configurator init(Context context) {
        getConfigurator().withApplicationContext(context.getApplicationContext());
        return getConfigurator();
    }

    public static <T> T getConfiguration(ConfigType configType) {
        return getConfigurator().getConfiguration(configType);
    }

    public static Handler getHandler() {
        return getConfiguration(ConfigType.HANDLER);
    }


    public static Context getApplicationContext() {
        return getConfiguration(ConfigType.APPLICATION_CONTEXT);
    }
}
