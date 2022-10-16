package com.example.securitydemo.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private final OAuth2UserService<OidcUserRequest, OidcUser> userService;

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
				.userInfoEndpoint().oidcUserService(userService)
				.and()
				.authorizationEndpoint().baseUri("/login/oauth2/authorization");
		return http.build();
	}
}
