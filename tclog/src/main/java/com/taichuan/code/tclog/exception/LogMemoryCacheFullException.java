package com.taichuan.code.tclog.exception;

/**
 * @author gui
 * @date 2020/5/17
 */
public class LogMemoryCacheFullException extends WriteLogErrException{
    public LogMemoryCacheFullException(String message) {
        super(message);
    }
}
