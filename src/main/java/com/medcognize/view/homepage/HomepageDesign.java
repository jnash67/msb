package com.medcognize.view.homepage;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@DesignRoot
public class HomepageDesign extends VerticalLayout {

    Image img;
    Label l1, l2;
    MenuBar menu;
    GridLayout withImage;
    Button registerButton;
    VerticalLayout body;
    Label top, middle, bottom;
    VerticalLayout spacer;
    HorizontalLayout bottomLinks;
    Button termsLink;

    public HomepageDesign() {
        Design.read(this);
    }
}

