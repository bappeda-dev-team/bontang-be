package cc.kertaskerja.bontang.program.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cc.kertaskerja.bontang.program.domain.exception.ProgramAlreadyExistException;
import cc.kertaskerja.bontang.program.domain.exception.ProgramNotFoundException;

@RestControllerAdvice
public class ProgramControllerAdvice {
    @ExceptionHandler(ProgramNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String programNotFoundHandler(ProgramNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ProgramAlreadyExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String programAlreadyExistException(ProgramAlreadyExistException ex) {
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
