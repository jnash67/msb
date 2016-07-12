package com.medcognize.view.crud;

import com.medcognize.UserRepository;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.DisplayFriendlyForm;
import com.medcognize.util.CrudUtil;
import com.vaadin.event.Action;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Window;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.form.AbstractForm.SavedHandler;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public abstract class EditTable<T extends DisplayFriendly> extends DisplayFriendlyTable<T> implements Action.Handler {

    protected final UserRepository repo;

    protected final Action ACTION_EDIT = new Action("Edit");

    protected User collectionOwner;
    protected final Class<? extends DisplayFriendlyForm<T>> defaultFormClazz;
    protected boolean editOnSingleClick = true;
    protected boolean contextMenuEnabled = false;

    public boolean isContextMenuEnabled() {
        return contextMenuEnabled;
    }

    public void setContextMenuEnabled(boolean contextMenuEnabled) {
        this.contextMenuEnabled = contextMenuEnabled;
    }

    public boolean isEditOnSingleClick() {
        return editOnSingleClick;
    }

    public void setEditOnSingleClick(boolean editOnSingleClick) {
        this.editOnSingleClick = editOnSingleClick;
    }

    public Class<? extends DisplayFriendlyForm<T>> getDefaultFormClazz() {
        return defaultFormClazz;
    }

    public EditTable(UserRepository repo, Class<T> entityClazz, final Class<? extends DisplayFriendlyForm<T>> formClazz,
                     ArrayList<String> orderedPidList) {
        super(entityClazz, orderedPidList);
        this.repo = repo;
        this.defaultFormClazz = formClazz;
    }

    public void setData(Collection<T> items, User collectionOwner) {
        super.setData(items);
        this.collectionOwner = collectionOwner;
        // the view can only be editable if we have a collection and a formClazz
        // owner can be null.  if it has no owner, it's a top level entity and we persist
        // it solo, as opposed to as part of an owner's collection
        if ((null != defaultFormClazz) && (null != items)) {
            setActionHandler();
        }
    }

    protected void setActionHandler() {
        addRowClickListener(new RowClickListener<T>() {
            @Override
            public void rowClick(RowClickEvent<T> event) {
                if (event.isDoubleClick()) {
                    if (!isEditOnSingleClick()) {
                        edit(event.getRow());
                    }
                }
                else {
                    if (MouseEventDetails.MouseButton.LEFT == event.getButton()) {
                        if (isEditOnSingleClick()) {
                            edit(event.getRow());
                        }
                    }
                }

            }
        });
        addActionHandler(this);
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        if (contextMenuEnabled) {
            return new Action[]{ACTION_EDIT};
        }
        return new Action[0];
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (contextMenuEnabled) {
            if (ACTION_EDIT == action) {
                if (null == target) {
                    return;
                }
                T item = getEntityFromContainer(target);
                edit(item);
            }
        }
    }

    protected void sortTable(String pid) {
        sort(new Object[]{pid}, new boolean[]{false});
    }

    protected DisplayFriendlyForm<T> createForm(final Class<? extends DisplayFriendlyForm<T>> formClazzToUse,
                                                final T item) {
        return CrudUtil.createForm(formClazzToUse, item, false);
    }

    protected DisplayFriendlyForm<T> getEditItemForm(final Class<? extends DisplayFriendlyForm<T>> formClazzToUse,
                                                     final T item) {
        return createForm(formClazzToUse, item);
    }

    protected Window edit(T row) {
        DisplayFriendlyForm<T> form = getEditItemForm(defaultFormClazz, row);
        form.setModalWindowTitle( "Edit " + DisplayFriendly.getFriendlyClassName(entityClazz));
        Window w = form.openInModalPopup();
        form.setSavedHandler(new SavedHandler<T>() {
            @Override
            public void onSave(T entity) {
                repo.save(collectionOwner);
                refreshRows();
                form.closePopup();
            }
        });
        form.setResetHandler(new AbstractForm.ResetHandler<T>() {
            @Override
            public void onReset(T entity) {
                form.closePopup();
            }
        });
        return w;
    }

    protected T getEntityFromContainer(Object target) {
        int index = getContainer().indexOfId(target);
        return getContainer().getIdByIndex(index);
    }
}