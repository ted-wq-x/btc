package com.go2going;

import com.go2going.utils.PackageScan;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

/**
 * Created by BlueT on 2017/7/4.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Set<Class<?>> classes = PackageScan.getClasses("com.go2going.interfaceApi");
        System.out.println(classes);
    }
}
