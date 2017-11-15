package com.equation.mycab.vehicle.services;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Date;

import com.equation.mycab.driver.DriverVehicle;
import com.equation.mycab.insert.InsertMileage;
import com.equation.mycab.partners.Partner;
import com.equation.mycab.utils.numerics.Numerics;
import com.equation.mycab.vehicle.AllVehicles;
import com.equation.mycab.vehicle.mileage.printable.MakeServiceRceeipt;
import com.equation.mycab.vehicle.service.list.ServicesList;
import com.equation.mycab.vehicle.service.span.GetServiceSpan;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Wellington
 */

@SuppressWarnings("serial")
public class CaptureCarService extends CustomComponent {

	ResultSet rs, rs1;
	Statement stm, stmt;
	AllVehicles collection;
	String id;

	public CaptureCarService(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, String id) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		this.id = id;
		collection = new AllVehicles(stm, stmt, rs, rs1);

		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel(left(), createLayout());
		splitPanel.setSplitPosition(25, Unit.PERCENTAGE);

		this.setCompositionRoot(splitPanel);
	}

	private VerticalLayout createLayout() {
		VerticalLayout layout = new VerticalLayout();

		ComboBox<String> vehicleId = new ComboBox<>("Select Vehicle");
		vehicleId.setIcon(VaadinIcons.CAR);
		vehicleId.setPlaceholder("Choose...");
		vehicleId.setDescription("Select the target vehicle");
		vehicleId.setRequiredIndicatorVisible(true);
		vehicleId.setEmptySelectionAllowed(false);
		vehicleId.setItems(collection.getRegNumbers());

		DateField dueDate = new DateField("Day of service");
		dueDate.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		dueDate.setDateFormat("yyyy-MM-dd");
		dueDate.setRequiredIndicatorVisible(true);
		dueDate.setDescription("Specify the due date");

		TextField distance = new TextField("Current Mileage (<i>Distance</i>)");
		distance.setCaptionAsHtml(true);
		distance.setIcon(VaadinIcons.CASH);
		distance.setPlaceholder("eg 33000");
		distance.setDescription("Enter the current Distance travelled");
		distance.setRequiredIndicatorVisible(true);

		ComboBox<String> unit = new ComboBox<>("Unit");
		unit.setIcon(VaadinIcons.CAR);
		unit.setPlaceholder("Choose...");
		unit.setDescription("Select the Distance Unit");
		unit.setRequiredIndicatorVisible(true);
		unit.setEmptySelectionAllowed(false);
		unit.setItems(Distance.getUnits());
		unit.setValue(Distance.KM);

		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_LARGE);
		submit.setDescription("Submit this expense");
		submit.addClickListener((e) -> {
			String vehicle = vehicleId.getValue();
			String due = String.format("%1$tY-%1$tm-%1$td", dueDate.getValue());
			String dist = distance.getValue();
			String un = unit.getValue();
			if (vehicle != null && !vehicle.isEmpty()) {
				if (!(due.equals("") || dist.equals("") || un.equals(""))) {
					if (Numerics.isNumeric(dist)) {
						String vid = (String) AllVehicles.getVehicleId(collection.getRegNumberAndId(), vehicle)
								.toString();
						new InsertMileage().insertData(stm, vid, due, dist, un);
						vehicleId.clear();
						distance.clear();
						unit.clear();
						String span = new GetServiceSpan(rs, rs1, stm, stmt).getSpanFor(vid);
						int nextService = Integer.parseInt(dist) + Integer.parseInt(span);
						String executive = new Partner(rs, rs1, stm, stmt).getFirstname(id).toUpperCase() + " "
								+ new Partner(rs, rs1, stm, stmt).getLasttname(id).substring(0, 1).toUpperCase();

						String driver = new DriverVehicle(stm, stmt, rs, rs1).getDriverName(vid);

						new MakeServiceRceeipt().makeReceipt(due, dist + un, executive, vehicle, "" + nextService + un,
								driver);

						new Notification("<h1>Record submitted.<br>The next service will be at a mileage of "
								+ nextService + " " + un + ".<br>Thank you<h1/>", "", Notification.Type.WARNING_MESSAGE,
								true).show(Page.getCurrent());
					} else {
						new Notification(
								"<h1 style='color:white;'>This distance is not a numeric value<br>Make sure that you have filled in all the details.<h1/>",
								"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
						distance.focus();
					}

				} else {
					new Notification(
							"<h1 style='color:white;'>An empty field has been detected<br>Make sure that you have filled in all the details.<h1/>",
							"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				}

			} else {
				new Notification("<h1>Select the target vehicle<h1/>", "", Notification.Type.ERROR_MESSAGE, true)
						.show(Page.getCurrent());
				vehicleId.focus();
			}

		});

		FormLayout formLayout = new FormLayout(vehicleId, dueDate, distance, unit, submit);
		formLayout.setCaption("<h2><b>Capture Car Servicing Details<b/><h2/>");
		formLayout.setCaptionAsHtml(true);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);
		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}

	private VerticalLayout left() {
		VerticalLayout layout = new VerticalLayout();
		Button rep = new Button("Services Report");
		rep.setIcon(VaadinIcons.SEARCH);
		rep.addStyleName(ValoTheme.BUTTON_PRIMARY);
		rep.addClickListener(e -> new ServicesList(rs, rs1, stm, stmt).listData());

		FormLayout formLayout = new FormLayout(rep);
		formLayout.setCaption("<h2><b>Search Car services<b/><h2/>");
		formLayout.setCaptionAsHtml(true);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);
		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;

	}

}
