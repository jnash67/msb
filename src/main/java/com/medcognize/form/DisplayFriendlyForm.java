package com.medcognize.form;


import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.field.ViritinFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class DisplayFriendlyForm<T extends DisplayFriendly> extends AbstractForm<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayFriendlyForm.class);

    // Bean validators are automatically created when using a BeanFieldGroup.
    // https://vaadin.com/book/vaadin7/-/page/datamodel.itembinding.html
    protected final Set<String> pidsToIgnore = new HashSet<>();
    protected final Collection<String> pidsToUse;
    protected final MFormLayout form;
    protected final MBeanFieldGroup<T> group;

    public DisplayFriendlyForm(T item, Collection<String> pids, FieldGroupFieldFactory factory) {
        setSizeUndefined();
        group = setEntity(item);
        @SuppressWarnings("unchecked") Class<T> clazz = (Class<T>) item.getClass();
        if (null == factory) {
            group.setFieldFactory(new ViritinFieldGroupFieldFactory());
        } else {
            group.setFieldFactory(factory);
        }
        form = new MFormLayout();

        String caption;
        if (null == pids) {
            pidsToUse = DisplayFriendly.propertyIdList(clazz);
        } else {
            pidsToUse = pids;
        }
        for (String pid : pidsToUse) {
            caption = DisplayFriendly.getPropertyCaption(clazz, pid);
            // Bean validators are automatically created when using a BeanFieldGroup.
            // https://vaadin.com/book/vaadin7/-/page/datamodel.itembinding.html
            group.buildAndBind(caption, pid);
        }

        // copy pids before the ones we set here to ignore
//        pidsToIgnore.add("id");
//        pidsToIgnore.add("LOGGER");
//        pidsToIgnore.add("uniqueSessionId");
    }

//    protected void validate() {
//        Set<String> remainder = new HashSet<>(pidsToIgnore);
//        for (Object propertyId : group.getUnboundPropertyIds()) {
//            if (!pidsToIgnore.contains(propertyId.toString())) {
//                LOGGER.warn("PropertyID " + propertyId + " not properly excluded from DisplayFriendlyForm");
//            } else {
//                //noinspection SuspiciousMethodCalls
//                remainder.remove(propertyId);
//            }
//        }
//        for (Object propertyId : group.getBoundPropertyIds()) {
//            if (!pidsToUse.contains(propertyId.toString())) {
//                LOGGER.warn("PropertyID " + propertyId + " not explicitly added to DisplayFriendlyForm");
//            }
//        }
//        if (remainder.size() > 0) {
//            LOGGER.warn("PropertyID(s) " + remainder + " removed but were not found");
//        }
//    }
}
