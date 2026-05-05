/*
 * Copyright 2014-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shkim.CTR.config;

import com.shkim.CTR.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Spring Security configuration.
 *
 * @author Rob Winch
 * @author Vedran Pavic
 */
@Configuration
public class SecurityConfig {

	@Autowired
	public static JdbcTemplate jdbcTemplate;

	@Autowired
	public static UserDetailsService userDetailsService;

	@Autowired
	public static UserService userService;

	public SecurityConfig(JdbcTemplate jdbcTemplate){
		SecurityConfig.jdbcTemplate = jdbcTemplate;
	}

	// @formatter:off
	// tag::config[]
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					.requestMatchers("/login", "/signup", "/signupComplete","/image/**").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((formLogin) -> formLogin
					.loginPage("/login")
					.loginProcessingUrl("/login")
					.failureUrl("/login?error")
					.defaultSuccessUrl("/home", true)
					.permitAll()
			)
			.logout((logout) -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout")
					.permitAll()
			)
//				.sessionManagement(session -> session
//						.sessionFixation().migrateSession() // 시큐리티가 세션 ID를 직접 바꾸지 못하게 방어
//						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//				)
				.rememberMe(remember -> remember
								.alwaysRemember(true) // 🔥 핵심: 파라미터 체크 여부와 상관없이 무조건 발동!
						.key("uniqueAndSecretKey") // 이 키가 유출되면 안 됩니다 (사령관님만의 비밀번호)
						.tokenValiditySeconds(60 * 60 * 24 * 30) // 30일 동안 유지 (1440분보다 훨씬 길죠!)
						.userDetailsService(userService) // 사령관님이 만든 그 UserService가 여기서 쓰입니다!
						.rememberMeParameter("remember-me") // 로그인 폼의 체크박스 이름
				)
			.build();
	}
	// end::config[]
	// @formatter:on
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user = User.builder()
//				.username("user")
//				.password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
//				.roles("USER")
//				.build();
//		UserDetails admin = User.builder()
//				.username("admin")
//				.password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
//				.roles("USER", "ADMIN")
//				.build();
//		return new InMemoryUserDetailsManager(user, admin);
//	}

}
