package com.medcognize.util;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class DbUtil implements Serializable {

    public static User getLoggedInUser() {
        return ((MedcognizeUI) MedcognizeUI.getCurrent()).getUser();
    }
}
