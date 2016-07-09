package com.medcognize.form;

import com.medcognize.domain.Provider;
import com.medcognize.domain.basic.ContactInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.vaadin.viritin.fields.ElementCollectionTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collection;

;

public class ProviderForm extends DisplayFriendlyForm<Provider> {

    public static class ContactInfoRow {
        MTextField address1 = new MTextField().withInputPrompt("Address1");
        MTextField address2 = new MTextField().withInputPrompt("Address1");
        MTextField city = new MTextField().withInputPrompt("city");
        MTextField state = new MTextField().withInputPrompt("state");
        MTextField zip = new MTextField().withInputPrompt("zip");
        MTextField phoneNumber = new MTextField().withInputPrompt("phone number");
        MTextField website = new MTextField().withInputPrompt("URL");
    }

    Field<?> providerName = createField("providerName");
    Field<?> providerInPlan = createField("providerName");
    Field<?> providerType = createField("providerName");
    Field<?> providerId = createField("providerName");
    Field<?> locations = new ElementCollectionTable<ContactInfo>(ContactInfo.class, ContactInfoRow.class);
    Field<?> website = createField("providerName");

    public ProviderForm(Provider item) {
        super(Provider.class, null);
        setSizeUndefined();
        setEntity(item);
    }

    public ProviderForm(Provider item, Collection<String> pids) {
        super(Provider.class, null);
        setSizeUndefined();
        setEntity(item);
    }

    @Override
    protected Component createContent() {
        MFormLayout form = new MFormLayout();
        form.addComponent(providerName);
        form.addComponent(providerInPlan);
        form.addComponent(providerType);
        form.addComponent(providerId);
        form.addComponent(locations);
        form.addComponent(website);

        return new MVerticalLayout(form.withWidth(""), getToolbar()).withWidth("");
    }
}
