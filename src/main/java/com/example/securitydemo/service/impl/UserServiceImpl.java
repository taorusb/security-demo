package com.example.securitydemo.service.impl;

import com.example.securitydemo.model.User;
import com.example.securitydemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Log4j2
@Service
@AllArgsConstructor
public class UserServiceImpl implements OAuth2UserService<OidcUserRequest, OidcUser> {

	private final UserRepository userRepository;
	private final KafkaTemplate<String, User> kafkaTemplate;
	@Value("${kafka.topic.name}")
	private final String TOPIC_NAME;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		final OidcUserService delegate = new OidcUserService();
		OidcUser oidcUser = delegate.loadUser(userRequest);
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
		// get or save custom user
		User user = mapUser(oidcUser.getAttributes());
		log.info("IN oidcUserService : authenticated user with email: " + user.getEmail());
		userRepository.getUserByEmail(user.getEmail())
				.ifPresentOrElse(u -> log.info("IN oidcUserService : found user: " + u) , () -> {
					User savedUser = userRepository.save(user);
					log.info("IN oidcUserService : saved user: " + savedUser);
					log.info("IN oidcUserService : send message to kafka topic: " + TOPIC_NAME);
					kafkaTemplate.send(TOPIC_NAME, savedUser);
				});
		oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
		return oidcUser;
	}

	private User mapUser(Map<String, Object> attributes) {
		String firstName = (String) attributes.get("given_name");
		String lastName = (String) attributes.get("family_name");
		String email = (String) attributes.get("email");
		return new User(firstName, lastName, email);
	}
}
