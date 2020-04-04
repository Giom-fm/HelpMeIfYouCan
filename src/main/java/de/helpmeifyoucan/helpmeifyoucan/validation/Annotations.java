package de.helpmeifyoucan.helpmeifyoucan.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

public class Annotations {

    @Documented
    @Constraint(validatedBy = EmailValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidEmail {

        String message() default "Invalid email pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        boolean canBeNull() default false;
    }

    @Documented
    @Constraint(validatedBy = PasswordValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidPassword {

        String message() default "Invalid password pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        boolean canBeNull() default false;
    }

    @Documented
    @Constraint(validatedBy = NameValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidName {
        String message() default "Invalid name pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        boolean canBeNull() default false;
    }

    @Documented
    @Constraint(validatedBy = PhoneValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidPhone {
        String message() default "Invalid phone pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        boolean canBeNull() default false;
    }

    @Documented
    @Constraint(validatedBy = StreetValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidStreet {
        String message() default "Invalid street pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        boolean canBeNull() default false;
    }

    @Documented
    @Constraint(validatedBy = DistrictValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidDistrict {
        String message() default "Invalid district pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        boolean canBeNull() default false;
    }

    @Documented
    @Constraint(validatedBy = HousenumberValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidHouseNumber {
        String message() default "Invalid house number pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        boolean canBeNull() default false;
    }

    @Documented
    @Constraint(validatedBy = ZipCodeValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidZipCode {
        String message() default "Invalid zipcode pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        boolean canBeNull() default false;
    }

    @Documented
    @Constraint(validatedBy = CountryValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidCountry {
        String message() default "Invalid country pattern";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        boolean canBeNull() default false;
    }

}