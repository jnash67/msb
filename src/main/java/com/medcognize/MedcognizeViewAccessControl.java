package com.medcognize;

import com.medcognize.util.SpringUtil;
import com.medcognize.view.admin.AdminView;
import com.medcognize.view.homepage.HomepageView;
import com.medcognize.view.homepage.LoginView;
import com.medcognize.view.homepage.RegisterView;
import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;
import org.springframework.stereotype.Component;

@Component
public class MedcognizeViewAccessControl implements ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
        String lcBeanName = beanName.toLowerCase();
        if ((lcBeanName.equals(HomepageView.class.getSimpleName().toLowerCase())) ||
                (lcBeanName.equals(LoginView.class.getSimpleName().toLowerCase())) ||
                (lcBeanName.equals(RegisterView.class.getSimpleName().toLowerCase()))) {
            return true;
        }
        if (lcBeanName.equals(AdminView.class.getSimpleName().toLowerCase())) {
            return SpringUtil.hasRole(SpringUtil.RoleType.ROLE_ADMIN);
        } else {
            return SpringUtil.hasRole(SpringUtil.RoleType.ROLE_USER);
        }
    }
}