package cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.web;

import cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain.PelaksanaanNotFoundException;
import cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain.exception.BobotExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class PelaksanaanControllerAdvice {
    @ExceptionHandler(PelaksanaanNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String rencanaAksiNotFoundHandler(PelaksanaanNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BobotExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String bobotExceededHandler(BobotExceededException ex) {
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
