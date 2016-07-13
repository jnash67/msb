package com.medcognize.form;

import com.medcognize.UserRepository;
import com.medcognize.domain.Plan;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.field.ViritinFieldGroupFieldFactory;
import com.medcognize.util.UserUtil;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class UserSettingsForm extends DisplayFriendlyForm<User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserFieldFactory.class);

    private PasswordField password = new PasswordField();
    private Field<?> firstName = createField("firstName");
    private Field<?> lastName = createField("lastName");
    private Field<?> email = createField("email");

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
    public UserSettingsForm(User u, boolean isNew, UserRepository repo) {
        super(User.class, isNew, new UserFieldFactory(u), repo);
        setEntity(u);
    }

    public PasswordField getPassword() {
        return password;
    }

    @Override
    protected Component createContent() {
        MFormLayout form = new MFormLayout();
        getFieldGroup().bind(password, "password");
        form.addComponent(email);
        if ((null == email.getValue() || "".equals(email.getValue()))) {
            // this is a new user
            RegisterUserForm.setupUserField((TextField) email);
            RegisterUserForm.setupPasswordField(password);
            form.addComponent(password);
        } else {
            // existing user so cannot change email or password (which isn't shown)
            email.setEnabled(false);
            ((TextField) email).setDescription("You cannot change your email address here");
        }
        form.addComponents(firstName, lastName);
        return new MVerticalLayout(form.withWidth(""), getToolbar()).withWidth("");
    }
}