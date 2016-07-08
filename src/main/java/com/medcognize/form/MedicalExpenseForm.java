package com.medcognize.form;

import com.medcognize.UserRepository;
import com.medcognize.domain.*;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.validator.vaadin.InPlanPeriodValidator;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.daterangefield.DateUtil;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class MedicalExpenseForm extends DisplayFriendlyForm<MedicalExpense> {

    @Autowired
    protected UserRepository repo;

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicalExpenseForm.class);

    public Field<?> dateField;
    public Field<?> planField;
    public Field<?> familyMemberField;
    public Field<?> providerField;
    public Field<?> medicalExpenseInPlanField;
    public Field<?> prescriptionTierTypeField;
    public Field<?> medicalExpenseTypeField;
    public Field<?> outOfPocketAmountField;
    public Field<?> costAccordingToProviderField;
    public Field<?> maximumAmountField;
    public Field<?> deductibleAmountField;
    public Field<?> copayAmountField;
    public Field<?> paymentAmountField;
    public Field<?> commentsField;

    public GridLayout prescriptionTiersLayout;

    public MedicalExpenseForm(MedicalExpense item) {
        setSizeUndefined();
        setEntity(item);
    }

    @Override
    protected Component createContent() {
        ((AbstractField) dateField).setDescription("This is the date of service");
        ((AbstractField) outOfPocketAmountField).setDescription("This is how much cash you've paid out of pocket");
        ((AbstractField) costAccordingToProviderField).setDescription("This is how much the provider is charging, " +
                "" + "also called the 'Billed Amount'");
        ((AbstractField) maximumAmountField).setDescription("This is the most the insurance company will pay under the " + "plan");
        ((AbstractField) deductibleAmountField).setDescription("This is the amount you have to pay before the plan will " + "start to pay.");
        ((AbstractField) copayAmountField).setDescription("This is the minimum amount you have to pay before " + "the plan will start to pay.");
        ((AbstractField) paymentAmountField).setDescription("This is the amount the insurance company will pay");

        //((DateField) date).setDateFormat("yyyy-MM-dd");
        if (null == dateField.getValue()) {
            ((DateField) dateField).setValue(DateUtil.now());
        }
        dateField.setInvalidAllowed(true);
        dateField.addValidator(new InPlanPeriodValidator("Date has to be within the plan period", planField));

        // must have a plan
        ((TypedSelect) planField).setNullSelectionAllowed(false);
        if (null == planField.getValue()) {
            // set to active plan
            User u = DbUtil.getLoggedInUser();
            Plan ap = UserUtil.getActivePlan(u);
            if (((TypedSelect) planField).getOptions().size() > 0) {
                ((TypedSelect) planField).setValue(ap);
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
            ((TypedSelect) familyMemberField).setNullSelectionAllowed(true);
            if (((TypedSelect) familyMemberField).getOptions().size() > 0) {
                Object firstItem = ((TypedSelect) familyMemberField).getOptions().toArray()[0];
                ((TypedSelect) familyMemberField).setValue(firstItem);
            }
        }
        if (null == providerField.getValue()) {
            ((TypedSelect) providerField).setNullSelectionAllowed(true);
            // set to first provider in list
            if (((TypedSelect) providerField).getOptions().size() > 0) {
                Object firstItem = ((TypedSelect) providerField).getOptions().toArray()[0];
                ((TypedSelect) providerField).setValue(firstItem);
            }
        }
        prescriptionTiersLayout = new MMGridLayout(1, 1);
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
        GridLayout costsLayout = new MMGridLayout(2, 3);
        costsLayout.setStyleName("borderstyle");
        costsLayout.setSizeUndefined();
        costsLayout.setSpacing(true);
        costsLayout.addComponent(outOfPocketAmountField, 0, 0);
        costsLayout.addComponent(costAccordingToProviderField, 1, 0);
        costsLayout.addComponent(maximumAmountField, 0, 1);
        costsLayout.addComponent(deductibleAmountField, 1, 1);
        costsLayout.addComponent(copayAmountField, 0, 2);
        costsLayout.addComponent(paymentAmountField, 1, 2);
        MFormLayout form = new MFormLayout();
        form.addComponent(costsLayout);

        Button addFamilyMemberButton = new NativeButton("<div style=\"text-align: center;\"><font " + "size=\"1\">Add<br>New</font></div>");
        addFamilyMemberButton.setDescription("Add new Family Member");
        addFamilyMemberButton.setHtmlContentAllowed(true);
        addFamilyMemberButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final FamilyMember fm = new FamilyMember();
                final FamilyMemberForm form = new FamilyMemberForm(fm);
                form.setSavedHandler(new SavedHandler<FamilyMember>() {
                    @Override
                    public void onSave(FamilyMember entity) {
                        User u = DbUtil.getLoggedInUser();
                        UserUtil.addToCollection(repo, u, entity);
                        ((TypedSelect) familyMemberField).addOption(entity);
                        // familyMemberField.select(fm);
                    }
                });
                form.setModalWindowTitle("Add Family Member");
                form.openInModalPopup();
            }
        });
        MHorizontalLayout familyMemberPlusButtonLayout = new MHorizontalLayout();
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
                final Provider pr = new Provider();
                ProviderForm form = new ProviderForm(pr);
                form.setSavedHandler(new SavedHandler<Provider>() {
                    @Override
                    public void onSave(Provider entity) {
                        User u = DbUtil.getLoggedInUser();
                        UserUtil.addToCollection(repo, u, entity);
                        ((TypedSelect) providerField).addOption(entity);
                        //providerField.select(entity);
                    }
                });
                form.setModalWindowTitle("Add Medical Provider");
                form.openInModalPopup();
            }
        });
        MHorizontalLayout providerPlusButtonLayout = new MHorizontalLayout();
        providerPlusButtonLayout.addComponent(providerField);
        providerPlusButtonLayout.addComponent(addProviderButton);
        addProviderButton.setWidth("35px");
        addProviderButton.setHeight("50%");
        providerPlusButtonLayout.setSpacing(true);
        providerPlusButtonLayout.setComponentAlignment(addProviderButton, Alignment.BOTTOM_LEFT);

        GridLayout topLayout = new MMGridLayout(2, 4);
        topLayout.setWidth("100%");
        topLayout.setSpacing(true);
        topLayout.addComponent(dateField, 0, 0);
        topLayout.addComponent(planField, 1, 0);
        topLayout.addComponent(familyMemberPlusButtonLayout, 0, 1);
        topLayout.addComponent(providerPlusButtonLayout, 1, 1);
        topLayout.addComponent(medicalExpenseInPlanField, 0, 2);
        topLayout.addComponent(medicalExpenseTypeField, 0, 3);
        topLayout.addComponent(prescriptionTierTypeField, 1, 3);

        form.addComponent(topLayout);
        form.addComponent(costsLayout);
        form.addComponent(commentsField);
        commentsField.setWidth("100%");

        return new MVerticalLayout(form.withWidth(""), getToolbar()).withWidth("");
    }

    private void showOrHidePrescriptionTiersLayout(Object val) {
        if (null == val) {
            prescriptionTiersLayout.setVisible(false);
            // prescriptionTiersLayout.discardInvalidBufferedValues();
        } else {
            if (val.toString().equals(DisplayFriendly.getEnumCaption(MedicalExpense.MedicalExpenseType.PRESCRIPTION))) {
                prescriptionTiersLayout.setVisible(true);
                // prescriptionTiersLayout.discardInvalidBufferedValues();
            } else {
                prescriptionTiersLayout.setVisible(false);
            }
        }
    }

    private String getCaption(String propName) {
        return DisplayFriendly.getPropertyCaption(MedicalExpense.class, propName);
    }
}