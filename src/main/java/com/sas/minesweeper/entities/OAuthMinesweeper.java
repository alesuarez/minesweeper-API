package com.sas.minesweeper.entities;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class OAuthMinesweeper {
    @NotNull
    protected String username;
    @NotNull
    protected String password;
}
