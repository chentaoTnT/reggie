package com.itheima.reggie.common;


/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * 因为一次请求的线程是同一个，因此可以选择使用ThreadLocal类来保存共享变量
 */
public class MyBaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
