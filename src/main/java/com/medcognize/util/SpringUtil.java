package com.medcognize.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;

public final class SpringUtil implements Serializable {

    public static enum RoleType {
        ROLE_ADMIN, ROLE_USER
    }

    private SpringUtil() {
    }

    public static boolean isDebugMode() {
        // in development run the VM with -Dspring.profiles.active=dev
        // (previously we used -Dmedcognize.debug.mode=true)
        String prop = System.getProperty("spring.profiles.active");
        return null != prop && prop.equals("dev");
    }

    public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean hasRole(RoleType role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority(role.name()));
    }

}
