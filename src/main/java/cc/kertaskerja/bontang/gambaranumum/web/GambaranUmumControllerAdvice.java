package cc.kertaskerja.bontang.gambaranumum.web;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import cc.kertaskerja.bontang.gambaranumum.domain.GambaranUmumNotFoundException;

@RestControllerAdvice
public class GambaranUmumControllerAdvice {
	@ExceptionHandler(GambaranUmumNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String gambaranUmumNotFoundHandler(GambaranUmumNotFoundException ex) {
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