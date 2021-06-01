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
	
	
		$(document).ready(function(){
		
		$("#buttonCansel").on("click",function(){
			
			window.location = "[[@{/users}]]";
		});
		
		$("#photoFile").change(function() {
		if(!showFileSize(this)){
		return ;
		}
			
				showImageThumbanil(this);
			
			
		});
		
	});
	function showImageThumbanil(fileInput){
		var file = fileInput.files[0];
		var reder = new FileReader();
		reder .onload = function(e){
			$("#thumbanil").attr("src",e.target.result);
		};
		reder.readAsDataURL(file)
		
		
	}
	function showFileSize(fileInput){
	
	
	fileSize = fileInput.files[0].size;
			alert("File Size " +fileSize);
			if(fileSize > 5048576){
				fileInput.setCustomValidity("Thse File Must 1MB");
				fileInput.reportValidity();
				return false;
			}else{
				fileInput.setCustomValidity("");
				return true;
			}
			
	
	}