package com.ordernow.backend.common.validation;

import com.ordernow.backend.common.exception.RequestValidationException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class RequestValidator {
    public static void validateRequest(Object obj)
            throws RequestValidationException {

        try {
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if(f.get(obj) == null){
                    throw new RequestValidationException("Field " + f.getName() + " is null");
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }
}
