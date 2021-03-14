package dev.weslley.trabalho.config;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SwaggerConfig.class, args);
	}
	
	@Bean
	public springfox.documentation.spring.web.plugins.Docket docket() {
		Docket docket = new Docket(springfox.documentation.spi.DocumentationType.SWAGGER_2);
		return docket.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		@SuppressWarnings("deprecation")
		ApiInfo apiInfo = new ApiInfo("Acme Producer (AP) REST API", "Lista de serviços da empresa AP para integração", "Versão API 1.0",
				"Termos de uso", "weslleyng@hotmail.com", "API License", "API License URL");
		return apiInfo;
	}

}
