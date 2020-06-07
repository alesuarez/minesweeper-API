package com.sas.minesweeper.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sas.minesweeper.controller.response.AuthResponse;
import com.sas.minesweeper.entities.model.MinesweeperUser;
import com.sas.minesweeper.exception.DuplicateUsernameException;
import com.sas.minesweeper.exception.InvalidUserException;
import com.sas.minesweeper.repository.MinesweeperUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Optional;

public class OAuth2ServiceTest {

    @Mock
    private MinesweeperUserRepository minesweeperUserRepositoryMock;
    @Mock
    private TokenEndpoint tokenEndpointMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;

    @InjectMocks
    private OAuth2Service oAuth2ServiceMock;

    @BeforeEach
    void initMocks() throws HttpRequestMethodNotSupportedException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void login_withValidData_successful() throws HttpRequestMethodNotSupportedException {

        OAuth2AccessToken oAuth2AccessToken = mock(OAuth2AccessToken.class);
        ResponseEntity<OAuth2AccessToken> oAuth2AccessTokenResponseEntity = ResponseEntity.ok(oAuth2AccessToken);

        doReturn(oAuth2AccessTokenResponseEntity).when(tokenEndpointMock).postAccessToken(any(), any());
        doReturn("accessToken").when(oAuth2AccessToken).getValue();
        OAuth2RefreshToken oAuth2RefreshToken = mock(OAuth2RefreshToken.class);
        doReturn(oAuth2RefreshToken).when(oAuth2AccessToken).getRefreshToken();
        doReturn("refreshToken").when(oAuth2RefreshToken).getValue();


        final AuthResponse authResponse = oAuth2ServiceMock.login("username", "password");

        assertEquals("accessToken", authResponse.getAuthToken());
        assertEquals("refreshToken", authResponse.getRefreshToken());

        verify(tokenEndpointMock, times(1)).postAccessToken(any(), any());
    }

    @Test
    public void login_withDuplicateData_shouldThrowInvalidUserException() throws HttpRequestMethodNotSupportedException {
        doThrow(HttpRequestMethodNotSupportedException.class).when(tokenEndpointMock).postAccessToken(any(), any());

        Exception exception = assertThrows(InvalidUserException.class, () -> {
            oAuth2ServiceMock.login("username", "password");
        });

        assertEquals("The username or password are not valid", exception.getMessage());

        verify(tokenEndpointMock, times(1)).postAccessToken(any(), any());
    }

    @Test
    public void register_withValidData_successful() throws HttpRequestMethodNotSupportedException {

        Optional<MinesweeperUser> minesweeperUserOptional = Optional.empty();
        doReturn(minesweeperUserOptional).when(minesweeperUserRepositoryMock).findByUsername("username");

        OAuth2AccessToken oAuth2AccessToken = mock(OAuth2AccessToken.class);
        ResponseEntity<OAuth2AccessToken> oAuth2AccessTokenResponseEntity = ResponseEntity.ok(oAuth2AccessToken);

        doReturn(oAuth2AccessTokenResponseEntity).when(tokenEndpointMock).postAccessToken(any(), any());
        doReturn("accessToken").when(oAuth2AccessToken).getValue();
        OAuth2RefreshToken oAuth2RefreshToken = mock(OAuth2RefreshToken.class);
        doReturn(oAuth2RefreshToken).when(oAuth2AccessToken).getRefreshToken();
        doReturn("refreshToken").when(oAuth2RefreshToken).getValue();

        final AuthResponse authResponse = oAuth2ServiceMock.register("username", "password");

        assertEquals("accessToken", authResponse.getAuthToken());
        assertEquals("refreshToken", authResponse.getRefreshToken());

        verify(tokenEndpointMock, times(1)).postAccessToken(any(), any());
        verify(minesweeperUserRepositoryMock, times(1)).findByUsername("username");
        verify(minesweeperUserRepositoryMock, times(1)).save(any());
    }

    @Test
    public void register_withDuplicateData_shouldThrowDuplicateUsernameException() {

        Optional<MinesweeperUser> minesweeperUserOptional = Optional.of(MinesweeperUser.builder().username("username").build());
        doReturn(minesweeperUserOptional).when(minesweeperUserRepositoryMock).findByUsername("username");

        Exception exception = assertThrows(DuplicateUsernameException.class, () -> {
            oAuth2ServiceMock.register("username", "password");
        });

        assertEquals("The username exist, please choose an other one", exception.getMessage());
        verify(minesweeperUserRepositoryMock, times(1)).findByUsername("username");
        verify(minesweeperUserRepositoryMock, times(0)).save(any());
    }
}
