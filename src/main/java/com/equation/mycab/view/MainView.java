
package com.equation.mycab.view;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;

import com.equation.mycab.accounts.revenue.RevenueTransactions;
import com.equation.mycab.dividend.search.SearchDividend;
import com.equation.mycab.driver.CashIn;
import com.equation.mycab.driver.NewDriver;
import com.equation.mycab.driver.SearchDriverTransactions;
import com.equation.mycab.netcash.Net;
import com.equation.mycab.partners.AddCurrency;
import com.equation.mycab.partners.AddPartner;
import com.equation.mycab.partners.CaptureDividend;
import com.equation.mycab.partners.CaptureExpense;
import com.equation.mycab.partners.CaptureInvestment;
import com.equation.mycab.partners.CaptureWithdrawal;
import com.equation.mycab.receipts.reprint.ListReceipts;
import com.equation.mycab.util.maintab.InsertTab;
import com.equation.mycab.utils.application.basepath.ApplicationBasePath;
import com.equation.mycab.utils.date.DateUtility;
import com.equation.mycab.vehicle.ListVehicles;
import com.equation.mycab.vehicle.NewVehicle;
import com.equation.mycab.vehicle.service.span.CaptureServiceSpan;
import com.equation.mycab.vehicle.services.CaptureCarService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MainView extends CustomComponent implements View {
	Statement stm, stmt;
	ResultSet rs, rs1;

	NewDriver driver;
	NewVehicle newVehicle;
	InsertTab insertTab;
	CashIn cashIn;
	AddPartner addPartner;
	CaptureDividend captureDividend;
	CaptureExpense captureExpense;
	CaptureInvestment captureInvestment;
	AddCurrency addCurrency;
	CaptureWithdrawal captureWithdrawal;

	SearchDriverTransactions driverTransactions;

	SearchDividend searchDividend;

	String id;

	public MainView(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1, String id) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		this.id = id;
		driver = new NewDriver(stm, stmt, rs, rs1);
		newVehicle = new NewVehicle(stm, stmt, rs, rs1);
		cashIn = new CashIn(stmt, stmt, rs1, rs1, id);
		addPartner = new AddPartner(rs, rs1, stm, stmt);
		captureDividend = new CaptureDividend(rs, rs1, stm, stmt, id);
		captureExpense = new CaptureExpense(stm, stmt, rs, rs1);
		captureInvestment = new CaptureInvestment(stm, stmt, rs, rs1, id);
		addCurrency = new AddCurrency(rs, rs1, stm, stmt);
		captureWithdrawal = new CaptureWithdrawal(stm, stmt, rs, rs1, id);

		searchDividend = new SearchDividend(rs, rs1, stm, stmt);

		driverTransactions = new SearchDriverTransactions(rs, rs1, stm, stmt);

		this.setCompositionRoot(createMainLayout());
	}

	private VerticalLayout createMainLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(false);
		layout.setStyleName("main_container");
		
		HorizontalLayout topBanner = new HorizontalLayout();
		topBanner.addStyleName(ValoTheme.WINDOW_TOP_TOOLBAR);

		TabSheet tabs = new TabSheet();
		tabs.addTab(welcomeWindow(), "Home", VaadinIcons.HOME_O);
		tabs.addStyleName(ValoTheme.TABSHEET_FRAMED);
		tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		tabs.setSizeFull();

		topBanner.addComponent(createToolbar(tabs));

		VerticalLayout footer = new VerticalLayout();
		footer.addStyleName(ValoTheme.LAYOUT_WELL);

		Label flabel = new Label(
				"&copy;" + new DateUtility().getYear()
						+ "<i> <span style='display:inline-block; width: 80px;'></span>\u2211quation <i/> (wm) ",
				ContentMode.HTML);
		flabel.addStyleName(ValoTheme.LABEL_LIGHT);

		footer.addComponent(flabel);

		layout.addComponents(topBanner, tabs, footer);
		layout.setExpandRatio(tabs, 1.0f);

		return layout;
	}

	private HorizontalLayout createToolbar(TabSheet tabs) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setStyleName("mytoolbar_container");
		TabSheet toolbar = new TabSheet();
		// toolbar.addStyleName(ValoTheme.TABSHEET_FRAMED);
		toolbar.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

		Button newVehicles = new Button("New Vehicle");
		newVehicles.addStyleName(ValoTheme.BUTTON_QUIET);
		newVehicles.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		newVehicles.addClickListener((e) -> {
			InsertTab.insertTab(tabs, newVehicle, "New Vehicle", VaadinIcons.CAR);
		});

		Button newDriver = new Button("New Driver");
		newDriver.addStyleName(ValoTheme.BUTTON_QUIET);
		newDriver.addClickListener((e) -> {
			InsertTab.insertTab(tabs, driver, "New Driver", VaadinIcons.USER);
		});
		Button cashin = new Button("Cash In");
		cashin.addStyleName(ValoTheme.BUTTON_QUIET);
		cashin.addClickListener((e) -> {
			InsertTab.insertTab(tabs, cashIn, "Cash In", VaadinIcons.MONEY);
		});
		Button newpartner = new Button("Add Partner");
		newpartner.addStyleName(ValoTheme.BUTTON_QUIET);
		newpartner.addClickListener((e) -> {
			InsertTab.insertTab(tabs, addPartner, "New Partner", VaadinIcons.USER);
		});
		Button dividend = new Button("Dividend");
		dividend.addStyleName(ValoTheme.BUTTON_QUIET);
		dividend.addClickListener((e) -> {
			InsertTab.insertTab(tabs, captureDividend, "Dividend", VaadinIcons.CASH);
		});

		Button expense = new Button("Expenses");
		expense.addStyleName(ValoTheme.BUTTON_QUIET);
		expense.addClickListener((e) -> {
			InsertTab.insertTab(tabs, captureExpense, "Expenses", VaadinIcons.SEARCH_MINUS);
		});

		Button createInvestment = new Button("Investment");
		createInvestment.addStyleName(ValoTheme.BUTTON_QUIET);
		createInvestment.addClickListener((e) -> {
			InsertTab.insertTab(tabs, captureInvestment, "Investment", VaadinIcons.MONEY_DEPOSIT);
		});

		Button currency = new Button("Currency");
		currency.addStyleName(ValoTheme.BUTTON_QUIET);
		currency.addClickListener((e) -> {
			InsertTab.insertTab(tabs, addCurrency, "Currency", VaadinIcons.MONEY_EXCHANGE);
		});

		Button withdraw = new Button("Withdrawals");
		withdraw.addStyleName(ValoTheme.BUTTON_QUIET);
		withdraw.addClickListener((e) -> {
			InsertTab.insertTab(tabs, captureWithdrawal, "Withdrawal", VaadinIcons.MONEY_WITHDRAW);
		});

		Button searchDriverTransact = new Button("Statements");
		searchDriverTransact.addStyleName(ValoTheme.BUTTON_QUIET);
		searchDriverTransact.addClickListener((e) -> {
			InsertTab.insertTab(tabs, driverTransactions, "Driver Statements", VaadinIcons.FOLDER_SEARCH);
		});

		Button searchPartnerTransact = new Button("Statements");
		searchPartnerTransact.addStyleName(ValoTheme.BUTTON_QUIET);
		searchPartnerTransact.addClickListener((e) -> {
			InsertTab.insertTab(tabs, searchDividend, "Partner Dividend", VaadinIcons.FOLDER_SEARCH);
		});
		Button revenue = new Button("Revenue Statements");
		revenue.addStyleName(ValoTheme.BUTTON_QUIET);
		revenue.addClickListener((e) -> {
			new RevenueTransactions(stm, stmt, rs, rs1).searchData();
		});

		Button listvehi = new Button("All Cars");
		listvehi.addStyleName(ValoTheme.BUTTON_QUIET);
		listvehi.addClickListener((e) -> {
			new ListVehicles(stm, stmt, rs, rs1).searchData();
		});

		Button netcash = new Button("Net Cash");
		netcash.addStyleName(ValoTheme.BUTTON_QUIET);
		netcash.addClickListener((e) -> {
			Net net = new Net(rs, rs1, stm, stmt);
			InsertTab.insertTab(tabs, net, "Net Cash", VaadinIcons.CASH);
		});

		Button mileage = new Button("Car Servicing");
		mileage.addStyleName(ValoTheme.BUTTON_QUIET);
		mileage.addClickListener((e) -> {
			CaptureCarService captureMileage = new CaptureCarService(rs, rs1, stm, stmt, id);
			InsertTab.insertTab(tabs, captureMileage, "Car Servicing", VaadinIcons.CASH);
		});

		Button reprint = new Button("Receipts");
		reprint.addStyleName(ValoTheme.BUTTON_QUIET);
		reprint.addClickListener((e) -> {
			ListReceipts listReceipts = new ListReceipts(rs, rs1, stm, stmt);
			listReceipts.listReceipts();
		});

		Button span = new Button("Car Service Span");
		span.setDescription("Set the car service span");
		span.addStyleName(ValoTheme.BUTTON_QUIET);
		span.addClickListener((e) -> {
			CaptureServiceSpan captureServiceSpan = new CaptureServiceSpan(stm, stmt, rs, rs1);
			InsertTab.insertTab(tabs, captureServiceSpan, "Service Span", VaadinIcons.CASH);
		});

		HorizontalLayout driver = new HorizontalLayout(newDriver, cashin, searchDriverTransact);
		HorizontalLayout vehicle = new HorizontalLayout(newVehicles, listvehi, mileage);
		HorizontalLayout partner = new HorizontalLayout(newpartner, dividend, createInvestment, expense, withdraw,
				searchPartnerTransact);
		HorizontalLayout revenues = new HorizontalLayout(netcash, revenue);
		HorizontalLayout settings = new HorizontalLayout(currency, span);
		HorizontalLayout receipts = new HorizontalLayout(reprint);

		toolbar.addTab(settings, "Settings", VaadinIcons.CONTROLLER);
		toolbar.addTab(vehicle, "Vehicles", VaadinIcons.CAR);
		toolbar.addTab(partner, "Partners", VaadinIcons.USERS);
		toolbar.addTab(driver, "Drivers", VaadinIcons.USERS);
		toolbar.addTab(revenues, "Revenue", VaadinIcons.PIE_CHART);
		toolbar.addTab(receipts, "Receipts", VaadinIcons.RECORDS);

		layout.addComponent(toolbar);
		return layout;
	}

	private VerticalLayout welcomeWindow() {
		VerticalLayout layout = new VerticalLayout();

		Image car = new Image("",
				new FileResource(new File(ApplicationBasePath.basePath() + "/WEB-INF/images/fiat.png")));
		Label equation = new Label(
				"<center><h1><b>M y C a b</b></h1><br><i>powered by</i><br><h2><i>E Q U A T I O N</i></h2></center>",
				ContentMode.HTML);
		equation.addStyleName(ValoTheme.LABEL_COLORED);
		equation.addStyleName(ValoTheme.LABEL_LARGE);
		equation.addStyleName(ValoTheme.LABEL_BOLD);

		Image eqimage = new Image("",
				new FileResource(new File(ApplicationBasePath.basePath() + "/WEB-INF/images/sigma_burned.png")));

		car.setWidth("450px");
		car.setHeight("250px");
		eqimage.setWidth("250px");
		eqimage.setHeight("250px");

		HorizontalLayout horizontalLayout = new HorizontalLayout(car, equation, eqimage);

		layout.addComponents(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

		return layout;
	}
}