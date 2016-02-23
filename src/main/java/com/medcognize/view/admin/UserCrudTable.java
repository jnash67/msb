package com.medcognize.view.admin;

import com.medcognize.UserService;
import com.medcognize.domain.User;
import com.medcognize.form.DisplayFriendlyForm;
import com.medcognize.view.crud.CrudTable;
import com.vaadin.data.util.BeanItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;


public class UserCrudTable extends CrudTable<User> {

    private UserService repo;

    private DisplayFriendlyForm<User> uf = null;

    @Autowired
    public UserCrudTable(UserService repo, Class<User> entityClazz, Class<? extends DisplayFriendlyForm<User>> formClazz,
                         ArrayList<String> orderedPidList) {
        super(entityClazz, formClazz, orderedPidList);
        this.repo = repo;
    }

    @Override
    protected void deleteItem(final Object target) {
        ConfirmDialog.show(this.getUI(), "Please Confirm:", "Area you really sure? All the plans, providers, " +
                        "" + "family members and expenses will also be deleted!", "YES", "No",
                new ConfirmDialog.Listener() {
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            BeanItem<User> bi = getData().getItem(target);
                            removeItem(target);
                            repo.deleteUser(bi.getBean());
                            refreshItems();
                        }
                    }
                }
        );
    }

    @Override
    protected void deleteAction(Object target) {
        // not called. Handled by deleteItem.
    }

    @Override
    protected void saveItem(final BeanItem<User> bi, final boolean isNew) {
        if (isNew) {
            // repo.save(bi.getBean());
        } else {
            // already exists. Overwrite(????)
        }
    }

    @Override
    protected DisplayFriendlyForm<User> createForm(Class<? extends DisplayFriendlyForm<User>> formClazzToUse,
                                                   BeanItem<User> bi, boolean isNew) {
        uf = super.createForm(formClazzToUse, bi, false);
        return uf;
    }
}