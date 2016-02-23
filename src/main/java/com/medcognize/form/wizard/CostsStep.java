package com.medcognize.form.wizard;

import com.medcognize.domain.validator.vaadin.LessThanOrEqualDoubleValidator;
import com.medcognize.form.MedicalExpenseForm;
import com.medcognize.form.field.YesNoSelect;
import com.medcognize.form.field.errorful.ErrorfulFormLayout;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.wizards.WizardStep;

public class CostsStep implements WizardStep {

    final MedicalExpenseForm shadowForm;
    private LessThanOrEqualDoubleValidator coPayValidator;

    public CostsStep(MedicalExpenseForm shadowForm) {
        this.shadowForm = shadowForm;
    }

    @Override
    public String getCaption() {
        return "Costs";
    }

    @Override
    public Component getContent() {
        VerticalLayout vl = new VerticalLayout();
        vl.setSizeUndefined();
        ErrorfulFormLayout content = new ErrorfulFormLayout();
        content.setSizeUndefined();

        shadowForm.outOfPocketAmountField.setCaption("How much have you paid out of pocket so far?");
        final YesNoSelect coPayField = new YesNoSelect("Did you pay a co-pay?", null);
        coPayValidator = new LessThanOrEqualDoubleValidator("The copay must be less than or equal to the out of pocket amount",
                shadowForm.outOfPocketAmountField, true);
        if ((Double) shadowForm.copayAmountField.getPropertyDataSource().getValue() > 0) {
            coPayField.setValue(true);
        } else {
            coPayField.setValue(false);
        }
        showCopay(coPayField.getValue());
        coPayField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                showCopay(coPayField.getValue());
            }
        });
        // get rid of the already existing description because we have a new one from the validator
        ((AbstractField) shadowForm.copayAmountField).setDescription(null);
        shadowForm.copayAmountField.setCaption("How much was the copay?");
        shadowForm.costAccordingToProviderField.setCaption("How much did the provider charge?");
        String eobText = "<span style=\"font-weight: bold;\">For each medical expense</span><br\n" +
                "style=\"font-weight: bold;\" />\n" +
                "<span style=\"font-weight: bold;\">the insurance company\n" +
                "</span><br style=\"font-weight: bold;\" />\n" +
                "<span style=\"font-weight: bold;\">will send you an\n" +
                "EOB</span><br style=\"font-weight: bold;\" />\n" +
                "<span style=\"font-weight: bold;\">(Explanation of Benefits)</span><br\n" +
                "style=\"font-weight: bold;\" />\n" +
                "<span style=\"font-weight: bold;\">";
        Label noteLabel = new Label(eobText, ContentMode.HTML);
        noteLabel.setDescription("You can also get this information online");
        final YesNoSelect eobField = new YesNoSelect("Do you have the EOB information?", null);
        shadowForm.maximumAmountField.setCaption("Maximum Amount on the EOB?");
        shadowForm.deductibleAmountField.setCaption("Deductible Amount on the EOB?");
        shadowForm.paymentAmountField.setCaption("Payment Amount on the EOB?");
        if (((Double) shadowForm.maximumAmountField.getPropertyDataSource().getValue() > 0) ||
                ((Double) shadowForm.deductibleAmountField.getPropertyDataSource().getValue() > 0) ||
                ((Double) shadowForm.paymentAmountField.getPropertyDataSource().getValue() > 0)) {
            eobField.setValue(true);
        } else {
            eobField.setValue(false);
        }
        showEobFields(eobField.getValue());
        eobField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                showEobFields(eobField.getValue());
            }
        });
        content.addComponent(shadowForm.outOfPocketAmountField);
        content.addComponent(coPayField);
        content.addComponent(shadowForm.copayAmountField);
        content.addComponent(shadowForm.costAccordingToProviderField);
        content.addComponent(noteLabel);
        content.addComponent(eobField);
        content.addComponent(shadowForm.maximumAmountField);
        content.addComponent(shadowForm.deductibleAmountField);
        content.addComponent(shadowForm.paymentAmountField);
        return content;
    }

    private void showCopay(boolean b) {
        if (b) {
            shadowForm.copayAmountField.setVisible(true);
            shadowForm.copayAmountField.addValidator(coPayValidator);
        } else {
            shadowForm.copayAmountField.setVisible(false);
            if (!shadowForm.copayAmountField.isValid()) {
                shadowForm.copayAmountField.discard();
            }
            shadowForm.copayAmountField.removeValidator(coPayValidator);
        }
    }

    private void showEobFields(boolean b){
        if (b) {
            shadowForm.maximumAmountField.setVisible(true);
            shadowForm.deductibleAmountField.setVisible(true);
            shadowForm.paymentAmountField.setVisible(true);
        } else {
            shadowForm.maximumAmountField.setVisible(false);
            shadowForm.deductibleAmountField.setVisible(false);
            shadowForm.paymentAmountField.setVisible(false);
            if (!shadowForm.maximumAmountField.isValid()) {
                shadowForm.maximumAmountField.discard();
            }
            if (!shadowForm.deductibleAmountField.isValid()) {
                shadowForm.deductibleAmountField.discard();
            }
            if (!shadowForm.paymentAmountField.isValid()) {
                shadowForm.paymentAmountField.discard();
            }
        }
    }

    @Override
    public boolean onAdvance() {
        try {
            shadowForm.outOfPocketAmountField.validate();
        } catch (Validator.InvalidValueException v) {
            Notification.show(v.getMessage(), Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public boolean onBack() {
        return true;
    }
}
