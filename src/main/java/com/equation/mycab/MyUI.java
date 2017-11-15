package com.equation.mycab;

import javax.servlet.annotation.WebServlet;

import com.equation.mycab.connection.ConnectionPool;
import com.equation.mycab.pagetitles.PageTitle;
import com.equation.mycab.signin.AuthenticationWindow;
import com.equation.mycab.view.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@SuppressWarnings("serial")
@Theme("mycab")
public class MyUI extends UI {
	private final static String HOME = "Home";
	private final static String LOGIN = "Login";

	ConnectionPool pool;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		pool = new ConnectionPool();
		pool.connectionDb();

		String id = "1";

		Navigator navigator = new Navigator(this, this);
		MainView mainView = new MainView(pool.stm, pool.stmt, pool.rs, pool.rs1, id);
		AuthenticationWindow authenticationWindow = new AuthenticationWindow(pool.stm, pool.stmt, pool.rs, pool.rs1);
		navigator.addView(HOME, mainView);
		navigator.addView(LOGIN, authenticationWindow);
		navigator.navigateTo(HOME);

		Page.getCurrent().setTitle(PageTitle.HOME);

	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}