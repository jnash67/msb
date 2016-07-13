package com.medcognize.form.field;

import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;

public class DisplayFriendlySelectAndButton<T extends DisplayFriendly> extends TypedSelectAndButton<T> {

    private Class<T> clazz;

    public DisplayFriendlySelectAndButton(Class<T> clazz) {
        super(clazz);
        this.clazz = clazz;
        setSizeUndefined();
        setNullSelectionAllowed(false);
        setBeans(UserUtil.getAll(DbUtil.getLoggedInUser(), clazz));
    }
}
