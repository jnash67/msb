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
        int index = getContainer().indexOfId(target);
        Fsa fsa = getContainer().getIdByIndex(index);
        removeItem(target);
        collectionOwner.getFsas().remove(fsa);
        refreshRows();
    }

    @Override
    protected void saveItem(BeanItem<Fsa> bi, boolean isNew) {
        collectionOwner.getFsas().add(bi.getBean());
    }
}
