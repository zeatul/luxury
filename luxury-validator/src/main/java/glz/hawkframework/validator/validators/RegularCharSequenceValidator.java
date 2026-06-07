package glz.hawkframework.validator.validators;


import glz.hawkframework.validator.constraints.RegularCharSequence;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * This class is responsible for validating the object annotated by
 * {@link RegularCharSequence}
 *
 * @author Hawk
 */
public class RegularCharSequenceValidator implements ConstraintValidator<RegularCharSequence, CharSequence> {

    private RegularCharSequence.CommonPattern pattern;
    private String messageTemplate;

    @Override
    public void initialize(RegularCharSequence constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
        this.messageTemplate = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        boolean isValid = pattern.isMatch(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format(messageTemplate, pattern.name().toLowerCase()))
                .addConstraintViolation();
        }

        return isValid;
    }

}
