package com.equation.mycab.driver;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Date;

import com.equation.mycab.currency.Currency;
import com.equation.mycab.utils.date.DateUtility;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Wellington
 *
 */
@SuppressWarnings("serial")
public class SearchDriverTransactions extends CustomComponent {

	ResultSet rs, rs1;
	Statement stm, stmt;
	DriversCollection collection;

	public SearchDriverTransactions(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;

		collection = new DriversCollection(stm, stmt, rs, rs1);

		TabSheet sheet = new TabSheet();

		sheet.addTab(singleDate(), "Single Date", VaadinIcons.CALENDAR_CLOCK);
		sheet.addTab(dateRange(), "Date Range", VaadinIcons.CALENDAR_ENVELOPE);
		sheet.addTab(driverName(), "Driver Name", VaadinIcons.CALENDAR_USER);

		HorizontalLayout horizontalLayout = new HorizontalLayout(sheet);
		VerticalLayout layout = new VerticalLayout(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		this.setCompositionRoot(layout);
	}

	private VerticalLayout singleDate() {
		VerticalLayout layout = new VerticalLayout();

		DateField joinDate = new DateField("<h3>Select Date</h3>");
		joinDate.setCaptionAsHtml(true);
		joinDate.setRequiredIndicatorVisible(true);
		joinDate.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		joinDate.setDateFormat("yyyy-MM-dd");
		joinDate.setRequiredIndicatorVisible(true);

		ComboBox<String> currency = new ComboBox<>("<h3>Currency</h3>");
		currency.setCaptionAsHtml(true);
		currency.setPlaceholder("Choose...");
		currency.setDescription("Select Currency");
		currency.setItems(new Currency(rs, rs1, stm, stmt).getCurrency());
		currency.setEmptySelectionAllowed(false);

		Button search = new Button("Search");
		search.addStyleName(ValoTheme.BUTTON_PRIMARY);
		search.setIcon(VaadinIcons.SEARCH);
		search.addClickListener((e) -> {
			String datePosted = String.format("%1$tY-%1$tm-%1$td", joinDate.getValue());
			String currency1 = currency.getValue();
			if (!datePosted.equals("")) {
				if (currency1 != null && !currency1.isEmpty()) {
					SingleDateSearch dateSearch = new SingleDateSearch(rs, rs1, stm, stmt);
					dateSearch.searchData(datePosted, currency1);
				} else {
					new Notification(
							"<h1 style='color:white;'>Currency field is empty!<br>You need to supply the currency in order to search a record.</h1>",
							"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
					currency.focus();
				}

			} else {
				new Notification(
						"<h1 style='color:white;'>Date field is empty!<br>You need to supply the date in order to search a record.</h1>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				joinDate.focus();
			}

		});

		FormLayout formLayout = new FormLayout(joinDate, currency, search);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);

		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		return layout;
	}

	private VerticalLayout dateRange() {
		VerticalLayout layout = new VerticalLayout();

		DateField from = new DateField("<h3>Beginning From</h3>");
		from.setCaptionAsHtml(true);
		from.setRequiredIndicatorVisible(true);
		from.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		from.setDateFormat("yyyy-MM-dd");
		from.setRequiredIndicatorVisible(true);

		DateField to = new DateField("<h3>Ending on</h3>");
		to.setCaptionAsHtml(true);
		to.setRequiredIndicatorVisible(true);
		to.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		to.setDateFormat("yyyy-MM-dd");
		to.setRequiredIndicatorVisible(true);

		ComboBox<String> currency = new ComboBox<>("<h3>Currency</h3>");
		currency.setCaptionAsHtml(true);
		currency.setPlaceholder("Choose...");
		currency.setDescription("Select Currency");
		currency.setItems(new Currency(rs, rs1, stm, stmt).getCurrency());
		currency.setEmptySelectionAllowed(false);

		Button search = new Button("Search");
		search.addStyleName(ValoTheme.BUTTON_PRIMARY);
		search.setIcon(VaadinIcons.SEARCH);
		search.addClickListener((e) -> {
			String fromm = String.format("%1$tY-%1$tm-%1$td", from.getValue());
			String too = String.format("%1$tY-%1$tm-%1$td", to.getValue());
			String currency1 = currency.getValue();
			if (!(fromm.equals("") || too.equals(""))) {
				if (currency1 != null && !currency1.isEmpty()) {
					DateRangeSearch dateRangeSearch = new DateRangeSearch(rs, rs1, stm, stmt);
					dateRangeSearch.searchData(fromm, too, currency1);

				} else {
					new Notification(
							"<h1 style='color:white;'>An empty currency field has been detected.!<br>You need to supply the currency in order to search a record.</h1>",
							"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				}

			} else {
				new Notification(
						"<h1 style='color:white;'>An empty date field has been detected.!<br>You need to supply the date range in order to search a record.</h1>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());

			}

		});

		FormLayout formLayout = new FormLayout(from, to, currency, search);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);

		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}

	private VerticalLayout driverName() {
		VerticalLayout layout = new VerticalLayout();

		ComboBox<String> drivers = new ComboBox<String>("<h3>Select Driver</h3>");
		drivers.setRequiredIndicatorVisible(true);
		drivers.setCaptionAsHtml(true);
		drivers.setPlaceholder("Choose...");
		drivers.setEmptySelectionAllowed(false);
		drivers.setItems(collection.getDrivernames());

		DateField from = new DateField("<h3>Beginning From</h3>");
		from.setCaptionAsHtml(true);
		from.setRequiredIndicatorVisible(true);
		from.setValue(new DateUtility().firstOfJanuary().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		from.setDateFormat("yyyy-MM-dd");
		from.setRequiredIndicatorVisible(true);

		DateField to = new DateField("<h3>Ending on</h3>");
		to.setCaptionAsHtml(true);
		to.setRequiredIndicatorVisible(true);
		to.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		to.setDateFormat("yyyy-MM-dd");
		to.setRequiredIndicatorVisible(true);

		ComboBox<String> currency = new ComboBox<>("<h3>Currency</h3>");
		currency.setCaptionAsHtml(true);
		currency.setPlaceholder("Choose...");
		currency.setDescription("Select Currency");
		currency.setItems(new Currency(rs, rs1, stm, stmt).getCurrency());
		currency.setEmptySelectionAllowed(false);

		Button search = new Button("Search");
		search.addStyleName(ValoTheme.BUTTON_PRIMARY);
		search.setIcon(VaadinIcons.SEARCH);
		search.addClickListener((e) -> {
			String fromm = String.format("%1$tY-%1$tm-%1$td", from.getValue());
			String too = String.format("%1$tY-%1$tm-%1$td", to.getValue());
			String drivername = drivers.getValue();
			String currency1 = currency.getValue();
			if (drivername != null && !drivername.isEmpty()) {
				if (!(fromm.equals("") || too.equals(""))) {
					if (currency1 != null && !currency1.isEmpty()) {
						DriverNameSearch driverNameSearch = new DriverNameSearch(rs, rs1, stm, stmt);
						driverNameSearch.searchData(fromm, too, drivername, currency1);

					} else {
						new Notification(
								"<h1 style='color:white;'>An empty currency field has been detected.!<br>You need to supply the currency in order to search a record.<h1/>",
								"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
					}

				} else {
					new Notification(
							"<h1 style='color:white;'>An empty date field has been detected.!<br>You need to supply the date range in order to search a record.<h1/>",
							"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				}

			} else {
				new Notification(
						"<h1 style='color:white;'>An empty Driver Name Detected.!<br>You need to supply the driver name in order to search a record.<h1/>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			}

		});

		FormLayout formLayout = new FormLayout(drivers, from, to, currency, search);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);

		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}

}
