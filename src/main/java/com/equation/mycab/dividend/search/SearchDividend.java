package com.equation.mycab.dividend.search;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Date;

import com.equation.mycab.partners.PartnersCollection;
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
 *
 * @author Wellington
 */

@SuppressWarnings("serial")
public class SearchDividend extends CustomComponent {

	ResultSet rs, rs1;
	Statement stm, stmt;
	PartnersCollection collection;

	public SearchDividend(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;

		collection = new PartnersCollection(stm, stmt, rs, rs1);

		TabSheet sheet = new TabSheet();

		sheet.addTab(singleDate(), "Single Date", VaadinIcons.CALENDAR_CLOCK);
		sheet.addTab(dateRange(), "Date Range", VaadinIcons.CALENDAR_ENVELOPE);
		sheet.addTab(partnerName(), "Partner Name", VaadinIcons.CALENDAR_USER);

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

		Button search = new Button("Search");
		search.addStyleName(ValoTheme.BUTTON_PRIMARY);
		search.setIcon(VaadinIcons.SEARCH);
		search.addClickListener((e) -> {
			String datePosted = String.format("%1$tY-%1$tm-%1$td", joinDate.getValue());
			if (!datePosted.equals("")) {
				SingleDateDividend dateSearch = new SingleDateDividend(rs, rs1, stm, stmt);
				dateSearch.searchData(datePosted);
			} else {
				new Notification(
						"<h1 style='color:white;'>Date field is empty!<br>You need to supply the date in order to search a record.</h1>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			}

		});

		FormLayout formLayout = new FormLayout(joinDate, search);

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

		Button search = new Button("Search");
		search.addStyleName(ValoTheme.BUTTON_PRIMARY);
		search.setIcon(VaadinIcons.SEARCH);
		search.addClickListener((e) -> {
			String fromm = String.format("%1$tY-%1$tm-%1$td", from.getValue());
			String too = String.format("%1$tY-%1$tm-%1$td", to.getValue());

			if (!(fromm.equals("") || too.equals(""))) {
				DateRangeDividend dateRangeSearch = new DateRangeDividend(rs, rs1, stm, stmt);
				dateRangeSearch.searchData(fromm, too);

			} else {
				new Notification(
						"<h1 style='color:white;'>An empty date field has been detected.!<br>You need to supply the date range in order to search a record.</h1>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());

			}

		});

		FormLayout formLayout = new FormLayout(from, to, search);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);

		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}

	private VerticalLayout partnerName() {
		VerticalLayout layout = new VerticalLayout();

		ComboBox<String> partners = new ComboBox<String>("<h3>Select Partner</h3>");
		partners.setRequiredIndicatorVisible(true);
		partners.setCaptionAsHtml(true);
		partners.setPlaceholder("Choose...");
		partners.setEmptySelectionAllowed(false);
		partners.setItems(collection.getPartnernames());

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

		Button search = new Button("Search");
		search.addStyleName(ValoTheme.BUTTON_PRIMARY);
		search.setIcon(VaadinIcons.SEARCH);
		search.addClickListener((e) -> {
			String fromm = String.format("%1$tY-%1$tm-%1$td", from.getValue());
			String too = String.format("%1$tY-%1$tm-%1$td", to.getValue());
			String drivername = partners.getValue();
			if (drivername != null && !drivername.isEmpty()) {
				if (!(fromm.equals("") || too.equals(""))) {
					PartnerNameDividendSearch driverNameSearch = new PartnerNameDividendSearch(rs, rs1, stm, stmt);
					driverNameSearch.searchData(fromm, too, drivername);
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

		FormLayout formLayout = new FormLayout(partners, from, to, search);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);

		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}

}
