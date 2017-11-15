package com.equation.mycab.partner.withdrawal.print;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import com.equation.mycab.printsupport.PrintSupport;

/**
 *
 * @author Wellington
 */

public class MakeWithdrawalReceipt {

	public void makeReceipt(String amoun, String partner, String due, String dated, String receiver,
			String receiptNumber, String currency) {
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setPrintable(new WithdrawalPrintable(amoun, partner, due, dated, receiver, receiptNumber, currency),
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
