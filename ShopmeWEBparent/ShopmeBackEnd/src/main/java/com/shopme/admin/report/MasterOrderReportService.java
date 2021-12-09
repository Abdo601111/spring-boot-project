package com.shopme.admin.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.order.OrderRepository;
import com.shopme.common.entity.Order;

@Service
public class MasterOrderReportService  extends ApstractReportService{

	@Autowired
	private OrderRepository repo;


	protected List<ReportItem> getReportByDateRangEnternal(Date startTime, Date endTime,ReportType reportType) {
		List<Order> orderList = repo.findByOrderTimeBetween(startTime, endTime);
		printRowData(orderList);

		List<ReportItem> listReportItems = createReportDate(startTime, endTime,reportType);

		
		 calculateSalesForReportData(orderList,listReportItems);
		printReportData(listReportItems);
		return listReportItems;
	}

	
	
	private void calculateSalesForReportData(List<Order> orderList,List<ReportItem> listReportItems) {
		
		for (Order order : orderList) {
			String orderDataString=dataFormat.format(order.getOrderTime());
			
			ReportItem reportItem= new ReportItem(orderDataString);
			int indexItem = listReportItems.indexOf(reportItem);
			if(indexItem >=0) {
				reportItem=listReportItems.get(indexItem);
				reportItem.addGrossSeles(order.getTotal());
				reportItem.addNetSales(order.getSubTotal()-order.getProductCost());
				reportItem.incressOrderCount();
			}
		}
		
	}

	
	
	private void printReportData(List<ReportItem> listReportItems) {
		listReportItems.forEach(item -> {
			System.out.printf("%s , %10.2f , %10.2f , %d \n", item.getIdentifier(),item.getGrossSales()
					,item.getNetSales(),item.getOrdersCount());
		});
	}

	private List<ReportItem> createReportDate(Date startTime, Date endTime,ReportType reportType) {

		List<ReportItem> listReportItem = new ArrayList<>();
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(startTime);

		Calendar endDate = Calendar.getInstance();
		endDate.setTime(endTime);

		Date currentDate = startDate.getTime();
		String dataString = dataFormat.format(currentDate);
		listReportItem.add(new ReportItem(dataString));
		do {
			if(reportType.equals(ReportType.DAYS)){
			startDate.add(Calendar.DAY_OF_MONTH, 1);
			}else if(reportType.equals(ReportType.MONTH)) {
				startDate.add(Calendar.MONTH, 1);

			}
			currentDate = startDate.getTime();
			dataString = dataFormat.format(currentDate);
			listReportItem.add(new ReportItem(dataString));

		} while (startDate.before(endDate));

		return listReportItem;
	}

	private void printRowData(List<Order> orderList) {
		orderList.forEach(order -> {
			System.out.printf("%-3d | %s %10.2f | %10.2f\n", order.getId(), order.getOrderTime(), order.getTotal(),
					order.getProductCost());
		});
	}



	
 

	
	

}
