package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.user.ApstractExport;
import com.shopme.common.entity.Category;

public class CategoryCvsExport extends ApstractExport {
	
	public void export(List<Category>listCategory,HttpServletResponse response) throws IOException {
		super.export(response, "text/csv", ".csv");
			
			ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
			String []scvHeader= {"Id"," Name"," Alias","Enabled"};
			String []fillMapping= {"id","name","alias","enabled"};
			
			writer.writeHeader(scvHeader);

			
			for (Category category : listCategory) {
				
				writer.write(category, fillMapping);
			}

			writer.close();
			
		}


}
