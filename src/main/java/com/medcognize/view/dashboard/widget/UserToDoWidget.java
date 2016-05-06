package com.medcognize.view.dashboard.widget;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.Plan;
import com.medcognize.domain.Provider;
import com.medcognize.domain.User;
import com.medcognize.view.dashboard.DashboardView;

public class UserToDoWidget extends ToDoWidget {
    User u;

    public UserToDoWidget(final User u, final String title, final DashboardView view) {
        super(title, null, view);
        this.u = u;
        setToDos();
    }

    public void setToDos() {
        if (null == u) {
            return;
        }
        clearToDos();
        int numPlans = u.getRepo().getAll(u, Plan.class).size();
        int numFamilyMembers = u.getRepo().getAll(u, FamilyMember.class).size();
        int numProviders = u.getRepo().getAll(u, Provider.class).size();
        if (numPlans == 0 && numFamilyMembers == 0 && numProviders == 0) {
            addToDo("You should have at least one plan, one family member, and one health care provider");
        } else if (numPlans == 0 && numFamilyMembers == 0) {
            addToDo("You should have at least one plan and one family member");
        } else if (numPlans == 0 && numProviders == 0) {
            addToDo("You should have at least one plan and one provider");
        } else if (numFamilyMembers == 0 && numProviders == 0) {
            addToDo("You should have at least one family member and one provider");
        } else if (numFamilyMembers == 0) {
            addToDo("You should have at least one family member");
        } else if (numProviders == 0) {
            addToDo("You should have at least one provider");
        } else if (numPlans == 0) {
            addToDo("You should have at least one plan");
        }

        if ((null == u.getFirstName()) || ("".equals(u.getFirstName()))) {
            addToDo("Set your real name.  Go to the little cog icon at bottom left.");
        }
        addToDo("Submit your expenses to your plan and your FSA");
    }

    @Override
    public void refresh() {
        setToDos();
        markAsDirty();
    }
}