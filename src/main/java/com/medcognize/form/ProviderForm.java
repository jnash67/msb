package com.medcognize.form;

import com.medcognize.domain.Provider;
import com.medcognize.domain.basic.ContactInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class ProviderForm extends DisplayFriendlyForm<Provider> {

    public static class ContactInfoRow {
        MTextField address1 = new MTextField().withInputPrompt("address 1");
        MTextField address2 = new MTextField().withInputPrompt("address 2");
        MTextField city = new MTextField().withInputPrompt("city");
        MTextField state = new MTextField().withInputPrompt("state");
        MTextField zip = new MTextField().withInputPrompt("zip");
        MTextField phoneNumber = new MTextField().withInputPrompt("phone number");
        MTextField website = new MTextField().withInputPrompt("URL");
    }

    Field<?> providerName = createField("providerName");
    Field<?> providerInPlan = createField("providerInPlan");
    Field<?> providerType = createField("providerType");
    Field<?> providerId = createField("providerId");
    Field<?> locations = new ElementCollectionField<ContactInfo>(ContactInfo.class, ContactInfoRow.class);

    public ProviderForm(Provider item) {
        super(Provider.class, null);
        setSizeUndefined();
        setEntity(item);
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(new MFormLayout(providerName, providerInPlan, providerType, providerId, locations).withWidth(""), getToolbar()).withWidth("");
    }
}
