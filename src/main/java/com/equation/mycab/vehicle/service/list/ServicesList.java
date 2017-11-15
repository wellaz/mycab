package com.equation.mycab.vehicle.service.list;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.equation.mycab.vehicle.AllVehicles;
import com.equation.mycab.vehicle.service.span.GetServiceSpan;
import com.equation.print.utils.print.PrintCurrentPage;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
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
public class ServicesList {
	ResultSet rs, rs1;
	Statement stm, stmt;
	AllVehicles allVehicles;

	public ServicesList(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		allVehicles = new AllVehicles(stm, stmt, rs, rs1);
	}

	public void listData() {
		ArrayList<String> allcars = allVehicles.getRegNumbers();
		int size = allcars.size();
		if (size > 0) {
			TabSheet tabs = new TabSheet();
			tabs.addStyleName(ValoTheme.TABSHEET_FRAMED);
			tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
			tabs.setSizeFull();

			allcars.stream().forEach(car -> {
				String vehicleId = (String) AllVehicles.getVehicleId(allVehicles.getRegNumberAndId(), car).toString();
				String query = "SELECT dateofservice,distance,unit FROM mileage WHERE vehicleId = '" + vehicleId + "'";

				VerticalLayout layout = new VerticalLayout();

				try {
					rs1 = stmt.executeQuery(query);
					rs1.last();
					int rows = rs1.getRow(), i = 0;
					if (rows > 0) {
						Table table = new Table();
						table.addContainerProperty("Last Date Of Service", String.class, null);
						table.addContainerProperty("Mileage", String.class, null);
						LinkedHashMap<String, String> hash = new LinkedHashMap<>();

						String query1 = "SELECT dateofservice,distance,unit FROM mileage WHERE vehicleId = '"
								+ vehicleId + "' ORDER BY mileageId DESC";
						rs = stm.executeQuery(query1);
						while (rs.next()) {
							String dateofservice = rs.getString(1);
							String distance = rs.getString(2);
							String unit = rs.getString(3);
							hash.put(unit, "" + distance);
							table.addItem(new Object[] { dateofservice, distance + unit }, new Integer(i));
							i++;
						}
						table.setSizeFull();
						table.setSelectable(true);
						table.setColumnCollapsingAllowed(true);
						table.setFooterVisible(true);
						int numberofservices = table.size();
						Label numberofserviceslbl = new Label("Number of services: " + numberofservices);
						numberofserviceslbl.addStyleName(ValoTheme.LABEL_HUGE);
						numberofserviceslbl.addStyleName(ValoTheme.LABEL_BOLD);

						Button download = new Button("Download Report");
						download.addStyleName(ValoTheme.BUTTON_PRIMARY);
						download.setIcon(VaadinIcons.DOWNLOAD);

						Button print = new Button("Print");
						print.addStyleName(ValoTheme.BUTTON_PRIMARY);
						print.setIcon(VaadinIcons.PRINT);
						print.addClickListener(e -> PrintCurrentPage.print());

						Map.Entry<String, String> entry = hash.entrySet().iterator().next();
						String un = entry.getKey();
						String dist = entry.getValue();

						String servicespan = new GetServiceSpan(rs, rs1, stm, stmt).getSpanFor(vehicleId);
						int nextschedule = Integer.parseInt(dist) + Integer.parseInt(servicespan);

						Label nextschedulelbl = new Label();
						nextschedulelbl.addStyleName(ValoTheme.LABEL_COLORED);
						nextschedulelbl.addStyleName(ValoTheme.LABEL_H2);

						nextschedulelbl.setCaption("Next Service for this car is scheduled at " + nextschedule + un);

						HorizontalLayout horizontalLayout = new HorizontalLayout(numberofserviceslbl, download, print);

						layout.addComponents(horizontalLayout, nextschedulelbl, table);
						layout.setComponentAlignment(horizontalLayout, Alignment.TOP_CENTER);
						layout.setComponentAlignment(tabs, Alignment.MIDDLE_CENTER);

					} else {
						Label noservice = new Label("No services found");
						noservice.addStyleName(ValoTheme.LABEL_FAILURE);
						noservice.addStyleName(ValoTheme.LABEL_HUGE);
						layout.addComponent(noservice);
						layout.setComponentAlignment(noservice, Alignment.MIDDLE_CENTER);
					}
				} catch (SQLException ee) {
					ee.printStackTrace();
				}

				tabs.addTab(layout, car, VaadinIcons.CAR);

			});

			Window window = new Window();
			window.center();
			window.setModal(true);
			window.setSizeFull();

			Button close = new Button("Close");
			close.addStyleName(ValoTheme.BUTTON_DANGER);
			close.setIcon(VaadinIcons.CLOSE);
			close.addClickListener(e -> {
				UI.getCurrent().removeWindow(window);
				window.close();
			});
			String narration = (size > 1) ? "vehicles were" : "vehicle";

			Label number = new Label("" + size + " " + narration + " found. Navigate each vehicle record.");
			number.addStyleName(ValoTheme.LABEL_HUGE);
			number.addStyleName(ValoTheme.LABEL_COLORED);

			HorizontalLayout top = new HorizontalLayout(close, number);
			top.setExpandRatio(number, 3);

			VerticalLayout content = new VerticalLayout(top, tabs);
			content.setComponentAlignment(top, Alignment.TOP_LEFT);

			window.setContent(content);

			UI.getCurrent().addWindow(window);

		} else {
			new Notification("<h1 style='color:white;'>There are no vehicles yet registered in the system<h1/>", "",
					Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
		}
	}
}