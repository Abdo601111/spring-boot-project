package com.shopme.admin.user;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;



import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;

public class UserPdfExporter extends ApstractExport {

	public void export(List<User> listUser, HttpServletResponse response) throws IOException {
		super.export(response, "application/pdf", ".pdf");
		
		Document doc = new Document(PageSize.A4);
		PdfWriter.getInstance(doc, response.getOutputStream());
		doc.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.BLUE);
		font.setSize(18);
		Paragraph paragraph = new Paragraph("List Of Users",font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		doc.add(paragraph);
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.5f,3.5f,3.5f,3.5f,3.5f,1.5f});
		writeTableHeater(table);
		writeTableData(table,listUser);
		
		doc.add(table);
		doc.close();
	}

	private void writeTableData(PdfPTable table, List<User> listUser) {
		
		for(User user: listUser) {
			
			table.addCell(String.valueOf(user.getId()));
			table.addCell(user.getEmail());
			table.addCell(user.getFirstName());
			table.addCell(user.getLastName());
			table.addCell(user.getRole().toString());
			table.addCell(String.valueOf(user.isEnabled()));
		}
		
	}

	private void writeTableHeater(PdfPTable table) {
		PdfPCell cell= new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.white);
		
		cell.setPhrase(new Phrase("User Id",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Email",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("First name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Last Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Roles",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Enabled",font));
		table.addCell(cell);
		
		
		
		
	}

}
