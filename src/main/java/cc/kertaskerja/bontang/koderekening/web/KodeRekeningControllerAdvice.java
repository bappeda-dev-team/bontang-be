package cc.kertaskerja.bontang.koderekening.web;

import cc.kertaskerja.bontang.koderekening.domain.exception.KodeRekeningAlreadyExistException;
import cc.kertaskerja.bontang.koderekening.domain.exception.KodeRekeningNotFoundException;
import cc.kertaskerja.bontang.program.domain.exception.ProgramAlreadyExistException;
import cc.kertaskerja.bontang.program.domain.exception.ProgramDeleteForbiddenException;
import cc.kertaskerja.bontang.program.domain.exception.ProgramNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class KodeRekeningControllerAdvice {
    @ExceptionHandler(KodeRekeningNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String programNotFoundHandler(KodeRekeningNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(KodeRekeningAlreadyExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String programAlreadyExistException(KodeRekeningAlreadyExistException ex) {
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
