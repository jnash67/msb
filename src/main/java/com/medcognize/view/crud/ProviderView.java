package com.medcognize.view.crud;

import com.medcognize.UserRepository;
import com.medcognize.domain.Provider;
import com.medcognize.domain.User;
import com.medcognize.form.ProviderForm;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@SpringView(name = ProviderView.NAME)
public class ProviderView extends CrudView<Provider> {
	public static final String NAME = "provider";
	private final UserRepository repo;
	static final ArrayList<String> pids = new ArrayList<String>() {
		{
			add("providerName");
			add("providerType");
			add("providerInPlan");
		}
	};

	@Autowired
	public ProviderView(UserRepository repo) {
		super(Provider.class, "Providers", new CrudTable<Provider>(repo, Provider.class, ProviderForm.class, pids));
		this.repo = repo;
		User owner = DbUtil.getLoggedInUser();
		if (null == owner) {
			log.error("owner should not be null here");
			return;
		}
		Collection<Provider> providers = UserUtil.getAll(owner, Provider.class);
		setData(providers, owner);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		super.enter(event);
		if (showHeader) {
			toolbar.showAddNewButton();
		}
	}
}
