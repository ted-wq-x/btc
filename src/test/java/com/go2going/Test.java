package com.go2going;

import com.go2going.utils.PackageScan;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * Created by BlueT on 2017/7/4.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE,-1);
        calendar.set(Calendar.SECOND,0);
        Date before = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(before));
    }
}
