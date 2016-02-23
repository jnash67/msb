package com.medcognize.view.admin;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Sets;
import com.medcognize.UserService;
import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.MedicalExpense;
import com.medcognize.domain.Plan;
import com.medcognize.domain.Provider;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.util.export.CsvUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ManualCsvUploadTab extends VerticalLayout {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManualCsvUploadTab.class);
	private boolean hasHeaderRow = false;
	private boolean userCsv = false;
	private Class<? extends DisplayFriendly> csvClazz;
	private Table csvTable = new Table("CSV Data Table");
	private final TextArea requiredColumns = new TextArea("Required Columns", "");
	private final TextArea editor = new TextArea("Please enter your CSV data", "");
	final TextField emailField = new TextField("Email of User");
	final NativeSelect planField = new NativeSelect("Plan of MedicalExpense");
	User selectedUser = null;

	@Autowired
	UserService repo;

	public ManualCsvUploadTab() {
		super();
		setSpacing(true);
		setMargin(true);
		Label note = new Label("NOTE: If the CSV data does not have a header row, " + "the columns must be ordered" +
			" exactly like the required columns.");
		addComponent(note);

		HorizontalLayout csvEntry = new HorizontalLayout();
		csvEntry.setSpacing(true);
		csvEntry.setMargin(true);

		VerticalLayout editorLayout = new VerticalLayout();
		editor.setRows(5);
		editor.setColumns(20);
		editor.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				String text = editor.getValue();
				if (text != null) {
					if (!"".equals(text)) {
						buildTable(text);
					}
				}
			}
		});
		editor.setImmediate(true);
		requiredColumns.setRows(2);
		requiredColumns.setColumns(20);
		requiredColumns.setImmediate(true);
		requiredColumns.setReadOnly(true);

		VerticalLayout buttons = new VerticalLayout();
		buttons.setSpacing(true);
		buttons.setMargin(true);
		buttons.setSizeUndefined();

		// the TextArea is immediate, and its valueChange updates the Label,
		// so this button actually does nothing
		Button validateButton = new Button("Validate");
		final CheckBox headerCheckBox = new CheckBox("Header Row");
		final CheckBox isUserClassCheckBox = new CheckBox("User CSV");
		emailField.addValidator(new EmailValidator("Invalid email address"));
		emailField.setDescription("Please enter the email address (i.e. the username) of the user");
		emailField.setImmediate(true);
		planField.setNullSelectionAllowed(false);
		planField.setDescription("Please specify the plan the expenses belong to");
		csvTable.setPageLength(10);

		final OptionGroup nonUserClassTypes = new OptionGroup("Select Type of non-User CSV Data");
		nonUserClassTypes.addItem(Plan.class);
		nonUserClassTypes.setItemCaption(Plan.class, DisplayFriendly.getFriendlyClassName(Plan.class));
		nonUserClassTypes.addItem(FamilyMember.class);
		nonUserClassTypes.setItemCaption(FamilyMember.class, DisplayFriendly.getFriendlyClassName(FamilyMember.class));
		nonUserClassTypes.addItem(Provider.class);
		nonUserClassTypes.setItemCaption(Provider.class, DisplayFriendly.getFriendlyClassName(Provider.class));
		nonUserClassTypes.addItem(MedicalExpense.class);
		nonUserClassTypes.setItemCaption(MedicalExpense.class, DisplayFriendly.getFriendlyClassName(MedicalExpense
			.class));

		nonUserClassTypes.setImmediate(true);
		nonUserClassTypes.setMultiSelect(false);
		nonUserClassTypes.setValue(Plan.class);
		nonUserClassTypes.setNullSelectionAllowed(false);
		nonUserClassTypes.setEnabled(false);
		emailField.setEnabled(false);
		planField.setEnabled(false);
		isUserClassCheckBox.setValue(true);
		setHeadersArea(User.class);
		nonUserClassTypes.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				//noinspection unchecked
				setHeadersArea((Class<? extends DisplayFriendly>) nonUserClassTypes.getValue());
				if (MedicalExpense.class == csvClazz) {
					validateUser(true);
					planField.setEnabled(true);
				} else {
					planField.setEnabled(false);
				}
			}
		});
		final Button uploadButton = new Button("Upload to datastore");
		headerCheckBox.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				hasHeaderRow = headerCheckBox.getValue();
				uploadButton.setEnabled(hasHeaderRow);
				String text = editor.getValue();
				if (text != null) {
					if (!"".equals(text)) {
						buildTable(text);
					}
				}
			}
		});
		isUserClassCheckBox.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				userCsv = isUserClassCheckBox.getValue();
				if (userCsv) {
					nonUserClassTypes.setEnabled(false);
					emailField.setEnabled(false);
					planField.setEnabled(false);
					setHeadersArea(User.class);
				} else {
					nonUserClassTypes.setEnabled(true);
					emailField.setEnabled(true);
					//noinspection unchecked
					setHeadersArea((Class<? extends DisplayFriendly>) nonUserClassTypes.getValue());
					if (MedicalExpense.class == csvClazz) {
						planField.setEnabled(true);
					} else {
						planField.setEnabled(false);
					}
				}
			}
		});
		validateButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				String text = editor.getValue();
				if ((text != null) && (!"".equals(text))) {
					validate(true);
				} else {
					Notification.show("No data provided");
				}
			}
		});
		uploadButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				String text = editor.getValue();
				if (text != null) {
					if (!"".equals(text)) {
						if (validate(false)) {
							// validation passed, do the upload to datastore
							if (!hasHeaderRow) {
								text = requiredColumns.getValue() + "\n" + text;
							}
							if (User.class == csvClazz) {
								List<User> users = CsvUtil.csvWithHeaderToDisplayFriendlyList(User.class, text, null);
								// create new users through UserService to save them
								// repo.save(users);
							} else {
								List<? extends DisplayFriendly> dfs =
										CsvUtil.csvWithHeaderToDisplayFriendlyList(csvClazz, text, selectedUser);
								// DbUtil.savePossiblyExistingDisplayFriendly(selectedUser, dfs);
							}
						} else {
							Notification.show("Failed to validate!", Notification.Type.WARNING_MESSAGE);
						}
					}
				}
			}
		});
		editorLayout.addComponent(requiredColumns);
		editorLayout.addComponent(editor);
		buttons.addComponent(validateButton);
		buttons.addComponent(headerCheckBox);
		buttons.addComponent(isUserClassCheckBox);
		buttons.addComponent(nonUserClassTypes);
		buttons.addComponent(emailField);
		buttons.addComponent(planField);
		buttons.addComponent(uploadButton);
		csvEntry.addComponent(editorLayout);
		csvEntry.addComponent(buttons);
		csvEntry.addComponent(csvTable);

		addComponent(csvEntry);
	}

	private boolean validate(final boolean showNotifications) {
		boolean b = validateData(showNotifications);
		if (!b) {
			return false;
		}
		if (User.class == csvClazz) {
			return true;
		}
		b = validateUser(showNotifications);
		if (!b) {
			return false;
		}
		if (MedicalExpense.class == csvClazz) {
			Plan p = (Plan) planField.getValue();
			if (planField.getItemIds().size() == 0) {
				if (showNotifications) {
					Notification.show("The specified User has no plans to add expenses to");
				}
				return false;
			}
			if (null == p) {
				if (showNotifications) {
					Notification.show("No Plan for the User specified");
				}
				return false;
			}
			if (selectedUser.getRepo().getAll(selectedUser, Plan.class).contains(p)) {
				return true;
			}
			LOGGER.warn("This should never happen.  The only way a non-null Plan can show up is from the User.");
			if (showNotifications) {
				Notification.show("The specified Plan does not belong to the user");
			}
			return false;
		} else {
			return true;
		}
	}

	private boolean validateUser(final boolean showNotifications) {
		try {
			emailField.validate();
		} catch (Validator.InvalidValueException ive) {
			if (showNotifications) {
				Notification.show("Invalid User email address");
			}
			return false;
		}
		if (null != selectedUser) {
			// no need to load again
			if (emailField.getValue().equalsIgnoreCase(selectedUser.getUsername())) {
				return true;
			}
		}
		selectedUser = repo.getUserByUsername(emailField.getValue());
		if (null == selectedUser) {
			if (showNotifications) {
				Notification.show("No User found for the specified email address");
			}
			return false;
		}
		// load planField just in case
		planField.removeAllItems();
		Plan first = null;
		for (final Iterator<Plan> i = selectedUser.getRepo().getAll(selectedUser, Plan.class).iterator(); i.hasNext(); ) {
			if (null == first) {
				first = i.next();
				planField.addItem(first);
			} else {
				planField.addItem(i.next());
			}
		}
		if (planField.getItemIds().size() > 0) {
			planField.setValue(first);
		}
		return true;
	}

	private boolean validateData(final boolean showNotifications) {
		@SuppressWarnings("unchecked") Collection<String> tablePids = (Collection<String>) csvTable.getContainerPropertyIds();
		Collection<String> requiredPids = DisplayFriendly.propertyIdList(csvClazz);

		// make sure no duplicate columns
		Set<String> requiredPidsSet = Sets.newHashSet(requiredPids);
		Set<String> tablePidsSet = Sets.newHashSet(tablePids);
		if (tablePids.size() != tablePidsSet.size()) {
			if (showNotifications) {
				Notification.show("Table has duplicate column headers.  Must fix.", Notification.Type.WARNING_MESSAGE);
			}
			return false;
		}
		if (requiredPids.size() != requiredPidsSet.size()) {
			LOGGER.warn("Required Columns list has duplicates.  This is not possible.  Serious error");
			if (showNotifications) {
				Notification.show("Required Columns list has duplicates.  This is not possible.  Serious " + "error",
					Notification.Type.ERROR_MESSAGE);
			}
		}

		if (requiredPidsSet.size() == tablePidsSet.size()) {
			if (hasHeaderRow) {
				if (CollectionUtils.isEqualCollection(requiredPidsSet, tablePidsSet)) {
					if (showNotifications) {
						Notification.show("Validated!", Notification.Type.HUMANIZED_MESSAGE);
					}
					return true;
				} else {
					if (showNotifications) {
						Notification.show("Some column headers do not match", Notification.Type.WARNING_MESSAGE);
					}
					return false;
				}
			} else {
				if (showNotifications) {
					Notification.show("Without column headers cannot properly validate.  However, " +
							"" + "number of columns match. Be very careful.  Validated!!",
						Notification.Type.HUMANIZED_MESSAGE
					);
				}
				return true;
			}
		}

		if (tablePidsSet.size() > requiredPidsSet.size()) {
			if (!hasHeaderRow) {
				if (showNotifications) {
					Notification.show("Without headers, CSV data must have exactly the same number of columns" +
						" as " + "required. It has too many.", Notification.Type.WARNING_MESSAGE);
				}
				return false;
			}
			Set<String> missing = Sets.difference(requiredPidsSet, tablePidsSet);
			if (0 == missing.size()) {
				if (showNotifications) {
					Set<String> extra = Sets.difference(tablePidsSet, requiredPidsSet);
					Notification.show("CSV data has extra columns (" + extra.toString() + ") that will be " +
						"ignored. " +
						"Validated!", Notification.Type.HUMANIZED_MESSAGE);
				}
				return true;
			}
			if (showNotifications) {
				Notification.show("CSV data is missing the following columns: " + missing.toString(),
					Notification.Type.WARNING_MESSAGE);
			}
			return false;
		}

		// we know tablePidsSet.size() < requiredPidsSet.size() here
		if (!hasHeaderRow) {
			if (showNotifications) {
				Notification.show("Without headers, CSV data must have exactly the same number of columns as " +
					"" + "required. It has too many.", Notification.Type.WARNING_MESSAGE);
			}
			return false;
		}

		Set<String> missing = Sets.difference(requiredPidsSet, tablePidsSet);
		Set<String> extra = Sets.difference(tablePidsSet, requiredPidsSet);
		if (0 == missing.size()) {
			if (showNotifications) {
				Notification.show("CSV data has extra columns that will be ignored. Validated!",
					Notification.Type.HUMANIZED_MESSAGE);
			}
			return true;
		}
		if (showNotifications) {
			Notification.show("CSV data is missing some columns (" + missing.toString() + ") and has some " +
				"extraneous " +
				"columns (" + extra.toString() + ")", Notification.Type.WARNING_MESSAGE);
		}
		return false;
	}

	private void setHeadersArea(final Class<? extends DisplayFriendly> clazz) {
		csvClazz = clazz;
		if (null == csvClazz) {
			return;
		}
		String req = "";
		for (String pid : DisplayFriendly.propertyIdList(csvClazz)) {
			req = req + pid + ",";
		}
		requiredColumns.setReadOnly(false);
		requiredColumns.setValue(req.substring(0, req.length() - 1));
		requiredColumns.setReadOnly(true);
	}

	private void buildTable(final String text) {
		try {
			Reader r = new StringReader(text);
			IndexedContainer indexedContainer = buildContainerFromCSV(r);
			r.close();
			csvTable.setContainerDataSource(indexedContainer);
		} catch (final java.io.IOException e) {
			new Notification("Could not read entered text<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE).show
				(Page.getCurrent());
		}
	}

	// This stuff is from: https://gist.github.com/canthony/3655917

	/**
	 * Uses http://opencsv.sourceforge.net/ to read the entire contents of a CSV
	 * file, and creates an IndexedContainer from it
	 *
	 * @param reader
	 * @return
	 * @throws java.io.IOException
	 */
	@SuppressWarnings("JavaDoc")
	protected IndexedContainer buildContainerFromCSV(Reader reader) throws IOException {
		IndexedContainer container = new IndexedContainer();
		CSVReader csvReader = new CSVReader(reader);
		String[] columnHeaders = null;
		String[] record;
		int rowCount = 0;
		while ((record = csvReader.readNext()) != null) {
			if (0 == rowCount) {
				if (hasHeaderRow) {
					columnHeaders = record;
					addItemProperties(container, columnHeaders);
				} else {
					int cols = record.length;
					@SuppressWarnings("MismatchedQueryAndUpdateOfCollection") ArrayList<String> al = new ArrayList<>();
					for (int i = 1; i <= cols; i++) {
						al.add("Col" + i);
					}
					columnHeaders = new String[cols];
					al.toArray(columnHeaders);
					addItemProperties(container, columnHeaders);
					addItem(container, columnHeaders, record);
				}
			} else {
				addItem(container, columnHeaders, record);
			}
			rowCount++;
		}
		return container;
	}

	/**
	 * Set's up the item property ids for the container. Each is a String (of course,
	 * you can create whatever data type you like, but I guess you need to parse the whole file
	 * to work it out)
	 *
	 * @param container     The container to set
	 * @param columnHeaders The column headers, i.e. the first row from the CSV file
	 */
	private static void addItemProperties(IndexedContainer container, String[] columnHeaders) {
		for (String propertyName : columnHeaders) {
			container.addContainerProperty(propertyName, String.class, null);
		}
	}

	/**
	 * Adds an item to the given container, assuming each field maps to it's corresponding property id.
	 * Again, note that I am assuming that the field is a string.
	 *
	 * @param container
	 * @param propertyIds
	 * @param fields
	 */
	@SuppressWarnings("JavaDoc")
	private static void addItem(IndexedContainer container, String[] propertyIds, String[] fields) {
		if (propertyIds.length != fields.length) {
			throw new IllegalArgumentException("Hmmm - Different number of columns to fields in the record");
		}
		Object itemId = container.addItem();
		Item item = container.getItem(itemId);
		for (int i = 0; i < fields.length; i++) {
			String propertyId = propertyIds[i];
			String field = fields[i];
			//noinspection unchecked
			item.getItemProperty(propertyId).setValue(field);
		}
	}

}
