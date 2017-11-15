package com.equation.mycab.vehicle.service.span;

import java.sql.ResultSet;
import java.sql.Statement;

import com.equation.mycab.insert.InsertServiceSpan;
import com.equation.mycab.utils.date.DateUtility;
import com.equation.mycab.utils.numerics.Numerics;
import com.equation.mycab.vehicle.AllVehicles;
import com.equation.mycab.vehicle.services.Distance;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Wellington
 */

@SuppressWarnings("serial")
public class CaptureServiceSpan extends CustomComponent {

	Statement stm, stmt;
	ResultSet rs, rs1;
	AllVehicles collection;

	public CaptureServiceSpan(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		collection = new AllVehicles(stm, stmt, rs, rs1);
		this.setCompositionRoot(createLayout());
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

		TextField distance = new TextField("Mileage (<i>Distance<i/>)");
		distance.setCaptionAsHtml(true);
		distance.setIcon(VaadinIcons.CASH);
		distance.setPlaceholder("eg 33000");
		distance.setDescription("Enter the mileage to travel before the next car service.");
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
			String dist = distance.getValue();
			String un = unit.getValue();
			if (vehicle != null && !vehicle.isEmpty()) {
				if (!dist.equals("")) {
					if (Numerics.isNumeric(dist)) {
						String datePosted = new DateUtility().getDate();
						String vehicleId1 = (String) AllVehicles.getVehicleId(collection.getRegNumberAndId(), vehicle)
								.toString();

						new InsertServiceSpan().insertData(stm, vehicleId1, dist, un, datePosted);
						new Notification("<h1>Record successfully posted<br>Thank you<h1/>", "",
								Notification.Type.HUMANIZED_MESSAGE, true).show(Page.getCurrent());
						vehicleId.clear();
						distance.clear();
					} else {
						new Notification(
								"<h1>The distance entered is not a numeric value<br>Can you make a correction please<h1/>",
								"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
						distance.focus();
					}
				} else {
					new Notification("<h1>Distance Span cannot be empty!!!<h1/>", "", Notification.Type.ERROR_MESSAGE,
							true).show(Page.getCurrent());
					distance.focus();
				}

			} else {
				new Notification("<h1>Select a vehicle<h1/>", "", Notification.Type.ERROR_MESSAGE, true)
						.show(Page.getCurrent());
				vehicleId.focus();
			}

		});

		FormLayout formLayout = new FormLayout(vehicleId, distance, unit, submit);
		formLayout.setCaption("<h2><b>Set how long a car needs to travel before the next service.<b/><h2/>");
		formLayout.setCaptionAsHtml(true);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);
		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}

}
