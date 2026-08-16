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

package com.shkim.CTR.Config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shkim.CTR.Domain.User.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author jitendra on 3/3/16.
 */
// tag::class[]
@Configuration
@EnableRedisHttpSession
public class SessionConfig implements BeanClassLoaderAware {

	private ClassLoader loader;

	@Bean
	public ObjectMapper springSessionDefaultRedisSerializer() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.activateDefaultTyping(
				mapper.getPolymorphicTypeValidator(),
				ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.PROPERTY
		);
		mapper.addMixIn(UserService.CustomUserDetails.class, Object.class);

		mapper.registerModules(SecurityJackson2Modules.getModules(this.getClass().getClassLoader()));
		mapper.registerModule(new JavaTimeModule());

		return mapper;
	}

	/**
	 * Customized {@link JsonMapper} to add mix-in for class that doesn't have default
	 * constructors
	 * @return the {@link JsonMapper} to use
	 */

	private ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		List<Module> modules = SecurityJackson2Modules.getModules(this.getClass().getClassLoader());

		mapper.registerModules(modules);
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.loader = classLoader;
	}

	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setCookiePath("/");
		serializer.setSameSite("Lax");
		serializer.setUseSecureCookie(false);
		return serializer;
	}

	@Bean
	public void setSession(String key, String value, HttpServletRequest request) {
		if (!ObjectUtils.isEmpty(key) && !ObjectUtils.isEmpty(value)) {
			request.getSession().setAttribute(key, value);
		}
	}
}
