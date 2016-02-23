package com.medcognize.form.field;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 * From: http://alfrescodev.com/node/29
 *
 * @author www.alfrescodev.com
 *
 */
public class MultiLineLabel extends Label {

    private static final long serialVersionUID = 1L;

    private static final String MAIN_STYLE="multiline-label";
    private static final String VALUE_STYLE="multiline-value";

    private static final String DIV_PREFIX="<div class=\""+VALUE_STYLE+"\">";
    private static final String DIV_SUFFIX="</div>";

    public MultiLineLabel() {
        this("");
    }

    public MultiLineLabel(String labelText) {
        super(DIV_PREFIX+labelText+DIV_SUFFIX, ContentMode.HTML);
        setStyleName(MAIN_STYLE);
    }

    public void setValue(String labelText)
    {
        super.setValue(DIV_PREFIX+labelText+DIV_SUFFIX);
        setContentMode(ContentMode.HTML);
    }

}