package com.medcognize.view.homepage;

import com.medcognize.UserRepository;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.EmailAddress;
import com.medcognize.domain.validator.vaadin.ExistingUsernameValidator;
import com.medcognize.domain.validator.vaadin.PasswordRequirementsValidator;
import com.medcognize.event.MedcognizeEvent;
import com.medcognize.event.MedcognizeEventBus;
import com.medcognize.form.field.errorful.ErrorfulHorizontalLayout;
import com.medcognize.util.SpringUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
@Slf4j
@SpringView(name = RegisterView.NAME)
public class RegisterView extends VerticalLayout implements View {

    public static final String NAME = "register";
    @Autowired
    UserRepository repo;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setSizeFull();
        final VerticalLayout registerForm = new VerticalLayout();
        registerForm.setSizeUndefined();
        registerForm.setSpacing(true);
        // Responsive.makeResponsive(registerForm);
        registerForm.addStyleName("login-panel");
        registerForm.addComponent(buildLabels());
        registerForm.addComponent(buildFields());
        addComponent(registerForm);
        setComponentAlignment(registerForm, Alignment.MIDDLE_CENTER);
        Notification notification = new Notification("Welcome to Medcognize");
        notification.setDescription("<span>Please enter a valid email address and a password to create an account.  The password must be at least 8 characters long and contain at least one number.</span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("whitebackground tray dark small login-help closable");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(10000);
        notification.show(Page.getCurrent());
    }

    private Component buildFields() {
        ErrorfulHorizontalLayout fields = new ErrorfulHorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");
        final TextField usernameField = new TextField("Username");
        usernameField.setIcon(FontAwesome.USER);
        usernameField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        usernameField.setRequired(true);
        // username.setRequiredError("Please use a valid email address");
        usernameField.addValidator(new EmailValidator("Username must be an email address"));
        usernameField.addValidator(new ExistingUsernameValidator(repo, null));
        usernameField.setInvalidAllowed(true);
        // couldn't get Vaadin CapsLockWarning to work with Spring
        final PasswordField passwordField = new PasswordField("Password");
        passwordField.setIcon(FontAwesome.LOCK);
        passwordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        passwordField.setRequired(true);
        // password.setRequiredError("Please enter the password");
        passwordField.setValue("");
        passwordField.setNullRepresentation("");
        passwordField.addValidator(new PasswordRequirementsValidator());
        passwordField.setInvalidAllowed(true);
        if (SpringUtil.isDebugMode()) {
            String un;
            //username.setValue("test@test.com");
            int y = 67;
            do {
                un = "jnash" + y + "@yahoo.com";
                y = y + 1;
            } while (repo.existsByUsername(un));
            usernameField.setValue(un);
            passwordField.setValue("Passwor1");
        }
        final VerticalLayout buttonLayout = new VerticalLayout();
        final VerticalLayout smallTextLayout = new VerticalLayout();
        final HorizontalLayout line3 = new HorizontalLayout();
        Label terms1 = new Label("<font size=\"1\">By clicking \'Register\'</font>", com.vaadin.shared.ui.label.ContentMode.HTML);
        Label terms2 = new Label("<font size=\"1\">you agree to our</font>", com.vaadin.shared.ui.label.ContentMode.HTML);
        Button service = new Button("<font size=\"1\">Terms of Service</font>");
        service.setHtmlContentAllowed(true);
        //service.addStyleName("icon-link");
        service.addStyleName(Reindeer.BUTTON_LINK);
        service.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                HomepageView.showTermsOfService();
            }
        });
        line3.addComponent(service);
        final Button register = new Button("Register");
        register.addStyleName(ValoTheme.BUTTON_PRIMARY);
        buttonLayout.addComponent(register);
        smallTextLayout.addComponent(terms1);
        smallTextLayout.addComponent(terms2);
        smallTextLayout.addComponent(line3);
        smallTextLayout.setSpacing(false);
        smallTextLayout.setMargin(false);
        buttonLayout.addComponent(smallTextLayout);
        buttonLayout.setComponentAlignment(register, Alignment.MIDDLE_CENTER);
        smallTextLayout.setComponentAlignment(terms1, Alignment.MIDDLE_CENTER);
        smallTextLayout.setComponentAlignment(terms2, Alignment.MIDDLE_CENTER);
        smallTextLayout.setComponentAlignment(line3, Alignment.MIDDLE_CENTER);
        smallTextLayout.setHeight("35px");
        buttonLayout.setExpandRatio(register, 1.0F);
        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight("30px");
        buttonLayout.addComponent(spacer);
        fields.addComponents(usernameField, passwordField, buttonLayout);
        fields.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);
        final ShortcutListener enter = new ShortcutListener("Register", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                register.click();
            }
        };
        register.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    usernameField.validate();
                    passwordField.validate();
                } catch (Validator.InvalidValueException ive) {
                    return;
                }
                String username = usernameField.getValue();
                String password = passwordField.getValue();
                User u;
                try {
                    u = UserUtil.createNewRegularUser(repo, new EmailAddress(username), password);
                    // send registration emails
//                    try {
//                        EmailUtil.sendWelcomeEmail(username);
//                        EmailUtil.sendNewUserNotification(username);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    MedcognizeEventBus.post(new MedcognizeEvent.UserLoginEvent(u));
                    passwordField.setValue(null);
                    register.removeShortcutListener(enter);
                } catch (Exception e) {
                    log.error(e.toString());
                    e.printStackTrace();
                }
            }
        });
        register.addShortcutListener(enter);
        return fields;
    }

    private Component buildLabels() {
        HorizontalLayout labels = new HorizontalLayout();
        labels.setSpacing(true);
        labels.setWidth("100%");
        labels.addStyleName("labels");
        Label welcome = new Label("Register");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);
        labels.setExpandRatio(welcome, 1.0F);
        HorizontalLayout titleWithButton = LoginView.getTitleWithButton();
        labels.addComponent(titleWithButton);
        labels.setComponentAlignment(titleWithButton, Alignment.MIDDLE_RIGHT);
        labels.setExpandRatio(titleWithButton, 0);
        return labels;
    }
}
