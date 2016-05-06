package com.medcognize.form;

import com.medcognize.domain.User;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.Plan;
import com.medcognize.form.field.MedcognizeFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

;

public class UserSettingsForm extends DisplayFriendlyForm<User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserFieldFactory.class);

    private PasswordField passwordField;
    private Field<?> firstName;
    private Field<?> lastName;
    private Field<?> email;

    static class UserFieldFactory extends MedcognizeFieldGroupFieldFactory {
        final User u;

        public UserFieldFactory(final User u) {
            super();
            this.u = u;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends Field> T createField(Class<?> dataType, Class<T> fieldType) {
            if (DisplayFriendly.class.isAssignableFrom(dataType)) {
                if (Plan.class.isAssignableFrom(dataType)) {
                    // v. important we use the plans of the user we're editing
                    NativeSelect field = new NativeSelect("", u.getAll(Plan.class));
                    Plan ap = u.getActivePlan();
                    if (null != ap) {
                        field.setValue(ap);
                    }
                    field.setImmediate(true);
                    return (T) field;
                } else {
                    LOGGER.error("We should not be here. Plan should be the only DisplayFriendly for this form.");
                    return null;
                }
            }
            T field = super.createField(dataType, fieldType);
            return field;
        }
    }

    // we ignore the passed in FieldFactory
    public UserSettingsForm(BeanItem<User> bean, boolean isNew) {
        super(bean, null, new UserFieldFactory(bean.getBean()), isNew);
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    @Override
    public void setupForm() {
        // these are variables in User that are never Fields.  Admin is not a Field in this form
        // but it is a Field in another, and it is listed in the entity caption string.
        // So we don't need to specify to ignore Admin.
        pidsToIgnore.add("encryptedData");
        pidsToIgnore.add("encryptedDataFile");
        pidsToIgnore.add("id");
        pidsToIgnore.add("activePlan");

        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        BeanFieldGroup<User> group = this.getFieldGroup();

        firstName = group.getField("firstName");
        lastName = group.getField("lastName");
        email = group.getField("username");
        passwordField = (PasswordField) group.getField("password");
        addComponent(email);
        if ((null == email.getValue() || "".equals(email.getValue()))) {
            // this is a new user
            RegisterUserForm.setupUserField((TextField) email);
            RegisterUserForm.setupPasswordField(passwordField);
            addComponent(passwordField);
        } else {
            // existing user so cannot change email or password (which isn't shown)
            email.setEnabled(false);
            ((TextField) email).setDescription("You cannot change your email address here");
        }
        addComponent(firstName);
        addComponent(lastName);
    }
}