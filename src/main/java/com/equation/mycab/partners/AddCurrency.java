package com.equation.mycab.partners;

import java.sql.ResultSet;
import java.sql.Statement;

import com.equation.mycab.insert.InsertMyCurrency;
import com.equation.mycab.utils.date.DateUtility;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class AddCurrency extends CustomComponent {

	ResultSet rs, rs1;
	Statement stm, stmt;

	public AddCurrency(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		this.setCompositionRoot(createlayout());
	}

	private VerticalLayout createlayout() {
		VerticalLayout layout = new VerticalLayout();

		TextField currenyName = new TextField("<h4>Enter Currency Name</h4>");
		currenyName.setCaptionAsHtml(true);
		currenyName.setRequiredIndicatorVisible(true);
		currenyName.setDescription("Enter the currency name here");
		currenyName.setIcon(VaadinIcons.MONEY_EXCHANGE);
		currenyName.setPlaceholder("Enter the currency name");

		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_LARGE);
		submit.addClickListener((e) -> {
			String c = currenyName.getValue();
			if (!c.equals("")) {
				new InsertMyCurrency().insertData(stm, c, new DateUtility().getDate());

				new Notification("<h1>Currency has been added<br>Thank you!<h1/>", "",
						Notification.Type.HUMANIZED_MESSAGE, true).show(Page.getCurrent());
				currenyName.clear();
			} else {
				new Notification("<h1>No Currency name to submit.<br>Can you please supply the currency name<h1/>", "",
						Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			}
		});

		FormLayout formLayout = new FormLayout(currenyName, submit);
		formLayout.setCaption("<h2> <b> Capture New Currency<b/></h2>");
		formLayout.setCaptionAsHtml(true);

		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);

		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		return layout;
	}
}