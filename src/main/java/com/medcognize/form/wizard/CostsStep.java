package com.medcognize.form.wizard;

import com.medcognize.domain.validator.vaadin.LessThanOrEqualDoubleValidator;
import com.medcognize.form.MedicalExpenseForm;
import com.medcognize.form.field.YesNoSelect;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.viritin.layouts.MFormLayout;

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
        MFormLayout content = new MFormLayout();
        content.setSizeUndefined();

        shadowForm.outOfPocketAmount.setCaption("How much have you paid out of pocket so far?");
        final YesNoSelect coPay = new YesNoSelect("Did you pay a co-pay?", null);
        coPayValidator = new LessThanOrEqualDoubleValidator("The copay must be less than or equal to the out of pocket amount",
                shadowForm.outOfPocketAmount, true);
        if ((Double) shadowForm.copayAmount.getPropertyDataSource().getValue() > 0) {
            coPay.setValue(true);
        } else {
            coPay.setValue(false);
        }
        showCopay(coPay.getValue());
        coPay.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                showCopay(coPay.getValue());
            }
        });
        // get rid of the already existing description because we have a new one from the validator
        ((AbstractField) shadowForm.copayAmount).setDescription(null);
        shadowForm.copayAmount.setCaption("How much was the copay?");
        shadowForm.costAccordingToProvider.setCaption("How much did the provider charge?");
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
        final YesNoSelect eob = new YesNoSelect("Do you have the EOB information?", null);
        shadowForm.maximumAmount.setCaption("Maximum Amount on the EOB?");
        shadowForm.deductibleAmount.setCaption("Deductible Amount on the EOB?");
        shadowForm.paymentAmount.setCaption("Payment Amount on the EOB?");
        if (((Double) shadowForm.maximumAmount.getPropertyDataSource().getValue() > 0) ||
                ((Double) shadowForm.deductibleAmount.getPropertyDataSource().getValue() > 0) ||
                ((Double) shadowForm.paymentAmount.getPropertyDataSource().getValue() > 0)) {
            eob.setValue(true);
        } else {
            eob.setValue(false);
        }
        showEobFields(eob.getValue());
        eob.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                showEobFields(eob.getValue());
            }
        });
        content.addComponent(shadowForm.outOfPocketAmount);
        content.addComponent(coPay);
        content.addComponent(shadowForm.copayAmount);
        content.addComponent(shadowForm.costAccordingToProvider);
        content.addComponent(noteLabel);
        content.addComponent(eob);
        content.addComponent(shadowForm.maximumAmount);
        content.addComponent(shadowForm.deductibleAmount);
        content.addComponent(shadowForm.paymentAmount);
        return content;
    }

    private void showCopay(boolean b) {
        if (b) {
            shadowForm.copayAmount.setVisible(true);
            shadowForm.copayAmount.addValidator(coPayValidator);
        } else {
            shadowForm.copayAmount.setVisible(false);
            if (!shadowForm.copayAmount.isValid()) {
                shadowForm.copayAmount.discard();
            }
            shadowForm.copayAmount.removeValidator(coPayValidator);
        }
    }

    private void showEobFields(boolean b){
        if (b) {
            shadowForm.maximumAmount.setVisible(true);
            shadowForm.deductibleAmount.setVisible(true);
            shadowForm.paymentAmount.setVisible(true);
        } else {
            shadowForm.maximumAmount.setVisible(false);
            shadowForm.deductibleAmount.setVisible(false);
            shadowForm.paymentAmount.setVisible(false);
            if (!shadowForm.maximumAmount.isValid()) {
                shadowForm.maximumAmount.discard();
            }
            if (!shadowForm.deductibleAmount.isValid()) {
                shadowForm.deductibleAmount.discard();
            }
            if (!shadowForm.paymentAmount.isValid()) {
                shadowForm.paymentAmount.discard();
            }
        }
    }

    @Override
    public boolean onAdvance() {
        try {
            shadowForm.outOfPocketAmount.validate();
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
