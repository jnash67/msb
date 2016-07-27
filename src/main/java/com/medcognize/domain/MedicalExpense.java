package com.medcognize.domain;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.basic.DisplayFriendlyCaption;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.csveed.annotations.CsvDate;
import org.vaadin.addon.daterangefield.DateUtil;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
// You can have more than one expense with all the same fields.  However,
// if two expenses have the same unique DB id then they are the same
@EqualsAndHashCode(callSuper = true, of = {"id"})
@Entity
public class MedicalExpense extends DisplayFriendlyAbstractEntity implements Serializable {

	public static enum MedicalExpenseType {
		DOCTOR("Doctor"), PRESCRIPTION("Prescription"), EMERGENCY("Emergency Room");
		private String name;

		MedicalExpenseType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static final MedicalExpenseType defaultMedicalExpenseType = MedicalExpenseType.DOCTOR;

	public static enum PrescriptionTierType {
		TIER1("Tier 1"), TIER2("Tier 2"), TIER3("Tier 3");
		private String name;

		PrescriptionTierType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
	public static final PrescriptionTierType defaultPrescriptionTierType = PrescriptionTierType.TIER1;

	@CsvDate(format = MedcognizeUI.US_DATE_FORMAT)
	private Date date = DateUtil.now();
	@ManyToOne
	@JoinColumn(name = "plan_id")
	private Plan plan;
	@ManyToOne
	@JoinColumn(name = "family_member_id")
	private FamilyMember familyMember;
	@ManyToOne
	@JoinColumn(name = "provider_id")
	private Provider provider;
	@DisplayFriendlyCaption("In Plan")
	private boolean medicalExpenseInPlan;
	@DisplayFriendlyCaption("Expense Type")
	private MedicalExpenseType medicalExpenseType = defaultMedicalExpenseType;
	@DisplayFriendlyCaption("Prescription Tier")
	private PrescriptionTierType prescriptionTierType = defaultPrescriptionTierType;
	@Min(0)
	private double outOfPocketAmount = 0.0;
	@Min(0)
	@DisplayFriendlyCaption("Provider Cost")
	private double costAccordingToProvider = 0.0;
	@Min(0)
	private double maximumAmount = 0.0;
	@Min(0)
	private double deductibleAmount = 0.0;
	@Min(0)
	private double copayAmount = 0.0;
	@Min(0)
	private double paymentAmount = 0.0;
	private String comments = "";

	@SuppressWarnings("UnusedDeclaration")
	public double amountInsuranceWillNotPay() {
		if (maximumAmount >= costAccordingToProvider) {
			return 0;
		}
		return costAccordingToProvider - maximumAmount;
	}
}
