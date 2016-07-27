package com.medcognize.view.admin;

import com.medcognize.UserRepository;
import com.medcognize.domain.User;
import com.medcognize.form.AdminUserSettingsForm;
import com.medcognize.util.DbUtil;
import com.medcognize.view.crud.CrudView;
import com.vaadin.navigator.ViewChangeListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class UserEditAdminView extends CrudView<User> {
    static final ArrayList<String> pids = new ArrayList<String>() {
        {
            add("username");
            add("firstName");
            add("lastName");
        }
    };

    public UserEditAdminView(UserRepository repo) {
        super(User.class, "Users", new UserCrudTable(repo, User.class, AdminUserSettingsForm.class, pids));
        User owner = DbUtil.getLoggedInUser();
        if (null == owner) {
            log.error("owner should not be null here");
            return;
        }
        Collection<User> users = repo.findAll();
        setData(users);
        // we extend a View here to get the functionality but we don't use it as a view
        // therefore enter won't be called automatically so we must do it manually
        enter(null);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
        if (showHeader) {
            toolbar.showAddNewButton();
        }
    }
}
