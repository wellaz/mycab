package com.equation.mycab.netcash;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.equation.mycab.utils.date.DateUtility;
import com.equation.mycab.utils.roundoff.Money;
import com.equation.print.utils.print.PrintCurrentPage;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.Table;

/**
 *
 * @author Wellington
 */

@SuppressWarnings({ "serial", "deprecation" })
public class Net extends CustomComponent {

	ResultSet rs, rs1;
	Statement stm, stmt;

	public Net(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		this.setCompositionRoot(createLayout());
	}

	@SuppressWarnings({ "rawtypes" })
	private VerticalLayout createLayout() {
		VerticalLayout layout = new VerticalLayout();
		Table table = new Table();
		table.addContainerProperty("Description", String.class, null);
		table.addContainerProperty("Debit", String.class, null);
		table.addContainerProperty("Credit", String.class, null);
		table.addContainerProperty("Balance", String.class, null);

		TotalCashIn totalCashIn = new TotalCashIn(rs, rs1, stm, stmt);
		HashMap<String, Double> cashinlist = totalCashIn.getCollection();
		int size = cashinlist.size();
		if (size > 0) {
			Iterator<?> iterator = cashinlist.entrySet().iterator();
			int i = 0;
			while (iterator.hasNext()) {
				Map.Entry map = (Map.Entry) iterator.next();

				String curency = (String) map.getKey();
				double amount = (double) map.getValue();

				table.addItem(new Object[] { "Total Cash In (" + curency + ")", "", String.valueOf(amount), "" },
						new Integer(i));
				i++;
			}
			double cashInGrandTotal = totalCashIn.getTotal();
			table.addItem(new Object[] { "Grand Total ", "", String.valueOf(cashInGrandTotal), "" }, new Integer(i));
			table.addItem(new Object[] { "", "", "", "" }, new Integer(i + 1));

			TotalExpense expense = new TotalExpense(rs, rs1, stm, stmt);
			double expenseGrandTotal = expense.getTotal();
			table.addItem(new Object[] { "Total Expense (Operations)", String.valueOf(expenseGrandTotal), "", "" },
					new Integer(i + 2));

			double totaldividend = new TotalDividend(rs, rs1, stm, stmt).getTotal();

			table.addItem(new Object[] { "Partner Dividend(Total)", String.valueOf(totaldividend), "", "" },
					new Integer(i + 3));

			double totalwithdrawal = new TotalWithdrawal(rs, rs1, stm, stmt).getTotal();
			table.addItem(new Object[] { "Partner Withdrawals(Total)", String.valueOf(totalwithdrawal), "", "" },
					new Integer(i + 4));

			double reimbursemet = new TotalReimbursement(rs, rs1, stm, stmt).getTotal();
			table.addItem(new Object[] { "Partner Reimbursement(Total)", "", String.valueOf(reimbursemet), "" },
					new Integer(i + 5));

			table.addItem(new Object[] { "", "", "", "" }, new Integer(i + 6));

			double netBalance = cashInGrandTotal - (expenseGrandTotal + totaldividend + totalwithdrawal) + reimbursemet;
			table.addItem(new Object[] { "Balance (Net)", "", "", String.valueOf(Money.toMoney(netBalance)) },
					new Integer(i + 7));

			table.setFooterVisible(true);
			table.setSelectable(true);
			table.setColumnFooter("Description", "Net");
			table.setColumnFooter("Balance", String.valueOf(Money.toMoney(netBalance)));

			table.setPageLength(table.size());
			
			
			
			//fr downloading the file, particularly a pdf
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

			FormLayout formLayout = new FormLayout(download, print);

			new NetPDF().generatePDF(table, download, new DateUtility().getDate() + " " + new DateUtility().getTime(),
					Money.toMoney(netBalance));

			HorizontalLayout horizontalLayout = new HorizontalLayout(table, formLayout);
			layout.addComponent(horizontalLayout);
			layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		} else {

			Label nodata = new Label("No Data");
			nodata.addStyleName(ValoTheme.LABEL_FAILURE);
			nodata.addStyleName(ValoTheme.LABEL_HUGE);

			layout.addComponent(nodata);
			layout.setComponentAlignment(nodata, Alignment.MIDDLE_CENTER);

			new Notification("<h1 style='color:white;'>No cash In records were found!<h1/>", "",
					Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());

		}

		return layout;
	}

}
