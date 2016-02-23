
package com.medcognize.view.crud;

import com.medcognize.domain.basic.DisplayFriendly;
import com.vaadin.data.util.AbstractBeanContainer;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

public class DisplayFriendlyTable<T extends DisplayFriendly> extends Table {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayFriendlyTable.class);

    public class DisplayFriendlyBeanContainer<U extends DisplayFriendly> extends BeanContainer<Long, U> {
        public DisplayFriendlyBeanContainer(Class<U> type) {
            super(type);
            setBeanIdResolver(new AbstractBeanContainer.BeanIdResolver<Long, U>() {
                @Override
                public Long getIdForBean(U bean) {
                    return bean.getUniqueSessionId();
                }
            });
        }
        public void refreshItems() {
            fireItemSetChange();
        }
    }

    protected final DisplayFriendlyBeanContainer<T> data;
    protected final Class<T> entityClazz;

    public void setData(Collection<T> items) {
        this.getData().addAll(items);
    }

    public DisplayFriendlyTable(final Class<T> entityClazz, ArrayList<String> orderedPidList )   {
        super();
        this.entityClazz = entityClazz;
        data = new DisplayFriendlyBeanContainer<>(entityClazz);
        setContainerDataSource(data);
        setPropertyIds(orderedPidList);
    }

    public void setPropertyIds(ArrayList<String> orderedPidList) {
        final Collection<String> propertyIds;
        if (null == orderedPidList) {
            propertyIds = DisplayFriendly.propertyIdList(entityClazz);
        } else {
            propertyIds = orderedPidList;
        }

        String header;
        ArrayList<String> order = new ArrayList<>();
        for (String propertyId : propertyIds) {
            header = DisplayFriendly.getPropertyCaption(entityClazz, propertyId);
            setColumnHeader(propertyId, header);
            order.add(propertyId);
        }
        try {
            // this sets the order of columns
            setVisibleColumns(order.toArray());
        } catch (IllegalArgumentException iea) {
            LOGGER.warn("One of the property ids probably doesn't exist in the container: " + order);
        }
    }

    public  DisplayFriendlyBeanContainer<T> getData(){
        return data;
    }

    public void refreshItems() {
        data.refreshItems();
    }
}
