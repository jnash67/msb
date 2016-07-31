package com.medcognize.form;

import com.medcognize.UserRepository;
import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.Provider;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.ContactInfo;
import com.medcognize.domain.validator.vaadin.ExistingProviderNameValidator;
import com.medcognize.form.field.DisplayFriendlySelectAndButton;
import com.medcognize.form.field.TypedSelectAndButton;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class ProviderForm extends DisplayFriendlyForm<Provider> {

    Field<?> providerName = createField("providerName");
    Field<?> providerInPlan = createField("providerInPlan");
    Field<?> providerType = createField("providerType");
    Field<?> providerId = createField("providerId");
    //Field<?> locations = new ElementCollectionField<ContactInfo>(ContactInfo.class, ContactInfoRow.class);
    Field<?> locations = new TypedSelectAndButton<ContactInfo>().withCaption("Addresses").withSelectType(ComboBox
            .class);

    protected final Provider item;

    public ProviderForm(Provider item, boolean isNew, UserRepository repo) {
        super(Provider.class, isNew, null, repo);
        this.item = item;
        setSizeUndefined();
        setEntity(item);
    }

    @Override
    protected Component createContent() {
        ((TypedSelectAndButton) locations).setBeans(item.getLocations());
        Button addNewContactInfoButton = new MButton(FontAwesome.PLUS, this::addNewContactInfo);
        addNewContactInfoButton.setDescription("Add new Address");
        ((TypedSelectAndButton) locations).setButton(addNewContactInfoButton);

        if (addingNewItem()) {
            providerName.addValidator(new ExistingProviderNameValidator((String) providerName.getValue()));
        } else {
            providerName.setEnabled(false);
        }
        return new MVerticalLayout(new MFormLayout(providerName, providerInPlan, providerType, providerId, locations)
                .withMargin(false), getToolbar());
    }

    private void addNewContactInfo(Button.ClickEvent e) {
        final ContactInfo ci = new ContactInfo();
        final AbstractForm<ContactInfo> form = new ContactInfoForm(ci, true, repo);
        form.setSavedHandler(new SavedHandler<ContactInfo>() {
            @Override
            public void onSave(ContactInfo entity) {
                // we don't save address to repo because the entity might not have been saved.
                // e.g. This might be an address being added to a brand new unsaved Provider.
                ((TypedSelect) locations).addOption(entity);
                // ((TypedSelect) locations).setValue(entity);
                form.closePopup();
            }
        });
        form.setResetHandler(new ResetHandler<ContactInfo>() {
            @Override
            public void onReset(ContactInfo entity) {
                form.closePopup();
            }
        });
        form.setModalWindowTitle("Add Contact Info");
        form.openInModalPopup();
    }
}
