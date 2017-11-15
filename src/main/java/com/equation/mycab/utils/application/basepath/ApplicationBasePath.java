package com.equation.mycab.utils.application.basepath;

import com.vaadin.server.VaadinService;

/**
 *
 * @author Wellington
 */
public class ApplicationBasePath {
	public ApplicationBasePath() {
		// TODO Auto-generated constructor stub
	}

	public static String basePath() {
		return VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	}

}
