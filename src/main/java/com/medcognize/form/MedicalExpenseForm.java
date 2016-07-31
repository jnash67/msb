package com.medcognize.form;

import com.medcognize.UserRepository;
import com.medcognize.domain.*;
import com.medcognize.domain.validator.vaadin.InPlanPeriodValidator;
import com.medcognize.form.field.DisplayFriendlySelectAndButton;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.daterangefield.DateUtil;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@Slf4j
public class MedicalExpenseForm extends DisplayFriendlyForm<MedicalExpense> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicalExpenseForm.class);

    public Field<?> date = createField("date");
    public Field<?> plan = createField("plan");
    public Field<?> familyMember = new DisplayFriendlySelectAndButton<FamilyMember>(FamilyMember.class);
    public Field<?> provider = new DisplayFriendlySelectAndButton<Provider>(Provider.class);
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

    public MedicalExpenseForm(MedicalExpense item, boolean isNew, UserRepository repo) {
        super(MedicalExpense.class, isNew, null, repo);
        setSizeUndefined();
        setEntity(item);
        setupFields();
    }

    protected void setupFields() {
        ((AbstractField) date).setDescription("This is the date of service");
        ((AbstractField) outOfPocketAmount).setDescription("This is how much cash you've paid out of pocket");
        ((AbstractField) costAccordingToProvider).setDescription("This is how much the provider is charging, " +
                "" + "also called the 'Billed Amount'");
        ((AbstractField) maximumAmount).setDescription("This is the most the insurance company will pay under the " +
                "plan");
        ((AbstractField) deductibleAmount).setDescription("This is the amount you have to pay before the plan will "
                + "start to pay.");
        ((AbstractField) copayAmount).setDescription("This is the minimum amount you have to pay before " + "the plan" +
                " will start to pay.");
        ((AbstractField) paymentAmount).setDescription("This is the amount the insurance company will pay");
        //((DateField) date).setDateFormat("yyyy-MM-dd");
        if (null == date.getValue()) {
            ((DateField) date).setValue(DateUtil.now());
        }
        date.setInvalidAllowed(true);
        date.addValidator(new InPlanPeriodValidator("Date has to be within the plan period", plan));
        // must have a plan
        ((TypedSelect<Plan>) plan).setNullSelectionAllowed(false);
        if (null == plan.getValue()) {
            // set to active plan
            User u = DbUtil.getLoggedInUser();
            Plan ap = UserUtil.getActivePlan(u);
            if (((TypedSelect<Plan>) plan).getOptions().size() > 0) {
                ((TypedSelect<Plan>) plan).setValue(ap);
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
            ((TypedSelect<FamilyMember>) familyMember).setNullSelectionAllowed(true);
            if (((TypedSelect<FamilyMember>) familyMember).getOptions().size() > 0) {
                Object firstItem = ((TypedSelect) familyMember).getOptions().toArray()[0];
                ((TypedSelect<FamilyMember>) familyMember).setValue(firstItem);
            }
        }
        if (null == provider.getValue()) {
            ((TypedSelect<Provider>) provider).setNullSelectionAllowed(true);
            // set to first provider in list
            if (((TypedSelect<Provider>) provider).getOptions().size() > 0) {
                Object firstItem = ((TypedSelect) provider).getOptions().toArray()[0];
                ((TypedSelect<Provider>) provider).setValue(firstItem);
            }
        }
        ((EnumSelect<MedicalExpense.MedicalExpenseType>) medicalExpenseType).addMValueChangeListener(new MValueChangeListener() {
            @Override
            public void valueChange(MValueChangeEvent event) {
                showOrHidePrescriptionTiersLayout(event.getValue());
            }
        });
        showOrHidePrescriptionTiersLayout(medicalExpenseType.getValue());
    }

    @Override
    protected Component createContent() {

        Button addNewFamilyMemberButton = new MButton(FontAwesome.PLUS, this::addNewFamilyMember);
        addNewFamilyMemberButton.setDescription("Add new Family Member");
        ((DisplayFriendlySelectAndButton) familyMember).setButton(addNewFamilyMemberButton);

        Button addNewProviderButton = new MButton(FontAwesome.PLUS, this::addNewProvider);
        addNewProviderButton.setDescription("Add new Medical Provider");
        ((DisplayFriendlySelectAndButton) provider).setButton(addNewProviderButton);

//        MHorizontalLayout fml = new MHorizontalLayout(familyMember).withMargin(false);
//        MHorizontalLayout familyMemberPlusButton = new MHorizontalLayout(fml, addNewFamilyMemberButton)
//                .withSizeUndefined();
//        familyMemberPlusButton.setComponentAlignment(addNewFamilyMemberButton, Alignment.BOTTOM_LEFT);
//
//        MHorizontalLayout providerPlusButton = new MHorizontalLayout(provider, addNewProviderButton).withMargin
//                (false).withSizeUndefined();
//        providerPlusButton.setComponentAlignment(addNewProviderButton, Alignment.BOTTOM_LEFT);

        comments.setWidth("100%");
        return new MVerticalLayout(new MHorizontalLayout(date, plan), familyMember, provider,
                medicalExpenseInPlan, new MHorizontalLayout(medicalExpenseType, prescriptionTierType), new
                MHorizontalLayout(outOfPocketAmount,
                costAccordingToProvider), new MHorizontalLayout(maximumAmount, deductibleAmount), new
                MHorizontalLayout(copayAmount,
                paymentAmount), comments, getToolbar());
    }

    private void addNewFamilyMember(Button.ClickEvent e) {
        final FamilyMember fm = new FamilyMember();
        final FamilyMemberForm form = new FamilyMemberForm(fm, true, repo);
        form.setSavedHandler(new SavedHandler<FamilyMember>() {
            @Override
            public void onSave(FamilyMember entity) {
                User u = DbUtil.getLoggedInUser();
                UserUtil.addToCollection(repo, u, entity);
                // ((TypedSelect) familyMember).addOption(entity);
                ((TypedSelect) familyMember).setValue(entity);
                form.closePopup();
            }
        });
        form.setResetHandler(new ResetHandler<FamilyMember>() {
            @Override
            public void onReset(FamilyMember entity) {
                form.closePopup();
            }
        });
        form.setModalWindowTitle("Add Family Member");
        form.openInModalPopup();
    }

    private void addNewProvider(Button.ClickEvent e) {
        final Provider pr = new Provider();
        ProviderForm form = new ProviderForm(pr, true, repo);
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
        form.setResetHandler(new ResetHandler<Provider>() {
            @Override
            public void onReset(Provider entity) {
                form.closePopup();
            }
        });
        form.setModalWindowTitle("Add Medical Provider");
        form.openInModalPopup();
    }

    private void showOrHidePrescriptionTiersLayout(Object val) {
        System.out.println(val);
        if (null == val) {
            // prescriptionTiersLayout.setVisible(false);
            // prescriptionTierType.setEnabled(false);
            prescriptionTierType.setVisible(false);
            // prescriptionTiersLayout.discardInvalidBufferedValues();
        } else {
            String p = MedicalExpense.MedicalExpenseType.PRESCRIPTION.getName();
            if (val.toString().equals(p)) {
                // prescriptionTiersLayout.setVisible(true);
                // prescriptionTierType.setEnabled(true);
                prescriptionTierType.setVisible(true);
                // prescriptionTiersLayout.discardInvalidBufferedValues();
            } else {
                // prescriptionTiersLayout.setVisible(false);
                // prescriptionTierType.setEnabled(false);
                prescriptionTierType.setVisible(false);
            }
        }
    }
}