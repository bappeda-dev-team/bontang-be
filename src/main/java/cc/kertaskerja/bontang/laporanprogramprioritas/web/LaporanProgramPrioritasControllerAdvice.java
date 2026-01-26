package cc.kertaskerja.bontang.laporanprogramprioritas.web;

import cc.kertaskerja.bontang.laporanprogramprioritas.domain.exception.LaporanProgramPrioritasNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class LaporanProgramPrioritasControllerAdvice {
    @ExceptionHandler(LaporanProgramPrioritasNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String laporanProgramPrioritasNotFoundHandler(LaporanProgramPrioritasNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((org.springframework.validation.FieldError) err).getField();
            String errMessage = err.getDefaultMessage();
            errors.put(fieldName, errMessage);
        });

        return errors;
    }
}
