package Capstone.Aeroponics.services;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Capstone.Aeroponics.config.security.UserInfoDetails;
import Capstone.Aeroponics.exception.ResourceNotFoundException;
import Capstone.Aeroponics.models.entities.User;
import Capstone.Aeroponics.models.request.UserRO;
import Capstone.Aeroponics.repositories.UserRepository;
import Capstone.Aeroponics.utils.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService{

    public static final String USERS = "Users";

    public static final String USER = "User";

    private final UserRepository userRepository;



    public List<User> getall() {
        try {
            List<User> users = userRepository.findAll();
            log.info(USER + " found: " + users.size());
            return users;
        } catch (Exception e) {
            String errormessage = "Error while getting " + USERS;
            log.error(errormessage);
            throw new ServiceException(errormessage, e);
        }
    }

    public Optional<User> getById(int id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }

        return userRepository.findById(id);
    }

    public User getUserById(int id) {
        try {
            Optional<User> user = getById(id);

            if (user.isEmpty()) {
                throw new Exception("User not found.");
            }
            log.info(MessageUtils.retrieveSuccessMessage(USER));
            return user.get();
        } catch (Exception e) {
            String errorMessage = MessageUtils.retrieveErrorMessage(USER);
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void save(UserRO userRO) {
        try {
            userRepository.save(userRO.toEntity(null));
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(USER);
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void update(int id, UserRO userRO) {
        try {
            User user = getUserById(id);

            if (Objects.isNull(user)) {
                throw new ResourceNotFoundException("User not found");
            }

            userRepository.save(userRO.toEntity(user));
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(USER);
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void delete(int id) {
        try {
            User user = getUserById(id);

            if (Objects.isNull(user)) {
                throw new ResourceNotFoundException("User not found");
            }

            userRepository.delete(user);
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(USER);
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    // public void login(UserRO userRO) {
    //     try {
    //         User user = userRepository.findByEmail(userRO.email());

    //         if (Objects.isNull(user)) {
    //             throw new ResourceNotFoundException("User not found");
    //         }

    //         if (!user.getPassword().equals(userRO.password())) {
    //             throw new ResourceNotFoundException("Password does not match");
    //         }

    //         log.info("User logged in successfully");
    //     } catch (Exception e) {
    //         String errorMessage = "Error while logging in user with email: " + userRO.email();
    //         log.error(errorMessage);
    //         throw new ServiceException(errorMessage, e);
    //     }
    // }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
            .findByEmail(username)
            .map(UserInfoDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException(
                MessageFormat.format("User with username {0} does not exist", username)));
    }
    public User loadUserInfoByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("user with username {0} does not exist", email)));
    }
}

