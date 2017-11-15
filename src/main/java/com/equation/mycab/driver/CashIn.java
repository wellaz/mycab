package com.equation.mycab.driver;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Date;

import com.equation.mycab.currency.Currency;
import com.equation.mycab.driver.cashin.printable.MakeCashInReceipt;
import com.equation.mycab.driver.cashin.printable.ReceipNumber;
import com.equation.mycab.insert.InsertCashIn;
import com.equation.mycab.insert.InsertReceipt;
import com.equation.mycab.insert.InsertRevenue;
import com.equation.mycab.partners.Partner;
import com.equation.mycab.transactions.types.Type;
import com.equation.mycab.utils.currency.check.CheckValue;
import com.equation.mycab.utils.date.DateUtility;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CashIn extends CustomComponent {
	Statement stm, stmt;
	ResultSet rs, rs1;
	String id;

	public CashIn(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1, String id) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		this.id = id;
		this.setCompositionRoot(createlayout());
	}

	public VerticalLayout createlayout() {
		VerticalLayout layout = new VerticalLayout();

		ComboBox<String> drivers = new ComboBox<>("<h2>Select Driver:</h2>");
		drivers.setCaptionAsHtml(true);
		drivers.setIcon(VaadinIcons.USER);
		drivers.setEmptySelectionAllowed(false);
		drivers.setItems(new DriversCollection(stm, stmt, rs, rs1).getDrivernames());
		drivers.setPlaceholder("Choose...");
		drivers.setDescription("Select the driver name");

		TextField amount = new TextField("<h2>Enter Amount:</h2>");
		amount.setCaptionAsHtml(true);
		amount.setIcon(VaadinIcons.MONEY);

		amount.setPlaceholder("amount...");
		amount.setDescription("Enter the amount tendered");

		ComboBox<String> currency = new ComboBox<>("<h2>Currency:</h2>");
		currency.setCaptionAsHtml(true);
		currency.setIcon(VaadinIcons.CASH);
		currency.setEmptySelectionAllowed(false);
		currency.setItems(new Currency(rs, rs, stm, stmt).getCurrency());
		currency.setPlaceholder("Choose...");
		currency.setDescription("Select the currency");

		DateField dueDate = new DateField("<h2>Payment Date<h2/>");
		dueDate.setCaptionAsHtml(true);
		dueDate.setIcon(VaadinIcons.CALENDAR_CLOCK);
		dueDate.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		dueDate.setDateFormat("yyyy-MM-dd");
		dueDate.setRequiredIndicatorVisible(true);
		dueDate.setDescription("Specify the due date");

		HorizontalLayout cashlayout = new HorizontalLayout(amount, currency);

		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_LARGE);
		submit.setDescription("Submit this form");

		submit.addClickListener((e) -> {
			String drivername = drivers.getValue();
			String theamount = amount.getValue();
			String thecurrency = currency.getValue();

			if (thecurrency != null && !thecurrency.isEmpty()) {
				if (!theamount.equals("")) {
					if (drivername != null && !drivername.isEmpty()) {
						if (CheckValue.isMoney(theamount)) {
							DateUtility ut = new DateUtility();

							String datePosted = String.format("%1$tY-%1$tm-%1$td", dueDate.getValue());
							String timePosted = ut.getTime();

							// new InsertReceipt().inserrData(stm, theamount,
							// datePosted, timePosted, Type.CASHIN);
							DriversCollection collection = new DriversCollection(stm, stmt, rs, rs1);
							String driverid = (String) DriversCollection
									.getDriverId(collection.getDriverAndId(), drivername).toString();

							String receiver = new Partner(rs, rs1, stm, stmt).getFirstname(id).toUpperCase() + " "
									+ new Partner(rs, rs1, stm, stmt).getLasttname(id).substring(0, 1).toUpperCase();

							String vehicleName = new DriverVehicle(stm, stmt, rs, rs1)
									.getCarName(drivername.split(" ")[0], drivername.split(" ")[1]);

							UI.getCurrent().addWindow(confirmDialog(receiver, vehicleName, drivername, theamount,
									thecurrency, amount, datePosted, timePosted, driverid));

						} else {
							new Notification(
									"<h1>Correct the amount entered!<br>Probably a typing error could have been made!</h1>",
									"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
							amount.focus();

						}

					} else {
						// no driver
						new Notification(
								"<h1>No driver selected!<br>Specify the driver name who is doing cash in!</h1>", "",
								Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
						drivers.focus();
					}
				} else {
					// no amounnt
					new Notification("<h1>No amount entered!<br>Specify the cash in amount!</h1>", "",
							Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
					amount.focus();
				}
			} else {
				// select currency
				new Notification("<h1>Currency is not selected!<br>Specify the currency please!</h1>", "",
						Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				currency.focus();
			}

		});

		FormLayout formLayout = new FormLayout(drivers, dueDate, cashlayout, submit);
		formLayout.setCaption("<h2><b>Make a Cash In Record<b/><h2/>");
		formLayout.setCaptionAsHtml(true);
		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);

		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}

	private Window confirmDialog(String receiver, String vehicleName, String drivername, String theamount,
			String thecurrency, TextField amField, String datePosted, String timePosted, String driverId) {
		Window window = new Window("<h2>Confirm</h2>");
		window.setCaptionAsHtml(true);
		window.center();
		window.setModal(true);
		window.setWidth("100%");
		window.setHeight("80%");

		Label vehicle = new Label("<h2>Vehicle<h2/>", ContentMode.HTML);
		Label vehicle1 = new Label("<h2>" + vehicleName + "<h2/>", ContentMode.HTML);
		Label driver = new Label("<h2>Driver<h2/>", ContentMode.HTML);
		Label driver1 = new Label("<h2>" + drivername + "<h2/>", ContentMode.HTML);
		Label amount = new Label("<h2>Amount<h2/>", ContentMode.HTML);
		Label amount1 = new Label("<h2>" + theamount + "<h2/>", ContentMode.HTML);
		Label curr = new Label("<h2>Currency<h2/>", ContentMode.HTML);
		Label curr1 = new Label("<h2>" + thecurrency + "<h2/>", ContentMode.HTML);

		GridLayout gridLayout = new GridLayout(2, 4, vehicle, vehicle1, driver, driver1, amount, amount1, curr, curr1);

		HorizontalLayout horizontalLayout = new HorizontalLayout(gridLayout);

		Button print = new Button("Print");
		print.addStyleName(ValoTheme.BUTTON_PRIMARY);
		print.addStyleName(ValoTheme.BUTTON_LARGE);
		print.setIcon(VaadinIcons.PRINT);
		print.addClickListener((e) -> {
			UI.getCurrent().removeWindow(window);
			window.close();
			new InsertReceipt().inserrData(stm, theamount, datePosted, timePosted, Type.CASHIN);
			String receiptnumber = new ReceipNumber(stm, stmt, rs, rs1).newReceiptNumber();
			new MakeCashInReceipt().makeReceipt(receiver, receiptnumber, vehicleName, drivername, theamount,
					thecurrency);
			new InsertCashIn().insertData(stmt, driverId, theamount, thecurrency, datePosted, timePosted,
					receiptnumber);

			new InsertRevenue().insertCredit(stm, datePosted, timePosted, theamount, drivername + " " + Type.CASHIN);
			amField.clear();
			amField.focus();
		});

		Button cancel = new Button("Cancel");
		cancel.addStyleName(ValoTheme.BUTTON_DANGER);
		cancel.addStyleName(ValoTheme.BUTTON_LARGE);
		cancel.setIcon(VaadinIcons.CLOSE);
		cancel.addClickListener((e) -> {
			UI.getCurrent().removeWindow(window);
			window.close();
			amField.focus();
		});

		HorizontalLayout buttons = new HorizontalLayout(print, cancel);
		buttons.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

		VerticalLayout layout = new VerticalLayout(horizontalLayout, buttons);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);

		window.setContent(layout);

		return window;
	}
}