package com.equation.mycab.partner.withdrawal.print;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import com.equation.mycab.printsupport.PrintSupport;

/**
 *
 * @author Wellington
 */

public class MakeReimburseReceipt {

	public void makeReceipt(String receiver, String receiptnumber, String partner, String amountPaid, String currency,
			String balance, String principal, String due) {
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setPrintable(
				new ReimbursePrintable(receiver, receiptnumber, partner, amountPaid, currency, balance, principal, due),
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
