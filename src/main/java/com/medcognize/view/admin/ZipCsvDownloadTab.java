package com.medcognize.view.admin;

import com.medcognize.MedcognizeUI;
import com.medcognize.util.LinkButton;
import com.medcognize.util.export.ZipUtil;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.io.input.ReaderInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class ZipCsvDownloadTab extends VerticalLayout {

	@SuppressWarnings("unused")
	public static StreamResource.StreamSource getCsvStreamResource(final String csv) {
		return new StreamResource.StreamSource() {
			public InputStream getStream() {
				try (StringReader sr = new StringReader(csv);
					 ReaderInputStream ris = new ReaderInputStream(sr)) {
					return ris;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}

	public ZipCsvDownloadTab() {
		setSpacing(true);
		setMargin(true);
		StreamResource sr = ZipUtil.createDownloadZipFileStreamResource(((MedcognizeUI) MedcognizeUI.getCurrent())
			.getUser());
		LinkButton lb = new LinkButton("Download All", sr, 1, 1, BorderStyle.DEFAULT);
		addComponent(lb);
	}
}