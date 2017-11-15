package com.equation.mycab.partners.dividend.print;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import com.equation.mycab.printsupport.PrintSupport;

/**
 *
 * @author Wellington
 */

public class MakeDividendRecipt {
	public void makeReceipt(String amoun, String partner, String dated, String receiver, String receiptNumber,
			String currency) {
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setPrintable(new DividendPrintable(amoun, partner, dated, receiver, receiptNumber, currency),
				PrintSupport.getPageFormat(pj, 0));
		// boolean ok = pj.printDialog();
		// if (ok) {
		try {
			pj.print();
		} catch (PrinterException ex) {
			ex.printStackTrace();
		}
		// }
	}

}
