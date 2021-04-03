package com.shopme.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;



@RestController
public class BrandRestController {
	
	@Autowired
	public BrandService service;
	
	@PostMapping("/brand/check_name")
	public String checkDuplicateEmail(@Param("id") Integer id,@Param("name") String name) {
		return service.isNameUnique(id, name) ;
		
		
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> getCategoriesByBrand(@PathVariable("id") Integer id) throws BrandNotFoundRestException{
		List<CategoryDTO> listCategorys= new ArrayList<>();
		try {
		
		Brand brand=service.get(id);
		Set<Category> categorys= brand.getCategories();
		for(Category category: categorys) {
			CategoryDTO cat = new CategoryDTO(category.getId(),category.getName());
			 listCategorys.add(cat);
		}
		return listCategorys;
		}catch(BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
		
	}

	

//	@PostMapping("/brands/check_unique")
//	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
//		return service.checkUnique(id, name);
//	}
}

	

