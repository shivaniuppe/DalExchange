package com.asdc.dalexchange.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up global CORS (Cross-Origin Resource Sharing) settings.
 */
@Configuration
@EnableWebMvc
public class GlobalCorsConfig implements WebMvcConfigurer {

    @Value("${frontend.url}")
    private String frontendUrl;

    /**
     * Configures CORS mapping to allow cross-origin requests.
     *
     * @param registry the registry to add the CORS configuration to.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendUrl)  // Allow requests from the specified frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow common HTTP methods
                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With")  // Allow common headers
                .allowCredentials(false);  // Don't allow credentials
    }
}
