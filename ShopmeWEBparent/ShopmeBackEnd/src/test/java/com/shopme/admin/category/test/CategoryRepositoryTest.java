package com.shopme.admin.category.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
	
	@Autowired
	private CategoryRepository repo;
	
//	@Test
//	public void testCrateRepository() {
//		Category cat = new Category("Electronecs");
//		Category c = repo.save(cat);
//		assertThat(c.getId()).isGreaterThan(0);
//		
//		
//	}
	@Test
	public void testCrateRepositorysup() {
		Category cat = new Category(1);
		Category ca = new Category("Desktop",cat);
		Category c = repo.save(ca);
		assertThat(c.getId()).isGreaterThan(0);
		
		
	}
	
	

}
