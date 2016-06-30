package com.medcognize.form;

import com.medcognize.domain.*;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.validator.vaadin.InPlanPeriodValidator;
import com.medcognize.form.field.ViritinFieldGroupFieldFactory;
import com.medcognize.form.field.errorful.ErrorfulGridLayout;
import com.medcognize.form.field.errorful.ErrorfulHorizontalLayout;
import com.medcognize.util.CrudUtil;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.medcognize.view.crud.CommitAction;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.daterangefield.DateUtil;
import org.vaadin.risto.stepper.DateStepper;

import java.util.List;

public class MedicalExpenseForm extends DisplayFriendlyForm<MedicalExpense> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MedicalExpenseForm.class);

    public DateStepper dateField;
    public NativeSelect planField;
    public NativeSelect familyMemberField;
    public NativeSelect providerField;
    public Field<?> medicalExpenseInPlanField;
    public ErrorfulGridLayout prescriptionTiersLayout;
    public Field<?> prescriptionTierTypeField;
    public Field<?> medicalExpenseTypeField;
    public Field<?> outOfPocketAmountField;
    public Field<?> costAccordingToProviderField;
    public Field<?> maximumAmountField;
    public Field<?> deductibleAmountField;
    public Field<?> copayAmountField;
    public Field<?> paymentAmountField;
    public Field<?> commentsField;

    public MedicalExpenseForm(BeanItem<MedicalExpense> bean, boolean isNew) {
        super(bean, null, new ViritinFieldGroupFieldFactory(), isNew);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setupForm() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        BeanFieldGroup<MedicalExpense> group = this.getFieldGroup();

        dateField = (DateStepper) group.getField("date");
        dateField.setDescription("This is the date of service");
        planField = (NativeSelect) group.getField("plan");
        familyMemberField = (NativeSelect) group.getField("familyMember");
        providerField = (NativeSelect) group.getField("provider");
        medicalExpenseInPlanField = group.getField("medicalExpenseInPlan");
        medicalExpenseTypeField = group.getField("medicalExpenseType");
        prescriptionTierTypeField = group.getField("prescriptionTierType");
        outOfPocketAmountField = group.getField("outOfPocketAmount");
        ((AbstractField) outOfPocketAmountField).setDescription("This is how much cash you've paid out of pocket");
        costAccordingToProviderField = group.getField("costAccordingToProvider");
        ((AbstractField) costAccordingToProviderField).setDescription("This is how much the provider is charging, " +
                "" + "also called the 'Billed Amount'");
        maximumAmountField = group.getField("maximumAmount");
        ((AbstractField) maximumAmountField).setDescription("This is the most the insurance company will pay under the " + "plan");
        deductibleAmountField = group.getField("deductibleAmount");
        ((AbstractField) deductibleAmountField).setDescription("This is the amount you have to pay before the plan will " + "start to pay.");
        copayAmountField = group.getField("copayAmount");
        ((AbstractField) copayAmountField).setDescription("This is the minimum amount you have to pay before " + "the plan will start to pay.");
        paymentAmountField = group.getField("paymentAmount");
        ((AbstractField) paymentAmountField).setDescription("This is the amount the insurance company will pay");
        commentsField = group.getField("comments");
        ((TextField)commentsField).setNullRepresentation("");

        //((DateField) date).setDateFormat("yyyy-MM-dd");
        if (null == dateField.getValue()) {
            dateField.setValue(DateUtil.now());
        }
        dateField.setInvalidAllowed(true);
        dateField.addValidator(new InPlanPeriodValidator("Date has to be within the plan period", planField));

        // must have a plan
        planField.setNullSelectionAllowed(false);
        if (null == planField.getValue()) {
            // set to active plan
            User u = DbUtil.getLoggedInUser();
            Plan ap = UserUtil.getActivePlan(u);
            if (planField.getItemIds().size() > 0) {
                planField.setValue(ap);
            } else {
                LOGGER.warn("Should not happen. Unable to set MedicalExpense to ActivePlan (" + ap + "). The User's " +
                        "plans list is empty.");
            }
            if (null == planField.getValue()) {
                // still null
                LOGGER.warn("MedicalExpense plan still null after setting it to active plan (" + ap + ")");
            }
        }
        if (null == familyMemberField.getValue()) {
            // set to first family member in list
            familyMemberField.setNullSelectionAllowed(true);
            if (familyMemberField.getItemIds().size() > 0) {
                Object firstItem = familyMemberField.getItemIds().toArray()[0];
                familyMemberField.setValue(firstItem);
            }
        }
        if (null == providerField.getValue()) {
            providerField.setNullSelectionAllowed(true);
            // set to first provider in list
            if (providerField.getItemIds().size() > 0) {
                Object firstItem = providerField.getItemIds().toArray()[0];
                providerField.setValue(firstItem);
            }
        }
        prescriptionTiersLayout = new ErrorfulGridLayout(1, 1);
        prescriptionTiersLayout.setStyleName("borderstyle");
        prescriptionTiersLayout.setSizeUndefined();
        prescriptionTiersLayout.setSpacing(true);
        prescriptionTiersLayout.addComponent(prescriptionTierTypeField, 0, 0);
        showOrHidePrescriptionTiersLayout(prescriptionTierTypeField.getValue());
        medicalExpenseTypeField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                showOrHidePrescriptionTiersLayout(event.getProperty().getValue());
            }
        });

        Label costsLabel = new Label("Costs");
        costsLabel.addStyleName("formAreaHeader");
        ErrorfulGridLayout costsLayout = new ErrorfulGridLayout(2, 3);
        costsLayout.setStyleName("borderstyle");
        costsLayout.setSizeUndefined();
        costsLayout.setSpacing(true);
        costsLayout.addComponent(outOfPocketAmountField, 0, 0);
        costsLayout.addComponent(costAccordingToProviderField, 1, 0);
        costsLayout.addComponent(maximumAmountField, 0, 1);
        costsLayout.addComponent(deductibleAmountField, 1, 1);
        costsLayout.addComponent(copayAmountField, 0, 2);
        costsLayout.addComponent(paymentAmountField, 1, 2);
        addComponent(costsLayout);

        Button addFamilyMemberButton = new NativeButton("<div style=\"text-align: center;\"><font " + "size=\"1\">Add<br>New</font></div>");
        addFamilyMemberButton.setDescription("Add new Family Member");
        addFamilyMemberButton.setHtmlContentAllowed(true);
        addFamilyMemberButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                FamilyMember fm = new FamilyMember();
                final BeanItem<FamilyMember> bi = new BeanItem(fm);
                final DisplayFriendlyForm<FamilyMember> form = CrudUtil.createForm(FamilyMemberForm.class, bi, true);
                final CommitAction ca = new CommitAction() {

                    @Override
                    public void run() {
                        User u = DbUtil.getLoggedInUser();
                        u.getFamilyMembers().add(bi.getBean());
                        Container c = familyMemberField.getContainerDataSource();
                        c.removeAllItems();
                        List<FamilyMember> fms = UserUtil.getAll(u, FamilyMember.class);
                        for (FamilyMember fm : fms) {
                            c.addItem(fm);
                        }
                        familyMemberField.select(bi.getBean());
                    }
                };
                CrudUtil.showForm(form, ca, "Add Family Member");
            }
        });
        ErrorfulHorizontalLayout familyMemberPlusButtonLayout = new ErrorfulHorizontalLayout();
        familyMemberPlusButtonLayout.addComponent(familyMemberField);
        familyMemberPlusButtonLayout.addComponent(addFamilyMemberButton);
        addFamilyMemberButton.setWidth("35px");
        addFamilyMemberButton.setHeight("50%");
        familyMemberPlusButtonLayout.setSpacing(true);
        familyMemberPlusButtonLayout.setComponentAlignment(addFamilyMemberButton, Alignment.BOTTOM_LEFT);

        Button addProviderButton = new NativeButton("<div style=\"text-align: center;\"><font " + "size=\"1\">Add<br>New</font></div>");
        addProviderButton.setDescription("Add new Medical Provider");
        addProviderButton.setHtmlContentAllowed(true);
        addProviderButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Provider pr = new Provider();
                final BeanItem<Provider> bi = new BeanItem(pr);
                DisplayFriendlyForm<Provider> form = CrudUtil.createForm(ProviderForm.class, bi, true);
                CommitAction ca = new CommitAction() {

                    @Override
                    public void run() {
                        User u = DbUtil.getLoggedInUser();
                        u.getProviders().add(bi.getBean());
                        Container c = providerField.getContainerDataSource();
                        c.removeAllItems();
                        List<Provider> prs = UserUtil.getAll(u, Provider.class);
                        for (Provider pr : prs) {
                            c.addItem(pr);
                        }
                        providerField.select(bi.getBean());
                    }
                };
                CrudUtil.showForm(form, ca, "Add Medical Provider");
            }
        });
        ErrorfulHorizontalLayout providerPlusButtonLayout = new ErrorfulHorizontalLayout();
        providerPlusButtonLayout.addComponent(providerField);
        providerPlusButtonLayout.addComponent(addProviderButton);
        addProviderButton.setWidth("35px");
        addProviderButton.setHeight("50%");
        providerPlusButtonLayout.setSpacing(true);
        providerPlusButtonLayout.setComponentAlignment(addProviderButton, Alignment.BOTTOM_LEFT);

        ErrorfulGridLayout topLayout = new ErrorfulGridLayout(2, 4);
        topLayout.setWidth("100%");
        topLayout.setSpacing(true);
        topLayout.addComponent(dateField, 0, 0);
        topLayout.addComponent(planField, 1, 0);
        topLayout.addComponent(familyMemberPlusButtonLayout, 0, 1);
        topLayout.addComponent(providerPlusButtonLayout, 1, 1);
        topLayout.addComponent(medicalExpenseInPlanField, 0, 2);
        topLayout.addComponent(medicalExpenseTypeField, 0, 3);
        topLayout.addComponent(prescriptionTierTypeField, 1, 3);

        addComponent(topLayout);
        addComponent(costsLayout);
        addComponent(commentsField);
        commentsField.setWidth("100%");
    }

    private void showOrHidePrescriptionTiersLayout(Object val) {
        if (null == val) {
            prescriptionTiersLayout.setVisible(false);
            prescriptionTiersLayout.discardInvalidBufferedValues();
        } else {
            if (val.toString().equals(DisplayFriendly.getEnumCaption(MedicalExpense.MedicalExpenseType.PRESCRIPTION))) {
                prescriptionTiersLayout.setVisible(true);
                prescriptionTiersLayout.discardInvalidBufferedValues();
            } else {
                prescriptionTiersLayout.setVisible(false);
            }
        }
    }

}