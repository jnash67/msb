package com.medcognize.domain;

import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.basic.DisplayName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class PlanLimit extends DisplayFriendlyAbstractEntity implements Serializable {

    String limitName = "";
    double annualLimit = 0.0;
    @DisplayName("Amount Used")
    double usage = 0.0;
    @DisplayName("Remaining Balance")
    double balance = 0.0;

    public PlanLimit(String limitName, double annualLimit, double usage) {
        this.limitName = limitName;
        this.annualLimit = annualLimit;
        this.usage = usage;
        this.balance = annualLimit - usage;
    }
}
