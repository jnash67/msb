package com.medcognize.form.field;

import com.medcognize.domain.basic.Address;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.view.ComponentWindow;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MFormLayout;

import java.util.Collection;

public abstract class AddressField extends CustomField<Address> {

    private final BeanFieldGroup<Address> fieldGroup = new BeanFieldGroup<>(Address.class);
    private Button editAddressButton = new Button("Edit Address");

    public AddressField(final Address address) {
        super();
        fieldGroup.setBuffered(true);
        fieldGroup.setItemDataSource(address);
        this.fieldGroup.setFieldFactory(new ViritinFieldGroupFieldFactory());
        Collection<String> pids = DisplayFriendly.propertyIdList(Address.class);
        String caption;
        for (String pid : pids) {
            caption = DisplayFriendly.getPropertyCaption(Address.class, pid);
            this.fieldGroup.buildAndBind(caption, pid);
        }
    }

    public Address getAddress() {
        return fieldGroup.getItemDataSource().getBean();
    }

    @Override
    protected Component initContent() {
        Field<?> address1Field = fieldGroup.getField("address1");
        Field<?> address2Field = fieldGroup.getField("address2");
        Field<?> cityField = fieldGroup.getField("city");
        Field<?> stateField = fieldGroup.getField("state");
        Field<?> zipField = fieldGroup.getField("zip");
        Field<?> phoneNumberField = fieldGroup.getField("phoneNumber");

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        MFormLayout fieldsLayout = new MFormLayout(address1Field, address2Field, cityField, stateField, zipField, phoneNumberField);
        layout.addComponent(fieldsLayout);

        final ComponentWindow window = new ComponentWindow("Edit Address", false, false);
        editAddressButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().addWindow(window);
            }
        });

        window.setContent(layout);
        window.addClickListenerToSubmitButton(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    fieldGroup.commit();
                    editAddressButton.removeStyleName("errorstyle");
                    successfulCommitAction();
                    window.close();
                } catch (Validator.InvalidValueException | FieldGroup.CommitException ignored) {
                    editAddressButton.addStyleName("errorstyle");
                }
            }
        });
        window.center();
        window.setWidth(null);
        layout.setWidth(null);
        layout.setMargin(true);
        return editAddressButton;
    }

    public abstract void successfulCommitAction();

    @Override
    public Class<? extends Address> getType() {
        return Address.class;
    }

//    @Override
//    protected void setInternalValue(Address address) {
//        if (null == address) {
//            address = new Address();
//        }
//        super.setInternalValue(address);
//        group.setItemDataSource(new BeanItem<>(address));
//    }
}
