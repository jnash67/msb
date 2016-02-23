package com.medcognize.domain;

import com.google.common.collect.BiMap;
import com.medcognize.MedcognizeUI;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.csveed.annotations.CsvDate;
import org.vaadin.addon.daterangefield.DateUtil;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
// two expenses are equal if all the fields are equal
@EqualsAndHashCode(callSuper = false)
@Entity
public class MedicalExpense extends DisplayFriendlyAbstractEntity implements Serializable {

	public static enum MedicalExpenseType {DOCTOR, PRESCRIPTION, EMERGENCY}

	public static final MedicalExpenseType defaultMedicalExpenseType = MedicalExpenseType.DOCTOR;

	public static enum PrescriptionTierType {TIER1, TIER2, TIER3}

	public static final PrescriptionTierType defaultPrescriptionTierType = PrescriptionTierType.TIER1;

	private static final String medicalExpenseTypeCaptionString = MedicalExpenseType.DOCTOR.toString() + ":Doctor " +
		"Visit, " + MedicalExpenseType.PRESCRIPTION.toString() + ":Prescription," +
		MedicalExpenseType.EMERGENCY.toString() + ":Emergency Room";

	private static final String prescriptionTierTypeCaptionString = PrescriptionTierType.TIER1.toString() + ":Tier " +
		"1," + PrescriptionTierType.TIER2.toString() + ":Tier 2," + PrescriptionTierType.TIER3.toString() + ":Tier 3";

	private static final String captionString = "date:Date, plan:Plan, familyMember:Family Member, " +
		"provider:Provider, " +
		"medicalExpenseInPlan:In Plan, medicalExpenseType:Expense Type, " +
		"prescriptionTierType:Prescription Tier, costAccordingToProvider:Provider Cost, " +
		"maximumAmount:Maximum Amount, deductibleAmount:Deductible Amount, " +
		"copayAmount:Copay Amount, outOfPocketAmount:Out of Pocket Amount, " +
		"paymentAmount:Payment Amount, comments:Comments";

	@SuppressWarnings("UnusedDeclaration")
	public static final BiMap<String, String> captionMap = createBiMap(captionString);
	public static final BiMap<String, String> medicalExpenseTypeStringMap = createBiMap
		(medicalExpenseTypeCaptionString);
	public static final BiMap<String, String> prescriptionTierStringMap = createBiMap
		(prescriptionTierTypeCaptionString);

	@CsvDate(format = MedcognizeUI.US_DATE_FORMAT)
	private Date date = DateUtil.now();

	private Plan plan;

	private FamilyMember familyMember;

	private Provider provider;

	private boolean medicalExpenseInPlan;
	private MedicalExpenseType medicalExpenseType = defaultMedicalExpenseType;
	private PrescriptionTierType prescriptionTierType = defaultPrescriptionTierType;
	@Min(0)
	private double outOfPocketAmount = 0.0;
	@Min(0)
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