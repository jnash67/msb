package com.medcognize.form;

import com.medcognize.domain.Provider;
import com.medcognize.domain.basic.Address;
import com.medcognize.domain.validator.vaadin.ExistingProviderNameValidator;
import com.medcognize.form.field.AddressField;
import com.medcognize.form.field.ViritinFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;

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

    public ProviderForm(BeanItem<Provider> bean, boolean isNew) {
        super(bean, null, new ViritinFieldGroupFieldFactory(), isNew);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setupForm() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        BeanFieldGroup<Provider> group = this.getFieldGroup();

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
        addressField = new AddressField(address, isNew()) {
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
        addComponent(providerName);
        if (isNew()) {
            providerName.addValidator(new ExistingProviderNameValidator(null));
        } else {
            providerName.addValidator(new ExistingProviderNameValidator((String) providerName.getValue()));
        }
        addComponent(providerInPlan);
        addComponent(providerType);
        addComponent(providerId);
        addComponent(addressField);
        addComponent(website);
    }

    @Override
    public void commit() throws FieldGroup.CommitException {
        super.commit();
    }



}
