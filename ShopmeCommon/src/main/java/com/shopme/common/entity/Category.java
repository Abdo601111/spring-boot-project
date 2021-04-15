package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;


@Entity
public class Category {
	

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 140,nullable = false,unique = true)
	private String name;
	
	@Column(length = 140,nullable = false,unique = true)
	private String alias;
	
	@Column(length = 60,nullable = false)
	private String image;
	
	private boolean enabled;
	
	private String allParentIDs;
	
	@OneToOne
	@JoinColumn(name="parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	private Set<Category> children = new HashSet<>();
	
	
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public Set<Category> getChildren() {
		return children;
	}
	public void setChildren(Set<Category> children) {
		this.children = children;
	}
	
	
     @Transient
	public String getImagePath() {
		
		if (this.id == null) return "/images/image-thumbnail.png";
		return "/categorys-image/" + this.id +"/" + this.image;
	}
     @Transient
     private boolean hasCheldern;


	public boolean isHasCheldern() {
		return hasCheldern;
	}



	public void setHasCheldern(boolean hasCheldern) {
		this.hasCheldern = hasCheldern;
	}

	public static Category copyIdAndName(Integer id, String name) {
		Category copyCategory = new Category();
		copyCategory.setId(id);
		copyCategory.setName(name);
		
		return copyCategory;
	}
	public static Category copyIdAndName(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		
		return copyCategory;
	}



	@Override
	public String toString() {
		return  name ;
	}



	public String getAllParentIDs() {
		return allParentIDs;
	}



	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}

	
	
	
	
	public Category(int id) {
		super();
		this.id = id;
	}


public static Category copy(Category category) {
	Category categiry1 = new Category();
	categiry1.setId(category.getId());
	categiry1.setName(category.getName());
	return categiry1;
}



public static Category copyFull(Category category) {
	Category copyCategory = new Category();
	copyCategory.setId(category.getId());
	copyCategory.setName(category.getName());
	copyCategory.setImage(category.getImage());
	copyCategory.setAlias(category.getAlias());
	copyCategory.setEnabled(category.isEnabled());
	copyCategory.setHasCheldern(category.getChildren().size() > 0);
	
	return copyCategory;		
}




public static Category copyFull(Category category,String name) {
	
	Category copyCategory = Category.copyFull(category);
	copyCategory.setName(name);
	return copyCategory;
}


public static Category copy(int id,String name) {
	Category categiry1 = new Category();
	categiry1.setId(id);
	categiry1.setName(name);
	return categiry1;
}
	public Category(String name,Category ct) {

		this(name);
		this.parent=ct;
	}
	
	public Category(String name) {
		this.name=name;
		this.alias=name;
		this.image="default.png";
		
		
	}
	
	public Category()
	{}
	

}
