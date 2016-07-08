package com.medcognize.form;

import com.medcognize.domain.User;
import com.medcognize.domain.validator.vaadin.PasswordRequirementsValidator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.ArrayList;

public class RegisterUserForm extends DisplayFriendlyForm<User> {

	private TextField userField;
    private PasswordField passwordField;

	static final ArrayList<String> pids = new ArrayList<String>() {
		{
			add("username");
			add("password");
		}
	};

	public RegisterUserForm(User u) {
		super(u, pids, null);
	}

    @Override
    protected Component createContent() {
        Field<?> admin = group.getField("admin");

        userField = (TextField) group.getField("username");
        passwordField = new PasswordField("password");

        setupUserField(userField);
        userField.focus();

        setupPasswordField(passwordField);

        form.addComponents(admin, userField, passwordField);
        return new MVerticalLayout(form.withWidth(""), getToolbar()).withWidth("");
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

	public TextField getUserField() {
		return userField;
	}

	public PasswordField getPasswordField() {
		return passwordField;
	}

	public void commit() throws FieldGroup.CommitException {
		boolean uv = userField.isValid();
		boolean pv = passwordField.isValid();
		if (!uv || !pv) {
			throw new FieldGroup.CommitException("Commit failed");
		}
	}


}
