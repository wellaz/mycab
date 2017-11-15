package com.equation.mycab.partners;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Date;

import com.equation.mycab.insert.InsertPartner;
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
public class AddPartner extends CustomComponent {
	ResultSet rs, rs1;
	Statement stm, stmt;

	public AddPartner(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		this.setCompositionRoot(createLayout());
	}

	private VerticalLayout createLayout() {
		VerticalLayout layout = new VerticalLayout();

		TextField firstName = new TextField("<h3>First Name</h3>");
		firstName.setIcon(VaadinIcons.USER);
		firstName.setCaptionAsHtml(true);
		firstName.setRequiredIndicatorVisible(true);

		TextField lastName = new TextField("<h3>Last Name</h3>");
		lastName.setCaptionAsHtml(true);
		lastName.setIcon(VaadinIcons.USER);
		lastName.setRequiredIndicatorVisible(true);
		HorizontalLayout username = new HorizontalLayout(firstName, lastName);

		TextField nationalId = new TextField("<h3>National Id</h3>");
		nationalId.setCaptionAsHtml(true);
		nationalId.setRequiredIndicatorVisible(true);

		TextField mobile = new TextField("<h3>Mobile</h3>");
		mobile.setCaptionAsHtml(true);
		mobile.setRequiredIndicatorVisible(true);
		mobile.setIcon(VaadinIcons.PHONE);

		TextField email = new TextField("<h3>Email</h3>");
		email.setCaptionAsHtml(true);
		email.setIcon(VaadinIcons.ENVELOPE_OPEN);
		HorizontalLayout contacts = new HorizontalLayout(mobile, email);

		TextArea address = new TextArea("<h3>Address</h3>");
		address.setCaptionAsHtml(true);
		address.setRequiredIndicatorVisible(true);

		DateField joinDate = new DateField("<h3>Date Of Membership</h3>");
		joinDate.setCaptionAsHtml(true);
		joinDate.setRequiredIndicatorVisible(true);
		joinDate.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		joinDate.setDateFormat("yyyy-MM-dd");
		joinDate.setRequiredIndicatorVisible(true);

		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_LARGE);
		submit.addClickListener((e) -> {
			String fname = firstName.getValue();
			String lname = lastName.getValue();
			String nid = nationalId.getValue();
			String mobi = mobile.getValue();
			String emai = email.getValue();
			String addr = address.getValue();
			String jdate = String.format("%1$tY-%1$tm-%1$td", joinDate.getValue());

			if (!(fname.equals("") || lname.equals("") || nid.equals("") || mobi.equals("") || addr.equals("")
					|| jdate.equals(""))) {
				new InsertPartner().insertData(stm, fname, lname, nid, mobi, emai, addr, jdate);
				new Notification("<h1>New Partner has been added.<br>Thank you!<h1/>", "",
						Notification.Type.HUMANIZED_MESSAGE, true).show(Page.getCurrent());
				firstName.clear();
				lastName.clear();
				nationalId.clear();
				mobile.clear();
				email.clear();
				address.clear();

			} else {
				new Notification(
						"<h1>An empty field is detected. <br>Can you please make sure that you have supplied all form data where the fields are marked IMPORTANT with a red asterisk<h1/>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			}

		});

		FormLayout formLayout = new FormLayout(username, contacts, nationalId, joinDate, address, submit);
		formLayout.setCaption("<h2><b>Add new partner<b/><h2/>");
		formLayout.setCaptionAsHtml(true);
		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);
		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}
}