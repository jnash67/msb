package com.medcognize.view.crud;

import com.medcognize.domain.FamilyMember;
import com.medcognize.form.DisplayFriendlyForm;
import com.vaadin.data.util.BeanItem;

import java.util.ArrayList;

/**
 * Created by jnash on 2/23/2016.
 */
public class FamilyMemberTable extends CrudTable<FamilyMember> {

    public FamilyMemberTable(final Class<? extends DisplayFriendlyForm<FamilyMember>> formClazz,
                         ArrayList<String> orderedPidList) {
        super(FamilyMember.class, formClazz, orderedPidList);
    }

    @Override
    protected void deleteAction(Object target) {
        BeanItem<FamilyMember> bi = getContainer().getItem(target);
        removeItem(target);
        collectionOwner.getFamilyMembers().remove(bi.getBean());
        refreshItems();
    }

    @Override
    protected void saveItem(BeanItem<FamilyMember> bi, boolean isNew) {
        collectionOwner.getFamilyMembers().add(bi.getBean());
    }
}
