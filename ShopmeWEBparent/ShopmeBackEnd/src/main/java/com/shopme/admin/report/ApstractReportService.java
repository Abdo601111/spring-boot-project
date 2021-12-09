package com.shopme.admin.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class  ApstractReportService {
	
	protected DateFormat dataFormat;

	public List<ReportItem> getDateForLast7Days(ReportType reportType) {

		return getDateForLastxDays(7,reportType);
	}
	
	public List<ReportItem> getDateForLast28Days(ReportType reportType) {

		return getDateForLastxDays(28,reportType);
	}

	protected List<ReportItem> getDateForLastxDays(int days,ReportType reportType) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
		Date startTime = cal.getTime();

		dataFormat = new SimpleDateFormat("yyyy-MM-dd");

		return getReportByDateRangEnternal(startTime, endTime,reportType);
	}
	

	public List<ReportItem> getDateForLast6Months(ReportType reportType) {
		
		
		return getDateForLastxMonths(6,reportType);
	}
	
public List<ReportItem> getDateForLastYears(ReportType reportType) {
		
		
		return getDateForLastxMonths(12,reportType);
	}
	
	
	
	protected List<ReportItem> getDateForLastxMonths(int months,ReportType reportType) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -(months - 1));
		Date startTime = cal.getTime();

		dataFormat = new SimpleDateFormat("yyyy-MM");

		return getReportByDateRangEnternal(startTime, endTime,reportType);
	}
	
	
	public  List<ReportItem> getReportByDateRang(Date startTime, Date endTime,ReportType reportType){
	dataFormat = new SimpleDateFormat("yyyy-MM-dd");
	return getReportByDateRangEnternal(startTime,endTime,reportType);
}
	
	protected abstract List<ReportItem> getReportByDateRangEnternal(Date startDate,Date endDate,ReportType reportType);

	

}
