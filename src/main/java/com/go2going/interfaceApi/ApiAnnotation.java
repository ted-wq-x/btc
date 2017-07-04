package com.go2going.interfaceApi;

import java.lang.annotation.*;

/**
 * channel和类的映射
 * Created by BlueT on 2017/7/4.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited()
public @interface ApiAnnotation {
    /**
     * @return api
     */
    String api();
}
