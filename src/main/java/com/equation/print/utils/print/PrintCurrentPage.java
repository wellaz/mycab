package com.equation.print.utils.print;

/**
 *
 * @author Wellington
 */
public class PrintCurrentPage {
	/**
	 * Print any current page at will
	 */
	public static void print() {
		com.vaadin.ui.JavaScript.getCurrent().execute("setTimeout(function() {" + " print();self.close();},0);");
	}

}
