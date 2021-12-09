package com.shopme.admin.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.order.OrderDetailRepository;
import com.shopme.common.entity.OrderDetail;

@Service
public class OrderDetailsReportService extends ApstractReportService {

	
	@Autowired private OrderDetailRepository repo;
	
	@Override
	protected List<ReportItem> getReportByDateRangEnternal(Date startDate, Date endDate, ReportType reportType) {
		
		List<OrderDetail> listOrderDetails=null;
		if(reportType.equals(ReportType.CATEGORY)) {
			listOrderDetails= repo.findWithCategoryAndTimeBetween(startDate, endDate);
		}else if(reportType.equals(ReportType.PRODUCT)){
			listOrderDetails= repo.findWithProductAndTimeBetween(startDate, endDate);
		}
		//printRowData(listOrderDetails);
		
		
		
		List<ReportItem> listReportItem= new ArrayList<>();
		
		for (OrderDetail detail : listOrderDetails) {
			String identfier="";
			if(reportType.equals(ReportType.CATEGORY)){
			identfier= detail.getProduct().getCategory().getName();
			}else if(reportType.equals(ReportType.PRODUCT)){
				identfier= detail.getProduct().getName();
			}
			
			
			ReportItem item = new ReportItem(identfier);
			float grossSales= detail.getSubTotal() + detail.getShippingCost();
			float netSales= detail.getSubTotal() - detail.getProductCost();
			
			int indexItem= listReportItem.indexOf(item);
			
			if(indexItem >=0) {
				item= listReportItem.get(indexItem);
				item.addGrossSeles(grossSales);
				item.addNetSales(netSales);
				item.increaseProductCount(detail.getQuantity());
				
			}else {
				listReportItem.add( new ReportItem(identfier, grossSales, netSales ,detail.getQuantity()));
			}

		}
		
		printReportData(listReportItem);
		return listReportItem;
	}



	private void printReportData(List<ReportItem> listReportItem) {
		for (ReportItem item : listReportItem) {
			System.out.printf("%-30s | %10.2f | %10.2f| %10.2f | %10.2f \n"
					,item.getIdentifier(),item.getGrossSales(),item.getNetSales(),item.getProductsCount(),item.getProductsCount());
		}

		
		
	}



//	private void printRowData(List<OrderDetail> listOrderDetails) {
//
//           for (OrderDetail orderDetail : listOrderDetails) {
//        	   System.out.printf("%d, %-20s, %10.2f, %10.2f, %10.2f \n"+orderDetail.getQuantity(),orderDetail.getProduct().getCategory().getName()
//        			   ,orderDetail.getSubTotal(),orderDetail.getProductCost(),orderDetail.getShippingCost());
//	
//           }		
//	}
//	


	
	
	
}
