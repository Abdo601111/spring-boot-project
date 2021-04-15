package com.shopme.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundExeption;
@Service
public class ProductService {
	
public static final int PRODUCTS_PER_PAGE = 10;
	
	@Autowired private ProductRepository repo;
	
	public Page<Product> listByCategory(int pageNum, Integer categoryId) {
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		
		return repo.listByCategoryId(categoryId, categoryIdMatch, pageable);
		
	}

	
	public Product getByAlias(String  alias) throws ProductNotFoundExeption {
		
		Product productalias = repo.findByAlias(alias);
		if(productalias == null) {
			throw new ProductNotFoundExeption("could Not find Product Alias "+alias);
		}
		return productalias;
		
	}

}
