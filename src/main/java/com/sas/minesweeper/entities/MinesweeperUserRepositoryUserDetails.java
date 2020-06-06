package com.sas.minesweeper.entities;

import static com.sas.minesweeper.util.OAuthConstants.ROLE_USER;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MinesweeperUserRepositoryUserDetails implements UserDetails {

    private OAuthMinesweeper oAuthMinesweeper;

    public MinesweeperUserRepositoryUserDetails(OAuthMinesweeper oAuthMinesweeper) {
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
