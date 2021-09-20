package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService());
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests().antMatchers("/users/**","/users/new").hasAuthority("Manger")
		.antMatchers("/category/new","/category/delete/**","/category/save","/category/edit/**" ).hasAuthority("Manger")
		.antMatchers("/brand/new","/brand/delete/**","/brand/save","/brand/edit/**" ).hasAuthority("Manger")

		.antMatchers("/products/new","/products/delete/**","/products/save","/products/edit/**" ,"/products/check_name").hasAuthority("Manger")
		.anyRequest()
		.authenticated().and().formLogin().loginPage("/login")
		.usernameParameter("email").permitAll().and().logout().permitAll()
		.and().rememberMe().key("ABcDefgHijklmnopqrs_1234567890")
		.tokenValiditySeconds(7 * 24 * 60 * 60);
		http.headers().frameOptions().sameOrigin();
		
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		
		web.ignoring().antMatchers("/images/**","/js/**","/webjars/**");
	}
	
	
	
	
	

}
