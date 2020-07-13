package com.sas.minesweeper.config.security;

import static com.sas.minesweeper.util.OAuthConstants.ACCESS_TOKEN_EXPIRATION;
import static com.sas.minesweeper.util.OAuthConstants.ALL_PATHS;
import static com.sas.minesweeper.util.OAuthConstants.PERMIT_ALL;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(ALL_PATHS)
                .allowedOrigins(PERMIT_ALL)
                .allowedMethods(PERMIT_ALL)
                .allowedHeaders(PERMIT_ALL)
                .allowCredentials(true)
                .maxAge(ACCESS_TOKEN_EXPIRATION);
    }
}
