package com.medcognize.util;

import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.DisplayFriendlyForm;
import com.medcognize.form.WizardForm;
import com.medcognize.view.ComponentWindow;
import com.medcognize.view.crud.CommitAction;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class CrudUtil implements Serializable {

    public static <T extends DisplayFriendly> DisplayFriendlyForm<T> createForm(final Class<? extends
            DisplayFriendlyForm<T>> formClazz, T item) {
        DisplayFriendlyForm<T> form = null;
        try {
            form = formClazz.getDeclaredConstructor(new Class[]{item.getClass()}).newInstance(item);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException
                e) {
            e.printStackTrace();
        }
        return form;
    }

    public static <T extends DisplayFriendly> DisplayFriendlyForm<T> getNewItemForm(final Class<T> entityClazz,
                                                                                    final Class<? extends
                                                                                            DisplayFriendlyForm<T>>
                                                                                            formClazzToUse) {
        final T target;
        try {
            target = entityClazz.newInstance();
            return createForm(formClazzToUse, target);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends DisplayFriendly> Window showForm(final DisplayFriendlyForm<T> form,
                                                              final CommitAction action, String title) {
        if (null == form) {
            return null;
        }
        final Window window;
        if (WizardForm.class.isAssignableFrom(form.getClass())) {
            window = ((WizardForm) form).getWindowWeAreIn();
            ((WizardForm) form).setSuccessfulCommitAction(action);
        } else {
            window = new ComponentWindow(title, false, false);
            Button.ClickListener cl = new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    try {
                        form.getFieldGroup().commit();
                        action.run();
                        window.close();
                    } catch (FieldGroup.CommitException e) {
                        // Throwable c = e.getCause();
                        //Notification.show(c.getMessage(), Notification.Type.ERROR_MESSAGE);
                    }
                }
            };
            ((ComponentWindow) window).addClickListenerToSubmitButton(cl);
        }
        window.setContent(form);
        UI.getCurrent().addWindow(window);
        window.focus();
        return window;
    }
}
