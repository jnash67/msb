package com.medcognize.form.field;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.NativeSelect;

public class YesNoSelect extends NativeSelect {

    public YesNoSelect(String caption, final CheckBox toShadow) {
        super(caption);
        this.addItem(Boolean.TRUE);
        setItemCaption(Boolean.TRUE, "Yes");
        this.addItem(Boolean.FALSE);
        setItemCaption(Boolean.FALSE, "No");
        this.setNullSelectionAllowed(false);
        this.setMultiSelect(false);
        this.setImmediate(true);
        if (null != toShadow) {
            this.setValue(toShadow.getValue());
        }
        addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (null != toShadow) {
                    toShadow.setValue(getValue());
                }
            }
        });

    }

    public Boolean getValue() {
        return (Boolean) super.getValue();
    }

}
