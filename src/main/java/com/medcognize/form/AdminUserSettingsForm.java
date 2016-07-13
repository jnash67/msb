package com.medcognize.form;

import com.medcognize.UserRepository;
import com.medcognize.domain.User;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

/**
 * Created by jnash on 5/16/2014.
 */
public class AdminUserSettingsForm extends UserSettingsForm {
    public AdminUserSettingsForm(User u, boolean isNew, UserRepository repo) {
        super(u, isNew, repo);
    }

    @Override
    protected Component createContent() {
        Component c = super.createContent();
        Field<?> admin = getFieldGroup().getField("admin");
        //form.addComponent(admin);
        return c;
    }

}
