package com.medcognize.form;


import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.field.ViritinFieldGroupFieldFactory;
import com.vaadin.ui.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class DisplayFriendlyForm<T extends DisplayFriendly> extends AbstractForm<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayFriendlyForm.class);

    // Bean validators are automatically created when using a BeanFieldGroup.
    // https://vaadin.com/book/vaadin7/-/page/datamodel.itembinding.html
    protected final Set<String> pidsToIgnore = new HashSet<>();
    protected ArrayList<Field<?>> fields = new ArrayList<>();
    // protected final Collection<String> pidsToUse;
    protected final MFormLayout form;

    protected DisplayFriendlyForm(T item, Collection<String> pids, ViritinFieldGroupFieldFactory factory) {
        setSizeUndefined();
        setEntity(item);
        @SuppressWarnings("unchecked") Class<T> clazz = (Class<T>) item.getClass();
        if (null == factory) {
            getFieldGroup().setFieldFactory(new ViritinFieldGroupFieldFactory());
        } else {
            getFieldGroup().setFieldFactory(factory);
        }
        form = new MFormLayout();

//        String caption;
//        if (null == pids) {
//            pidsToUse = DisplayFriendly.propertyIdList(clazz);
//        } else {
//            pidsToUse = pids;
//        }
//        for (String pid : pidsToUse) {
//            caption = DisplayFriendly.getPropertyCaption(clazz, pid);
//            // Bean validators are automatically created when using a BeanFieldGroup.
//            // https://vaadin.com/book/vaadin7/-/page/datamodel.itembinding.html
//            fields.add(group.buildAndBind(caption, pid));
//        }

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
