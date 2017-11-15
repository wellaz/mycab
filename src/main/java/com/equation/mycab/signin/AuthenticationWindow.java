
package com.equation.mycab.signin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.equation.mycab.pagetitles.PageTitle;
import com.equation.mycab.view.MainView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class AuthenticationWindow extends CustomComponent implements View {
	ResultSet rs, rs1;
	Statement stm, stmt;
	String id;
	private TextField username;
	static final String address = "signup";

	public AuthenticationWindow(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1) {
		this.rs = rs;
		this.stm = stm;
		this.rs1 = rs1;
		this.stmt = stmt;
		username = new TextField("<h2>Username<h2/>");
		username.setCaptionAsHtml(true);
		username.addStyleName("username");
		username.setPlaceholder("Username");
		PasswordField password = new PasswordField("<h2>Password<h2/>");
		password.setCaptionAsHtml(true);
		password.addStyleName("password");
		password.setDescription("Password should be more than 7 characters");
		Button submit = new Button("Sign In");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.setIcon(VaadinIcons.PLAY_CIRCLE_O);
		submit.addClickListener((e) -> {
			String usernametxt = username.getValue();
			String passwordtxt = password.getValue();
			if (!(usernametxt.equals(""))) {
				if (!passwordtxt.equals("")) {
					String query = "SELECT partnerId,firstName,lastName,mobile FROM partner WHERE mobile ='"
							+ usernametxt + "' AND mobile = '" + passwordtxt + "'";

					try {
						this.rs = this.stm.executeQuery(query);
						this.rs.last();
						int rows = this.rs.getRow();
						// System.out.println(rows);
						if (rows == 1) {
							id = this.rs.getString(1);
							String URL = "Home";
							MainView mainView = new MainView(stm, stmt, rs, rs1, id);

							UI.getCurrent().getNavigator().addView(URL, mainView);
							UI.getCurrent().getNavigator().navigateTo(URL);
							Page.getCurrent().setTitle(PageTitle.HOME);

						} else {
							new Notification(
									"<h1 style = 'color:white;'>User account does not exist!!!<br/>Make user you have entered correct user details.<br/>Please make sure that you are part of this organisation!<h1/>",
									"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
						}

					} catch (SQLException ex) {
						ex.printStackTrace();
						new Notification(
								"<h1 style='color:white;'>Fatal error occured<br/>User details could not be verified<h1/>",
								"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
					}

				} else {
					new Notification(
							"<h1 style = 'color:white;'>Password is blank!!!<br/>Make user you have entered correct user details.<br/>Please make sure that you are part of this organisation!<h1/>",
							"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
					password.focus();
				}

			} else {
				new Notification(
						"<h1 style = 'color:white;'>Username is blank!!!<br/>Make user you have entered correct user details.<br/>Please make sure that you are part of this organisation!<h1/>",
						"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				username.focus();
			}

		});
		FormLayout logInForm = new FormLayout(username, password, submit);
		Panel panel = new Panel("Sign In");
		panel.setContent(logInForm);
		panel.setIcon(VaadinIcons.USERS);
		panel.addStyleName("login");
		panel.setWidth("400px");
		// panel.setHeight("00px");

		HorizontalLayout layout = new HorizontalLayout(panel);
		layout.addStyleName("login_layout");
		VerticalLayout layout2 = new VerticalLayout(new SignInBanner(), layout);
		layout2.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
		this.setCompositionRoot(layout2);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		username.focus();
	}
}