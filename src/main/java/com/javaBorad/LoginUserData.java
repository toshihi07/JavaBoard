package com.javaBorad;

import lombok.Data;
import lombok.EqualsAndHashCode;
 
import org.springframework.security.core.authority.AuthorityUtils;

import com.javaBorad.entity.User;
 
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginUserData extends org.springframework.security.core.userdetails.User {
 
	public User getUser() {
		return user;
	}
 
	public void setUser(User user) {
		this.user = user;
	}
 
	private static final long serialVersionUID = 1L;
	private User user;
 
	public LoginUserData(User user) {
		super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
		this.user = user;
	}
	
 
}
