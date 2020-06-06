package com.sas.config.security;

public class OAuthConstants {
    public static final String CLIENT_ID = "minesweeperClientId";
    public static final String CLIENT_SECRET = "minesweeperClientSecret";
    public static final int ACCESS_TOKEN_EXPIRATION = 20 * 60;
    public static final int REFRESH_TOKEN_EXPIRATION = 10 * ACCESS_TOKEN_EXPIRATION;
    public static final String SCOPE_READ = "read";
    public static final String SCOPE_WRITE = "write";
    public static final String RESOURCE_ID = "api";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String AUTHORIZED_GRANT_TYPES_PASSWORD = "password";
    public static final String AUTHORIZED_GRANT_TYPES_AUTHORIZATION_CODE = "authorization_code";
    public static final String AUTHORIZED_GRANT_TYPES_REFRESH_TOKEN = "refresh_token";
}
