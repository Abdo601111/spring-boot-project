package com.shopme.admin.setting.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.CurrencyRepository;
import com.shopme.common.entity.Currency;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTest {

	@Autowired
	private CurrencyRepository repo;
	
	
	@Test
	public void testCrateRepositorysup() {
//		List<Currency> listCurrency= Arrays.asList(
//				new Currency("United States Doller","$", "USA"),
//				new Currency("Eyept pound","Pound" ,"EYP"),
//				new Currency("Indea Rupee","Rupee", "INR"),
//				new Currency("Vetnam dinf","v", "VND"),
//				new Currency("Canadian Doller","$", "CAD"),
//				new Currency("Chines Yane","$", "CNU")
//			
//			);
//		
//		repo.saveAll(listCurrency);
//		Iterable<Currency> iterable = repo.findAll();
//		assertThat(iterable).size().isEqualTo(6);
	}
	
}
