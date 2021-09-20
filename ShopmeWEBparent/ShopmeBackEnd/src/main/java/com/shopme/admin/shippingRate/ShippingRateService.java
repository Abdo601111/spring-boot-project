package com.shopme.admin.shippingRate;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.country.CountryRepository;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ShippingRate;

@Service
@Transactional
public class ShippingRateService {
	public static final int RATES_PER_PAGE = 10;
	public static final int DIM_DEVIVOR = 139;
	
	@Autowired private ShippingRateRepository shipRepo;
	@Autowired private CountryRepository countryRepo;
	@Autowired private ProductRepository repo;

	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, RATES_PER_PAGE, shipRepo);
	}	
	
	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}		
	
	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		ShippingRate rateInDB = shipRepo.findByCountryAndState(
				rateInForm.getCountry().getId(), rateInForm.getState());
		boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null && !rateInDB.equals(rateInForm);
		
		if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExistsException("There's already a rate for the destination "
						+ rateInForm.getCountry().getName() + ", " + rateInForm.getState()); 					
		}
		shipRepo.save(rateInForm);
	}

	public ShippingRate get(Integer id) throws ShippingRateNotFoundException  {
		try {
			return shipRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
	}
	
	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
		
		shipRepo.updateCODSupport(id, codSupported);
	}
	
	public void delete(Integer id) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
			
		}
		shipRepo.deleteById(id);
	}	
	
	
	public float  calculateShippingRate(Integer productId,Integer countryId,String state) throws ShippingRateNotFoundException {
		ShippingRate shippungRate= shipRepo.findByCountryAndState(countryId, state);
		if(shippungRate == null) {
			
			throw new ShippingRateNotFoundException("No Shipping Rate Found	For The Given Destination"
					+ "You Have Enter Shippig Cost Manually " );

		}
		Product product=repo.findById(productId).get();
		float dmiWieght= (product.getLength()* product.getWidth()*product.getHeight())/DIM_DEVIVOR;
		float finalWight = product.getWeight() > dmiWieght ?product.getWeight() : dmiWieght;
		return finalWight *shippungRate.getRate() ;
	}
	
}
