package org.cibseven.examples.springboot.config;

import org.cibseven.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterConfiguration {

    @Value("${spring.jersey.application-path:/engine-rest}")
    private String applicationPath;

    @Bean
    public FilterRegistrationBean<ProcessEngineAuthenticationFilter> AuthenticationFilter() {
        // Composite Authentication Filter with Jwt Token and Http Basic
        FilterRegistrationBean<ProcessEngineAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ProcessEngineAuthenticationFilter());
        
        // Ensure application path starts with / and doesn't end with /
        String basePath = applicationPath.startsWith("/") ? applicationPath : "/" + applicationPath;
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        
        // Apply to all URLs under the configured application path, except /engine-rest/identity/verify
        registrationBean.addUrlPatterns(basePath + "/*");
        registrationBean.setName("cibseven-composite-auth");
        // Enable async support
        registrationBean.setAsyncSupported(true);
        // Init parameters
        registrationBean.addInitParameter(
            "authentication-provider",
            org.cibseven.bpm.engine.rest.security.auth.impl.CompositeAuthenticationProvider.class.getName()
        );
        registrationBean.setOrder(10);// Order of execution if multiple filters
        return registrationBean;
    }
}