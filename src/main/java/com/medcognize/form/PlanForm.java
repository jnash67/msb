package com.medcognize.form;

import com.medcognize.domain.Plan;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.validator.vaadin.ExistingPlanNameValidator;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class PlanForm extends DisplayFriendlyForm<Plan> {

    Field<?> planName = createField("planName");
    Field<?> planType = createField("planType");

    Field<?> planYear = createField("planYear");
    Field<?> planStartDate = createField("planStartDate");
    Field<?> planEndDate = createField("planEndDate");

    Field<?> individualInNetworkDeductible = createField("individualInNetworkDeductible");
    Field<?> familyInNetworkDeductible = createField("familyInNetworkDeductible");
    Field<?> individualOutOfNetworkDeductible = createField("individualOutOfNetworkDeductible");
    Field<?> familyOutOfNetworkDeductible = createField("familyOutOfNetworkDeductible");
    Field<?> individualOutOfPocketLimit = createField("individualOutOfPocketLimit");
    Field<?> familyOutOfPocketLimit = createField("familyOutOfPocketLimit");
    Field<?> primaryCareCopay = createField("primaryCareCopay");
    Field<?> specialistCopay = createField("specialistCopay");
    Field<?> emergencyRoomCopay = createField("emergencyRoomCopay");
    Field<?> tier1PrescriptionCopay = createField("tier1PrescriptionCopay");
    Field<?> tier2PrescriptionCopay = createField("tier2PrescriptionCopay");
    Field<?> tier3PrescriptionCopay = createField("tier3PrescriptionCopay");
    Field<?> notes = createField("notes");
    GridLayout outOfNetworkDeductiblesLayout;

    public PlanForm(Plan item) {
        super(Plan.class, null);
        setSizeUndefined();
        setEntity(item);
    }

    @Override
    protected Component createContent() {

        MFormLayout form = new MFormLayout();
        planName.addValidator(new ExistingPlanNameValidator((String) planName.getValue()));
        ((AbstractField) planName).setDescription("e.g. Acme Insurance Company HMO");
        //planName.addValidator(new MinStringLengthValidator("Plan name cannot be blank", 1));
        //planName.addValidator(new MaxStringLengthValidator("You have exceeded the maximum allowed length", 50));
        form.addComponent(planName);

        MHorizontalLayout dateRangeField = new MHorizontalLayout(planYear, planStartDate, planEndDate);
        form.addComponent(dateRangeField);

        //planType.addValidator(new NullValidator("You must select a plan type", false));
        form.addComponent(planType);

        final Label inNetworkDeductiblesLabel = new Label("In Network Annual Deductibles");
        inNetworkDeductiblesLabel.addStyleName("formAreaHeader");
        final Label outOfNetworkDeductiblesLabel = new Label("Out of Network Annual Deductibles");
        outOfNetworkDeductiblesLabel.addStyleName("formAreaHeader");
        final Label outOfPocketLabel = new Label("Out of Pocket Limits");
        outOfPocketLabel.addStyleName("formAreaHeader");

        GridLayout inNetworkDeductiblesLayout = new MMGridLayout(2, 2);
        inNetworkDeductiblesLayout.setStyleName("borderstyle");
        inNetworkDeductiblesLayout.setSizeUndefined();
        inNetworkDeductiblesLayout.setSpacing(true);
        inNetworkDeductiblesLayout.addComponent(inNetworkDeductiblesLabel, 0, 0, 1, 0);
        inNetworkDeductiblesLayout.addComponent(individualInNetworkDeductible, 0, 1);
        inNetworkDeductiblesLayout.addComponent(familyInNetworkDeductible, 1, 1);

        //default is HMO so we start out with this part not visible
        outOfNetworkDeductiblesLayout = new MMGridLayout(2, 2);
        outOfNetworkDeductiblesLayout.setStyleName("borderstyle");
        // without this, the initial value would get lost whenever hidden
        individualOutOfNetworkDeductible.setInvalidAllowed(true);
        familyOutOfNetworkDeductible.setInvalidAllowed(true);
        outOfNetworkDeductiblesLayout.addComponent(outOfNetworkDeductiblesLabel, 0, 0, 1, 0);
        outOfNetworkDeductiblesLayout.addComponent(individualOutOfNetworkDeductible, 0, 1);
        outOfNetworkDeductiblesLayout.addComponent(familyOutOfNetworkDeductible, 1, 1);
        showOrHideOutOfNetworkDeductibles(planType.getValue());

        GridLayout outOfPocketLimitsLayout = new MMGridLayout(2, 2);
        outOfPocketLimitsLayout.setStyleName("borderstyle");
        outOfPocketLimitsLayout.setSizeUndefined();
        outOfPocketLimitsLayout.setSpacing(true);
        outOfPocketLimitsLayout.addComponent(outOfPocketLabel, 0, 0, 1, 0);
        outOfPocketLimitsLayout.addComponent(individualOutOfPocketLimit, 0, 1);
        outOfPocketLimitsLayout.addComponent(familyOutOfPocketLimit, 1, 1);

        form.addComponent(inNetworkDeductiblesLayout);
        form.addComponent(outOfNetworkDeductiblesLayout);
        form.addComponent(outOfPocketLimitsLayout);

        Label copaysLabel = new Label("Medical Copays");
        copaysLabel.addStyleName("formAreaHeader");
        GridLayout copaysLayout = new GridLayout(3, 3);
        copaysLayout.setStyleName("borderstyle");
        copaysLayout.setSizeUndefined();
        copaysLayout.setSpacing(true);
        copaysLayout.addComponent(copaysLabel, 0, 0, 2, 0);
        copaysLayout.addComponent(primaryCareCopay, 0, 1);
        copaysLayout.addComponent(specialistCopay, 1, 1);
        copaysLayout.addComponent(emergencyRoomCopay, 2, 1);
        copaysLayout.addComponent(tier1PrescriptionCopay, 0, 2);
        copaysLayout.addComponent(tier2PrescriptionCopay, 1, 2);
        copaysLayout.addComponent(tier3PrescriptionCopay, 2, 2);
        form.addComponent(copaysLayout);
        form.addComponent(notes);

        planType.addValueChangeListener(new Property.ValueChangeListener() {
                                            @Override
                                            public void valueChange(Property.ValueChangeEvent event) {
                                                showOrHideOutOfNetworkDeductibles(event.getProperty().getValue());
                                            }
                                        }
        );

        return new MVerticalLayout(form.withWidth(""), getToolbar()).withWidth("");
    }

    private void showOrHideOutOfNetworkDeductibles(Object val) {
        if (null == val) {
            outOfNetworkDeductiblesLayout.setVisible(false);
            //outOfNetworkDeductiblesLayout.discardInvalidBufferedValues();
        } else {
            if (val.toString().equals(DisplayFriendly.getEnumCaption(Plan.PlanType.HMO))) {
                outOfNetworkDeductiblesLayout.setVisible(false);
                //outOfNetworkDeductiblesLayout.discardInvalidBufferedValues();
            } else {
                outOfNetworkDeductiblesLayout.setVisible(true);
            }
        }
    }

}