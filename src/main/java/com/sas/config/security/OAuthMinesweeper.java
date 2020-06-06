package com.sas.config.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthMinesweeper {
    protected String username;
    protected String password;
}
