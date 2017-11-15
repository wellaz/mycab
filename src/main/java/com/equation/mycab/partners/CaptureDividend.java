package com.equation.mycab.partners;

import java.sql.ResultSet;
import java.sql.Statement;

import com.equation.mycab.currency.Currency;
import com.equation.mycab.driver.cashin.printable.ReceipNumber;
import com.equation.mycab.insert.InsertIntoDividend;
import com.equation.mycab.insert.InsertReceipt;
import com.equation.mycab.insert.InsertRevenue;
import com.equation.mycab.partners.dividend.print.MakeDividendRecipt;
import com.equation.mycab.transactions.types.Type;
import com.equation.mycab.utils.date.DateUtility;
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

@SuppressWarnings("serial")
public class CaptureDividend extends CustomComponent {
	ResultSet rs, rs1;
	Statement stm, stmt;
	PartnersCollection collection;
	String id;

	public CaptureDividend(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, String id) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		this.id = id;
		collection = new PartnersCollection(stm, stmt, rs, rs1);
		this.setCompositionRoot(createLayout());
	}

	public VerticalLayout createLayout() {
		VerticalLayout layout = new VerticalLayout();
		TextField amount = new TextField("<h3>Amount</h3>");
		amount.setCaptionAsHtml(true);
		amount.setIcon(VaadinIcons.MONEY);

		ComboBox<String> currency = new ComboBox<>("<h3>Currency</h3>");
		currency.setCaptionAsHtml(true);
		currency.setIcon(VaadinIcons.MONEY);
		currency.setItems(new Currency(rs, rs1, stm, stmt).getCurrency());
		currency.setEmptySelectionAllowed(false);

		ComboBox<String> partnerId = new ComboBox<>("<h3>Partner Name</h3>");
		partnerId.setCaptionAsHtml(true);
		partnerId.setIcon(VaadinIcons.USER);
		partnerId.setItems(collection.getPartnernames());
		partnerId.setEmptySelectionAllowed(false);

		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_LARGE);
		submit.addClickListener((e) -> {
			String am = amount.getValue();
			String curr = currency.getValue();
			String name = partnerId.getValue();

			if (name != null && !name.isEmpty()) {
				if (!(am.equals("") || curr.equals(""))) {
					DateUtility dateUtility = new DateUtility();
					String datePosted = dateUtility.getDate();
					String timePosted = dateUtility.getTime();
					Object value = (Object) name;
					String pid = PartnersCollection.getPartnerId(collection.getPartnerAndId(), value).toString();

					new InsertReceipt().inserrData(stm, am, datePosted, timePosted, Type.DIVIDEND);
					new InsertRevenue().insertDebit(stm, datePosted, timePosted, am, name + " " + Type.DIVIDEND);

					String receiptId = new ReceipNumber(stm, stmt, rs, rs1).newReceiptNumber();

					new InsertIntoDividend().insertData(stm, am, curr, pid, datePosted, timePosted, receiptId);

					String receiver = new Partner(rs, rs1, stm, stmt).getFirstname(id).toUpperCase() + " "
							+ new Partner(rs, rs1, stm, stmt).getLasttname(id).substring(0, 1).toUpperCase();

					new MakeDividendRecipt().makeReceipt(am, name, datePosted, receiver, receiptId, curr);

					new Notification("<h1>The dividend has been submitted.<br>Thank you <h1/>", "",
							Notification.Type.HUMANIZED_MESSAGE, true).show(Page.getCurrent());
					amount.clear();
					currency.clear();
					partnerId.clear();

				} else {
					new Notification(
							"<h1>An important field is empty.<br>Ensure that all fields are filled in order to capture dividend details.<h1/>",
							"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				}

			} else {
				new Notification(
						"<h1>Partner Field is empty.<br>Select the partner who is to receive the dividend.<h1/>", "",
						Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			}
		});

		FormLayout formLayout = new FormLayout(partnerId, amount, currency, submit);
		formLayout.setCaption("<h2><b>Capture dividend for a business partner<b/><h2/>");
		formLayout.setCaptionAsHtml(true);
		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);

		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}
}