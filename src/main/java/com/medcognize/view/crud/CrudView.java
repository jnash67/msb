package com.medcognize.view.crud;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.form.DisplayFriendlyForm;
import com.medcognize.view.ComponentView;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

public abstract class CrudView<T extends DisplayFriendly> extends VerticalLayout implements ComponentView {

	@SuppressWarnings("UnusedDeclaration")
	private static final Logger LOGGER = LoggerFactory.getLogger(CrudView.class);

	protected final CrudTable<T> table;
	protected final Class<T> entityClazz;
	protected CrudViewToolbar<T> toolbar;
	protected String header;
	protected boolean showHeader = true;
	protected boolean showFilter = true;

	// the list of propertyIds passed in should be ordered
	// In the constructor we should deal with data issues only
	// in the enter() method we should deal with appearance
//	public CrudView(final Class<T> entityClazz, final Class<? extends DisplayFriendlyForm<T>> formClazz,
//					ArrayList<String> tablePids, String header) {
//		this(entityClazz, header, new CrudTable<>(entityClazz, formClazz, tablePids));
//	}

	public CrudView(final Class<T> entityClazz, String header, CrudTable<T> table) {
		super();
		this.entityClazz = entityClazz;
		this.header = header;
		this.table = table;
		this.table.setLocale(MedcognizeUI.LOCALE);
	}

	@Override
	public void showHeader(boolean b) {
		showHeader = b;
	}

	@Override
	public void ShowFilter(boolean b) { showFilter = b; }

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		if (showHeader) {
			toolbar = new CrudViewToolbar<>(entityClazz, this, showFilter);
			toolbar.setHeader(header);
			toolbar.setPidsToFilterOn(table.getContainerPropertyIds());
			addComponent(toolbar);
		}
		setSizeFull();
		addStyleName("transactions");

		table.setSizeFull();
		table.addStyleName("borderless");
		table.setSelectable(true);
//		table.setColumnCollapsingAllowed(true);
//		table.setColumnReorderingAllowed(true);

		addComponent(table);
		setExpandRatio(table, 1);
		table.setImmediate(true);
	}

	public void setData(Collection<T> items, User collectionOwner) {
		table.setData(items, collectionOwner);
	}

	protected CrudTable<T> getTable() {
		return table;
	}

	protected BeanContainer<Long, T> getContainer() {
		return table.getData();
	}
}