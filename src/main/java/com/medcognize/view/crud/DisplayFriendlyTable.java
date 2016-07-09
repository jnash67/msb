
package com.medcognize.view.crud;

import com.medcognize.domain.basic.DisplayFriendly;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.fields.FilterableTable;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class DisplayFriendlyTable<T extends DisplayFriendly> extends FilterableTable<T> {

    protected final Class<T> entityClazz;

    public DisplayFriendlyTable(final Class<T> entityClazz, ArrayList<String> orderedPidList) {
        super(entityClazz);
        this.entityClazz = entityClazz;

        final Collection<String> propertyIds;
        if (null == orderedPidList) {
            propertyIds = DisplayFriendly.propertyIdList(entityClazz);
        } else {
            propertyIds = orderedPidList;
        }

        ArrayList<String> headers = new ArrayList<>();
        for (String propertyId : propertyIds) {
            headers.add(DisplayFriendly.getFriendlyPropertyName(entityClazz, propertyId));
        }

        if (propertyIds.size() > 0) {
            withProperties(propertyIds.toArray(new String[propertyIds.size()]));
            withColumnHeaders(headers.toArray(new String[headers.size()]));
        }
    }
}
