package com.medcognize.form;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.validator.vaadin.ExistingFamilyNameValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class FamilyMemberForm extends DisplayFriendlyForm<FamilyMember> {

    Field<?> familyMemberNameField = createField("familyMemberName");

    public FamilyMemberForm(FamilyMember item) {
        setSizeUndefined();
        setEntity(item);
    }

    @Override
    protected Component createContent() {
        familyMemberNameField.addValidator(new ExistingFamilyNameValidator((String) familyMemberNameField.getValue()));
        return new MVerticalLayout(new MFormLayout(familyMemberNameField), getToolbar());
    }


}
