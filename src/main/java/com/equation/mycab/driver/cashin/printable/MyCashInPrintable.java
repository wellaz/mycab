package com.equation.mycab.driver.cashin.printable;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import com.equation.mycab.utils.date.DateUtility;

/**
 *
 * @author Wellington
 */
public class MyCashInPrintable implements Printable {

	private String receiver;
	private String receiptnumber;
	private String vehicleName;
	private String driverName;
	private String amountPaid;
	private String currency;

	public MyCashInPrintable(String receiver, String receiptnumber, String vehicleName, String driverName,
			String amountPaid, String currency) {
		this.receiver = receiver;
		this.receiptnumber = receiptnumber;
		this.vehicleName = vehicleName;
		this.driverName = driverName;
		this.amountPaid = amountPaid;
		this.currency = currency;
	}

	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss a";

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		int result = NO_SUCH_PAGE;
		if (pageIndex == 0) {
			Graphics2D g2d = (Graphics2D) graphics;

			g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
			Font font = new Font("Monospaced", Font.BOLD, 7);
			g2d.setFont(font);

			try {
				/*
				 * Draw Image* assume that printing reciept has logo on top that
				 * logo image is in .gif format .png also support image
				 * resolution is width 100px and height 50px image located in
				 * root--->image folder
				 */
				int x = 0; // print start at 0 on x axies
				int y = 10; // print start at 10 on y axies
				int imagewidth = 100;
				int imageheight = 30;
				BufferedImage read = ImageIO.read(this.getClass().getResource("images/mycab.PNG"));
				// draw image
				g2d.drawImage(read, x, y, imagewidth, imageheight, null);
				g2d.drawLine(0, y + 60, 180, y + 60); // draw line
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				/* Draw Header */
				int y = 80;
				g2d.drawString("Cash In Slip".toUpperCase(), 0, y);

				g2d.drawLine(0, y + 10, 180, y + 10);

				g2d.drawString("Date: " + now(), 5, y + 20); // print date
				g2d.drawString("Receiver :" + receiver, 5, y + 30);
				g2d.drawLine(0, y + 40, 180, y + 40);
				g2d.drawString("Receipt Number " + receiptnumber, 30, y + 50);
				g2d.drawString("Vehicle: " + vehicleName, 30, y + 60);
				g2d.drawString("Driver :" + driverName, 30, y + 70);

				// shift a line by adding 10 to y value
				g2d.drawLine(0, y + 80, 180, y + 80);

				g2d.drawString("TOTAL PAID $" + amountPaid, 10, y + 90);
				g2d.drawString("Currency " + currency, 10, y + 100);

				/* Draw Colums */
				g2d.drawLine(0, y + 110, 180, y + 110);

				/* Footer */
				font = new Font("Arial", Font.ITALIC + Font.PLAIN, 5); // changed
																		// font
				// size
				g2d.setFont(font);
				g2d.drawString("\u00A9 " + new DateUtility().getYear() + "\u2192  Equation \u2122 (W.M)", 30, y + 120);
				// end of the reciept
			} catch (Exception r) {
				r.printStackTrace();
			}
			result = PAGE_EXISTS;
		}
		return result;
	}

	public static String now() {
		// get current date and time as a String output
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
}