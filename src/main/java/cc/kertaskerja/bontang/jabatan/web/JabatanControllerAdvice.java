package cc.kertaskerja.bontang.jabatan.web;

import cc.kertaskerja.bontang.jabatan.domain.exception.JabatanAlreadyExistException;
import cc.kertaskerja.bontang.jabatan.domain.exception.JabatanNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class JabatanControllerAdvice {
    @ExceptionHandler(JabatanNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String jabatanNotFoundHandler(JabatanNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(JabatanAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String jabatanAlreadyExistHandler(JabatanAlreadyExistException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String errMessage = err.getDefaultMessage();
            errors.put(fieldName, errMessage);
        });

        return errors;
    }
}
