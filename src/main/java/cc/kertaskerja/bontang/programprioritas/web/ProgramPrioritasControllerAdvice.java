package cc.kertaskerja.bontang.programprioritas.web;

import cc.kertaskerja.bontang.programprioritas.domain.exception.ProgramPrioritasAlreadyExistException;
import cc.kertaskerja.bontang.programprioritas.domain.exception.ProgramPrioritasNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ProgramPrioritasControllerAdvice {
    @ExceptionHandler(ProgramPrioritasNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String programPrioritasNotFoundHandler(ProgramPrioritasNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ProgramPrioritasAlreadyExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String programPrioritasAlreadyExistException(ProgramPrioritasAlreadyExistException ex) {
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
