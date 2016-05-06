package com.medcognize.view.crud;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.User;
import com.medcognize.domain.Provider;
import com.medcognize.form.ProviderForm;
import com.vaadin.navigator.ViewChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

public class ProviderView extends CrudView<Provider> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProviderView.class);

	static final ArrayList<String> pids = new ArrayList<String>() {
		{
			add("providerName");
			add("providerType");
			add("providerInPlan");
		}
	};

	public ProviderView() {
		super(Provider.class, ProviderForm.class, pids, "Providers");
		User owner = ((MedcognizeUI) MedcognizeUI.getCurrent()).getUser();
		if (null == owner) {
			LOGGER.error("owner should not be null here");
			return;
		}
		Collection<Provider> providers = owner.getAll(Provider.class);
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
