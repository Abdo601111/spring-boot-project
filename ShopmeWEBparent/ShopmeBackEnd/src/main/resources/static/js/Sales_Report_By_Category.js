
var data;
var chartsOptions;





$(document).ready(function(){
setupEventHandelar("_category",loadSalesReportByDateForCategory);
	
});



 function loadSalesReportByDateForCategory(period){
	
	if(period == "custom"){
		
		startDate = $("#startDate_category").val();
		endDate = $("#endDate_category").val();
		requestUrl= contextPath+"reports/category/" + startDate + "/" + endDate;
		console.log(contextPath);
	}else{
			requestUrl= contextPath+"reports/category/" + period;

	}
	
	$.get(requestUrl,function(responseJSON){
		perpareChartDataForSalesByCategory(responseJSON);
		customizeChartForSalesByCategort();
		formatChartData(data,1,2);
		drawChartForSalesByCategort();
		setSalesAmount(period,'_category',"Total Products");
		
		
	});
	}
	
 function perpareChartDataForSalesByCategory(responseJSON){
			data = new google.visualization.DataTable();
			data.addColumn('string','Category');
			data.addColumn('number','Gross Sales');
			data.addColumn('number','Net Sales');
			
			
			 totalGrossSales=0.0;
             totalNatSales=0.0;
              totalItems=0;
              
			
			$.each(responseJSON,function(index,reportItem){
				data.addRows([[reportItem.identifier,reportItem.grossSales,reportItem.netSales]]);
				totalGrossSales += parseFloat(reportItem.grossSales);
				totalNatSales += parseFloat(reportItem.netSales);
				 totalItems += parseInt(reportItem.productsCount);
			});
	}
	
function customizeChartForSalesByCategort(){
	chartsOptions= {
		heigth: 360, legend: {position: 'rigth'}
	};
	
	
}

function drawChartForSalesByCategort(){
	var salesChart = new google.visualization.PieChart(document.getElementById('chart_sales_by_category'));
	
		salesChart.draw(data,chartsOptions);
		
		
	
		
		
}



