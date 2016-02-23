package com.medcognize.util.export;

import org.csveed.bean.conversion.AbstractConverter;

public abstract class EasierAbstractConverter<K> extends AbstractConverter<K> {

    public EasierAbstractConverter(Class<K> clazz) {
        super(clazz);
    }

    @Override
    public String toString(K value) throws Exception {
        return value.toString();
    }
}