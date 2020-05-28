package com.taichuan.code.tclog.exception;

/**
 * @author gui
 * @date 2020/5/17
 */
public class LogCacheFullException extends WriteLogErrException{
    public LogCacheFullException(String message) {
        super(message);
    }
}
