/**
 * 
 */
 
 $(document).ready(function(){
 $("a[name='removeproductDetails']").each(function(index){
			$(this).click(function(){
			removeDetailsSectionByIndex(index);
			});
			
			});
 
 });

	function addNewDetailsSection(){
	
	allDivaDetails = $("[id^='divDetails']");
	divDetailsCount = allDivaDetails.length;
	
	html = `
	
	<div class="form-inline" id="divDetails${divDetailsCount}">
		<label class="m-3">Name :</label>
		<input  type="text" class="form-control w-25" name="detailsName" >
		
		<label class="m-3">Value :</label>
		<input  type="text" class="form-control w-25" name="detailsValue" >
		
		
		</div>
	
	`;
	$("#divProductDetails").append(html);
	
		previosDivDetails= allDivaDetails.last();
		previosDivId=previosDivDetails.attr("id");
	
	
	linkRemoveHeder = `
		<a class="btn fas fa-times-circle fa-2x icon-dark " title="Remove This Details" 
		href="javascript:removeDetailsSection('${previosDivId}')"></a>
		`;
		
	
	
	previosDivDetails.append(linkRemoveHeder);
	
	}	
	
	function removeDetailsSection(id){
	
	$("#"+id).remove();
	
	}
	
	function removeDetailsSectionByIndex(index){
	$("#divDetails"+index).remove();
	}
	
	
	 
		
	