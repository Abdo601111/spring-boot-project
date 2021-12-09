package com.shopme.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundExeption;

import java.util.NoSuchElementException;

@Service
public class ProductService {
	
public static final int PRODUCTS_PER_PAGE = 10;
public static final int SEARCH_RESULTS_PER_PAGE = 10;
	
	@Autowired private ProductRepository repo;
	
	public Page<Product> listByCategory(int pageNum, Integer categoryId) {
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		
		return repo.listByCategoryId(categoryId, categoryIdMatch, pageable);
		
	}

	public Iterable<Product> listAll(){
		return repo.findAll();
	}

	
	public Product getByAlias(String  alias) throws ProductNotFoundExeption {
		
		Product productalias = repo.findByAlias(alias);
		if(productalias == null) {
			throw new ProductNotFoundExeption("could Not find Product Alias "+alias);
		}
		return productalias;
		
	}

	public Product getByAlias(Integer  alias) throws ProductNotFoundExeption {

try {
	Product productalias = repo.findById(alias).get();
              return productalias;
  }catch (NoSuchElementException ex){
	throw new ProductNotFoundExeption("could Not find Product Alias "+alias);

}
}


	public Page<Product> search(String keyword, int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
		return repo.search(keyword, pageable);

	}

}
