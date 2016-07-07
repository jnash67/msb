package com.medcognize.view.crud;

import com.medcognize.domain.Provider;
import com.medcognize.form.DisplayFriendlyForm;

import java.util.ArrayList;

public class ProviderTable extends CrudTable<Provider> {

    public ProviderTable(final Class<? extends DisplayFriendlyForm<Provider>> formClazz,
                     ArrayList<String> orderedPidList) {
        super(Provider.class, formClazz, orderedPidList);
    }

}
