package org.cibseven.examples.springboot.config;

import java.util.regex.Pattern;

import org.cibseven.bpm.engine.rest.impl.NamedProcessEngineRestServiceImpl;
import org.cibseven.bpm.spring.boot.starter.security.oauth2.CamundaSpringSecurityOAuth2CommonAutoConfiguration;
import org.cibseven.bpm.spring.boot.starter.security.oauth2.CamundaSpringSecurityOAuth2EngineAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.web.servlet.JerseyApplicationPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.Nullable;

@AutoConfigureOrder(CamundaSpringSecurityOAuth2CommonAutoConfiguration.CAMUNDA_OAUTH2_ORDER + 10)
@AutoConfigureAfter(CamundaSpringSecurityOAuth2EngineAutoConfiguration.class)
@Configuration
public class EngineRestSecurityFilterChainFix {

  private static final Logger logger = LoggerFactory.getLogger(EngineRestSecurityFilterChainFix.class);

  private boolean deletedFilterChain = false;

  public EngineRestSecurityFilterChainFix() {
    logger.info("EngineRestSecurityFilterChainFix configuration is being instantiated!");
  }

  protected static final Pattern[] WHITE_LISTED_URL_PATTERNS = new Pattern[] {
    Pattern.compile("^" + NamedProcessEngineRestServiceImpl.PATH + "/?"),
    Pattern.compile("^" + NamedProcessEngineRestServiceImpl.PATH + "/[^/]+/identity/verify$"),
    Pattern.compile("^/identity/verify$")
  };

  @Bean
  public BeanFactoryPostProcessor removeBean() {
    return bf -> {
        var reg = (BeanDefinitionRegistry) bf;
        try {
            reg.removeBeanDefinition("engineRestSecurityFilterChain");
            logger.info("Successfully removed original engineRestSecurityFilterChain bean");
            deletedFilterChain = true;
        } catch(NoSuchBeanDefinitionException e) {
            logger.info("Original engineRestSecurityFilterChain bean not found, nothing to remove");
        } catch (Exception e) {
            logger.warn("Failed to remove engineRestSecurityFilterChain bean: {}", e.getMessage());
        }
    };
  }

  @Bean
  @Order(1)
  public SecurityFilterChain engineRestSecurityFilterChainFixed(HttpSecurity http,
          JerseyApplicationPath applicationPath,
          @Nullable JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
    if (!deletedFilterChain) {
      return null;
    }

    logger.info("Enabling FIXED Camunda Spring Security oauth2 integration for engine-rest");
    String engineRestPath = applicationPath.getPath();

    // @formatter:off
    http.securityMatcher(request -> {
          String pathInfo = (request.getPathInfo() != null ? request.getPathInfo() : "");
          String fullPath = request.getServletPath() + pathInfo;
          if (!fullPath.startsWith(engineRestPath)) {
            return false;
          }
          String contextPath = fullPath.replaceFirst(engineRestPath + "/?", "/");
          for (Pattern pattern : WHITE_LISTED_URL_PATTERNS) {
            if (pattern.matcher(contextPath).matches()) {
              return false;
            }
          }
          return true;
        })
        .authorizeHttpRequests(c -> c
          .requestMatchers(engineRestPath + "/**").authenticated())
        .anonymous(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
             if (jwtAuthenticationConverter != null) {
               jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
             }
        }));
    // @formatter:on
    return http.build();
  }
}
