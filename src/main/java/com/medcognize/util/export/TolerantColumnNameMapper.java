package com.medcognize.util.export;

import lombok.extern.slf4j.Slf4j;
import org.csveed.api.Header;
import org.csveed.bean.ColumnNameMapper;
import org.csveed.common.Column;
import org.csveed.report.CsvException;

@Slf4j
public class TolerantColumnNameMapper<T> extends ColumnNameMapper<T> {

    @Override
    protected void checkKey(Header header, Column key) {
        try {
            header.getIndex(key.getColumnName());
        } catch (CsvException err) {
            TolerantColumnNameMapper.log.warn("In class " + this.beanInstructions.getBeanClass().getSimpleName() + " -- This is probably a new " + "field.  Issue will go away when CSV file is persisted.");
        }
    }
}
