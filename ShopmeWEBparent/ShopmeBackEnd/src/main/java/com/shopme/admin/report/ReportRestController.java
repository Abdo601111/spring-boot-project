package com.shopme.admin.report;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportRestController {
	
	@Autowired
	private MasterOrderReportService masterOrderReportService;
	@Autowired
	private OrderDetailsReportService orderDetailsReportService;
	
	@GetMapping("/reports/sales_by_date/{period}")
	public List<ReportItem> getReportByDataPeriod(@PathVariable("period") String period){
		
		switch (period) {
		case "last_7_Days": 
			return masterOrderReportService.getDateForLast7Days(ReportType.DAYS);
			
		case "last_28_Days": 
			return masterOrderReportService.getDateForLast28Days(ReportType.DAYS);
			
		case "last_6_months": 
			return masterOrderReportService.getDateForLast6Months(ReportType.MONTH);
			
		case "last_year": 
			return masterOrderReportService.getDateForLastYears(ReportType.MONTH);
		default:
			return masterOrderReportService.getDateForLast7Days(ReportType.DAYS);		
			}
		
	}

	
	@GetMapping("/reports/sales_by_date/{startDate}/{endDate}")
	public List<ReportItem> getReportByDataPeriod(@PathVariable("startDate") String startDate
			,@PathVariable("endDate") String endDate) throws ParseException{
		
		DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime= dataFormat.parse(startDate);
		Date endTime= dataFormat.parse(endDate);
		
		
		
		return masterOrderReportService.getReportByDateRang(startTime, endTime,ReportType.DAYS);
	}
	
	
	@GetMapping("/reports/{groupBy}/{period}")
	public List<ReportItem> getReportGroupbyPeriod(@PathVariable("groupBy") String groupBy,
			@PathVariable("period") String period){
		
		ReportType reportType= ReportType.valueOf(groupBy.toUpperCase());
		
		switch (period) {
		case "last_7_Days": 
			return orderDetailsReportService.getDateForLast7Days(reportType);
			
		case "last_28_Days": 
			return orderDetailsReportService.getDateForLast28Days(reportType);
			
		case "last_6_months": 
			return orderDetailsReportService.getDateForLast6Months(reportType);
			
		case "last_year": 
			return orderDetailsReportService.getDateForLastYears(reportType);
		default:
			return orderDetailsReportService.getDateForLast7Days(reportType);		
			}
		
	}
	
	
	
}

