package com.medcognize.form.wizard;

import com.medcognize.domain.MedicalExpense;
import com.medcognize.form.MedicalExpenseForm;
import com.medcognize.form.WizardForm;
import com.vaadin.ui.Component;
import org.vaadin.teemu.wizards.Wizard;

public class MedicalExpenseWizard extends WizardForm<MedicalExpense> {

    public MedicalExpenseWizard(MedicalExpense me, boolean isNew) {
        super(MedicalExpenseForm.class, me);
    }

    @Override
    public Wizard createWizard() {
        Wizard w = new Wizard();
        w.addStep(new BasicDetailsStep((MedicalExpenseForm) this.shadowForm), "basic");
        w.addStep(new CostsStep((MedicalExpenseForm) this.shadowForm), "costs");
        w.setWidth("500px");
        return w;
    }

    @Override
    protected Component createContent() {
        // nop - form setup is being done in the shadow form
        validate();
        return null;
    }

}
