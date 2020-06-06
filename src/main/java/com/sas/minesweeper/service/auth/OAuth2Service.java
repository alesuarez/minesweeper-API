package com.sas.minesweeper.service.auth;

import static com.sas.minesweeper.util.OAuthConstants.CLIENT_ID;
import static com.sas.minesweeper.util.OAuthConstants.CLIENT_SECRET;
import static com.sas.minesweeper.util.OAuthConstants.RESOURCE_ID;
import static com.sas.minesweeper.util.OAuthConstants.ROLE_USER;
import static com.sas.minesweeper.util.OAuthConstants.SCOPE_WRITE;

import com.sas.minesweeper.controller.response.AuthResponse;
import com.sas.minesweeper.entities.model.MinesweeperUser;
import com.sas.minesweeper.exception.DuplicateUsernameException;
import com.sas.minesweeper.exception.InvalidUserException;
import com.sas.minesweeper.repository.MinesweeperUserRepository;
import com.sas.minesweeper.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OAuth2Service implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(OAuth2Service.class);

    private static final String GRANT_TYPE = "grant_type";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String CODE = "code";

    @Autowired
    private MinesweeperUserRepository minesweeperUserRepository;
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(String username, String password) {
        logger.info("User {} is trying to logger", username);

        OAuth2AccessToken accessToken = getAuthToken(username, password);

        logger.info("User {} logged successfully", username);

        return AuthResponse.builder()
                .authToken(accessToken.getValue())
                .refreshToken(accessToken.getRefreshToken().getValue())
                .build();
    }

    @Override
    public AuthResponse register(String username, String password) {
        logger.info("User {} is trying to register", username);

        Optional<MinesweeperUser> minesweeperUserOptional = minesweeperUserRepository.findByUsername(username);

        minesweeperUserOptional.ifPresent(x -> {
            logger.error("Duplicate username {}", username);
            throw new DuplicateUsernameException();
        });

        MinesweeperUser minesweeperUser = MinesweeperUser.builder()
                .username(username)
                .password(passwordEncoder.encode(password)).build();

        minesweeperUserRepository.save(minesweeperUser);

        logger.info("User {} register successfully", username);

        return login(username, password);
    }

    private OAuth2AccessToken getAuthToken(String username, String password) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put(GRANT_TYPE, PASSWORD);
        parameters.put(PASSWORD, password);
        parameters.put(USERNAME, username);

        try {
            return tokenEndpoint(parameters);
        } catch (Exception exception) {
            logger.error("Error when user {} try to get a token", username);
            throw new InvalidUserException();
        }
    }

    private OAuth2AccessToken tokenEndpoint(HashMap<String, String> parameters)
            throws HttpRequestMethodNotSupportedException {
        Set<String> responseTypes = new HashSet<>();
        responseTypes.add(CODE);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(ROLE_USER));

        HashSet<String> resourceIds = new HashSet<>();
        resourceIds.add(RESOURCE_ID);

        HashSet<String> scope = new HashSet<>();
        scope.add(SCOPE_WRITE);

        HashMap<String, Serializable> extensionProperties = new HashMap<>();
        HashMap<String, String> requestParameters = new HashMap<>();

        OAuth2Request oauth2Request =
                new OAuth2Request(
                        requestParameters,
                        CLIENT_ID,
                        authorities,
                        true,
                        scope,
                        resourceIds,
                        null,
                        responseTypes,
                        extensionProperties);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(CLIENT_ID, CLIENT_SECRET, authorities);

        OAuth2Authentication auth = new OAuth2Authentication(oauth2Request, authenticationToken);

        return tokenEndpoint.postAccessToken(auth, parameters).getBody();
    }
}
