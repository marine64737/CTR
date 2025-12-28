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

import org.jspecify.annotations.Nullable;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Spring Security configuration.
 *
 * @author Rob Winch
 * @author Vedran Pavic
 */
@Configuration(proxyBeanMethods = false)
@EnableRedisHttpSession
public class SecurityConfig {

	// @formatter:off
	// tag::config[]
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((formLogin) -> formLogin
				.permitAll()
			)
			.build();
	}
	// end::config[]
	// @formatter:on
	@Bean
	public RedisConnectionFactory connectionFactory() {
		return new RedisConnectionFactory() {
			@Override
			public boolean getConvertPipelineAndTxResults() {
				return false;
			}

			@Override
			public RedisConnection getConnection() {
				return this.getConnection();
			}

			@Override
			public RedisClusterConnection getClusterConnection() {
				return this.getClusterConnection();
			}

			@Override
			public RedisSentinelConnection getSentinelConnection() {
				return this.getSentinelConnection();
			}

			@Override
			public @Nullable DataAccessException translateExceptionIfPossible(RuntimeException ex) {
				return null;
			}
		};
	}

}
