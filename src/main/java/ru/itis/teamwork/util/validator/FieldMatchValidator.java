package ru.itis.teamwork.util.validator;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import ru.itis.teamwork.util.validAnnotation.FieldMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String errorMessage;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstField();
        secondFieldName = constraintAnnotation.secondField();
        errorMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        final BeanWrapperImpl bean = new BeanWrapperImpl(value);
        final Object firstObj = bean.getPropertyValue(firstFieldName);
        final Object secondObj = bean.getPropertyValue(secondFieldName);

        boolean returnValue = true;
        if(firstObj == null) {
            return true;
        } else {
            if(secondObj != null && !firstObj.equals(secondObj)) {
                returnValue = false;
            }
        }

        if (!returnValue) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(errorMessage)
                    .addPropertyNode(secondFieldName)
                    .addConstraintViolation();
            return returnValue;
        }

        return true;
    }
}
