package com.sas.config.security;

import com.sas.minesweeper.model.MinesweeperUser;
import com.sas.minesweeper.repository.MinesweeperUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.stereotype.Service;

@Service
public class MinesweeperUserDetailsService implements UserDetailsService {

    @Autowired
    protected MinesweeperUserRepository minesweeperUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        MinesweeperUser user = minesweeperUserRepository.findByUsername(username).orElseThrow(() -> new InvalidClientException("Invalid user"));

        OAuthMinesweeper oauthClient = OAuthMinesweeper.builder()
                .username(username)
                .password(user.getPassword())
                .build();

        return new ClientRepositoryUserDetails(oauthClient);
    }
}
