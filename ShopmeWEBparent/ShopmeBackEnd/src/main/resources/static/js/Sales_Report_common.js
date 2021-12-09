
var MILLESECOUND_A_DAYS=24* 60 *60 *1000;

function  setupEventHandelar(reportType,callbackFunction){

	$(".button_salse_by"+reportType).on("click",function(){
		
		
		$(".button_salse_by"+reportType).each(function(e){
			$(this).removeClass('btn-primary').addClass('btn-light');
		});
		
		$(this).removeClass('btn-light').addClass('btn-primary');
		
		period = $(this).attr("period");
		if(period){
		  callbackFunction(period);
            $("#customDateRang" + reportType).addClass("d-none");

		}else{
			 $("#customDateRang" + reportType).removeClass("d-none");
		}
		
	});
	initCustomDateReng(reportType);
	
	$("#buttonViewReportByRang"+reportType).on("click",function(){
		validateDateRang(reportType,callbackFunction);
	});
}



   function validateDateRang(reportType,callbackFunction){
   startDateField = document.getElementById('startDate'+reportType);
	days= calculateDays(reportType);
	 startDateField.setCustomValidity("");
	if(days >= 7 && days <=30){
		callbackFunction("custom");
	}else{
   startDateField.setCustomValidity("Dates Must Be In The Rang 7....30 Days");	
   startDateField.reportValidity();
}
}

  function  calculateDays(reportType){
  startDateField = document.getElementById('startDate'+reportType);
  endDateField = document.getElementById('endDate'+reportType);
	startDate = startDateField.valueAsDate;
	endDate  = endDateField.valueAsDate ;
	
	defferencInMilliSecound=startDate - endDate;
	
	return defferencInMilliSecound/MILLESECOUND_A_DAYS;
	
}

  function initCustomDateReng(reportType){
    startDateField = document.getElementById('startDate'+reportType);
    endDateField = document.getElementById('endDate'+reportType);
	toDate = new Date();
	endDateField.valueAsDate = toDate;
	fromDate = new Date();
	fromDate.setDate(toDate.getDate()-30);
	startDateField.valueAsDate=fromDate;
	
	
}


function getChartTitle(period){
	
	if(period == "last_7_Days") return "Last On 7 Days";
	if(period == "last_28_Days") return "Last On 28 Days";
    if(period == "last_6_months") return "Last On 6 months";
    if(period == "last_year") return "Sales In Last Year";
    if(period == "custom") return "Custom Date Range";


	return "";
}

 function  getdenominator(period,reportType){
	
	if(period== "last_7_Days") return 7;
	if(period== "last_28_Days") return 28;
	if(period== "last_6_months") return 6;
	if(period== "last_year") return 12;
	 if(period == "custom") return calculateDays(reportType);
	return 7;
}


function setSalesAmount(period,reportType,LabelTotalItems){

        $("#textTotalGrossSales"+reportType).text(totalGrossSales);
		$("#textTotalNetSales"+reportType).text(totalNatSales);
		
		denominator = getdenominator(period,reportType);
		
		$("#textAvgGrossSales"+reportType).text(totalGrossSales/denominator);
		$("#textAvgNetSales"+reportType).text(totalNatSales/denominator);
		$("#lableTotalItem"+reportType).text(LabelTotalItems);
		$("#textTotalOrders"+reportType).text(totalItems);

}








function formatChartData(data,columnIndex1,columnIndex2){

var formater = new google.visualization.NumberFormat({
		
		prefix: $
	}); 
	formater.format(data,columnIndex1);
	formater.format(data,columnIndex2);
	
		
}