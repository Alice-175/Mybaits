package com.baiye.www.exceptions;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/08/10:50
 * @Description:
 */
public class MybatisException extends RuntimeException {
    public MybatisException() {
    }

    public MybatisException(String message) {
        super(message);
    }

    public MybatisException(String message, Throwable cause) {
        super(message, cause);
    }

    public MybatisException(Throwable cause) {
        super(cause);
    }
}
