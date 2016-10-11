package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Ming on 2016/10/11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestLimit {

    /*
     * 允许访问次数
     */
    int count()  default Integer.MAX_VALUE;

    /*
     * 时间段，单位为毫秒，默认值为 1 min
     */
    long time() default  60000;

}
