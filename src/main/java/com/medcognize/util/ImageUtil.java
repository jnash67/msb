package com.medcognize.util;

import com.vaadin.server.ThemeResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@SuppressWarnings({"unused", "StaticNonFinalField"})
public class ImageUtil implements Serializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtil.class);
	private static ThemeResource lightBulbImage, homeImage, calcImage, registerImage, loginImage, flashingArrowImage,
		providerImage, familyMemberImage, medicalExpenseImage, planImage, logoutImage, profilePicImage;

	public static ThemeResource getLightBulbImage() {
		if (null == lightBulbImage) {
			lightBulbImage = new ThemeResource("img/Gnome-Dialog-Information-32.png");
		}
		return lightBulbImage;
	}

	public static ThemeResource getProfilePicImage() {
		if (null == profilePicImage) {
			profilePicImage = new ThemeResource("img/profile-pic.png");
		}
		return profilePicImage;
	}


	public static ThemeResource getHomeImage() {
		if (null == homeImage) {
			homeImage = new ThemeResource("img/go-home-16x16.png");
		}
		return homeImage;
	}

	public static ThemeResource getCalcImage() {
		if (null == calcImage) {
			calcImage = new ThemeResource("img/accessories-calculator-16x16.png");
		}
		return calcImage;
	}

	public static ThemeResource getRegisterImage() {
		if (null == registerImage) {
			registerImage = new ThemeResource("img/gnome-word-16x16.svg");
		}
		return registerImage;
	}

	public static ThemeResource getLoginImage() {
		if (null == loginImage) {
			loginImage = new ThemeResource("img/locked-16x16.svg");
		}
		return loginImage;
	}

	public static ThemeResource getFlashingArrowImage() {
		if (null == flashingArrowImage) {
			flashingArrowImage = new ThemeResource("img/graphics-arrows-645370.gif");
		}
		return flashingArrowImage;
	}

	public static ThemeResource getProviderImage() {
		if (null == providerImage) {
			providerImage = new ThemeResource("img/building.png");
		}
		return providerImage;
	}

	public static ThemeResource getFamilyMemberImage() {
		if (null == familyMemberImage) {
			familyMemberImage = new ThemeResource("img/emblem-people-16x16.svg");
		}
		return familyMemberImage;
	}

	public static ThemeResource getMedicalExpenseImage() {
		if (null == medicalExpenseImage) {
			medicalExpenseImage = new ThemeResource("img/x-office-spreadsheet-16x16.png");
		}
		return medicalExpenseImage;
	}

	public static ThemeResource getPlanImage() {
		if (null == planImage) {
			planImage = new ThemeResource("img/gnome-info-16x16.svg");
		}
		return planImage;
	}

	public static ThemeResource getLogoutImage() {
		if (null == logoutImage) {
			logoutImage = new ThemeResource("img/edit-delete-16.svg");
		}
		return logoutImage;
	}
}
