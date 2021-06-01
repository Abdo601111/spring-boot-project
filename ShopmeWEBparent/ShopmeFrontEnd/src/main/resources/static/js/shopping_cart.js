$(document).ready(function(){
	
	$(".linkMinus").on("click",function(event){
		event.preventDefault();
		productId = $(this).attr("pid");
		quantityInput = $("#quantity"+productId);
		newQuantity = parseInt(quantityInput.val()) - 1;
		if(newQuantity >0){
			quantityInput.val(newQuantity);
			updateTocard(productId,newQuantity);
		}else{
		showModelDailog("Minimum Quantity is 1");
		}
		
	});
	$(".linlPlus").on("click",function(event){
		event.preventDefault();
		productId = $(this).attr("pid");
		quantityInput = $("#quantity"+productId);
		newQuantity = parseInt(quantityInput.val()) + 1;
		
		if(newQuantity <= 5){
			quantityInput.val(newQuantity);
			updateTocard(productId,newQuantity);
		}else{
		showModelDailog("Maximum Quantity is 5");
		}
	});
	
	
	$(".linkRemove").on("click",function(event){
		event.preventDefault();
		removeProduct($(this));
	
	
});	
});





function updateTocard(productId,quantity){
	
	url = contextPath + "card/update/" + productId+ "/" + quantity;

	$.ajax({
		type: "POST",
		url: url ,
		beforeSend: function(xhr){
		
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
		
	}).done(function(updateSupTotal){
		updatedSupTotal(updateSupTotal, productId);
		 updatedTotal();
	}).fail(function(){
		alert("Error While Updated Product in cart");
		
	});
}




function updatedSupTotal(updateSupTotal,productId){
  $("#supTotal" + productId).text(updateSupTotal);
}




function updatedTotal(){
total=0.0;
productCount =0;
$(".supTotal").each(function(index,element){
total += parseFloat(element.innerHTML);
productCount ++;
});
if(productCount < 1){

showEmptyShoppingCart();
}else{
$("#total").text(total);
}

}

function showEmptyShoppingCart(){

$("#sectionTotal").hide();
$("#sectionEmptyCartMessage").removeClass("d-none");
}





function removeProduct(link){
   url = link.attr("href");
	

$.ajax({
		type: "DELETE",
		url: url ,
		beforeSend: function(xhr){
		
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
		
	}).done(function(response){
	    rowNumber = link.attr("rowNumber");
		removeProductHTML(rowNumber);
		updatedTotal();
		updateCountNumbers();
	    alert(response);
		
	}).fail(function(){
		alert("Error While Removed Product ");
		
	});


}

function removeProductHTML(rowNumber){

$("#row" + rowNumber).remove();

}

function updateCountNumbers(){
 
 $(".divCount").each(function(index,element){
 
 element.innerHTML= "" +(index + 1);
 
 });

}



