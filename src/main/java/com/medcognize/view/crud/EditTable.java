package com.medcognize.view.crud;

import com.medcognize.UserRepository;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.DisplayFriendlyForm;
import com.medcognize.util.CrudUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public abstract class EditTable<T extends DisplayFriendly> extends DisplayFriendlyTable<T> implements Action.Handler {

    @Autowired
    protected UserRepository repo;

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

    public EditTable(Class<T> entityClazz, final Class<? extends DisplayFriendlyForm<T>> formClazz,
                     ArrayList<String> orderedPidList) {
        super(entityClazz, orderedPidList);
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
        addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    if (!isEditOnSingleClick()) {
                        getEditItemFormAndShow(event.getItemId());
                    }
                } else {
                    if (MouseEventDetails.MouseButton.LEFT == event.getButton()) {
                        if (isEditOnSingleClick()) {
                            getEditItemFormAndShow(event.getItemId());
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
                getEditItemFormAndShow(target);
            }
        }
    }

    protected void sortTable(String pid) {
        sort(new Object[]{pid}, new boolean[]{false});
    }

    protected DisplayFriendlyForm<T> createForm(final Class<? extends DisplayFriendlyForm<T>> formClazzToUse,
                                                final T item) {
        return CrudUtil.createForm(formClazzToUse, item);
    }

    protected DisplayFriendlyForm<T> getEditItemForm(final Class<? extends DisplayFriendlyForm<T>> formClazzToUse,
                                                     final T item) {
        return createForm(formClazzToUse, item);
    }

    protected Window getEditItemFormAndShow(Object itemId) {
        int index = getContainer().indexOfId(itemId);
        T item = getContainer().getIdByIndex(index);
        DisplayFriendlyForm<T> form = getEditItemForm(defaultFormClazz, item);
        return showForm(form, false, "Edit " + DisplayFriendly.getFriendlyClassName(entityClazz));
    }

    protected Window showForm(final DisplayFriendlyForm<T> form, final boolean isNew, String title) {
        if (null == form) {
            return null;
        }
        CommitAction ca = new CommitAction() {
            @Override
            public void run() {
                Notification.show("Item successfully submitted");
                if (isNew) {
                    // add the new item and save
                    UserUtil.addToCollection(repo, collectionOwner, form.getEntity());
                } else {
                    // save the user with the changed item
                    repo.save(collectionOwner);
                }
            }
        };
        return CrudUtil.showForm(form, ca, title);
    }
}