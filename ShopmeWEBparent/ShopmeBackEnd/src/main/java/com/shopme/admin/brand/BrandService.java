package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.common.entity.Brand;

@Service
public class BrandService {
	@Autowired
	private BrandRepository repo;
	
	
	public static final int  BRANDS_PER_PAGE=4;
	
	
	public List<Brand>listAll(){
		
		return (List<Brand>) repo.findAll();
		
	}
	
	public Brand save(Brand brand) {
		return repo.save(brand);
	}
	
	public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
				
		Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);
		
		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		
		return repo.findAll(pageable);		
	}


	
	public Brand get(Integer id) throws BrandNotFoundException  {
		// TODO Auto-generated method stub
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			throw new BrandNotFoundException("Not Found Excption"+id);
		}
	}
	
	public void delete(Integer id) throws UserNotFoundException {
		Long countById= repo.countById(id);
		
		if(countById == null || countById == 0) {
			
			throw new UserNotFoundException("Not Found Excption"+id);
			
		}
		repo.deleteById(id);
		
	}
	
//	public boolean isNameUnique(Integer id,String name) {
//		Brand brand =repo.getBrandByName(name);
//		
//		if(brand == null) return true;
//		
//		boolean isCreatingNew= (id == null);
//		
//		if(isCreatingNew) {
//			
//			if(brand != null)return false;
//		}else {
//			if(brand.getId() != id) {
//				
//				return false;
//			}
//		}
//			
//			
//		
//		
//		return true;
//	}
//	
	
	public String isNameUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Brand brandByName = repo.findByName(name);
		
		if (isCreatingNew) {
			if (brandByName != null) return "Duplicate";
		} else {
			if (brandByName != null && brandByName.getId() != id) {
				return "Duplicate";
			}
		}
		
		return "OK";
	}
}

	

