package com.medcognize.view.crud;

import com.medcognize.domain.Fsa;
import com.medcognize.form.DisplayFriendlyForm;

import java.util.ArrayList;

/**
 * Created by jnash on 2/23/2016.
 */
public class FsaTable extends CrudTable<Fsa> {

    public FsaTable(final Class<? extends DisplayFriendlyForm<Fsa>> formClazz,
                         ArrayList<String> orderedPidList) {
        super(Fsa.class, formClazz, orderedPidList);
    }
}
