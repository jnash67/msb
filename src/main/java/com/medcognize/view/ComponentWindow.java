package com.medcognize.view;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ComponentWindow extends Window {

    private VerticalLayout vl;
    private Button cancelButton;
    private Button submitButton;
    private String cancelText = "Cancel";
    private String submitText = "Submit";
    private boolean showSubmitButton = true;
    private boolean showCancelButton = true;

    public ComponentWindow(String title, boolean closable, boolean resizable) {
        super(title);
        vl = new VerticalLayout();
        cancelButton = new Button();
        submitButton = new Button();
        super.setContent(vl);

        this.setModal(true);
        this.setClosable(closable);
        this.setResizable(resizable);
        // this.addStyleName("edit-dashboard");
    }

    @SuppressWarnings("unused")
    public void showSubmitButton(boolean o) {
        this.showSubmitButton = o;
    }

    public void showCancelButton(boolean o) {
        this.showCancelButton = o;
    }

    @SuppressWarnings("unused")
    public void setCancelButtonText(String ct) {
        this.cancelText = ct;
        if (null != cancelButton) {
            cancelButton.setCaption(cancelText);
        }
    }

    public void setSubmitButtonText(String st) {
        this.submitText = st;
        if (null != submitButton) {
            submitButton.setCaption(submitText);
        }
    }

    public void addClickListenerToSubmitButton(Button.ClickListener cl) {
        submitButton.addClickListener(cl);
    }

    @SuppressWarnings("unused")
    public void addClickListenerToCancelButton(Button.ClickListener cl) {
        cancelButton.addClickListener(cl);
    }

    @Override
    public void setContent(Component comp) {
        if (null == comp) {
            return;
        }
        comp.setSizeUndefined();
        if (AbstractOrderedLayout.class.isAssignableFrom(comp.getClass())) {
            ((AbstractOrderedLayout) comp).setMargin(true);
        }
        vl.addComponent(comp);

        if (showCancelButton || showSubmitButton) {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setMargin(true);
            hl.setSpacing(true);
            // hl.addStyleName("footer");
            hl.setWidth("100%");

            if (showCancelButton) {
                cancelButton.setCaption(cancelText);
                cancelButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ComponentWindow.this.close();
                    }
                });
                cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, (int[]) null);
                addShortcutListener(new ShortcutListener("Close", ShortcutAction.KeyCode.ESCAPE, null) {
                    @Override
                    public void handleAction(Object sender, Object target) {
                        cancelButton.click();
                    }
                });
                hl.addComponent(cancelButton);
                hl.setExpandRatio(cancelButton, 1);
                hl.setComponentAlignment(cancelButton, Alignment.TOP_RIGHT);
            } else {
                hl.addComponent(submitButton);
                hl.setExpandRatio(submitButton, 1);
                hl.setComponentAlignment(submitButton, Alignment.TOP_RIGHT);
            }
            if (showSubmitButton) {
                submitButton.setCaption(submitText);
                //submitButton.addStyleName("wide");
                //submitButton.addStyleName("default");
                submitButton.setClickShortcut(ShortcutAction.KeyCode.ENTER, (int[]) null);
                hl.addComponent(submitButton);
            }
            vl.addComponent(hl);
        } else {
            setClosable(true);
        }
    }

}
