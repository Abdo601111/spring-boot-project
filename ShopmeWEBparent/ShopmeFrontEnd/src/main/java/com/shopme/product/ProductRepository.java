package com.shopme.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true  AND (p.category.id=?1 OR p.category.allParentIDs LIKE %?2%)")
	public Page<Product> listByCategoryId(Integer categoryId,String categoryIdMatch,Pageable pageble);

	
	public Product findByAlias(String alias);
}