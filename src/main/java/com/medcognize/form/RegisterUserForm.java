package com.medcognize.form;

import com.medcognize.domain.User;
import com.medcognize.domain.validator.vaadin.PasswordRequirementsValidator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class RegisterUserForm extends DisplayFriendlyForm<User> {

    private Field<?> username = createField("username");
    private Field<?> password = new PasswordField("password");
    private Field<?> admin = createField("admin");

	public RegisterUserForm(User u) {
        super(User.class, null);
        setSizeUndefined();
        setEntity(u);
    }

    @Override
    protected Component createContent() {
        getFieldGroup().bind(password, "password");
        setupUserField((TextField) username);
        username.focus();
        setupPasswordField((PasswordField) password);

        ;
        return new MVerticalLayout(new MFormLayout(admin, username, password).withWidth(""), getToolbar()).withWidth("");
    }

    public static void setupUserField(final TextField userField) {
        userField.setRequired(true);
        userField.setRequiredError("Please use a valid email address");
        userField.addValidator(new EmailValidator("Username must be an email address"));
      //  userField.addValidator(new ExistingUsernameValidator(null));
        userField.setInvalidAllowed(true);
    }

    public static void setupPasswordField(final PasswordField passwordField) {
        passwordField.setRequired(true);
        passwordField.setRequiredError("Blank password not allowed");
        passwordField.addValidator(new PasswordRequirementsValidator());
        passwordField.setNullRepresentation("");
        passwordField.setNullSettingAllowed(false);
        passwordField.setInvalidAllowed(true);
    }

    public TextField getUsername() {
        return (TextField) username;
    }

    public PasswordField getPassword() {
        return (PasswordField) password;
    }

	public void commit() throws FieldGroup.CommitException {
        boolean uv = username.isValid();
        boolean pv = password.isValid();
        if (!uv || !pv) {
            throw new FieldGroup.CommitException("Commit failed");
        }
	}


}
