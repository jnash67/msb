package com.medcognize.domain;

import com.google.common.collect.BiMap;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class PlanLimit extends DisplayFriendlyAbstractEntity implements Serializable {

    private static final String captionString = "limitName:Limit Type, annualLimit:Annual Limit, " +
            "usage:Amount Used, balance:Remaining Balance";
    @SuppressWarnings("UnusedDeclaration")
    public static final BiMap<String, String> captionMap = createBiMap(captionString);

    String limitName = "";
    double annualLimit = 0.0;
    double usage = 0.0;
    @Setter(AccessLevel.NONE)
    double balance = 0.0;

    public PlanLimit(String limitName, double annualLimit, double usage) {
        this.limitName = limitName;
        this.annualLimit = annualLimit;
        this.usage = usage;
        this.balance = annualLimit - usage;
    }
}