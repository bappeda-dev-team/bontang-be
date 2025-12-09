package cc.kertaskerja.bontang.rencanakinerja.web;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cc.kertaskerja.bontang.rencanakinerja.domain.exception.RencanaKinerjaAlreadyExist;
import cc.kertaskerja.bontang.rencanakinerja.domain.exception.RencanaKinerjaNotFoundException;

@RestControllerAdvice
public class RencanaKinerjaControllerAdvice {
    @ExceptionHandler(RencanaKinerjaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String rencanaKinerjaNotFoundHandler(RencanaKinerjaNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(RencanaKinerjaAlreadyExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String rencanaKinerjaAlreadyExistException(RencanaKinerjaAlreadyExist ex) {
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
