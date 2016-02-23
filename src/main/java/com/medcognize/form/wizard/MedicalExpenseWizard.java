package com.medcognize.form.wizard;

import com.medcognize.domain.MedicalExpense;
import com.medcognize.form.MedicalExpenseForm;
import com.medcognize.form.WizardForm;
import com.vaadin.data.util.BeanItem;
import org.vaadin.teemu.wizards.Wizard;

public class MedicalExpenseWizard extends WizardForm<MedicalExpense> {

    public MedicalExpenseWizard(BeanItem<MedicalExpense> bean, boolean isNew) {
        super(MedicalExpenseForm.class, bean, isNew);
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
    public void setupForm() {
        // nop - form setup is being done in the shadow form
    }
}
