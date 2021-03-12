package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {
	
	public static final int PAGE_NUMPER=4;
	
	@Autowired
	public UserRepository repo;
	
	@Autowired
	public RoleRepository repoRole;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public User getUserEmail(String email) {
		return repo.getUserByEmail(email);
	}
	
	
	
	public List<User> listAll(){
		
		return (List<User>) repo.findAll();
	}
	
	
	public Page<User>ListPage(int number,String keyWord){
		Pageable pageable = PageRequest.of(number -1, PAGE_NUMPER);
		
		if(keyWord != null) {
			return repo.findAll(keyWord,pageable);
		}
		
		return repo.findAll(pageable);
		
		
	}
	
	
	
public List<Role> listAllRole(){
		
		return (List<Role>) repoRole.findAll();
	}
	


	
	public User save(User user) {
		
		boolean isUpdateUser=(user.getId() != null);
		if(isUpdateUser) {
			
			User userEx= repo.findById(user.getId()).get();
			
			if(user.getPassword().isEmpty()) {
				
				user.setPassword(userEx.getPassword());
				
			}else {
				
				encoderpassword(user);
			}
		}else {
			encoderpassword(user);
			
		}
		
		return repo.save(user);
	}

	public void encoderpassword(User user) {
		
		String passworedEncod= passwordEncoder.encode(user.getPassword());
		user.setPassword(passworedEncod);
	}
	
	
	
	
	public boolean isEmailUn(Integer id,String email) {
		User user =repo.getUserByEmail(email);
		
		if(user == null) return true;
		
		boolean isCreatingNew= (id == null);
		
		if(isCreatingNew) {
			
			if(user != null)return false;
		}else {
			if(user.getId() != id) {
				
				return false;
			}
		}
			
			
		
		
		return true;
	}


	public User get(Integer id) throws UserNotFoundException {
		// TODO Auto-generated method stub
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			throw new UserNotFoundException("Not Found Excption"+id);
		}
	}
	
	public void delete(Integer id) throws UserNotFoundException {
		Long countById= repo.countById(id);
		
		if(countById == null || countById == 0) {
			
			throw new UserNotFoundException("Not Found Excption"+id);
			
		}
		repo.deleteById(id);
		
	}
	
	public void updateEnabledStatus(Integer id,boolean enabled) {
		
		repo.updateEnabledStatus(id, enabled);
	}
	
	
	public User updateAccount(User userForm) {
		
		User userDb = repo.findById(userForm.getId()).get();
		if(userForm.getPassword().isEmpty()) {
			userDb.setPassword(userForm.getPassword());
			encoderpassword(userDb);
		}
		if(userForm.getPhotos() !=null) {
			userDb.setPhotos(userForm.getPhotos());
		}
		userDb.setFirstName(userForm.getFirstName());
		userDb.setLastName(userForm.getLastName());
		return userDb;
	}
	
	
	
}
