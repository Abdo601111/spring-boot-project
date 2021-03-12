package com.shopme.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

public class UserScvExporter  extends ApstractExport{
	
	public void export(List<User>listUser,HttpServletResponse response) throws IOException {
	super.export(response, "text/csv", ".csv");
		
		ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String []scvHeader= {"User Id","User Email","First Name","Last Name","Roles","Enabled"};
		String []fillMapping= {"id","email","firstName","lastName","role","enabled"};
		
		writer.writeHeader(scvHeader);

		
		for (User user : listUser) {
			
			writer.write(user, fillMapping);
		}

		writer.close();
		
	}

}
