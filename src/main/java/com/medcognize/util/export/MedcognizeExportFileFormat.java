package com.medcognize.util.export;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.Fsa;
import com.medcognize.domain.MedicalExpense;
import com.medcognize.domain.*;
import com.medcognize.domain.User;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.FileHeader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MedcognizeExportFileFormat {
    public static final String zipFileName = "medcognize.zip";
    public static final String userZipEntryFileName = "csv";
    public static final String plansZipEntryFileName = "plans.csv";
    public static final String fsasZipEntryFileName = "fsas.csv";
    public static final String familyMembersZipEntryFileName = "familymembers.csv";
    public static final String providersZipEntryFileName = "providers.csv";
    public static final String medicalExpensesZipEntryFileName = "medicalexpenses.csv";
    public static final ArrayList<String> entryFileNames = new ArrayList<String>() {
        {
            add(userZipEntryFileName);
            add(plansZipEntryFileName);
            add(fsasZipEntryFileName);
            add(familyMembersZipEntryFileName);
            add(providersZipEntryFileName);
            add(medicalExpensesZipEntryFileName);
        }
    };

    public static SerializableZipFile createFileFromUser(User u) {
        SerializableZipFile szf = null;
        InMemoryOutputStream inMemoryOutputStream = new InMemoryOutputStream();
        ZipOutputStream zos = new ZipOutputStream(inMemoryOutputStream);
        char[] blankPassword = "".toCharArray();
        ZipUtil.writeZipEntry(zos, userZipEntryFileName, CsvUtil.displayFriendlyToCsv(User.class, u, u).getBytes(), blankPassword);
        ZipUtil.writeZipEntry(zos, plansZipEntryFileName, CsvUtil.displayFriendlyListToCsv(Plan.class, u.getPlans(), u).getBytes(), blankPassword);
        ZipUtil.writeZipEntry(zos, fsasZipEntryFileName, CsvUtil.displayFriendlyListToCsv(Fsa.class, u.getFsas(), u).getBytes(), blankPassword);
        ZipUtil.writeZipEntry(zos, familyMembersZipEntryFileName, CsvUtil.displayFriendlyListToCsv(FamilyMember.class, u.getFamilyMembers(), u).getBytes(), blankPassword);
        ZipUtil.writeZipEntry(zos, providersZipEntryFileName, CsvUtil.displayFriendlyListToCsv(Provider.class, u.getProviders(), u).getBytes(), blankPassword);
        ZipUtil.writeZipEntry(zos, medicalExpensesZipEntryFileName, CsvUtil.displayFriendlyListToCsv(MedicalExpense.class, u.getMedicalExpenses(), u).getBytes(), blankPassword);
        try {
            zos.finish();
            zos.close();
            szf = ZipUtil.createInMemoryZipFile(zipFileName, inMemoryOutputStream.getZipContent());
        } catch (IOException | ZipException e) {
            e.printStackTrace();
        }
        return szf;
    }

    public static User createUserFromFile(SerializableZipFile zf) {
        try {
            FileHeader userZipEntry = ZipUtil.getZipEntryFileHeader(zf, userZipEntryFileName);
            FileHeader plansZipEntry = ZipUtil.getZipEntryFileHeader(zf, plansZipEntryFileName);
            FileHeader fsasZipEntry = ZipUtil.getZipEntryFileHeader(zf, fsasZipEntryFileName);
            FileHeader familyMembersZipEntry = ZipUtil.getZipEntryFileHeader(zf, familyMembersZipEntryFileName);
            FileHeader providersZipEntry = ZipUtil.getZipEntryFileHeader(zf, providersZipEntryFileName);
            FileHeader medicalExpensesZipEntry = ZipUtil.getZipEntryFileHeader(zf, medicalExpensesZipEntryFileName);
            if (null == userZipEntry) {
                log.warn("Invalid zip file.  No user.csv entry.");
                return null;
            }
            if (null == plansZipEntry) {
                log.warn("Invalid zip file.  No plans.csv entry.");
                return null;
            }
            if (null == fsasZipEntry) {
                log.warn("Invalid zip file.  No fsas.csv entry.");
                return null;
            }
            if (null == familyMembersZipEntry) {
                log.warn("Invalid zip file.  No familymembers.csv entry.");
                return null;
            }
            if (null == providersZipEntry) {
                log.warn("Invalid zip file.  No providers.csv entry.");
                return null;
            }
            if (null == medicalExpensesZipEntry) {
                log.warn("Invalid zip file.  No expenses.csv entry.");
                return null;
            }
            List<User> users = CsvUtil.csvWithHeaderToDisplayFriendlyList(User.class, ZipUtil.getCsvEntryFromZip(zf, userZipEntry), null);
            if (null == users) {
                log.warn("There should only be a single User in user.csv.  There are none.");
                return null;
            }
            if (users.size() != 1) {
                log.warn("There should only be a single User in user.csv.  There are " + users.size() + "" + ".");
                return null;
            }
            User csvUser = users.get(0);
            return csvUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
