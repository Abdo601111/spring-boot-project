package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.util.SystemOutLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImages;

@Controller
public class ProductController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService service;
	@Autowired
	private BrandService brand;
	@Autowired
	private CategoryService categoryService;
	
	
	
	@GetMapping("/products")
	public String listAll(Model model) {
		model.addAttribute("listProducts", service.listAll());
		
		return listByPage(1,model,"name","asc",null,0);
	}
	

	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@Param("categoryId")Integer categoryId) {
		Page<Product> page = service.listByPage(pageNum, sortField, sortDir, keyword,categoryId);
		List<Product> listProducts = page.getContent();
		System.out.println(categoryId);
		
		long startCount = (pageNum - 1) * service.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + service.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		if(categoryId != null) model.addAttribute("categoryId", categoryId);
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);		
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategory", categoryService.listCategoriesUsedInForm());
		
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
			,@RequestParam(name="detailsValue",required = false) String[] detailsValue
			,@RequestParam(name="detailsIDs",required = false) String[] detailsIDs
			,@RequestParam(name="imageIDs",required = false) String[] imageIDs
			,@RequestParam(name="imageName",required = false) String[] imageName) throws IOException {
		
		setMainImageName(mainImageMultipart,product);
		setExteraImages(exteraMultiPartFile,product);
		setProductDetails(detailsIDs,detailsName,detailsValue,product);
		setExteraExistingImageName(imageIDs,imageName,product);
		
			Product productSave = service.save(product);
		 saveUploudedImages(mainImageMultipart,exteraMultiPartFile,productSave);
		 deleteExteraImageWhereRemovedOnForm(product);
			
			
		 r.addFlashAttribute("message", "The Product Save Successfully");
		return "redirect:/products";
		
	}
	

	private void deleteExteraImageWhereRemovedOnForm(Product product) {
		String uploadDir = "../product-images/" + product.getId() + "/extera"; 	
		Path path =Paths.get(uploadDir);
		try {
			Files.list(path).forEach(file ->{
				String fileName = file.toFile().getName();
				if(!product.containImageName(fileName)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted"+fileName);
					} catch (IOException e) {
						LOGGER.error("Could Not Delete Extera Image"+fileName);
					}
				}
			});
		} catch (Exception e) {
			LOGGER.error("Could Not List Directory"+uploadDir);		}
	}

	private void setExteraExistingImageName(String[] imageIDs, String[] imageName, Product product) {
        if(imageIDs==null || imageIDs.length==0) return ;{
        	Set<ProductImages> images = new HashSet<>();
 	  for (int count =0;count< imageIDs.length;count ++) {
		  Integer id = Integer.parseInt(imageIDs[count]);
		  String name = imageName[count];
				  images.add(new ProductImages(id,name,product));
	  }
            	product.setImages(images);
           }		
	}

	private void setProductDetails(String[] detailsIDs,String[] detailsName, String[] detailsValue, Product product) {
            if(detailsName ==null || detailsName.length ==0) return;	
            for(int count =0 ; count < detailsName.length ;count ++) {
            	String name = detailsName[count];
            	String value = detailsValue[count];
            	Integer id = Integer.parseInt(detailsIDs[count]);
            	if(id !=0 ) {
            		product.addDetail(id,name, value);
            	}
            	else if(!name.isEmpty()&& !value.isEmpty()) {
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
                    if(!product.containImageName(fileName))
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
	
	
	@GetMapping("/products/details/{id}")
	public String viewOroductDetails(@PathVariable("id") Integer id,RedirectAttributes r,Model model) {
		
		try {
			
			Product product = service.get(id);
			
			model.addAttribute("product", product);
		return "products/product_form_details";
		} catch (Exception e) {
			r.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/products";
		
	}
	
	


}
