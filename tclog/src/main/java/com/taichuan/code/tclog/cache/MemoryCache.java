package com.taichuan.code.tclog.cache;

import com.taichuan.code.tclog.bean.Log;

import java.util.List;

/**
 * @author gui
 * @date 2020/6/4
 */
public interface MemoryCache {
    /**
     * 获取缓存在内存的log
     *
     * @return
     */
    List<Log> getCacheLogList();

    /**
     * 内存里是否还有缓存的log
     *
     * @return
     */
    boolean isHaveCache();

}
