package com.medcognize.form;


import com.medcognize.UserRepository;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.field.ViritinFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Field;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.form.AbstractForm;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

@SpringComponent
@Slf4j
public abstract class DisplayFriendlyForm<T extends DisplayFriendly> extends AbstractForm<T> {

    @Autowired
    protected UserRepository repo;

    protected final Class<T> clazz;
    // Bean validators are automatically created when using a BeanFieldGroup.
    // https://vaadin.com/book/vaadin7/-/page/datamodel.itembinding.html
//    protected final Set<String> pidsToIgnore = new HashSet<>();
//    protected final Collection<String> pidsToUse;
//    protected final MFormLayout form;
//    protected final MBeanFieldGroup<T> group;

    protected final FieldGroupFieldFactory f;

    public Field createField(String propertyId) {
        return createField(propertyId, f);
    }

    public Field createField(String propertyId, FieldGroupFieldFactory factory) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(propertyId, clazz);
            Class<?> type = pd.getPropertyType();
            Field f = factory.createField(type, null);
            String fn = DisplayFriendly.getFriendlyPropertyName(clazz, propertyId);
            f.setCaption(fn);
            return f;
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DisplayFriendlyForm(Class<T> clazz, FieldGroupFieldFactory factory) {
        this.clazz = clazz;
        if (null == factory) {
            f = new ViritinFieldGroupFieldFactory();
        } else {
            f = factory;
        }
    }

//    public DisplayFriendlyForm(T item, Collection<String> pids, FieldGroupFieldFactory factory) {
//        super();
//        // this is a hack to ensure setEntity doesn't call createContent() yet.
//        setCompositionRoot(new VerticalLayout());
//        setSizeUndefined();
//        group = setEntity(item);
//        @SuppressWarnings("unchecked") Class<T> clazz = (Class<T>) item.getClass();

//        form = new MFormLayout();
//
//        String caption;
//        if (null == pids) {
//            pidsToUse = DisplayFriendly.propertyIdList(clazz);
//        } else {
//            pidsToUse = pids;
//        }
//        for (String pid : pidsToUse) {
//            caption = DisplayFriendly.getFriendlyPropertyName(clazz, pid);
//            // Bean validators are automatically created when using a BeanFieldGroup.
//            // https://vaadin.com/book/vaadin7/-/page/datamodel.itembinding.html
//            group.buildAndBind(caption, pid);
//        }
//        setCompositionRoot(null);
//        createContent();
//        // copy pids before the ones we set here to ignore
////        pidsToIgnore.add("id");
////        pidsToIgnore.add("LOGGER");
////        pidsToIgnore.add("uniqueSessionId");
//    }

//    protected void validate(Collection<String> pidsToUse) {
//        Set<String> remainder = new HashSet<>(pidsToIgnore);
//        for (Object propertyId : getFieldGroup().getUnboundPropertyIds()) {
//            if (!pidsToIgnore.contains(propertyId.toString())) {
//                LOGGER.warn("PropertyID " + propertyId + " not properly excluded from DisplayFriendlyForm");
//            } else {
//                //noinspection SuspiciousMethodCalls
//                remainder.remove(propertyId);
//            }
//        }
//        for (Object propertyId : getFieldGroup().getBoundPropertyIds()) {
//            if (!pidsToUse.contains(propertyId.toString())) {
//                LOGGER.warn("PropertyID " + propertyId + " not explicitly added to DisplayFriendlyForm");
//            }
//        }
//        if (remainder.size() > 0) {
//            LOGGER.warn("PropertyID(s) " + remainder + " removed but were not found");
//        }
//    }
}
