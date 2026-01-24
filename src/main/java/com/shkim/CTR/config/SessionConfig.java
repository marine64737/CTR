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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shkim.CTR.user.UserService;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.util.List;

/**
 * @author jitendra on 3/3/16.
 */
// tag::class[]
@Configuration
@EnableRedisHttpSession
public class SessionConfig implements BeanClassLoaderAware {

	private ClassLoader loader;

	/**
	 * Note that the bean name for this bean is intentionally
	 * {@code springSessionDefaultRedisSerializer}. It must be named this way to override
	 * the default {@link RedisSerializer} used by Spring Session.
	 */
//	@Bean
//	public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//		return new JacksonJsonRedisSerializer<>(objectMapper(), Object.class);
//	}
	@Bean
	public ObjectMapper springSessionDefaultRedisSerializer() {
//		return new GenericJackson2JsonRedisSerializer();
//		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//		serializer.setCookiePath("/");
//		serializer.setUseSecureCookie(false); // HTTP 환경 필수
//		return new JacksonJsonRedisSerializer<>(objectMapper(), Object.class);
		ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new JavaTimeModule());
//		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
////		mapper.registerModules(SecurityJacksonModules.getModules(getClass().getClassLoader()));
//
//		mapper.findAndRegisterModules();
//
//		// 1. 보안 검문소(Validator) 설정: 사령관님의 패키지 경로는 '안전'하다고 선언
//		PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
//				.allowIfBaseType("com.shkim.CTR") // 이 패키지 하위 클래스들은 다 허용해!
//				.allowIfBaseType("org.springframework.security") // 시큐리티 핵심 패키지 허용 (핵심!)
////				.allowIfBaseType("org.springframework.data")
////				.allowIfBaseType(Object.class)
//				.allowIfSubType("java.util")
////				.allowIfSubType("java.lang")
////				.allowIfSubType("java.time")
////				.allowIfSubType("org.springframework.security.web.savedrequest.DefaultSavedRequest") // 이 녀석을 정밀 타격해서 허용
//				.build();
////
////		// 마법의 한 줄: 타입 정보를 함께 저장하도록 설정
//		mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
//		mapper.registerModules(SecurityJackson2Modules.getModules(this.getClass().getClassLoader()));
//		mapper.registerModule(new JavaTimeModule());
//
//
////		return new GenericJackson2JsonRedisSerializer();
//		return mapper;

		// 1. 시큐리티가 권장하는 기본 타이핑 설정 (수동 PTV보다 이게 더 호환성이 좋습니다)
		// SecurityJackson2Modules.enableDefaultTyping(mapper)를 찾거나 아래와 같이 수동 설정
		mapper.activateDefaultTyping(
				mapper.getPolymorphicTypeValidator(),
				ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.PROPERTY
		);

		// 2. 사령관님 패키지의 Mixin 등록 (이게 핵심입니다)
		// "CustomUserDetails는 이 방식으로 읽어라"라고 명시하는 표식입니다.
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

//	private tools.jackson.databind.json.JsonMapper objectMapper() {
//		return tools.jackson.databind.json.JsonMapper.builder().addModules(SecurityJacksonModules.getModules(this.loader)).build();
//	}
	private ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		List<Module> modules =
				SecurityJackson2Modules.getModules(this.getClass().getClassLoader());

		mapper.registerModules(modules);
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}

	/*
	 * @see
	 * org.springframework.beans.factory.BeanClassLoaderAware#setBeanClassLoader(java.lang
	 * .ClassLoader)
	 */
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.loader = classLoader;
	}

//	@Bean
//	public ConfigureRedisAction configureRedisAction() {
//		// 레디스 설정 시 명령어를 자동으로 실행하지 않도록 안전장치를 겁니다. (운영 환경 권장)
//		return ConfigureRedisAction.NO_OP;
//	}
//
	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//		serializer.setCookieName("SESSION"); // 쿠키 이름을 기존과 통일
		serializer.setCookiePath("/");
//		serializer.setDomainNamePattern("^.+?\\.(\\w+\\.\\w+)$"); // 도메인 패턴 허용
		// SameSite 설정을 아예 없애거나 빈 값으로 설정 (브라우저 기본값 사용 유도)
		serializer.setSameSite("Lax");
		serializer.setUseSecureCookie(false); // HTTP 환경 필수
		return serializer;
	}
}
// end::class[]
