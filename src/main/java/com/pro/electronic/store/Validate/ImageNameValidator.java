package com.pro.electronic.store.Validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator <ImageNameValid,String>{
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        //logic for validation

        if(value.isBlank())
        {
            return  false;
        }else
        {
            return true;
        }

    }
}
