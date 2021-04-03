package com.shopme.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.shopme.common.entity.Product;

@Service
@Transactional
public class ProductService {

	
	@Autowired
	private ProductRepository repo;
	
	public List<Product> listAll(){
		return (List<Product>) repo.findAll();
		
	}
	
	
	public Product save(Product product) {
		if(product.getId() == null) {
			product.setCreatedTime(new Date());
			
		}
		if(product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		}else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}
		product.setUpdatedTime(new Date ());
		return repo.save(product);
		
	}
	
	public String isNameUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = repo.findByName(name);
		
		if (isCreatingNew) {
			if (productByName != null) return "Duplicate";
		} else {
			if (productByName != null && productByName.getId() != id) {
				return "Duplicate";
			}
		}
		
		return "OK";
	}
	
	
	
	

	public Product get(Integer id) throws ProductNotFoundException  {
		// TODO Auto-generated method stub
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			throw new ProductNotFoundException("Not Found Excption"+id);
		}
	}
	
	public void delete(Integer id) throws ProductNotFoundException {
		Long countById= repo.countById(id);
		
		if(countById == null || countById == 0) {
			
			throw new ProductNotFoundException("Not Found Excption"+id);
			
		}
		repo.deleteById(id);
		
	}
	
	public void updateEnabledStatus(Integer id,boolean enabled) {
		
		repo.updateEnabledStatus(id, enabled);
	}

	
}
