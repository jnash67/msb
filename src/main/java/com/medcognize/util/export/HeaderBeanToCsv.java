package com.medcognize.util.export;

import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.BeanToCsv;
import au.com.bytecode.opencsv.bean.MappingStrategy;

import java.util.List;

/**
 * This writes a header even if the list is empty
 */
public class HeaderBeanToCsv<T> extends BeanToCsv<T> {

    public HeaderBeanToCsv(Class<T> clazz) {
        super(clazz);
    }

    public boolean write(MappingStrategy<T> mapper, CSVWriter csv, List<?> objects) {

        if (objects == null) {
            return false;
        }
        if (objects.isEmpty()) {
            try {
                csv.writeNext(processHeader(mapper));
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Error writing CSV !", e);
            }
        }
        return super.write(mapper, csv, objects);
    }
}
