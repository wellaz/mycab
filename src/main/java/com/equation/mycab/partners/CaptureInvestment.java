package com.equation.mycab.partners;

import java.sql.ResultSet;
import java.sql.Statement;

import com.equation.mycab.currency.Currency;
import com.equation.mycab.driver.cashin.printable.ReceipNumber;
import com.equation.mycab.insert.InsertInvestment;
import com.equation.mycab.insert.InsertReceipt;
import com.equation.mycab.insert.InsertRevenue;
import com.equation.mycab.partners.investment.print.MakeInvestmentReceipt;
import com.equation.mycab.transactions.types.Type;
import com.equation.mycab.utils.currency.check.CheckValue;
import com.equation.mycab.utils.date.DateUtility;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
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
public class CaptureInvestment extends CustomComponent {

	Statement stm, stmt;
	ResultSet rs, rs1;
	PartnersCollection collection;
	String id;

	public CaptureInvestment(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1, String id) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		this.id = id;
		collection = new PartnersCollection(stm, stmt, rs, rs1);
		this.setCompositionRoot(createLayout());
	}

	private VerticalLayout createLayout() {
		VerticalLayout layout = new VerticalLayout();

		ComboBox<String> partnerId = new ComboBox<>("Partner Name");
		partnerId.setPlaceholder("Choose...");
		partnerId.setEmptySelectionAllowed(false);
		partnerId.setDescription("Select the partner");
		partnerId.setItems(collection.getPartnernames());

		TextField amount = new TextField("Amount");
		amount.setPlaceholder("amount...");
		amount.setDescription("Enter the amount");

		ComboBox<String> currency = new ComboBox<>("Currency");
		currency.setPlaceholder("Select the currency");
		currency.setPlaceholder("Choose...");
		currency.setDescription("Select the currency");
		currency.setItems(new Currency(rs, rs1, stm, stmt).getCurrency());

		TextArea description = new TextArea("Description");
		description.setPlaceholder("Write a short descrition for this investment...");

		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_LARGE);
		submit.addClickListener((e) -> {
			String partner = partnerId.getValue();
			String amoun = amount.getValue();
			String curr = currency.getValue();
			String des = description.getValue();
			if (partner != null && !partner.isEmpty()) {
				if (curr != null && !curr.isEmpty()) {
					if (!(amoun.equals("") || des.equals(""))) {
						if (CheckValue.isMoney(amoun)) {
							DateUtility dateUtility = new DateUtility();
							String datePosted = dateUtility.getDate();
							String timePosted = dateUtility.getTime();

							new InsertReceipt().inserrData(stm, amoun, datePosted, timePosted, Type.INVESTMENT);
							new InsertRevenue().insertCredit(stm, datePosted, timePosted, amoun,
									partner + " " + Type.INVESTMENT);

							String receiptnumber = new ReceipNumber(stm, stmt, rs, rs1).newReceiptNumber();

							String receiver = new Partner(rs, rs1, stm, stmt).getFirstname(id).toUpperCase() + " "
									+ new Partner(rs, rs1, stm, stmt).getLasttname(id).substring(0, 1).toUpperCase();

							new MakeInvestmentReceipt().makeReceipt(amoun, partner, datePosted, receiver, receiptnumber,
									curr);

							String id = PartnersCollection.getPartnerId(collection.getPartnerAndId(), (Object) partner)
									.toString();

							new InsertInvestment().insertData(stm, id, amoun, curr, datePosted, timePosted, des,
									receiptnumber);

							new Notification("<h1>An Investment has been added<br>Thank you!<h1/>", "",
									Notification.Type.HUMANIZED_MESSAGE, true).show(Page.getCurrent());
							partnerId.clear();
							amount.clear();
							currency.clear();
							description.clear();

						} else {
							// value is not money
							new Notification(
									"<h1 style = 'color:white;'>The amount entered is not real money.<br>Please make sure that the investment amount is real money.<h1/>",
									"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
							amount.focus();
						}

					} else {
						// empty des or MOUNT
						new Notification(
								"<h1 style = 'color:white;'>Empty fields have been detected.<br>Please fill in all the details before submission.<h1/>",
								"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
					}

				} else {
					// empty curr
					new Notification(
							"<h1 style = 'color:white;'>No Currency has been selected.<br>Select a currency and proceed.<h1/>",
							"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				}

			} else {
				// empty partner
				new Notification(
						"<h1 style = 'color:white;'>No Investor has been selected.<br>Select an investor please.<h1/>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());

			}

		});

		FormLayout formLayout = new FormLayout(partnerId, amount, currency, description, submit);
		formLayout.setCaption("<h2><b>Capture Investment details made by a business partner.<b/><h2/>");
		formLayout.setCaptionAsHtml(true);
		formLayout.setCaption("<h3>Submit an Investment Record</h3>");
		formLayout.setCaptionAsHtml(true);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);
		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}
}