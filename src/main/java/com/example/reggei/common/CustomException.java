package com.example.reggei.common;

/**
 * 自定义业务异常
 * @author Hao zeng
 * @create 2022-06-25 11:52
 */

public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
