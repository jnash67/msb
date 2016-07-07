package com.medcognize.form;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.validator.vaadin.ExistingFamilyNameValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collection;

public class FamilyMemberForm extends DisplayFriendlyForm<FamilyMember> {

    Field<?> familyMemberName;

    public FamilyMemberForm(FamilyMember item) {
        super(item, null, null);
    }

    public FamilyMemberForm(FamilyMember item, Collection<String> pids) {
        super(item, pids, null);
    }

    @Override
    protected Component createContent() {
        familyMemberName = group.getField("familyMemberName");
        familyMemberName.addValidator(new ExistingFamilyNameValidator((String) familyMemberName.getValue()));
        return new MVerticalLayout(
                getToolbar(),
                new FormLayout(familyMemberName));
    }
}
