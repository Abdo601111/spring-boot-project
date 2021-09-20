package com.shopme.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Product;

@Repository
public interface ProductRepository  extends PagingAndSortingRepository<Product, Integer>{

	
	public Product findByName(String name);

	public Long countById(Integer id);

	@Query("UPDATE Product u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	  @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%"
	  +"OR p.shortDescriptiobn LIKE %?1%"
	  +"OR p.fullDescription LIKE %?1%"
	  +"OR p.brand.name LIKE %?1%"
	  +"OR p.category.name LIKE %?1%")
     public Page<Product> findAll(String keyWord ,Pageable pagable);
	  
	  @Query("SELECT p FROM Product p WHERE p.category.id=?1 OR p.category.allParentIDs  LIKE %?2%")
	 public Page<Product> findAllInCategory(Integer categoryId,String categoryInMath ,Pageable pagable);
	  
	  @Query("SELECT p FROM Product p WHERE (p.category.id=?1 OR p.category.allParentIDs  LIKE %?2%) AND "
			  +" (p.name LIKE %?3%"
			  +"OR p.shortDescriptiobn LIKE %?3%"
			  +"OR p.fullDescription LIKE %?3%"
			  +"OR p.brand.name LIKE %?3%"
			  +"OR p.category.name LIKE %?3%)")
	public Page<Product> searchInCategory(Integer categoryId,String categoryInMath ,String ketWord,Pageable pagable);
	
	  
	      @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
		public Page<Product> searchProductsByName(String keyword, Pageable pageable);


	  
}
