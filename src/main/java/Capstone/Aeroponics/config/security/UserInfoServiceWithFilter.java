package Capstone.Aeroponics.config.security;

import Capstone.Aeroponics.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoServiceWithFilter implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("user with email {0} does not exist", email)));
    }
}