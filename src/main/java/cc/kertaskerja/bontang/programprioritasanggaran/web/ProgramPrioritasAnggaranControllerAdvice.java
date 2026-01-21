package cc.kertaskerja.bontang.programprioritasanggaran.web;

import cc.kertaskerja.bontang.programprioritasanggaran.domain.exception.ProgramPrioritasAnggaranAlreadyExistException;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.exception.ProgramPrioritasAnggaranNotFoundException;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.exception.ProgramPrioritasAnggaranRencanaKinerjaAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ProgramPrioritasAnggaranControllerAdvice {
    @ExceptionHandler(ProgramPrioritasAnggaranNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String programPrioritasAnggaranNotFoundHandler(ProgramPrioritasAnggaranNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ProgramPrioritasAnggaranAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String programPrioritasAnggaranAlreadyExistExceptionHandler(ProgramPrioritasAnggaranAlreadyExistException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ProgramPrioritasAnggaranRencanaKinerjaAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String programPrioritasAnggaranRencanaKinerjaAlreadyExistExceptionHandler(ProgramPrioritasAnggaranRencanaKinerjaAlreadyExistException ex) {
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
