package com.equation.mycab.utils.pdf.headerfooter;

import com.equation.mycab.utils.date.DateUtility;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

@SuppressWarnings("restriction")
public class PDFHeaderFooter extends PdfPageEventHelper {
	public PDFHeaderFooter() {
		return;
	}

	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(""), rect.getLeft(),
				rect.getTop(), 0);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase("Page " + document.getPageNumber(),
						FontFactory.getFont(FontFactory.TIMES_ITALIC, 5, Font.ITALIC, BaseColor.BLACK)),
				rect.getRight(), rect.getTop(), 0);
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(""), rect.getLeft(),
				rect.getBottom(), 0);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase("\u00A9 " + new DateUtility().getYear() + " Equation \u2122  W M",
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, Font.ITALIC, BaseColor.DARK_GRAY)),
				rect.getRight(), rect.getBottom(), 0);
	}

}
