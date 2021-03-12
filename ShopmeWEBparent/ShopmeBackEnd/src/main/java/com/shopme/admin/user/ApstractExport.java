package com.shopme.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;





public class ApstractExport {

	
	
public void export(HttpServletResponse response,String contentType,String extension) throws IOException {
		
	  DateFormat date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timeStamp = date.format(new Date());
	  //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	  System.out.println(timeStamp);
  
		String fileName = "User_" + timeStamp + extension;
		response.setContentType(contentType);
	   String headerKey= "Content-Disposition";
	    String headerVsalue= "attachment; filename="+fileName;
  	   response.setHeader(headerKey, headerVsalue);
//		ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
//		String []scvHeader= {"User Id","User Email","First Name","Last Name","Roles","Enabled"};
//		String []fillMapping= {"id","email","firstName","lastName","role","enabled"};
//		
//		writer.writeHeader(scvHeader);
//
//		
//		for (User user : listUser) {
//			
//			writer.write(user, fillMapping);
//		}
//
//		writer.close();
		
	}
}
