
package com.medcognize.view.crud;

import com.medcognize.UserRepository;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.fields.FilterableTable;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class DisplayFriendlyTable<T extends DisplayFriendly> extends FilterableTable<T> {

    protected final Class<T> entityClazz;
    protected final UserRepository repo;
    protected User collectionOwner;

    public DisplayFriendlyTable(final UserRepository repo, final Class<T> entityClazz, ArrayList<String> orderedPidList) {
        super(entityClazz);
        this.entityClazz = entityClazz;
        this.repo = repo;

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

    @Override
    public void refreshRows() {
        if (null != collectionOwner) {
            setBeans(UserUtil.getAll(repo, collectionOwner, entityClazz));
        }
        super.refreshRows();
    }

    public void setData(UserRepository repo, User collectionOwner) {
        this.collectionOwner = collectionOwner;
        setBeans(UserUtil.getAll(repo, collectionOwner, entityClazz));
    }
}
