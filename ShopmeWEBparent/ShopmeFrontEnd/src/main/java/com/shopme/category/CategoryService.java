package com.shopme.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repo;
	
	public List<Category> listNoChilderCategory(){
		List<Category> listNoChildern = new ArrayList<>();
		List<Category> listCategory = repo.findByEnabled();
		listCategory.forEach(category ->{
			Set<Category> childern = category.getChildren();
			if(childern ==null || childern.size()==0) {
				listNoChildern.add(category);
			}
		});
		
		return listNoChildern;
		
	}
	
	
	
	
	
	public Category getCategpriesByAlias(String alias) {
		return repo.findByEnabledAlias(alias);
	}
	
	
	public List<Category> getCategoryParents(Category child) {
		List<Category> listParents = new ArrayList<>();
		
		Category parent = child.getParent();
		
		while (parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent();
		}
		
		listParents.add(child);
		
		return listParents;
	}

	
	
	

}
