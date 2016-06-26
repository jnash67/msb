package com.medcognize.view.admin;

import com.medcognize.UserRepository;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.EmailAddress;
import com.medcognize.util.UserUtil;
import com.medcognize.util.export.CsvUtil;
import com.medcognize.util.export.MedcognizeExportFileFormat;
import com.medcognize.util.export.SerializableZipFile;
import com.medcognize.util.export.ZipUtil;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
@Slf4j
public class AnyUserZipCsvUploadTab extends VerticalLayout implements Upload.Receiver, Upload.SucceededListener {
    UserRepository repo;
    String filename = "";
    ByteArrayOutputStream fos;
    Upload upload = new Upload();
    PasswordField passwordField = new PasswordField("Password");

    public AnyUserZipCsvUploadTab(UserRepository repo) {
        this.repo = repo;
        setSpacing(true);
        setMargin(true);
        upload.setCaption("Specify your data zip file");
        upload.setReceiver(this);
        upload.addSucceededListener(this);
        addComponent(upload);
        addComponent(passwordField);
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        // Create upload stream - Stream to write to
        this.filename = filename;
        this.fos = new ByteArrayOutputStream();
        return this.fos;
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        try {
            //SerializableZipFile zf = new SerializableZipFile(file);
            if ("".equals(filename)) {
                return;
            }
            SerializableZipFile zf = ZipUtil.createInMemoryZipFile(filename, fos.toByteArray());
            char[] password = passwordField.getValue().toCharArray();
            if (zf.isEncrypted()) {
                try {
                    zf.setPassword(password);
                } catch (NullPointerException npe) {
                    log.warn("Null password");
                }
            }
            FileHeader userZipEntry = ZipUtil.getZipEntryFileHeader(zf, MedcognizeExportFileFormat.userZipEntryFileName);
            FileHeader plansZipEntry = ZipUtil.getZipEntryFileHeader(zf, MedcognizeExportFileFormat.plansZipEntryFileName);
            FileHeader fsasZipEntry = ZipUtil.getZipEntryFileHeader(zf, MedcognizeExportFileFormat.fsasZipEntryFileName);
            FileHeader familyMembersZipEntry = ZipUtil.getZipEntryFileHeader(zf, MedcognizeExportFileFormat.familyMembersZipEntryFileName);
            FileHeader providersZipEntry = ZipUtil.getZipEntryFileHeader(zf, MedcognizeExportFileFormat.providersZipEntryFileName);
            FileHeader medicalExpensesZipEntry = ZipUtil.getZipEntryFileHeader(zf, MedcognizeExportFileFormat.medicalExpensesZipEntryFileName);
            if (null == userZipEntry) {
                log.warn("Invalid zip file.  No user.csv entry.");
                return;
            }
            if (null == plansZipEntry) {
                log.warn("Invalid zip file.  No plans.csv entry.");
                return;
            }
            if (null == fsasZipEntry) {
                log.warn("Invalid zip file.  No fsas.csv entry.");
                return;
            }
            if (null == familyMembersZipEntry) {
                log.warn("Invalid zip file.  No familymembers.csv entry.");
                return;
            }
            if (null == providersZipEntry) {
                log.warn("Invalid zip file.  No providers.csv entry.");
                return;
            }
            if (null == medicalExpensesZipEntry) {
                log.warn("Invalid zip file.  No expenses.csv entry.");
                return;
            }
            List<User> users = CsvUtil.csvWithHeaderToDisplayFriendlyList(User.class, ZipUtil.getCsvEntryFromZip(zf, userZipEntry), null);
            if (null == users) {
                log.warn("There should only be a single User in user.csv.  There are none.");
                return;
            }
            if (users.size() != 1) {
                log.warn("There should only be a single User in user.csv.  There are " + users.size() + ".");
                return;
            }
            User csvUser = users.get(0);
            User csvUserWithListManager = UserUtil.createNewRegularUser(repo, new EmailAddress(csvUser.getUsername()), passwordField.getValue());
            if (null != csvUserWithListManager) {
                log.warn("File successfully uploaded");
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
