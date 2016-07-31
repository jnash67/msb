package com.medcognize.form;

import com.medcognize.UserRepository;
import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.basic.ContactInfo;
import com.medcognize.domain.validator.vaadin.ExistingFamilyNameValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class ContactInfoForm extends DisplayFriendlyForm<ContactInfo> {

    Field<?> address1 = createField("address1");
    Field<?> address2 = createField("address2");
    Field<?> city = createField("city");
    Field<?> state = createField("state");
    Field<?> zip = createField("zip");
    Field<?> phoneNumber = createField("phoneNumber");
    Field<?> website = createField("website");

    public ContactInfoForm(ContactInfo item, boolean isNew, UserRepository repo) {
        super(ContactInfo.class, isNew, null, repo);
        setSizeUndefined();
        setEntity(item);
    }

    @Override
    protected Component createContent() {
        ((MTextField)address1).withInputPrompt("address 1");
        ((MTextField)address2).withInputPrompt("address 2");
        ((MTextField)city).withInputPrompt("city");
        ((MTextField)state).withInputPrompt("state");
        ((MTextField)zip).withInputPrompt("zip");
        ((MTextField)phoneNumber).withInputPrompt("phone number");
        ((MTextField)website).withInputPrompt("URL");
        return new MVerticalLayout(new MFormLayout(address1, address2, city, state, zip, phoneNumber, website)
                .withMargin(false), getToolbar());
    }

}
