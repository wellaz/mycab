package com.equation.mycab.driver.cashin.printable;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import com.equation.mycab.printsupport.PrintSupport;

public class MakeCashInReceipt {

	public MakeCashInReceipt() {
		// TODO Auto-generated constructor stub
	}

	public void makeReceipt(String receiver, String receiptnumber, String vehicleName, String driverName,
			String amountPaid, String currency) {
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setPrintable(new MyCashInPrintable(receiver, receiptnumber, vehicleName, driverName, amountPaid, currency),
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