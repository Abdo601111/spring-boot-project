package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String fileDir= "user-photo";
		Path pathName= Paths.get(fileDir);
		String photoName = pathName.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + fileDir  + "/**")
		.addResourceLocations("file:/"+photoName+"/");
		
	}
	
	

}
