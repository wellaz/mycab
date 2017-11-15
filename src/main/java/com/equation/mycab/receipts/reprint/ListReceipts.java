package com.equation.mycab.receipts.reprint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.equation.mycab.driver.cashin.printable.MakeCashInReceipt;
import com.equation.mycab.partner.withdrawal.print.MakeReimburseReceipt;
import com.equation.mycab.partner.withdrawal.print.MakeWithdrawalReceipt;
import com.equation.mycab.partners.dividend.print.MakeDividendRecipt;
import com.equation.mycab.partners.investment.print.MakeInvestmentReceipt;
import com.equation.mycab.transactions.types.Type;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
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
public class ListReceipts {
	ResultSet rs, rs1;
	Statement stm, stmt;

	public ListReceipts(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
	}

	// list all receipts that are found in the receipts table.....if not
	// found.....fine
	public void listReceipts() {
		String query = "SELECT * FROM receipt";
		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			int rows = rs1.getRow(), i = 0;
			if (rows > 0) {
				// construct a table for adding the receipts if they are at all
				// found
				Table table = new Table();
				table.addContainerProperty("#", Integer.class, null);
				table.addContainerProperty("Amount", String.class, null);
				table.addContainerProperty("Date Posted", String.class, null);
				table.addContainerProperty("Transaction Type", String.class, null);

				String query1 = "SELECT * FROM receipt ORDER BY receiptId DESC";
				rs = stm.executeQuery(query1);
				while (rs.next()) {
					int receiptNumber = rs.getInt(1);
					String amount = rs.getString(2);
					String dated = rs.getString(3) + " " + rs.getString(4);
					String txnType = rs.getString(5);

					table.addItem(new Object[] { receiptNumber, String.valueOf(amount), dated, txnType },
							new Integer(i));
					i++;
				}
				int size = table.size();
				table.setPageLength(size);
				table.setSelectable(true);
				table.setColumnCollapsingAllowed(true);
				table.setFooterVisible(true);
				table.setSizeFull();
				table.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);

				// adding a click liste
				table.addContextClickListener((e) -> {
					int row = (int) table.getValue();
					Item item = table.getItem(row);
					String receiptId = (String) item.getItemProperty("#").getValue().toString();
					String amount = (String) item.getItemProperty("Amount").getValue().toString();
					String dated = (String) item.getItemProperty("Date Posted").getValue().toString();
					String transtype = (String) item.getItemProperty("Transaction Type").getValue().toString();

					// the transactio type is a cash in this time
					if (transtype.startsWith(Type.CASHIN)) {
						String query3 = "SELECT firstName,lastName,regNumber,currency FROM driver,vehicle,cashin WHERE cashin.receiptId = '"
								+ receiptId + "' AND driver.vehicleId = vehicle.vehicleId ";
						try {
							rs = stm.executeQuery(query3);
							rs.next();

							String vehicleName = rs.getString(3);
							String driverName = rs.getString(1) + " " + rs.getString(2);
							String currency = rs.getString(4);
							new MakeCashInReceipt().makeReceipt("Administrator", receiptId, vehicleName, driverName,
									amount, currency);

						} catch (SQLException ee) {
							ee.printStackTrace();
						}
					}

					// print a receipt for a reimbursement receipt
					if (transtype.startsWith(Type.REIMBURSE)) {
						String query3 = "SELECT firstName,lastName,reimbursement.currency,balance,withdrawal.amount,dueDate FROM withdrawal,reimbursement,partner WHERE reimbursement.withdrawalId = withdrawal.withdrawalId AND reimbursement.receiptId ='"
								+ receiptId + "'";
						try {
							rs = stm.executeQuery(query3);
							rs.next();
							String partner = rs.getString(1) + " " + rs.getString(2);
							String currency = rs.getString(3);
							String balance = rs.getString(4);
							String principal = rs.getString(5);
							String due = rs.getString(6);

							new MakeReimburseReceipt().makeReceipt("Administrator", receiptId, partner, amount,
									currency, balance, principal, due);

						} catch (SQLException ee) {
							ee.printStackTrace();
						}

					}
					// the transaction this time is a withdrawal
					if (transtype.startsWith(Type.WITHDRAWAL)) {
						String query3 = "firstName,lastName,dueDate,currency FROM withdrawal,partner WHERE receiptId = '"
								+ receiptId + "'";
						try {
							rs = stm.executeQuery(query3);
							rs.next();
							String partner = rs.getString(1) + " " + rs.getString(2);
							String due = rs.getString(3);
							String currency = rs.getString(4);

							new MakeWithdrawalReceipt().makeReceipt(amount, partner, due, dated, "Administrator",
									receiptId, currency);

						} catch (SQLException ee) {
							ee.printStackTrace();
						}

					}
					// the transaction type this time is a dividend
					if (transtype.startsWith(Type.DIVIDEND)) {
						String query3 = "firstName,lastName,currency FROM dividend,partner WHERE receiptId = '"
								+ receiptId + "'";
						try {
							rs = stm.executeQuery(query3);
							rs.next();
							String partner = rs.getString(1) + " " + rs.getString(2);

							String currency = rs.getString(3);
							new MakeDividendRecipt().makeReceipt(amount, partner, dated, "Administrator", receiptId,
									currency);

						} catch (SQLException ee) {
							ee.printStackTrace();
						}

					}
					// the transaction type is an investment
					if (transtype.startsWith(Type.INVESTMENT)) {
						String query3 = "SELECT firstName,lastName,currency FROM partner,investment WHERE receiptId = '"
								+ receiptId + "'";
						try {
							rs = stm.executeQuery(query3);
							rs.next();
							String partner = rs.getString(1) + " " + rs.getString(2);
							String currency = rs.getString(3);
							new MakeInvestmentReceipt().makeReceipt(amount, partner, dated, "Administrator", receiptId,
									currency);
						} catch (SQLException ee) {
							ee.printStackTrace();
						}

					}
				});

				// label for displaying the number of fetched receipts that are
				// found

				Label lbl = new Label("Number of receipts found: " + size);
				lbl.addStyleName(ValoTheme.LABEL_BOLD);
				lbl.addStyleName(ValoTheme.LABEL_LARGE);

				HorizontalLayout horizontalLayout = new HorizontalLayout(lbl);
				horizontalLayout.addStyleName(ValoTheme.LAYOUT_WELL);

				VerticalLayout layout = new VerticalLayout(horizontalLayout, table);
				layout.setComponentAlignment(horizontalLayout, Alignment.TOP_CENTER);

				Window window = new Window();
				window.center();
				window.setSizeFull();
				window.setModal(true);
				window.setContent(layout);

				UI.getCurrent().addWindow(window);

			} else {
				new Notification(
						"<h1 style='color:white;'>There are no receipts to be diplayed for now.<br>Chech te next time after we update a transaction. Thank you.<h1/>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}