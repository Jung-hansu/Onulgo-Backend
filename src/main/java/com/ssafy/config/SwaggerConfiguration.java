package com.ssafy.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info().title("Onulgo API 명세서")
                .description(
                        "<h3>SSAFY API Reference for Developers</h3>" +
                                "Swagger를 이용한 Onulgo API" +
                                "<br>" +
                                "<img src=\"/assets/img/ssafy_logo.png\" width=\"150\">"
                )
                .version("v1")
                .contact(new io.swagger.v3.oas.models.info.Contact().name("hissam")
                        .email("hissam@ssafy.com")
                        .url("http://edu.ssafy.com"));

        return new OpenAPI().components(new Components()).info(info);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder().group("ssafy-admin").pathsToMatch("/admin/**").build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder().group("ssafy-user").pathsToMatch("/user/**").build();
    }

    @Bean
    public GroupedOpenApi fileApi() {
        return GroupedOpenApi.builder().group("ssafy-file").pathsToMatch("/file/**").build();
    }

}