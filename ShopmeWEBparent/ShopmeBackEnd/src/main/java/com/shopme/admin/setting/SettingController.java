package com.shopme.admin.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;

@Controller
public class SettingController {
	
	@Autowired
	private SettingService service;
	@Autowired
	private  CurrencyRepository curruncy;
	@GetMapping("/settings")
	public String listAll(Model model) {
		
		model.addAttribute("listCurrency", curruncy.findAllByOrderByNameAsc());
		for (Setting setting : service.listAll() ) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}

		return "setting/settings";
	}
	
	@PostMapping("/general/save")
	public String save(@RequestParam("photoFile") MultipartFile multipartFile,HttpServletRequest request,RedirectAttributes ra) throws IOException 
	{
		CeneralSettingBag setting = service.getGenerarSetting();
		
		saveSiteLogo(multipartFile, setting);
		saveCurrencySymbol(request, setting);
		updateSettingValuesFromForm(request,setting.list());
		

		
		ra.addFlashAttribute("message","general setting have been Save");
		return "redirect:/settings";
	}
	
	private void saveSiteLogo(MultipartFile multipartFile, CeneralSettingBag settingBag) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			settingBag.updateLogo(value);
			
			String uploadDir = "../site-logo/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
	}
	
	private void saveCurrencySymbol(HttpServletRequest request, CeneralSettingBag settingBag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByIdResult = curruncy.findById(currencyId);
		
		if (findByIdResult.isPresent()) {
			Currency currency = findByIdResult.get();
			settingBag.updateSetting(currency.getSymbol());
		}
	}

	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}
		
		service.saveAll(listSettings);
	}
	
	@PostMapping("/setting/save_mail_server")
	public String getMailServer(HttpServletRequest request,RedirectAttributes ra) {
		List<Setting>ListAllMailServer= service.getByMailServer();
		
		updateSettingValuesFromForm(request,ListAllMailServer);
		ra.addFlashAttribute("message","The Maile server Have Been Save");
		return "redirect:/settings";
		
	}
	
	@PostMapping("/setting/save_mail_temlates")
	public String getMailTemplet(HttpServletRequest request,RedirectAttributes ra) {
		List<Setting>ListAllMailServer= service.getByMailTemlets();
		
		updateSettingValuesFromForm(request,ListAllMailServer);
		ra.addFlashAttribute("message","The Maile Templets Have Been Save");
		return "redirect:/settings";
		
	}

	

}
