package com.medcognize.view.crud;

import com.medcognize.UserRepository;
import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.User;
import com.medcognize.form.FamilyMemberForm;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Slf4j
@SpringView(name = FamilyMemberView.NAME)
public class FamilyMemberView extends CrudView<FamilyMember> {
	public static final String NAME = "family";

	@Autowired
	public FamilyMemberView(UserRepository repo) {
		super(FamilyMember.class, "Family Members", new CrudTable<FamilyMember>(repo, FamilyMember.class, FamilyMemberForm.class, null));
		User owner = DbUtil.getLoggedInUser();
		if (null == owner) {
			log.error("owner should not be null here");
			return;
		}
		Collection<FamilyMember> members = UserUtil.getAll(owner, FamilyMember.class);
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
