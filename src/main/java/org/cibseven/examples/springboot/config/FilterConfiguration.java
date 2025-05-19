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
            "/engine-rest/process-definition/*",
            "/engine-rest/process-instance/*",
            "/engine-rest/history/*",
            "/engine-rest/execution/*",
            "/engine-rest/batch/*",
            "/engine-rest/decision-definition/*",
            "/engine-rest/deployment/*",
            "/engine-rest/filter/*",
            "/engine-rest/incident/*",
            "/engine-rest/job-definition/*",
            "/engine-rest/job/*",
            "/engine-rest/telemetry/*",
            "/engine-rest/metrics/*",
            "/engine-rest/authorization/*",
            "/engine-rest/group/*",
            "/engine-rest/user/*",
            "/engine-rest/message/*",
            "/engine-rest/event-subscription/*",
            "/engine-rest/variable-instance/*",
            "/engine-rest/task/*"
        ));
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
