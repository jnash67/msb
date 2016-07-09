package com.medcognize.form;

import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.util.CrudUtil;
import com.medcognize.view.crud.CommitAction;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.*;

;

public abstract class WizardForm<T extends DisplayFriendly> extends DisplayFriendlyForm<T> implements WizardProgressListener {

    @SuppressWarnings("UnusedDeclaration")
    private static final Logger LOGGER = LoggerFactory.getLogger(WizardForm.class);
    protected final Window windowWeAreIn;
    protected final Wizard wiz;
    protected final DisplayFriendlyForm<T> shadowForm;
    protected CommitAction ca = null;

    protected WizardForm(Class<? extends DisplayFriendlyForm<T>> formClazz, T item) {
        super((Class<T>) item.getClass(), null);
        this.shadowForm = CrudUtil.createForm(formClazz, item);
        // we don't use the shadowForm for display purposes
        this.wiz = createWizard();
        this.wiz.addListener(this);
        windowWeAreIn = createWindow();
        setSizeUndefined();
    }

    public abstract Wizard createWizard();

    protected Window createWindow() {
        final Window window = new Window();
        window.setModal(true);
        window.setClosable(false);
        window.setResizable(false);
        window.addStyleName("edit-dashboard");
        window.addShortcutListener(new ShortcutListener("Close", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                window.close();
            }
        });
        window.setSizeUndefined();
        window.setContent(this);
        return window;
    }

    public Window getWindowWeAreIn() {
        return windowWeAreIn;
    }

    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent event) {
    }

    @Override
    public void wizardCompleted(WizardCompletedEvent event) {
        try {
            commit();
            ca.run();
            windowWeAreIn.close();
        } catch (FieldGroup.CommitException e) {
            Throwable c = e.getCause();
            Notification.show(c.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    public void setSuccessfulCommitAction(CommitAction ca) {
        this.ca = ca;
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {
        windowWeAreIn.close();
    }


    public void commit() throws FieldGroup.CommitException {
        shadowForm.getFieldGroup().commit();
    }

}
