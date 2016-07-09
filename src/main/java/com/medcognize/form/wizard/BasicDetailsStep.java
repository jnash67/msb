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

        shadowForm.date.setCaption("What is the date of the medical expense:");
        shadowForm.date.setWidth("75px");
        shadowForm.familyMember.setCaption("Who is the expense for:");
        shadowForm.provider.setCaption("Which medical provider is the expense with:");
        shadowForm.medicalExpenseInPlan.setCaption("Is this expense in plan?");
        YesNoSelect shadow = new YesNoSelect("Is this expense in plan?", (com.vaadin.ui.CheckBox) shadowForm.medicalExpenseInPlan);
        shadowForm.medicalExpenseType.setCaption("What sort of expense is this?");

        content.addComponent(shadowForm.date);
        content.addComponent(shadowForm.familyMember);
        content.addComponent(shadowForm.provider);
        content.addComponent(shadow);
        content.addComponent(shadowForm.medicalExpenseType);
        content.addComponent(shadowForm.prescriptionTierType);
        if (shadowForm.medicalExpenseType.getPropertyDataSource().getValue().toString().equals(DisplayFriendly.getEnumCaption(MedicalExpense
                .MedicalExpenseType.PRESCRIPTION))) {
            shadowForm.prescriptionTierType.setVisible(true);
        } else {
            shadowForm.prescriptionTierType.setVisible(false);
        }
        shadowForm.medicalExpenseType.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue().toString().equals(DisplayFriendly.getEnumCaption(MedicalExpense
                        .MedicalExpenseType.PRESCRIPTION))) {
                    shadowForm.prescriptionTierType.setVisible(true);
                } else {
                    shadowForm.prescriptionTierType.setVisible(false);
                    if (!shadowForm.prescriptionTierType.isValid()) {
                        shadowForm.prescriptionTierType.discard();
                    }
                }
            }
        });
        return content;
    }

    public boolean onAdvance() {
        try {
            shadowForm.date.validate();
            shadowForm.familyMember.validate();
            shadowForm.provider.validate();
            shadowForm.medicalExpenseInPlan.validate();
            shadowForm.medicalExpenseType.validate();
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