package com.sas.config.security;

import static com.sas.config.security.OAuthConstants.ROLE_USER;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ClientRepositoryUserDetails implements UserDetails, Serializable {
    private OAuthMinesweeper oAuthMinesweeper;

    public ClientRepositoryUserDetails(OAuthMinesweeper oAuthMinesweeper) {
        super();
        this.oAuthMinesweeper = oAuthMinesweeper;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority roleUser = (GrantedAuthority) () -> ROLE_USER;

        List<GrantedAuthority> roles = new LinkedList<>();
        roles.add(roleUser);

        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return oAuthMinesweeper.getUsername();
    }

    @Override
    public String getPassword() {
        return oAuthMinesweeper.getPassword();
    }
}
