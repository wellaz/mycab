package com.equation.mycab.partners;

import java.sql.ResultSet;
import java.sql.Statement;

import com.equation.mycab.currency.Currency;
import com.equation.mycab.driver.cashin.printable.ReceipNumber;
import com.equation.mycab.insert.InsertReceipt;
import com.equation.mycab.insert.InsertReimbursement;
import com.equation.mycab.insert.InsertRevenue;
import com.equation.mycab.partner.withdrawal.print.MakeReimburseReceipt;
import com.equation.mycab.reimburse.balance.ReturnBalance;
import com.equation.mycab.transactions.types.Type;
import com.equation.mycab.utils.currency.check.CheckValue;
import com.equation.mycab.utils.date.DateUtility;
import com.equation.mycab.utils.roundoff.Money;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CaptureReimbursement extends CustomComponent {
	Statement stm, stmt;
	ResultSet rs, rs1;
	double prevBalance;
	String withdrawald, prevCurrency;
	Window window, main;

	String principal, partner, due, id;

	public CaptureReimbursement(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1, double prevBalance,
			String withdrawald, Window window, Window main, String principal, String partner, String due, String id,
			String prevCurrency) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		this.prevBalance = prevBalance;
		this.withdrawald = withdrawald;
		this.window = window;
		this.main = main;
		this.principal = principal;
		this.partner = partner;
		this.due = due;
		this.id = id;
		this.prevCurrency = prevCurrency;
		this.setCompositionRoot(createlayout());
	}

	private VerticalLayout createlayout() {
		VerticalLayout layout = new VerticalLayout();

		TextField amount = new TextField("Amount");
		amount.setDescription("Enter amount");
		amount.setPlaceholder("amount");

		ComboBox<String> currency = new ComboBox<>("Currency");
		currency.setPlaceholder("Choose...");
		currency.setDescription("Select Currency");
		currency.setItems(new Currency(rs, rs1, stm, stmt).getCurrency());

		TextField rate = new TextField("Current Rate");
		rate.setDescription("Enter current rate");
		rate.setPlaceholder("rate...");

		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_LARGE);
		submit.addClickListener((e) -> {
			String amoun = amount.getValue();
			String curren = currency.getValue();
			String rrt = rate.getValue();

			if (!amoun.equals("")) {
				if (curren != null && !curren.isEmpty()) {
					if (CheckValue.isMoney(amoun)) {
						if (Double.parseDouble(amoun) <= prevBalance) {
							String therate = (rrt.equals("")) ? "" : rrt;
							DateUtility dateUtility = new DateUtility();
							String datePosted = dateUtility.getDate(), timePosted = dateUtility.getTime();

							// double balance = prevBalance -
							// Double.parseDouble(amoun);
							double balance = ReturnBalance.getBalance(prevCurrency, curren, Double.parseDouble(amoun),
									prevBalance, Double.parseDouble(rrt));

							new InsertReceipt().inserrData(stm, amoun, datePosted, timePosted, Type.REIMBURSE);
							new InsertRevenue().insertCredit(stm, datePosted, timePosted, amoun,
									partner + " " + Type.REIMBURSE);
							String receiptnumber = new ReceipNumber(stm, stmt, rs, rs1).newReceiptNumber();

							String receiver = new Partner(rs, rs1, stm, stmt).getFirstname(id).toUpperCase() + " "
									+ new Partner(rs, rs1, stm, stmt).getLasttname(id).substring(0, 1).toUpperCase();

							new InsertReimbursement().insertData(stm, withdrawald, amoun, curren,
									Double.toString(Money.toMoney(balance)), datePosted, timePosted, therate,
									receiptnumber);

							new MakeReimburseReceipt().makeReceipt(receiver, receiptnumber, partner, amoun, curren,
									Double.toString(Money.toMoney(balance)), principal, due);

							UI.getCurrent().removeWindow(window);
							window.close();
							UI.getCurrent().removeWindow(main);
							main.close();
							new Notification("<h1 style='color:white;'>The record was submitted.<br>Thank you.<h1/>",
									"", Notification.Type.HUMANIZED_MESSAGE, true).show(Page.getCurrent());

						} else {
							new Notification(
									"<h1 style='color:white;'>The amount entered is "
											+ (Double.parseDouble(amoun) - prevBalance)
											+ " greater than the outstanding balance<br>You may need to correct the amount etered.<h1/>",
									"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
						}

					} else {
						new Notification(
								"<h1 style='color:white;'>The entered amount is not real money.<br>Check the amount again, probably a capturing error occured.<h1/>",
								"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
					}

				} else {
					new Notification("<h1 style='color:white;'>Currency cannot be empty!<h1/>", "",
							Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				}
			} else {
				new Notification("<h1 style='color:white;'>Amount cannot be empty!<h1/>", "",
						Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			}

		});

		FormLayout formLayout = new FormLayout(amount, currency, rate, submit);
		formLayout.setCaption("<h2><b>Capture Reimbursement details<b/><h2/>");
		formLayout.setCaptionAsHtml(true);
		formLayout.setCaption("<h2>Reimburse</h2>");

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);
		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		return layout;
	}
}