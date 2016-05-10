package com.medcognize.view.dashboard;

import com.google.common.eventbus.Subscribe;
import com.medcognize.MedcognizeUI;
import com.medcognize.domain.DashboardNotification;
import com.medcognize.domain.User;
import com.medcognize.event.MedcognizeEvent;
import com.medcognize.event.MedcognizeEventBus;
import com.medcognize.form.MedicalExpenseForm;
import com.medcognize.view.crud.MedicalExpenseTable;
import com.medcognize.view.dashboard.widget.FlotChartWidget;
import com.medcognize.view.dashboard.widget.NotesWidget;
import com.medcognize.view.dashboard.widget.TableWidget;
import com.medcognize.view.dashboard.widget.ToDoWidget;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
@SpringView(name = DashboardView.NAME)
public final class DashboardView extends Panel implements View,
        DashboardEdit.DashboardEditListener {

    public static final String NAME = "dashboard";

    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";

    private Label titleLabel;
    private NotificationsButton notificationsButton;
    private CssLayout dashboardPanels;
    private final VerticalLayout root;
    private Window notificationsWindow;
    ToDoWidget todo;

    public DashboardView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        MedcognizeEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                MedcognizeEventBus.post(new MedcognizeEvent.CloseOpenWindowsEvent());
            }
        });
    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        titleLabel = new Label("Dashboard");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        notificationsButton = buildNotificationsButton();
        Component edit = buildEditButton();
        HorizontalLayout tools = new HorizontalLayout(notificationsButton, edit);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private NotificationsButton buildNotificationsButton() {
        NotificationsButton result = new NotificationsButton();
        result.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                openNotificationsPopup(event);
            }
        });
        return result;
    }

    private Component buildEditButton() {
        Button result = new Button();
        result.setId(EDIT_ID);
        result.setIcon(FontAwesome.EDIT);
        result.addStyleName("icon-edit");
        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        result.setDescription("Edit Dashboard");
        result.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                getUI().addWindow(
                        new DashboardEdit(DashboardView.this, titleLabel
                                .getValue()));
            }
        });
        return result;
    }

    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);
        todo = new ToDoWidget("To Do", null, root, dashboardPanels);
        dashboardPanels.addComponent(todo);
        dashboardPanels.addComponent(buildNotes());
        dashboardPanels.addComponent(buildExpenseTableWidget());
        dashboardPanels.addComponent(buildChart());

        return dashboardPanels;
    }

    private Component buildNotes() {
        String msg = "Remember:\nPlans are for a maximum of one year.  For a new year, " +
                "create a new plan.\n" + "Expenses are assigned to a plan and the expense date must be within the " +
                "plan " +
                "period.";
        NotesWidget notes = new NotesWidget("Notes", msg, root, dashboardPanels);
        notes.getContent().setReadOnly(true);
        return notes;
    }

    private Component buildExpenseTableWidget() {
        ArrayList<String> pids = new ArrayList<String>() {
            {
                add("date");
                add("familyMember");
                add("costAccordingToProvider");
            }
        };
        TableWidget expenseTableWidget = new TableWidget("Recent Expenses", root, dashboardPanels);
        // DisplayFriendlyTable<MedicalExpense> table = new DisplayFriendlyTable<>(MedicalExpense.class, pids);
        MedicalExpenseTable met = new MedicalExpenseTable(MedicalExpenseForm.class, pids);
        expenseTableWidget.setTable(met);
        expenseTableWidget.addStyleName("top10-revenue");
        return expenseTableWidget;
    }

    private Component buildChart() {
        FlotChartWidget fcw = new FlotChartWidget("Chart",root, dashboardPanels);
        return fcw;
    }

    private void openNotificationsPopup(final ClickEvent event) {
        VerticalLayout notificationsLayout = new VerticalLayout();
        notificationsLayout.setMargin(true);
        notificationsLayout.setSpacing(true);

        Label title = new Label("Notifications");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        notificationsLayout.addComponent(title);

        Collection<DashboardNotification> notifications = MedcognizeUI
                .getDataProvider().getNotifications();
        MedcognizeEventBus.post(new MedcognizeEvent.NotificationsCountUpdatedEvent());

        for (DashboardNotification notification : notifications) {
            VerticalLayout notificationLayout = new VerticalLayout();
            notificationLayout.addStyleName("notification-item");

            Label titleLabel = new Label(notification.getFirstName() + " "
                    + notification.getLastName() + " "
                    + notification.getAction());
            titleLabel.addStyleName("notification-title");

            Label timeLabel = new Label(notification.getPrettyTime());
            timeLabel.addStyleName("notification-time");

            Label contentLabel = new Label(notification.getContent());
            contentLabel.addStyleName("notification-content");

            notificationLayout.addComponents(titleLabel, timeLabel,
                    contentLabel);
            notificationsLayout.addComponent(notificationLayout);
        }

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth("100%");
        Button showAll = new Button("View All Notifications",
                new ClickListener() {
                    @Override
                    public void buttonClick(final ClickEvent event) {
                        Notification.show("Not implemented in this demo");
                    }
                });
        showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showAll.addStyleName(ValoTheme.BUTTON_SMALL);
        footer.addComponent(showAll);
        footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
        notificationsLayout.addComponent(footer);

        if (notificationsWindow == null) {
            notificationsWindow = new Window();
            notificationsWindow.setWidth(300.0f, Unit.PIXELS);
            notificationsWindow.addStyleName("notifications");
            notificationsWindow.setClosable(false);
            notificationsWindow.setResizable(false);
            notificationsWindow.setDraggable(false);
            notificationsWindow.setCloseShortcut(KeyCode.ESCAPE, null);
            notificationsWindow.setContent(notificationsLayout);
        }

        if (!notificationsWindow.isAttached()) {
            notificationsWindow.setPositionY(event.getClientY()
                    - event.getRelativeY() + 40);
            getUI().addWindow(notificationsWindow);
            notificationsWindow.focus();
        } else {
            notificationsWindow.close();
        }
    }

    @Override
    public void enter(final ViewChangeEvent event) {
        notificationsButton.updateNotificationsCount(null);
        setToDos();
    }

    protected void setToDos() {
        User u = ((MedcognizeUI) MedcognizeUI.getCurrent()).getUser();
        int numPlans = u.getPlans().size();
        int numFamilyMembers = u.getFamilyMembers().size();
        int numProviders = u.getProviders().size();
        if (numPlans == 0 && numFamilyMembers == 0 && numProviders == 0) {
            todo.addToDo("You should have at least one plan, one family member, and one health care provider");
        } else if (numPlans == 0 && numFamilyMembers == 0) {
            todo.addToDo("You should have at least one plan and one family member");
        } else if (numPlans == 0 && numProviders == 0) {
            todo.addToDo("You should have at least one plan and one provider");
        } else if (numFamilyMembers == 0 && numProviders == 0) {
            todo.addToDo("You should have at least one family member and one provider");
        } else if (numFamilyMembers == 0) {
            todo.addToDo("You should have at least one family member");
        } else if (numProviders == 0) {
            todo.addToDo("You should have at least one provider");
        } else if (numPlans == 0) {
            todo.addToDo("You should have at least one plan");
        }

        if ((null == u.getFirstName()) || ("".equals(u.getFirstName()))) {
            todo.addToDo("Set your real name.  Go to the little cog icon at bottom left.");
        }

        todo.addToDo("Submit your expenses to your plan and your FSA");
    }

    @Override
    public void dashboardNameEdited(final String name) {
        titleLabel.setValue(name);
    }

    public static final class NotificationsButton extends Button {
        private static final String STYLE_UNREAD = "unread";
        public static final String ID = "dashboard-notifications";

        public NotificationsButton() {
            setIcon(FontAwesome.BELL);
            setId(ID);
            addStyleName("notifications");
            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            MedcognizeEventBus.register(this);
        }

        @Subscribe
        public void updateNotificationsCount(
                final MedcognizeEvent.NotificationsCountUpdatedEvent event) {
            setUnreadCount(MedcognizeUI.getDataProvider()
                    .getUnreadNotificationsCount());
        }

        public void setUnreadCount(final int count) {
            setCaption(String.valueOf(count));

            String description = "Notifications";
            if (count > 0) {
                addStyleName(STYLE_UNREAD);
                description += " (" + count + " unread)";
            } else {
                removeStyleName(STYLE_UNREAD);
            }
            setDescription(description);
        }
    }

}