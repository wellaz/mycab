package com.equation.mycab.partners;

import java.sql.ResultSet;
import java.sql.Statement;

import com.equation.mycab.expense.types.ExpenseType;
import com.equation.mycab.insert.InsertExpense;
import com.equation.mycab.insert.InsertRevenue;
import com.equation.mycab.utils.currency.check.CheckValue;
import com.equation.mycab.utils.date.DateUtility;
import com.equation.mycab.vehicle.AllVehicles;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CaptureExpense extends CustomComponent {
	Statement stm, stmt;
	ResultSet rs, rs1;
	AllVehicles collection;

	public CaptureExpense(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		collection = new AllVehicles(stm, stmt, rs, rs1);
		this.setCompositionRoot(createLayout());
	}

	public VerticalLayout createLayout() {
		VerticalLayout layout = new VerticalLayout();

		ComboBox<String> vehicleId = new ComboBox<>("Select Vehicle");
		vehicleId.setIcon(VaadinIcons.CAR);
		vehicleId.setPlaceholder("Choose...");
		vehicleId.setDescription("Select the vehicle<br>to capture its expense", ContentMode.HTML);
		vehicleId.setRequiredIndicatorVisible(true);
		vehicleId.setEmptySelectionAllowed(false);
		vehicleId.setItems(collection.getRegNumbers());

		ComboBox<String> expenseType = new ComboBox<>("Expense Type");
		expenseType.setIcon(VaadinIcons.WALLET);
		expenseType.setPlaceholder("Choose...");
		expenseType.setDescription("Select the transaction type");
		expenseType.setRequiredIndicatorVisible(true);
		expenseType.setEmptySelectionAllowed(false);
		expenseType.setItems(ExpenseType.expenseList());

		CheckBox mark = new CheckBox("<h4>Mark as an operational cost.<h4/>(<i style='color:red;'><em>leave it blank if its not an operational expense<em/><i/>)");
		mark.setCaptionAsHtml(true);

		TextField amount = new TextField("Amount");
		amount.setIcon(VaadinIcons.CASH);
		amount.setPlaceholder("eg 33.41");
		amount.setDescription("Enter the amount here");
		amount.setRequiredIndicatorVisible(true);

		TextArea description = new TextArea("Short Description");
		description.setIcon(VaadinIcons.FILE_ADD);
		description.setPlaceholder("Write a short description about this expense");

		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_LARGE);
		submit.setDescription("Submit this expense");
		submit.addClickListener((e) -> {
			String vehicle = vehicleId.getValue();
			String type = expenseType.getValue();
			String amoun = amount.getValue();
			String des = description.getValue();
			
			//do validation and cleck for null values here
			if (vehicle != null && !vehicle.isEmpty()) {
				if (type != null && !type.isEmpty()) {
					if (!(amoun.equals("") || des.equals(""))) {
						if (CheckValue.isMoney(amoun)) {
							String datePosted = new DateUtility().getDate(), timePosted = new DateUtility().getTime();
							String id = AllVehicles.getVehicleId(collection.getRegNumberAndId(), (Object) vehicle)
									.toString();
							new InsertExpense().insertData(stm, id, type, amoun, datePosted, des, mark.getValue());
							new InsertRevenue().insertDebit(stm, datePosted, timePosted, amoun, type);

							new Notification("<h1>An expense has been posted.<br>Thank you!<h1/>", "",
									Notification.Type.HUMANIZED_MESSAGE, true).show(Page.getCurrent());

							vehicleId.clear();
							amount.clear();
							description.clear();
							expenseType.clear();

						} else {
							// not money
							new Notification(
									"<h1 style='color:white;'>The amount entered is not real money.<br>Make some adjustments so that the expense amount is real money.<h1/>",
									"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
							amount.focus();

						}

					} else {
						// empty fields
						new Notification(
								"<h1 style='color:white;'>Empty fields have been detected.<br>Fill in all the details to at least describe the nature of the expense.<h1/>",
								"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());

					}

				} else {
					// empty type
					new Notification(
							"<h1 style='color:white;'>Empty field for expense type has been detected.<br>Fill in all the details to at least describe the nature of the expense.<h1/>",
							"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
					expenseType.focus();
				}

			} else {
				// empty vehicle
				new Notification(
						"<h1 style='color:white;'>Empty field for vehicle Reg Number has been detected.<br>Let us at least know the vehicle that brought about this expense, to at least describe the nature of the expense.<h1/>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				vehicleId.focus();
			}

		});

		FormLayout formLayout = new FormLayout(vehicleId, amount, expenseType, description, mark, submit);
		formLayout.setCaption("<h2><b>Capture an expense<b/><h2/>");
		formLayout.setCaptionAsHtml(true);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);
		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		return layout;
	}
}