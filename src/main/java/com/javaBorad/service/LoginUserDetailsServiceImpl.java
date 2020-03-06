package com.javaBorad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.javaBorad.LoginUserData;
import com.javaBorad.entity.User;
import com.javaBorad.mapper.UserRepository;
 
@Service
public class LoginUserDetailsServiceImpl implements UserDetailsService {
 
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
 
		if (user == null) {
			throw new UsernameNotFoundException("not found");
		}
		return new LoginUserData(user);
	}
 
}

