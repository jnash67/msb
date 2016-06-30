package com.medcognize.form;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.validator.vaadin.ExistingFamilyNameValidator;
import com.medcognize.form.field.ViritinFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;

public class FamilyMemberForm extends DisplayFriendlyForm<FamilyMember> {

    public FamilyMemberForm(BeanItem<FamilyMember> bean, boolean isNew) {
        super(bean, null, new ViritinFieldGroupFieldFactory(), isNew);
    }

    @Override
    public void setupForm() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        BeanFieldGroup<FamilyMember> group = this.getFieldGroup();

        Field<?> familyMemberName = group.getField("familyMemberName");
        addComponent(familyMemberName);
        if (isNew()) {
            familyMemberName.addValidator(new ExistingFamilyNameValidator(null));
        } else {
            familyMemberName.addValidator(new ExistingFamilyNameValidator((String) familyMemberName.getValue()));
        }
    }
}
