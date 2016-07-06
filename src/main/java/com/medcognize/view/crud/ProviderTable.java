package com.medcognize.view.crud;

import com.medcognize.domain.Provider;
import com.medcognize.form.DisplayFriendlyForm;

import java.util.ArrayList;

/**
 * Created by jnash on 2/23/2016.
 */
public class ProviderTable extends CrudTable<Provider> {

    public ProviderTable(final Class<? extends DisplayFriendlyForm<Provider>> formClazz,
                     ArrayList<String> orderedPidList) {
        super(Provider.class, formClazz, orderedPidList);
    }

}
