package com.medcognize.form;

import com.medcognize.domain.Provider;
import com.medcognize.domain.basic.Address;
import com.medcognize.form.field.AddressField;
import com.medcognize.form.field.ViritinFieldGroupFieldFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.vaadin.viritin.layouts.MVerticalLayout;

;

public class ProviderForm extends DisplayFriendlyForm<Provider> {

    AddressField addressField;
    Field<?> providerName;
    Field<?> providerInPlan;
    Field<?> providerType;
    Field<?> providerId;
    Field<String> address1;
    Field<String> address2;
    Field<String> city;
    Field<String> state;
    Field<String> zip;
    Field<String> phoneNumber;
    Field<?> website;

    public ProviderForm(Provider pr) {
        super(pr, null, new ViritinFieldGroupFieldFactory());
    }

    @Override
    protected Component createContent() {
        validate();

        providerName = group.getField("providerName");
        providerInPlan = group.getField("providerInPlan");
        providerType = group.getField("providerType");
        providerId = group.getField("providerId");

        address1 = (Field<String>) group.getField("address1");
        address2 = (Field<String>) group.getField("address2");
        city = (Field<String>) group.getField("city");
        state = (Field<String>) group.getField("state");
        zip = (Field<String>) group.getField("zip");
        phoneNumber = (Field<String>) group.getField("phoneNumber");
        Address address = new Address();
        address.setAddress1(address1.getValue());
        address.setAddress2(address2.getValue());
        address.setCity(city.getValue());
        address.setState(state.getValue());
        address.setZip(zip.getValue());
        addressField = new AddressField(address) {
            @SuppressWarnings("unchecked")
            @Override
            public void successfulCommitAction() {
                Address changedAddress = addressField.getAddress();
                address1.setValue(changedAddress.getAddress1());
                address2.setValue(changedAddress.getAddress2());
                city.setValue(changedAddress.getCity());
                state.setValue(changedAddress.getState());
                zip.setValue(changedAddress.getZip());
                phoneNumber.setValue(changedAddress.getPhoneNumber());
            }
        };
        website = group.getField("website");
        form.addComponent(providerName);
        form.addComponent(providerInPlan);
        form.addComponent(providerType);
        form.addComponent(providerId);
        form.addComponent(addressField);
        form.addComponent(website);

        return new MVerticalLayout(form.withWidth(""), getToolbar()).withWidth("");
    }
}
