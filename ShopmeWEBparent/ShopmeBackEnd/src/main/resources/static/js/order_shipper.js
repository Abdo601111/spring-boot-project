var iconNams={

   'PICKED':'fa-people-carry',
   'SHIPPING':'fa-shipping-fast',
   'DELIVERED':'fa-box-open',
   'RETURNED':'fa-undo fa-2x'

}
var confirmText;
var confirmModalDialog;
var yesButton;
var noButton;

	$(document).ready(function(){
	confirmText=$("#confirmText");
	 confirmModalDialog=$("#confirmModal");
	 yesButton =$("#yesButton");
	 noButton =$("#noButton");
	
	$(".linkUpdateStatus").on("click", function(e){
		
		e.preventDefault();
		link = $(this);
	showUpdateConfirmModel(link);
		
		});
		addEventForYesButton();
	});
	
function addEventForYesButton(){

    yesButton.click(function(e){
     e.preventDefault();
     sendRequestToUpdateOrderStatus($(this));
     
   });
}	
	
	
	function sendRequestToUpdateOrderStatus(button){
	requestUrl=button.attr("href");
	$.ajax({
		type: 'POST',
		url: requestUrl,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
			
	}).done(function(shippingCost) {
		showMessageModal("Order Updated Successfully");
		updateIcon(response.orderId,response.status);
	}).fail(function(err) {
	showMessageModal("Error Updated Order Status");
		
	})	
	
	
	}
	
	
	function updateIcon(orderId,status){
	
	  link=$("#link" + status+orderId);
	  link.replaceWith("<i class='fas  "+iconNames[status]+" fa-2x icon-green'></i>");
	
	}
	
function showUpdateConfirmModel(link){
noButton.text("No");
	yesButton.show();
    orderId= link.attr("orderId");
    status= link.attr("status");
    yesButton.attr("href",link.attr("href"));
    confirmText.text("Are You Sure You Wont to Update Status Of Order Id" +orderId
    + "To" + status + "?");
    
    confirmModalDialog.modal();
	
	}
	
	function showMessageModal(message){
	noButton.text("close");
	yesButton.hide();
	confirmText.text(message);
	
	}