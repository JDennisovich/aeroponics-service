package Capstone.Aeroponics.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import Capstone.Aeroponics.exception.ResourceNotFoundException;
import Capstone.Aeroponics.models.RO.UserRO;
import Capstone.Aeroponics.models.entities.User;
import Capstone.Aeroponics.repositories.UserRepository;
import Capstone.Aeroponics.utils.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServices {

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

    public Optional<User> getById(Long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }

        return userRepository.findById(id);
    }

    public User getUserById(Long id) {
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

    public void update(Long id, UserRO userRO) {
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

    public void delete(Long id) {
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
    
}
