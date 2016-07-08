package com.medcognize.view.crud;

import com.medcognize.domain.basic.DisplayFriendly;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;

import java.util.Collection;

public class CrudViewToolbar<T extends DisplayFriendly> extends HorizontalLayout {
	final CrudView<T> view;
	final Label title;
	Collection<?> pidsToFilterOn;


	public CrudViewToolbar(final Class<T> entityClazz, final CrudView<T> view, final boolean filter) {
		this.view = view;
		setWidth("100%");
		setSpacing(true);
		setMargin(true);
		addStyleName("toolbar");

		title = new Label("");
		title.addStyleName("h1");
		title.setSizeUndefined();
		addComponent(title);
		setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		if (filter) {
			pidsToFilterOn = DisplayFriendly.propertyIdList(entityClazz);
			showFilter();
		}
	}

	public void setHeader(String header) {
		title.setValue(header);
	}

	public void setPidsToFilterOn(Collection<?> pids) {
		if (null != pids) {
			pidsToFilterOn = pids;
		}
	}

	public void showAddNewButton() {
		Button addNew = new Button("Add New");
		addNew.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				view.getTable().addAction(view.getTable().getDefaultFormClazz());
			}
		});
		addNew.addStyleName("small");
		addComponent(addNew);
		setComponentAlignment(addNew, Alignment.MIDDLE_LEFT);
	}

	protected void showFilter() {
		final TextField filter = new TextField();
		filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
			@Override
			public void textChange(final FieldEvents.TextChangeEvent event) {
				view.getContainer().removeAllContainerFilters();
				view.getContainer().addContainerFilter(new Container.Filter() {
					@Override
					public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
						//noinspection SimplifiableIfStatement
						if ((event.getText() == null) || (event.getText().equals(""))) {
							return true;
						}
						return filterByProperty(pidsToFilterOn, item, event.getText());
					}

					@Override
					public boolean appliesToProperty(Object propertyId) {
						return null != pidsToFilterOn && pidsToFilterOn.contains(propertyId.toString());
					}
				});
			}
		});
		filter.setInputPrompt("Filter");
		filter.addShortcutListener(new ShortcutListener("Clear", ShortcutAction.KeyCode.ESCAPE, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filter.setValue("");
				view.getContainer().removeAllContainerFilters();
			}
		});
		addComponent(filter);
		setExpandRatio(filter, 1);
		setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
	}

	protected boolean filterByProperty(Collection<?> props, Item item, String text) {
		if ((item == null) || (null == props)) {
			return false;
		}
		Property prop;
		Object propVal;
		String propValString;
		for (Object propString : props) {
			prop = item.getItemProperty(propString);
			if (null != prop) {
				propVal = prop.getValue();
				if (null != propVal) {
					propValString = propVal.toString().trim().toLowerCase();
					if (propValString.startsWith(text.toLowerCase().trim())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
