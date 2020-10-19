package com.rrk.manage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger的配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        //设置请求头以及请求头默认值
//        List<Parameter> pars = new ArrayList<>();
//        ParameterBuilder ticketPar3 = new ParameterBuilder();
//        ticketPar3.name("email").description("邮箱信息")
//                .defaultValue("name@example.com")
//                .modelRef(new ModelRef("string")).parameterType("header")
//                //header中的ticket参数非必填，传空也可以
//                .required(false).build();
//
//        pars.add(ticketPar3.build());
        return new Docket(DocumentationType.SWAGGER_2).groupName("后台产品模块swagger接口文档")
                .apiInfo(apiInfo())
                .select()
                //此处为controller包路径
                .apis(RequestHandlerSelectors.basePackage("com.rrk.manage.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());


    }

    private List<? extends SecurityScheme> securitySchemes() {
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeys;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^(?!auth).*$")).build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    private ApiInfo apiInfo() {

       return new ApiInfoBuilder().title("后台产品管理模块swagger接口文档")
                .contact(new Contact("dinghao", "", "@qq.com"))
                .version("1.0").build();
    }
}
