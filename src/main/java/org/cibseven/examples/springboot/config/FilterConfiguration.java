package org.cibseven.examples.springboot.config;


import org.cibseven.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<ProcessEngineAuthenticationFilter> AuthenticationFilter() {
    	// Composite Authentication Filter with Jwt Token and Http Basic
        FilterRegistrationBean<ProcessEngineAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ProcessEngineAuthenticationFilter());
        
        // Apply to all URLs under engine-rest except /engine-rest/identity/verify
        registrationBean.setUrlPatterns(Arrays.asList(
            "/process-definition/*",
            "/process-instance/*",
            "/history/*",
            "/execution/*",
            "/batch/*",
            "/decision-definition/*",
            "/deployment/*",
            "/filter/*",
            "/incident/*",
            "/job-definition/*",
            "/job/*",
            "/telemetry/*",
            "/metrics/*",
            "/authorization/*",
            "/group/*",
            "/user/*",
            "/message/*",
            "/event-subscription/*",
            "/variable-instance/*",
            "/task/*"
        ));
        registrationBean.setName("cibseven-composite-auth");
        // Enable async support
        registrationBean.setAsyncSupported(true);
        // Init parameters
        registrationBean.addInitParameter(
            "authentication-provider",
            "org.cibseven.bpm.engine.rest.security.auth.impl.CompositeAuthenticationProvider"
        );
        registrationBean.setOrder(10);// Order of execution if multiple filters
        return registrationBean;
    }
}
