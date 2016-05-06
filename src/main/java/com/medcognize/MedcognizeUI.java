package com.medcognize;

import com.google.common.eventbus.Subscribe;
import com.medcognize.data.DataProvider;
import com.medcognize.data.dummy.DummyDataProvider;
import com.medcognize.domain.User;
import com.medcognize.event.MedcognizeEvent.BrowserResizeEvent;
import com.medcognize.event.MedcognizeEvent.CloseOpenWindowsEvent;
import com.medcognize.event.MedcognizeEvent.UserLoggedOutEvent;
import com.medcognize.event.MedcognizeEvent.UserLoginEvent;
import com.medcognize.event.MedcognizeEventBus;
import com.medcognize.util.DbUtil;
import com.medcognize.view.DashboardMenu;
import com.medcognize.view.dashboard.DashboardView;
import com.medcognize.view.homepage.HomepageView;
import com.medcognize.view.homepage.LoginView;
import com.medcognize.view.homepage.RegisterView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

@SpringUI
@Theme("dashboard")
// Don't need @Widgetset annotation if using vwscdn-maven-plugin
@Widgetset("com.medcognize.MedcognizeWidgetSet")
@Title("Medcognize")
@PreserveOnRefresh
@Slf4j
public class MedcognizeUI extends UI {

    public static final String US_DATE_FORMAT = "MM-dd-yyyy";
    public static final Locale LOCALE = Locale.US;

    private final DataProvider dataProvider = new DummyDataProvider();
    private final UserRepository repo;
    private final MedcognizeEventBus medcognizeEventbus;
    private final MedcognizeNavigator nav;
    private User user = null;

    /*
    The navigation component is either the whole page in the case of the home page, registration and login or
    it's a dashboard view where it's the component to the right of the menu.
     */
    private CssLayout navigationComponent = new CssLayout();
    private AbstractOrderedLayout dashboardMenuPlusComponentToRight;
    private DashboardMenu dashboardMenu;

    public static boolean isDebugMode() {
        // in development, run the VM with -Dmedcognize.debug.mode=true
        // in development run the VM with -Dspring.profiles.active=dev
        String prop = System.getProperty("spring.profiles.active");
        return null != prop && prop.equals("dev");
    }

    @Autowired
    SpringViewProvider viewProvider;

    // @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public MedcognizeUI(UserRepository repo, MedcognizeEventBus bus, MedcognizeNavigator nav) {
        this.repo = repo;
        this.medcognizeEventbus = bus;
        this.nav = nav;
    }

    @Override
    protected void init(final VaadinRequest request) {
        if (isDebugMode()) {
            log.warn("-------------RUNNING IN DEBUG MODE--------------");
        }
        DbUtil.dbChecks(repo);
        setLocale(LOCALE);
        MedcognizeEventBus.register(this);
        Responsive.makeResponsive(MedcognizeUI.this);

        // the ViewDisplay of the navigator decides whether the view takes up the whole page or
        // just the part to the right of the menu.
        // rootLayout.addStyleName("root");
        nav.initAfterSpringCreation(this, new ViewDisplay() {

            @Override
            public void showView(View view) {
                removeStyleName("loginview");
                removeStyleName("view-content");
                removeStyleName("whitebackground");
                removeStyleName("homepagemenuformat");
                removeStyleName("mainview");
                removeStyleName(ValoTheme.UI_WITH_MENU);

                if (HomepageView.class.isAssignableFrom(view.getClass())) {
                    setStyleName("whitebackground");
                    navigationComponent.removeAllComponents();
                    navigationComponent.setSizeFull();
                    navigationComponent.addComponent((Component) view);
                    setContent(navigationComponent);
                    return;
                }

                addStyleName(ValoTheme.UI_WITH_MENU);
                if (LoginView.class.isAssignableFrom(view.getClass()) ||
                        (RegisterView.class.isAssignableFrom(view.getClass()))) {
                    setStyleName("whitebackground");
                    navigationComponent.removeAllComponents();
                    // navigationComponent = new CssLayout();
                    navigationComponent.setSizeFull();
                    navigationComponent.addComponent((Component) view);
                    setContent(navigationComponent);
                    // setContent((LoginView) view);
                    addStyleName("loginview");
                    return;
                }

                if (isLoggedIn()) {
                    // Authenticated user
                    Responsive.makeResponsive(MedcognizeUI.this);
                    addStyleName(ValoTheme.UI_WITH_MENU);

                    if (null == dashboardMenu) {
                        dashboardMenu = new DashboardMenu();
                    }
                    if (null == dashboardMenuPlusComponentToRight) {
                        dashboardMenuPlusComponentToRight = new HorizontalLayout();
                        dashboardMenuPlusComponentToRight.setSizeFull();
                        dashboardMenuPlusComponentToRight.addStyleName("mainview");
                    }
                    dashboardMenuPlusComponentToRight.removeAllComponents();
                    dashboardMenuPlusComponentToRight.addComponent(dashboardMenu);

                    navigationComponent.removeAllComponents();
                    navigationComponent.setSizeFull();
                    navigationComponent.addComponent((Component) view);
                    navigationComponent.addStyleName("view-content");
                    dashboardMenuPlusComponentToRight.addComponent(navigationComponent);
                    dashboardMenuPlusComponentToRight.setExpandRatio(navigationComponent, 1.0f);

                    setContent(dashboardMenuPlusComponentToRight);
                    System.out.println("navigator state is --> " + getNavigator().getState());
                    // getNavigator().navigateTo(getNavigator().getState());
                }
                return;
            }
        });

        nav.addProvider(viewProvider);
        nav.navigateTo(HomepageView.NAME);

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(
                new BrowserWindowResizeListener() {
                    @Override
                    public void browserWindowResized(
                            final BrowserWindowResizeEvent event) {
                        MedcognizeEventBus.post(new BrowserResizeEvent());
                    }
                });
    }

    /**
     * Updates the correct content for this UI based on the current user status.
     * If the user is logged in with appropriate privileges, main view is shown.
     * Otherwise login view is shown.
     */
    private void updateContent() {
//        User user = (User) VaadinSession.getCurrent().getAttribute(
//                User.class.getName());
//        if (user != null && "admin".equals(user.getRole())) {
//            // Authenticated user
//            Responsive.makeResponsive(this);
//            addStyleName(ValoTheme.UI_WITH_MENU);
//
//            setContent(new MainView());
//            setTheme("dashboard");
//            removeStyleName("loginview");
//            System.out.println("navigator state is --> " + getNavigator().getState());
//            getNavigator().navigateTo(getNavigator().getState());
//        } else {
//            setTheme("homepage");
//            //nav.addProvider(viewProvider);
//            // nav.addView(HomepageView.NAME, HomepageView.class);
//
//            nav.addView(HomepageView.NAME, new HomepageView());
//            nav.setErrorView(HomepageView.class);
//            System.out.println("navigator state is --> " + getNavigator().getState());
//            nav.navigateTo(HomepageView.NAME);
//            System.out.println("navigator state is --> " + getNavigator().getState());
//			setContent(new HomepageView());
//			setContent(new LoginView());
//			addStyleName("loginview");
//        }
    }

    @Subscribe
    public void userLogin(final UserLoginEvent event) {
        User user = event.getUser();
        setUser(user);
        getNavigator().navigateTo(DashboardView.NAME);
    }

    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        this.user = null;
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
        nav.navigateTo(HomepageView.NAME);
    }

    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    /**
     * @return An instance for accessing the (dummy) services layer.
     */
    public static DataProvider getDataProvider() {
        return ((MedcognizeUI) getCurrent()).dataProvider;
    }

    public static MedcognizeEventBus getMedcognizeEventbus() {
        return ((MedcognizeUI) getCurrent()).medcognizeEventbus;
    }

    public boolean isLoggedIn() {
        return null != this.user;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(final User u) {
        this.user = u;
    }

}
