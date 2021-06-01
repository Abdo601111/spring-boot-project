package com.test.craditem;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.CradItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.shopmeCradItem.CartItemRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CradItemTest {

	
	@Autowired private CartItemRepository repo;
	@Autowired private TestEntityManager entityManger;
	
	@Test
	public void save() {
		Integer productId= 2;
		Integer customerId=1;
		Customer costomer = entityManger.find(Customer.class, customerId);
		Product product = entityManger.find(Product.class, productId);
		CradItem  c= new CradItem();
		c.setCustomer(costomer);
		c.setProduct(product);
		c.setQuantity(1);
		CradItem save = repo.save(c);
		
		assertThat(save.getId()).isGreaterThan(0);
	}
	
	
}
