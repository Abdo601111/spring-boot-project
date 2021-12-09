
var data;
var chartsOptions;
var totalGrossSales;
var totalNatSales;
var totalItems;




$(document).ready(function(){
setupEventHandelar("_date",loadSalesReportByDate);
	
});



 function loadSalesReportByDate(period){
	
	if(period == "custom"){
		
		startDate = $("#startDate_date").val();
		endDate = $("#endDate_date").val();
		requestUrl= contextPath+"reports/sales_by_date/" + startDate + "/" + endDate;
	}else{
			requestUrl= contextPath+"reports/sales_by_date/" + period;

	}
	
	$.get(requestUrl,function(responseJSON){
		perpareChartDataForSalesByDate(responseJSON);
		customizeChartForSalesByDate(period);
		formatChartData(data,1,2);
		drawChartForSalesByDate(period);
		setSalesAmount(period,"_date","Total Item");
		
	});
	}
	
 function perpareChartDataForSalesByDate(responseJSON){
			data = new google.visualization.DataTable();
			data.addColumn('string','Date');
			data.addColumn('number','Gross Sales');
			data.addColumn('number','Net Sales');
			data.addColumn('number','Orders');
			
			 totalGrossSales=0.0;
             totalNatSales=0.0;
              totalItems=0;
			
			$.each(responseJSON,function(index,reportItem){
				data.addRows([[reportItem.identifier,reportItem.grossSales,reportItem.netSales,reportItem.ordersCount]]);
				totalGrossSales += parseFloat(reportItem.grossSales);
				totalNatSales += parseFloat(reportItem.netSales);
				totalItems += parseInt(reportItem.ordersCount);
			});
	}
	
function customizeChartForSalesByDate(period){
	chartsOptions= {
		title: getChartTitle(period),
		'height': 360,
		legend: {position: 'top'},
		series: {
			0: {targetAxisIndex: 0},
			1: {targetAxisIndex: 0},
			2: {targetAxisIndex: 1}
			
		},
		vAxes: {
		 0: {title: 'Sales Amount',format: 'currency'},
	     1: {title: 'Number Of Orders'}
     }
	};
	
	
	}
	
function drawChartForSalesByDate(){
	var salesChart = new google.visualization.ColumnChart(document.getElementById('chart_sales_by_date'));
		salesChart.draw(data,chartsOptions);
		
		
}



