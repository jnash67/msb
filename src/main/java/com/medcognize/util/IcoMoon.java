package com.medcognize.util;

import com.vaadin.server.FontIcon;

/**
 * Enum that matches the content of our font icon (this example font has
 * only one icon).
 */
public enum IcoMoon implements FontIcon {
    HOME(0xe81c),
    VCARD(0xe82e),
    CREDIT(0xe600),
    USERADD(0xe80b),
    MEDKIT(0xe845),
    MONEY(0xe844),
    BELL(0xe82d),
    COG(0xe84d),
    CANCEL(0xe80e),
    // AKA pencil2
    EDIT (0xe601),
    CALCULATOR(0xe858);

    // You can see the codepoints in the IcoMoon app, or in the demo.html
    private final int codepoint;
    // This must match (S)CSS
    private final String fontFamily = "IcoMoon";

    IcoMoon(int codepoint) {
        this.codepoint = codepoint;
    }

    @Override
    public String getFontFamily() {
        return fontFamily;
    }

    @Override
    public int getCodepoint() {
        return codepoint;
    }

    @Override
    public String getHtml() {
        return "<span class=\"v-icon IcoMoon\">&#x" + Integer.toHexString(codepoint) + ";</span>";
    }

    @Override
    public String getMIMEType() {
        // Font icons are not real resources
        throw new UnsupportedOperationException(FontIcon.class.getSimpleName() + " should not be used where a MIME " +
                "type is needed.");
    }

}