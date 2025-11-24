package cc.kertaskerja.bontang.subkegiatan.web;

import cc.kertaskerja.bontang.subkegiatan.domain.exception.SubKegiatanAlreadyExistException;
import cc.kertaskerja.bontang.subkegiatan.domain.exception.SubKegiatanNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SubKegiatanControllerAdvice {
    @ExceptionHandler(SubKegiatanNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String subKegiatanNotFoundHandler(SubKegiatanNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(SubKegiatanAlreadyExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String subKegiatanAlreadyExistException(SubKegiatanAlreadyExistException ex) {
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
