package cc.kertaskerja.bontang.opd.web;

import cc.kertaskerja.bontang.opd.domain.exception.OpdAlreadyExistException;
import cc.kertaskerja.bontang.opd.domain.exception.OpdDeleteForbiddenException;
import cc.kertaskerja.bontang.opd.domain.exception.OpdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class OpdControllerAdvice {
    @ExceptionHandler(OpdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String opdNotFoundHandler(OpdNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(OpdAlreadyExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String opdAlreadyExistException(OpdAlreadyExistException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(OpdDeleteForbiddenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String opdDeleteForbiddenException(OpdDeleteForbiddenException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String errMessage = err.getDefaultMessage();
            errors.put(fieldName, errMessage);
        });

        return errors;
    }
}
