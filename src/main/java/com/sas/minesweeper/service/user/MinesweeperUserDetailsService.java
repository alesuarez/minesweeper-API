package com.sas.minesweeper.service.user;

import com.sas.minesweeper.entities.MinesweeperUserRepositoryUserDetails;
import com.sas.minesweeper.entities.OAuthMinesweeper;
import com.sas.minesweeper.entities.model.MinesweeperUser;
import com.sas.minesweeper.exception.InvalidUserException;
import com.sas.minesweeper.repository.MinesweeperUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MinesweeperUserDetailsService implements UserDetailsService {

    @Autowired
    protected MinesweeperUserRepository minesweeperUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        MinesweeperUser user = minesweeperUserRepository.findByUsername(username).orElseThrow(InvalidUserException::new);

        OAuthMinesweeper oauthClient = OAuthMinesweeper.builder()
                .username(username)
                .password(user.getPassword())
                .build();

        return new MinesweeperUserRepositoryUserDetails(oauthClient);
    }
}
