package de.helpmeifyoucan.helpmeifyoucan.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

public class Annotations {

    @Documented
    @Constraint(validatedBy = PasswordValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Password {

        String message() default "Invalid password pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Constraint(validatedBy = NameValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Name {
        String message() default "Invalid name pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }


    @Documented
    @Constraint(validatedBy = PhoneValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Phone {
        String message() default "Invalid phone pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}