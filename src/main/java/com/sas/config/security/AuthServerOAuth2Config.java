package com.sas.config.security;

import static com.sas.config.security.OAuthConstants.ACCESS_TOKEN_EXPIRATION;
import static com.sas.config.security.OAuthConstants.AUTHORIZED_GRANT_TYPES_AUTHORIZATION_CODE;
import static com.sas.config.security.OAuthConstants.AUTHORIZED_GRANT_TYPES_PASSWORD;
import static com.sas.config.security.OAuthConstants.AUTHORIZED_GRANT_TYPES_REFRESH_TOKEN;
import static com.sas.config.security.OAuthConstants.CLIENT_ID;
import static com.sas.config.security.OAuthConstants.CLIENT_SECRET;
import static com.sas.config.security.OAuthConstants.REFRESH_TOKEN_EXPIRATION;
import static com.sas.config.security.OAuthConstants.RESOURCE_ID;
import static com.sas.config.security.OAuthConstants.SCOPE_READ;
import static com.sas.config.security.OAuthConstants.SCOPE_WRITE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    protected MinesweeperUserDetailsService userDetailsService;

    @Autowired protected PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients
                .inMemory()
                .withClient(CLIENT_ID)
                .secret(passwordEncoder.encode(CLIENT_SECRET))
                .accessTokenValiditySeconds(ACCESS_TOKEN_EXPIRATION)
                .refreshTokenValiditySeconds(REFRESH_TOKEN_EXPIRATION)
                .authorizedGrantTypes(
                        AUTHORIZED_GRANT_TYPES_PASSWORD,
                        AUTHORIZED_GRANT_TYPES_AUTHORIZATION_CODE,
                        AUTHORIZED_GRANT_TYPES_REFRESH_TOKEN)
                .scopes(SCOPE_READ, SCOPE_WRITE)
                .resourceIds(RESOURCE_ID);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .accessTokenConverter(accessTokenConverter())
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
    }

    @Bean
    JwtAccessTokenConverter accessTokenConverter() {
        return new JwtAccessTokenConverter();
    }
}
