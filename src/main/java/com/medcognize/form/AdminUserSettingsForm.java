package com.medcognize.form;

import com.medcognize.domain.User;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

/**
 * Created by jnash on 5/16/2014.
 */
public class AdminUserSettingsForm extends UserSettingsForm {
    public AdminUserSettingsForm(User u) {
        super(u);
    }

    @Override
    protected Component createContent() {
        validate();

        Component c = super.createContent();
        Field<?> admin = group.getField("admin");
        form.addComponent(admin);
        return c;
    }

}
