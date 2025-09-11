package Capstone.Aeroponics.models.request.validations;

import Capstone.Aeroponics.models.request.validations.PasswordFormatValidation.PasswordFormatValidator;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordFormatValidator.class)
public @interface PasswordFormatValidation {

    String message() default "Password is invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PasswordFormatValidator implements ConstraintValidator<PasswordFormatValidation, String> {

        @Override
        public void initialize(PasswordFormatValidation validation) {
        }

        @Override
        public boolean isValid(String password, ConstraintValidatorContext cxt) {
            if (StringUtils.isBlank(password)) {
                return true;
            }

            return isValidPassword(password);
        }

        private boolean isValidPassword(String password) {
            Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(password);
            boolean hasSpecialCharacters = matcher.find();

            return password.length() >= 8 &&
                    password.chars().anyMatch(i -> Character.isLetter(i) && Character.isLowerCase(i)) &&
                    password.chars().anyMatch(i -> Character.isLetter(i) && Character.isUpperCase(i)) &&
                    password.chars().anyMatch(Character::isDigit) &&
                    hasSpecialCharacters;
        }

    }

}