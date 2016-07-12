package com.medcognize.domain;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.basic.DisplayFriendlyCaption;
import com.medcognize.domain.validator.jsr303.DateFieldInYear;
import com.medcognize.domain.validator.jsr303.DateFieldOnOrAfter;
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
import java.util.Date;

@Data
// Two plans are equal if they have the same name. Plan Name is unique.
@EqualsAndHashCode(callSuper = false, of = {"planName"})
@NoArgsConstructor
@DateFieldOnOrAfter(first = "planEndDate", second = "planStartDate", message = "The end date must be equal to or after the start date")
@DateFieldInYear.List({@DateFieldInYear(dateField = "planStartDate", yearField = "planYear", message = "The start date is not in the year of the plan"), @DateFieldInYear(dateField = "planEndDate", yearField = "planYear", message = "The end date is not in the year of the plan")})
@Entity
public class Plan extends DisplayFriendlyAbstractEntity implements Serializable {

    public static enum PlanType {
        HMO, PPO;
    }

    public static final PlanType defaultPlanType = PlanType.HMO;
    @NotBlank(message = "The plan name cannot be blank")
    @javax.validation.constraints.Size(max = 50, message = "The plan name must be less than 50 characters long")
    private String planName = "";
    @DisplayFriendlyCaption("Is Active Plan")
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
    @DisplayFriendlyCaption("Individual In Network")
    private double individualInNetworkDeductible = 0.0;
    @Min(0)
    @DisplayFriendlyCaption("Family In Network")
    private double familyInNetworkDeductible = 0.0;
    @Min(0)
    @DisplayFriendlyCaption("Individual Out Of Network")
    private double individualOutOfNetworkDeductible = 0.0;
    @Min(0)
    @DisplayFriendlyCaption("Family Out Of Network")
    private double familyOutOfNetworkDeductible = 0.0;
    // annual out of pocket limits
    @Min(0)
    @DisplayFriendlyCaption("Individual Out Of Pocket")
    private double individualOutOfPocketLimit = 0.0;
    @Min(0)
    @DisplayFriendlyCaption("Family Out Of Pocket")
    private double familyOutOfPocketLimit = 0.0;
    // copays
    @Min(0)
    @DisplayFriendlyCaption("Primary Care")
    private double primaryCareCopay = 0.0;
    @Min(0)
    @DisplayFriendlyCaption("Specialist")
    private double specialistCopay = 0.0;
    @Min(0)
    @DisplayFriendlyCaption("Emergency Room")
    private double emergencyRoomCopay = 0.0;
    @Min(0)
    @DisplayFriendlyCaption("Tier 1")
    private double tier1PrescriptionCopay = 0.0;
    @Min(0)
    @DisplayFriendlyCaption("Tier 2")
    private double tier2PrescriptionCopay = 0.0;
    @Min(0)
    @DisplayFriendlyCaption("Tier 3")
    private double tier3PrescriptionCopay = 0.0;
    private String notes = "";

    @Override
    public String toString() {
        if (this.planName.contains(String.valueOf(this.planYear))) {
            return this.planName;
        }
        return this.planName + " (" + this.planYear + ")";
    }
}
