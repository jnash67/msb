package com.medcognize.domain;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.basic.DisplayFriendlyCaption;
import com.medcognize.domain.validator.jsr303.DateFieldInYear;
import com.medcognize.domain.validator.jsr303.DateFieldOnOrAfter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.csveed.annotations.CsvDate;
import org.hibernate.validator.constraints.NotBlank;
import org.vaadin.addon.daterangefield.DateUtil;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
// Two FSAs are equal if they have the same name. FSA Name is unique.
@EqualsAndHashCode(callSuper = false, of = {"fsaName"})
@DateFieldOnOrAfter(first = "fsaEndDate", second = "fsaStartDate", message = "The end date must be equal to or after the start date")
@DateFieldInYear.List({@DateFieldInYear(dateField = "fsaStartDate", yearField = "fsaYear", message = "The start date is not in the year of the FSA"), @DateFieldInYear(dateField = "fsaEndDate", yearField = "fsaYear", message = "The end date is not in the year of the FSA")})
@Entity
@Slf4j
@DisplayFriendlyCaption("FSA")
public class Fsa extends DisplayFriendlyAbstractEntity implements Serializable {

	@NotBlank(message = "The FSA name cannot be blank")
	private String fsaName = "";

	@Min(0)
	@DisplayFriendlyCaption("Amount In FSA")
	private double amountInFsa = 0.0;

	@Min(value = 2012, message = "Year cannot be before 2012")
	@Max(value = 2020, message = "Year cannot be after 2020")
	@DisplayFriendlyCaption("Year")
	private int fsaYear = DateUtil.currentYear();

	@CsvDate(format = MedcognizeUI.US_DATE_FORMAT)
	@DisplayFriendlyCaption("Period Start")
	private Date fsaStartDate = DateUtil.firstDayOfYear(DateUtil.currentYear());

	@CsvDate(format = MedcognizeUI.US_DATE_FORMAT)
	@DisplayFriendlyCaption("Period End")
	private Date fsaEndDate = DateUtil.lastDayOfYear(DateUtil.currentYear());

	@Override
	public String toString() {
		return fsaName;
	}
}
