package com.shopme.admin.customer;

import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.user.ApstractExport;
import com.shopme.common.entity.Customer;


public class CustomerScvExporter  extends ApstractExport{
	
	public void export(List<Customer>listCustomer,HttpServletResponse response) throws IOException {
	super.export(response, "text/csv", ".csv");
		
		ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String []scvHeader= {"Customer Id","Customer Email","First Name","Last Name","City","State","Countryt","Enabled"};
		String []fillMapping= {"id","email","firstName","lastName","sity","state","country","enabled"};
		
		writer.writeHeader(scvHeader);

		
		for (Customer user : listCustomer) {
			
			writer.write(user, fillMapping);
		}

		writer.close();
		
	}

}
