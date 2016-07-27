package com.medcognize.view.admin;

import com.medcognize.UserDetailsServiceImpl;
import com.medcognize.domain.User;
import com.medcognize.util.DbUtil;
import com.medcognize.util.export.MedcognizeExportFileFormat;
import com.medcognize.util.export.SerializableZipFile;
import com.medcognize.util.export.ZipUtil;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Slf4j
public class CurrentUserZipCsvUploadTab extends VerticalLayout implements Upload.Receiver, Upload.SucceededListener {
	@Autowired
	UserDetailsServiceImpl repo;
	//File file;
	String filename;
	ByteArrayOutputStream fos;
	Upload upload = new Upload();

	public CurrentUserZipCsvUploadTab() {
		setSpacing(true);
		setMargin(true);
		Label l = new Label("This data file will be uploaded to the currently logged in user: " + DbUtil.getLoggedInUser().getUsername());
		upload.setCaption("Specify your data zip file");
		upload.setReceiver(this);
		upload.addSucceededListener(this);
		addComponent(l);
		addComponent(upload);
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
		//SerializableZipFile zf = new SerializableZipFile(file);
		SerializableZipFile zf = ZipUtil.createInMemoryZipFile(filename, fos.toByteArray());
		User u = MedcognizeExportFileFormat.createUserFromFile(zf);
		if (null != u) {
			// repo.saveAndFlush(u);
			log.warn("File successfully uploaded");
		}
	}
}
