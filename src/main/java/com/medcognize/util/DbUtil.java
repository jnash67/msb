package com.medcognize.util;


import com.medcognize.MedcognizeUI;
import com.medcognize.UserService;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.EmailAddress;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class DbUtil implements Serializable {

    public static User getLoggedInUser() {
        return ((MedcognizeUI) MedcognizeUI.getCurrent()).getUser();
    }

    public static void dbChecks(UserService repo) {
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
                if (!repo.existsByUsername(ae)) {
                    repo.createNewAdminUser(ae, pass);
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