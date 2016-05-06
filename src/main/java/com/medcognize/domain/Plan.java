package com.medcognize.domain;

import com.google.common.collect.BiMap;
import com.medcognize.MedcognizeUI;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.validator.jsr303.DateFieldInYear;
import com.medcognize.domain.validator.jsr303.DateFieldOnOrAfter;
import com.medcognize.util.DbUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.csveed.annotations.CsvDate;
import org.hibernate.validator.constraints.NotBlank;
import org.vaadin.addon.daterangefield.DateUtil;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@DateFieldOnOrAfter(first = "planEndDate", second = "planStartDate", message = "The end date must be equal to or " +
        "after the " + "start date")
@DateFieldInYear.List({@DateFieldInYear(dateField = "planStartDate", yearField = "planYear",
        message = "The start date is not in the year of the plan"), @DateFieldInYear(dateField = "planEndDate",
        yearField = "planYear", message = "The end date is not in the year of the plan")})
@Data
// Two plans are equal if they have the same name. Plan Name is unique.
@EqualsAndHashCode(callSuper = false, of = {"planName"})
@NoArgsConstructor
@Entity
public class Plan extends DisplayFriendlyAbstractEntity implements Serializable {

    public static enum PlanType {HMO, PPO}

    public static final PlanType defaultPlanType = PlanType.HMO;

    private static final String planTypeCaptionString = PlanType.HMO.toString() + ":HMO, " +
            "" + PlanType.PPO.toString() + ":PPO";
    private static final String captionString = "planName:Plan Name, activePlan:Is Active Plan, planType:Plan Type, " +
            "planYear:Year, planStartDate:Period Start, planEndDate:Period End, " +
            "individualInNetworkDeductible:Individual In Network,familyInNetworkDeductible:Family In Network, " +
            "individualOutOfNetworkDeductible:Individual Out Of Network, " +
            "familyOutOfNetworkDeductible:Family Out Of Network, individualOutOfPocketLimit:Individual Out Of Pocket," +
            " familyOutOfPocketLimit:Family Out Of Pocket, primaryCareCopay:Primary Care," +
            "specialistCopay:Specialist, emergencyRoomCopay:Emergency Room, tier1PrescriptionCopay:Tier 1," +
            "tier2PrescriptionCopay:Tier 2, tier3PrescriptionCopay:Tier 3, notes:Notes";

    @SuppressWarnings("UnusedDeclaration")
    public static final BiMap<String, String> captionMap = createBiMap(captionString);
    public static final BiMap<String, String> planTypeStringMap = createBiMap(planTypeCaptionString);

    @NotBlank(message = "The plan name cannot be blank")
    @javax.validation.constraints.Size(max = 50, message = "The plan name must be less than 50 characters long")
    private String planName = "";

    private boolean activePlan = false;

    @NotNull
    private PlanType planType = defaultPlanType;

    public static final int MIN_YEAR = 2012;
    public static final int MAX_YEAR = 2020;
    @Min(value = 2012, message = "Year cannot be before 2012")
    @Max(value = 2020, message = "Year cannot be after 2020")
    private int planYear = DateUtil.currentYear();
    @CsvDate(format = MedcognizeUI.US_DATE_FORMAT)
    private Date planStartDate = DateUtil.firstDayOfYear(DateUtil.currentYear());
    @CsvDate(format = MedcognizeUI.US_DATE_FORMAT)
    private Date planEndDate = DateUtil.lastDayOfYear(DateUtil.currentYear());
    // annual deductibles
    @Min(0)
    private double individualInNetworkDeductible = 0.0;
    @Min(0)
    private double familyInNetworkDeductible = 0.0;
    @Min(0)
    private double individualOutOfNetworkDeductible = 0.0;
    @Min(0)
    private double familyOutOfNetworkDeductible = 0.0;
    // annual out of pocket limits
    @Min(0)
    private double individualOutOfPocketLimit = 0.0;
    @Min(0)
    private double familyOutOfPocketLimit = 0.0;
    // copays
    @Min(0)
    private double primaryCareCopay = 0.0;
    @Min(0)
    private double specialistCopay = 0.0;
    @Min(0)
    private double emergencyRoomCopay = 0.0;
    @Min(0)
    private double tier1PrescriptionCopay = 0.0;
    @Min(0)
    private double tier2PrescriptionCopay = 0.0;
    @Min(0)
    private double tier3PrescriptionCopay = 0.0;
    private String notes = "";

    @Override
    public String toString() {
        if (this.planName.contains(String.valueOf(this.planYear))) {
            return this.planName;
        }
        return this.planName + " (" + this.planYear + ")";
    }

    public static String ensureUniqueName(String initialName) {
        Collection<Plan> pls = DbUtil.getLoggedInUser().getAll(Plan.class);
        String name = initialName;
        int v = 2;
        while (!existsName(name, pls)) {
            name = initialName + "v" + String.valueOf(v);
            v++;
        }
        return name;
    }

    public static boolean existsName(String name, Collection<Plan> pls) {
        for (Plan p : pls) {
            if (name.equals(p.getPlanName())) {
                return false;
            }
        }
        return true;
    }
}