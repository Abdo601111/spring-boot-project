package com.shopme.admin.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Order;

@Controller
public class OrderController {

	@Autowired
	private OrderService service;
	
	@GetMapping("/orders")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "orderTime", "asc", null);
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword
			) {
		Page<Order> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Order> listOrders = page.getContent();
		
		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);		
		model.addAttribute("listOrders", listOrders);
		
		return "order/orders";		
	}
	
	@GetMapping("/orders/detail/{id}")
	public String get(@PathVariable("id") int id,RedirectAttributes ra,Model model) {
		try {
			Order order = service.get(id);
			model.addAttribute("order", order);
			return "order/order_details";
		} catch (Exception e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/orders";
				
	}
	
	@GetMapping("/orders/delete/{id}")
	public String delete(@PathVariable("id") int id,RedirectAttributes ra) {
		try {
			service.delete(id);
			ra.addFlashAttribute("message", "Order has Been Deleting");
			
		} catch (Exception e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/orders";
				
	}
	
	

}
