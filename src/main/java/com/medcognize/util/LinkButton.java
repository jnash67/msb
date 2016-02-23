package com.medcognize.util;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Button;

/*
    Button that looks like a button but functions like a Link
 */
public class LinkButton extends Button {

	public LinkButton(final String caption, final Resource res, final int width, final int height,
					  final BorderStyle border) {
		super(caption);

		setImmediate(true);
        addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				//noinspection deprecation
				LinkButton.this.getUI().getPage().open(res, "_blank", width, height, border);
			}
		});
	}
}