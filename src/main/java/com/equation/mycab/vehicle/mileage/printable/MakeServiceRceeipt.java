package com.equation.mycab.vehicle.mileage.printable;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import com.equation.mycab.printsupport.PrintSupport;

/**
 *
 * @author Wellington
 */

public class MakeServiceRceeipt {

	public void makeReceipt(String lastdateofsercice, String lastmileage, String executive, String vehicleId,
			String nextschedule, String drivername) {
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setPrintable(new NextServicePrintable(lastdateofsercice, lastmileage, executive, vehicleId, nextschedule,
				drivername), PrintSupport.getPageFormat(pj, 0));
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
