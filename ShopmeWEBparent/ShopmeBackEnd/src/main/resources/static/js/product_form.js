/**
 * 
 */

		
	 
		brandDropDown = $("#brand");
		categoryDropDown = $("#category");
		var imageCount =0;
		
		$(document).ready(function(){
			
			$("#shortDescriptiobn").richText();
			$("#fullDescription").richText();
			
			$("a[name='removLinkImage']").each(function(index){
			$(this).click(function(){
			removeExtraImage(index);
			});
			
			});
			
			brandDropDown.change(function(){
				categoryDropDown.empty();
				getCategoryByBrand();	
			});
			getCategoryByBrand();
			
			$("input[name='exteraImage']").each(function(index){
			   imageCount++;
			  $(this).change(function(){
			 
			    extraShowImageThumbanil(this,index);
			  });
			
			});
			
			

		});
		function extraShowImageThumbanil(fileInput,index){
		
		var file = fileInput.files[0];
			var reder = new FileReader();
			reder .onload = function(e){
				$("#extraThumbanil"+index).attr("src",e.target.result);
			};
			reder.readAsDataURL(file)
			
			if(index >= imageCount-1){
			addExternImagesSection(index+1);
			}
			
			
		}
		
		
	  function addExternImagesSection(index){
		
	    formHtml = `
	    
	     <div class="col border m-3 p-2" id="exterImageRemove${index}">
					<div id="extraImageHeader${index}">
						<label>Extra  Image${index+1}</label>
					</div>
					<div>
						<img id="extraThumbanil${index}" alt="Extra Image${index+1} Preview " class="img-fluid"
							src="${defultImage}" />
					</div>
					<div>
						<input type="file"  name="exteraImage"
							accept="image/png, image/jpeg"  onchange="extraShowImageThumbanil(this,${index})">
					</div>

				</div>
	    `;
		
		linkRemoveHeder = `
		<a class="btn fas fa-times-circle fa-2x icon-dark float-right" title="Remove This Image" 
		href="javascript:removeExtraImage(${index-1})"></a>
		`;
		
		$("#divProductImages").append(formHtml);
		$("#extraImageHeader"+(index-1)).append(linkRemoveHeder);
		 imageCount++;
		
		}
	function removeExtraImage(index){
	$("#exterImageRemove"+index).remove();
	

	}
	
	function getCategoryByBrand(){
		brandId = brandDropDown.val();
		url = brandModalURL + "/" + brandId + "/categories";
		$.get(url,function(responseJson){
			$.each(responseJson,function(index,category){
				$("<option>").val(category.id).text(category.name).appendTo(categoryDropDown);
			});
			
			
		});
		
		
	}
	
	function checkUnique(form) {
		brandId = $("#id").val();
		brandName = $("#name").val();
		
		csrfValue = $("input[name='_csrf']").val();
		
		
		
		params = {id: brandId, name: brandName, _csrf: csrfValue};
		
		$.post(urlProduct, params, function(response) {
			if (response == "OK") {
				form.submit();
			} else if (response == "Duplicate") {
				shoeWorningModal("There is another brand having same name " + brandName);	
			} else {
				shoeErrorModal("Unknown response from server");
			}
			
		}).fail(function() {
			shoeErrorModal("Could not connect to the server");
		});
		
		return false;
	}