package com.devsuperior.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {

	@Autowired
	private UserService service;

	public void validateSelfOrAdmin(Long userId) {
		User me = service.authenticated();
		if (me.hasSpecificRole("ROLE_ADMIN")){
			return;
		}
		if (!me.getId().equals(userId)){
			throw new ForbiddenException("Access denied");
		}
	}
}
