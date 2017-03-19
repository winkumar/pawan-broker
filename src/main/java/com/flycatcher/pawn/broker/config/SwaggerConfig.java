package com.flycatcher.pawn.broker.config;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <h1>Swagger Configuration</h1>
 * This class use maintain swagger configuration.
 * 
 * 
 * @author kumar
 * @version 1.0.0
 * @since 19-03-2017
 * 
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Value("${sparrow.admin.swagger.enable}")
	private boolean isSwaggerEnable;
    
	/*
	 * sparrow bean admin api
	 */
	
	@Bean
    public Docket sparrowAdminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("flycatcher-pawn-broker-api")
                .apiInfo(apiInfo())
                .enable(isSwaggerEnable)
                .select()
                .paths(sparrowAdminPaths())
                .build();
    }


@SuppressWarnings("unchecked")
private Predicate<String> sparrowAdminPaths() {
        return or(
                regex("/api/v1/.*")
              );
    }

private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Pawnbroker Application")
                .description("Pawnbroker rest Api this hole api maintain by Flycatcher's...")
                .termsOfServiceUrl("www.flycatchersolution.com")
                .contact("Flycatcher")
                .license("Flycatcher License Version 2.0")
              //  .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                
                .version("1.0")
                .build();
    }
    

}
