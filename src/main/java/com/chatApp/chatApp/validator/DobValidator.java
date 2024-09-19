package com.chatApp.chatApp.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;

    //Occur before isValid
    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        //Gan min = ... cua @DobConstraint cho min variable
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
//        Neu muon validate not null, co the thuc hien o day
        if(Objects.isNull(value)) {
            return true;
        }

        long years =  ChronoUnit.YEARS.between(value, LocalDate.now());

//        log.info("Years: {}", years);

        return years >= min;
    }
}
