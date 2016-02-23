package com.medcognize.form;

import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.Plan;
import com.medcognize.domain.validator.vaadin.ExistingPlanNameValidator;
import com.medcognize.form.field.MedcognizeFieldGroupFieldFactory;
import com.medcognize.form.field.errorful.ErrorfulGridLayout;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import org.vaadin.addon.daterangefield.DateRangeField;
import org.vaadin.risto.stepper.DateStepper;
import org.vaadin.risto.stepper.IntStepper;

public class PlanForm extends DisplayFriendlyForm<Plan> {

    Field<?> planNameField;
    Field<?> planTypeField;

    // we will show the DateRangeField instead of the three other Fields
    DateRangeField dateRangeField;
    IntStepper planYearField;
    DateStepper planStartDateField;
    DateStepper planEndDateField;

    Field<?> individualInNetworkDeductibleField;
    Field<?> familyInNetworkDeductibleField;
    Field<?> individualOutOfNetworkDeductibleField;
    Field<?> familyOutOfNetworkDeductibleField;
    Field<?> individualOutOfPocketLimitField;
    Field<?> familyOutOfPocketLimitField;
    Field<?> primaryCareCopayField;
    Field<?> specialistCopayField;
    Field<?> emergencyRoomCopayField;
    Field<?> tier1PrescriptionCopayField;
    Field<?> tier2PrescriptionCopayField;
    Field<?> tier3PrescriptionCopayField;
    Field<?> notesField;
    ErrorfulGridLayout outOfNetworkDeductiblesLayout;

    public PlanForm(BeanItem<Plan> bean, boolean isNew) {
        super(bean, null, new MedcognizeFieldGroupFieldFactory(), isNew);
    }

    @Override
    public void setupForm() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        BeanFieldGroup<Plan> group = this.getFieldGroup();

        planNameField = group.getField("planName");
        if (isNew()) {
            planNameField.addValidator(new ExistingPlanNameValidator(null));
        } else {
            planNameField.addValidator(new ExistingPlanNameValidator((String) planNameField.getValue()));
        }
        planTypeField = group.getField("planType");
        planYearField = (IntStepper) group.getField("planYear");
        planStartDateField = (DateStepper) group.getField("planStartDate");
        planEndDateField = (DateStepper) group.getField("planEndDate");
        individualInNetworkDeductibleField = group.getField("individualInNetworkDeductible");
        familyInNetworkDeductibleField = group.getField("familyInNetworkDeductible");
        individualOutOfNetworkDeductibleField = group.getField("individualOutOfNetworkDeductible");
        familyOutOfNetworkDeductibleField = group.getField("familyOutOfNetworkDeductible");
        individualOutOfPocketLimitField = group.getField("individualOutOfPocketLimit");
        familyOutOfPocketLimitField = group.getField("familyOutOfPocketLimit");
        primaryCareCopayField = group.getField("primaryCareCopay");
        specialistCopayField = group.getField("specialistCopay");
        emergencyRoomCopayField = group.getField("emergencyRoomCopay");
        tier1PrescriptionCopayField = group.getField("tier1PrescriptionCopay");
        tier2PrescriptionCopayField = group.getField("tier2PrescriptionCopay");
        tier3PrescriptionCopayField = group.getField("tier3PrescriptionCopay");
        notesField = group.getField("notes");

        ((TextField) planNameField).setDescription("e.g. Acme Insurance Company HMO");
        //planName.addValidator(new MinStringLengthValidator("Plan name cannot be blank", 1));
        //planName.addValidator(new MaxStringLengthValidator("You have exceeded the maximum allowed length", 50));
        addComponent(planNameField);

        //noinspection unchecked
        dateRangeField = new DateRangeField(planStartDateField.getPropertyDataSource(), planEndDateField.getPropertyDataSource(), true,
                planYearField.getPropertyDataSource(), true);
        dateRangeField.setErrorStyleName("steppererrorstyle");
        addComponent(dateRangeField);
        addExcludedField(dateRangeField);
        dateRangeField.setMinYear(Plan.MIN_YEAR);
        dateRangeField.setMaxYear(Plan.MAX_YEAR);

        //planType.addValidator(new NullValidator("You must select a plan type", false));
        addComponent(planTypeField);

        final Label inNetworkDeductiblesLabel = new Label("In Network Annual Deductibles");
        inNetworkDeductiblesLabel.addStyleName("formAreaHeader");
        final Label outOfNetworkDeductiblesLabel = new Label("Out of Network Annual Deductibles");
        outOfNetworkDeductiblesLabel.addStyleName("formAreaHeader");
        final Label outOfPocketLabel = new Label("Out of Pocket Limits");
        outOfPocketLabel.addStyleName("formAreaHeader");

        ErrorfulGridLayout inNetworkDeductiblesLayout = new ErrorfulGridLayout(2, 2);
        inNetworkDeductiblesLayout.setStyleName("borderstyle");
        inNetworkDeductiblesLayout.setSizeUndefined();
        inNetworkDeductiblesLayout.setSpacing(true);
        inNetworkDeductiblesLayout.addComponent(inNetworkDeductiblesLabel, 0, 0, 1, 0);
        inNetworkDeductiblesLayout.addComponent(individualInNetworkDeductibleField, 0, 1);
        inNetworkDeductiblesLayout.addComponent(familyInNetworkDeductibleField, 1, 1);

        //default is HMO so we start out with this part not visible
        outOfNetworkDeductiblesLayout = new ErrorfulGridLayout(2, 2);
        outOfNetworkDeductiblesLayout.setStyleName("borderstyle");
        // without this, the initial value would get lost whenever hidden
        individualOutOfNetworkDeductibleField.setInvalidAllowed(true);
        familyOutOfNetworkDeductibleField.setInvalidAllowed(true);
        outOfNetworkDeductiblesLayout.addComponent(outOfNetworkDeductiblesLabel, 0, 0, 1, 0);
        outOfNetworkDeductiblesLayout.addComponent(individualOutOfNetworkDeductibleField, 0, 1);
        outOfNetworkDeductiblesLayout.addComponent(familyOutOfNetworkDeductibleField, 1, 1);
        showOrHideOutOfNetworkDeductibles(planTypeField.getValue());

        ErrorfulGridLayout outOfPocketLimitsLayout = new ErrorfulGridLayout(2, 2);
        outOfPocketLimitsLayout.setStyleName("borderstyle");
        outOfPocketLimitsLayout.setSizeUndefined();
        outOfPocketLimitsLayout.setSpacing(true);
        outOfPocketLimitsLayout.addComponent(outOfPocketLabel, 0, 0, 1, 0);
        outOfPocketLimitsLayout.addComponent(individualOutOfPocketLimitField, 0, 1);
        outOfPocketLimitsLayout.addComponent(familyOutOfPocketLimitField, 1, 1);

        addComponent(inNetworkDeductiblesLayout);
        addComponent(outOfNetworkDeductiblesLayout);
        addComponent(outOfPocketLimitsLayout);

        Label copaysLabel = new Label("Medical Copays");
        copaysLabel.addStyleName("formAreaHeader");
        ErrorfulGridLayout copaysLayout = new ErrorfulGridLayout(3, 3);
        copaysLayout.setStyleName("borderstyle");
        copaysLayout.setSizeUndefined();
        copaysLayout.setSpacing(true);
        copaysLayout.addComponent(copaysLabel, 0, 0, 2, 0);
        copaysLayout.addComponent(primaryCareCopayField, 0, 1);
        copaysLayout.addComponent(specialistCopayField, 1, 1);
        copaysLayout.addComponent(emergencyRoomCopayField, 2, 1);
        copaysLayout.addComponent(tier1PrescriptionCopayField, 0, 2);
        copaysLayout.addComponent(tier2PrescriptionCopayField, 1, 2);
        copaysLayout.addComponent(tier3PrescriptionCopayField, 2, 2);
        addComponent(copaysLayout);
        addComponent(notesField);

        planTypeField.addValueChangeListener(new Property.ValueChangeListener() {
                                            @Override
                                            public void valueChange(Property.ValueChangeEvent event) {
                                                showOrHideOutOfNetworkDeductibles(event.getProperty().getValue());
                                            }
                                        }
        );
    }

    private void showOrHideOutOfNetworkDeductibles(Object val) {
        if (null == val) {
            outOfNetworkDeductiblesLayout.setVisible(false);
            outOfNetworkDeductiblesLayout.discardInvalidBufferedValues();
        } else {
            if (val.toString().equals(DisplayFriendly.getEnumCaption(Plan.PlanType.HMO))) {
                outOfNetworkDeductiblesLayout.setVisible(false);
                outOfNetworkDeductiblesLayout.discardInvalidBufferedValues();
            } else {
                outOfNetworkDeductiblesLayout.setVisible(true);
            }
        }
    }

    @Override
    public void commit() throws FieldGroup.CommitException {
        dateRangeField.commit();
        super.commit();
    }
}