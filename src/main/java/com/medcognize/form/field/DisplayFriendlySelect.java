package com.medcognize.form.field;

import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import org.vaadin.viritin.fields.TypedSelect;

import javax.annotation.PostConstruct;

public class DisplayFriendlySelect<T extends DisplayFriendly> extends TypedSelect<T> {

    private Class<T> clazz;

    public DisplayFriendlySelect(Class<T> clazz) {
        super(clazz);
        this.clazz = clazz;
        setSizeUndefined();
        setNullSelectionAllowed(false);
        setBeans(UserUtil.getAll(DbUtil.getLoggedInUser(), clazz));
    }

}
