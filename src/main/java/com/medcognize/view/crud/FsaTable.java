package com.medcognize.view.crud;

import com.medcognize.domain.Fsa;
import com.medcognize.form.DisplayFriendlyForm;
import com.vaadin.data.util.BeanItem;

import java.util.ArrayList;

/**
 * Created by jnash on 2/23/2016.
 */
public class FsaTable extends CrudTable<Fsa> {

    public FsaTable(final Class<? extends DisplayFriendlyForm<Fsa>> formClazz,
                         ArrayList<String> orderedPidList) {
        super(Fsa.class, formClazz, orderedPidList);
    }

    @Override
    protected void deleteAction(Object target) {
        BeanItem<Fsa> bi = getContainer().getItem(target);
        removeItem(target);
        collectionOwner.getFsas().remove(bi.getBean());
        refreshItems();
    }

    @Override
    protected void saveItem(BeanItem<Fsa> bi, boolean isNew) {
        collectionOwner.getFsas().add(bi.getBean());
    }
}
