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
}
