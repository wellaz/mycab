package com.equation.mycab.utils.withdrawals.unpaid;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.equation.mycab.partners.CaptureReimbursement;
import com.equation.print.utils.print.PrintCurrentPage;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.ui.Table;

/**
 *
 * @author Wellington
 */

@SuppressWarnings("deprecation")
public class UnpaidWithdrawals {
	Statement stm, stmt;
	ResultSet rs, rs1;
	String id;

	public UnpaidWithdrawals(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1, String id) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		this.id = id;
	}

	public void searchForUnpid() {
		ArrayList<Integer> ids = new AllWithdrawalIDs(stm, stmt, rs, rs1).getIds();
		int size = ids.size();
		if (size > 0) {
			ArrayList<Integer> fromReimb = new AllReimbursementIDs(stm, stmt, rs, rs1).getIds();
			Table table = new Table();
			table.addContainerProperty("#", String.class, null);
			table.addContainerProperty("Partner", String.class, null);
			table.addContainerProperty("Principal Amount", String.class, null);
			table.addContainerProperty("Currency", String.class, null);
			table.addContainerProperty("Dated", String.class, null);
			table.addContainerProperty("Amount Paid", String.class, null);
			table.addContainerProperty("Due Date", String.class, null);
			table.addContainerProperty("Balance", String.class, null);

			for (int i = 0; i < size; i++) {
				int id = ids.get(i);
				if (fromReimb.contains(id)) {
					// then check the balance
					if (isCleared(id)) {
						// skip
					} else {
						// list it
						listOutstanding(id, table, i);
					}

				} else {
					// it was never paid back, list it
					listUnPaid(id, table, i);
				}
			}
			int length = table.size();
			table.setPageLength(length);
			table.setColumnCollapsingAllowed(true);
			table.setSelectable(true);
			table.setFooterVisible(true);
			table.setColumnFooter("Partner", String.valueOf(size) + " records found");
			table.setSizeFull();

			Window main = new Window();

			table.addContextClickListener((e) -> {
				int row = (int) table.getValue();
				Item item = table.getItem(row);
				String partner = (String) item.getItemProperty("Partner").getValue().toString();
				String principal = (String) item.getItemProperty("Principal Amount").getValue().toString();
				String currency = (String) item.getItemProperty("Currency").getValue().toString();
				String dated = (String) item.getItemProperty("Dated").getValue().toString();
				String paid = (String) item.getItemProperty("Amount Paid").getValue().toString();
				String due = (String) item.getItemProperty("Due Date").getValue().toString();
				String balance = (String) item.getItemProperty("Balance").getValue().toString();
				String wid = (String) item.getItemProperty("#").getValue().toString();

				Window window = new Window();
				window.setWidth("100%");
				window.center();
				window.setHeight("60%");
				window.setModal(true);

				Label partner1 = new Label("Partner");
				Label partner2 = new Label(partner);

				Label principal1 = new Label("Principal Amount");
				Label principal2 = new Label(principal);

				Label currency1 = new Label("Currency");
				Label currency2 = new Label(currency);

				Label dated1 = new Label("Dated");
				Label dated2 = new Label(dated);

				Label paid1 = new Label("Amount Paid");
				Label paid2 = new Label(paid);

				Label due1 = new Label("Due Date");
				Label due2 = new Label(due);

				Label balance1 = new Label("Current Balance");
				Label balance2 = new Label(balance);

				GridLayout gridLayout = new GridLayout(2, 7, partner1, partner2, principal1, principal2, currency1,
						currency2, dated1, dated2, paid1, paid2, due1, due2, balance1, balance2);

				double prevBalance = Double.parseDouble(balance);

				VerticalLayout right = new VerticalLayout(new CaptureReimbursement(stm, stmt, rs, rs1, prevBalance, wid,
						window, main, principal, partner, due, id, currency));

				VerticalLayout left = new VerticalLayout(gridLayout);

				HorizontalSplitPanel splitPanel = new HorizontalSplitPanel(left, right);
				splitPanel.setSplitPosition(45, Unit.PERCENTAGE);

				window.setContent(splitPanel);
				UI.getCurrent().addWindow(window);

			});

			main.setModal(false);
			main.center();
			main.setSizeFull();

			Button download = new Button("Download");
			download.setIcon(VaadinIcons.DOWNLOAD);
			download.addStyleName(ValoTheme.BUTTON_LARGE);
			download.addStyleName(ValoTheme.BUTTON_FRIENDLY);

			Button print = new Button("Print");
			print.setIcon(VaadinIcons.PRINT);
			print.addStyleName(ValoTheme.BUTTON_LARGE);
			print.addStyleName(ValoTheme.BUTTON_PRIMARY);
			print.addClickListener((e) -> {
				PrintCurrentPage.print();
			});

			Button cancel = new Button("Cancel");
			cancel.setIcon(VaadinIcons.CLOSE);
			cancel.addStyleName(ValoTheme.BUTTON_LARGE);
			cancel.addStyleName(ValoTheme.BUTTON_DANGER);
			cancel.addClickListener((e) -> {
				UI.getCurrent().removeWindow(main);
				main.close();
			});

			HorizontalLayout horizontalLayout = new HorizontalLayout(download, print, cancel);

			VerticalLayout layout = new VerticalLayout(horizontalLayout, table);
			layout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
			layout.setComponentAlignment(table, Alignment.MIDDLE_CENTER);

			main.setContent(layout);

			UI.getCurrent().addWindow(main);

		} else {
			new Notification(
					"<h1 style='color:white;'>No withdrawals were found!<br>There is nothing to pay back.<h1/>", "",
					Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
		}

	}

	private void listOutstanding(int id, Table table, int i) {
		String query = "SELECT firstName,lastName,withdrawal.amount,withdrawal.currency,withdrawal.datePosted,withdrawal.timePosted,dueDate,reimbursement.amount,balance,reimbursement.withdrawalId FROM reimbursement,partner,withdrawal WHERE withdrawal.withdrawalId = '"
				+ id + "'";
		try {
			rs = stm.executeQuery(query);
			rs.last();
			String partner = rs.getString(1) + " " + rs.getString(2);
			String amount = rs.getString(3);
			String currency = rs.getString(4);
			String dated = rs.getString(5) + " " + rs.getString(6);
			String dueDate = rs.getString(7);
			String amountPaid = rs.getString(8);
			String balance = rs.getString(9);
			String wid = rs.getString(10);
			table.addItem(new Object[] { String.valueOf(wid), partner, String.valueOf(amount), currency, dated,
					String.valueOf(amountPaid), dueDate, String.valueOf(balance) }, new Integer(i));
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	private void listUnPaid(int id, Table table, int i) {
		String query = "SELECT firstName,lastName,amount,currency,datePosted,timePosted,dueDate,withdrawalId FROM partner,withdrawal WHERE withdrawalId = '"
				+ id + "'";
		try {
			rs = stm.executeQuery(query);
			rs.next();
			String partner = rs.getString(1) + " " + rs.getString(2);
			String amount = rs.getString(3);
			String currency = rs.getString(4);
			String dated = rs.getString(5) + " " + rs.getString(6);
			String amountPaid = "0.00";
			String dueDate = rs.getString(7);
			String balance = amount;
			String wid = rs.getString(8);
			table.addItem(new Object[] { String.valueOf(wid), partner, String.valueOf(amount), currency, dated,
					String.valueOf(amountPaid), dueDate, String.valueOf(balance) }, new Integer(i));
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	private boolean isCleared(int id) {
		boolean value = true;
		String query = "SELECT balance FROM reimbursement WHERE withdrawalId = '" + id + "'";
		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			String bal = rs1.getString(1);
			if (Double.parseDouble(bal) > 0)
				value = false;

		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return value;
	}
}
