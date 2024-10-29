package bg.softuni._17_jsonexercise.util;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public interface ValidationUtil {
    <E> boolean isValid(E entity);
    <E> Set<ConstraintViolation<E>> getViolations(E entity);
}
