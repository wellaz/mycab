package com.equation.mycab.partners;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;

import com.equation.mycab.currency.Currency;
import com.equation.mycab.driver.cashin.printable.ReceipNumber;
import com.equation.mycab.insert.InsertReceipt;
import com.equation.mycab.insert.InsertRevenue;
import com.equation.mycab.insert.InsertWithdrawal;
import com.equation.mycab.partner.withdrawal.print.MakeWithdrawalReceipt;
import com.equation.mycab.transactions.types.Type;
import com.equation.mycab.utils.currency.check.CheckValue;
import com.equation.mycab.utils.date.DateUtility;
import com.equation.mycab.utils.withdrawals.unpaid.UnpaidWithdrawals;
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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CaptureWithdrawal extends CustomComponent {

	Statement stm, stmt;
	ResultSet rs, rs1;
	String id;
	PartnersCollection collection;

	public CaptureWithdrawal(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1, String id) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		this.id = id;
		collection = new PartnersCollection(stm, stmt, rs, rs1);

		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();

		splitPanel.setFirstComponent(leftLayout());
		splitPanel.setSecondComponent(createLayout());
		splitPanel.setSplitPosition(30, Unit.PERCENTAGE);
		splitPanel.setResponsive(true);

		this.setCompositionRoot(splitPanel);
	}

	private VerticalLayout createLayout() {
		VerticalLayout layout = new VerticalLayout();

		TextField amount = new TextField("Enter Amount");
		amount.setPlaceholder("Amount");
		amount.setRequiredIndicatorVisible(true);
		amount.setDescription("Enter the amount");

		ComboBox<String> currency = new ComboBox<>("Select the Currency");
		currency.setPlaceholder("Choose...");
		currency.setRequiredIndicatorVisible(true);
		currency.setDescription("Select the currency");
		currency.setItems(new Currency(rs, rs1, stm, stmt).getCurrency());

		DateField dueDate = new DateField("Due On");
		dueDate.setValue(new DateUtility().dayThirty().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		dueDate.setDateFormat("yyyy-MM-dd");
		dueDate.setRequiredIndicatorVisible(true);
		dueDate.setDescription("Specify the due date");

		TextArea description = new TextArea("Description");
		description.setDescription("Write a short description about this withdrawal");

		ComboBox<String> partnerId = new ComboBox<String>("Select Partner");
		partnerId.setRequiredIndicatorVisible(true);
		partnerId.setPlaceholder("Choose...");
		partnerId.setEmptySelectionAllowed(false);
		partnerId.setDescription("Select the one who is taking the money");
		partnerId.setItems(collection.getPartnernames());

		Button submit = new Button("Submit");
		submit.setDescription("Submit this withdrawal");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_LARGE);
		submit.setDescription("Submit the withdrwal form");
		submit.addClickListener((e) -> {
			String amoun = amount.getValue();
			String curr = currency.getValue();
			String due = String.format("%1$tY-%1$tm-%1$td", dueDate.getValue());
			String des = description.getValue();
			String partner = partnerId.getValue();
			if (curr != null && !curr.isEmpty()) {
				if (partner != null && !partner.isEmpty()) {
					if (!(amoun.equals("") || due.equals("") || des.equals(""))) {
						if (CheckValue.isMoney(amoun)) {
							String id = PartnersCollection.getPartnerId(collection.getPartnerAndId(), (Object) partner)
									.toString();
							DateUtility dateUtility = new DateUtility();
							String datePosted = dateUtility.getDate(), timePosted = dateUtility.getTime();
							String receiptNumber = new ReceipNumber(stm, stmt, rs, rs1).newReceiptNumber();
							new InsertReceipt().inserrData(stm, amoun, datePosted, timePosted, Type.WITHDRAWAL);

							new InsertRevenue().insertDebit(stm, datePosted, timePosted, amoun,
									partner + " " + Type.WITHDRAWAL);

							String pid = (String) PartnersCollection.getPartnerId(collection.getPartnerAndId(), partner)
									.toString();

							new InsertWithdrawal().insertData(stm, amoun, curr, datePosted, timePosted, due, des, pid,
									receiptNumber);

							String receiver = new Partner(rs, rs1, stm, stmt).getFirstname(id).toUpperCase() + " "
									+ new Partner(rs, rs1, stm, stmt).getLasttname(id).substring(0, 1).toUpperCase();

							new MakeWithdrawalReceipt().makeReceipt(amoun, partner, due, datePosted + " " + timePosted,
									receiver, receiptNumber, curr);

							new Notification("<h1>Withdrawal has been successfully posted<br>Thank you!<h1/>", "",
									Notification.Type.HUMANIZED_MESSAGE, true).show(Page.getCurrent());
							amount.clear();
							currency.clear();
							description.clear();
							partnerId.clear();
						} else {
							// is not money
							new Notification(
									"<h1 style= 'color:white;'>The entered amount is not real money!<br>Make sure that you have entered the correct amount to further describe this withrawal.<h1/>",
									"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
							amount.focus();
						}

					} else {
						// emoty fields
						new Notification(
								"<h1 style= 'color:white;'>Some important fields are still empty!<br>Can you please make sure that you fill in important fields to further describe this withrawal.<h1/>",
								"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
					}

				} else {
					// empty partner
					new Notification(
							"<h1 style= 'color:white;'>The Partner is not selcted<br>Can you please select the correct Partner for this withrawal.<h1/>",
							"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
					partnerId.focus();
				}

			} else {
				// empty currency
				new Notification(
						"<h1 style= 'color:white;'>The currency is not selcted<br>Can you please select the currency for this withrawal.<h1/>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				currency.focus();
			}

		});

		FormLayout formLayout = new FormLayout(partnerId, amount, currency, dueDate, description, submit);
		formLayout.setCaption("<h2><b>Make a withdrawal<b/><h2/>");
		formLayout.setCaptionAsHtml(true);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);
		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}

	private VerticalLayout leftLayout() {
		Button search = new Button("Reimbursement");
		search.addStyleName(ValoTheme.BUTTON_PRIMARY);
		search.addStyleName(ValoTheme.BUTTON_LARGE);
		search.addClickListener((e) -> {
			new UnpaidWithdrawals(stm, stmt, rs, rs1, id).searchForUnpid();
		});

		FormLayout formLayout = new FormLayout(search);
		formLayout.setCaption("<h2>Search and make reimbursements <h2/>");
		formLayout.setCaptionAsHtml(true);

		VerticalLayout layout = new VerticalLayout(formLayout);
		return layout;
	}
}