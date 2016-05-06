package com.medcognize.view.crud;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.User;
import com.medcognize.domain.FamilyMember;
import com.medcognize.form.FamilyMemberForm;
import com.vaadin.navigator.ViewChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class FamilyMemberView extends CrudView<FamilyMember> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FamilyMemberView.class);

    public FamilyMemberView() {
        super(FamilyMember.class, FamilyMemberForm.class, null, "Family Members");
        User owner = ((MedcognizeUI) MedcognizeUI.getCurrent()).getUser();
        if (null == owner) {
            LOGGER.error("owner should not be null here");
            return;
        }
        Collection<FamilyMember> members = owner.getAll(FamilyMember.class);
        setData(members, owner);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
	    if (showHeader) {
		    toolbar.showAddNewButton();
	    }
    }
}
