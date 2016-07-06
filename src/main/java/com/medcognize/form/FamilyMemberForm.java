package com.medcognize.form;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.validator.vaadin.ExistingFamilyNameValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class FamilyMemberForm extends AbstractForm<FamilyMember> {

    TextField familyMemberName = new MTextField(getCaption("familyMemberName"));

    @Override
    protected Component createContent() {
        familyMemberName.addValidator(new ExistingFamilyNameValidator((String) familyMemberName.getValue()));
        return new MVerticalLayout(
                getToolbar(),
                new FormLayout(familyMemberName));
    }

    private String getCaption(String propName) {
        return DisplayFriendly.getPropertyCaption(FamilyMember.class, propName);
    }

}
