package com.medcognize.form;

import com.medcognize.domain.*;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.validator.vaadin.InPlanPeriodValidator;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.daterangefield.DateUtil;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class MedicalExpenseForm extends DisplayFriendlyForm<MedicalExpense> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicalExpenseForm.class);

    public Field<?> date = createField("date");
    public Field<?> plan = createField("plan");
    public Field<?> familyMember = createField("familyMember");
    public Field<?> provider = createField("provider");
    public Field<?> medicalExpenseInPlan = createField("medicalExpenseInPlan");
    public Field<?> prescriptionTierType = createField("prescriptionTierType");
    public Field<?> medicalExpenseType = createField("medicalExpenseType");
    public Field<?> outOfPocketAmount = createField("outOfPocketAmount");
    public Field<?> costAccordingToProvider = createField("costAccordingToProvider");
    public Field<?> maximumAmount = createField("maximumAmount");
    public Field<?> deductibleAmount = createField("deductibleAmount");
    public Field<?> copayAmount = createField("copayAmount");
    public Field<?> paymentAmount = createField("paymentAmount");
    public Field<?> comments = createField("comments");

    public GridLayout prescriptionTiersLayout;

    public MedicalExpenseForm(MedicalExpense item) {
        super(MedicalExpense.class, null);
        setSizeUndefined();
        setEntity(item);
    }

    @Override
    protected Component createContent() {
        ((AbstractField) date).setDescription("This is the date of service");
        ((AbstractField) outOfPocketAmount).setDescription("This is how much cash you've paid out of pocket");
        ((AbstractField) costAccordingToProvider).setDescription("This is how much the provider is charging, " +
                "" + "also called the 'Billed Amount'");
        ((AbstractField) maximumAmount).setDescription("This is the most the insurance company will pay under the " + "plan");
        ((AbstractField) deductibleAmount).setDescription("This is the amount you have to pay before the plan will " + "start to pay.");
        ((AbstractField) copayAmount).setDescription("This is the minimum amount you have to pay before " + "the plan will start to pay.");
        ((AbstractField) paymentAmount).setDescription("This is the amount the insurance company will pay");

        //((DateField) date).setDateFormat("yyyy-MM-dd");
        if (null == date.getValue()) {
            ((DateField) date).setValue(DateUtil.now());
        }
        date.setInvalidAllowed(true);
        date.addValidator(new InPlanPeriodValidator("Date has to be within the plan period", plan));

        // must have a plan
        ((TypedSelect) plan).setNullSelectionAllowed(false);
        if (null == plan.getValue()) {
            // set to active plan
            User u = DbUtil.getLoggedInUser();
            Plan ap = UserUtil.getActivePlan(u);
            if (((TypedSelect) plan).getOptions().size() > 0) {
                ((TypedSelect) plan).setValue(ap);
            } else {
                LOGGER.warn("Should not happen. Unable to set MedicalExpense to ActivePlan (" + ap + "). The User's " +
                        "plans list is empty.");
            }
            if (null == plan.getValue()) {
                // still null
                LOGGER.warn("MedicalExpense plan still null after setting it to active plan (" + ap + ")");
            }
        }
        if (null == familyMember.getValue()) {
            // set to first family member in list
            ((TypedSelect) familyMember).setNullSelectionAllowed(true);
            if (((TypedSelect) familyMember).getOptions().size() > 0) {
                Object firstItem = ((TypedSelect) familyMember).getOptions().toArray()[0];
                ((TypedSelect) familyMember).setValue(firstItem);
            }
        }
        if (null == provider.getValue()) {
            ((TypedSelect) provider).setNullSelectionAllowed(true);
            // set to first provider in list
            if (((TypedSelect) provider).getOptions().size() > 0) {
                Object firstItem = ((TypedSelect) provider).getOptions().toArray()[0];
                ((TypedSelect) provider).setValue(firstItem);
            }
        }
        prescriptionTiersLayout = new MMGridLayout(1, 1);
        prescriptionTiersLayout.setStyleName("borderstyle");
        prescriptionTiersLayout.setSizeUndefined();
        prescriptionTiersLayout.setSpacing(true);
        prescriptionTiersLayout.addComponent(prescriptionTierType, 0, 0);
        showOrHidePrescriptionTiersLayout(prescriptionTierType.getValue());
        medicalExpenseType.addValueChangeListener(new Property.ValueChangeListener() {
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
        costsLayout.addComponent(outOfPocketAmount, 0, 0);
        costsLayout.addComponent(costAccordingToProvider, 1, 0);
        costsLayout.addComponent(maximumAmount, 0, 1);
        costsLayout.addComponent(deductibleAmount, 1, 1);
        costsLayout.addComponent(copayAmount, 0, 2);
        costsLayout.addComponent(paymentAmount, 1, 2);
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
                        ((TypedSelect) familyMember).addOption(entity);
                        // familyMemberField.select(fm);
                        form.closePopup();
                    }
                });
                form.setModalWindowTitle("Add Family Member");
                form.openInModalPopup();
            }
        });
        MHorizontalLayout familyMemberPlusButtonLayout = new MHorizontalLayout();
        familyMemberPlusButtonLayout.addComponent(familyMember);
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
                        ((TypedSelect) provider).addOption(entity);
                        //providerField.select(entity);
                        form.closePopup();
                    }
                });
                form.setModalWindowTitle("Add Medical Provider");
                form.openInModalPopup();
            }
        });
        MHorizontalLayout providerPlusButtonLayout = new MHorizontalLayout();
        providerPlusButtonLayout.addComponent(provider);
        providerPlusButtonLayout.addComponent(addProviderButton);
        addProviderButton.setWidth("35px");
        addProviderButton.setHeight("50%");
        providerPlusButtonLayout.setSpacing(true);
        providerPlusButtonLayout.setComponentAlignment(addProviderButton, Alignment.BOTTOM_LEFT);

        GridLayout topLayout = new MMGridLayout(2, 4);
        topLayout.setWidth("100%");
        topLayout.setSpacing(true);
        topLayout.addComponent(date, 0, 0);
        topLayout.addComponent(plan, 1, 0);
        topLayout.addComponent(familyMemberPlusButtonLayout, 0, 1);
        topLayout.addComponent(providerPlusButtonLayout, 1, 1);
        topLayout.addComponent(medicalExpenseInPlan, 0, 2);
        topLayout.addComponent(medicalExpenseType, 0, 3);
        topLayout.addComponent(prescriptionTierType, 1, 3);

        form.addComponent(topLayout);
        form.addComponent(costsLayout);
        form.addComponent(comments);
        comments.setWidth("100%");

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
        return DisplayFriendly.getFriendlyPropertyName(MedicalExpense.class, propName);
    }
}