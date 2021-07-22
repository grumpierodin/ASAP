package com.grumpierodin.asap;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	private static final HashSet<String> PRODUCES = new HashSet<String>(Arrays.asList("application/json", "application/xml"));
	private static final Contact CONTACT = new Contact("Steve Boxall", "", "");
	private static final ApiInfo INFO = new ApiInfoBuilder().title("Asap API").version("1.0.0").description("Asap api docs").contact(CONTACT).build();

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(INFO).produces(PRODUCES).consumes(PRODUCES);
	}
}
