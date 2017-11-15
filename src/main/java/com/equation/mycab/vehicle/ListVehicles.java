package com.equation.mycab.vehicle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.equation.print.utils.print.PrintCurrentPage;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
public class ListVehicles {

	Statement stm, stmt;
	ResultSet rs, rs1;

	public ListVehicles(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
	}

	public void searchData() {
		String query = "SELECT vehicleId,regNumber,engineNumber,model,color,description,dateReceived FROM vehicle";
		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			int rows = rs1.getRow();
			if (rows > 0) {
				Table table = new Table();
				table.addContainerProperty("Description", String.class, null);
				table.addContainerProperty("Registration", String.class, null);
				table.addContainerProperty("Engine Number", String.class, null);
				table.addContainerProperty("Model", String.class, null);

				String query1 = "SELECT vehicleId,regNumber,engineNumber,model,color,description,dateReceived FROM vehicle";
				rs = stm.executeQuery(query1);
				int i = 0;
				while (rs.next()) {
					String descript = rs.getString(6);

					String reg = rs.getString(2);
					String eng = rs.getString(3);
					String mode = rs.getString(5) + " " + rs.getString(4);

					table.addItem(new Object[] { descript, String.valueOf(reg), String.valueOf(eng), mode },
							new Integer(i));
					i++;
				}
				int size = table.size();
				table.setSelectable(true);
				table.setPageLength(size);
				table.setFooterVisible(true);
				table.setColumnCollapsingAllowed(true);
				table.setSizeFull();
				table.setColumnFooter("Description", String.valueOf(size) + " Records Found");

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
				HorizontalLayout horizontalLayoutb = new HorizontalLayout(download, print, close);
				horizontalLayoutb.addStyleName(ValoTheme.LAYOUT_WELL);
				horizontalLayoutb.setWidth("100%");

				Label bal = new Label("Number of cars " + rows);
				bal.addStyleName(ValoTheme.LABEL_BOLD);
				bal.addStyleName(ValoTheme.LABEL_LARGE);
				bal.addStyleName(ValoTheme.LABEL_COLORED);
				HorizontalLayout horizontalLayout = new HorizontalLayout(bal, horizontalLayoutb);

				VerticalLayout layout = new VerticalLayout(horizontalLayout, table);
				layout.setComponentAlignment(horizontalLayout, Alignment.TOP_CENTER);

				new ListCarsPDF().generatePDF(table, download, Integer.toString(size));

				window.setContent(layout);
				UI.getCurrent().addWindow(window);

			} else {
				new Notification(
						"<h1 style='color:white;'>There is nothing so far in the Cars list.<br/>You can check after any new car registration<h1/>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

}
