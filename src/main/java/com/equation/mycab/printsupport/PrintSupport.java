package com.equation.mycab.printsupport;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;

/**
 *
 * @author Wellington
 */
public class PrintSupport {

	public PrintSupport() {
		return;
	}

	public static PageFormat getPageFormat(PrinterJob pj, double rowcount) {
		PageFormat pf = pj.defaultPage();
		Paper paper = pf.getPaper();

		double middleHeight = rowcount * 5.0; // dynamic----->change
												// with the row count of
												// jtable
		double headerHeight = 5.0; // fixed----->but can be mod
		double footerHeight = 5.0; // fixed----->but can be mod

		double width = convert_CM_To_PPI(7); // printer know only point per
												// inch.default value is 72ppi
		double height = convert_CM_To_PPI(headerHeight + middleHeight + footerHeight);
		paper.setSize(width, height);
		paper.setImageableArea(convert_CM_To_PPI(0.25), convert_CM_To_PPI(0.5), width - convert_CM_To_PPI(0.35),
				height - convert_CM_To_PPI(1)); // define boarder size after
												// that print area width is
												// about 180 points

		pf.setOrientation(PageFormat.PORTRAIT); // select orientation portrait
												// or landscape but for this
												// time portrait
		pf.setPaper(paper);

		return pf;
	}

	protected static double convert_CM_To_PPI(double cm) {
		return toPPI(cm * 0.393600787);
	}

	protected static double toPPI(double inch) {
		return inch * 72d;
	}
}