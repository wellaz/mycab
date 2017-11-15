package com.equation.mycab.dividend.search;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.equation.mycab.utils.roundoff.Money;
import com.equation.print.utils.print.PrintCurrentPage;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.Table;

/**
 *
 * @author Wellington
 */

@SuppressWarnings("deprecation")
public class SingleDateDividend {
	ResultSet rs, rs1;
	Statement stm, stmt;
	double total = 0;

	public SingleDateDividend(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
	}

	public void searchData(String datePosted) {
		String query = "SELECT firstName,lastName,amount,currency,datePosted,timePosted,receiptId FROM partner,dividend WHERE dividend.datePosted = '"
				+ datePosted + "' ";
		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			int rows = rs1.getRow(), i = 0;
			if (rows > 0) {
				Table table = new Table();
				table.addContainerProperty("Partner Name", String.class, null);
				table.addContainerProperty("Amount", String.class, null);
				table.addContainerProperty("Currency", String.class, null);
				table.addContainerProperty("Dated", String.class, null);
				table.addContainerProperty("Receipt", String.class, null);

				String query1 = "SELECT firstName,lastName,amount,currency,datePosted,timePosted,receiptId FROM partner,dividend WHERE dividend.datePosted = '"
						+ datePosted + "' ";
				rs = stm.executeQuery(query1);
				while (rs.next()) {
					String name = rs.getString(1) + " " + rs.getString(2);
					String amount = rs.getString(3);
					total += Double.parseDouble(amount);
					String currency = rs.getString(4);
					String dated = rs.getString(5) + " " + rs.getString(6);
					String receipt = rs.getString(7);

					table.addItem(
							new Object[] { name, String.valueOf(amount), currency, dated, String.valueOf(receipt) },
							new Integer(i));
					i++;

				}

				table.setColumnCollapsingAllowed(true);
				table.setFooterVisible(true);
				int size = table.size();
				table.setColumnFooter("Amount", String.valueOf(Money.toMoney(total)));
				table.setColumnFooter("Driver Name", String.valueOf(size) + " records found");
				table.setPageLength(size);
				table.setSelectable(true);
				table.setWidth("100%");

				Window window = new Window();
				window.setSizeFull();
				window.center();
				window.setModal(true);

				Button download = new Button("Download Statement");
				download.setIcon(VaadinIcons.DOWNLOAD);
				download.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				download.addStyleName(ValoTheme.BUTTON_LARGE);

				Button print = new Button("Print");
				print.setIcon(VaadinIcons.PRINT);
				print.addStyleName(ValoTheme.BUTTON_PRIMARY);
				print.addStyleName(ValoTheme.BUTTON_LARGE);
				print.addClickListener((e) -> {
					PrintCurrentPage.print();
				});

				Button close = new Button("Close");
				close.setIcon(VaadinIcons.CLOSE);
				close.addStyleName(ValoTheme.BUTTON_DANGER);
				close.addStyleName(ValoTheme.BUTTON_LARGE);
				close.addClickListener((e) -> {
					UI.getCurrent().removeWindow(window);
					window.close();
				});
				HorizontalLayout horizontalLayout = new HorizontalLayout(download, print, close);
				horizontalLayout.addStyleName(ValoTheme.LAYOUT_WELL);
				horizontalLayout.setWidth("100%");

				VerticalLayout layout = new VerticalLayout(horizontalLayout, table);
				layout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);

				window.setContent(layout);

				new SingleDatePartnerPDF().generatePDF(table, download, datePosted,
						Double.toString(Money.toMoney(total)));

				UI.getCurrent().addWindow(window);

			} else {
				new Notification(
						"<h1 style='color:white;'>We have searched all records and did not find any transaction for this date "
								+ datePosted
								+ " as was specified in the search criteria.<br>Could you please supply another date, or change the search criteria.</h1>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			}

		} catch (SQLException e0) {
			e0.printStackTrace();
		}

	}

	public double getTotal() {
		return total;
	}

}
