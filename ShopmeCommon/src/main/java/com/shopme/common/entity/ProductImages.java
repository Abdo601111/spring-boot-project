package com.shopme.common.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class ProductImages {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	@ManyToOne
	private Product product;
	
	
	public ProductImages() {
		super();
	}
	public ProductImages(Integer id,String name, Product product) {
		super();
		this.name = name;
		this.product = product;
		this.id=id;
	}
	public ProductImages(String name, Product product) {
		super();
		this.name = name;
		this.product = product;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	@Transient
	public String getImagePath() {
		
		return "/product-images/" + product.getId()+"/extera/" +this.name;
	}
	

}
