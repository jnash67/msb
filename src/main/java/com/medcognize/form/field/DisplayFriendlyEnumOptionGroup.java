package com.medcognize.form.field;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.OptionGroup;

import java.util.ArrayList;

/**
 * Created by jnash on 5/16/2014.
 */
public class DisplayFriendlyEnumOptionGroup<E extends Enum<E>> extends OptionGroup {

    public DisplayFriendlyEnumOptionGroup(Class<E> enumClass) {
        super();
        StringToDisplayFriendlyEnumConverter<E> converter = new StringToDisplayFriendlyEnumConverter<>(enumClass);
        setConverter(converter);
        // code taken from AbstractSelect constructor
        // Creates the options container and add given options to it
        final Container c = new IndexedContainer();
        ArrayList<String> options = converter.getOptions();
        if (options != null) {
            for (String obj : options) {
                c.addItem(obj);
            }
        }
        this.setContainerDataSource(c);
    }

}