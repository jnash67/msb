package com.medcognize.domain.validator.vaadin;

import com.medcognize.domain.Plan;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.daterangefield.DateUtil;

import java.util.Date;

public class InFsaPeriodValidator extends AbstractValidator<Date> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InPlanPeriodValidator.class);

    Field planField;

    public InFsaPeriodValidator(String message, Field<?> planField) {
        super(message);
        this.planField = planField;
    }

    @Override
    protected boolean isValidValue(Date value) {
        Plan p;
        try {
            p = (Plan) planField.getValue();
        } catch (Exception e) {
            LOGGER.error("Field did not contain a plan.");
            return false;
        }
        if (null == p) {
            LOGGER.warn("Plan should not be null for an expense");
            return true;
        }
        if (null == value) {
            LOGGER.warn("Date field has null value");
            return false;
        }
        return DateUtil.isBetweenInclusive(value, p.getPlanStartDate(), p.getPlanEndDate());
    }

    @Override
    public Class<Date> getType() {
        return Date.class;
    }
}