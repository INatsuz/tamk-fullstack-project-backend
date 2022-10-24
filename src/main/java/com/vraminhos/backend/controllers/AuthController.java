package com.vraminhos.backend.controllers;

import com.vraminhos.backend.models.Role;
import com.vraminhos.backend.models.User;
import com.vraminhos.backend.payload.requests.LoginRequest;
import com.vraminhos.backend.payload.requests.RegisterRequest;
import com.vraminhos.backend.repositories.RoleRepository;
import com.vraminhos.backend.repositories.UserRepository;
import com.vraminhos.backend.security.jwt.JwtUtils;
import com.vraminhos.backend.security.services.UserDetailsImpl;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
	AuthenticationManager authenticationManager;
	UserRepository userRepository;
	RoleRepository roleRepository;
	PasswordEncoder encoder;
	JwtUtils jwtUtils;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
	}

	@PostMapping("/signin")
	public String authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return jwt;
	}

	@PostMapping("/signup")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		System.out.println(registerRequest);
		System.out.println(registerRequest.getUsername());
		System.out.println(registerRequest.getEmail());
		System.out.println(registerRequest.getPassword());
		System.out.println(registerRequest.getRoles());

		Set<Role> roles = new HashSet<>();
		registerRequest.getRoles().forEach(eRole -> {
			roles.add(roleRepository.findByName(eRole).orElseThrow());
		});

		User user = new User(registerRequest.getUsername(), registerRequest.getEmail(), encoder.encode(registerRequest.getPassword()));
		user.setRoles(roles);

		userRepository.insert(user);

		return "Success";
	}

}
