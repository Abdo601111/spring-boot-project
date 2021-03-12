package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.common.entity.User;

public class UserExcelExporter extends ApstractExport {
	
	private XSSFWorkbook work;
	 private XSSFSheet sheet;
	 
	 public UserExcelExporter() {
		 work = new XSSFWorkbook();
	 }
	 
	 public void WriteSheet() {
		 
		 sheet = work.createSheet("Users");
		 XSSFRow row = sheet.createRow(0);
		 

			XSSFCellStyle cellStyle= work.createCellStyle();
			XSSFFont font = work.createFont();
			font.setBold(true);
			font.setFontHeight(16);
			cellStyle.setFont(font);
			cell(row,0,"User Id",cellStyle);
			cell(row,1,"Email",cellStyle);
			cell(row,2,"First Name",cellStyle);
			cell(row,3,"Last Name",cellStyle);
			cell(row,4,"Role",cellStyle);
			cell(row,5,"Enable",cellStyle);
	 }
	 
	 public void cell(XSSFRow row,int index,Object value,XSSFCellStyle cellStyle) {
		 
			XSSFCell cell = row.createCell(index);
			 
		 if(value instanceof Integer) {
			 cell.setCellValue((Integer)value);
			 
		 }else if(value instanceof Boolean) {
			 cell.setCellValue((Boolean)value);

		 }else {
			 cell.setCellValue((String)value);

		 }

		
	 }
	 
	
	public void export(List<User>listUser,HttpServletResponse response) throws IOException {
		super.export(response, "application/octet-stream", ".xlsx");
		
		
		
		
		
		
		WriteSheet() ;
		writeDateLine(listUser);
		
		ServletOutputStream outPutStreem = response.getOutputStream();
		work.write(outPutStreem);
		work.close();
		outPutStreem.close();

	}

	private void writeDateLine(List<User> listUser) {
		int row = 1;
		XSSFCellStyle cellStyle= work.createCellStyle();
		XSSFFont font = work.createFont();
		font.setBold(true);
		font.setFontHeight(14);
		cellStyle.setFont(font);
		
		for (User user :listUser) {
			 XSSFRow rows = sheet.createRow(row++);
			 int columnIndex=0;
			 cell(rows,columnIndex++,user.getId(),cellStyle);
			 cell(rows,columnIndex++,user.getEmail(),cellStyle);
			 cell(rows,columnIndex++,user.getFirstName(),cellStyle);
			 cell(rows,columnIndex++,user.getLastName(),cellStyle);
			 cell(rows,columnIndex++,user.getRole().toString(),cellStyle);
			 cell(rows,columnIndex++,user.isEnabled(),cellStyle);
		}
		
	}
}
