package com.shopme.admin.product;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	@Autowired
	private ProductService service;
	@Autowired
	private BrandService brand;
	
	
	@GetMapping("/products")
	public String listAll(Model model) {
		model.addAttribute("listProducts", service.listAll());
		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		model.addAttribute("product", product);
		model.addAttribute("listProducts", service.listAll());
		model.addAttribute("listBrand", brand.listAll());
		model.addAttribute("pageTitle", "Create New Product");
		return "products/product_form";
	}
	@PostMapping("/products/save")
	public String save (Product product,RedirectAttributes r,@RequestParam("photoFile")MultipartFile mainImageMultipart
			,@RequestParam("exteraImage")MultipartFile[] exteraMultiPartFile
			,@RequestParam(name="detailsName",required = false) String[] detailsName
			,@RequestParam(name="detailsValue",required = false) String[] detailsValue) throws IOException {
		
		setMainImageName(mainImageMultipart,product);
		setExteraImages(exteraMultiPartFile,product);
		setProductDetails(detailsName,detailsValue,product);
		
			Product productSave = service.save(product);
		 saveUploudedImages(mainImageMultipart,exteraMultiPartFile,productSave);
			
			
		 r.addFlashAttribute("message", "The Product Save Successfully");
		return "redirect:/products";
		
	}
	

	private void setProductDetails(String[] detailsName, String[] detailsValue, Product product) {
            if(detailsName ==null || detailsName.length ==0) return;	
            for(int count =0 ; count < detailsName.length ;count ++) {
            	String name = detailsName[count];
            	String value = detailsValue[count];
            	
            	if(!name.isEmpty() && !value.isEmpty()) {
            		product.addDetail(name, value);
            	}
            	
            }
            
	}
	

	private void saveUploudedImages(MultipartFile mainImageMultipart, MultipartFile[] exteraMultiPartFile,
			Product productSave) throws IOException {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
		String uploadDir = "../product-images/"+productSave.getId(); 
		FileUploadUtil.cleanDir(uploadDir);
		FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);	
		}
		if(exteraMultiPartFile.length >0) {
			String uploadDir = "../product-images/" + productSave.getId() + "/extera"; 

     	   for(MultipartFile multiPart : exteraMultiPartFile) {
     		   if(multiPart.isEmpty()) continue;
   			String fileName = StringUtils.cleanPath(multiPart.getOriginalFilename());
   			FileUploadUtil.saveFile(uploadDir, fileName, multiPart);	

     	   }
		}
		
		
	}

	private void setExteraImages(MultipartFile[] exteraMultiPartFile, Product product) {
               if(exteraMultiPartFile.length > 0) {
            	   for(MultipartFile multiPart : exteraMultiPartFile) {
            		   if(!multiPart.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multiPart.getOriginalFilename());
                          product.addExtenImages(fileName);
            		   }
            	   }
            	   
               }		
	}

	private void setMainImageName(MultipartFile mainImageMultipart,Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateStatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,RedirectAttributes r) {
		
        service.updateEnabledStatus(id, enabled);
		
		String status = enabled ? "Enabled" : "Desabled";
		String message= "The Product  Id :" +id +"has Been" +status;
		r.addFlashAttribute("message", message);
		
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String delete(@PathVariable("id") Integer id,RedirectAttributes r,Model model) {
		
		try {
			service.delete(id);
			String uploadDirExteraImages = "../product-images/"+id+"/extera"; 
			String uploadDirMainImage = "../product-images/"+id; 
			FileUploadUtil.removeDir(uploadDirExteraImages);
			FileUploadUtil.removeDir(uploadDirMainImage);

			r.addFlashAttribute("message", "The Product Is Deleting "+id);
		} catch (Exception e) {
			r.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/products";
		
	}
	@GetMapping("/products/edit/{id}")
	public String update(@PathVariable("id") Integer id,RedirectAttributes r,Model model) {
		
		try {
			
			Product product = service.get(id);
			int numberImage= product.getImages().size();
			
			
			model.addAttribute("listProducts", service.listAll());
			model.addAttribute("product", product);
			model.addAttribute("numberImage", numberImage);
			model.addAttribute("listBrand", brand.listAll());
			model.addAttribute("pageTitle", "Edit Product"+id);
			return "products/product_form";
		} catch (Exception e) {
			r.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/products";
		
	}
	
	

}
