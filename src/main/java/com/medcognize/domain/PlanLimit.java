package com.medcognize.domain;

import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.basic.DisplayFriendlyCaption;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.io.Serializable;

public class PlanLimit extends DisplayFriendlyAbstractEntity implements Serializable {

    String limitName = "";
    double annualLimit = 0.0;
    @DisplayFriendlyCaption("Amount Used")
    double usage = 0.0;
    @DisplayFriendlyCaption("Remaining Balance")
    double balance = 0.0;

    public PlanLimit(String limitName, double annualLimit, double usage) {
        this.limitName = limitName;
        this.annualLimit = annualLimit;
        this.usage = usage;
        this.balance = annualLimit - usage;
    }
}
