package com.medcognize.view.crud;

import com.medcognize.domain.Provider;
import com.medcognize.form.DisplayFriendlyForm;
import com.vaadin.data.util.BeanItem;

import java.util.ArrayList;

/**
 * Created by jnash on 2/23/2016.
 */
public class ProviderTable extends CrudTable<Provider> {

    public ProviderTable(final Class<? extends DisplayFriendlyForm<Provider>> formClazz,
                     ArrayList<String> orderedPidList) {
        super(Provider.class, formClazz, orderedPidList);
    }

    @Override
    protected void deleteAction(Object target) {
        BeanItem<Provider> bi = getContainer().getItem(target);
        removeItem(target);
        collectionOwner.getProviders().remove(bi.getBean());
        refreshItems();
    }

    @Override
    protected void saveItem(BeanItem<Provider> bi, boolean isNew) {
        collectionOwner.getProviders().add(bi.getBean());
    }
}
