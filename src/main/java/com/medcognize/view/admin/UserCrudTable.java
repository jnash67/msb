package com.medcognize.view.admin;

import com.medcognize.UserRepository;
import com.medcognize.domain.User;
import com.medcognize.form.DisplayFriendlyForm;
import com.medcognize.view.crud.CrudTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;


public class UserCrudTable extends CrudTable<User> {

    private UserRepository repo;

    private DisplayFriendlyForm<User> uf = null;

    @Autowired
    public UserCrudTable(UserRepository repo, Class<User> entityClazz, Class<? extends DisplayFriendlyForm<User>> formClazz,
                         ArrayList<String> orderedPidList) {
        super(repo, entityClazz, formClazz, orderedPidList);
        this.repo = repo;
    }

    @Override
    protected void deleteActionWithConfirm(final Object target) {
        ConfirmDialog.show(this.getUI(), "Please Confirm:", "Area you really sure? All the plans, providers, " +
                        "" + "family members and expenses will also be deleted!", "YES", "No",
                new ConfirmDialog.Listener() {
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            User user = getEntityFromContainer(target);
                            removeItem(target);
                            repo.delete(user);
                            refreshRows();
                        }
                    }
                }
        );
    }
}