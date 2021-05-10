package com.shopme.admin.country;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Country;

@RestController
public class CountryRestContreoller {
	
	@Autowired
	private CountryRepository repo;
	
	@GetMapping("/country/listAll")
	public List<Country> listAll(){
		return repo.findAllByOrderByNameAsc();
	}
	
	@PostMapping("/country/save")
	public String save(@RequestBody Country country) {
		Country countrySave = repo.save(country);
		return String.valueOf(countrySave.getId());
		
	}
	
	@GetMapping("/country/delete/{id}")
	public void delete(@PathVariable("id") int id) {
		repo.deleteById(id);
	}

}
