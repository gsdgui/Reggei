package com.example.reggei.common;

/**
 * 基于ThreadLocal封装的工具类，用于保存和获取当前用户登录的id
 * @author Hao zeng
 * @create 2022-06-24 23:43
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
