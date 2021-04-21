package com.zzh.rpc.common.utils;

import ch.qos.logback.core.encoder.EchoEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类
 */
public class ReflectionUtils {
    /**
     * 根据class创建对象
     *
     * @param <T> 对象类型
     * @param clazz 待创建对象的类
     * @return 创建好的对象
     */
    public static <T> T newInstance(Class clazz){
        try {
            return (T) clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    /**
     * 获取某个class的公有方法
     * @param clazz
     * @return 当前类声明的公有方法
     */
    public static Method[] getPublicMethods(Class clazz){
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> pmethods = new ArrayList<>();
        for (Method m : methods){
            if (Modifier.isPublic(m.getModifiers())){
                pmethods.add(m);
            }
        }
        return pmethods.toArray(new Method[0]);
    }

    /**
     * 调用指定对象的指定方法
     * @param obj 被调用方法的对象
     * @param method 被调用的方法
     * @param args  方法的参数
     * @return  返回结果
     */
    public static Object invoke(Object obj, Method method, Object... args){
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }
}
