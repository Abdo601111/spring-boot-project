
var data;
var chartsOptions;





$(document).ready(function(){
setupEventHandelar("_product",loadSalesReportByDateForProduct);
	
});



 function loadSalesReportByDateForProduct(period){
	
	if(period == "custom"){
		
		startDate = $("#startDate_product").val();
		endDate = $("#endDate_product").val();
		requestUrl= contextPath+"reports/product/" + startDate + "/" + endDate;
		console.log(contextPath);
	}else{
			requestUrl= contextPath+"reports/product/" + period;

	}
	
	$.get(requestUrl,function(responseJSON){
		perpareChartDataForSalesByProduct(responseJSON);
		customizeChartForSalesByProduct();
		formatChartData(data,2,3);
		drawChartForSalesByProduct();
		setSalesAmount(period,'_product',"Total Products");
		
		
	});
	}
	
 function perpareChartDataForSalesByProduct(responseJSON){
			data = new google.visualization.DataTable();
			data.addColumn('string','Product');
			data.addColumn('number','Quantity');
			
			data.addColumn('number','Gross Sales');
			data.addColumn('number','Net Sales');
			
			
			 totalGrossSales=0.0;
             totalNatSales=0.0;
              totalItems=0;
              
			
			$.each(responseJSON,function(index,reportItem){
				data.addRows([[reportItem.identifier,reportItem.productsCount,reportItem.grossSales,reportItem.netSales]]);
				totalGrossSales += parseFloat(reportItem.grossSales);
				totalNatSales += parseFloat(reportItem.netSales);
				 totalItems += parseInt(reportItem.productsCount);
			});
	}
	
function customizeChartForSalesByProduct(){
	chartsOptions= {
		heigth: 360, width: '80%',
		showRowNumber: true,
		page: 'enable',
		sortColumn: 2,
		sortAscending: false
	};
	
	
}

function drawChartForSalesByProduct(){
	var salesChart = new google.visualization.Table(document.getElementById('chart_sales_by_product'));
	
		salesChart.draw(data,chartsOptions);
		
		
	
		
		
}



