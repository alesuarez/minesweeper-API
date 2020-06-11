package com.sas.minesweeper.config;

import static com.sas.minesweeper.util.ShootType.DISCOVER;
import static com.sas.minesweeper.util.ShootType.FLAG;

import com.sas.minesweeper.service.game.PlayStrategy;
import com.sas.minesweeper.service.game.strategy.DiscoveryStrategy;
import com.sas.minesweeper.service.game.strategy.FlaggedStrategy;
import com.sas.minesweeper.util.ShootType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class BeanConfig {

    @Autowired
    private DiscoveryStrategy discoveryStrategy;

    @Autowired
    private FlaggedStrategy flaggedStrategy;

    @Bean
    public Map<ShootType, PlayStrategy> strategyMap() {
        Map<ShootType, PlayStrategy> strategyMap = new HashMap<>();
        strategyMap.put(DISCOVER, discoveryStrategy);
        strategyMap.put(FLAG, flaggedStrategy);
        return strategyMap;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://quiet-dusk-07692.herokuapp.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
                "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin",
                "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
