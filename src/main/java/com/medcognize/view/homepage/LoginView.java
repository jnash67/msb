package com.medcognize.view.homepage;

import com.medcognize.MedcognizeUI;
import com.medcognize.UserRepository;
import com.medcognize.domain.User;
import com.medcognize.event.MedcognizeEvent;
import com.medcognize.event.MedcognizeEventBus;
import com.medcognize.util.SpringUtil;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.Position;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@SuppressWarnings("serial")
@SpringView(name = LoginView.NAME)
@Slf4j
public class LoginView extends VerticalLayout implements View {
    public static final String NAME = "login";

    private AuthenticationManager authenticationManager;
    private UserRepository repo;

    @Autowired
    public LoginView(AuthenticationManager am, UserRepository repo) {
        this.authenticationManager = am;
        this.repo = repo;
    }

    public static HorizontalLayout getTitleWithButton() {
        Label title = new Label("Medcognize");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        Button cancelButton = createCancelButton();
        HorizontalLayout titleWithButton = new HorizontalLayout();
        titleWithButton.setSpacing(false);
        titleWithButton.setSizeUndefined();
        titleWithButton.addComponent(title);
        titleWithButton.addComponent(cancelButton);
        return titleWithButton;
    }

    public static Button createCancelButton() {
        final MedcognizeUI ref = (MedcognizeUI) MedcognizeUI.getCurrent();
        Button configure = new Button();
        configure.setIcon(FontAwesome.CLOSE);
        configure.addStyleName("configure");
        configure.addStyleName("icon-cancel");
        configure.addStyleName("icon-only");
        configure.addStyleName("borderless");
        configure.setDescription("Configure");
        configure.addStyleName("small");
        configure.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ref.getNavigator().navigateTo(HomepageView.NAME);
            }
        });
        return configure;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setSizeFull();

        final VerticalLayout loginForm = new VerticalLayout();
        loginForm.setSizeUndefined();
        loginForm.setSpacing(true);
        Responsive.makeResponsive(loginForm);
        loginForm.addStyleName("login-panel");

        loginForm.addComponent(buildLabels());
        loginForm.addComponent(buildFields());

        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        Notification notification = new Notification("Welcome to Medcognize");
        notification
                .setDescription("<span>Please enter your username and password.</span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("whitebackground tray dark small login-help closable");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(10000);
        notification.show(Page.getCurrent());
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final TextField usernameField = new TextField("Username");
        usernameField.setIcon(FontAwesome.USER);
        usernameField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        usernameField.setRequired(true);
        usernameField.setRequiredError("Please use a valid email address");
        usernameField.addValidator(new EmailValidator("Username must be an email address"));
        usernameField.setInvalidAllowed(true);

        // couldn't get Vaadin CapsLockWarning to work with Spring
        final PasswordField passwordField = new PasswordField("Password");
        passwordField.setIcon(FontAwesome.LOCK);
        passwordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        passwordField.setRequired(true);
        passwordField.setRequiredError("Please enter the password");
        passwordField.setValue("");
        passwordField.setNullRepresentation("");
        passwordField.setInvalidAllowed(true);

        if (SpringUtil.isDebugMode()) {
            //username.setValue("test@test.com");
            usernameField.setValue("admin@admin.com");
            passwordField.setValue("Passwor4");
        }

        final Button signin = new Button("Sign In");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(usernameField, passwordField, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                String username = usernameField.getValue();
                String password = passwordField.getValue();
                try {
                    Authentication token = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(username, password));
                    // Reinitialize the session to protect against session fixation attacks. This does not work
                    // with websocket communication.
                    VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
                    SecurityContextHolder.getContext().setAuthentication(token);
                    // Now when the session is reinitialized, we can enable websocket communication. Or we could have just
                    // used WEBSOCKET_XHR and skipped this step completely.
                    getUI().getPushConfiguration().setTransport(Transport.WEBSOCKET);
                    getUI().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
                    // Show the main UI
                    User u = repo.findByUsername(username);
                    MedcognizeEventBus.post(new MedcognizeEvent.UserLoginEvent(u));
                } catch (AuthenticationException ex) {
                    log.warn("AuthenticationException ---> " + ex);
                }
            }
        });
        return fields;
    }

    private Component buildLabels() {
        HorizontalLayout labels = new HorizontalLayout();
        labels.setSpacing(true);
        labels.setWidth("100%");
        labels.addStyleName("labels");

        Label welcome = new Label("Login");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);
        labels.setExpandRatio(welcome, 1.0f);

        HorizontalLayout titleWithButton = getTitleWithButton();
        labels.addComponent(titleWithButton);
        labels.setComponentAlignment(titleWithButton, Alignment.MIDDLE_RIGHT);
        labels.setExpandRatio(titleWithButton, 0);

        return labels;
    }

}