/**
 * 
 */

var returnOrder;
var modalTitle;
var filedNote;
var orderId;
var divReason;
var divMessag;
var firstButton;
var secondButton;

$(document).ready(function(){
	returnOrder =$("#returnOrderModal");
	modalTitle = $("#returnOrderModalTitle");
	filedNote= $("#returnNote");
	divReason=$("#divReason");
	divMessag=$("#divMessag");
	firstButton=$("#firstButton");
	secondButton=$("#secondButton");
	
	handelReturnOrder();
	
});

function showReturnModalDialog(link){
	divMessag.hide();
	divReason.show();
	firstButton.show();
	secondButton.text("Cancel");
	filedNote.val("");
	
	orderId= link.attr("orderId");
	returnOrder.modal("show");
	modalTitle.text("Return Order Id #"+orderId);
	
	
}

function showMessageDialog(message){
	
	
	divReason.hide();
	firstButton.hide();
	secondButton.text("Close");
	divMessag.val(message);
	
	divMessag.show();
	
}


function handelReturnOrder(){
	$(".linkReturnOrder").on("click",function(e){
		e.preventDefault();
           showReturnModalDialog($(this))		
	});
	
	
}


function submitReturnedOrderForm(){
	
	reason= $("input[name='returnReason'] : checked").val();
	note = filedNote.val();
	sendReturnOrderRequedt(reason,note);
	return false;
}

function sendReturnOrderRequedt(reason,note){
	
	requestUrl=contextPath +"order/return";
	requestBody ={orderId: orderId,reason: reason,note: note};
	
	
	$.ajax({
		type: "POST",
		url: requestUrl ,
		beforeSend: function(xhr){
		
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(requestBody),
		contentType: 'application/json'
		
	}).done(function(returnResponse){
		showMessageDialog("Return Request has Send");
		updateStatusTextAndhideButton(returnResponse.orderId);
	}).fail(function(err){
		showMessageDialog(err.responseText);
		
	});
	
	
	
}

function updateStatusTextAndhideButton(orderId){
	
	$(".textOrderStatus"+orderId).each(function(index){
		
		$(this).text("RETURN_REQUEST");
	})
	
	$(".linkReturn"+orderId).each(function(index){
		
		$(this).hide();
	})
	}


