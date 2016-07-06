package com.medcognize.form.wizard;

import com.medcognize.domain.MedicalExpense;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.MedicalExpenseForm;
import com.medcognize.form.field.YesNoSelect;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.viritin.layouts.MFormLayout;

public class BasicDetailsStep implements WizardStep {

    final MedicalExpenseForm shadowForm;

    public BasicDetailsStep(MedicalExpenseForm shadowForm) {
        this.shadowForm = shadowForm;
    }

    public String getCaption() {
        return "Basic Details";
    }

    public Component getContent() {
        MFormLayout content = new MFormLayout();
        content.setSizeUndefined();

        shadowForm.dateField.setCaption("What is the date of the medical expense:");
        shadowForm.dateField.setWidth("75px");
        shadowForm.familyMemberField.setCaption("Who is the expense for:");
        shadowForm.providerField.setCaption("Which medical provider is the expense with:");
        shadowForm.medicalExpenseInPlanField.setCaption("Is this expense in plan?");
        YesNoSelect shadow = new YesNoSelect("Is this expense in plan?", (com.vaadin.ui.CheckBox) shadowForm.medicalExpenseInPlanField);
        shadowForm.medicalExpenseTypeField.setCaption("What sort of expense is this?");

        content.addComponent(shadowForm.dateField);
        content.addComponent(shadowForm.familyMemberField);
        content.addComponent(shadowForm.providerField);
        content.addComponent(shadow);
        content.addComponent(shadowForm.medicalExpenseTypeField);
        content.addComponent(shadowForm.prescriptionTierTypeField);
        if (shadowForm.medicalExpenseTypeField.getPropertyDataSource().getValue().toString().equals(DisplayFriendly.getEnumCaption(MedicalExpense
                .MedicalExpenseType.PRESCRIPTION))) {
            shadowForm.prescriptionTierTypeField.setVisible(true);
        } else {
            shadowForm.prescriptionTierTypeField.setVisible(false);
        }
        shadowForm.medicalExpenseTypeField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue().toString().equals(DisplayFriendly.getEnumCaption(MedicalExpense
                        .MedicalExpenseType.PRESCRIPTION))) {
                    shadowForm.prescriptionTierTypeField.setVisible(true);
                } else {
                    shadowForm.prescriptionTierTypeField.setVisible(false);
                    if (!shadowForm.prescriptionTierTypeField.isValid()) {
                        shadowForm.prescriptionTierTypeField.discard();
                    }
                }
            }
        });
        return content;
    }

    public boolean onAdvance() {
        try {
            shadowForm.dateField.validate();
            shadowForm.familyMemberField.validate();
            shadowForm.providerField.validate();
            shadowForm.medicalExpenseInPlanField.validate();
            shadowForm.medicalExpenseTypeField.validate();
        } catch (Validator.InvalidValueException v) {
            Notification.show(v.getMessage(), Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean onBack() {
        return true;
    }

}