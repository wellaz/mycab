package com.equation.mycab.vehicle;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;

import com.equation.mycab.insert.InsertVehicle;
import com.equation.mycab.utils.date.DateUtility;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
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
public class NewVehicle extends CustomComponent {

	Statement stm, stmt;
	ResultSet rs, rs1;

	public NewVehicle(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		this.setCompositionRoot(createLayout());
	}

	private VerticalLayout createLayout() {
		VerticalLayout layout = new VerticalLayout();

		TextField regNumber = new TextField("Registration Number");
		regNumber.setIcon(VaadinIcons.STOP);
		regNumber.setRequiredIndicatorVisible(true);
		regNumber.setPlaceholder("car registration number");
		regNumber.setDescription("Enter the car registration number here");

		TextField engineNumber = new TextField("Engine Number");
		engineNumber.setIcon(VaadinIcons.PIN_POST);
		engineNumber.setRequiredIndicatorVisible(true);
		engineNumber.setPlaceholder("the engine number");
		engineNumber.setDescription("Enter the car engine number here");

		TextField model = new TextField("Vehicle Model");
		model.setRequiredIndicatorVisible(true);
		model.setPlaceholder("eg. Volkswagen GTI");
		model.setDescription("Specify the car model here");

		TextField vehicleColor = new TextField("Vehicle Color");
		vehicleColor.setRequiredIndicatorVisible(true);
		vehicleColor.setIcon(VaadinIcons.PICTURE);
		vehicleColor.setPlaceholder("eg Red");
		vehicleColor.setDescription("Generalise the color of your car");

		TextArea description = new TextArea("Desription");
		description.setIcon(VaadinIcons.NOTEBOOK);
		description.setRequiredIndicatorVisible(true);
		description.setPlaceholder("Write a short description adding any impotant informatiion about your car");

		DateField dateReceived = new DateField("Beginning from");
		dateReceived
				.setValue(new DateUtility().firstOfJanuary().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		dateReceived.setDateFormat("yyyy-MM-dd");
		dateReceived.setRequiredIndicatorVisible(true);

		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_LARGE);
		submit.addClickListener((e) -> {
			String reg = regNumber.getValue();
			String engine = engineNumber.getValue();
			String mode = model.getValue();
			String color = vehicleColor.getValue();
			String des = description.getValue();
			String rec = String.format("%1$tY-%1$tm-%1$td", dateReceived.getValue());
			if (!(reg.equals("") || engine.equals("") || mode.equals("") || color.equals("") || des.equals("")
					|| rec.equals(""))) {
				new InsertVehicle().insertData(stm, reg, engine, mode, color, des, rec);
				regNumber.clear();
				engineNumber.clear();
				model.clear();
				vehicleColor.clear();
				description.clear();
				new Notification("<h1 stype='color:white;'>New Vehicle has been registered.<br>Thank you<h1/>", "",
						Notification.Type.HUMANIZED_MESSAGE, true).show(Page.getCurrent());

			} else {
				new Notification(
						"<h1 stype='color:white;'>A blank field has been detected.<br>The form cannot be submitted.<h1/>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			}

		});

		submit.setDescription("Submit this form");

		FormLayout formLayout = new FormLayout(regNumber, engineNumber, model, vehicleColor, dateReceived, description,
				submit);
		formLayout.setCaption("<h1><b>New Vehicle Registration Form<b/></h1>");
		formLayout.setCaptionAsHtml(true);
		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);
		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}
}