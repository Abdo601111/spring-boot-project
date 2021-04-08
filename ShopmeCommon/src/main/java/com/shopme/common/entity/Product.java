package com.shopme.common.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false,unique = true,length = 256)
	private String name;
	@Column(nullable = false,unique = true,length = 256)
	private String alias;
	@Column(length =512,nullable = false)
	private String shortDescriptiobn;
	@Column(length = 4096,nullable = false)
	private String fullDescription;
	@Column(name ="main_image",nullable = false)
	private String mainImage;
	
	@OneToMany(mappedBy = "product" ,cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<ProductImages> images = new HashSet<>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true)
	private List<ProductDetail> details = new ArrayList<>();


	
	private Date createdTime;
	private Date updatedTime;
	private boolean enabled;
	private boolean inStock;
	
	private float cost;
	
	private float price;
	private float discountPercent;
	
	private float length;
	private float width;
	private float height;
	private float weight;
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	@ManyToOne
	@JoinColumn(name="brand_id")
	private Brand brand;
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getShortDescriptiobn() {
		return shortDescriptiobn;
	}
	public void setShortDescriptiobn(String shortDescriptiobn) {
		this.shortDescriptiobn = shortDescriptiobn;
	}
	public String getFullDescription() {
		return fullDescription;
	}
	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isInStock() {
		return inStock;
	}
	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getDiscountPercent() {
		return discountPercent;
	}
	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}
	public float getLength() {
		return length;
	}
	public void setLength(float length) {
		this.length = length;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public String getMainImage() {
		return mainImage;
	}
	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}
	public Set<ProductImages> getImages() {
		return images;
	}
	public void setImages(Set<ProductImages> images) {
		this.images = images;
	}
	
	public void addExtenImages(String imageName) {
		this.images.add(new ProductImages(imageName,this));
	}
	@Transient
	public String getMainImagePath() {
		if(id == null || mainImage==null)return "/images/image-thumbnail.png";
		return"/product-images/" + this.id + "/" + this.mainImage;
	}
	
	
	public List<ProductDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductDetail> details) {
		this.details = details;
	}
	
	public void addDetail(String name, String value) {
		this.details.add(new ProductDetail(name, value, this));
	}
	
	public void addDetail(Integer id,String name, String value) {
		this.details.add(new ProductDetail(id,name, value, this));
	}
	public boolean containImageName(String fileName) {
		Iterator<ProductImages> iterator=images.iterator();
		while(iterator.hasNext()) {
			ProductImages image = iterator.next();
			if(image.getName().equals(fileName) ) {
				return true;
			}
		}
		return false;
	}

	
}
