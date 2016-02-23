package com.medcognize.form;

import com.medcognize.domain.User;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;

/**
 * Created by jnash on 5/16/2014.
 */
public class AdminUserSettingsForm extends UserSettingsForm {
    public AdminUserSettingsForm(BeanItem<User> bean, boolean isNew) {
        super(bean, isNew);
    }

    @Override
    public void setupForm() {
        super.setupForm();

        BeanFieldGroup<User> group = this.getFieldGroup();
        Field<?> admin = group.getField("admin");
        addComponent(admin);
    }

}
