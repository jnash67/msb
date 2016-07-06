package com.medcognize.form;

import com.medcognize.domain.Plan;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.field.ViritinFieldGroupFieldFactory;
import com.medcognize.util.UserUtil;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class UserSettingsForm extends DisplayFriendlyForm<User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserFieldFactory.class);

    private PasswordField passwordField;
    private Field<?> firstName;
    private Field<?> lastName;
    private Field<?> email;

    static class UserFieldFactory extends ViritinFieldGroupFieldFactory {
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
                    NativeSelect field = new NativeSelect("", UserUtil.getAll(u, Plan.class));
                    Plan ap = UserUtil.getActivePlan(u);
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
    public UserSettingsForm(User u) {
        super(u, null, new UserFieldFactory(u));
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    @Override
    protected void validate() {
        // these are variables in User that are never Fields.  Admin is not a Field in this form
        // but it is a Field in another, and it is listed in the entity caption string.
        // So we don't need to specify to ignore Admin.
        pidsToIgnore.add("encryptedData");
        pidsToIgnore.add("encryptedDataFile");
        pidsToIgnore.add("id");
        pidsToIgnore.add("activePlan");
        super.validate();
    }

    @Override
    protected Component createContent() {
        validate();

        firstName = group.getField("firstName");
        lastName = group.getField("lastName");
        email = group.getField("username");
        passwordField = (PasswordField) group.getField("password");
        form.addComponent(email);
        if ((null == email.getValue() || "".equals(email.getValue()))) {
            // this is a new user
            RegisterUserForm.setupUserField((TextField) email);
            RegisterUserForm.setupPasswordField(passwordField);
            form.addComponent(passwordField);
        } else {
            // existing user so cannot change email or password (which isn't shown)
            email.setEnabled(false);
            ((TextField) email).setDescription("You cannot change your email address here");
        }
        form.addComponent(firstName);
        form.addComponent(lastName);

        return new MVerticalLayout(form.withWidth(""), getToolbar()).withWidth("");
    }
}