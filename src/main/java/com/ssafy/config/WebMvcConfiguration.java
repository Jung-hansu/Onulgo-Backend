package com.ssafy.config;

import com.ssafy.interceptor.ConfirmInterceptor;
import org.apache.tomcat.Jar;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
public class WebMvcConfiguration implements WebMvcConfigurer {
//    TODO: 추후 파일경로(S3) 추가하기
//    private final String uploadFilePath;
//    public WebMvcConfiguration(@Value("${file.path.upload-files}") String uploadFilePath) {
//        this.uploadFilePath = uploadFilePath;
//    }

//    private final List<String> patterns = Arrays.asList("users/plans", "/plans/**", "/users/join","/check","/logout");
    private final List<String> patterns = Arrays.asList("/**");
    private final List<String> excludePatterns = Arrays.asList("/login", "/find/**","/users/join", "/check/{email}","/users/test", "/users/validate/*", "/users/find/*", "/auth/find/*", "/ai/**", "/reviews", "/reviews/detail/*", "/comments/*", "/reviews/popular");

    private final ConfirmInterceptor confirmInterceptor;

    public WebMvcConfiguration(ConfirmInterceptor confirmInterceptor){
        this.confirmInterceptor = confirmInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
//			.allowedOrigins("http://localhost:8080", "http://localhost:8081")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(),
                        HttpMethod.PATCH.name())
                .maxAge(1800); // 1800초 동안 preflight 결과를 캐시에 저장
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(confirmInterceptor).addPathPatterns(patterns).excludePathPatterns(excludePatterns);
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/upload/file/**").addResourceLocations("file:///" + uploadFilePath + "/")
//                .setCachePeriod(3600).resourceChain(true).addResolver(new PathResourceResolver());
//    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
}
