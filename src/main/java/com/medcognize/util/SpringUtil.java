package com.medcognize.util;

import java.io.Serializable;

public class SpringUtil implements Serializable {

    public static boolean isDebugMode() {
        // in development run the VM with -Dspring.profiles.active=dev
        // (previously we used -Dmedcognize.debug.mode=true)
        String prop = System.getProperty("spring.profiles.active");
        return null != prop && prop.equals("dev");
    }

}
