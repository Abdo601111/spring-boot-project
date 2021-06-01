/**
 * 
 */

function showModelDailog(title,message){
		
		$("#modalTitle").text(title);
		$("#modalBody").text(message);
		$("#modalDailog").modal();
	}
	
	function shoeErrorModal(message){
		 showModelDailog("Error",message)
	}
	function shoeWorningModal(message){
		 showModelDailog("Worning",message)
	}