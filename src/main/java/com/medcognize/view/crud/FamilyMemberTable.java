package com.medcognize.view.crud;

import com.medcognize.domain.FamilyMember;
import com.medcognize.form.DisplayFriendlyForm;

import java.util.ArrayList;

/**
 * Created by jnash on 2/23/2016.
 */
public class FamilyMemberTable extends CrudTable<FamilyMember> {

    public FamilyMemberTable(final Class<? extends DisplayFriendlyForm<FamilyMember>> formClazz,
                         ArrayList<String> orderedPidList) {
        super(FamilyMember.class, formClazz, orderedPidList);
    }
}
