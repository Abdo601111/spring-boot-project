/**
 * 
 */
$(document).ready(function(){
	$("#buttonAdd2Card").on("click",function(eve){
		addTocard();
		
	});
	
});
function addTocard(){
	quantity = $("#quantity"+productId).val();
	url = contextPath + "card/add/" + productId+ "/" + quantity;

	$.ajax({
		type: "POST",
		url: url ,
		beforeSend: function(xhr){
		
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
		
	}).done(function(response){
		alert("Shopping Cart",response);
	}).fail(function(){
		alert("Error While Adding Product in cart");
		
	});
}