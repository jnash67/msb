package com.medcognize.util;


import com.medcognize.MedcognizeUI;
import com.medcognize.UserRepository;
import com.medcognize.domain.basic.EmailAddress;
import com.medcognize.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class DbUtil implements Serializable {

    public static User getLoggedInUser() {
        return ((MedcognizeUI) MedcognizeUI.getCurrent()).getUser();
    }

    public static void setLoggedInUser(final User u) {
        ((MedcognizeUI) MedcognizeUI.getCurrent()).setUser(u);
    }

    public static boolean existsUser(UserRepository repo, EmailAddress emailAddress) {
        return existsUser(repo, emailAddress.toString());
    }

    public static boolean existsUser(UserRepository repo, String username) {
        if (null == repo) {
            log.error("Null repo in DbUtil.existsUser");
            return false;
        }
        User u = repo.findByUsername(username);
        return null != u;
    }

    public static boolean existsAdminUser(UserRepository repo) {
        return null != repo.findByAdmin(true);
    }

    public static void dbChecks(UserRepository repo) {
        boolean adminExists = repo.existsByAdminTrue();
        System.out.println("existsByAdminTrue --> " + adminExists);
        long numAdmins = repo.countByAdminTrue();
        System.out.println("countByAdminTrue --> " + numAdmins);
        boolean existsByUserName = repo.existsByUsername("admin@admin.com");
        System.out.println("existsByUserName --> " + existsByUserName);
        if (!adminExists) {
            try {
                EmailAddress ae = new EmailAddress("admin@admin.com");
                String pass = "Passwor4";
                if (!DbUtil.existsUser(repo, ae)) {
                    User a = new User(ae, pass);
                    a.setAdmin(true);
                    repo.saveAndFlush(a);
                    adminExists = repo.existsByAdminTrue();
                    System.out.println("existsByAdminTrue --> " + adminExists);
                    numAdmins = repo.countByAdminTrue();
                    System.out.println("countByAdminTrue --> " + numAdmins);
                } else {
                    log.error("Default admin email/username exists as non-admin user!");
                }
            } catch (Exception e) {
                log.error("Error adding default admin user!", e);
                e.printStackTrace();
            }
        }
    }

}