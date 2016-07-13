package com.medcognize.view.crud;

import com.medcognize.UserRepository;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.DisplayFriendlyForm;
import com.medcognize.util.CrudUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.event.Action;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.form.AbstractForm;

import java.util.ArrayList;

@Slf4j
public class CrudTable<T extends DisplayFriendly> extends EditTable<T> {

    protected final Action ACTION_ADD = new Action("Add");
    protected final Action ACTION_DELETE = new Action("Delete");

    public CrudTable(UserRepository repo, Class<T> entityClazz, final Class<? extends DisplayFriendlyForm<T>> formClazz,
                     ArrayList<String> orderedPidList) {
        super(repo, entityClazz, formClazz, orderedPidList);
        setEditOnSingleClick(false);
        // for CrudTable, enabled by default
        setContextMenuEnabled(true);
    }

    @Override
    protected void setActionHandler() {
        super.setActionHandler();
        addGeneratedColumns();
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        if (contextMenuEnabled) {
            return new Action[]{ACTION_ADD, ACTION_EDIT, ACTION_DELETE};
        }
        return new Action[0];
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (contextMenuEnabled) {
            if (ACTION_DELETE == action) {
                if (null == target) {
                    return;
                }
                deleteActionWithConfirm(target);
            } else if (ACTION_ADD == action) {
                addAction(defaultFormClazz);
            } else {
                super.handleAction(action, sender, target);
            }
        }
    }

    protected void addGeneratedColumns() {
        addGeneratedColumn("", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                HorizontalLayout cell = new HorizontalLayout();
                Button removeButton = new MButton(FontAwesome.TIMES_CIRCLE).withDescription("Delete");
                //removeButton.addStyleName("icon-cancel");
                removeButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        deleteActionWithConfirm(itemId);
                    }
                });
                Button editButton = new MButton(FontAwesome.EDIT).withDescription("Edit");
                // editButton.addStyleName("icon-edit");
                editButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        T item = getEntityFromContainer(itemId);
                        edit(item);
                    }
                });
                cell.setSpacing(true);
                cell.addComponent(removeButton);
                cell.addComponent(editButton);
                return cell;
            }
        });
    }

    protected void deleteActionWithConfirm(final Object target) {
        Window w = ConfirmDialog.show(this.getUI(), "Please Confirm:", "Area you really sure?", "YES", "No",
                new ConfirmDialog.Listener() {
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            deleteAction(target);
                        }
                    }
                });
        w.setClosable(false);
    }

    // Am not making deleting generic because it depends on the collection we are removing.  Originally
    // did this via reflection but got too cumbersome.
    protected void deleteAction(final Object target) {
        T df = getEntityFromContainer(target);
        removeItem(target);
        UserUtil.removeFromCollection(repo, collectionOwner, df);
        refreshRows();
    }

    protected Window addAction(final Class<? extends DisplayFriendlyForm<T>> formClazzToUse) {
        DisplayFriendlyForm<T> form = CrudUtil.getNewItemForm(entityClazz, formClazzToUse, repo);
        form.setModalWindowTitle("Add " + DisplayFriendly.getFriendlyClassName(entityClazz));
        Window w = form.openInModalPopup();
        form.setSavedHandler(new AbstractForm.SavedHandler<T>() {
            @Override
            public void onSave(T entity) {
                UserUtil.addToCollection(repo, collectionOwner, entity);
                addItem(entity);
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
}
