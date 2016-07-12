package com.medcognize.form;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.validator.vaadin.ExistingFamilyNameValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class FamilyMemberForm extends DisplayFriendlyForm<FamilyMember> {

    Field<?> familyMemberName = createField("familyMemberName");

    public FamilyMemberForm(FamilyMember item, boolean isNew) {
        super(FamilyMember.class, isNew, null);
        setSizeUndefined();
        setEntity(item);
        if (addingNewItem()) {
            familyMemberName.addValidator(new ExistingFamilyNameValidator((String) familyMemberName.getValue()));
        } else {
            familyMemberName.setEnabled(false);
        }
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(new MFormLayout(familyMemberName), getToolbar());
    }


}
