//package Capstone.Aeroponics.models.request.validations;
//
//import Capstone.Aeroponics.models.request.validations.StartDateValidation.StartDateValidator;
//import Capstone.Aeroponics.utils.DateUtils;
//import jakarta.validation.Constraint;
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import jakarta.validation.Payload;
//import org.apache.commons.lang3.StringUtils;
//
//import java.lang.annotation.Documented;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.util.Date;
//
//@Documented
//@Target({ElementType.METHOD, ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = StartDateValidator.class)
//public @interface StartDateValidation {
//
//    String message() default "Dates must not overlap.";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
//
//    class StartDateValidator implements ConstraintValidator<StartDateValidation, String> {
//
//        @Override
//        public void initialize(StartDateValidation validation) {
//        }
//
//        @Override
//        public boolean isValid(String startDate, ConstraintValidatorContext cxt) {
//            String StringUtils;
//            if (StringUtils.isBlank(startDate)) {
//                return true;
//            }
//
//            try {
//                return DateUtils.isOverlapping(new Date(), DateUtils.parseDate(startDate));
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}
