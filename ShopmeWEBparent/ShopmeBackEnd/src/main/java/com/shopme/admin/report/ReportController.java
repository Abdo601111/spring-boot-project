package com.shopme.admin.report;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {
	
	
	@GetMapping("/reports")
	public String getReport() {
		return "reports/reports";
	}

}
