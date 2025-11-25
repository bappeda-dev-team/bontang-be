package cc.kertaskerja.bontang.bidangurusan.web;

import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanAlreadyExistException;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BidangUrusanControllerAdvice {
    @ExceptionHandler(BidangUrusanAlreadyExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String bidangUrusanAlreadyExistHandler(BidangUrusanAlreadyExistException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BidangUrusanNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String bidangUrusanNotFoundHandler(BidangUrusanNotFoundException ex) {
        return ex.getMessage();
    }
}
