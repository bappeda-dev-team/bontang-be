package cc.kertaskerja.bontang.rencanaaksi.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.http.HttpStatus;
import cc.kertaskerja.bontang.rencanaaksi.domain.exception.RencanaAksiAlreadyExistException;
import cc.kertaskerja.bontang.rencanaaksi.domain.exception.RencanaAksiNotFoundException;

@RestControllerAdvice
public class RencanaAksiControllerAdvice {
    @ExceptionHandler(RencanaAksiNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String rencanaAksiNotFoundHandler(RencanaAksiNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(RencanaAksiAlreadyExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String rencanaKinerjaAlreadyExistException(RencanaAksiAlreadyExistException ex) {
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
