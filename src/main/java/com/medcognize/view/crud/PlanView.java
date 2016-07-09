package com.medcognize.view.crud;

import com.medcognize.MedcognizeUI;
import com.medcognize.UserRepository;
import com.medcognize.domain.Plan;
import com.medcognize.domain.PlanLimitsStatus;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.validator.vaadin.NotEqualIntegerValidator;
import com.medcognize.form.DisplayFriendlyForm;
import com.medcognize.form.PlanForm;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.medcognize.view.ComponentWindow;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.event.Action;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.daterangefield.DateUtil;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.risto.stepper.IntStepper;
import org.vaadin.viritin.layouts.MFormLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Slf4j
@SpringView(name = PlanView.NAME)
public class PlanView extends CrudView<Plan> {

    public static final String NAME = "plan";
    static final ArrayList<String> pids = new ArrayList<String>() {
        {
            add("planName");
            add("planType");
            add("planStartDate");
            add("planEndDate");
        }
    };

    private final UserRepository repo;

    static class PlanCrudTable extends CrudTable<Plan> {
        private UserRepository repo;

        public PlanCrudTable(UserRepository repo, Class<Plan> entityClazz, Class<? extends DisplayFriendlyForm<Plan>> formClazz, ArrayList<String> orderedPidList) {
            super(repo, entityClazz, formClazz, orderedPidList);
            this.repo = repo;
        }

        protected final Action ACTION_LIMITS = new Action("See Limits Usage");

        @Override
        protected void deleteActionWithConfirm(final Object target) {
            Plan p = getEntityFromContainer(target);
            int count = UserUtil.getAll(collectionOwner, Plan.class).size();
            if (1 == count) {
                if (!p.isActivePlan()) {
                    log.warn("This should not happen. The last plan left should always be the active plan");
                }
                Notification.show("You cannot delete the last plan.  You always need to have at least one plan.");
                return;
            }
            if (p.isActivePlan()) {
                Notification.show("You cannot delete the currently active plan");
                return;
            }
            int num = 0;
            try {
                User u = (User) collectionOwner;
                num = (UserUtil.getMedicalExpensesForPlan(u, p)).size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (0 == num) {
                Window w = ConfirmDialog.show(this.getUI(), "Please Confirm:", "Area you really sure?", "YES", "No", new ConfirmDialog.Listener() {
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            deleteAction(target);
                        }
                    }
                });
                w.setClosable(false);
            } else {
                Window w = ConfirmDialog.show(this.getUI(), "Please Confirm:", "Area you really sure? All " + num + " expenses tied to this plan will also be deleted", "YES", "No", new ConfirmDialog.Listener() {
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            deleteAction(target);
                        }
                    }
                });
                w.setClosable(false);
            }
        }

        @Override
        public Action[] getActions(Object target, Object sender) {
            if (contextMenuEnabled) {
                return new Action[]{ACTION_ADD, ACTION_EDIT, ACTION_DELETE, ACTION_LIMITS};
            }
            return new Action[0];
        }

        @Override
        public void handleAction(Action action, Object sender, Object target) {
            if (contextMenuEnabled) {
                if (ACTION_LIMITS == action) {
                    if (null == target) {
                        return;
                    }
                    Plan p = getEntityFromContainer(target);
                    showLimitsReport(p);
                } else {
                    super.handleAction(action, sender, target);
                }
            }
        }

        public void showLimitsReport(Plan p) {
            PlanLimitsStatus pls = new PlanLimitsStatus((User) this.collectionOwner, p);
            ComponentWindow window = new ComponentWindow("Plan Limits Usage", false, false);
            window.showSubmitButton(false);
            window.setSizeUndefined();
            VerticalLayout tables = pls.getLimitsReportAllFamilyMembers();
            window.setContent(tables);
            UI.getCurrent().addWindow(window);
            window.focus();
        }
    }

    final User u;

    @Autowired
    public PlanView(UserRepository repo) {
        super(Plan.class, "Plans", new PlanCrudTable(repo, Plan.class, PlanForm.class, pids));
        this.repo = repo;
        u = DbUtil.getLoggedInUser();
        if (null == u) {
            log.error("owner should not be null here");
            return;
        }
        Collection<Plan> plans = UserUtil.getAll(u, Plan.class);
        // we want this added before the other column that gets generated when we call setData
        table.addGeneratedColumn("Active Plan", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                int index = getContainer().indexOfId(itemId);
                Plan p = getContainer().getIdByIndex(index);
                CheckBox cb;
                if (UserUtil.getActivePlan(u).equals(p)) {
                    cb = new CheckBox("", true);
                    cb.setReadOnly(true);
                    return cb;
                }
                cb = new CheckBox("", false);
                cb.setReadOnly(true);
                return cb;
            }
        });
        setData(plans, u);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
        table.setConverter("planStartDate", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(java.util.Locale locale) {
                return new SimpleDateFormat(MedcognizeUI.US_DATE_FORMAT);
            }
        });
        table.setConverter("planEndDate", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(java.util.Locale locale) {
                return new SimpleDateFormat(MedcognizeUI.US_DATE_FORMAT);
            }
        });
        if (showHeader) {
            toolbar.showAddNewButton();
            addMakeActivePlanButton();
            addCopyToNewYearButton();
        }
    }

    public void addMakeActivePlanButton() {
        Button makeActivePlan = new Button("Make Active");
        makeActivePlan.setDescription("Make Selected Plan the Active Plan");
        makeActivePlan.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Object s = table.getValue();
                if (null == s) {
                    Notification.show("No plan selected to make the active plan", Notification.Type.HUMANIZED_MESSAGE);
                    return;
                }
                @SuppressWarnings("unchecked")
                Plan p = ((BeanItem<Plan>) table.getItem(s)).getBean();
                if (p.equals(UserUtil.getActivePlan(u))) {
                    Notification.show("Selected plan is already the active plan", Notification.Type.HUMANIZED_MESSAGE);
                    return;
                }
                UserUtil.setActivePlan(repo, u, p);
                table.refreshRows();
            }
        });
        makeActivePlan.addStyleName("small");
        toolbar.addComponent(makeActivePlan);
        toolbar.setComponentAlignment(makeActivePlan, Alignment.MIDDLE_LEFT);
    }

    public void addCopyToNewYearButton() {
        Button copyToNewYearButton = new Button("Copy Plan");
        copyToNewYearButton.setDescription("Copy Plan to Different Year");
        copyToNewYearButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Object s = table.getValue();
                if (null == s) {
                    Notification.show("No plan selected to copy", Notification.Type.HUMANIZED_MESSAGE);
                    return;
                }
                @SuppressWarnings("unchecked")
                final Plan p = ((BeanItem<Plan>) table.getItem(s)).getBean();
                final ComponentWindow window = new ComponentWindow("Copy Selected Plan for a Different Year", false, false);
                final IntStepper newYearField = new IntStepper("New Year");
                Component form = new MFormLayout() {
                    {
                        setSizeUndefined();
                        setMargin(true);
                        final TextField existingYearField = new TextField("Current Year");
                        existingYearField.setValue(String.valueOf(p.getPlanYear()));
                        existingYearField.setEnabled(false);
                        newYearField.setValue(p.getPlanYear() + 1);
                        newYearField.addValidator(new NotEqualIntegerValidator("New Year must be different from the Existing Year", p.getPlanYear()));
                        addComponent(existingYearField);
                        addComponent(newYearField);
                        newYearField.focus();
                    }
                };
                window.setContent(form);
                UI.getCurrent().addWindow(window);
                window.setSubmitButtonText("Save");
                Button.ClickListener addAction = new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        try {
                            newYearField.validate();
                            int newYear = newYearField.getValue();
                            int oldYear = p.getPlanYear();
                            Date newStartDate = DateUtil.sameDayDifferentYear(p.getPlanStartDate(), newYear);
                            Date newEndDate = DateUtil.sameDayDifferentYear(p.getPlanEndDate(), newYear);
                            Plan newPlan = new Plan();
                            DisplayFriendly.copyListedProperties(p, newPlan);
                            newPlan.setPlanYear(newYear);
                            newPlan.setPlanStartDate(newStartDate);
                            newPlan.setPlanEndDate(newEndDate);
                            String oldName = p.getPlanName();
                            String newName;
                            if (oldName.contains(String.valueOf(oldYear))) {
                                newName = oldName.replaceAll(String.valueOf(oldYear), String.valueOf(newYear));
                            } else {
                                newName = oldName + " " + String.valueOf(newYear);
                            }
                            String confirmedUniqueNewName = Plan.ensureUniqueName(newName);
                            newPlan.setPlanName(confirmedUniqueNewName);
                            u.getPlans().add(newPlan);
                            getContainer().addItem(newPlan);
                            window.close();
                        } catch (Validator.InvalidValueException ive) {
                        }
                    }
                };
                window.addClickListenerToSubmitButton(addAction);
            }
        });
        copyToNewYearButton.addStyleName("small");
        toolbar.addComponent(copyToNewYearButton);
        toolbar.setComponentAlignment(copyToNewYearButton, Alignment.MIDDLE_LEFT);
    }
}
