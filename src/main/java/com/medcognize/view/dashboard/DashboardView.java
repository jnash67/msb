package com.medcognize.view.dashboard;

import com.google.common.eventbus.Subscribe;
import com.medcognize.MedcognizeUI;
import com.medcognize.domain.DashboardNotification;
import com.medcognize.domain.User;
import com.medcognize.event.MedcognizeEvent;
import com.medcognize.event.MedcognizeEventBus;
import com.medcognize.form.MedicalExpenseForm;
import com.medcognize.view.crud.MedicalExpenseTable;
import com.medcognize.view.dashboard.widget.HighChartWidget;
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

        Component edit = buildEditButton();
        HorizontalLayout tools = new HorizontalLayout(edit);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
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
        HighChartWidget hcw = new HighChartWidget("Chart",root, dashboardPanels);
        return hcw;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
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
}