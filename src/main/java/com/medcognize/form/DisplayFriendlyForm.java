package com.medcognize.form;


import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.field.ViritinFieldGroupFieldFactory;
import com.medcognize.form.field.errorful.ErrorfulVerticalLayout;
import com.medcognize.util.ErrorfulUtil;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class DisplayFriendlyForm<T extends DisplayFriendly> extends ErrorfulVerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayFriendlyForm.class);

    // Bean validators are automatically created when using a BeanFieldGroup.
    // https://vaadin.com/book/vaadin7/-/page/datamodel.itembinding.html
    private final BeanFieldGroup<T> binder;
    private final BeanItem<T> bi;
    protected final Set<String> pidsToIgnore = new HashSet<>();
    private final boolean isNew;

    protected DisplayFriendlyForm(BeanItem<T> bi, Collection<String> pids, ViritinFieldGroupFieldFactory factory,
                                  boolean isNew) {
        super();
        this.isNew = isNew;
        if (null != bi) {
            @SuppressWarnings("unchecked") Class<T> clazz = (Class<T>) bi.getBean().getClass();
            this.bi = bi;
            this.binder = new BeanFieldGroup<>(clazz);
            this.binder.setItemDataSource(bi);
            if (null == factory) {
                this.binder.setFieldFactory(new ViritinFieldGroupFieldFactory());
            } else {
                this.binder.setFieldFactory(factory);
            }

            String caption;
            Collection<String> pidsToUse;
            if (null == pids) {
                pidsToUse = DisplayFriendly.propertyIdList(clazz);
            } else {
                pidsToUse = pids;
            }
            for (String pid : pidsToUse) {
                caption = DisplayFriendly.getPropertyCaption(clazz, pid);
                // Bean validators are automatically created when using a BeanFieldGroup.
                // https://vaadin.com/book/vaadin7/-/page/datamodel.itembinding.html
                this.binder.buildAndBind(caption, pid);
            }
            setupForm();
            // copy pids before the ones we set here to ignore
            Set<String> remainder = new HashSet<>(pidsToIgnore);
            pidsToIgnore.add("id");
            pidsToIgnore.add("LOGGER");
            pidsToIgnore.add("uniqueSessionId");
            for (Object propertyId : this.binder.getUnboundPropertyIds()) {
                // Since DisplayFriendly implements item, the getItemPropertyIds method causes an itemPropertyIds
                // property to show up here that we can ignore
                if (!pidsToIgnore.contains(propertyId.toString())) {
                    LOGGER.warn("PropertyID " + propertyId + " not properly excluded from DisplayFriendlyForm");
                } else {
                    //noinspection SuspiciousMethodCalls
                    remainder.remove(propertyId);
                }
            }
            for (Object propertyId : this.binder.getBoundPropertyIds()) {
                if (!pidsToUse.contains(propertyId.toString())) {
                    LOGGER.warn("PropertyID " + propertyId + " not explicitly added to DisplayFriendlyForm");
                }
            }
            if (remainder.size() > 0) {
                LOGGER.warn("PropertyID(s) " + remainder + " removed but were not found");
            }
            if (!isNew) {
                //noinspection ThrowableResultOfMethodCallIgnored
                ErrorfulUtil.highlightInvalidFields(this.binder.getFields(), this);
            }
        } else {
            //noinspection AssignmentToNull
            this.binder = null;
            //noinspection AssignmentToNull
            this.bi = null;
        }
    }

    public abstract void setupForm();

    public BeanFieldGroup<T> getFieldGroup() {
        return binder;
    }

    public BeanItem<T> getBeanItem() {
        return bi;
    }

    public void commit() throws FieldGroup.CommitException {
        highlightInvalidFields();
        binder.commit();
    }

    public boolean isNew() {
        return isNew;
    }
}
