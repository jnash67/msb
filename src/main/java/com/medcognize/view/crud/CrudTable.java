package com.medcognize.view.crud;

import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.basic.DisplayFriendlyCollectionOwner;
import com.medcognize.form.DisplayFriendlyForm;
import com.medcognize.util.CrudUtil;
import com.medcognize.util.IcoMoon;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Action;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;

public class CrudTable<T extends DisplayFriendly> extends EditTable<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrudTable.class);
    protected final Action ACTION_ADD = new Action("Add");
    protected final Action ACTION_DELETE = new Action("Delete");

    public CrudTable(Class<T> entityClazz, final Class<? extends DisplayFriendlyForm<T>> formClazz,
                     ArrayList<String> orderedPidList) {
        super(entityClazz, formClazz, orderedPidList);
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
                deleteItem(target);
            } else if (ACTION_ADD == action) {
                getNewItemFormAndShow(defaultFormClazz);
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
                Button removeButton = new Button();
                removeButton.setIcon(IcoMoon.CANCEL);
                //removeButton.addStyleName("icon-cancel");
                removeButton.setDescription("Delete");
                removeButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        deleteItem(itemId);
                    }
                });
                Button editButton = new Button();
                editButton.setIcon(IcoMoon.EDIT);
                // editButton.addStyleName("icon-edit");
                editButton.setDescription("Edit");
                editButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        getEditItemFormAndShow(itemId);
                    }
                });
                cell.setSpacing(true);
                cell.addComponent(removeButton);
                cell.addComponent(editButton);
                return cell;
            }
        });
    }

    protected void deleteItem(final Object target) {
        Window w = ConfirmDialog.show(this.getUI(), "Please Confirm:", "Area you really sure?", "YES", "No",
                new ConfirmDialog.Listener() {
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            if (DisplayFriendlyCollectionOwner.class.isAssignableFrom(entityClazz)) {
                                LOGGER.warn("You probably want to override method deleteItem for " + entityClazz
                                        .getSimpleName() + " to properly handle the deletion of items it is the owner" +
                                        " of. " +
                                        "It is a DisplayFriendlyCollectionOwner.");
                            }
                            deleteAction(target);
                        }
                    }
                });
        w.setClosable(false);
    }

    protected void deleteAction(final Object target) {
        BeanItem<T> bi = getData().getItem(target);
        removeItem(target);
        if (null == collectionOwner) {
            LOGGER.warn("If no collection owner, this method should be overridden which UserEditView " + "does, " +
                    "and it is the only entity with no owner");
        } else {
            collectionOwner.remove(bi.getBean());
        }
        refreshItems();
    }

    protected Window getNewItemFormAndShow(final Class<? extends DisplayFriendlyForm<T>> formClazzToUse) {
        DisplayFriendlyForm<T> form = CrudUtil.getNewItemForm(entityClazz, formClazzToUse);
        return showForm(form, true, "Add " + DisplayFriendly.getFriendlyClassName(entityClazz));
    }

}
