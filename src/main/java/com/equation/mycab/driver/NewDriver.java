package com.equation.mycab.driver;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Date;

import com.equation.mycab.insert.InsertDriver;
import com.equation.mycab.vehicle.AllVehicles;
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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class NewDriver extends CustomComponent {

	Statement stm, stmt;
	ResultSet rs, rs1;
	AllVehicles vehicles;

	public NewDriver(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		vehicles = new AllVehicles(stm, stmt, rs, rs1);
		this.setCompositionRoot(createLayout());
	}

	private VerticalLayout createLayout() {
		VerticalLayout layout = new VerticalLayout();

		TextField firstName = new TextField("First Name:");
		firstName.setRequiredIndicatorVisible(true);
		firstName.setIcon(VaadinIcons.USER);
		firstName.setDescription("Enter the first name of the driver");
		firstName.setPlaceholder("First name");

		TextField lastName = new TextField("First Name:");
		lastName.setRequiredIndicatorVisible(true);
		lastName.setIcon(VaadinIcons.USER);
		lastName.setDescription("Enter the last name if the driver");
		lastName.setPlaceholder("Last Name");

		HorizontalLayout names = new HorizontalLayout(firstName, lastName);

		TextField nationalId = new TextField("National ID:");
		nationalId.setRequiredIndicatorVisible(true);
		nationalId.setIcon(VaadinIcons.USER_CARD);
		nationalId.setPlaceholder("eg. 75-9085-K-75");
		nationalId.setDescription("Enter the national Id for the driver");

		TextField emailAddress = new TextField("Email Address:");
		emailAddress.setRequiredIndicatorVisible(true);
		emailAddress.setIcon(VaadinIcons.ENVELOPE);
		emailAddress.setPlaceholder("driver@mycab.com");
		emailAddress.setDescription("Enter the email address forn the driver");

		TextField mobileNumber = new TextField("Mobile Number:");
		mobileNumber.setRequiredIndicatorVisible(true);
		mobileNumber.setIcon(VaadinIcons.PHONE);
		mobileNumber.setDescription("Enter the mobile number for the driver");
		mobileNumber.setPlaceholder("eg. 0864495612");

		ComboBox<String> cars = new ComboBox<>("Assign a car");
		cars.setEmptySelectionAllowed(false);
		cars.setPlaceholder("Choose...");
		cars.setDescription("Select a car for the driver");
		cars.setIcon(VaadinIcons.CAR);
		cars.setItems(vehicles.getRegNumbers());

		DateField joinDate = new DateField("<h3>Date Of Membership</h3>");
		joinDate.setCaptionAsHtml(true);
		joinDate.setRequiredIndicatorVisible(true);
		joinDate.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		joinDate.setDateFormat("yyyy-MM-dd");
		joinDate.setRequiredIndicatorVisible(true);

		HorizontalLayout contacts = new HorizontalLayout(mobileNumber, emailAddress);

		TextArea homeAddress = new TextArea("Home Address");
		homeAddress.setRequiredIndicatorVisible(true);
		homeAddress.setPlaceholder("eg. No 22 Corner Street A, City, Country ");

		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_LARGE);
		submit.setDescription("Submit the form");
		submit.addClickListener((e) -> {
			String fname = firstName.getValue();
			String lname = lastName.getValue();
			String natid = nationalId.getValue();
			String ema = emailAddress.getValue();
			String mobi = mobileNumber.getValue();
			String car = cars.getValue();
			String home = homeAddress.getValue();
			String jdate = String.format("%1$tY-%1$tm-%1$td", joinDate.getValue());
			if (car != null && !car.isEmpty()) {
				if (!(fname.equals("") || lname.equals("") || natid.equals("") || ema.equals("") || mobi.equals("")
						|| home.equals("") || jdate.equals(""))) {

					String vehicleId = (String) AllVehicles.getVehicleId(vehicles.getRegNumberAndId(), car).toString();

					new InsertDriver().insertData(stm, fname, lname, natid, home, ema, mobi, jdate, vehicleId);

					firstName.clear();
					lastName.clear();
					nationalId.clear();
					emailAddress.clear();
					mobileNumber.clear();
					cars.clear();
					homeAddress.clear();
				} else {
					new Notification(
							"<h1 style='color:white;'>A blank field has been detected.<br>Can you please check all fields before submission.<br>Thank you!<h1/>",
							"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				}

			} else {
				new Notification("<h1 style='color:white;'>Select the car for the driver<h1/>", "",
						Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			}

		});

		FormLayout formLayout = new FormLayout(names, contacts, nationalId, cars, joinDate, homeAddress, submit);
		formLayout.setCaption("<h2><b>New Driver Entry Form<b/></h2>");
		formLayout.setCaptionAsHtml(true);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);

		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}
}