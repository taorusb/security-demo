package com.example.securitydemo.security;

import com.example.securitydemo.model.User;
import com.example.securitydemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private final UserRepository userRepository;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.oauth2Login()
				.loginPage("/login/oauth2").permitAll()
				.defaultSuccessUrl("/", true)
				.userInfoEndpoint().oidcUserService(this.oidcUserService())
				.and()
				.authorizationEndpoint().baseUri("/login/oauth2/authorization");
		return http.build();
	}


	private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
		final OidcUserService delegate = new OidcUserService();
		return (userRequest) -> {
			OidcUser oidcUser = delegate.loadUser(userRequest);
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			// get or save custom user
			User user = mapUser(oidcUser.getAttributes());
			log.info("IN oidcUserService : authenticated user with email: " + user.getEmail());
			userRepository.getUserByEmail(user.getEmail())
					.ifPresentOrElse(u -> log.info("IN oidcUserService : found user: " + u) , () -> {
						User savedUser = userRepository.save(user);
						log.info("IN oidcUserService : saved user: " + savedUser);
					});
			oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
			return oidcUser;
		};
	}

	private User mapUser(Map<String, Object> attributes) {
		String firstName = (String) attributes.get("given_name");
		String lastName = (String) attributes.get("family_name");
		String email = (String) attributes.get("email");
		return new User(firstName, lastName, email);
	}
}
